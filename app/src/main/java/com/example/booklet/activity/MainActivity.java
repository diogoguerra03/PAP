package com.example.booklet.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booklet.R;
import com.example.booklet.config.ConfiguracaoFirebase;
import com.example.booklet.model.Utilizador;
import com.example.booklet.utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Utilizador utilizador;
    private FirebaseAuth auth;
    private DatabaseReference utilizadorRef;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textRegisto = findViewById(R.id.textRegisto);
        final EditText campoEmail = findViewById(R.id.editEmailAluno);
        final EditText campoSenha = findViewById(R.id.editPasswordAluno);
        Button botaoEntrar = findViewById(R.id.buttonEntrarAluno);

        //Ao clicar no botao de login irá validar todos os campos e ver se correspondem às da BD para fazer o login
        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoEmailAluno = campoEmail.getText().toString();
                String textoSenhaAluno = campoSenha.getText().toString();

                if (!textoEmailAluno.isEmpty()){
                    if(!textoSenhaAluno.isEmpty()){

                        utilizador = new Utilizador();
                        utilizador.setEmail( textoEmailAluno );
                        utilizador.setSenha( textoSenhaAluno );
                        validarLogin();

                    }else{
                        campoSenha.requestFocus();
                        Toast.makeText(getApplicationContext(),
                                "Preencha a senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    campoEmail.requestFocus();
                    campoEmail.setError("Campo obrigatório");
                }
            }
        });


        //Ao clicar no texto para registar o aluno será redirecionado para a activity de registo
        textRegisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistoAlunoActivity.class);
                startActivity(intent);
            }
        });

    }

    public void validarLogin(){
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.signInWithEmailAndPassword(
                utilizador.getEmail(),
                utilizador.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    abrirTelaPrincipal();

                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Utilizador não está registado.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "E-mail e senha não correspondem a um utilizador regisado";
                    } catch (Exception e) {
                        excecao = "Erro ao tentar entrar: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(getApplicationContext(), excecao, Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
        verificarUtilizadorLogado();
    }

    @Override
    public void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
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
