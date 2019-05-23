package com.example.booker.data.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.booker.AppExecutors;
import com.example.booker.data.database.Publicacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Utiliza la API para hacer todas las operaciones
public class PublicacionNetworkDataSource {

    private static final String LOG_TAG = PublicacionNetworkDataSource.class.getSimpleName();
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static PublicacionNetworkDataSource sInstance;
    private final Context mContext;

    // LiveData storing the latest downloaded weather forecasts
    private final MutableLiveData<Publicacion[]> publicacionesDescargadas;

    private PublicacionNetworkDataSource(Context context, AppExecutors executors) {
        mContext = context;
        publicacionesDescargadas = new MutableLiveData<Publicacion[]>();
    }


    public LiveData<Publicacion[]> getCurrentBooks() {
        return this.publicacionesDescargadas;
    }

    /**
     * Get the singleton for this class
     */
    public static PublicacionNetworkDataSource getInstance(Context context, AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PublicacionNetworkDataSource(context.getApplicationContext(), executors);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    /**
     * Starts an intent service to fetch the weather.
     */
    public void startFetchPublicacionesGeneralService() {
        System.out.println("startFetchPublicacionesGeneralService - DataNetwork");
        Intent intentToFetch = new Intent(mContext, PublicacionSyncIntentService.class);
        mContext.startService(intentToFetch);
        Log.d(LOG_TAG, "Service created");
    }

    public void startFetchPublicacionesNombreService(String name) {
        Intent intentToFetch = new Intent(mContext, PublicacionSyncIntentService.class);
        intentToFetch.putExtra("nombre", name);
        mContext.startService(intentToFetch);
    }

    public void addPublicacionesDescargadas(Publicacion[] publicacionesNuevas) {
        this.publicacionesDescargadas.postValue(publicacionesNuevas);
        System.out.println("Metiendo en principio " + publicacionesNuevas.length);

    }


    public void fetchBooks2() {
        System.out.println("En fetch books2");
        FetchBook fb = new FetchBook(this);
        fb.execute("sapiens");
    }

    public void fetchBooksNombre(String name) {
        System.out.println("En fetch books nombre");
        FetchBook fb = new FetchBook(this);
        fb.execute(name);
    }

}
