package com.example.finalprog2.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Usuario;
import com.example.finalprog2.negocio.NegocioUsuario;


public class RegistroUsuarioFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro_usuario, container, false);
        EditText input_nombre = view.findViewById(R.id.input_nombre);
        EditText input_apellido = view.findViewById(R.id.input_apellido);
        EditText input_usuario = view.findViewById(R.id.input_usuario);
        EditText input_email = view.findViewById(R.id.input_email);
        EditText input_pass = view.findViewById(R.id.input_pass);
        EditText input_repetir_pass = view.findViewById(R.id.input_repetir_pass);
        Button btn_registro_usuario = view.findViewById(R.id.btn_registro_usuario);
        TextView tv_Link_LogIn = view.findViewById(R.id.tv_link_LogIn);

        btn_registro_usuario.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String nombre = input_nombre.getText().toString();
                String apellido = input_apellido.getText().toString();
                String usuario = input_usuario.getText().toString();
                String email = input_email.getText().toString();
                String pass = input_pass.getText().toString();
                String repetir_pass = input_repetir_pass.getText().toString();


                NegocioUsuario negociousuario = new NegocioUsuario(getActivity());
                if(!nombre.isEmpty() && !apellido.isEmpty() && !usuario.isEmpty() && !email.isEmpty() && !pass.isEmpty() && !repetir_pass.isEmpty()){
                    if(pass.equals(repetir_pass)){
                        Usuario nuevoUsuario = new Usuario(nombre, apellido, usuario, email, pass);
                        if(negociousuario.registrarUsuario(nuevoUsuario)) {
                            Toast.makeText(getActivity(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                            // Redirige a la pantalla de LogInFragment
                            requireActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, new LogInFragment())
                                    .addToBackStack(null) // Esto permite regresar al fragmento anterior
                                    .commit();
                        }
                        else{
                            Toast.makeText(getActivity(), "El usuario ya existe", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
                }
            }



        });


        tv_Link_LogIn.setOnClickListener(v -> {
            // Redirige a la pantalla de LogInFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LogInFragment())
                    .addToBackStack(null) // Esto permite regresar al fragmento anterior
                    .commit();

        });

        return view;
    }
}