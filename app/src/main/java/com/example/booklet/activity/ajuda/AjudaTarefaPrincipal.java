package com.example.booklet.activity.ajuda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.booklet.R;
import com.example.booklet.activity.AjudaActivity;

public class AjudaTarefaPrincipal extends AppCompatActivity {

    Button btnAjudaCriarTarefa, btnAjudaEditarTarefa, btnAjudaRealizarTarefa, btnAjudaEliminarTarefa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda_tarefa_principal);

        getSupportActionBar().setTitle(R.string.paginaNoticias);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_seta_sair_branca_24);


        btnAjudaCriarTarefa = findViewById(R.id.btnAjudaCriarTarefa);
        btnAjudaEditarTarefa = findViewById(R.id.btnAjudaEditarTarefa);
        btnAjudaRealizarTarefa = findViewById(R.id.btnAjudaRealizarTarefa);
        btnAjudaEliminarTarefa = findViewById(R.id.btnAjudaEliminarTarefa);

        btnAjudaCriarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AjudaTarefaPrincipal.this, AjudaTarefaCriar.class));
            }
        });

        btnAjudaEditarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AjudaTarefaPrincipal.this, AjudaTarefaEditar.class));
            }
        });

        btnAjudaRealizarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AjudaTarefaPrincipal.this, AjudaTarefaRealizar.class));
            }
        });

        btnAjudaEliminarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AjudaTarefaPrincipal.this, AjudaTarefaEliminar.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}