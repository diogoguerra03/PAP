package com.example.booklet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.booklet.R;
import com.example.booklet.activity.ajuda.AjudaLogin;
import com.example.booklet.activity.ajuda.AjudaRegisto;

public class AjudaActivity extends AppCompatActivity {

    //Contactos
    Button btnFb,btnInsta, btnGmail;

    //Ajuda
    Button btnAjudaLogin, btnAjudaRegisto, btnAjudaNoticias, btnAjudaTarefa, btnAjudaHorario, btnAjudaPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);

        getSupportActionBar().setTitle(R.string.ajuda);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_seta_sair_branca_24);

        //Ajuda
        btnAjudaLogin = findViewById(R.id.btnAjudaLogin);
        btnAjudaRegisto = findViewById(R.id.btnAjudaRegisto);
        btnAjudaNoticias = findViewById(R.id.btnAjudaNoticias);
        btnAjudaTarefa = findViewById(R.id.btnAjudaTarefa);
        btnAjudaHorario = findViewById(R.id.btnAjudaHorario);
        btnAjudaPerfil = findViewById(R.id.btnAjudaPerfil);

        btnAjudaLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AjudaActivity.this, AjudaLogin.class));
            }
        });

        btnAjudaRegisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AjudaActivity.this, AjudaRegisto.class));
            }
        });

        //contactos
        btnFb = findViewById(R.id.btnFacebook);
        btnInsta = findViewById(R.id.btnInsta);
        btnGmail = findViewById(R.id.btnGmail);

        btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:diogoguerra578@gmail.com")));
            }
        });

        btnInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/diogoguerra_22/");
                Intent instagram = new Intent(Intent.ACTION_VIEW, uri);
                instagram.setPackage("com.instagram.android");
                try {
                    startActivity(instagram);
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/diogoguerra_22/")));
                }
            }
        });

        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOpenFacebookIntent("diogo.guerra.5817"); //1165712407220289
            }
        });
    }

   private void getOpenFacebookIntent(String id){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + id));
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + id));
            startActivity(intent);
        }
   }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}