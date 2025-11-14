package com.example.motorinferencia.motor;

import java.io.Serializable;

public class Condicion implements Serializable {

    private String atributo;
    private String operador;
    private String valor;
    private String conector; // "Y" u "O"
    private boolean negado;  // por si luego agregas negación lógica

    public Condicion(String atributo, String operador, String valor, String conector, boolean negado) {
        this.atributo = atributo;
        this.operador = operador;
        this.valor = valor;
        this.conector = conector;
        this.negado = negado;
    }

    public String getAtributo() { return atributo; }
    public String getOperador() { return operador; }
    public String getValor() { return valor; }
    public String getConector() { return conector; }
    public boolean isNegado() { return negado; }

    public boolean evaluar(String valorHecho) {
        try {
            double valHecho = Double.parseDouble(valorHecho);
            double valCond = Double.parseDouble(valor);
            switch (operador) {
                case "=":
                    return valHecho == valCond;
                case ">":
                    return valHecho > valCond;
                case "<":
                    return valHecho < valCond;
                case ">=":
                    return valHecho >= valCond;
                case "<=":
                    return valHecho <= valCond;
                case "!=":
                    return valHecho != valCond;
                default:
                    return false;
            }
        } catch (NumberFormatException e) {
            return valorHecho.equalsIgnoreCase(valor);
        }
    }

    @Override
    public String toString() {
        String texto = atributo + " " + operador + " " + valor;
        return negado ? "NO (" + texto + ")" : texto;
    }
}
