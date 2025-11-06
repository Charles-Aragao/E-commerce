package com.ecommerce.model;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int estoque;
    private String categoria;
    private String urlImagem;

    public Product() {}

    public Product(int id, String nome, String descricao, BigDecimal preco, int estoque, String categoria, String urlImagem) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.categoria = categoria;
        this.urlImagem = urlImagem;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }

    @Override
    public String toString() {
        return nome + " - R$ " + preco;
    }
}