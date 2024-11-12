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
import com.example.finalprog2.entidad.Noticia;
import com.example.finalprog2.R;
import com.example.finalprog2.fragment.VerNoticiaFragment;

import java.util.List;

public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiaViewHolder> {

    private List<Noticia> listaNoticias;
    private final OnNoticiaClickListener listener; // Interfaz para manejar el clic
    private FragmentActivity activity;

    public NoticiaAdapter(List<Noticia> listaNoticias, OnNoticiaClickListener listener) {
        this.listaNoticias = listaNoticias;
        this.listener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public NoticiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_noticia, parent, false);
        return new NoticiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticiaViewHolder holder, int position) {
        Noticia noticia = listaNoticias.get(position);
        holder.titulo.setText(noticia.getTitulo());
        holder.descripcion.setText(noticia.getDescripcionCorta());

        Glide.with(holder.itemView.getContext())
                .load(noticia.getImagenUrl())
                .placeholder(R.drawable.sample_main_news)
                .error(R.drawable.ic_news)
                .into(holder.imagen);

        // Configura el clic en el botón para ver la noticia
        holder.verButton.setOnClickListener(v -> listener.onNoticiaClick(noticia));
    }
    @Override
    public int getItemCount() {
        return listaNoticias != null ? listaNoticias.size() : 0;
    }

    public void actualizarNoticias(List<Noticia> nuevaListaNoticias) {
        this.listaNoticias = nuevaListaNoticias;
        notifyDataSetChanged();
    }

    public static class NoticiaViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, descripcion;
        ImageView imagen;
        View verButton;

        public NoticiaViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.noticia_titulo);
            descripcion = itemView.findViewById(R.id.noticia_descripcion);
            imagen = itemView.findViewById(R.id.noticia_imagen);
            verButton = itemView.findViewById(R.id.button_view_news);  // Asume que hay un botón
        }
    }

    // Interfaz para manejar el clic en una noticia
    public interface OnNoticiaClickListener {
        void onNoticiaClick(Noticia noticia);
    }
}
