package com.example.finalprog2.fragment;

import android.content.Intent;
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


public class LogInFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        Button btn_login = view.findViewById(R.id.btn_login);
        TextView tv_pass_olvidada = view.findViewById(R.id.tv_pass_olvidada);
        TextView tv_registro_usuario = view.findViewById(R.id.tv_registro_usuario);


        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // implementar lógica para iniciar sesion
                EditText input_usuario = view.findViewById(R.id.input_usuario);
                EditText input_pass = view.findViewById(R.id.input_pass);
                String usuario = input_usuario.getText().toString();
                String pass = input_pass.getText().toString();

                Toast.makeText(getActivity(), "Iniciar Sesion", Toast.LENGTH_SHORT).show();
                NegocioUsuario negociousuario = new NegocioUsuario(getActivity());
                if(!usuario.isEmpty() && !pass.isEmpty()){

                    Usuario nuevologuser = new Usuario(usuario, null, pass);
                    if(negociousuario.verificarUsuario(nuevologuser)){
                        Toast.makeText(getActivity(), "Bienvenido", Toast.LENGTH_SHORT).show();


                    }else{
                        Toast.makeText(getActivity(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Por favor ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
                }


            }

        });

        tv_pass_olvidada.setOnClickListener(v -> {
            // Reemplazar el fragmento actual por RegistroUsuarioFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new RecuperarPassFragment()) // Usa el ID correcto de tu contenedor de fragmentos
                    .addToBackStack(null) // Esto permite regresar al fragmento anterior
                    .commit();
        });

        tv_registro_usuario.setOnClickListener(v -> {
            // Reemplazar el fragmento actual por RegistroUsuarioFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new CrearForoFragment()) // Usa el ID correcto de tu contenedor de fragmentos
                    .addToBackStack(null) // Esto permite regresar al fragmento anterior
                    .commit();
        });

     return view;
    }
}