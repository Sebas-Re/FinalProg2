package com.example.finalprog2.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Usuario;
import com.example.finalprog2.interfaces.VerificarEmailCallback;
import com.example.finalprog2.interfaces.VerificarTokenCallback;
import com.example.finalprog2.interfaces.updateUsuarioCallback;
import com.example.finalprog2.negocio.NegocioUsuario;


public class RecuperarPassFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recuperar_pass, container, false);

        EditText input_email = view.findViewById(R.id.input_email);
        Usuario usuario = new Usuario();
        EditText input_codigo = view.findViewById(R.id.input_codigo);
        Button btn_enviar_token = view.findViewById(R.id.btn_enviar_token);
        Button btn_recu_pass = view.findViewById(R.id.btn_recu_pass);


        btn_enviar_token.setOnClickListener(view12 -> {
            usuario.setEmail(input_email.getText().toString());

            if (!usuario.getEmail().isEmpty()) {
                NegocioUsuario negocioUsuario = new NegocioUsuario(getActivity());
                negocioUsuario.verificarEmail(usuario, new VerificarEmailCallback() {

                    @Override
                    public void onSuccess() {
                        negocioUsuario.enviarToken(usuario, new updateUsuarioCallback(){
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getActivity(), "Token enviado", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getActivity(), "Error al enviar token", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getActivity(), "Email inexistente", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                Toast.makeText(getActivity(), "Campo Email vacío", Toast.LENGTH_SHORT).show();
            }
        });


        //Este boton se tocaria una vez ingresado el token
        btn_recu_pass.setOnClickListener(view1 -> {

            if (!input_codigo.getText().toString().isEmpty()) {
                usuario.setToken(Integer.parseInt(input_codigo.getText().toString()));

                //si ambos campos no estan vacios
                if(!usuario.getEmail().isEmpty() && String.valueOf(usuario.getToken()).length() == 6) {

                    NegocioUsuario negocioUsuario = new NegocioUsuario(getActivity());

                    negocioUsuario.verificarEmail(usuario, new VerificarEmailCallback() {
                        @Override
                        public void onSuccess() {

                            negocioUsuario.VerificarToken(usuario, new VerificarTokenCallback() {
                                @Override
                                public void onSuccess() {
                                    // Redireccionar a la pantalla de nueva contraseña con el email como argumento
                                    Bundle args = new Bundle();
                                    args.putString("email", usuario.getEmail());
                                    NuevaPassFragment nuevaPassFragment = new NuevaPassFragment();
                                    nuevaPassFragment.setArguments(args);
                                    requireActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.fragment_container, nuevaPassFragment)
                                            .addToBackStack(null) // Esto permite regresar al fragmento anterior
                                            .commit();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast.makeText(getActivity(), "Código incorrecto", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getActivity(), "Email inexistente", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    Toast.makeText(getActivity(), "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getActivity(), "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show();
            }


        });

        return view;
    }
}