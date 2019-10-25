/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.booker.ui.Search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.booker.data.PublicacionRepository;
import com.example.booker.data.database.Publicacion;
import com.example.booker.ui.Home.MainActivity;

import java.util.List;

/**
 * {@link ViewModel} for {@link MainActivity}
 */
class SearchActivityViewModel extends ViewModel {

    // Weather forecast the user is looking at
    private LiveData<List<Publicacion>> mPublicaciones;


    // Date for the weather forecast

    private final PublicacionRepository mRepository;

    public SearchActivityViewModel(PublicacionRepository repository) {
        mRepository = repository;
        mPublicaciones = mRepository.getPublicacionesInicialesBusqueda();
    }


    public LiveData<List<Publicacion>> getPublicacionesNombre(String name) {
        if (name != null) {
            System.out.println("En Busqueda Activity View Model");
            this.mPublicaciones = mRepository.getPublicacionesNombre(name);
        }
        return this.mPublicaciones;
    }
}
