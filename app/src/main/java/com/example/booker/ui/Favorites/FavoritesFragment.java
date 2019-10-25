package com.example.booker.ui.Favorites;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booker.R;
import com.example.booker.ui.HomeAdapter;
import com.example.booker.utils.InjectorUtils;

public class FavoritesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    private FavoritesViewModel mFavoritosViewMmodel;
    private int posicion; //PAra usarlo como apuntador para volver a la misma posicion cuando entremos en detalle
    NavigationView navigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindData();
        loadItems();
        return inflater.inflate(R.layout.activity_favoritos_fragment, container, false);
    }



    //  Para que meta los valores en el adapter
    private void bindData() {
        System.out.println("En bindData");

        this.mHomeAdapter = new HomeAdapter(getContext());
        this.mRecyclerView = getActivity().findViewById(R.id.recyclerview);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setAdapter(this.mHomeAdapter);
    }

    private void loadItems() {
        // igual que el den ssun sain
//        aqui se instancia el factory y otda la pesca
        System.out.println("En Load Items FAVORITOS");
        FavoritesViewModelFactory factory = InjectorUtils.provideFavoritosViewModelFactory(getActivity().getApplicationContext());
        this.mFavoritosViewMmodel = ViewModelProviders.of(this, factory).get(FavoritesViewModel.class);
        this.mFavoritosViewMmodel.getmPublicaciones().observe(this, publicaciones -> {
            mHomeAdapter.actualizarLista(publicaciones);
            if (posicion == RecyclerView.NO_POSITION) {
                posicion = 0;
            }
            mRecyclerView.smoothScrollToPosition(posicion);
        });


    }

}
