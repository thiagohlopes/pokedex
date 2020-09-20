package com.example.podedex_api.API.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.example.podedex_api.models.RespostaPokemon;

public interface PokeapiService {

    @GET("pokemon")
    Call<RespostaPokemon> ObterListaPokemon (@Query("limit") int limit, @Query("offset") int offset); //Call é o tipo da classe que controlara as respostas
}
