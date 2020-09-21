package com.example.Pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.Pokedex.API.Pokemon.PokeapiService;
import com.example.Pokedex.models.Pokemon;
import com.example.Pokedex.models.PokemonResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "POKEDEX";

    private Retrofit retrofit;//criando instancia do retrofit

    private RecyclerView recyclerView;
    private PokemonAdapter pokemonAdapter;

    private int offset;

    private boolean aptoParaCarregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        pokemonAdapter = new PokemonAdapter(this);
        recyclerView.setAdapter(pokemonAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (aptoParaCarregar) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, " Fim.");

                            aptoParaCarregar = false;
                            offset += 20;
                            ObtençãoDados(offset);
                        }
                    }
                }

            }
        });

        retrofit = new Retrofit.Builder()//Instanciando uma retrofit
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())//formatando as respostas que chegam para Json
                .build();
        aptoParaCarregar = true;
        offset = 0;
        ObtençãoDados (offset);
    }

    private void ObtençãoDados(int offset) {
        PokeapiService service =retrofit.create(PokeapiService.class);
        Call<PokemonResponse> respostaPokemonCall = service.getPokemonList(20, offset);
        respostaPokemonCall.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                aptoParaCarregar = true;
                if (response.isSuccessful()) {

                    PokemonResponse pokemonResponse = response.body();
                    ArrayList<Pokemon> listaPokemon = pokemonResponse.getResults();

                    pokemonAdapter.updatePokemonList(listaPokemon);

                } else {
                    Log.e(TAG, "OnResponde" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                aptoParaCarregar = true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}