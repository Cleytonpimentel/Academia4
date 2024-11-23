package Entidades;

import java.math.BigDecimal;

public class Plano {
    private int id;
    private String nomePlano;
    private BigDecimal valor;

    // Construtor padrão
    public Plano() {}

    // Construtor com nome e valor como BigDecimal
    public Plano(String nomePlano, BigDecimal valor) {
        this.nomePlano = nomePlano;
        this.valor = valor;
    }

    // Construtor com nome e valor como double (chama o setter para garantir a conversão para BigDecimal)
    public Plano(String nomePlano, double valor) {
        this.nomePlano = nomePlano;
        setValor(valor); // Usando o setter para garantir a conversão
    }

    // Construtor com ID, nome e valor
    public Plano(int id, String nomePlano, double valor) {
        this.id = id;
        this.nomePlano = nomePlano;
        setValor(valor); // Usando o setter para garantir a conversão
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomePlano() {
        return nomePlano;
    }

    public void setNomePlano(String nomePlano) {
        this.nomePlano = nomePlano;
    }

    public BigDecimal getValor() {
        return valor;
    }

    // Setter que garante a conversão de valor para BigDecimal
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    // Setter com valor como double, converte para BigDecimal
    public void setValor(double valor) {
        this.valor = BigDecimal.valueOf(valor);
    }

    // Método alternativo para retornar o nome do plano
    public String getNome() {
        return nomePlano;
    }

    @Override
    public String toString() {
        return String.format("Plano{id=%d, nomePlano='%s', valor=%s}", id, nomePlano, valor.toString());
    }
}
