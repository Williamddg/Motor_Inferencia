package com.example.motorinferencia.motor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MotorInferencia {

    // Clase para representar un paso en la inferencia
    public static class InferenceStep implements Serializable {
        public final Regla regla;
        public final List<hecho> condicionesSatisfechas;
        public final hecho resultado;
        public final int iteracion;

        public InferenceStep(Regla regla, List<hecho> condiciones, hecho resultado, int iteracion) {
            this.regla = regla;
            this.condicionesSatisfechas = condiciones;
            this.resultado = resultado;
            this.iteracion = iteracion;
        }
    }

    private BaseConocimiento base;
    private Set<hecho> hechos; // Usar objetos 'hecho' para incluir certeza
    private List<String> trazas;
    private List<InferenceStep> inferenceGraph; // Grafo de inferencia

    // Umbral para considerar una condición como "verdadera"
    private static final double UMBRAL_CERTEZA = 0.0;

    public MotorInferencia(BaseConocimiento base) {
        this.base = base;
        this.hechos = new HashSet<>();
        this.trazas = new ArrayList<>();
        this.inferenceGraph = new ArrayList<>();
    }

    public void agregarHecho(hecho h) {
        hechos.add(h);
    }

    public List<String> ejecutar() {
        trazas.clear();
        inferenceGraph.clear();
        boolean nuevaInferencia;
        int iteracion = 1;

        do {
            nuevaInferencia = false;
            trazas.add("Iteración " + iteracion + ":");

            for (Regla regla : base.getReglas()) {
                boolean cumpleRegla = false;
                double certezaMinima = 1.0;
                List<hecho> hechosCondicion = new ArrayList<>();

                boolean esDisyuntiva = !regla.getCondiciones().isEmpty()
                        && "O".equalsIgnoreCase(regla.getCondiciones().get(0).getConector());

                if (esDisyuntiva) {
                    for (Condicion c : regla.getCondiciones()) {
                        hecho h = verificarHecho(c);
                        if (h != null && h.getCerteza() > UMBRAL_CERTEZA && c.evaluar(h.getValor())) {
                            cumpleRegla = true;
                            certezaMinima = Math.min(certezaMinima, h.getCerteza());
                            hechosCondicion.add(h);
                            trazas.add("   Cumple (O): " + c.toString());
                            break;
                        }
                    }
                } else {
                    cumpleRegla = true;
                    for (Condicion c : regla.getCondiciones()) {
                        hecho h = verificarHecho(c);
                        if (h == null || !c.evaluar(h.getValor()) || h.getCerteza() <= UMBRAL_CERTEZA) {
                            cumpleRegla = false;
                            trazas.add("   No cumple (Y): " + c.toString());
                            break;
                        } else {
                            certezaMinima = Math.min(certezaMinima, h.getCerteza());
                            hechosCondicion.add(h);
                            trazas.add("   Cumple (Y): " + c.toString());
                        }
                    }
                }

                if (cumpleRegla) {
                    String[] partesResultado = regla.getResultado().split(" ");
                    if (partesResultado.length >= 3) {
                        String atributoRes = partesResultado[0];
                        String valorRes = partesResultado[2];
                        hecho nuevoHecho = new hecho(atributoRes, valorRes, certezaMinima);

                        if (!hechos.contains(nuevoHecho)) {
                            hechos.add(nuevoHecho);
                            nuevaInferencia = true;
                            inferenceGraph.add(new InferenceStep(regla, hechosCondicion, nuevoHecho, iteracion));
                            trazas.add("    Nueva inferencia: " + nuevoHecho.toString());
                        }
                    }
                }
            }
            iteracion++;
            trazas.add("-----------------------------");
        } while (nuevaInferencia);

        trazas.add("Inferencia finalizada\n");
        trazas.add("\n Hechos finales:");
        for (hecho h : hechos) {
            trazas.add("   • " + h.toString());
        }

        return trazas;
    }

    private hecho verificarHecho(Condicion condicion) {
        for (hecho h : hechos) {
            if (h.getAtributo().equalsIgnoreCase(condicion.getAtributo())) {
                if (condicion.evaluar(h.getValor())) {
                    return h;
                }
            }
        }
        return null;
    }

    public Set<hecho> getHechos() {
        return hechos;
    }

    public List<InferenceStep> getInferenceGraph() {
        return inferenceGraph;
    }
}
