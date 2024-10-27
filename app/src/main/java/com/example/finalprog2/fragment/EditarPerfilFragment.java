package com.example.finalprog2.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.finalprog2.R;


public class EditarPerfilFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_editar_perfil, container, false);
        EditText input_nombre = view.findViewById(R.id.input_nombre);
        EditText input_apellido = view.findViewById(R.id.input_apellido);
        EditText input_usuario = view.findViewById(R.id.input_usuario);
        EditText input_email = view.findViewById(R.id.input_email);
        EditText input_pass = view.findViewById(R.id.input_pass);
        EditText input_repetir_pass = view.findViewById(R.id.input_repetir_pass);
        Button btn_guardarCambios = view.findViewById(R.id.btn_guardarCambios);


        btn_guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String nombre = input_nombre.getText().toString();
            String apellido = input_apellido.getText().toString();
            String usuario = input_usuario.getText().toString();
            String email = input_email.getText().toString();
            String pass = input_pass.getText().toString();
            String repetir_pass = input_repetir_pass.getText().toString();

            //Logica de negocio para almacenar los datos modificados


            }
        });


       return view;
    }
}