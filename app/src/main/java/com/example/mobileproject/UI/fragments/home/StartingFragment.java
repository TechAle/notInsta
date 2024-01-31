package com.example.mobileproject.UI.fragments.home;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileproject.R;
import com.example.mobileproject.ViewModels.Posts.PostsVMFactory;
import com.example.mobileproject.ViewModels.Posts.PostsViewModel;
import com.example.mobileproject.dataLayer.repositories.PostRepository;
import com.example.mobileproject.models.Post.Post;
import com.example.mobileproject.models.Post.PostResp;
import com.example.mobileproject.utils.PostAdapter;
import com.example.mobileproject.utils.Result;
import com.example.mobileproject.utils.ServiceLocator;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartingFragment extends Fragment {

    private List<Post> postSet;
    private PostsViewModel PVM;
    private int nTotalItem;
    private PostAdapter pa;
    public StartingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StartingFragment.
     */
    public static StartingFragment newInstance() {
        return new StartingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PostRepository pr = ServiceLocator.getInstance().getPostRepo();
        if(pr != null){
            PVM = new ViewModelProvider(requireActivity(), new PostsVMFactory(pr)).get(PostsViewModel.class);
        } else { //TODO: Sostituire testo con una risorsa di tipo stringa
            Snackbar.make(requireActivity().findViewById(android.R.id.content),
                    "Unexpected Error", Snackbar.LENGTH_SHORT).show();
        }
        postSet = new ArrayList<>();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        //TODO: bindings???
        return inflater.inflate(R.layout.fragment_starting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView tags = view.findViewById(R.id.tags);
        Log.d("AAAA","Presente");
        RecyclerView.LayoutManager lmt = new LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL, false);
        RecyclerView posts = view.findViewById(R.id.posts);
        StaggeredGridLayoutManager lmp = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
//        ArrayAdapter<String> aas = new ArrayAdapter<>(requireContext(), R.layout.taglist_item);
        pa = new PostAdapter(postSet, requireActivity().getApplication(), new PostAdapter.OnItemClickListener(){
            //Qua non metto una funzione anonima
            @Override
            public void onItemClicked() {
                //Volendo si può sostituire questa linea con qualcosa di altro
                Snackbar.make(view, "Item Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
        tags.setLayoutManager(lmt);
        posts.setLayoutManager(lmp);
        posts.setAdapter(pa);
        //bindings
        PVM.getPosts().observe(getViewLifecycleOwner(), result -> {
            if(result.successful()){
                PostResp resp = ((Result.PostResponseSuccess) result).getData();
                List<Post> res = resp.getPostList();
                if(!PVM.isLoading()){
                    if(PVM.isFirstLoading()){
                        //PVM.setTotalResults(PostResp.getTotalResults());
                        PVM.setFirstLoading(false);
                        postSet.addAll(res);
                        pa.notifyItemRangeInserted(0, postSet.size());
                    } else {
                        postSet.clear();
                        postSet.addAll(res);
                        pa.notifyItemRangeInserted(0, res.size());
                    }
                    //bindings
                } else {
                    PVM.setLoading(false);
                    PVM.setTotalResults(postSet.size());
                    int initial_size = postSet.size();
                    for(int i = 0; i < postSet.size(); i++){
                        if(postSet.get(i) == null){
                            postSet.remove(null);//TODO: esaminare qua
                        }
                    }
                    int start_index = (PVM.getPage()-1)*20; //TODO: volendo il 20 si può sostituire con una costante
                }
                //TODO: codice relativo
            } else {
                //TODO: codice relativo
            }
        });
        posts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (internetConnection()                //la connessione deve esserci, altrimenti nulla avrebbe senso
                && nTotalItem != PVM.getTotalResults()) //se non sono tutti i risultati
                {
                    nTotalItem = lmp.getItemCount();
                    //TODO: controllare la seguente linea
                    int pastVisiblesItems = Arrays.stream(lmp.findLastVisibleItemPositions(new int[2])).sum();

                    int visibleItemCount = lmp.getChildCount();

                    // Condition to enable the loading of other news while the user is scrolling the list
                    if (nTotalItem == visibleItemCount
                    || (nTotalItem <= (pastVisiblesItems + 2)
                        && dy > 0
                        && !PVM.isLoading())
                    && PVM.getPosts().getValue() != null
                    && PVM.getCurrentResults() != PVM.getTotalResults())
                    {
                        MutableLiveData<Result> m = PVM.getPosts();

                        if (m.getValue() != null && m.getValue().successful()) {
                            PVM.setLoading(true);
                            postSet.add(null);
                            pa.notifyItemRangeInserted(postSet.size(),postSet.size() + 1);
                            PVM.setPage(PVM.getPage() + 1); //"giro" la pagina
                            //Inizio ad andare a prendere altrii post
                            //PVM.fetchPost();//TODO: Sistemare qua
                        }
                    }
                }
            }
        });
    }

//TODO: controllare questa funzione. Se ha problemi, decommentare le linee commentate e aggiustare gli import
    private boolean internetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        // <----
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        return nc != null
            && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        // ---->
        /*
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
        try{
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");
        } catch (Exception e){
            return false;
        }*/
    }
}