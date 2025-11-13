package com.example.motorinferencia.motor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MotorInferencia {

    private BaseConocimiento base;
    private Set<hecho> hechos; // Usar objetos 'hecho' para incluir certeza
    private List<String> trazas;

    // Umbral para considerar una condición como "verdadera"
    private static final double UMBRAL_CERTEZA = 0.2;

    public MotorInferencia(BaseConocimiento base) {
        this.base = base;
        this.hechos = new HashSet<>();
        this.trazas = new ArrayList<>();
    }

    public void agregarHecho(hecho h) {
        hechos.add(h);
    }

    public List<String> ejecutar() {
        trazas.clear();
        boolean nuevaInferencia;
        int iteracion = 1;

        do {
            nuevaInferencia = false;
            trazas.add("Iteración " + iteracion + ":");
            iteracion++;

            for (Regla regla : base.getReglas()) {
                double certezaMinima = 1.0; // Certeza de la conclusión de la regla
                boolean cumpleTodas = true;

                for (Condicion c : regla.getCondiciones()) {
                    hecho hechoCoincidente = verificarHecho(c);

                    if (hechoCoincidente != null && hechoCoincidente.getCerteza() > UMBRAL_CERTEZA) {
                        trazas.add("   ✅ Cumple: " + c.toString() + " (Certeza: " + String.format("%.2f", hechoCoincidente.getCerteza()) + ")");
                        certezaMinima = Math.min(certezaMinima, hechoCoincidente.getCerteza());
                    } else {
                        cumpleTodas = false;
                        trazas.add("   ❌ No cumple: " + c.toString());
                        break;
                    }
                }

                if (cumpleTodas) {
                    // Extraer atributo y valor del resultado de la regla
                    String[] partesResultado = regla.getResultado().split(" ");
                    if (partesResultado.length >= 3) {
                        String atributoRes = partesResultado[0];
                        String valorRes = partesResultado[2];
                        hecho nuevoHecho = new hecho(atributoRes, valorRes, certezaMinima);

                        if (!hechos.contains(nuevoHecho)) {
                            hechos.add(nuevoHecho);
                            trazas.add("   🎯 Nueva inferencia: " + nuevoHecho.toString());
                            nuevaInferencia = true;
                        }
                    }
                }
            }
            trazas.add("-----------------------------");
        } while (nuevaInferencia);

        trazas.add("✅ Inferencia finalizada\n");
        trazas.add("\n🧩 Hechos base:");
        for (hecho h : hechos) {
            trazas.add("   • " + h.toString());
        }

        return trazas;
    }

    private hecho verificarHecho(Condicion condicion) {
        for (hecho h : hechos) {
            if (h.getAtributo().equalsIgnoreCase(condicion.getAtributo())) {
                // La condición se evalúa contra el valor del hecho
                if (condicion.evaluar(h.getValor())) {
                    return h; // Devuelve el hecho si la condición es verdadera
                }
            }
        }
        return null; // No se encontró un hecho que cumpla la condición
    }
     public Set<hecho> getHechos() {
        return hechos;
    }
}
