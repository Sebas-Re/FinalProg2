package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.finalprog2.R;
import com.example.finalprog2.utils.PopupMenuHelper;

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


        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Tutorial");
        }

        //Ocultar el nombre de la app
        toolbar.setTitle("");

        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button);
        leftMenuButton.setOnClickListener(v -> PopupMenuHelper.showPopupMenu(getContext(), leftMenuButton, requireActivity()));

        // Configuracion del boton perfil
        ImageButton rightUserButton = view.findViewById(R.id.right_user_button);
        rightUserButton.setOnClickListener(v -> {
            navigateToFragment(new EditarPerfilFragment());
        });


        ImageView imageTutorial = view.findViewById(R.id.image_tutorial);
        TextView titleTextView = view.findViewById(R.id.text_tutorial_titulo);
        TextView contentTextView = view.findViewById(R.id.text_tutorial_contenido);


        // Obtener los datos de la noticia del bundle
        Bundle arguments = getArguments();
        Bundle args = getArguments();
        if (args != null) {
            String titulo = args.getString("titulo");
            String imagenUrl = args.getString("imagen");
            String contenido = args.getString("textoCompleto");

            titleTextView.setText(titulo);
            contentTextView.setText(contenido);

            Glide.with(this).load(imagenUrl).into(imageTutorial); // Utiliza Glide o Picasso para cargar la imagen
        }

    }

    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


}