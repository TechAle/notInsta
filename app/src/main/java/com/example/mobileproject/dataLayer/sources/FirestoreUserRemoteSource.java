package com.example.mobileproject.dataLayer.sources;

import android.app.Application;

import android.net.Uri;

import android.util.Log;

import androidx.annotation.NonNull;
/*
import com.example.mobileproject.dataLayer.sources.AdvertisementSource;
import com.example.mobileproject.models.Product;
import com.example.mobileproject.models.StoreApiResponse;*/
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.service.StoreAPIService;
import com.example.mobileproject.utils.ServiceLocator;
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
import com.google.firebase.auth.UserProfileChangeRequest;
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
    //private Application app;
    StoreAPIService storeAPIService;

    public FirestoreUserRemoteSource(/*Application app*/){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        storeAPIService = ServiceLocator.getInstance().getProductsApiService();
        //this.app = app;
    }
    @Override
    public void retrieveUsers(){
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
                        c.onUploadFailure(task.getException());
                    }
                });
    }
    @Override
    public void retrieveUserByDocumentId(String tag){
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
                        c.onUploadFailure(task.getException());
                    }
                });
    }
    /*
    @Override
    public void createUser(Users post) {
        Map<String, Object> documentFields = new HashMap<>();
        documentFields.put("cognome", post.getCognome());
        documentFields.put("nome", post.getNome());
        documentFields.put("dataNascita", post.getDataNascita());
        documentFields.put("descrizione", post.getDescrizione());
        documentFields.put("followers", post.getFollowers());
        documentFields.put("following", post.getFollowing());
        documentFields.put("tags", post.getTags());
        documentFields.put("username", post.getUsername());
        createDocument("utenti", documentFields, c);
    }*/
    @Override
    public void editUsername(String newUsername) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            db.collection("utenti")
                    .whereEqualTo("username", newUsername)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                updateField("utenti", user.getUid(), "username", newUsername, c);
                            } else c.onUploadFailure(new Exception("Someone already has this error"));
                        } else c.onUploadFailure(new Exception("Firebase error"));
                    });
        } else {
            c.onUploadFailure(new Exception("Firebase error"));
        }
    }
    @Override
    public void editPassword(String newPassword) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) user.updatePassword(newPassword);
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
                        ci.onUploadFailure(new Exception("Error creating document"));
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
                        c.onUploadFailure(new Exception("Firebase errore field"));
                    }
                });
    }
    @Override
    public void createUser(Users user) {
        String uid = firebaseAuth.getCurrentUser().getUid();
        db.collection("utenti")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    c.onSuccessFromRemoteDatabase(user);
                } else {
                    Map<String, Object> documentFields = new HashMap<>();
                    documentFields.put("email", user.getEmail());
                    documentFields.put("cognome", user.getCognome());
                    documentFields.put("nome", user.getNome());
                    documentFields.put("dataNascita", user.getDataNascita());
                    documentFields.put("descrizione", user.getDescrizione());
                    documentFields.put("followers", user.getFollowers());
                    documentFields.put("following", user.getFollowing());
                    documentFields.put("tags", user.getTags());
                    documentFields.put("username", user.getUsername());
                    db.collection("utenti").document(uid)
                            .set(documentFields)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    c.onSuccessFromRemoteDatabase(user);
                                } else {
                                    c.onFailureFromRemoteDatabase("Errore creating user");
                                }
                            });
                }
            }
        }).addOnFailureListener(e -> {
                c.onFailureFromRemoteDatabase(e.getLocalizedMessage());
        });
    }
    @Override
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }
    @Override
    public void deleteAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User account deleted.");
                        }
                    }
                });
    }
    @Override
    public void changeImage(Uri uri) {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(user.getDisplayName())
                    .setPhotoUri(uri)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "User profile updated.");
                            }
                        }
                    });
        }
    }

    /*public FirebaseUser getLoggedUser(){
        return firebaseAuth.getCurrentUser();
    }*/

    @Override
    public void getLoggedUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            c.onFailureFromRemoteDatabase("aaa");
        }
        else {
            Map<String, Object> documentFields = new HashMap<>();
            //Stessa cosa
            db.collection("utenti").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        // User found, extract data
                        Map<String, Object> m = documentSnapshot.getData();
                        m.put("immagine", getUriFromId(firebaseAuth.getUid()));
                        m.put("followers", null);
                        m.put("following", null);
                        m.put("tags", null);
                        c.onSuccessFromRemoteDatabase(new Users(m, firebaseAuth.getUid()));
                        /*
                        String email = documentSnapshot.getString("email");
                        String cognome = documentSnapshot.getString("cognome");
                        String nome = documentSnapshot.getString("nome");
                        String username = documentSnapshot.getString("username");
                        String descrizione = documentSnapshot.getString("descrizione");
                        Date dataNascita = documentSnapshot.getDate("dataNascita");
                        ArrayList<String> followers = null; //placeholder
                        ArrayList<String> following = null; //placeholder
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
                        */
                    }
                }
            });
        }
    }
    @Override
    public void logout() {
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
    public void signUp(String email, String password) {
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
    public void signIn(String email, String password) {
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
    public void signInWithGoogle(String idToken) {
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
    public void passwordReset(String email) {
        if (email != null) {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Email sent.");
                        }
                    });
        }
    }
    private Uri getUriFromId(String id){
        return Uri.parse("https://firebasestorage.googleapis.com/v0/b/notinsta-941ae.appspot.com/o/PFP%2F" + id + ".png?alt=media");
    }
}