package com.example.finalprog2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.finalprog2.R;
import com.example.finalprog2.utils.PopupMenuHelper;

import android.widget.PopupMenu;

public class ListarForoFragment extends Fragment {

    // Método para inflar el layout
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflamos el layout del fragmento
        return inflater.inflate(R.layout.fragment_listado_foro, container, false);
    }

    // Habilitar el menú en el fragmento
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Esto habilita el menú en este fragmento
    }

    // No inflar el menú, así no aparecen los tres puntos
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // No inflar el menú aquí para ocultar los tres puntos
    }

    // Método para configurar vistas y eventos después de que el layout ha sido creado
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar el Toolbar
        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        // Configurar el título
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Foro" );
        }

        // Buscar elementos del layout
        EditText etBuscarForo = view.findViewById(R.id.et_buscar_foro);
        ImageView ivFlechaIzquierda = view.findViewById(R.id.iv_flecha_izquierda);
        ImageView ivFlechaDerecha = view.findViewById(R.id.iv_flecha_derecha);
        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button); // Tu ImageButton

        // Configurar evento de clic para el botón del menú
        leftMenuButton.setOnClickListener(v -> {
            PopupMenuHelper.showPopupMenu(getContext(), leftMenuButton, requireActivity());

        });
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
    }

}
