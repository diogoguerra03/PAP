package com.example.booklet.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booklet.R;
import com.example.booklet.activity.PrincipalActivityAluno;
import com.example.booklet.activity.RegistoAlunoActivity;
import com.example.booklet.config.ConfiguracaoFirebase;
import com.example.booklet.helper.Base64Custom;
import com.example.booklet.model.Utilizador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;


public class AlunoFragment extends Fragment {

    public AlunoFragment() {
        // Required empty public constructor
    }

    private Utilizador utilizador;
    private FirebaseAuth auth;
    private DatabaseReference utilizadorRef;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aluno, container, false);

        TextView textRegisto = view.findViewById(R.id.textRegisto);
        final EditText campoEmail = view.findViewById(R.id.editEmailAluno);
        final EditText campoSenha = view.findViewById(R.id.editPasswordAluno);
        Button botaoEntrar = view.findViewById(R.id.buttonEntrarAluno);

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
                        Toast.makeText(getActivity(),
                                "Preencha a senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(),
                            "Preencha o email!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Ao clicar no texto para registar o aluno será redirecionado para a activity de registo
        textRegisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegistoAlunoActivity.class);
                startActivity(intent);
            }
        });

        return view;
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
                        excecao = "Erro ao registar utilizador: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(getActivity(), excecao, Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(getActivity(), PrincipalActivityAluno.class));
        getActivity().finish();
    }

}