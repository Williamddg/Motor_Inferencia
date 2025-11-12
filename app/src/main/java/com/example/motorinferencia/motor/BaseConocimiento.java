package com.example.motorinferencia.motor;

import java.util.ArrayList;
import java.util.List;

public class BaseConocimiento {
    private static BaseConocimiento instancia;
    private List<Regla> reglas;

    private BaseConocimiento() {
        reglas = new ArrayList<>();
    }

    public static BaseConocimiento getInstancia() {
        if (instancia == null) instancia = new BaseConocimiento();
        return instancia;
    }

    public void agregarRegla(Regla r) {
        reglas.add(r);
    }

    public List<Regla> getReglas() {
        return reglas;
    }

    public Regla buscarRegla(String nombre) {
        for (Regla r : reglas) {
            if (r.getNombre().equalsIgnoreCase(nombre)) {
                return r;
            }
        }
        return null;
    }
}
