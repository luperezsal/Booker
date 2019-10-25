package com.example.booker.ui.Settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.booker.data.PublicacionRepository;

public class SettingsFactory {

    private final PublicacionRepository mRepository;
    private final Context mContext;
    private SharedPreferences preferences;

    public SettingsFactory(PublicacionRepository repository, Context context) {
        mRepository = repository;
        this.mContext = context;
        preferences = context.getSharedPreferences("credencial", Context.MODE_PRIVATE);
    }

    public void deleteAllFavorites() {
        mRepository.deleteAllFavorites();
    }

    public void deleteAllDatabase() {
        mRepository.deleteAllData();
    }

    public void cambiarUsuario(String nombreUsuario) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", nombreUsuario);
        editor.commit();
    }

    public String getNombreUsuario() {
        String valor = preferences.getString("user", "");
        System.out.println("Obteniendo nombre de usuario " + valor);
        return valor;
    }

}
