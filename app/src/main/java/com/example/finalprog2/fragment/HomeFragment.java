package com.example.finalprog2.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Usuario;
import com.example.finalprog2.interfaces.ObtenerUsuarioCallback;
import com.example.finalprog2.interfaces.VerificarEmailCallback;
import com.example.finalprog2.utils.PopupMenuHelper;
import com.example.finalprog2.negocio.NegocioUsuario;
import com.google.android.gms.tasks.TaskCompletionSource;

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

        //Ocultar el nombre de la app
        toolbar.setTitle("");

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

        View btnConfiguracionElectrodomestico = view.findViewById(R.id.btnConfiguracionElectrodomesticos);
        // Carga de datos del usuario y verificación del rol
        cargarDatosUsuario(view);

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

    private void cargarDatosUsuario(View view) {
        // Obtener datos del usuario de SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = sharedPreferences.getString("usuario", null);
        if (nombreUsuario == null) {
            Toast.makeText(getActivity(), "No se encontró el nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        NegocioUsuario negocioUsuario = new NegocioUsuario(getActivity());
        Usuario usuarioAcargar = new Usuario();
        usuarioAcargar.setUsuario(nombreUsuario);

        negocioUsuario.ObtenerUsuario(usuarioAcargar, new ObtenerUsuarioCallback() {
            @Override
            public boolean onSuccess(Usuario usuarioEncontrado) {
                // Aquí puedes actualizar la interfaz con los datos del usuario si lo necesitas
                verificarRolUsuario(usuarioEncontrado.getEmail(), view);
                return true;
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verificarRolUsuario(String email, View view) {
        NegocioUsuario negocioUsuario = new NegocioUsuario(getActivity());
        negocioUsuario.obtenerRolUsuario(email, new VerificarEmailCallback() {
            @Override
            public void onSuccess() {
                // Verificar si el rol es "administrador"
                    // Mostrar la tarjeta de configuración si el usuario es administrador
                    View btnConfiguracionElectrodomestico = view.findViewById(R.id.btnConfiguracionElectrodomesticos);
                    btnConfiguracionElectrodomestico.setVisibility(View.VISIBLE);
               //
                  btnConfiguracionElectrodomestico.setOnClickListener(v -> navigateToFragment(new ConfiguracionElectroFragment()));

            }

            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onFailure(Exception e) {
                Log.e("HomeFragment", "Error al verificar el rol del usuario", e);
            }
        });
    }
}
