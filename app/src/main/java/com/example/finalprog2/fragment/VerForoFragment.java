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

public class VerForoFragment extends Fragment {

    private TextView tvForumTitle;
    private TextView tvForumBody;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_ver_foro, container, false);

        // Obtén referencias a los TextViews
        tvForumTitle = view.findViewById(R.id.tv_forum_title);
        tvForumBody = view.findViewById(R.id.tv_forum_body);

        // Obtener los argumentos pasados desde ListarForoFragment
        if (getArguments() != null) {
            String tituloForo = getArguments().getString("titulo");
            String cuerpoForo = getArguments().getString("cuerpo");

            // Establece los valores en los TextViews
            tvForumTitle.setText(tituloForo);
            tvForumBody.setText(cuerpoForo);
        }

        return view;
    }
}
