package com.example.finalprog2.negocio;

import android.content.Context;
import android.content.SharedPreferences;


import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import com.example.finalprog2.entidad.Usuario;
import com.example.finalprog2.interfaces.LogInCallback;
import com.example.finalprog2.interfaces.ObtenerUsuarioCallbak;
import com.example.finalprog2.interfaces.RegistrationCallback;
import com.example.finalprog2.interfaces.updateUsuarioCallback;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class NegocioUsuario {


    public NegocioUsuario(Context context){

    }


    // Funcion de LogIn
    @OptIn(markerClass = UnstableApi.class)
    public void verificarUsuario(Usuario usuario, LogInCallback callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        if(usuario.getEmail() == null){
            //Inicia sesion a traves del usuario

            db.collection("usuarios")
                    .whereEqualTo("usuario", usuario.getUsuario())
                    .whereEqualTo("pass", usuario.getPass())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                Log.d("Firestore", "Usuario encontrado");
                                callback.onSuccess();


                            } else {
                                Log.w("Firestore", "Nombre de usuario incorrecto");
                                callback.onFailure(null);
                            }
                        } else {
                            Log.w("Firestore", "Error al verificar LogIn", task.getException());
                            callback.onFailure(null);

                        }
                    });
        }
        else{
            mAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getPass())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d("Firebase", "signInWithEmail:success");
                            callback.onSuccess();
                        } else {
                            // Sign in failed
                            Log.w("Firebase", "signInWithEmail:failure", task.getException());
                            callback.onFailure(null);
                        }
                    });
        }
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
    public void registrarUsuario(Usuario nuevoUsuario, RegistrationCallback callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance(); //instanciado de firebase Auth
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios")
                .whereEqualTo("usuario", nuevoUsuario.getUsuario())
                .get()
                .addOnCompleteListener(chequeoUsuarioTask -> {
                    if (chequeoUsuarioTask.isSuccessful()) {
                        if (!chequeoUsuarioTask.getResult().isEmpty()) {
                            // El usuario ya existe
                            Log.w("Firestore", "El nombre de usuario ya está en uso");
                            callback.onFailure(null);
                        }
                        else{
                            //Caso contrario, continua con la ejecucion del codigo

                            //Chequeo de Email
                            db.collection("usuarios")
                                    .whereEqualTo("email", nuevoUsuario.getEmail())
                                    .get()
                                    .addOnCompleteListener(emailTask -> {
                                        if (emailTask.isSuccessful()) {
                                            if (!emailTask.getResult().isEmpty()) {
                                                // El email ya está en uso
                                                Log.w("Firestore", "El email ya está en uso");
                                                callback.onFailure(null);
                                            }
                                            else{
                                                // Email no esta en uso
                                                mAuth.createUserWithEmailAndPassword(nuevoUsuario.getEmail(), nuevoUsuario.getPass())
                                                        .addOnCompleteListener(task -> {
                                                            if (task.isSuccessful()) {
                                                                // Sign up success, updateUI with the signed-in user's information
                                                                Log.d("Firebase", "createUserWithEmail:success");
                                                                FirebaseUser user = mAuth.getCurrentUser();

                                                                assert user != null;
                                                                // Crea un mapa con los datos del usuario registrado
                                                                Map<String, Object> userData = mapeoUsuarioAregistrar(nuevoUsuario, user);

                                                                // Guarda los datos del usuario en la base de datos

                                                                db.collection("usuarios").document(user.getUid()).set(userData)
                                                                        .addOnSuccessListener(aVoid -> {
                                                                            // Datos de usuario añadidos exitosamente
                                                                            Log.d("Firestore", "Datos de usuario añadidos exitosamente");
                                                                            callback.onSuccess();
                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                            // Error añadiendo datos del usuario
                                                                            Log.w("Firestore", "Error añadiendo datos del usuario", e);
                                                                            callback.onFailure(e);
                                                                        });
                                                            } else {
                                                                // Si el registro falla, pone la flag en false
                                                                Log.w("Firebase", "createUserWithEmail:failure", task.getException());
                                                                callback.onFailure(null);
                                                            }
                                                        });
                                            }

                                        }
                                        else {
                                            Log.w("Firestore", "Error al verificar email", emailTask.getException());
                                            callback.onFailure(null);
                                        }
                                    });
                        }

                    } else {
                        Log.w("Firestore", "Error al verificar usuario", chequeoUsuarioTask.getException());
                        callback.onFailure(null);
                    }
                });
    }

    private static @NonNull Map<String, Object> mapeoUsuarioAregistrar(Usuario nuevoUsuario, FirebaseUser user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", Math.abs(user.getUid().hashCode()));
        userData.put("nombre", nuevoUsuario.getNombre());
        userData.put("apellido", nuevoUsuario.getApellido());
        userData.put("usuario", nuevoUsuario.getUsuario());
        userData.put("email", nuevoUsuario.getEmail());
        userData.put("pass", nuevoUsuario.getPass());
        userData.put("token", nuevoUsuario.getToken());
        userData.put("estado", nuevoUsuario.isEstado());
        return userData;
    }


    @OptIn(markerClass = UnstableApi.class)
    public void modificarUsuario(Usuario usuario, updateUsuarioCallback callback) {
        // Busqueda SQL para modificar el usuario en la base de datos, con los datos del usuario
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> updates = mapeoUsuarioAmodificar(usuario);

        db.collection("usuarios").document(String.valueOf(usuario.getId())).update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Datos actualizados correctamente
                    Log.d("Firestore", "Datos de usuario actualizados exitosamente");
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    // Error actualizando los datos del usuario
                    Log.w("Firestore", "Error actualizando los datos del usuario", e);
                    callback.onFailure(null);

                });

    }

    private static @NonNull Map<String, Object> mapeoUsuarioAmodificar(Usuario usuario) {
        Map<String, Object> updates = new HashMap<>();
        // Si la contra recibida es nula,
        // es porque el usuario no quiere cambiarla
        if(usuario.getPass() == null){
            updates.put("nombre", usuario.getNombre());
            updates.put("apellido", usuario.getApellido());
            updates.put("usuario", usuario.getUsuario());
            updates.put("email", usuario.getEmail());

        }
        else{
            updates.put("nombre", usuario.getNombre());
            updates.put("apellido", usuario.getApellido());
            updates.put("usuario", usuario.getUsuario());
            updates.put("email", usuario.getEmail());
            updates.put("pass", usuario.getPass());
        }
        return updates;
    }



    // Esta funcion es util en el fragment EditarPerfil, puesto que cargaría la vista
    // a partir del objeto usuario que retorna esta funcion
    @OptIn(markerClass = UnstableApi.class)
    public Usuario ObtenerUsuario(Usuario usuarioAbuscar, ObtenerUsuarioCallbak callback) {

        // Recibe Nombre de Usuario, retorna usuario
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Usuario[] usuarioEncontrado = {new Usuario()};


        // Buscaría al usuario en la base de datos a partir del nombre de usuario
        db.collection("usuarios")
                .whereEqualTo("usuario", usuarioAbuscar.getUsuario())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            Log.d("Firestore", "Usuario encontrado");

                            //obtiene el documento
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            usuarioEncontrado[0] = document.toObject(Usuario.class);

                        } else {
                            Log.w("Firestore", "ID incorrecto");
                        }
                    } else {
                        Log.w("Firestore", "Error al obtener usuario", task.getException());

                    }
                });
        return usuarioEncontrado[0];
    }


    @OptIn(markerClass = UnstableApi.class)
    public Usuario obtenerUsuario(String email){
        //recibe email, retorna objeto usuario
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Usuario[] usuario = {new Usuario()};
        // Buscaría al usuario en la base de datos a partir del email

        db.collection("usuarios")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            Log.d("Firestore", "Usuario encontrado");

                            //obtiene el documento
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            usuario[0] = document.toObject(Usuario.class);


                        } else {
                            Log.w("Firestore", "Email incorrecto");
                        }
                    } else {
                        Log.w("Firestore", "Error al obtener Usuario", task.getException());

                    }
                });

        return usuario[0];
    }

    public boolean verificarCodigo(String email, int codigo) {
        // En este paso, el email ya fue verificado, por lo que no es necesario verificar nuevamente

        // Se busca al usuario en la base de datos a partir del email (funcion obtenerUsuario)
        Usuario usuario = obtenerUsuario(email);

        // Se verifica el código
        return usuario.getToken() == codigo;
    }

    public void cambiarPass(Usuario usuarioAmodificar, updateUsuarioCallback callback) {
        // En este paso, el email ya fue verificado, por lo que no es necesario verificar nuevamente
        // Lógica para cambiar la contraseña
        // Buscaría al usuario en la base de datos a partir del email y actualizaría su contraseña

        //Se busca al usuario en la base de datos a partir del email (funcion obtenerUsuario),
        // y se almacenan los datos en un objeto
        Usuario usuario = obtenerUsuario(usuarioAmodificar.getEmail());

        // Se modifica la contraseña del usuario segun lo ingresado
        usuario.setPass(usuarioAmodificar.getPass());


        //Se actualiza la contraseña del usuario en la base de datos (funcion modificarUsuario)
        modificarUsuario(usuario, callback);

    }
}
