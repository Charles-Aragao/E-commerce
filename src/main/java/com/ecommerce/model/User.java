package com.ecommerce.model;

public class User {
    private int id;
    private String nomeUsuario;
    private String senha;
    private String email;
    private String nomeCompleto;
    private String endereco;
    private String telefone;
    private TipoUsuario tipoUsuario;

    public enum TipoUsuario {
        CLIENTE, ADMIN
    }

    public User() {}

    public User(int id, String nomeUsuario, String senha, String email, String nomeCompleto, String endereco, String telefone, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.email = email;
        this.nomeCompleto = nomeCompleto;
        this.endereco = endereco;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }
}