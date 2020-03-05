package com.example.myapplication;

public class Utilizador {

    private String email;
    private String nome;
    private String password;

    public Utilizador(String email, String nome, String password) {
        this.setEmail(email);
        this.setNome(nome);
        this.setPassword(password);
    }

    public Utilizador() {
        email = "";
        nome = "";
        password = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
