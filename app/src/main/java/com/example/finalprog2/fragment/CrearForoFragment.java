package com.example.finalprog2.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Publicacion;

import java.util.HashMap;
import java.util.Map;

public class CrearForoFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crear_publicacion, container, false);
        EditText input_titulo = view.findViewById(R.id.input_titulo);
        EditText input_descripcion_post = view.findViewById(R.id.input_descripcion_post);
        RadioGroup rg_relacionado_a = view.findViewById(R.id.rg_relacionado_a);
        Button btn_crear_post = view.findViewById(R.id.btn_crear_post);


        //Esto todavia debe probarse. Por algun motivo se pueden seleccionar multiples opciones de radiobutton,
        // como si no pertenecieran al mismo radiogroup.
        Map<Integer, Runnable> radioButtonActions = new HashMap<>();
        radioButtonActions.put(R.id.rb_opcion_eolica, () -> {
            Toast.makeText(getActivity(), "Opción 1 seleccionada", Toast.LENGTH_SHORT).show();
        });
        radioButtonActions.put(R.id.rb_opcion_electrica, () -> {
            Toast.makeText(getActivity(), "Opción 2 seleccionada", Toast.LENGTH_SHORT).show();
        });
        radioButtonActions.put(R.id.rb_opcion_ecologia, () -> {
           Toast.makeText(getActivity(), "Opción 3 seleccionada", Toast.LENGTH_SHORT).show();
        });

        rg_relacionado_a.setOnCheckedChangeListener((group, checkedId) -> {
            Runnable action = radioButtonActions.get(checkedId);
            if (action != null) {
                action.run();
            }
        });

        /*
        btn_crear_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = input_titulo.getText().toString();
                String descripcion = input_descripcion_post.getText().toString();
                //int seleccionadoId = rg_relacionado_a.getCheckedRadioButtonId();





               Toast.makeText(getActivity(), "Publicación creada", Toast.LENGTH_SHORT).show();
            }
        });*/

        btn_crear_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titulo = input_titulo.getText().toString();
                String descripcion = input_descripcion_post.getText().toString();

                // Obtener la opción seleccionada del RadioGroup
                int seleccionadoId = rg_relacionado_a.getCheckedRadioButtonId();
                String categoriaSeleccionada = "";

                if (seleccionadoId == R.id.rb_opcion_eolica) {
                    categoriaSeleccionada = "Eólica";
                } else if (seleccionadoId == R.id.rb_opcion_electrica) {
                    categoriaSeleccionada = "Eléctrica";
                } else if (seleccionadoId == R.id.rb_opcion_ecologia) {
                    categoriaSeleccionada = "Ecología";
                }

                if (!titulo.isEmpty() && !descripcion.isEmpty() /*&& !categoriaSeleccionada.isEmpty()*/) {
                    // Crear un nuevo objeto Publicacion
                    Publicacion publicacion = new Publicacion();
                    publicacion.setTitulo(titulo);
                    publicacion.setDescripcion(descripcion);
                    publicacion.setRelacionEnergetica(categoriaSeleccionada);
                    publicacion.setEstado(true); // Por ejemplo, la publicación puede estar activa al crearse

                    // Guardar la publicación en la base de datos
                    publicacion.guardarPublicacion();

                    // Mostrar un mensaje de éxito
                    Toast.makeText(getActivity(), "Publicación creada correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}