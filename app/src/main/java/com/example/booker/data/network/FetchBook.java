package com.example.booker.data.network;

/*
 * Copyright (C) 2016 Google Inc.
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

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.booker.R;
import com.example.booker.data.database.Publicacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * AsyncTask implementation that opens a network connection and
 * query's the Book Service API.
 */
public class FetchBook extends AsyncTask<String, Void, JSONObject> {

    PublicacionNetworkDataSource publicacionNetworkDataSource;



    public FetchBook(PublicacionNetworkDataSource publicacionNetworkDataSourceParametro) {
        this.publicacionNetworkDataSource = publicacionNetworkDataSourceParametro;
        System.out.println("Creando fetch");
    }


    @Override
    protected JSONObject doInBackground(String... query) {

        System.out.println("EN background");
        // Stop if cancelled
        if (isCancelled()) {
            return null;
        }
        String queryString = query[0];

        final String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";

        final String QUERY_PARAM = "q"; // Parameter for the search string.
        final String MAX_RESULTS = "maxResults"; // Parameter that limits search results.
        final String PRINT_TYPE = "printType"; // Parameter to filter by print type.

        // Build up your query URI, limiting results to 10 items and printed books.
        Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, queryString)
                .appendQueryParameter(MAX_RESULTS, "30")
                .appendQueryParameter(PRINT_TYPE, "books")
                .build();

        try {
            HttpURLConnection connection = null;
            // Build Connection.
            try {
                URL url = new URL(builtURI.toString());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(5000); // 5 seconds
                connection.setConnectTimeout(5000); // 5 seconds
            } catch (MalformedURLException e) {
                // Impossible: The only two URLs used in the app are taken from string resources.
                e.printStackTrace();
            } catch (ProtocolException e) {
                // Impossible: "GET" is a perfectly valid request method.
                e.printStackTrace();
            }
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
                connection.disconnect();
                return null;
            }

            // Read data from response.
            StringBuilder builder = new StringBuilder();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = responseReader.readLine();
            while (line != null) {
                builder.append(line);
                line = responseReader.readLine();
            }
            String responseString = builder.toString();
            Log.d(getClass().getName(), "Response String: " + responseString);
            JSONObject responseJson = new JSONObject(responseString);
            // Close connection and return response code.
            connection.disconnect();
//            System.out.println(responseJson);

            return responseJson;
        } catch (SocketTimeoutException e) {
            Log.w(getClass().getName(), "Connection timed out. Returning null");
            return null;
        } catch (IOException e) {
            Log.d(getClass().getName(), "IOException when connecting to Google Books API.");
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            Log.d(getClass().getName(), "JSONException when connecting to Google Books API.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject responseJson) {
        if (isCancelled()) {
            // Request was cancelled due to no network connection.
            System.out.println("            // Request was cancelled due to no network connection.\n");
        } else if (responseJson == null) {
            System.out.println("JSONN nulo");
        } else {
            try {
                // Convert the response into a JSON object.
                // Get the JSONArray of book items.
                JSONArray itemsArray = responseJson.getJSONArray("items");
                if (itemsArray != null) {
                    Publicacion[] publicacionesObtenidas = new Publicacion[itemsArray.length()];
                    for (int i = 0; i < itemsArray.length(); i++) {

                        JSONObject book = itemsArray.getJSONObject(i);
                        JSONObject info = book.getJSONObject("volumeInfo");

                        String id = "no disponible";
                        JSONObject imageLinks = null;
                        String imageLink = "no disponible";
                        String title = "no disponible";
                        JSONArray authors = null;
                        String author = "no disponible";
                        String editorial = "no disponible";
                        String description = "no disponible";


                        if (!book.isNull("id")) {
                            id = book.getString("id");
                        }

                        if (!info.isNull("imageLinks")) {
                            System.out.println("No es nula la imagen");
                            imageLinks = info.getJSONObject("imageLinks");
                            if (!imageLinks.isNull("smallThumbnail")) {
                                imageLink = imageLinks.getString("smallThumbnail");
                                System.out.println(imageLink);
                            }
                        }

                        if (!info.isNull("title")) {
                            title = info.getString("title");
                        }

                        if (!info.isNull("authors")) {
                            authors = info.getJSONArray("authors");
                            System.out.println(authors.get(0).toString());
                            author = authors.get(0).toString();
                        }
                        if (!info.isNull("publisher")) {
                            editorial = info.getString("publisher");
                        }
                        if (!info.isNull("description")) {
                            description = info.getString("description");
                        }


                        Publicacion bookObject = new Publicacion(id, imageLink, title, author, editorial, description);
                        System.out.println("Publicacion " + title + ", en posicion: " + i);
                        publicacionesObtenidas[i] = bookObject;
                    }

                    if (publicacionesObtenidas != null) {
                        this.publicacionNetworkDataSource.addPublicacionesDescargadas(publicacionesObtenidas);
                        System.out.println("La lista de publicaciones en fetchBook se ha insertado, el tamaño " + publicacionesObtenidas.length);
                        System.out.println(publicacionNetworkDataSource.getCurrentBooks().getValue());

                    } else {
                        System.out.println("La lista de publicaciones en fetchBook es nula");
                    }
                } else {
                    System.out.println("Items nulos");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}