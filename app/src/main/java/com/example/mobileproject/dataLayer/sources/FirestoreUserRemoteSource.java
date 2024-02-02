package com.example.mobileproject.dataLayer.sources;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mobileproject.models.Users.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreUserRemoteSource extends GeneralUserRemoteSource{

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseAuth firebaseAuth;
    public FirestoreUserRemoteSource(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void retrieveUsers(CallbackUsers c){
        db.collection("utenti").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Users> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData();
                            Users p = new Users(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccess(results);
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }
    @Override
    public void retrieveUserByDocumentId(String tag, CallbackUsers c){
        db.collection("utenti").document(tag)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Map<String, Object> m = task.getResult().getData();
                        Users p = new Users(m, tag);
                        ArrayList<Users> results = new ArrayList<>();
                        results.add(p);
                        c.onSuccess(results);
                    }
                    else{
                        c.onFailure(task.getException());
                    }
                });
    }
    @Override
    public void createUser(Users post, CallbackUsers ci) {
        Map<String, Object> documentFields = new HashMap<>();
        documentFields.put("cognome", post.getCognome());
        documentFields.put("nome", post.getNome());
        documentFields.put("dataNascita", post.getDataNascita());
        documentFields.put("descrizione", post.getDescrizione());
        documentFields.put("followers", post.getFollowers());
        documentFields.put("following", post.getFollowing());
        documentFields.put("tags", post.getTags());
        documentFields.put("username", post.getUsername());
        createDocument("utenti", documentFields, ci);
    }
    @Override
    public void editUsername(String tag, String newUsername, CallbackUsers c) {
        // Controllo se nessuno ha quell'username
        db.collection("utenti")
                .whereEqualTo("username", newUsername)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // Allora possiamo cambiarlo
                            updateField("utenti", tag, "username", newUsername, c);
                        } else c.onFailure(new Exception("Someone already has this error"));
                    } else c.onFailure(new Exception("Firebase error"));
                });
    }

    @Override
    protected void createDocument(String collectionName, Map<String, Object> documentFields, CallbackInterface ci) {
        // Add the new document to our shared collection
        db.collection(collectionName)
                .add(documentFields)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        ci.onUploadSuccess(task.getResult().getId());
                    else
                        ci.onFailure(new Exception("Error creating document"));
                });
    }

    private void updateField(String collectionName, String documentId, String fieldToUpdate, Object newValue, CallbackInterface c) {
        // Create a map to represent the field to be updated
        Map<String, Object> updates = new HashMap<>();
        updates.put(fieldToUpdate, newValue);

        // Update the document with the new field value
        db.collection(collectionName)
                .document(documentId)
                .update(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        c.onSuccess();
                    } else {
                        c.onFailure(new Exception("Firebase errore field"));
                    }
                });
    }

    @Override
    public Users getLoggedUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            return null;
        } else {
            Map<String, Object> documentFields = new HashMap<>();
            db.collection("utenti").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        // User found, extract data

                        String email = documentSnapshot.getString("email");
                        String cognome = documentSnapshot.getString("cognome");
                        String nome = documentSnapshot.getString("nome");
                        String username = documentSnapshot.getString("username");
                        String descrizione = documentSnapshot.getString("descrizione");
                        Date dataNascita = documentSnapshot.getDate("dataNascita");
                        ArrayList<DocumentReference> followers = (ArrayList<DocumentReference>) documentSnapshot.get("followers");
                        ArrayList<DocumentReference> following = (ArrayList<DocumentReference>) documentSnapshot.get("following");
                        ArrayList<String> tags = (ArrayList<String>) documentSnapshot.get("following");

                        Map<String, Object> documentFields = new HashMap<>();
                        documentFields.put("email", email);
                        documentFields.put("cognome", cognome);
                        documentFields.put("nome", nome);
                        documentFields.put("dataNascita", dataNascita);
                        documentFields.put("descrizione", descrizione);
                        documentFields.put("followers", followers);
                        documentFields.put("following", following);
                        documentFields.put("tags", tags);
                        documentFields.put("username", username);
                    }
                }
            });

            return new Users(documentFields, firebaseUser.getUid());
        }
    }

    @Override
    public void logout(CallbackUsers c) {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    firebaseAuth.removeAuthStateListener(this);
                    c.onSuccessLogout();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
        firebaseAuth.signOut();
    }

    @Override
    public void signUp(String email, String password, CallbackUsers c) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    c.onSuccessFromAuthentication(new Users(firebaseUser.getUid(), email));
                } else {
                    c.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            } else {
                c.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });
    }

    @Override
    public void signIn(String email, String password, CallbackUsers c) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    c.onSuccessFromAuthentication(new Users(firebaseUser.getUid(), email));
                } else {
                    c.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            } else {
                c.onFailureFromAuthentication(getErrorMessage(task.getException()));
            }
        });
    }

    @Override
    public void signInWithGoogle(String idToken, CallbackUsers c) {
        if (idToken != null) {
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        c.onSuccessFromAuthentication(new Users(firebaseUser.getUid(), firebaseUser.getEmail()));
                    } else {
                        c.onFailureFromAuthentication(
                                getErrorMessage(task.getException()));
                    }
                } else {
                    c.onFailureFromAuthentication(getErrorMessage(task.getException()));
                }
            });
        }
    }


    //WTF
    private String getErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            return "passwordIsWeak";
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            return "invalidCredentials";
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            return "invalidUserError";
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            return "userCollisionError";
        }
        return "unexpected_error";
    }

    @Override
    public void passwordReset(String email, CallbackUsers c) {
        if (email != null) {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "Email sent.");
                            }
                        }
                    });
        }
    }
}