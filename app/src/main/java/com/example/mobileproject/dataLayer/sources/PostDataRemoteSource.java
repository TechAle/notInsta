package com.example.mobileproject.dataLayer.sources;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;
import static com.example.mobileproject.utils.Constants.ELEMENTS_SYNC;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.DBConverter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Classe per il recupero remoto dei post da Firebase
 */
public final class PostDataRemoteSource extends GeneralPostDataRemoteSource {
    FirebaseFirestore db;
    private DocumentSnapshot lastPost;
    private DocumentSnapshot lastPostSync;
    private DocumentSnapshot lastPostTag;
    private final Map<String, DocumentSnapshot> lastElementPerAuthor;
    public PostDataRemoteSource(){
        db = FirebaseFirestore.getInstance();
        lastElementPerAuthor = new HashMap<>();
    }
    @Override
    public void retrievePosts(int page){
        /*in genere:
           - se viene chiesta la pagina 0 allora prendo i nuovi dati
           - se viene chiesta un altra pagina allora continuo con gli stessi dati
        */
        DocumentReference refUser = db.collection("utenti").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(page == 0){
            lastPost = null;
            //lastPostDate = new Timestamp(253402300799L, 999999999); //Magic number: massimo valore di timestamp consentito
        }
        Query q = db.collection("post")
                .whereNotEqualTo("autore", refUser)  //l'utente quando cerca altri post non cerca i propri
                .orderBy("data", Query.Direction.DESCENDING);
        if(lastPost != null){
            q = q.startAfter(lastPost);
        }
        q.limit(ELEMENTS_LAZY_LOADING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            /*DocumentReference ref = i.getDocumentReference("autore");
                            Boolean b = i.getBoolean("promozionale");
                            Timestamp t = i.getTimestamp("data");
                            Object o = i.get("tags");
                            Post p2 = new Post(i.getId(),
                                    ref == null ? null : ref.getId(),
                                    i.getString("descrizione"),
                                    t == null ? null : t.toDate(),
                                    o instanceof ArrayList<?> ? (ArrayList<String>) o : null,
                                    //new ArrayList<>(),
                                    i.contains("tags") ? i.getBoolean("tags") : null,
                                    getUriFromId(i.getId()));*/
                            Map<String, Object> m = i.getData();
                            m.put("immagine", getUriFromId(i.getId()));
                            m.replace("autore", ((DocumentReference) m.get("autore")).getId());
                            m.put("data", ((Timestamp) m.get("data")).toDate());
                            List<DocumentReference> l = ((List<DocumentReference>)m.get("likes"));
                            List<String> likes = new ArrayList<>();
                            for(DocumentReference d : l){
                                likes.add(d.getId());
                            }
                            m.put("likes", likes);
                            Post p = new Post(m, i.getId());
                            results.add(p);
                            lastPost = i;
                        }
                        c.onSuccessG(results);
                    }
                    else{
                        c.onFailureG(task.getException());
                    }
                });
    }
    @Override
    public void retrievePostsSponsor() {
        db.collection("post")
                .whereEqualTo("promozionale", true)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<Post> sponsors = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            Map<String, Object> m = i.getData();
                            m.replace("autore", ((DocumentReference) m.get("autore")).getId());
                            m.put("immagine", getUriFromId(i.getId()));
                            m.replace("data", ((Timestamp) m.get("data")).toDate());
                            List<DocumentReference> l = ((List<DocumentReference>)m.get("likes"));
                            List<String> likes = new ArrayList<>();
                            for(DocumentReference d : l){
                                likes.add(d.getId());
                            }
                            m.put("likes", likes);
                            Post p = new Post(m, i.getId());
                            sponsors.add(p);
                        }
                        if (sponsors.size() > 0) {
                            c.onSuccessAdv(sponsors.get((int) (Math.random() * sponsors.size())));
                        }
                    }
                    else c.onFailureAdv(new Exception("No sponsor"));
                });
    }
    @Override
    public void retrievePostsByAuthor(@NonNull String idUser, int page){
        DocumentReference refUser = db.collection("utenti").document(idUser);
        if(page == 0){
            lastElementPerAuthor.put(idUser, null);
        }
        Query q = db.collection("post")
                .whereEqualTo("autore", refUser)
                .orderBy("data");
        DocumentSnapshot d = lastElementPerAuthor.get(idUser);
        if (d != null){
            q = q.startAfter(d);
        }
        q.limit(ELEMENTS_LAZY_LOADING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            Map<String, Object> m = i.getData();
                            m.put("immagine", getUriFromId(i.getId()));
                            m.replace("autore", ((DocumentReference) m.get("autore")).getId());
                            m.replace("data", ((Timestamp) m.get("data")).toDate());
                            List<DocumentReference> l = ((List<DocumentReference>)m.get("likes"));
                            List<String> likes = new ArrayList<>();
                            for(DocumentReference dr : l){
                                likes.add(dr.getId());
                            }
                            m.put("likes", likes);
                            Post p = new Post(m, i.getId());
                            results.add(p);
                            lastElementPerAuthor.replace((String) m.get("autore"), i);
                        }
                        c.onSuccessO(results);
                    } else {
                        c.onFailureO(task.getException());
                    }
                });
    }
    @Override
    public Future<String> createPost(Post post) {
        Map<String, Object> documentFields = new HashMap<>();
        documentFields.put("autore", db.collection("utenti").document(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        documentFields.put("likes", post.getLikes());
        documentFields.put("promozionale", post.isPromozionale());
        documentFields.put("tags", post.getTags());
        documentFields.put("descrizione", post.getDescrizione());
        documentFields.put("data", new Timestamp(DBConverter.dateFromTimestamp(System.currentTimeMillis())));
        return CallbackToFutureAdapter.getFuture(completer -> {
            db.collection("post")
                    .add(documentFields)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                            completer.set(task.getResult().getId());
                        else
                            completer.set(null);
                    });
            return "Post creation";
        });
    }
    @Override
    public void retrievePostsWithTagsLL(String[] tags, int page){
        if(page == 0){
            lastPostTag = null;
        }
        Query q = db.collection("post")
            .whereArrayContainsAny("tags", Arrays.asList(tags))
            .orderBy("data");
        if(lastPostTag != null){
            q = q.startAfter(lastPostTag);
        }
        q.limit(ELEMENTS_SYNC)
            .get()
            .addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    List<Post> results = new ArrayList<>();
                    for(QueryDocumentSnapshot i : task.getResult()){
                        Map<String, Object> m = i.getData();
                        m.put("immagine", getUriFromId(i.getId()));
                        m.replace("autore", ((DocumentReference) m.get("autore")).getId());
                        m.replace("data", ((Timestamp) m.get("data")).toDate());
                        Post p = new Post(m, i.getId());
                        results.add(p);
                        lastPostTag = i;
                    }
                    c.onSuccessF(results);
                } else {
                    c.onFailureF(task.getException());
                }
            });
    }
    
    @Override
    public Future<List<Post>> retrieveUserPostsForSync(int page, long lastUpdate){
        if(page == 0){
            lastPostSync = null;
        }
        DocumentReference refUser = db.collection("utenti").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Query q = db.collection("post")
                .whereEqualTo("autore", refUser)
                .whereGreaterThan("data", new Timestamp(new Date(lastUpdate)))
                .orderBy("data", Query.Direction.ASCENDING);
        if(lastPostSync != null){
            q = q.startAfter(lastPostSync);
        }
        Query finalQ = q;
        return CallbackToFutureAdapter.getFuture(completer -> {
            finalQ.limit(ELEMENTS_LAZY_LOADING*5)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            List<Post> results = new ArrayList<>();
                            for(QueryDocumentSnapshot i : task.getResult()){
                                Map<String, Object> m = i.getData();
                                m.put("immagine", getUriFromId(i.getId()));
                                m.replace("autore", ((DocumentReference) m.get("autore")).getId());
                                m.replace("data", ((Timestamp) m.get("data")).toDate());
                                Post p = new Post(m, i.getId());
                                results.add(p);
                                lastPostSync = i;
                            }
                            completer.set(results);
                        } else {
                            completer.set(null);
                        }
                    });
            return "Sync posts - Remote";
        });
    }
    private Uri getUriFromId(String id){
        return Uri.parse("https://firebasestorage.googleapis.com/v0/b/notinsta-941ae.appspot.com/o/POSTS%2F" + id + ".png?alt=media");
    }
}