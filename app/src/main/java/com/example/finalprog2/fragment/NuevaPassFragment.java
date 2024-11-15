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
import android.widget.Toast;

import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Usuario;
import com.example.finalprog2.interfaces.ObtenerUsuarioCallback;
import com.example.finalprog2.interfaces.updateUsuarioCallback;
import com.example.finalprog2.negocio.NegocioUsuario;


public class NuevaPassFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nueva_pass, container, false);
        EditText inputPass = view.findViewById(R.id.input_pass);
        EditText inputRepetirPass = view.findViewById(R.id.input_repetir_pass);
        Button btnRecuPass = view.findViewById(R.id.btn_recu_pass);


        btnRecuPass.setOnClickListener(view1 -> {
            String pass = inputPass.getText().toString();
            String repetirPass = inputRepetirPass.getText().toString();

            if(!pass.isEmpty() && !repetirPass.isEmpty()){
                if(pass.equals(repetirPass)){
                    if(passValida(pass)) {

                        // Obtener el email del bundle
                        Bundle bundle = getArguments();
                        assert bundle != null;
                        String email = bundle.getString("email");

                        NegocioUsuario negocioUsuario = new NegocioUsuario(getActivity());
                        // En este paso, tanto el email como el token ya fueron verificados,
                        // por lo que no es necesario verificar nuevamente


                        Usuario usuarioAmodificar = new Usuario();
                        usuarioAmodificar.setEmail(email);
                        usuarioAmodificar.setPass(pass);

                        negocioUsuario.cambiarPass(usuarioAmodificar, new updateUsuarioCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getActivity(), "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
                                negocioUsuario.obtenerUsuario(email, new ObtenerUsuarioCallback() {
                                    @Override
                                    public boolean onSuccess(Usuario usuarioEncontrado) {

                                        // Guardar el nombre de usuario en SharedPreferences
                                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("usuario", usuarioEncontrado.getUsuario());
                                        editor.apply();

                                        // Redireccionar a la pantalla de inicio de sesión
                                        // Redirige a la pantalla de LogInFragment
                                        LogInFragment logInFragment = new LogInFragment();
                                        requireActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container, logInFragment)
                                                .addToBackStack(null) // Esto permite regresar al fragmento anterior
                                                .commit();
                                        return true;
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(getActivity(), "Error al obtener el usuario", Toast.LENGTH_SHORT).show();
                                    }
                                    });
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getActivity(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(getActivity(), "La contraseña debe tener al menos 8 caracteres, una letra y un número", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getActivity(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            }


        });

        return view;
    }

    private boolean passValida(String pass) {
        // Verificar que la contraseña tenga al menos 8 caracteres
        if (pass.length() < 8) {
            return false;
        }

        // Uso de expresiones regulares para comprobar letras y números
        boolean tieneLetras = false;
        boolean tieneDigitos = false;

        for (char c : pass.toCharArray()) {
            if (Character.isLetter(c)) {
                tieneLetras = true;
            }
            if (Character.isDigit(c)) {
                tieneDigitos = true;
            }
            if (tieneLetras && tieneDigitos) {
                return true; // Si tiene ambos, la contraseña es válida
            }
        }

        return false; // Si no cumple los requisitos
    }

}