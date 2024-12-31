package com.example.mobileproject.dataLayer.sources;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;
import static com.example.mobileproject.utils.Constants.ELEMENTS_SYNC;

import android.net.Uri;

import android.util.Log;

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
 * Classe per il recupero remoto dei post da Firebase. Se non diversamente specificato, ogni metodo
 * recupera post dal database remoto.
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

    /**
     * Recupera post non creati dall'utente. Il numero di post recuperati è definito nella costante
     * ELEMENTS_LAZY_LOADING.
     *
     * @param page il numero di pagina. Se zero, allora carica dal primo elemento, altrimenti
     *             continua dall'ultimo elemento caricato
     * */
    @Override
    public void retrievePosts(int page){
        DocumentReference refUser = db.collection("utenti")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(page == 0){
            lastPost = null;
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
                            List<DocumentReference> l = ((List<DocumentReference>)i.get("likes"));
                            List<String> likes = new ArrayList<>();
                            if(l != null) {
                                for (DocumentReference d : l) {
                                    likes.add(d.getId());
                                }
                            } else Log.e("INPUT", "Some problems on retrieving likes, PostDataRemoteSource:71");
                            Post p = new Post(i.getId(),
                                    ((DocumentReference) i.get("autore")).getId(),
                                    (String) i.get("descrizione"),
                                    ((Timestamp) i.get("data")).toDate(),
                                    (ArrayList<String>) i.get("tags"),
                                    (Boolean) i.get("promozionale"), getUriFromId(i.getId()));
                            p.setLikes(likes);
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

    /**
     * Recupera post sponsorizzati
     *
     * @implNote Non viene utilizzato alcun parametro per la pagina perchè non richiesto.
     * */
    @Override
    public void retrievePostsSponsor() {
        db.collection("post")
                .whereEqualTo("promozionale", true)
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<Post> sponsors = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot i : task.getResult()) {
                            List<DocumentReference> l = ((List<DocumentReference>)i.get("likes"));
                            List<String> likes = new ArrayList<>();
                            if (l != null){
                                for(DocumentReference d : l){
                                    likes.add(d.getId());
                                }
                            } else Log.e("INPUT", "Some problems on retrieving likes, PostDataRemoteSource:116");
                            Post p = new Post(i.getId(),
                                    ((DocumentReference) i.get("autore")).getId(),
                                    (String) i.get("descrizione"),
                                    ((Timestamp) i.get("data")).toDate(),
                                    (ArrayList<String>) i.get("tags"),
                                    (Boolean) i.get("promozionale"), getUriFromId(i.getId()));
                            p.setLikes(likes);
                            sponsors.add(p);
                        }
                        if (sponsors.size() > 0) {
                            c.onSuccessAdv(sponsors.get((int) (Math.random() * sponsors.size())));
                        } //TODO: else?
                    }
                    else c.onFailureAdv(new Exception("No sponsor"));
                });
    }

    /**
     * Recupera post di un determinato utente
     *
     * @param page numero di pagina
     * @param idUser l'ID di un utente di cui si vuole recuperarne i post
     * */
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
                            List<DocumentReference> l = ((List<DocumentReference>)i.get("likes"));
                            List<String> likes = new ArrayList<>();
                            if (l != null) {
                                for (DocumentReference dr : l) {
                                    likes.add(dr.getId());
                                }
                            } else Log.e("INPUT", "Some problems on retrieving likes, PostDataRemoteSource:160");
                            Post p = new Post(i.getId(),
                                    ((DocumentReference) i.get("autore")).getId(),
                                    (String) i.get("descrizione"),
                                    ((Timestamp) i.get("data")).toDate(),
                                    (ArrayList<String>) i.get("tags"),
                                    (Boolean) i.get("promozionale"), getUriFromId(i.getId()));
                            p.setLikes(likes);
                            results.add(p);
                            lastElementPerAuthor.replace((String) i.get("autore"), i);
                        }
                        c.onSuccessF(results);
                    } else {
                        c.onFailureF(task.getException());
                    }
                });
    }
    /**
     * Metodo asincrono per la creazione di un post sul database remoto.
     *
     * @param post Il post da creare
     *
     * @return Oggetto "Future"
     * */
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
    /**
     * Recupera post con almeno un tag tra quelli indicati
     *
     * @param page numero di pagina
     * @param tags insieme dei tag da cercare
     * */
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
                        List<DocumentReference> l = ((List<DocumentReference>)i.get("likes"));
                        List<String> likes = new ArrayList<>();
                        if (l != null) {
                            for (DocumentReference dr : l) {
                                likes.add(dr.getId());
                            }
                        } else Log.e("INPUT", "Some problems on retrieving likes, PostDataRemoteSource:160");
                        Post p = new Post(i.getId(),
                                ((DocumentReference) i.get("autore")).getId(),
                                (String) i.get("descrizione"),
                                ((Timestamp) i.get("data")).toDate(),
                                (ArrayList<String>) i.get("tags"),
                                (Boolean) i.get("promozionale"),
                                getUriFromId(i.getId()));
                        p.setLikes(likes);
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
                                List<DocumentReference> l = ((List<DocumentReference>)i.get("likes"));
                                List<String> likes = new ArrayList<>();
                                if (l != null) {
                                    for (DocumentReference dr : l) {
                                        likes.add(dr.getId());
                                    }
                                } else Log.e("INPUT", "Some problems on retrieving likes, PostDataRemoteSource:160");
                                Post p = new Post(i.getId(),
                                        ((DocumentReference) i.get("autore")).getId(),
                                        (String) i.get("descrizione"),
                                        ((Timestamp) i.get("data")).toDate(),
                                        (ArrayList<String>) i.get("tags"),
                                        (Boolean) i.get("promozionale"),
                                        getUriFromId(i.getId()));
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
    /**
     * Metodo di convenienza per ottenere la URI dell'immagine di un post
     *
     * @param id identificatore del post
     *
     * @return Uri dell'immagine del post corrispondente
     * */
    private Uri getUriFromId(String id){
        return Uri.parse("https://firebasestorage.googleapis.com/v0/b/notinsta-941ae.appspot.com/o/POSTS%2F" + id + ".png?alt=media");
    }
}