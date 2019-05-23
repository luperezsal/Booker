package com.example.booker.ui.home;

import android.app.FragmentTransaction;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.booker.R;
import com.example.booker.ui.HomeAdapter;
import com.example.booker.ui.ajustes.AjustesActivity;
import com.example.booker.ui.ajustes.AjustesFragment;
import com.example.booker.ui.busqueda.BusquedaFragment;
import com.example.booker.ui.favoritos.FavoritosFragment;
import com.example.booker.utils.CargarPreferencias;
import com.example.booker.utils.InjectorUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    private MainActivityViewModel mMainViewMmodel;
    private int posicion; //PAra usarlo como apuntador para volver a la misma posicion cuando entremos en detalle
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBar();
        bindData();
        loadItems();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void setupActionBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.biblioteca);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        setContentView(R.layout.activity_main);

        loadSaludo();
    }

    public void loadSaludo() {
        CargarPreferencias cargarPreferencias = new CargarPreferencias(this);
        String nombreUserSaludo = cargarPreferencias.loadWelcomeName();

        View layoutHeader = (View) navigationView.getHeaderView(0);
        if (layoutHeader != null) {
            System.out.println("LO LOGRASTEEEEEEE 1");
            TextView textViewUserSaludo = layoutHeader.findViewById(R.id.nombre_saludo);
            if (textViewUserSaludo != null) {
                textViewUserSaludo.setText(nombreUserSaludo);
                System.out.println("LO LOGRASTEEEEEEE 2");
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.favoritos_menu_desplegable) {
            System.out.println("Algo ha hecho");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch (id) {

            case R.id.biblioteca_menu_desplegable:
                toolbar.setTitle(R.string.biblioteca);
                Intent intentBiblioteca = new Intent(this, MainActivity.class);
                this.startActivity(intentBiblioteca);
                break;

            case R.id.favoritos_menu_desplegable:
                toolbar.setTitle(R.string.favoritos);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoritosFragment()).commit();
                break;

            case R.id.busqueda_menu_desplegable:
                System.out.println("Pulsada activity_busqueda");
                toolbar.setTitle(R.string.title_activity_busqueda);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BusquedaFragment()).commit();

                break;

            case R.id.ajustes_menu_desplegable:
                System.out.println("Pulsado ajustes");
                System.out.println("Pulsado ajustes");
                Intent intentAjustes = new Intent(this, AjustesActivity.class);
                this.startActivity(intentAjustes);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //  Para que meta los valores en el adapter
    private void bindData() {
        System.out.println("En bindData");

        this.mHomeAdapter = new HomeAdapter(this);
        this.mRecyclerView = findViewById(R.id.recyclerview);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setAdapter(this.mHomeAdapter);
    }

    private void loadItems() {
        // igual que el den ssun sain
//        aqui se instancia el factory y otda la pesca
        System.out.println("En Load Items");
        MainViewModelFactory factory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        this.mMainViewMmodel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        this.mMainViewMmodel.getmPublicaciones().observe(this, publicaciones -> {
            mHomeAdapter.actualizarLista(publicaciones);
            if (posicion == RecyclerView.NO_POSITION) {
                posicion = 0;
            }
            mRecyclerView.smoothScrollToPosition(posicion);
        });


    }

}
