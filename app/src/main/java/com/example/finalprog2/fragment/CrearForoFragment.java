package com.example.finalprog2.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Publicacion;

import java.util.HashMap;
import java.util.Map;

public class CrearForoFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_publicacion, container, false);

        // Configuración del Toolbar
        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        // Configuración del título en el Toolbar
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Crear Publicación");
        }

        // Ocultar el nombre de la app
        toolbar.setTitle("");

        // Botón de retroceso (opcional)
        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button);
        leftMenuButton.setOnClickListener(v -> requireActivity().onBackPressed());

        // Configuración de los campos del formulario
        EditText inputTitulo = view.findViewById(R.id.input_titulo);
        EditText inputDescripcion = view.findViewById(R.id.input_descripcion_post);
        RadioGroup rgRelacionadoA = view.findViewById(R.id.rg_relacionado_a);
        Button btnCrearPost = view.findViewById(R.id.btn_crear_post);

        // Mapa para las opciones del RadioGroup
        Map<Integer, String> radioButtonValues = new HashMap<>();
        radioButtonValues.put(R.id.rb_opcion_eolica, "wind");
        radioButtonValues.put(R.id.rb_opcion_electrica, "flash");
        radioButtonValues.put(R.id.rb_opcion_ecologia, "ic_ecologia");

        // Lógica para crear la publicación
        btnCrearPost.setOnClickListener(view1 -> {
            String titulo = inputTitulo.getText().toString();
            String descripcion = inputDescripcion.getText().toString();

            // Obtener el id del RadioButton seleccionado
            int seleccionadoId = rgRelacionadoA.getCheckedRadioButtonId();
            String categoriaSeleccionada = radioButtonValues.get(seleccionadoId);

            if (!titulo.isEmpty() && !descripcion.isEmpty() && categoriaSeleccionada != null) {
                // Crear el objeto Publicacion
                Publicacion publicacion = new Publicacion();
                publicacion.setTitulo(titulo);
                publicacion.setDescripcion(descripcion);
                publicacion.setRelacionEnergetica(categoriaSeleccionada);
                String nombre = cargarDatosUsuario(view); // Guardar el valor en relacionEnergetica
                publicacion.setUsuario(nombre);
                publicacion.setEstado(true); // La publicación puede estar activa

                // Guardar la publicación en la base de datos
                publicacion.guardarPublicacion();

                // Mostrar un mensaje de éxito
                Toast.makeText(getActivity(), "Publicación creada correctamente", Toast.LENGTH_SHORT).show();

                // Limpiar los campos de texto y deseleccionar el RadioGroup
                inputTitulo.setText(""); // Limpiar el título
                inputDescripcion.setText(""); // Limpiar la descripción
                rgRelacionadoA.clearCheck(); // Deseleccionar cualquier opción del RadioGroup

                // Volver al fragmento de la lista de foros
               /* requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ListarForoFragment())  // Reemplaza con el fragmento que lista los foros
                        .addToBackStack(null)
                        .commit();*/
            } else {
                Toast.makeText(getActivity(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private String cargarDatosUsuario(View view) {
        // Obtener datos del usuario de SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = sharedPreferences.getString("usuario", null);
        if (nombreUsuario == null) {
            Toast.makeText(getActivity(), "No se encontró el nombre de usuario", Toast.LENGTH_SHORT).show();
        } else {
            return nombreUsuario;
        }
        return nombreUsuario;
    }
}
