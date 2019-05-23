package com.example.booker.data;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.booker.AppExecutors;
import com.example.booker.data.database.Publicacion;
import com.example.booker.data.database.PublicacionDAO;
import com.example.booker.data.network.PublicacionNetworkDataSource;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PublicacionRepository {
    private static final String LOG_TAG = PublicacionRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static PublicacionRepository sInstance;
    private final PublicacionDAO mpublicacionDAO;
    private final PublicacionNetworkDataSource publicacionNetworkDataSource;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;

    private PublicacionRepository(PublicacionDAO publicacionDAO,
                                  PublicacionNetworkDataSource publicacionNetworkDataSource,
                                  AppExecutors executors) {
        mpublicacionDAO = publicacionDAO;
        this.publicacionNetworkDataSource = publicacionNetworkDataSource;
        this.mExecutors = executors;

        System.out.println("En repositorio");
        // As long as the repository exists, observe the network LiveData.
        // If that LiveData changes, update the database.
        LiveData<Publicacion[]> networkData = this.publicacionNetworkDataSource.getCurrentBooks();
        networkData.observeForever(newBooksFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                System.out.println("Dentro de execute");
                // Insert our new weather data into Sunshine's database
                mpublicacionDAO.bulkInsert(newBooksFromNetwork);
                System.out.println("BULK INSERT: " + newBooksFromNetwork.length);
                Log.d(LOG_TAG, "New values inserted");
            });
        });
    }

    public synchronized static PublicacionRepository getInstance(
            PublicacionDAO publicacionDAO, PublicacionNetworkDataSource publicacionNetworkDataSource,
            AppExecutors executors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new PublicacionRepository(publicacionDAO, publicacionNetworkDataSource,
                        executors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    private synchronized void initializeData() {

        System.out.println("En inicializar datos");
        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) return;
        mInitialized = true;

        mExecutors.diskIO().execute(() -> {
            if (isFetchNeeded()) {
                System.out.println("Se necesita actualizar, fetch is needed");
                startFetchPublicacionService();
            }
        });
    }

    /**
     * Network related operations
     **/

    public LiveData<List<Publicacion>> getPublicaciones() {
        initializeData();
        return mpublicacionDAO.getAll();
    }

    public LiveData<List<Publicacion>> getPublicacionesInicialesBusqueda() {
        return mpublicacionDAO.getNullList();
    }

    public LiveData<List<Publicacion>> getPublicacionesFavoritas() {
        return mpublicacionDAO.getFavorites();
    }


    public LiveData<List<Publicacion>> getPublicacionesNombre(String name) {

        String nombreDAO = "%" + name + "%";
        System.out.println("A buscar en DAO " + nombreDAO);
        mExecutors.diskIO().execute(() -> {
            if (isFetchNeeded()) {
                System.out.println("Se necesita actualizar, fetch is needed");
                startFetchPublicacionesNombreService(name);
            }
        });

        return mpublicacionDAO.getFromName(nombreDAO);

    }


    public LiveData<Publicacion> getPublicacionId(String id) {
        return mpublicacionDAO.getFromId(id);
    }

    /**
     * Checks if there are enough days of future weather for the app to display all the needed data.
     *
     * @return Whether a fetch is needed
     */

    //TODO
    private boolean isFetchNeeded() {
        return true;
    }

    /**
     * Network related operation
     */

    private void startFetchPublicacionService() {
        System.out.println("StartFetchPublicacionService - Reposirotio");
        publicacionNetworkDataSource.startFetchPublicacionesGeneralService();
    }


    private void startFetchPublicacionesNombreService(String name) {
        publicacionNetworkDataSource.startFetchPublicacionesNombreService(name);
    }


    public void update (Publicacion publicacion){
        new updatePublicacionAsyncTask(this.mpublicacionDAO).execute(publicacion);
    }


    public void deleteAllFavorites () {
        new deletePublicacionesFavoritasAsyncTask(this.mpublicacionDAO).execute();

    }
    public void deleteAllData() {
        new deletePublicacionesAsyncTask(this.mpublicacionDAO).execute();
    }


    private static class updatePublicacionAsyncTask extends AsyncTask <Publicacion, Void,Void>{
        private PublicacionDAO publicacionDAO;

        private updatePublicacionAsyncTask(PublicacionDAO publicacionDAO){
            this.publicacionDAO = publicacionDAO;
        }
        @Override
        protected Void doInBackground(Publicacion... publicaciones) {
            this.publicacionDAO.updatePublicacion(publicaciones[0]);
            System.out.println("Actualizando publicacion en DAO, publicaicon favorita: " + publicaciones[0].isFavorite());
            return null;
        }

    }


    private static class deletePublicacionesAsyncTask extends AsyncTask <Void, Void,Void>{
        private PublicacionDAO publicacionDAO;

        private deletePublicacionesAsyncTask(PublicacionDAO publicacionDAO){
            this.publicacionDAO = publicacionDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            this.publicacionDAO.deleteAll();
            System.out.println("Eliminadas todas las publicaciones");
            return null;
        }

    }

    private static class deletePublicacionesFavoritasAsyncTask extends AsyncTask <Void, Void,Void>{
        private PublicacionDAO publicacionDAO;

        private deletePublicacionesFavoritasAsyncTask(PublicacionDAO publicacionDAO){
            this.publicacionDAO = publicacionDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            this.publicacionDAO.deleteAllFavorites();
            System.out.println("Eliminadas todas las publicaciones");
            return null;
        }

    }


}
