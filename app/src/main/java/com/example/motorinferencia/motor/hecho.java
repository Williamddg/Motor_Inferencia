package com.example.motorinferencia.motor;

public class hecho {
    private String atributo;
    private String valor;

    public hecho(String atributo, String valor) {
        this.atributo = atributo;
        this.valor = valor;
    }

    public String getAtributo() { return atributo; }
    public String getValor() { return valor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof hecho)) return false;
        hecho h = (hecho) o;
        return atributo.equalsIgnoreCase(h.atributo) && valor.equalsIgnoreCase(h.valor);
    }

    @Override
    public int hashCode() {
        return (atributo.toLowerCase() + "::" + valor.toLowerCase()).hashCode();
    }

    @Override
    public String toString() {
        return atributo + " = " + valor;
    }
}
