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

package com.example.booker.utils;


import android.content.Context;

import com.example.booker.AppExecutors;
import com.example.booker.data.PublicacionRepository;
import com.example.booker.data.database.PublicacionDatabase;
import com.example.booker.data.network.PublicacionNetworkDataSource;
import com.example.booker.ui.ajustes.AjustesFactory;
import com.example.booker.ui.busqueda.BusquedaViewModelFactory;
import com.example.booker.ui.detalle.DetalleViewModelFactory;
import com.example.booker.ui.favoritos.FavoritosViewModelFactory;
import com.example.booker.ui.home.MainViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for Sunshine
 */
public class InjectorUtils {

    public static PublicacionRepository provideRepository(Context context) {
        PublicacionDatabase database = PublicacionDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        PublicacionNetworkDataSource networkDataSource =
                PublicacionNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return PublicacionRepository.getInstance(database.publicacionDAO(), networkDataSource, executors);
    }

    public static PublicacionNetworkDataSource provideNetworkDataSource(Context context) {
        // This call to provide repository is necessary if the app starts from a service - in this
        // case the repository will not exist unless it is specifically created.
        provideRepository(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return PublicacionNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }
    public static DetalleViewModelFactory provideDetalleViewModelFactory(Context context, String id) {
        PublicacionRepository repository = provideRepository(context.getApplicationContext());
        return new DetalleViewModelFactory(repository, id);
    }

    public static MainViewModelFactory provideMainActivityViewModelFactory(Context context) {
        PublicacionRepository repository = provideRepository(context.getApplicationContext());
        return new MainViewModelFactory(repository);
    }

    public static BusquedaViewModelFactory provideBusquedaActivityViewModelFactory(Context context) {
        PublicacionRepository repository = provideRepository(context.getApplicationContext());
        return new BusquedaViewModelFactory(repository);
    }

    public static FavoritosViewModelFactory provideFavoritosViewModelFactory(Context context) {
        PublicacionRepository repository = provideRepository(context.getApplicationContext());
        return new FavoritosViewModelFactory(repository);
    }

    public static AjustesFactory provideAjustesFactory (Context context){
        PublicacionRepository repository = provideRepository(context.getApplicationContext());
        return new AjustesFactory(repository, context);
    }
}