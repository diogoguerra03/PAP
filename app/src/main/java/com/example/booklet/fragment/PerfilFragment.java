package com.example.booklet.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booklet.R;
import com.example.booklet.activity.MainActivity;
import com.example.booklet.config.ConfiguracaoFirebase;
import com.example.booklet.helper.Base64Custom;
import com.example.booklet.model.Utilizador;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;


public class PerfilFragment extends Fragment {

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference utilizadorRef;
    private ValueEventListener valueEventListenerUtilizador;
    private Utilizador utilizador;
    
    public PerfilFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Iniciar Componentes
        final TextInputEditText editNomePerfil = view.findViewById(R.id.editNomePerfil);
        final TextInputEditText editEmailPerfil = view.findViewById(R.id.editEmailPerfil);
        Button botaoDeslogar = view.findViewById(R.id.botaoDeslogar);
        Button botaoPass = view.findViewById(R.id.botaoMudarSenha);
        editEmailPerfil.setFocusable(false);
        editNomePerfil.setFocusable(false);

        //Referencia
        String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64(emailUtilizador);

        utilizadorRef = firebaseRef.child("Aluno").child(idUtilizador);

        valueEventListenerUtilizador = utilizadorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utilizador utilizador = snapshot.getValue(Utilizador.class);

                    editNomePerfil.setText(utilizador.getNome());
                    editEmailPerfil.setText(utilizador.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        botaoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarAtualizarPass();
            }
        });

        botaoDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTerminarSessao();

            }
        });

        return view;
    }

    private void mostrarTerminarSessao(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_terminar_sessao, null);
        Button btnTerminar = view.findViewById(R.id.btnTerminar);
        final Button btnCancelar = view.findViewById(R.id.btnCancelarTerminar);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deslogarUtilizador();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

    }


    private void mostrarAtualizarPass() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_mudar_password, null);
        final EditText passwordEt = view.findViewById(R.id.passwordET);
        final EditText newPasswordEt = view.findViewById(R.id.NewPasswordET);
        Button updatePasswordBtn = view.findViewById(R.id.BtnAtualizarPass);
        final Button btnCancelarPass = view.findViewById(R.id.btnCancelarPass);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        btnCancelarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = passwordEt.getText().toString().trim();
                String newPass = newPasswordEt.getText().toString().trim();
                if (TextUtils.isEmpty(oldPass)){
                    Toast.makeText(getActivity(), "Preenche a tua password atual...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPass.length()<6){
                    Toast.makeText(getActivity(), "A nova password é muito curta...", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();
                atualizarPass(oldPass, newPass);
            }
        });
    }

    private void atualizarPass(String oldPass, final String newPass){

        //utilizador atual
        final FirebaseUser user = autenticacao.getCurrentUser();

        //antes de alterar a pass, re-autenticar o utilizador
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPass);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newPass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "Password atualizada com sucesso", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deslogarUtilizador(){
        try{
            autenticacao.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}