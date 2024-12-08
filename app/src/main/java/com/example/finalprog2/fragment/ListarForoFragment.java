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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprog2.R;
import com.example.finalprog2.adapter.PublicacionAdapter;
import com.example.finalprog2.entidad.Publicacion;
import com.example.finalprog2.utils.PopupMenuHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListarForoFragment extends Fragment {

    private RecyclerView recyclerView;
    private PublicacionAdapter adapter;
    private List<Publicacion> publicaciones;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listado_foro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recyclerView_publicaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        publicaciones = new ArrayList<>();
        adapter = new PublicacionAdapter(publicaciones,getContext());
        recyclerView.setAdapter(adapter);



        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Listado Foro");
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


        // Cargar publicaciones desde Firebase
        cargarPublicaciones();

        // Configurar el botón flotante para agregar nuevas publicaciones
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add_publicacion);
        fabAdd.setOnClickListener(v -> {
            // Navegar manualmente a CrearForoFragment usando FragmentTransaction
            navigateToFragment(new CrearForoFragment());
        });
    }

    private void cargarPublicaciones() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("publicaciones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        publicaciones.clear();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Publicacion publicacion = document.toObject(Publicacion.class);
                            publicaciones.add(publicacion);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        // Manejar errores si es necesario
                    }
                });
    }

    // Método para navegar a un nuevo fragmento
    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
