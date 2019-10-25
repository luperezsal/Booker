package com.example.booker.ui.Search;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.booker.R;
import com.example.booker.ui.HomeAdapter;
import com.example.booker.utils.InjectorUtils;

public class SearchFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SearchActivityViewModel mBusquedaViewModel;
    private HomeAdapter mBusquedaAdapter;
    NavigationView navigationView;

    //    private Publicacion publicacion;
    private int posicion; //PAra usarlo como apuntador para volver a la misma posicion cuando entremos end etalla


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindData();
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_busqueda_fragment, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isInternetConnected()) {
            Toast toastInternet = Toast.makeText(getContext(),"Imposible acceder a la red", Toast.LENGTH_SHORT);
            toastInternet.show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        System.out.println("Lo estamos intentando");
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                loadItems(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                loadItems(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }


    public boolean isInternetConnected() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).

                getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).

                        getState() == NetworkInfo.State.CONNECTED) {


            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }
        return connected;
    }


    //  Para que meta los valores en el adapter
    private void bindData() {
//        System.out.println("En bindData");

        this.mBusquedaAdapter = new HomeAdapter(getContext());
        this.mRecyclerView = getActivity().findViewById(R.id.recyclerview);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setAdapter(this.mBusquedaAdapter);

    }

    private void loadItems(String nombre) {
        System.out.println("Buscando libros: " + nombre + " EN BUSQUEDA load items");
        SearchViewModelFactory factory = InjectorUtils.provideBusquedaActivityViewModelFactory(getContext());
        this.mBusquedaViewModel = ViewModelProviders.of(this, factory).get(SearchActivityViewModel.class);
        this.mBusquedaViewModel.getPublicacionesNombre(nombre).observe(this, publicaciones -> {
            mBusquedaAdapter.actualizarLista(publicaciones);
            if (posicion == RecyclerView.NO_POSITION) {
                posicion = 0;
            }
            mRecyclerView.smoothScrollToPosition(posicion);
        });
    }

}

