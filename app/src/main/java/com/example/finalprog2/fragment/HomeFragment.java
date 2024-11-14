package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.finalprog2.R;
import com.example.finalprog2.utils.PopupMenuHelper;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configuración del Toolbar
        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        // Configuración del título del Toolbar
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Inicio");
        }

        // Configuración del botón de menú en el Toolbar
        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button);
        leftMenuButton.setOnClickListener(v -> {
            PopupMenuHelper.showPopupMenu(getContext(), leftMenuButton, requireActivity());
        });

        // Configuracion del boton perfil
        ImageButton rightUserButton = view.findViewById(R.id.right_user_button);
        rightUserButton.setOnClickListener(v -> {
            navigateToFragment(new EditarPerfilFragment());
        });

        // Navegación entre fragmentos usando FragmentTransaction
        setupNavigationButtons(view);
    }

    private void setupNavigationButtons(View view) {
        view.findViewById(R.id.btnNoticias).setOnClickListener(v -> {
            navigateToFragment(new NoticiasMainFragment());
        });

        view.findViewById(R.id.btnTutoriales).setOnClickListener(v -> {
            navigateToFragment(new TutorialesMainFragment());
        });

        view.findViewById(R.id.btnAutoEvaluacion).setOnClickListener(v -> {
            navigateToFragment(new AutoEvalFragment());
        });

        view.findViewById(R.id.btnForos).setOnClickListener(v -> {
            navigateToFragment(new ListarForoFragment());
        });

        view.findViewById(R.id.btnSitios).setOnClickListener(v -> {
            navigateToFragment(new MapaSitiosFragment());
        });
    }

    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
