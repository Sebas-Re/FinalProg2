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


        btnRecuPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String pass = inputPass.getText().toString();
                String repetirPass = inputRepetirPass.getText().toString();

                if(!pass.isEmpty() && !repetirPass.isEmpty()){
                    if(pass.equals(repetirPass)){
                        if(passValida(pass)) {

                            // Obtener el email del bundle
                            Bundle bundle = getArguments();
                            String email = null;
                            if (bundle != null) {
                                email = bundle.getString("email");
                            }

                            NegocioUsuario negocioUsuario = new NegocioUsuario(getActivity());
                            // En este paso, tanto el email como el token ya fueron verificados,
                            // por lo que no es necesario verificar nuevamente
                            if (negocioUsuario.cambiarPass(email, pass)) {
                                Toast.makeText(getActivity(), "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
                                Usuario usuario = negocioUsuario.obtenerUsuario(email);

                                // Redireccionar a la pantalla de inicio de sesión, pasando el usuario como argumento
                                Bundle bundleUsuario = new Bundle();
                                bundleUsuario.putString("usuario", usuario.getUsuario());

                                LogInFragment logInFragment = new LogInFragment();
                                logInFragment.setArguments(bundleUsuario);

                            } else {
                                Toast.makeText(getActivity(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }
                    else{
                        Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                }


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