package com.example.motorinferencia.motor;

public class Condicion {
    private String atributo;
    private String operador;
    private String valor;

    public Condicion(String atributo, String operador, String valor) {
        this.atributo = atributo;
        this.operador = operador;
        this.valor = valor;
    }

    public String getAtributo() { return atributo; }
    public String getOperador() { return operador; }
    public String getValor() { return valor; }

    @Override
    public String toString() {
        return atributo + " " + operador + " " + valor;
    }

    // Evalúa si una condición es verdadera dado un valor actual
    public boolean evaluar(String valorActual) {
        try {
            double actual = Double.parseDouble(valorActual);
            double esperado = Double.parseDouble(valor);

            switch (operador) {
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
                    return false; // operador no reconocido
            }

        } catch (NumberFormatException e) {
            // Si no son números, comparar como texto
            switch (operador) {
                case "=":
                    return valorActual.equalsIgnoreCase(valor);
                case "!=":
                    return !valorActual.equalsIgnoreCase(valor);
                default:
                    return false;
            }
        }
    }
}
