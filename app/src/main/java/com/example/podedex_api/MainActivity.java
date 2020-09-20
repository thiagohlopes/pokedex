package com.example.podedex_api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.podedex_api.API.Pokemon.PokeapiService;
import com.example.podedex_api.models.Pokemon;
import com.example.podedex_api.models.RespostaPokemon;

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
    private ListaPokemonAdapter listaPokemonAdapter;

    private int offset;

    private boolean aptoParaCarregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        listaPokemonAdapter = new ListaPokemonAdapter(this);
        recyclerView.setAdapter(listaPokemonAdapter);
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
        Call<RespostaPokemon> respostaPokemonCall = service.ObterListaPokemon(20, offset);

        respostaPokemonCall.enqueue(new Callback<RespostaPokemon>() {
            @Override
            public void onResponse(Call<RespostaPokemon> call, Response<RespostaPokemon> response) {
                aptoParaCarregar = true;
                if (response.isSuccessful()) {

                    RespostaPokemon respostaPokemon = response.body();
                    ArrayList<Pokemon> listaPokemon = respostaPokemon.getResults();

                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon);
                } else {
                    Log.e(TAG, "OnResponde" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<RespostaPokemon> call, Throwable t) {
                aptoParaCarregar = true;
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}