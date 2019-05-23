package com.example.booker.data.network;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.booker.utils.InjectorUtils;

public class PublicacionSyncIntentService extends IntentService {

    private static final String LOG_TAG = PublicacionSyncIntentService.class.getSimpleName();

    public PublicacionSyncIntentService() {
        super("PublicacionSyncIntentService");
        System.out.println("Constructor - PublicacionSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(LOG_TAG, "Intent service started");
        System.out.println("onHandleIntent - PublicacionSyncIntentService");

        String nombre = intent.getStringExtra("nombre");
        System.out.println("El titulo a buscar es: " + nombre);
        if (nombre==null){
            System.out.println("Reconoce que es disitnto de nulo");
            PublicacionNetworkDataSource networkDataSource =
                    InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
            networkDataSource.fetchBooks2();
        } else {
            PublicacionNetworkDataSource networkDataSource =
                    InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
            networkDataSource.fetchBooksNombre(nombre);
        }

    }
}

