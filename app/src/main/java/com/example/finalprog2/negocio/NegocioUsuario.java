package com.example.finalprog2.negocio;

import android.content.Context;


import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import com.example.finalprog2.entidad.Usuario;
import com.example.finalprog2.interfaces.LogInCallback;
import com.example.finalprog2.interfaces.ObtenerUsuarioCallback;
import com.example.finalprog2.interfaces.RegistrationCallback;
import com.example.finalprog2.interfaces.VerificarEmailCallback;
import com.example.finalprog2.interfaces.VerificarTokenCallback;
import com.example.finalprog2.interfaces.updateUsuarioCallback;
import com.example.finalprog2.utils.EmailController;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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
                                Log.i("Firestore", "Usuario encontrado");
                                callback.onSuccess();


                            } else {
                                Log.w("Firestore", "Nombre de usuario incorrecto");
                                callback.onFailure(null);
                            }
                        } else {
                            Log.e("Firestore", "Error al verificar LogIn", task.getException());
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
                                                                            Log.i("Firestore", "Datos de usuario añadidos exitosamente");
                                                                            callback.onSuccess();
                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                            // Error añadiendo datos del usuario
                                                                            Log.e("Firestore", "Error añadiendo datos del usuario", e);
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
                                            Log.e("Firestore", "Error al verificar email", emailTask.getException());
                                            callback.onFailure(null);
                                        }
                                    });
                        }

                    } else {
                        Log.e("Firestore", "Error al verificar usuario", chequeoUsuarioTask.getException());
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

        db.collection("usuarios")
                .whereEqualTo("usuario", usuario.getUsuario()) // Busca el documento a partir del nombre de usuario
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Obtenemos la referencia del documento
                            DocumentReference docRef = task.getResult().getDocuments().get(0).getReference();

                            // mandamos la update
                            docRef.update(updates)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.i("Firestore", "Datos de usuario actualizados exitosamente");
                                        callback.onSuccess();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "Error actualizando los datos del usuario " + usuario.getUsuario(), e);
                                        callback.onFailure(null);
                                    });
                        } else {

                            Log.w("Firestore", "No se encontró un usuario con nombre de usuario proporcionado");

                        }
                    } else {

                        Log.e("Firestore", "Error al buscar el usuario", task.getException());

                    }
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
            updates.put("token", usuario.getToken());

        }
        else{
            updates.put("nombre", usuario.getNombre());
            updates.put("apellido", usuario.getApellido());
            updates.put("usuario", usuario.getUsuario());
            updates.put("email", usuario.getEmail());
            updates.put("pass", usuario.getPass());
            updates.put("token", usuario.getToken());
        }
        return updates;
    }



    // Esta funcion es util en el fragment EditarPerfil, puesto que cargaría la vista
    // a partir del objeto usuario que retorna esta funcion
    @OptIn(markerClass = UnstableApi.class)
    public void ObtenerUsuario(Usuario usuarioAbuscar, ObtenerUsuarioCallback callback) {
        // Recibe Nombre de Usuario, retorna usuario
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Buscaría al usuario en la base de datos a partir del nombre de usuario
        db.collection("usuarios")
                .whereEqualTo("usuario", usuarioAbuscar.getUsuario())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            Log.i("Firestore", "Usuario encontrado");

                            //obtiene el documento
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            Usuario usuarioEncontrado = document.toObject(Usuario.class);
                            callback.onSuccess(usuarioEncontrado);

                        } else {
                            Log.w("Firestore", "ID incorrecto");
                        }
                    } else {
                        Log.e("Firestore", "Error al obtener usuario", task.getException());

                    }
                });
    }


    @OptIn(markerClass = UnstableApi.class)
    public void obtenerUsuario(String email, ObtenerUsuarioCallback callback){
        //recibe email, retorna objeto usuario
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Buscaría al usuario en la base de datos a partir del email

        db.collection("usuarios").whereEqualTo("email", email).get().addOnCompleteListener(task ->
        {

            if (task.isSuccessful())
            {
                if (!task.getResult().isEmpty()) {
                    Log.i("Firestore", "Usuario encontrado a partir de email (ObtenerUsuario(String email) )");

                    //obtiene el documento
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    Usuario usuarioEncontrado = document.toObject(Usuario.class);

                    callback.onSuccess(usuarioEncontrado);


                } else {
                    Log.w("Firestore", "Email incorrecto");
                    callback.onFailure(new Exception("Email incorrecto"));
                }

            } else
                {
                  Log.e("Firestore", "Error al obtener Usuario", task.getException());
                }
        });
    }


    // Funcion para autogenerar un token numerico de 6 digitos

    public String generarToken(){
        String token = "";
        for (int i = 0; i < 6; i++) {
            token += (int)(Math.random() * 10);
        }
        return token;
    }

    @OptIn(markerClass = UnstableApi.class)
    public void verificarEmail(Usuario usuario, VerificarEmailCallback callback)
    {
        //Busca el email en sistema. Si existe devuelve success, si no devuelve fail.
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Buscaría al usuario en la base de datos a partir del email
        db.collection("usuarios").whereEqualTo("email", usuario.getEmail()).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
            {

                if (!task.getResult().isEmpty()) {
                    Log.d("Firestore", "Email encontrado (Funcion VerificarEmail");
                    callback.onSuccess();
                } else {
                    Log.w("Firestore", "Email incorrecto");
                    callback.onFailure(null);
                }
            } else
                {
                Log.e("Firestore", "Error al obtener Usuario", task.getException());
                callback.onFailure(null);
                }
        });
    }


    // Funcion para enviar Token por email
    public void enviarToken(Usuario usuario, updateUsuarioCallback callback){

        int tokenGenerado = Integer.parseInt(generarToken());
        obtenerUsuario(usuario.getEmail(), new ObtenerUsuarioCallback() {
            @Override
            public boolean onSuccess(Usuario usuarioEncontrado) {
                usuarioEncontrado.setToken(tokenGenerado);

                modificarUsuario(usuarioEncontrado, new updateUsuarioCallback()
                {
                    @OptIn(markerClass = UnstableApi.class)
                    @Override
                    public void onSuccess() {
                        Log.i("Firestore", "Token añadido exitosamente");
                        //si el token se seteo exitosamente, se envia el email
                        EmailController.enviarEmailUsuario(usuario.getEmail(), "Recuperación de contraseña", "Tu código de recuperación es: " + tokenGenerado);
                        callback.onSuccess();
                    }

                    @OptIn(markerClass = UnstableApi.class)
                    @Override
                    public void onFailure(Exception e) {
                        Log.e("Firestore", "Error al añadir token", e);
                        callback.onFailure(null);
                    }
                });
                return false;
            }

            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onFailure(Exception e) {
                Log.e("Firestore", "Error al obtener Usuario", e);
                callback.onFailure(null);
            }
        });

    }




    // Funcion para verificar el token
    public void VerificarToken(Usuario usuarioAverificar, VerificarTokenCallback callback) {
        // En este paso, el email ya fue verificado, por lo que no es necesario verificar nuevamente

        // Se busca al usuario en la base de datos a partir del email (funcion obtenerUsuario)
        obtenerUsuario(usuarioAverificar.getEmail(), new ObtenerUsuarioCallback() {
            @OptIn(markerClass = UnstableApi.class)
            @Override
            public boolean onSuccess(Usuario usuarioEnDB) {
                // Se verifica el código
                Log.i("Firestore", "Verificando token");
                if (usuarioEnDB.getToken() == usuarioAverificar.getToken()){
                    callback.onSuccess();

                }
                return true;
            }

            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onFailure(Exception e) {
                Log.w("Firestore", "Error al obtener Usuario", e);
            }

        });
    }

    public void cambiarPass(Usuario usuarioAmodificar, updateUsuarioCallback callback) {
        // En este paso, el email ya fue verificado, por lo que no es necesario verificar nuevamente
        // Lógica para cambiar la contraseña
        // Buscaría al usuario en la base de datos a partir del email y actualizaría su contraseña

        //Se busca al usuario en la base de datos a partir del email (funcion obtenerUsuario),
        // y se almacenan los datos en un objeto
        obtenerUsuario(usuarioAmodificar.getEmail(), new ObtenerUsuarioCallback() {
            @Override
            public boolean onSuccess(Usuario usuarioEncontrado) {
                // Se modifica la contraseña del usuario segun lo ingresado
                usuarioEncontrado.setPass(usuarioAmodificar.getPass());
                //Se actualiza la contraseña del usuario en la base de datos (funcion modificarUsuario)
                modificarUsuario(usuarioEncontrado, callback);
                return true;
            }

            @OptIn(markerClass = UnstableApi.class)
            @Override
            public void onFailure(Exception e) {
                Log.e("Firestore", "Error al obtener Usuario", e);
            }

            });
    }
}
