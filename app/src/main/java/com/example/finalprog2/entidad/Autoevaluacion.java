package com.example.finalprog2.entidad;

import java.util.List;

public class Autoevaluacion {
    private int id;
    private int idusuario;
    private float valorkwh;
    private List<String> tipo;
    private List<Integer> kwh;

    public Autoevaluacion() {
    }
    public Autoevaluacion(int id, int idusuario, float valorkwh, List<String> tipo, List<Integer> kwh) {
        this.id = id;
        this.idusuario = idusuario;
        this.valorkwh = valorkwh;
        this.tipo = tipo;
        this.kwh = kwh;
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public float getValorkwh() {
        return valorkwh;
    }

    public void setValorkwh(float valorkwh) {
        this.valorkwh = valorkwh;
    }

    public List<String> getTipo() {
        return tipo;
    }

    public void setTipo(List<String> tipo) {
        this.tipo = tipo;
    }

    public List<Integer> getKwh() {
        return kwh;
    }

    public void setKwh(List<Integer> kwh) {
        this.kwh = kwh;
    }
}