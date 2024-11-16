package com.example.finalprog2.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
        // Configuración del Toolbar
        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        // Configuración del título en el Toolbar
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Foro");
        }

        // Ocultar el nombre de la app
        toolbar.setTitle("");

        // Botón de retroceso (opcional)
        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button);
        leftMenuButton.setOnClickListener(v -> requireActivity().onBackPressed());

        // Vinculación de vistas
        tvForumTitle = view.findViewById(R.id.tv_forum_title);
        tvForumBody = view.findViewById(R.id.tv_descripcion_foro);
        etComment = view.findViewById(R.id.et_comment);
        btnSend = view.findViewById(R.id.btn_send);
        messagesLayout = view.findViewById(R.id.messages_layout);

        // Obtener los datos del bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String titulo = bundle.getString("titulo");
            String cuerpo = bundle.getString("cuerpo");
            foroId = bundle.getString("id");
            // Asignar los datos a los elementos del layout
            tvForumTitle.setText(titulo);
            tvForumBody.setText(cuerpo);
        }

        // Configuración del botón de enviar comentario
        btnSend.setOnClickListener(v -> {
            if (foroId != null) {
                String comentarioTexto = etComment.getText().toString();
                String nombre = cargarDatosUsuario(view);

                if (!comentarioTexto.isEmpty()) {
                    agregarComentarioAFirebase(comentarioTexto, nombre);
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

        return view; // Esta es la línea final del método onCreateView
    }

    private String cargarDatosUsuario(View view) {
        // Obtener datos del usuario de SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = sharedPreferences.getString("usuario", null);
        if (nombreUsuario == null) {
            Toast.makeText(getActivity(), "No se encontró el nombre de usuario", Toast.LENGTH_SHORT).show();

        }else {
            return nombreUsuario;
        }
        return nombreUsuario;
    }

    private void cargarComentarios() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("comentario")
                .whereEqualTo("idPublicacion", foroId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    messagesLayout.removeAllViews(); // Limpiar comentarios anteriores
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String comentarioTexto = document.getString("texto");
                        String autor = document.getString("usuario");

                        // Crear un LinearLayout para contener el TextView
                        LinearLayout comentarioLayout = new LinearLayout(getContext());
                        comentarioLayout.setOrientation(LinearLayout.VERTICAL);
                        comentarioLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        // Crear un TextView para cada comentario
                        TextView comentarioView = new TextView(getContext());
                        comentarioView.setText(autor + ": " + comentarioTexto);
                        comentarioView.setTextSize(16);
                        comentarioView.setPadding(16, 16, 16, 16); // Padding adicional

                        // Aplicar el fondo de estilo con el archivo XML
                        comentarioView.setBackgroundResource(R.drawable.foro_item_background);

                        // Asegurarse de que el TextView se ajuste al contenido
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        comentarioView.setLayoutParams(layoutParams);

                        // Agregar el TextView al LinearLayout
                        comentarioLayout.addView(comentarioView);

                        // Agregar el LinearLayout al layout de mensajes
                        messagesLayout.addView(comentarioLayout);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("VerForoFragment", "Error al cargar comentarios", e);
                });
    }




    private void agregarComentarioAFirebase(String comentarioTexto, String usuario) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> comentario = new HashMap<>();
        comentario.put("idPublicacion", foroId);
        comentario.put("texto", comentarioTexto);
        comentario.put("usuario", usuario); // Cambiar esto según sea necesario

        db.collection("comentario")
                .add(comentario)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Comentario agregado", Toast.LENGTH_SHORT).show();
                    cargarComentarios(); // Actualizar los comentarios
                })
                .addOnFailureListener(e -> {
                    Log.e("VerForoFragment", "Error al agregar comentario", e);
                    Toast.makeText(getContext(), "Error al agregar comentario", Toast.LENGTH_SHORT).show();
                });
    }
}
