package com.example.finalprog2.fragment;


import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.finalprog2.interfaces.LogInCallback;
import com.example.finalprog2.interfaces.ObtenerUsuarioCallback;
import com.example.finalprog2.negocio.NegocioUsuario;
//import com.google.firebase.firestore.FirebaseFirestore;

public class LogInFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        Button btn_login = view.findViewById(R.id.btn_login);
        TextView tv_pass_olvidada = view.findViewById(R.id.tv_pass_olvidada);
        TextView tv_registro_usuario = view.findViewById(R.id.tv_registro_usuario);

        // Obtener el nombre de usuario de sharedPreferences

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = sharedPreferences.getString("usuario", null);

        //setea el nombre de usuario en el campo de texto
        if (nombreUsuario != null) {
            EditText input_usuarioEmail = view.findViewById(R.id.input_usuarioEmail);
            input_usuarioEmail.setText(nombreUsuario);
        }

        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // implementar lógica para iniciar sesion
                EditText input_usuarioEmail = view.findViewById(R.id.input_usuarioEmail);
                EditText input_pass = view.findViewById(R.id.input_pass);
                String usuarioEmail = input_usuarioEmail.getText().toString();
                String pass = input_pass.getText().toString();


                NegocioUsuario negociousuario = new NegocioUsuario(getActivity());
                if(!usuarioEmail.isEmpty() && !pass.isEmpty()){

                    Usuario nuevoLogUser = new Usuario(usuarioEmail, pass);
                    negociousuario.verificarUsuario(nuevoLogUser, new LogInCallback() {

                        @Override
                        public void onSuccess() {

                            // Si el nombre de usuario el nulo (porque se inicio sesion con email),
                            // se busca la info del usuario y se almacena en sharedPreferences
                            // Si el nombre de usuario no es nulo, se almacena en sharedPreferences
                            if (nuevoLogUser.getUsuario() == null){
                                NegocioUsuario buscarUsuario = new NegocioUsuario(getActivity());
                                buscarUsuario.obtenerUsuario(nuevoLogUser.getEmail(), new ObtenerUsuarioCallback() {
                                    @Override
                                    public boolean onSuccess(Usuario usuarioEncontrado) {
                                        // Guardar el nombre de usuario en SharedPreferences
                                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("usuario", usuarioEncontrado.getUsuario());
                                        editor.apply();

                                        return false;
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                    // Manejar el error si es necesario
                                    }

                                    });

                            }
                            else{
                                // Guardar el nombre de usuario en SharedPreferences
                                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("usuario", nuevoLogUser.getUsuario());
                                editor.apply();
                            }


                            Toast.makeText(getActivity(), "Bienvenido", Toast.LENGTH_SHORT).show();
                            //Redirige a la pantalla de autoeval
                            AutoEvalFragment autoEvalFragment = new AutoEvalFragment();
                            requireActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, autoEvalFragment)
                                    .addToBackStack(null) // Esto permite regresar al fragmento anterior
                                    .commit();

                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getActivity(), "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        }
                    });
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
                    .replace(R.id.fragment_container, new RegistroUsuarioFragment()) // Usa el ID correcto de tu contenedor de fragmentos
                    .addToBackStack(null) // Esto permite regresar al fragmento anterior
                    .commit();
        });

     return view;
    }
}