package com.example.booker.ui.detalle;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.booker.R;
import com.example.booker.data.database.Publicacion;
import com.example.booker.databinding.ActivityDetalleBinding;
import com.example.booker.utils.InjectorUtils;

public class DetalleActivity extends AppCompatActivity {
    private ActivityDetalleBinding mDetailBinding;
    private DetalleActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("En activity_detalle");

        String id = getIntent().getExtras().getString("id");

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detalle);

        DetalleViewModelFactory factory = InjectorUtils.provideDetalleViewModelFactory(this.getApplicationContext(), id);
        mViewModel = ViewModelProviders.of(this, factory).get(DetalleActivityViewModel.class);

        mViewModel.getPublicacion().observe(this, publicacion -> {
            // If the weather forecast details change, update the UI
            if (publicacion != null) bindPublicacionToUI(publicacion);
        });
    }

    private void bindPublicacionToUI(Publicacion publicacion) {
//        ImageView imageView = null;
        String imagen = publicacion.getSmallThumbnail();

        String titulo = publicacion.getTitle();
        String autor = publicacion.getAuthor();
        String descripcion = publicacion.getDescription();

        mDetailBinding.tituloPublicacion.setText(titulo);
        mDetailBinding.autorPublicacion.setText(autor);
        mDetailBinding.descripcionPublicacion.setText(descripcion);
        Glide.with(this).load(imagen).into(mDetailBinding.imagenPublicacion);
        if (publicacion.isFavorite()) {
            mDetailBinding.favoritoNuevo.setImageResource(R.drawable.star_enabled);
        } else {
            mDetailBinding.favoritoNuevo.setImageResource(R.drawable.star_disabled);
        }
        ImageView favoritoListener = mDetailBinding.favoritoNuevo;
        favoritoListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean favorito = publicacion.isFavorite();
                publicacion.setFavorite(!favorito);
                System.out.println("Favorito pulsado, ahora es " + publicacion.isFavorite());
                mViewModel.update(publicacion);
            }
        });

        setupActionBar(titulo);
    }

    private void setupActionBar (String titulo){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(titulo);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

}
