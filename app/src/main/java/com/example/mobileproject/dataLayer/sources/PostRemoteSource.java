package com.example.mobileproject.dataLayer.sources;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import static com.example.mobileproject.utils.Constants.ELEMENTS_LAZY_LOADING;

import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.utils.DBConverter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe per il recupero remoto dei post da Firebase
 */
public class PostRemoteSource extends GeneralPostRemoteSource{
    FirebaseFirestore db;
    FirebaseStorage storage;
    private DocumentSnapshot lastPostDate;
    private final Map<String, DocumentSnapshot> lastElementPerAuthor;
    public PostRemoteSource(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        lastElementPerAuthor = new HashMap<>();
    }
    @Override
    public void retrievePosts(int page){
        /*in genere:
           - se viene chiesta la pagina 0 allora prendo i nuovi dati
           - se viene chiesta un altra pagina allora continuo con gli stessi dati
        */
        //DocumentReference refUser = db.collection("utenti").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(page == 0){
            lastPostDate = null;
            //lastPostDate = new Timestamp(253402300799L, 999999999); //Magic number: massimo valore di timestamp consentito
        }
        Query q = db.collection("post")
                //.whereNotEqualTo("autore", refUser)  //l'utente quando cerca altri post non cerca i propri //TODO: sistemare?
                .orderBy("data", Query.Direction.DESCENDING);
        if(lastPostDate != null){
            q = q.startAfter(lastPostDate);
        }
        q.limit(ELEMENTS_LAZY_LOADING)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData();
                            //m.put("immagine", getUriFromIdAndToken(i.getId(), (String) m.get("immagine")));
                            m.put("immagine", getUriFromId(i.getId()));
                            m.replace("autore", ((DocumentReference) m.get("autore")).getId());
                            m.put("data", ((Timestamp) m.get("data")).toDate());
                            Post p = new Post(m, i.getId());
                            results.add(p);
                            lastPostDate = i;
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
                            Post p = new Post(m, i.getId());
                            sponsors.add(p);
                        }
                        if (sponsors.size() > 0) {
                            c.onSuccessAdv(sponsors.get((int) (Math.random() * sponsors.size())));//TODO: Controllare
                        }
                    }
                    else c.onFailureAdv(new Exception("No sponsor"));
                });
    }
    @Override
    public void retrievePostsByAuthor(@NonNull String idUser, int page){//TODO: ma invece di mettere l'id, non si potrebbe mettere lo username?
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
    public void createPost(Post post) {
        Map<String, Object> documentFields = new HashMap<>();
        documentFields.put("autore", db.collection("utenti").document(FirebaseAuth.getInstance().getCurrentUser().getUid()));//TODO: controllare qua
        //documentFields.put("autore", post.getAutore());
        documentFields.put("likes", post.getLikes());
        documentFields.put("promozionale", post.isPromozionale());
        documentFields.put("tags", post.getTags());
        documentFields.put("descrizione", post.getDescrizione());
        documentFields.put("data", new Timestamp(DBConverter.dateFromTimestamp(System.currentTimeMillis())));
        db.collection("post")
                .add(documentFields)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        //c.onUploadSuccess(task.getResult().getId());
                        c.onUploadPostSuccess(task.getResult().getId());
                    else
                        c.onUploadPostFailure();
                });
    }

    @Override
    public void createPosts(List<Post> pl){//TODO: controllare qua
        for (Post p : pl){
            createPost(p);
        }
    }
    @Override
    public void createImage(/*Uri imageUri, String document, ContentResolver context, @Deprecated CallbackInterface ci,*/ String id, Bitmap bmp) {
        /*if (imageUri != null) {
            try {
                // Convert the image to PNG format
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context, imageUri);*/
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                // Create a unique filename for the uploaded image
                String fileName = id + ".png";
                storage.getReference("POSTS")
                        .child(fileName)
                        .putBytes(data)
                        .addOnSuccessListener(r -> c.onUploadImageSuccess())
                        .addOnFailureListener(r -> c.onUploadImageFailure());
            /*} catch (IOException e) {
                throw new RuntimeException(e);
            }
        }*/
    }
    @Override
    public void retrievePostsWithTagsLL(String[] tags, int page) {//mero segnaposto, è come quello normale
        db.collection("post")
                //.whereNotEqualTo("autore", <segnaposto per l'autore>)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            if(p.getTags() == null) continue;
                            for (String tag: tags) {
                                if (p.getTags().contains(tag)) {
                                    results.add(p);
                                    break;
                                }
                            }
                        }
                        c.onSuccessG(results);
                    }
                    else{
                        c.onFailureG(task.getException());
                    }
                });
    }

    //TODO: avevo in mente di utilizzare retrieveUserPosts(), ma mi servono callback diverse.
    // Se funziona tutto pensavo di chiamare quella (e di riscrivere in parte il codice) [CCL]
    @Override
    public void retrieveUserPostsForSync(int page){//TODO: fixare
        /*db.collection("posts")
                .whereEqualTo("autore", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .startAfter(page*ELEMENTS_LAZY_LOADING*5)
                .limit(ELEMENTS_LAZY_LOADING*5)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccessSyncRemote(results);
                    }
                    else{
                        c.onFailureSync();
                    }
                });*/
    }

    //Questa invece ha una query diversa, meglio tenerla separata (riferito al to do precedente) [CCL]
    @Override
    public void retrieveUserPostsForSync(int page, long lastUpdate){
        /*db.collection("posts")
                .whereEqualTo("autore", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereGreaterThan("pubblicazione", lastUpdate)
                .startAfter(page*ELEMENTS_LAZY_LOADING*5)
                .limit(ELEMENTS_LAZY_LOADING*5)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        List<Post> results = new ArrayList<>();
                        for(QueryDocumentSnapshot i : task.getResult()){
                            Map<String, Object> m = i.getData();
                            Post p = new Post(m, i.getId());
                            results.add(p);
                        }
                        c.onSuccessSyncRemote(results);
                    }
                    else{
                        c.onFailureSync();
                    }
                });*/
    }
    private Uri getUriFromId(String id){
        return Uri.parse("https://firebasestorage.googleapis.com/v0/b/notinsta-941ae.appspot.com/o/POSTS%2F" + id + ".png?alt=media");
    }
    /*private Uri getUriFromIdAndToken(String id, String token){//In pratica indovino l'url di download con un piccolo aiuto (rimane sempre lo stesso, tranne il valore del token e l'ID dell'autore)
        return Uri.parse("https://firebasestorage.googleapis.com/v0/b/notinsta-941ae.appspot.com/o/POSTS%2F" + id + ".png?alt=media&token=" + token);
    }*/
}