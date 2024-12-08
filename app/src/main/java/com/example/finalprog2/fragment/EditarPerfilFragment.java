package com.example.finalprog2.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Usuario;
import com.example.finalprog2.interfaces.ObtenerUsuarioCallback;
import com.example.finalprog2.interfaces.updateUsuarioCallback;
import com.example.finalprog2.negocio.NegocioUsuario;
import com.example.finalprog2.utils.PopupMenuHelper;


public class EditarPerfilFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editar_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inflate the layout for this fragment
        EditText input_nombre = view.findViewById(R.id.input_nombre);
        EditText input_apellido = view.findViewById(R.id.input_apellido);
        TextView tv_usuario = view.findViewById(R.id.tv_usuario);
        TextView tv_email = view.findViewById(R.id.tv_email);
        EditText input_pass = view.findViewById(R.id.input_pass);
        EditText input_repetir_pass = view.findViewById(R.id.input_repetir_pass);
        Button btn_guardarCambios = view.findViewById(R.id.btn_guardarCambios);


        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("EditarPerfil");
        }

        //Ocultar el nombre de la app
        toolbar.setTitle("");

        // Configuración del botón de menú en el Toolbar
        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button);
        leftMenuButton.setTag("left");
        leftMenuButton.setOnClickListener(v -> {
            PopupMenuHelper.showPopupMenu(getContext(), leftMenuButton, requireActivity());
        });

        // Configuracion del boton perfil
        ImageButton rightUserButton = view.findViewById(R.id.right_user_button);
        rightUserButton.setTag("right");
        rightUserButton.setOnClickListener(v -> {
            //navigateToFragment(new EditarPerfilFragment());
            PopupMenuHelper.showPopupMenu(getContext(), rightUserButton, requireActivity());
        });

                //Crea el objeto builder para los popup de error
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Carga de datos al iniciar el fragment
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
                    tv_usuario.setText(usuarioEncontrado.getUsuario());
                    tv_email.setText(usuarioEncontrado.getEmail());
                    return true;
                }

                @Override
                public void onFailure(Exception e) {
                    //Toast.makeText(getActivity(), "Error al cargar los datos", Toast.LENGTH_SHORT).show();
                    popupmsg("Error","Error al cargar los datos");
                }
            });
        }
        else{
            //Toast.makeText(getActivity(), "No se encontro el nombre de usuario", Toast.LENGTH_SHORT).show();
            popupmsg("Error","No se encontro el nombre de usuario");
        }


        btn_guardarCambios.setOnClickListener(view1 -> {
        String nombre = input_nombre.getText().toString();
        String apellido = input_apellido.getText().toString();
        String usuario = tv_usuario.getText().toString();
        String email = tv_email.getText().toString();
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
                    if(passValida(pass)) {
                        Usuario usuarioAeditar = new Usuario(nombre, apellido, usuario, email, pass);
                        usuarioAeditar.setId(idUsuario);
                        updateUsuario(negocioUsuario, usuarioAeditar);
                    }
                    else{
                        //Toast.makeText(getActivity(), "La contraseña no cumple con los requisitos", Toast.LENGTH_SHORT).show();
                        popupmsg("Error","La contraseña no cumple con los requisitos");
                    }
                }
                else{
                    //Toast.makeText(getActivity(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    popupmsg("Error","Las contraseñas no coinciden");

                }
            }


        });

    }

    private void cerrarSesion() {
        // Limpia las preferencias compartidas para eliminar datos del usuario
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Elimina todos los datos guardados
        editor.apply();

        // Navega a la actividad de inicio de sesión
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LogInFragment())
                .commit();

        Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
    }

    // Funcion para modificar los datos del usuario
    private void updateUsuario(NegocioUsuario negocioUsuario, Usuario usuarioAeditar) {
        negocioUsuario.modificarUsuario(usuarioAeditar, new updateUsuarioCallback() {
            @Override
            public void onSuccess() {
                //Toast.makeText(getActivity(), "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
                popupmsg("","Datos modificados correctamente");
                requireActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(Exception e) {
                 //Toast.makeText(getActivity(), "Error al modificar los datos", Toast.LENGTH_SHORT).show();
                popupmsg("Error","Error al modificar los datos");
            }
        });
    }


    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
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

    private void popupmsg(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);
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