package com.example.Pokedex.API.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.Pokedex.models.Pokemon;
import com.example.Pokedex.models.PokemonResponse;

public interface PokeapiService {

    @GET("pokemon")
    Call<PokemonResponse> getPokemonList (@Query("limit") int limit, @Query("offset") int offset); //Call é o tipo da classe que controlara as respostas

    @GET ("pokemon/{id}")
    Call<Pokemon> getPokemonDetails (@Path("id") String id);

}
