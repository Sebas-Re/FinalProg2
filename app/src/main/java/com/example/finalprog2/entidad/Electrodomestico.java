package com.example.finalprog2.entidad;

import java.util.Map;
import java.util.Objects;

public class Electrodomestico {
    private int id;
    private String tipo;
    private Map<String, Object> eficiencia; // Mapa que contendrá "kwh" y "descripcion"

    public Electrodomestico() {
    }

    public Electrodomestico(int id, String tipo, Map<String, Object> eficiencia) {
        this.id = id;
        this.tipo = tipo;
        this.eficiencia = eficiencia;
    }

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Map<String, Object> getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(Map<String, Object> eficiencia) {
        this.eficiencia = eficiencia;
    }

    @Override
    public String toString() {
        return "Electrodomestico{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", eficiencia=" + eficiencia +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Electrodomestico that = (Electrodomestico) o;
        return id == that.id && Objects.equals(tipo, that.tipo) && Objects.equals(eficiencia, that.eficiencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo, eficiencia);
    }
}
