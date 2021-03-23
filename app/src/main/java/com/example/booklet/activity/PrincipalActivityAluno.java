package com.example.booklet.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.booklet.R;
import com.example.booklet.config.ConfiguracaoFirebase;
import com.example.booklet.fragment.FeedFragment;
import com.example.booklet.fragment.HorarioFragment;
import com.example.booklet.fragment.PerfilFragment;
import com.example.booklet.fragment.TodoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class PrincipalActivityAluno extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Booklet");
        setSupportActionBar( toolbar );

        //Configura o Bottom Navigation
        configuraBottomNavView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();

    }

    private void configuraBottomNavView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bnve);

        //faz configurações iniciais do Bottom Navigation
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.enableShiftingMode(true);
        bottomNavigationViewEx.setTextVisibility(true);

        //Habilitar navegação
        habilitarNavegacao( bottomNavigationViewEx );

        //configura item selecionado inicialmente
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);



    }

    private void habilitarNavegacao(BottomNavigationViewEx viewEx){

        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                switch (item.getItemId()){
                    case R.id.ic_news :
                        fragmentTransaction.replace(R.id.viewPager, new FeedFragment()).commit();
                        return true;
                    case R.id.ic_todo :
                        fragmentTransaction.replace(R.id.viewPager, new TodoFragment()).commit();
                        return true;
                    case R.id.ic_horario:
                        fragmentTransaction.replace(R.id.viewPager, new HorarioFragment()).commit();
                        return true;
                    case R.id.ic_perfil :
                        fragmentTransaction.replace(R.id.viewPager, new PerfilFragment()).commit();
                        return true;

                }

                return false;
            }
        });

    }
}