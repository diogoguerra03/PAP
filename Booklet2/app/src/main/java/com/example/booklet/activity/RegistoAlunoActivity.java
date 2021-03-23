package com.example.booklet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.booklet.R;
import com.example.booklet.config.ConfiguracaoFirebase;
import com.example.booklet.helper.Base64Custom;
import com.example.booklet.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegistoAlunoActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Utilizador utilizador;

    private Button buttonRegistarAluno;
    private EditText campoProcesso;
    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo_aluno);

        getSupportActionBar().setTitle("Registo");

        buttonRegistarAluno = findViewById(R.id.buttonRegistarAluno);
        campoNome = findViewById(R.id.editNomeAluno);
        campoEmail = findViewById(R.id.editEmailAluno);
        campoSenha = findViewById(R.id.editPasswordAluno);
        campoProcesso = findViewById(R.id.editProcessoAluno);

        buttonRegistarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();
                String textoProcesso = campoProcesso.getText().toString();

                if (!textoProcesso.isEmpty()){
                    if (!textoNome.isEmpty()){
                        if (!textoEmail.isEmpty()){

                                if(!textoSenha.isEmpty()){

                                    utilizador = new Utilizador();
                                    utilizador.setTipo(0);
                                    utilizador.setProcesso( textoProcesso );
                                    utilizador.setNome( textoNome );
                                    utilizador.setEmail( textoEmail );
                                    utilizador.setSenha( textoSenha );


                                    registarAluno();

                                }else{
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Preencha a password!",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }

                        }else{
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Preencha o Email!",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }else{
                        Toast.makeText(
                                getApplicationContext(),
                                "Preencha o nome!",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }else{
                    Toast.makeText(
                            getApplicationContext(),
                            "Preencha o número de processo!",
                            Toast.LENGTH_LONG
                    ).show();
                }

            }
        });

    }

    public void registarAluno(){
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.createUserWithEmailAndPassword(
                utilizador.getEmail(), utilizador.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String idUtilizador = Base64Custom.codificarBase64(utilizador.getEmail());
                    utilizador.setIdUtilizador(idUtilizador);
                    utilizador.salvar();
                    finish();
                }else{
                    String excecao = "";

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma password mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao= "Por favor, digite um e-mail válido";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Este conta já foi registada";
                    } catch (Exception e) {
                        excecao = "Erro ao registar utilizador: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(RegistoAlunoActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}