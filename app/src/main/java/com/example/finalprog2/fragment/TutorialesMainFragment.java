package com.example.finalprog2.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprog2.R;
import com.example.finalprog2.adapter.TutorialAdapter;
import com.example.finalprog2.conexion.FirebaseTestData;
import com.example.finalprog2.entidad.Tutorial;
import com.example.finalprog2.utils.PopupMenuHelper;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TutorialesMainFragment extends Fragment implements TutorialAdapter.OnTutorialClickListener {

    private List<Tutorial> tutoriales = new ArrayList<>();
    private List<Tutorial> tutorialFiltradas = new ArrayList<>();
    private List<Tutorial> tutorialPagina;
    private int paginaActual = 0;
    private static final int TUTORIAL_POR_PAGINA = 4;

    private RecyclerView recyclerView;
    private TutorialAdapter tutorialAdapter;

    private TextView tituloTutorialPrincipal, descripcionTutorialPrincipal;
    private ImageView imagenTutorialPrincipal; // ImageView para la imagen de la tutorial principal
    private ImageButton btnPreviousPage, btnNextPage;
    private EditText searchEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_main, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_tutorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tutorialAdapter = new TutorialAdapter(tutorialPagina, this); // Pasa el fragmento como listener
        recyclerView.setAdapter(tutorialAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Tutoriales");
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


        FirebaseTestData firebaseTestData = new FirebaseTestData();
      //  firebaseTestData.crearTutorialDePrueba();
        inicializarTutorial();

        tituloTutorialPrincipal = view.findViewById(R.id.titulo_tutorial_principal);
        descripcionTutorialPrincipal = view.findViewById(R.id.descripcion_tutorial_principal);
        imagenTutorialPrincipal = view.findViewById(R.id.image_main); // Asigna el ImageView para la imagen del tutorial principal

        btnPreviousPage = view.findViewById(R.id.btn_previous_page);
        btnNextPage = view.findViewById(R.id.btn_next_page);

        cargarTutorialPagina(paginaActual);

        btnPreviousPage.setOnClickListener(v -> {
            if (paginaActual > 0) {
                paginaActual--;
                cargarTutorialPagina(paginaActual);
            }
        });

        btnNextPage.setOnClickListener(v -> {
            if ((paginaActual + 1) * TUTORIAL_POR_PAGINA < tutorialFiltradas.size()) {
                paginaActual++;
                cargarTutorialPagina(paginaActual);
            }
        });

        searchEditText = view.findViewById(R.id.et_buscar_foro);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filtrarTutorial(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void inicializarTutorial() {
        tutoriales.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference tutorialRef = db.collection("tutoriales");

        tutorialRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if (snapshot != null) {
                    for (QueryDocumentSnapshot document : snapshot) {
                        String titulo = document.getString("titulo");
                        String descripcion = document.getString("descripcion");
                        String enlace = document.getString("enlace");
                        String imagen = document.getString("imagen");
                        String textoCompleto = document.getString("texto_completo");
                        Date fechaPublicacion = document.getDate("fecha_publicacion");

                        Tutorial tutorial = new Tutorial(titulo, imagen, descripcion, textoCompleto, fechaPublicacion, enlace);
                        tutoriales.add(tutorial);
                    }
                   tutorialFiltradas.addAll(tutoriales);
                    cargarTutorialPagina(paginaActual);
                }
            } else {
                FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
                Log.e("TutorialesMainFragment", "Error al cargar tutoriales: ", exception);
                //Toast.makeText(getContext(), "Error al cargar tutoriales", Toast.LENGTH_SHORT).show();
                popupmsg("Error","Error al cargar tutoriales");
            }
        });
    }

    private void filtrarTutorial(String query) {
        tutorialFiltradas.clear();
        if (query.isEmpty()) {
            tutorialFiltradas.addAll(tutoriales);
        } else {
            for (Tutorial tutorial : tutoriales) {
                if (tutorial.getTitulo().toLowerCase().contains(query.toLowerCase())) {
                    tutorialFiltradas.add(tutorial);
                }
            }
        }
        paginaActual = 0;
        cargarTutorialPagina(paginaActual);
    }

    private void cargarTutorialPagina(int pagina) {
        int inicio = pagina * TUTORIAL_POR_PAGINA;
        int fin = Math.min(inicio + TUTORIAL_POR_PAGINA, tutorialFiltradas.size());

        tutorialPagina = new ArrayList<>(tutorialFiltradas.subList(inicio, fin));

        if (!tutorialPagina.isEmpty()) {
            Tutorial tutorialPrincipal = tutorialPagina.get(0);
            tituloTutorialPrincipal.setText(tutorialPrincipal.getTitulo());
            descripcionTutorialPrincipal.setText(tutorialPrincipal.getDescripcionCorta());

            // Carga la imagen del tutorial principal con Glide
            Glide.with(this)
                    .load(tutorialPrincipal.getImagenUrl())
                    .placeholder(R.drawable.tutorial_news_background)
                    .error(R.drawable.ic_tutorial)
                    .into(imagenTutorialPrincipal);
        }

        if (tutorialAdapter != null) {
            tutorialAdapter.actualizarTutorial(tutorialPagina);
        }
    }
    @Override
    public void onTutorialClick(Tutorial tutorial) {
        VerTutorialFragment verTutorialFragment = new VerTutorialFragment();

        Bundle bundle = new Bundle();
        bundle.putString("titulo", tutorial.getTitulo());
        bundle.putString("descripcion", tutorial.getDescripcionCorta());
        bundle.putString("imagen", tutorial.getImagenUrl());
        bundle.putString("textoCompleto", tutorial.getTextoCompleto());

        verTutorialFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, verTutorialFragment)
                .addToBackStack(null)
                .commit();
    }

    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void popupmsg(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cierra el diálogo
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}