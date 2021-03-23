package com.example.booklet.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booklet.R;
import com.example.booklet.config.ConfiguracaoFirebase;
import com.example.booklet.helper.Base64Custom;
import com.example.booklet.model.Utilizador;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
        final EditText editEmailPerfil = view.findViewById(R.id.editEmailPerfil);
        Button btnEditar = view.findViewById(R.id.btnUpdatePerfil);
        editEmailPerfil.setFocusable(false);

        //Referencia
        String emailUtilizador = autenticacao.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64(emailUtilizador);

        utilizadorRef = firebaseRef.child("utilizadores").child("Aluno").child(idUtilizador);

        valueEventListenerUtilizador = utilizadorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utilizador utilizador = snapshot.getValue(Utilizador.class);
                int tipo = utilizador.getTipo();
                String tip = String.valueOf(tipo);

                    editNomePerfil.setText(utilizador.getNome());
                    editEmailPerfil.setText(utilizador.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeAtualizado = editNomePerfil.getText().toString();

                //Atualizar nome do perfil


                Toast.makeText(getActivity(),
                        "Dados alterados com sucesso!",
                        Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

}