package com.example.proyectomovil.data;

import android.icu.text.CaseMap;
import android.os.Debug;
import android.util.Log;

import com.example.proyectomovil.data.model.LoggedInUser;
import com.example.proyectomovil.data.model.Usuario;
import com.example.proyectomovil.servicios.loginServices;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://x366f795-8000.brs.devtunnels.ms")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            loginServices postService = retrofit.create(loginServices.class);
            Call<Usuario> call = postService.login(username,password);

            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    Log.e("Titulo",response.body().toString());
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Log.e("Error",t.toString());
                }
            });

            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException(e.toString(), e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}