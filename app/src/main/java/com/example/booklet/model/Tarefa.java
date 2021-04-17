package com.example.booklet.model;

import android.provider.ContactsContract;

import com.example.booklet.config.ConfiguracaoFirebase;
import com.example.booklet.helper.Base64Custom;
import com.example.booklet.helper.DateCustom;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Tarefa {

    private String tarefa, descricao,data, Key;
    private Boolean realizada;

    public Tarefa(){

    }

    public String getTarefa() {
        return tarefa;
    }

    public void salvar(String dataEscolhida){

        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());
        String mesAno = DateCustom.mesAnoDataEscolhida( dataEscolhida );

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("tarefa")
                .child(idUtilizador)
                .push()
                .setValue(this);

    }

    public Boolean getRealizada() {
        return realizada;
    }

    public void setRealizada(Boolean realizada) {
        this.realizada = realizada;
    }

    public void setTarefa(String tarefa) {
        this.tarefa = tarefa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
