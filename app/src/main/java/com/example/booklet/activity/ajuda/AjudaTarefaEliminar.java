package com.example.booklet.activity.ajuda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.booklet.R;

public class AjudaTarefaEliminar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda_tarefa_eliminar);
        getSupportActionBar().setTitle(R.string.eliminarTarefa);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_seta_sair_branca_24);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}