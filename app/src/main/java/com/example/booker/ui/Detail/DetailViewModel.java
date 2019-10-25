package com.example.booker.ui.Detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.booker.data.PublicacionRepository;
import com.example.booker.data.database.Publicacion;

/**
 * {@link ViewModel} for {@link DetailActivity}
 */
public class DetailViewModel extends ViewModel {

    // Weather forecast the user is looking at
    private final LiveData<Publicacion> mPublicacion;
    private final String id;
    private final PublicacionRepository mRepository;

    public DetailViewModel(PublicacionRepository repository, String id) {
        mRepository = repository;
        this.id = id;
        mPublicacion = mRepository.getPublicacionId(id);
    }

    public LiveData<Publicacion> getPublicacion() {
        return mPublicacion;
    }

    public void update (Publicacion publicacion){
        mRepository.update(publicacion);
    }
}
