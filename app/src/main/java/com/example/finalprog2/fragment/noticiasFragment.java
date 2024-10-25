package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalprog2.R;

public class noticiasFragment extends Fragment {

    private static final String ARG_TEXT = "arg_text";

    public static noticiasFragment newInstance(String text) {
        return new noticiasFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_noticias1, container, false);

        TextView textView = view.findViewById(R.id.text_view);

        // Obtener el texto desde los argumentos
        if (getArguments() != null) {
            String text = getArguments().getString(ARG_TEXT);
            textView.setText(text);
        }

        return view;
    }
}