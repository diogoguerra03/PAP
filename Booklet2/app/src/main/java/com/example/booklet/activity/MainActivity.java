package com.example.booklet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.booklet.R;
import com.example.booklet.config.ConfiguracaoFirebase;
import com.example.booklet.fragment.AlunoFragment;
import com.example.booklet.fragment.ProfessorFragment;
import com.example.booklet.model.Utilizador;
import com.google.firebase.auth.FirebaseAuth;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewPagerTab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Login");

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Aluno", AlunoFragment.class)
                .add("Professor", ProfessorFragment.class)
                .create());


        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUtilizadorLogado();
    }

    public void verificarUtilizadorLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        //autenticacao.signOut();
        if( autenticacao.getCurrentUser() != null ){
            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivityAluno.class));
    }


}
