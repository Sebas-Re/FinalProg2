package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.finalprog2.R;
public class VerNoticiaFragment extends Fragment {

    // Método para crear la vista del fragmento
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.fragment_ver_noticia, container, false);
    }

    // Método que se llama cuando la vista ha sido creada
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageNoticia = view.findViewById(R.id.image_noticia);
        TextView titleTextView = view.findViewById(R.id.text_noticia_titulo);
        TextView contentTextView = view.findViewById(R.id.text_noticia_contenido);


        // Cargar imagen desde drawable
        imageNoticia.setImageResource(R.drawable.sample_main_news2); // Cambia "nombre_de_tu_imagen" por el nombre real de tu imagen sin la extensión

        // Asigna el título y contenido si es necesario
        titleTextView.setText("Título de la Noticia");
        contentTextView.setText("Contenido completo de la noticia...");


    }

}