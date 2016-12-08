package com.student.fahrtenbuchapp.realm.dataSync;

import com.student.fahrtenbuchapp.realm.models.Car;
import com.student.fahrtenbuchapp.realm.models.Credentials;
import com.student.fahrtenbuchapp.realm.models.Drive;
import com.student.fahrtenbuchapp.realm.models.Token;
import com.student.fahrtenbuchapp.realm.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Fabian on 07.12.16.
 */

public interface BackEndService {
    @POST("login")
    Call<Token> getToken(@Body Credentials credentials);

    @GET("users/{user}")
    Call<List<User>> listUsers(@Path("user") String user, @Header("Authentication") String token);

    @GET("cars/{car}")
    Call<List<Car>> listCars(@Path("car") String car);

    @POST("drives/")
    Call<Drive> createDrive(@Header("Authorization") String token, @Body Drive drive);

    @POST("drives/")
    Call<List<Drive>> createMultipleDrives(@Header("Authorization") String token, @Body List<Drive> drives);

}
