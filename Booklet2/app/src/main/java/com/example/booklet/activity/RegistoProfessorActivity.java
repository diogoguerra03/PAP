package com.example.booklet.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class RegistoProfessorActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Utilizador utilizador;

    private Button buttonRegistarProfessor;
    private EditText campoProcessoProfessor;
    private EditText campoNomeProfessor;
    private EditText campoEmailProfessor;
    private EditText campoSenhaProfessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo_professor);

        getSupportActionBar().setTitle("Registo");

        buttonRegistarProfessor = findViewById(R.id.buttonRegistarProfessor);
        campoProcessoProfessor = findViewById(R.id.editProcessoProfessor);
        campoNomeProfessor = findViewById(R.id.editNomeProfessor);
        campoEmailProfessor = findViewById(R.id.editEmailProfessor);
        campoSenhaProfessor = findViewById(R.id.editPasswordProfessor);

        buttonRegistarProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNome = campoNomeProfessor.getText().toString();
                String textoEmail = campoEmailProfessor.getText().toString();
                String textoSenha = campoSenhaProfessor.getText().toString();
                String textoProcesso = campoProcessoProfessor.getText().toString();

                if (!textoNome.isEmpty()){
                    if (!textoEmail.isEmpty()){
                        if(!textoSenha.isEmpty()){
                            if (!textoProcesso.isEmpty()){
                                utilizador = new Utilizador();
                                utilizador.setTipo(1);
                                utilizador.setProcesso(textoProcesso);
                                utilizador.setNome(textoNome);
                                utilizador.setEmail(textoEmail);
                                utilizador.setSenha(textoSenha);

                                registarProfessor();
                            }else{
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Preencha o número de processo!",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
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
            }
        });

    }


    public void registarProfessor(){
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

                    Toast.makeText(RegistoProfessorActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}