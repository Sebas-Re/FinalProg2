package com.example.finalprog2.entidad;

import android.util.Log;

import com.example.finalprog2.conexion.DataDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Publicacion {
    private int id;
    private Usuario usuario;
    private String titulo;
    private String descripcion;
    private String relacionEnergetica;
    private boolean estado;

    public Publicacion() {}

    public Publicacion(int id, Usuario usuario, String titulo, String descripcion, String relacionEnergetica, boolean estado) {
        this.id = id;
        this.usuario = usuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.relacionEnergetica = relacionEnergetica;
        this.estado = estado;
    }

    // Métodos getter y setter
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setRelacionEnergetica(String relacionEnergetica) {
        this.relacionEnergetica = relacionEnergetica;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // Método para guardar la publicación en la base de datos
    public void guardarPublicacion() {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // Cargar el driver JDBC
            Class.forName("com.mysql.jdbc.Driver");

            // Establecer la conexión
            connection = DriverManager.getConnection(DataDB.urlMySQL, DataDB.user, DataDB.pass);

            // Consulta SQL para insertar la publicación
            //Despues agregar usuario para tener todo mas prolijo
            String query = "INSERT INTO publicaciones (titulo, descripcion, relacionEnergetica, estado) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, this.titulo);
            statement.setString(2, this.descripcion);
            statement.setString(3, this.relacionEnergetica);
            statement.setBoolean(4, this.estado);

            // Ejecutar la consulta
            int result = statement.executeUpdate();

            if (result > 0) {
                Log.d("Publicacion", "Publicación insertada correctamente");
            } else {
                Log.d("Publicacion", "Error al insertar la publicación");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("Publicacion", "Error al conectar con la base de datos", e);
        } finally {
            // Cerrar los recursos
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
