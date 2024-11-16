package com.example.finalprog2.entidad;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Electrodomestico {
    private int id;
    private String tipo;
    private Map<String, Map<String, Integer>> eficiencia; // Cambiado a Map<String, Map<String, Integer>>
    private Long consumoPromedio; // Cambiado a Long para evitar problemas de conversión

    public Electrodomestico() {
    }

    public Electrodomestico(int id, String tipo, Map<String, Map<String, Integer>> eficiencia, Long consumoPromedio) {
        this.id = id;
        this.tipo = tipo;
        this.eficiencia = eficiencia;
        this.consumoPromedio = consumoPromedio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Map<String, Map<String, Integer>> getEficiencia() {
        return eficiencia;
    }

    public void setEficiencia(Map<String, Map<String, Integer>> eficiencia) {
        this.eficiencia = eficiencia;
    }

    public void setEficienciaPorHoras(String tipo, int kwh) {
        if (eficiencia == null) {
            eficiencia = new HashMap<>();
        }
        Map<String, Integer> detalleEficiencia = new HashMap<>();
        detalleEficiencia.put("kwh", kwh);
        eficiencia.put(tipo, detalleEficiencia);
    }

    public int getEficienciaPorHora(String tipo) {
        if (eficiencia != null && eficiencia.containsKey(tipo)) {
            Integer kwh = eficiencia.get(tipo).get("kwh");
            return kwh != null ? kwh : 0; // Si kwh es null, devuelve 0
        }
        return 0; // Devuelve 0 si no encuentra el tipo
    }

    public Long getconsumoPromedio() {
        return consumoPromedio;
    }

    public void setconsumoPromedio(Long consumoPromedio) {
        this.consumoPromedio = consumoPromedio;
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
