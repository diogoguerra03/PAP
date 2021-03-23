package com.example.booklet.model;

import com.example.booklet.config.ConfiguracaoFirebase;
import com.example.booklet.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Horario {

    private String disciplina, sala, id, horaInicial, horaFinal;

    public Horario(){

    }

    public void salvarSegunda(){

        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("horario")
                .child(idUtilizador)
                .child("segunda")
                .push()
                .setValue(this);

    }

    public void salvarTerca(){

        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("horario")
                .child(idUtilizador)
                .child("terça")
                .push()
                .setValue(this);

    }

    public void salvarQuarta(){

        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("horario")
                .child(idUtilizador)
                .child("quarta")
                .push()
                .setValue(this);

    }

    public void salvarQuinta(){

        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("horario")
                .child(idUtilizador)
                .child("quinta")
                .push()
                .setValue(this);

    }

    public void salvarSexta(){

        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUtilizador = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("horario")
                .child(idUtilizador)
                .child("sexta")
                .push()
                .setValue(this);

    }


    public Horario(String disciplina, String sala, String id, String horaInicial, String horaFinal) {
        this.disciplina = disciplina;
        this.sala = sala;
        this.id = id;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(String horaInicial) {
        this.horaInicial = horaInicial;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }
}
