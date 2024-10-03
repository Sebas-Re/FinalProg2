package com.example.finalprog2.entidad;

import java.util.Objects;

public class Electrodomestico {
    private int id;
    private String marca;
    private String modelo;
    private String eficiencia;
    private int kwh;
    private boolean estado;

    public Electrodomestico() {
    }

    public Electrodomestico(int id, String marca, String modelo, String eficiencia, int kwh, boolean estado) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.eficiencia = eficiencia;
        this.kwh = kwh;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(String eficiencia) {
        this.eficiencia = eficiencia;
    }

    public int getKwh() {
        return kwh;
    }

    public void setKwh(int kwh) {
        this.kwh = kwh;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Electrodomestico{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", eficiencia='" + eficiencia + '\'' +
                ", kwh=" + kwh +
                ", estado=" + estado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Electrodomestico that = (Electrodomestico) o;
        return id == that.id && kwh == that.kwh && estado == that.estado && Objects.equals(marca, that.marca) && Objects.equals(modelo, that.modelo) && Objects.equals(eficiencia, that.eficiencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, marca, modelo, eficiencia, kwh, estado);
    }
}
