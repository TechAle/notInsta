package com.example.mobileproject.dataLayer.sources;

import android.app.Application;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.example.mobileproject.dataLayer.repositories.ProductsRepository;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Product;
import com.example.mobileproject.models.Users.Users;
import com.example.mobileproject.utils.Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe per il recupero remoto
 */

public class FirestoreRemoteSource extends GeneralPostRemoteSource {

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseAuth firebaseAuth;

    public FirestoreRemoteSource(Application app) {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void retrievePosts(CallbackPosts c) {
        db.collection("post").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Post> results = new ArrayList<>();
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccess(results);
                    } else {
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void retrieveUsers(CallbackUsers c) {
        db.collection("utenti").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Users> results = new ArrayList<>();
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            Map<String, Object> m = i.getData();
                            Users p = new Users(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccess(results);
                    } else {
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void retrievePostsWithTags(String[] tags, CallbackPosts c) {
        db.collection("post").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Post> results = new ArrayList<>();
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            for (String tag : tags) {
                                if (p.getTags().contains(tag)) {
                                    results.add(p);
                                    break;
                                }
                            }
                        }
                        c.onSuccess(results);
                    } else {
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void retrievePostsSponsor(CallbackPosts c, LifecycleOwner ow) {
        ArrayList<Post> sponsors = new ArrayList<>();
        if ((int) (Math.random() * 3) == 1) {
            // Chiamata API
            // Prendi il titolo, l'immagine, e suppongo anche il link
            ArrayList<Post> output = new ArrayList<>();
            Post temp = new Post();

            output.add(temp);
            c.onSuccess(output);
        } else
            db.collection("post")
                    .whereEqualTo("promozionale", true)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot i : task.getResult()) {
                                Map<String, Object> m = i.getData();
                                Post p = new Post(m, i.getId());
                                sponsors.add(p);
                            }
                            if (sponsors.size() > 0) {
                                if ((int) (Math.random() * 3) != 1) {
                                    ArrayList<Post> output = new ArrayList<>();
                                    output.add(sponsors.get((int) (Math.random() * sponsors.size())));
                                    c.onSuccess(output);
                                    return;
                                }
                            }
                        }
                        c.onFailure(new Exception("No sponsor"));
                    });


    }


    @Override
    public void retrievePostByDocumentId(String tag, CallbackPosts c) {
        db.collection("post").document(tag)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> m = task.getResult().getData();
                        Post p = new Post(m, tag);
                        ArrayList<Post> results = new ArrayList<>();
                        results.add(p);
                        c.onSuccess(results);
                    } else {
                        c.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void retrieveUserByDocumentId(String tag, CallbackUsers c) {
        db.collection("utenti").document(tag)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Object> m = task.getResult().getData();
                        Users p = new Users(m, tag);
                        ArrayList<Users> results = new ArrayList<>();
                        results.add(p);
                        c.onSuccess(results);
                    } else {
                        c.onFailure(task.getException());
                    }
                });
    }

    public void editUsername(String tag, String newUsername, CallbackUsers c) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            db.collection("utenti")
                    .whereEqualTo("username", newUsername)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                updateField("utenti", user.getUid(), "username", newUsername, c);
                            } else c.onFailure(new Exception("Someone already has this error"));
                        } else c.onFailure(new Exception("Firebase error"));
                    });
        } else {
            c.onFailure(new Exception("Firebase error"));
        }


    }

    @Override
    public void editPassword(String newPassword, CallbackUsers c) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null)
            user.updatePassword(newPassword);

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
    public void createPosts(Post post, CallbackPosts ci) {
        Map<String, Object> documentFields = new HashMap<>();
        documentFields.put("autore", post.getAutore());
        documentFields.put("likes", post.getLikes());
        documentFields.put("promozionale", post.getPromozionale());
        documentFields.put("data", post.getPubblicazione());
        documentFields.put("tags", post.getTags());
        documentFields.put("descrizione", post.getDescrizione());
        createDocument("post", documentFields, ci);
    }

    @Override
    public void createUser(Users user, CallbackUsers ci) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("utenti").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    ci.onSuccessFromRemoteDatabase(user);
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
                                    ci.onSuccessFromRemoteDatabase(user);
                                } else {
                                    ci.onFailureFromRemoteDatabase("Errore creating user");
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ci.onFailureFromRemoteDatabase(e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void createDocument(String collectionName, Map<String, Object> documentFields, CallbackInterface ci) {
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

    @Override
    public void createImage(Uri imageUri, String document, ContentResolver context, CallbackInterface ci, String id) {
        if (imageUri != null) {
            try {
                // Convert the image to PNG format
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context, imageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                // Create a unique filename for the uploaded image
                String fileName = id + ".png";

                StorageReference storageReference = FirebaseStorage.getInstance().getReference(document);

                StorageReference fileReference = storageReference.child(fileName);


                fileReference.putBytes(data).addOnSuccessListener(ris -> {
                    ci.onSuccess();
                }).addOnFailureListener(ris -> {
                    ci.onFailure(new Exception("Caricamento fallito"));
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
    public void postPost() {
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
