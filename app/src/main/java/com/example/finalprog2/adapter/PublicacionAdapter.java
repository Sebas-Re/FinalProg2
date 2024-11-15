package com.example.finalprog2.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Publicacion;
import com.example.finalprog2.fragment.VerForoFragment;

import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.PublicacionViewHolder> {

    private List<Publicacion> publicaciones;
    private Context context;

    public PublicacionAdapter(List<Publicacion> publicaciones, Context context) {
        this.publicaciones = publicaciones;
        this.context = context;
    }

    @NonNull
    @Override
    public PublicacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_publicacion, parent, false);
        return new PublicacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicacionViewHolder holder, int position) {
        Publicacion publicacion = publicaciones.get(position);
        holder.bind(publicacion);
    }

    @Override
    public int getItemCount() {
        return publicaciones.size();
    }

    public class PublicacionViewHolder extends RecyclerView.ViewHolder {
        Button btnPublicacion;

        public PublicacionViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPublicacion = itemView.findViewById(R.id.btn_publicacion);
        }

        public void bind(final Publicacion publicacion) {
            btnPublicacion.setText(publicacion.getTitulo());

            btnPublicacion.setOnClickListener(v -> {
                VerForoFragment verForoFragment = new VerForoFragment();

                // Crea un bundle para pasar los datos
                Bundle bundle = new Bundle();
                bundle.putString("titulo", publicacion.getTitulo());
                bundle.putString("cuerpo", publicacion.getDescripcion());

                // Asigna el bundle al fragmento
                verForoFragment.setArguments(bundle);

                // Realiza la transacción del fragmento
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, verForoFragment)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }
}

