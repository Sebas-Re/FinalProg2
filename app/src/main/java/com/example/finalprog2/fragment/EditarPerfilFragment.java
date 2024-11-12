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


        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String nombreUsuario = sharedPreferences.getString("usuario", null);
        int idUsuario = sharedPreferences.getInt("id", -1);
        if (nombreUsuario != null) {
            NegocioUsuario negocioUsuario = new NegocioUsuario(getActivity());
            Usuario usuarioAcargar = new Usuario();
            usuarioAcargar.setUsuario(nombreUsuario);
            negocioUsuario.ObtenerUsuario(usuarioAcargar, new ObtenerUsuarioCallback() {
                @Override
                public boolean onSuccess(Usuario usuarioEncontrado) {
                    input_nombre.setText(usuarioEncontrado.getNombre());
                    input_apellido.setText(usuarioEncontrado.getApellido());
                    input_usuario.setText(usuarioEncontrado.getUsuario());
                    input_email.setText(usuarioEncontrado.getEmail());
                    return true;
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getActivity(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getActivity(), "No se encontro el nombre de usuario", Toast.LENGTH_SHORT).show();
        }


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
            NegocioUsuario negocioUsuario = new NegocioUsuario(getActivity());
                if(pass.isEmpty() && repetir_pass.isEmpty()){
                 Usuario usuarioAeditar = new Usuario(nombre, apellido, usuario, email, null);
                 usuarioAeditar.setId(idUsuario);
                 // Manda el usuario con la contraseña nula, para que no se modifique
                    updateUsuario(negocioUsuario, usuarioAeditar);
                }
                else{
                    if(pass.equals(repetir_pass)){
                        Usuario usuarioAeditar = new Usuario(nombre, apellido, usuario, email, pass);
                        usuarioAeditar.setId(idUsuario);
                        updateUsuario(negocioUsuario, usuarioAeditar);
                    }
                    else{
                        Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });


       return view;
    }

    // Funcion para modificar los datos del usuario
    private void updateUsuario(NegocioUsuario negocioUsuario, Usuario usuarioAeditar) {
        negocioUsuario.modificarUsuario(usuarioAeditar, new updateUsuarioCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(), "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity(), "Error al modificar los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }



}