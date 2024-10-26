package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalprog2.R;
import com.google.android.material.card.MaterialCardView;

public class ListarForoFragment extends Fragment {

    // Método para inflar el layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflamos el layout del fragmento
        return inflater.inflate(R.layout.fragment_listado_foro, container, false);
    }

    // Método para configurar vistas y eventos después de que el layout ha sido creado
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Buscar elementos del layout
        EditText etBuscarForo = view.findViewById(R.id.et_buscar_foro);
        ImageView ivFlechaIzquierda = view.findViewById(R.id.iv_flecha_izquierda);
        ImageView ivFlechaDerecha = view.findViewById(R.id.iv_flecha_derecha);

        // Configurar evento de clic para el campo de búsqueda
        etBuscarForo.setOnClickListener(v -> {
            String textoBusqueda = etBuscarForo.getText().toString();
            Toast.makeText(getContext(), "Buscando: " + textoBusqueda, Toast.LENGTH_SHORT).show();
            // Aquí puedes agregar la lógica de búsqueda
        });

        // Configurar evento de clic para flechas de navegación
        ivFlechaIzquierda.setOnClickListener(v ->
                Toast.makeText(getContext(), "Navegar a la izquierda", Toast.LENGTH_SHORT).show()
        );

        ivFlechaDerecha.setOnClickListener(v ->
                Toast.makeText(getContext(), "Navegar a la derecha", Toast.LENGTH_SHORT).show()
        );

        // Ejemplo de cómo configurar eventos en los CardViews

    }
}

