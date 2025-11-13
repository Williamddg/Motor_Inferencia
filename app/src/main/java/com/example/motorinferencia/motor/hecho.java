package com.example.motorinferencia.motor;

public class hecho {
    private String atributo;
    private String valor;
    private double certeza;

    public hecho(String atributo, String valor, double certeza) {
        this.atributo = atributo;
        this.valor = valor;
        this.certeza = certeza;
    }

    public String getAtributo() { return atributo; }
    public String getValor() { return valor; }
    public double getCerteza() { return certeza; }
    public void setCerteza(double certeza) { this.certeza = certeza; }

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
        return atributo + " = " + valor + " (Certeza: " + String.format("%.2f", certeza) + ")";
    }
}
