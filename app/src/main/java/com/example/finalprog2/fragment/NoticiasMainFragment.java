package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.finalprog2.R;
public class NoticiasMainFragment extends Fragment {

    // Método para crear la vista del fragmento
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout del fragmento
        return inflater.inflate(R.layout.fragment_noticias_main, container, false);
    }

    // Método que se llama cuando la vista ha sido creada
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Aquí puedes inicializar los elementos de la vista, por ejemplo:
        // ImageView imageView = view.findViewById(R.id.image_main);
        // TextView titleTextView = view.findViewById(R.id.text_title);
        // TextView descriptionTextView = view.findViewById(R.id.text_description);
        // EditText searchEditText = view.findViewById(R.id.et_buscar_foro);
        // ScrollView scrollView = view.findViewById(R.id.scrollView);
        // TextView paginationTextView = view.findViewById(R.id.paginationText);

        // Aquí puedes cargar datos en los elementos de la vista
    }
}
