package com.example.finalprog2.fragment;

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
import com.example.finalprog2.adapter.NoticiaAdapter;
import com.example.finalprog2.entidad.Noticia;
import com.example.finalprog2.utils.PopupMenuHelper;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoticiasMainFragment extends Fragment implements NoticiaAdapter.OnNoticiaClickListener {

    private List<Noticia> noticias = new ArrayList<>();
    private List<Noticia> noticiasFiltradas = new ArrayList<>();
    private List<Noticia> noticiasPagina;
    private int paginaActual = 0;
    private static final int NOTICIAS_POR_PAGINA = 4;

    private RecyclerView recyclerView;
    private NoticiaAdapter noticiaAdapter;

    private TextView tituloNoticiaPrincipal, descripcionNoticiaPrincipal;
    private ImageView imagenNoticiaPrincipal; // ImageView para la imagen de la noticia principal
    private ImageButton btnPreviousPage, btnNextPage;
    private EditText searchEditText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticias_main, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_noticias);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noticiaAdapter = new NoticiaAdapter(noticiasPagina, this); // Pasa el fragmento como listener
        recyclerView.setAdapter(noticiaAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Noticias");
        }

        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button);
        leftMenuButton.setOnClickListener(v -> PopupMenuHelper.showPopupMenu(getContext(), leftMenuButton, requireActivity()));

        // Configuracion del boton perfil
        ImageButton rightUserButton = view.findViewById(R.id.right_user_button);
        rightUserButton.setOnClickListener(v -> {
            navigateToFragment(new EditarPerfilFragment());
        });


        inicializarNoticias();

        tituloNoticiaPrincipal = view.findViewById(R.id.titulo_noticia_principal);
        descripcionNoticiaPrincipal = view.findViewById(R.id.descripcion_noticia_principal);
        imagenNoticiaPrincipal = view.findViewById(R.id.image_main); // Asigna el ImageView para la imagen de la noticia principal

        btnPreviousPage = view.findViewById(R.id.btn_previous_page);
        btnNextPage = view.findViewById(R.id.btn_next_page);

        cargarNoticiasPagina(paginaActual);

        btnPreviousPage.setOnClickListener(v -> {
            if (paginaActual > 0) {
                paginaActual--;
                cargarNoticiasPagina(paginaActual);
            }
        });

        btnNextPage.setOnClickListener(v -> {
            if ((paginaActual + 1) * NOTICIAS_POR_PAGINA < noticiasFiltradas.size()) {
                paginaActual++;
                cargarNoticiasPagina(paginaActual);
            }
        });

        searchEditText = view.findViewById(R.id.et_buscar_foro);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filtrarNoticias(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void inicializarNoticias() {
        noticias.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference noticiasRef = db.collection("noticias");

        noticiasRef.get().addOnCompleteListener(task -> {
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

                        Noticia noticia = new Noticia(titulo, imagen, descripcion, textoCompleto, fechaPublicacion, enlace);
                        noticias.add(noticia);
                    }
                    noticiasFiltradas.addAll(noticias);
                    cargarNoticiasPagina(paginaActual);
                }
            } else {
                FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
                Log.e("NoticiasMainFragment", "Error al cargar noticias: ", exception);
                Toast.makeText(getContext(), "Error al cargar noticias", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filtrarNoticias(String query) {
        noticiasFiltradas.clear();
        if (query.isEmpty()) {
            noticiasFiltradas.addAll(noticias);
        } else {
            for (Noticia noticia : noticias) {
                if (noticia.getTitulo().toLowerCase().contains(query.toLowerCase())) {
                    noticiasFiltradas.add(noticia);
                }
            }
        }
        paginaActual = 0;
        cargarNoticiasPagina(paginaActual);
    }

    private void cargarNoticiasPagina(int pagina) {
        int inicio = pagina * NOTICIAS_POR_PAGINA;
        int fin = Math.min(inicio + NOTICIAS_POR_PAGINA, noticiasFiltradas.size());

        noticiasPagina = new ArrayList<>(noticiasFiltradas.subList(inicio, fin));

        if (!noticiasPagina.isEmpty()) {
            Noticia noticiaPrincipal = noticiasPagina.get(0);
            tituloNoticiaPrincipal.setText(noticiaPrincipal.getTitulo());
            descripcionNoticiaPrincipal.setText(noticiaPrincipal.getDescripcionCorta());

            // Carga la imagen de la noticia principal con Glide
            Glide.with(this)
                    .load(noticiaPrincipal.getImagenUrl())
                    .placeholder(R.drawable.main_news_background)
                    .error(R.drawable.ic_news)
                    .into(imagenNoticiaPrincipal);
        }

        if (noticiaAdapter != null) {
            noticiaAdapter.actualizarNoticias(noticiasPagina);
        }
    }
    @Override
    public void onNoticiaClick(Noticia noticia) {
        VerNoticiaFragment verNoticiaFragment = new VerNoticiaFragment();

        Bundle bundle = new Bundle();
        bundle.putString("titulo", noticia.getTitulo());
        bundle.putString("descripcion", noticia.getDescripcionCorta());
        bundle.putString("imagen", noticia.getImagenUrl());
        bundle.putString("textoCompleto", noticia.getTextoCompleto());

        verNoticiaFragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, verNoticiaFragment)
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
}
