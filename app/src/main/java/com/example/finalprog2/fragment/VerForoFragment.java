package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.example.finalprog2.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class VerForoFragment extends Fragment {

    private TextView tvForumTitle;
    private TextView tvForumBody;
    private EditText etComment;
    private Button btnSend;
    private LinearLayout messagesLayout;
    private String foroId; // ID del foro actual

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_foro, container, false);

        // Vinculación de vistas
        tvForumTitle = view.findViewById(R.id.tv_forum_title);
        tvForumBody = view.findViewById(R.id.tv_forum_body);
        etComment = view.findViewById(R.id.et_comment);
        btnSend = view.findViewById(R.id.btn_send);
        messagesLayout = view.findViewById(R.id.messages_layout);

        // Obtener datos del foro desde los argumentos
        if (getArguments() != null) {
            foroId = getArguments().getString("foroId");
            String tituloForo = getArguments().getString("titulo");
            String cuerpoForo = getArguments().getString("cuerpo");

            tvForumTitle.setText(tituloForo);
            tvForumBody.setText(cuerpoForo);

            Log.d("VerForoFragment", "Foro ID recibido: " + foroId);
        } else {
            Log.e("VerForoFragment", "No se recibieron argumentos para el ID del foro");
            Toast.makeText(getContext(), "Error: No se encontró el ID del foro", Toast.LENGTH_SHORT).show();
        }

        // Configuración del botón de enviar comentario
        btnSend.setOnClickListener(v -> {
            if (foroId != null) {
                String comentarioTexto = etComment.getText().toString();
                if (!comentarioTexto.isEmpty()) {
                    agregarComentarioAFirebase(comentarioTexto);
                    etComment.setText(""); // Limpiar el campo de texto
                } else {
                    Toast.makeText(getContext(), "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Error: No se puede comentar sin un foro válido", Toast.LENGTH_SHORT).show();
            }
        });

        // Cargar comentarios existentes
        if (foroId != null) {
            cargarComentarios();
        }

        return view;
    }

    private void agregarComentarioAFirebase(String comentarioTexto) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear un mapa para almacenar el comentario
        Map<String, Object> comentario = new HashMap<>();
        comentario.put("usuario", "usuarioEjemplo"); // Cambiar por el usuario real del sistema
        comentario.put("texto", comentarioTexto);
        comentario.put("timestamp", System.currentTimeMillis());

        // Guardar comentario en Firebase
        db.collection("publicaciones")
                .document(foroId)
                .collection("comentarios")
                .add(comentario)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Comentario agregado", Toast.LENGTH_SHORT).show();
                    cargarComentarios(); // Actualizar lista de comentarios
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al agregar comentario", Toast.LENGTH_SHORT).show();
                });
    }

    private void cargarComentarios() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Leer comentarios desde Firebase
        db.collection("publicaciones")
                .document(foroId)
                .collection("comentarios")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    messagesLayout.removeAllViews(); // Limpiar comentarios anteriores

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String usuario = doc.getString("usuario");
                        String texto = doc.getString("texto");

                        // Crear un TextView para cada comentario
                        TextView tvComentario = new TextView(getContext());
                        tvComentario.setText(usuario + ": " + texto);
                        tvComentario.setTextSize(16f);
                        tvComentario.setPadding(16, 8, 16, 8);

                        // Agregar comentario a la vista
                        messagesLayout.addView(tvComentario);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
                });
    }
}
