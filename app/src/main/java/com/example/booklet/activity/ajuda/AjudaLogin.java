package com.example.booklet.activity.ajuda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toolbar;

import com.example.booklet.R;

public class AjudaLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda_login);

        getSupportActionBar().setTitle(R.string.ajuda);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_seta_sair_branca_24);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}