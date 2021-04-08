package com.example.booklet.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import com.example.booklet.utility.NetworkChangeListener;
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
    private EditText campoNome;
    private EditText campoEmail;
    private EditText campoSenha;
    private EditText campoConfirmarSenha;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo_aluno);

        getSupportActionBar().setTitle(R.string.registoText);

        buttonRegistarAluno = findViewById(R.id.buttonRegistarAluno);
        campoNome = findViewById(R.id.editNomeAluno);
        campoEmail = findViewById(R.id.editEmailAluno);
        campoSenha = findViewById(R.id.editPasswordAluno);
        campoConfirmarSenha = findViewById(R.id.editConfirmarPasswordAluno);

        buttonRegistarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();
                String textoconfirmarSenha = campoConfirmarSenha.getText().toString();

                if (!textoNome.isEmpty()) {
                    if (!textoEmail.isEmpty()) {
                        if (!textoSenha.isEmpty()) {
                            if (!textoconfirmarSenha.isEmpty()) {
                                if (textoSenha.equals(textoconfirmarSenha)) {
                                    utilizador = new Utilizador();
                                    utilizador.setNome(textoNome);
                                    utilizador.setEmail(textoEmail);
                                    utilizador.setSenha(textoSenha);

                                    registarAluno();
                                } else if (textoSenha.length() < 6 || textoconfirmarSenha.length() < 6) {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            R.string.passCurta,
                                            Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            R.string.passNaoCorresponde,
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            } else {
                                campoConfirmarSenha.requestFocus();
                                Toast.makeText(
                                        getApplicationContext(),
                                        R.string.passConfirmacaoVazia,
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } else {
                            campoSenha.requestFocus();
                            Toast.makeText(
                                    getApplicationContext(),
                                    R.string.preencherSenha,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    } else {
                        campoEmail.requestFocus();
                        campoEmail.setError("Campo obrigatório");
                        Toast.makeText(
                                getApplicationContext(),
                                R.string.preencherEmail,
                                Toast.LENGTH_LONG
                        ).show();
                    }
                } else {
                    campoNome.requestFocus();
                    campoNome.setError("Campo obrigatório");
                    Toast.makeText(
                            getApplicationContext(),
                            R.string.preencherNome,
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });

    }

    public void registarAluno() {
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.createUserWithEmailAndPassword(
                utilizador.getEmail(), utilizador.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String idUtilizador = Base64Custom.codificarBase64(utilizador.getEmail());
                    utilizador.setIdUtilizador(idUtilizador);
                    utilizador.salvar();
                    finish();
                } else {
                    String excecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma password mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Por favor, digite um e-mail válido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Este conta já foi registada";
                    } catch (Exception e) {
                        excecao = "Erro ao registar utilizador: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(RegistoAlunoActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
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