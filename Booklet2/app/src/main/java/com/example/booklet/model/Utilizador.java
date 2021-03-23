package com.example.booklet.model;

import com.example.booklet.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Utilizador {

    private String idUtilizador,nome, email, senha, processo;
    private int tipo;

    public Utilizador() {
    }

    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();

        if (tipo == 0){
            firebase.child("utilizadores")
                    .child("Aluno")
                    .child( this.idUtilizador )
                    .setValue( this );
        }

        if (tipo == 1){
            firebase.child("utilizadores")
                    .child("Professor")
                    .child( this.idUtilizador )
                    .setValue( this );
        }

    }


    @Exclude
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Exclude
    public String getIdUtilizador() {
        return idUtilizador;
    }

    public void setIdUtilizador(String idUtilizador) {
        this.idUtilizador = idUtilizador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }
}
