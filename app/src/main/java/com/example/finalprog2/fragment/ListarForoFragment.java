package com.example.finalprog2.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprog2.R;
import com.example.finalprog2.adapter.PublicacionAdapter;
import com.example.finalprog2.entidad.Publicacion;
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

        // Cargar publicaciones desde Firebase
        cargarPublicaciones();
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
}
