package com.example.finalprog2.negocio;

import android.content.Context;

import com.example.finalprog2.entidad.Usuario;

public class NegocioUsuario {


    public NegocioUsuario(Context context){

    }

    public boolean verificarUsuario(Usuario usuario){
        //Recibe un objeto usuario (Con el campo usuario y pass) y verifica si existe en la base de datos
        // Si existe, devuelve true
        // Esta funcion forma parte del LogIn
        return false;
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

    public boolean registrarUsuario(Usuario usuario){
        //Logica para añadir un usuario en BD


        return false;
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
