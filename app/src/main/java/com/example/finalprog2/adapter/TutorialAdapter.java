package com.example.finalprog2.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;

import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Tutorial;

import java.util.List;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder> {

    private List<Tutorial> listatutorial;
    private final OnTutorialClickListener listener; // Interfaz para manejar el click
    private FragmentActivity activity;

    public TutorialAdapter(List<Tutorial> listaTutorial, OnTutorialClickListener listener) {
        this.listatutorial = listatutorial;
        this.listener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TutorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutorial, parent, false);
        return new TutorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialViewHolder holder, int position) {
        Tutorial tutorial = listatutorial.get(position);
        holder.titulo.setText(tutorial.getTitulo());
        holder.descripcion.setText(tutorial.getDescripcionCorta());

        Glide.with(holder.itemView.getContext())
                .load(tutorial.getImagenUrl())
                .placeholder(R.drawable.sample_main_tutorial)
                .error(R.drawable.ic_tutorial)
                .into(holder.imagen);

        // Configura el clic en el botón para ver el tutorial
        holder.verButton.setOnClickListener(v -> listener.onTutorialClick(tutorial));
    }
    @Override
    public int getItemCount() {
        return listatutorial != null ? listatutorial.size() : 0;
    }

    public void actualizarTutorial(List<Tutorial> nuevaListaTutorial) {
        this.listatutorial = nuevaListaTutorial;
        notifyDataSetChanged();
    }

    public static class TutorialViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, descripcion;
        ImageView imagen;
        View verButton;

        public TutorialViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tutorial_titulo);
            descripcion = itemView.findViewById(R.id.tutorial_descripcion);
            imagen = itemView.findViewById(R.id.tutorial_imagen);
            verButton = itemView.findViewById(R.id.button_view_tutorial);  // Asume que hay un botón
        }
    }

    // Interfaz para manejar el clic en una noticia
    public interface OnTutorialClickListener {
        void onTutorialClick(Tutorial tutorial);
    }
}
