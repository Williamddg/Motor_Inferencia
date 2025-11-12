package com.example.motorinferencia.motor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Motor de inferencia con encadenamiento hacia adelante.
 * Usa la base de conocimiento (reglas) y hechos iniciales.
 */
public class MotorInferencia {

    private BaseConocimiento base;
    private Set<String> hechos; // hechos conocidos (memoria de trabajo)
    private List<String> trazas; // registro del razonamiento

    public MotorInferencia(BaseConocimiento base) {
        this.base = base;
        this.hechos = new HashSet<>();
        this.trazas = new ArrayList<>();
    }

    /** Agrega un hecho inicial (ejemplo: "temperatura = 30") */
    public void agregarHecho(String hecho) {
        hechos.add(hecho.trim().toLowerCase());
    }

    /** Ejecuta la inferencia (encadenamiento hacia adelante) */
    public List<String> ejecutar() {
        trazas.clear();

        boolean nuevaInferencia;
        int iteracion = 1;

        do {
            nuevaInferencia = false;
            trazas.add("🔁 Iteración " + iteracion + ":");
            iteracion++;

            for (Regla regla : base.getReglas()) {
                boolean cumpleTodas = true;

                for (Condicion c : regla.getCondiciones()) {
                    String expresion = (c.getAtributo() + " " + c.getOperador() + " " + c.getValor()).toLowerCase();

                    if (!verificarHecho(c)) {
                        cumpleTodas = false;
                        trazas.add("   ❌ No cumple: " + expresion);
                        break;
                    } else {
                        trazas.add("   ✅ Cumple: " + expresion);
                    }
                }

                if (cumpleTodas && !hechos.contains(regla.getResultado().toLowerCase())) {
                    hechos.add(regla.getResultado().toLowerCase());
                    trazas.add("   ➕ Nueva inferencia: " + regla.getResultado());
                    nuevaInferencia = true;
                }
            }

            trazas.add("-----------------------------");
        } while (nuevaInferencia);

        trazas.add("✅ Inferencia finalizada.");
        trazas.add("📘 Hechos finales:");
        for (String h : hechos) {
            trazas.add("   • " + h);
        }

        return trazas;
    }

    /** Verifica si una condición se cumple con los hechos actuales */
    private boolean verificarHecho(Condicion condicion) {
        // Se busca un hecho que coincida con el atributo
        for (String hecho : hechos) {
            String[] partes = hecho.split(" ");
            if (partes.length >= 3) {
                String atributo = partes[0];
                String operador = partes[1];
                String valor = partes[2];

                if (atributo.equalsIgnoreCase(condicion.getAtributo())) {
                    try {
                        double actual = Double.parseDouble(valor);
                        double esperado = Double.parseDouble(condicion.getValor());

                        switch (condicion.getOperador()) {
                            case "=":
                                return actual == esperado;
                            case ">":
                                return actual > esperado;
                            case "<":
                                return actual < esperado;
                            case ">=":
                                return actual >= esperado;
                            case "<=":
                                return actual <= esperado;
                            case "!=":
                                return actual != esperado;
                            default:
                                return false;
                        }
                    } catch (NumberFormatException e) {
                        // Comparación textual
                        return valor.equalsIgnoreCase(condicion.getValor());
                    }
                }
            }
        }
        return false;
    }
}
