package com.example.finalprog2.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.finalprog2.interfaces.RegistrationCallback;
import com.example.finalprog2.interfaces.ValidarRegistroCallback;
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
        //Crea el objeto builder para los popup de error
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        btn_registro_usuario.setOnClickListener(v -> {
            String nombre = input_nombre.getText().toString().trim();
            String apellido = input_apellido.getText().toString().trim();
            String usuario = input_usuario.getText().toString().trim();
            String email = input_email.getText().toString().trim();
            String pass = input_pass.getText().toString().trim();
            String repetir_pass = input_repetir_pass.getText().toString().trim();

            if(!nombre.isEmpty() && !apellido.isEmpty() && !usuario.isEmpty() && !email.isEmpty() && !pass.isEmpty() && !repetir_pass.isEmpty()){
                if(pass.equals(repetir_pass)){
                    if(passValida(pass)){
                        Usuario nuevoUsuario = new Usuario(nombre, apellido, usuario, email, pass);
                        nuevoUsuario.setEstado(false);
                        NegocioUsuario negociousuario = new NegocioUsuario(getActivity());
                        //Carga de usuario en sistema

                        negociousuario.validarRegistro(nuevoUsuario, new ValidarRegistroCallback() {
                            @Override
                            public void onSuccess() {

                                // guarda el objeto nuevoUsuario dentro de un bundle
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("DatosRegistro", nuevoUsuario);

                                // Redirige a la pantalla de ValidacionEmailFragment
                                ValidacionEmailFragment validacionEmailFragment = new ValidacionEmailFragment();
                                validacionEmailFragment.setArguments(bundle);


                                requireActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, validacionEmailFragment)
                                        .addToBackStack(null) // Esto permite regresar al fragmento anterior
                                        .commit();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                //Toast.makeText(getActivity(), "El usuario o email ya estan en uso", Toast.LENGTH_SHORT).show();
                                builder.setTitle("Error");
                                builder.setMessage("El usuario o email ya estan en uso");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Cierra el diálogo
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
                    }
                    else{
                        //Toast.makeText(getActivity(), "La contraseña no cumple con los requisitos", Toast.LENGTH_SHORT).show();
                        builder.setTitle("Error");
                        builder.setMessage("La contraseña no cumple con los requisitos");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Cierra el diálogo
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
                else{
                    //Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    builder.setTitle("Error");
                    builder.setMessage("Las contraseñas no coinciden");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Cierra el diálogo
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
            else{
                //Toast.makeText(getActivity(), "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show();
                builder.setTitle("Error");
                builder.setMessage("Por favor ingrese todos los campos");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cierra el diálogo
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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