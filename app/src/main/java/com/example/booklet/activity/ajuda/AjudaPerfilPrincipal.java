package com.example.booklet.activity.ajuda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.booklet.R;
import com.example.booklet.activity.AjudaActivity;
import com.example.booklet.utility.NetworkChangeListener;

public class AjudaPerfilPrincipal extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    Button btnAjudaMudarPass, btnAjudaTerminarSessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda_perfil_principal);

        getSupportActionBar().setTitle(R.string.paginaPerfil);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_seta_sair_branca_24);

        btnAjudaMudarPass = findViewById(R.id.btnAjudaMudarPass);
        btnAjudaTerminarSessao = findViewById(R.id.btnAjudaTerminarSessao);

        btnAjudaMudarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AjudaPerfilPrincipal.this, AjudaPerfilPassword.class));
            }
        });

        btnAjudaTerminarSessao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AjudaPerfilPrincipal.this, AjudaPerfilSessao.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    public void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}