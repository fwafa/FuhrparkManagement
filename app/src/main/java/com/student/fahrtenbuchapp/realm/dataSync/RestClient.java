package com.student.fahrtenbuchapp.realm.dataSync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.student.fahrtenbuchapp.realm.models.Credentials;
import com.student.fahrtenbuchapp.realm.models.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fabian on 08.12.16.
 */

public class RestClient {
    public static final String BASE_URL = "https//10.18.2.151/api/";

    private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    /**TODO: add Authentication Headers.
     * TODO: Realm.io data storage
     */

    public RestClient() {

        BackEndService apiService = retrofit.create(BackEndService.class);


        Credentials credentials = new Credentials("Administrator", "153cDc153");
        Call<Token> call = apiService.getToken(credentials);
        call.enqueue(new Callback<Token>(){
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = response.body();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });

    }

}
