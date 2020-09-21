package com.example.Pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.example.Pokedex.models.Pokemon;

import java.util.ArrayList;

public class PokemonAdapter extends  RecyclerView.Adapter<PokemonAdapter.ViewHolder>{

    private ArrayList<Pokemon> dataset;
    private Context context;

    public PokemonAdapter(Context context) {
        this.context = context;
        dataset = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        Pokemon p = dataset.get(position);
        holder.textViewName.setText(p.getName());


        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + p.getNumber() + ".png")
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageViewPokemon);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void updatePokemonList(ArrayList<Pokemon> pokemonList) {
        dataset.addAll(pokemonList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageViewPokemon;
        private TextView textViewName;

        @Override
        public void onClick(View view) {


        }

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            imageViewPokemon = (ImageView) itemView.findViewById(R.id.iv_pokemon);
            textViewName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
