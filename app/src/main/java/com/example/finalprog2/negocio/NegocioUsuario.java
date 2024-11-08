package com.example.finalprog2.negocio;

import android.content.Context;

import androidx.annotation.OptIn;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import com.example.finalprog2.entidad.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;

public class NegocioUsuario {


    public NegocioUsuario(Context context){

    }

    @OptIn(markerClass = UnstableApi.class)
    public boolean verificarUsuario(Usuario usuario) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final boolean[] verificacionExitosa = {false};

        db.collection("usuarios")
                .whereEqualTo("usuario", usuario.getUsuario())
                .whereEqualTo("pass", usuario.getPass())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            Log.d("Firestore", "Usuario encontrado");
                            verificacionExitosa[0] = true;
                            // Aquí puedes realizar acciones adicionales, como navegar a otra actividad
                        } else {
                            Log.w("Firestore", "Nombre de usuario o contraseña incorrectos");
                            verificacionExitosa[0] = false;
                            // Aquí puedes mostrar un mensaje de error al usuario
                        }
                    } else {
                        Log.w("Firestore", "Error al verificar LogIn", task.getException());
                        verificacionExitosa[0] = false;
                        // Aquí puedes mostrar un mensaje de error al usuario
                    }
                });

        // **Importante:** Debido a la naturaleza asíncrona,este valor de retorno no es confiable
        return verificacionExitosa[0];
    }

    public boolean verificarEmail(String email){
        //Busca el email en sistema. Si existe:
        // 1. Envia un codigo de recuperacion de contraseña al email
        // 2. Se obtiene el usuario correspondiente al email (funcion obtenerUsuario)
        // 3. Se almacena el codigo en la base de datos (campo "token" en clase Usuario)
        // 4. Se modifica el usuario en la base de datos (funcion modificarUsuario) para aplicar el token
        // devuelve true.

        // Si no existe, devuelve false
        return true;
    }

    @OptIn(markerClass = UnstableApi.class)
    public boolean registrarUsuario(Usuario nuevoUsuario) {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); //instanciado de firebase
        final boolean[] registrationSuccessful = {false}; // Flag para trackear el estado del registro

        db.collection("usuarios")
                .whereEqualTo("usuario", nuevoUsuario.getUsuario()).whereEqualTo("email", nuevoUsuario.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            db.collection("usuarios")
                                    .add(nuevoUsuario)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d("Firestore", "Usuario agregado con ID: " + documentReference.getId());
                                        registrationSuccessful[0] = true; // settea la flag a true en caso de exito
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("Firestore", "Error agregando usuario", e);
                                        registrationSuccessful[0] = false; // Settea la flag a false en caso de error
                                    });
                        } else {
                            Log.w("Firestore", "El nombre de usuario o el email ya están en uso");
                            registrationSuccessful[0] = false;
                        }
                    } else {
                        Log.w("Firestore", "Error al verificar usuario o email", task.getException());
                        registrationSuccessful[0] = false;
                    }
                });

        return registrationSuccessful[0]; // Retorna el estado del registro
    }



    public boolean modificarUsuario(Usuario usuario) {
        // Busqueda SQL para modificar el usuario en la base de datos, con los datos del usuario

        Usuario usuarioAmodificar = ObtenerUsuario(usuario.getId());

        // Si la contra recibida es nula,
        // es porque el usuario no quiere cambiarla
        if(usuario.getPass() == null){
            usuarioAmodificar.setNombre(usuario.getNombre());
            usuarioAmodificar.setApellido(usuario.getApellido());
            usuarioAmodificar.setUsuario(usuario.getUsuario());
            usuarioAmodificar.setEmail(usuario.getEmail());
        }
        else{
            usuarioAmodificar.setNombre(usuario.getNombre());
            usuarioAmodificar.setApellido(usuario.getApellido());
            usuarioAmodificar.setUsuario(usuario.getUsuario());
            usuarioAmodificar.setEmail(usuario.getEmail());
            usuarioAmodificar.setPass(usuario.getPass());
        }

        return true;
    }

    private Usuario ObtenerUsuario(int id) {
        // Recibe ID, retorna usuario
        Usuario usuarioEncontrado = new Usuario();
        return usuarioEncontrado;
    }


    public Usuario obtenerUsuario(String email){
        // Recibe un objeto usuario con el campo email cargado y devolvería el usuario correspondiente a ese email
        Usuario usuario = new Usuario();

        usuario.setEmail(email);

        // Lógica para obtener el usuario de la base de datos

        return usuario;
    }

    public boolean verificarCodigo(String email, int codigo) {
        // En este paso, el email ya fue verificado, por lo que no es necesario verificar nuevamente

        // Se busca al usuario en la base de datos a partir del email (funcion obtenerUsuario)
        Usuario usuario = obtenerUsuario(email);

        // Se verifica el código
         if(usuario.getToken() == codigo){
             return true;
         }
         else{
             return false;
         }
    }

    public boolean cambiarPass(String email, String pass) {
        // En este paso, el email ya fue verificado, por lo que no es necesario verificar nuevamente
        // Lógica para cambiar la contraseña
        // Buscaría al usuario en la base de datos a partir del email y actualizaría su contraseña

        Usuario usuario = obtenerUsuario(email);
        usuario.setPass(pass);
        modificarUsuario(usuario);

        return true;
    }
}
