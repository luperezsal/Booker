package com.example.booker.ui.ajustes;


import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.example.booker.R;
import com.example.booker.utils.InjectorUtils;

public class AjustesFragment extends PreferenceFragment {
    Context mContext;
    AjustesFactory factory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Creando fragment");


        FragmentManager fm = getActivity().getFragmentManager();

        this.mContext = getActivity().getApplicationContext();
        factory = InjectorUtils.provideAjustesFactory(mContext);

        addPreferencesFromResource(R.xml.preferencias);
        Preference preferenceBorrarFavoritos = (Preference) findPreference("borrar_favoritos");
        Preference preferenceBorrarDatabase = (Preference) findPreference("borrar_bd");
        EditTextPreference preferenceCambiarUsuario = (EditTextPreference) findPreference("nombre_usuario");

        String valorUsuarioTextVew = factory.getNombreUsuario();
        System.out.println(valorUsuarioTextVew);
        preferenceCambiarUsuario.setText(valorUsuarioTextVew);
        System.out.println(preferenceCambiarUsuario.getText());


        System.out.println("Asignada preferencia favoritos");
        preferenceBorrarFavoritos.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                System.out.println("Pulsado borrar favoritos");
                factory.deleteAllFavorites();
                Toast toastDeleteFavoritos = Toast.makeText(mContext,"Eliminados los favoritos", Toast.LENGTH_SHORT);
                toastDeleteFavoritos.show();
                return false;
            }
        });
        preferenceBorrarDatabase.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                System.out.println("Pulsado borrar base de datos");
                factory.deleteAllDatabase();
                Toast toastDeleteBD = Toast.makeText(mContext,"Eliminada la caché", Toast.LENGTH_SHORT);
                toastDeleteBD.show();
                return false;
            }
        });

        preferenceCambiarUsuario.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                factory.cambiarUsuario(newValue.toString());
                preferenceCambiarUsuario.setText(newValue.toString());

                return false;
            }
        });
    }




}
