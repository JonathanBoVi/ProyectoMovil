package com.example.proyectomovil.servicios;

import com.example.proyectomovil.data.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface loginServices {
    String API_ROUTE = "/login";
    @FormUrlEncoded
    @POST(API_ROUTE)
    Call<Usuario> login(@Field("email") String email, @Field("password") String password);
}
