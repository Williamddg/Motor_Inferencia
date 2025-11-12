package com.example.motorinferencia.motor;

import java.util.ArrayList;
import java.util.List;

public class Regla {
    private String nombre;
    private List<Condicion> condiciones;
    private String resultado;

    public Regla(String nombre) {
        this.nombre = nombre;
        this.condiciones = new ArrayList<>();
    }

    public void agregarCondicion(Condicion c) {
        condiciones.add(c);
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Condicion> getCondiciones() {
        return condiciones;
    }

    @Override
    public String toString() {
        return "SI " + condiciones + " ENTONCES " + resultado;
    }
}
