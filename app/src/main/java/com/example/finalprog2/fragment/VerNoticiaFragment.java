package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Noticias");
        }

        //Ocultar el nombre de la app
        toolbar.setTitle("");

        // Configuración del botón de menú en el Toolbar
        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button);
        leftMenuButton.setTag("left");
        leftMenuButton.setOnClickListener(v -> {
            PopupMenuHelper.showPopupMenu(getContext(), leftMenuButton, requireActivity());
        });

        // Configuracion del boton perfil
        ImageButton rightUserButton = view.findViewById(R.id.right_user_button);
        rightUserButton.setTag("right");
        rightUserButton.setOnClickListener(v -> {
            //navigateToFragment(new EditarPerfilFragment());
            PopupMenuHelper.showPopupMenu(getContext(), rightUserButton, requireActivity());
        });


        ImageView imageNoticia = view.findViewById(R.id.image_noticia);
        TextView titleTextView = view.findViewById(R.id.text_noticia_titulo);
        TextView contentTextView = view.findViewById(R.id.text_noticia_contenido);


        // Obtener los datos de la noticia del bundle
        Bundle arguments = getArguments();
        Bundle args = getArguments();
        if (args != null) {
            String titulo = args.getString("titulo");
            String imagenUrl = args.getString("imagen");
            String contenido = args.getString("textoCompleto");

            titleTextView.setText(titulo);
            contentTextView.setText(contenido);

           Glide.with(this).load(imagenUrl).into(imageNoticia); // Utiliza Glide o Picasso para cargar la imagen
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