package com.example.booker.ui;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.booker.R;
import com.example.booker.data.database.Publicacion;
import com.example.booker.ui.Detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private List<Publicacion> listaPublicaciones = new ArrayList<>();
    private Context mContext;
    


    public HomeAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_view, parent, false);
        return new HomeHolder(mContext, itemView);
    }

    @Override
    public void onBindViewHolder(final HomeHolder holder, int pos) {
//      Se obtiene la publicacion
        Publicacion currentPublicacion = listaPublicaciones.get(pos);

//      Se obtiene el id
        holder.id = currentPublicacion.getId();
        holder.titulo_item_view.setText(currentPublicacion.getTitle());
        System.out.println("En viewHOLDER");


//      Se obtiene la imagen
        String imagenString = currentPublicacion.getSmallThumbnail();
        Glide.with(holder.mContext).load(imagenString).into(holder.imagen_item);

        //      Se establece la estrella en función de si es favorita o no
        if (currentPublicacion.isFavorite()) {
            holder.estrella_item.setImageResource(R.drawable.star_enabled);
        } else {
            holder.estrella_item.setImageResource(R.drawable.star_disabled);
        }
//        holder.imagen.setImageResource(Integer.parseInt(imagenString));


//        holder.imagen.setImageResource();
//        holder.bind(listaPublicaciones.get(pos), pos);
    }

    @Override
    public int getItemCount() {
        return listaPublicaciones.size();
    }

    // PAara actualizar la lista local en background
    public void actualizarLista(final List<Publicacion> listaNuevaPublicaciones) {
        System.out.println("En actualizar Lista del home adapter");
        if (listaPublicaciones == null) {
            System.out.println("Lista de publicaciones es nula");
            ; // Ha cambiado el dataset
        } else { // se buscan las diferencias entre las dos lsitas
            System.out.println("Existe una lista de publicaciones, con tamaño " + listaPublicaciones.size());

            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return listaPublicaciones.size();
                }

                @Override
                public int getNewListSize() {
                    return listaNuevaPublicaciones.size();
                }

                // Se devuelven si son iguales o no
                @Override  // indices
                public boolean areItemsTheSame(int i, int i1) {
                    System.out.println("Items los mismos?");
//                    Comprobar si los item son los mismos
                    return false;
                }

                // Se comprueba el contenido
                @Override
                public boolean areContentsTheSame(int i, int i1) {

                    System.out.println("Contenido de items el mismo?");
                    return false;
                }
            });
            listaPublicaciones = listaNuevaPublicaciones;
            notifyDataSetChanged();
            System.out.println("Creada nueva lista de publicaciones");
            result.dispatchUpdatesTo(this);
        }
    }


    static class HomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context mContext;
        private ImageView imagen_item;
        private ImageView estrella_item;
        private TextView titulo_item_view;

//        private String titulo;
        private String id;

        // elementos de la vista
        HomeHolder(Context context, View itemView) {
            // Referencias a cada elemento con findViewById
            super(itemView);
            this.mContext = context;
            id = "";
//            titulo ="";
            imagen_item = (ImageView) itemView.findViewById(R.id.image_item_view);
            estrella_item = (ImageView) itemView.findViewById(R.id.favorito_nuevo);
            titulo_item_view = (TextView) itemView.findViewById(R.id.titulo_item_view);
//            titulo_item_view.setText("EN serio?");
//            System.out.println(titulo_item_view.getText());
//            System.out.println("Establecido titulo " + titulo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Se asignan los atributos del parametro a la vista
            Intent detalle = new Intent(mContext, DetailActivity.class);
            detalle.putExtra("id", id);
            System.out.println("Start activity con id: " + id);
            mContext.startActivity(detalle);

        }

    }

}
