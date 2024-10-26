package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalprog2.R;

public class VerTutorialFragment extends Fragment {

    // Método para crear la vista del fragmento
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.fragment_ver_tutorial, container, false);
    }

    // Método que se llama cuando la vista ha sido creada
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView imageTutorial = view.findViewById(R.id.image_tutorial);
        TextView titleTextView = view.findViewById(R.id.text_tutorial_titulo);
        TextView contentTextView = view.findViewById(R.id.text_tutorial_contenido);


        // Cargar imagen desde drawable
        imageTutorial.setImageResource(R.drawable.sample_main_tutorial2); // Cambia "nombre_de_tu_imagen" por el nombre real de tu imagen sin la extensión

        // Asigna el título y contenido si es necesario
        titleTextView.setText("Título del Tutorial");
        contentTextView.setText("Contenido completo del tutorial...");


    }

}