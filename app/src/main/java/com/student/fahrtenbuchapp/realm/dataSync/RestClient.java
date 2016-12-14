package com.student.fahrtenbuchapp.realm.dataSync;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.student.fahrtenbuchapp.realm.models.Car;
import com.student.fahrtenbuchapp.realm.models.Credentials;
import com.student.fahrtenbuchapp.realm.models.Drive;
import com.student.fahrtenbuchapp.realm.models.Token;
import com.student.fahrtenbuchapp.realm.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fabian on 08.12.16.
 */

public class RestClient {
    private static final String BASE_URL = "https//10.18.2.151/api/";

    /**
     * TODO: add Authentication Headers.
     * TODO: Realm.io data storage
     **/

    private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create();

    private  Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    private BackEndService apiService = retrofit.create(BackEndService.class);

    public RestClient() {

        Credentials credentials = new Credentials("Administrator", "153cDc153");

    }

    public void getToken(Credentials credentials){
        Call<Token> callToken = apiService.getToken(credentials);
        callToken.enqueue(new Callback<Token>(){
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                //TODO: Save Token!
                Token token = response.body();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {

            }
        });
    }

    public void getAllUser(Token token){
        Call<List<User>> callUser = apiService.listUsers("","Bearer " + token.getToken());
        callUser.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                //TODO: Save User List in Realm
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                //TODO: Implement error handling
            }
        });
    }

    public void getUserById(String id, Token token){
        Call<List<User>> callUser = apiService.listUsers(id,"Bearer " + token.getToken());
        callUser.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                //TODO: Save User List in Realm
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                //TODO: Implement error handling
            }
        });
    }

    public void getAllCars(){
        Call<List<Car>> callCar = apiService.listCars("");
        callCar.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                //TODO: Save Car List in Realm
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                //TODO: Implement error handling
            }
        });
    }

    public void postDrive(List<Drive> drives, Token token){
        Call<List<Drive>> postDrive = apiService.createMultipleDrives("Bearer " + token.getToken(), drives);
        postDrive.enqueue(new Callback<List<Drive>>() {
            @Override
            public void onResponse(Call<List<Drive>> call, Response<List<Drive>> response) {
                //TODO: implement success Case
            }

            @Override
            public void onFailure(Call<List<Drive>> call, Throwable t) {
                //TODO: Implement error handling
            }
        });
    }

}
