package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Comentario;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;


public class VerForoFragment extends Fragment {

    private TextView tvForumTitle;
    private TextView tvForumBody;
    private EditText etComment;
    private Button btnSend;
    private LinearLayout messagesLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_foro, container, false);

        tvForumTitle = view.findViewById(R.id.tv_forum_title);
        tvForumBody = view.findViewById(R.id.tv_forum_body);
        etComment = view.findViewById(R.id.et_comment);
        btnSend = view.findViewById(R.id.btn_send);
        messagesLayout = view.findViewById(R.id.messages_layout);

        if (getArguments() != null) {
            String tituloForo = getArguments().getString("titulo");
            String cuerpoForo = getArguments().getString("cuerpo");

            tvForumTitle.setText(tituloForo);
            tvForumBody.setText(cuerpoForo);
        }

        // Configurar el botón de enviar comentario
        btnSend.setOnClickListener(v -> {
            String comentarioTexto = etComment.getText().toString();
            if (!comentarioTexto.isEmpty()) {
                agregarComentarioAFirebase(comentarioTexto);
                etComment.setText("");  // Limpiar campo de texto
            } else {
                Toast.makeText(getContext(), "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void agregarComentarioAFirebase(String comentarioTexto) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String foroId = getArguments().getString("foroId"); // Id del foro actual

        Map<String, Object> comentario = new HashMap<>();
        comentario.put("usuario", "usuarioEjemplo"); // Debes obtener el nombre del usuario logueado
        comentario.put("texto", comentarioTexto);
        comentario.put("timestamp", System.currentTimeMillis());

        db.collection("publicaciones")
                .document(foroId)
                .collection("comentarios")
                .add(comentario)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Comentario agregado", Toast.LENGTH_SHORT).show();
                    cargarComentarios();  // Actualiza la lista de comentarios
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al agregar comentario", Toast.LENGTH_SHORT).show();
                });
    }

    private void cargarComentarios() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String foroId = getArguments().getString("foroId");

        db.collection("publicaciones")
                .document(foroId)
                .collection("comentarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    messagesLayout.removeAllViews();  // Limpiar la vista anterior
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String usuario = doc.getString("usuario");
                        String texto = doc.getString("texto");

                        // Crear TextView para el comentario
                        TextView tvComentario = new TextView(getContext());
                        tvComentario.setText(usuario + ": " + texto);
                        tvComentario.setTextSize(16f);
                        tvComentario.setPadding(16, 8, 16, 8);

                        // Añadir el comentario al layout
                        messagesLayout.addView(tvComentario);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
                });
    }

}
