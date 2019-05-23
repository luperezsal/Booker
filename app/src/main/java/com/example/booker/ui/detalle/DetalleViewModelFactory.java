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

package com.example.booker.ui.detalle;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import com.example.booker.data.PublicacionRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link PublicacionRepository} and an ID for the current {@link com.example.booker.data.database.Publicacion}
 */
public class DetalleViewModelFactory extends ViewModelProvider.NewInstanceFactory {


    private final PublicacionRepository mRepository;
    private final String id;

    public DetalleViewModelFactory(PublicacionRepository repository, String id) {
        this.mRepository = repository;
        this.id = id;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetalleActivityViewModel(mRepository, id);
    }
}