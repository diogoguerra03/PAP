package com.example.booklet.activity.horario;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklet.R;
import com.example.booklet.adapter.AdapterHorario;
import com.example.booklet.config.ConfiguracaoFirebase;
import com.example.booklet.helper.Base64Custom;
import com.example.booklet.helper.RecyclerItemClickListener;
import com.example.booklet.model.Horario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Quinta extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;

    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference horarioQuintaRef;
    private ValueEventListener valueEventListenerHorario;

    private AdapterHorario adapterHorario;
    private List<Horario> horarios = new ArrayList<>();
    private Horario horario;

    private String disciplinaPreenchida;
    private String salaPreenchida;
    private String HrInicialPreenchida;
    private String HrFinalPreenchida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quinta);

        getSupportActionBar().setTitle("Quinta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_seta_sair_branca_24);

        recyclerView = findViewById(R.id.recyclerViewQuinta);

        adapterHorario = new AdapterHorario(horarios, Quinta.this);

        //Configurar RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Quinta.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterHorario);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(Quinta.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onItemClick(View view, int position) {
                horario = horarios.get(position);

                disciplinaPreenchida = horario.getDisciplina();
                salaPreenchida = horario.getSala();
                HrInicialPreenchida = horario.getHoraInicial();
                HrFinalPreenchida = horario.getHoraFinal();

                atualizarTarefa();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        floatingActionButton = findViewById(R.id.fabQuinta);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarHorario();
            }
        });
    }

    private void adicionarHorario(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(Quinta.this);
        LayoutInflater inflater = LayoutInflater.from(Quinta.this);

        final View myView = inflater.inflate(R.layout.input_horario, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText campodisciplina = myView.findViewById(R.id.EdtDisciplina);
        final EditText camposala = myView.findViewById(R.id.EdtSala);
        final EditText campoHrInicial = myView.findViewById(R.id.EdthoraInicial);
        final EditText campoHrFinal = myView.findViewById(R.id.EdthoraFinal);
        Button btnCancelarHorario = myView.findViewById(R.id.btnCancelarHorario);
        Button btnGuardarDisciplina = myView.findViewById(R.id.btnGuardarDisciplina);

        dialog.show();

        btnCancelarHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnGuardarDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoDisciplina = campodisciplina.getText().toString();
                String textoSala = camposala.getText().toString();
                String textoHrInicial= campoHrInicial.getText().toString();
                String textoHrFinal = campoHrFinal.getText().toString();


                if (!textoDisciplina.isEmpty()){
                    if (!textoSala.isEmpty()){
                        if (!textoHrInicial.isEmpty()){
                            if (!textoHrFinal.isEmpty()){

                                horario = new Horario();

                                horario.setDisciplina(campodisciplina.getText().toString());
                                horario.setSala(camposala.getText().toString());
                                horario.setHoraInicial(campoHrInicial.getText().toString());
                                horario.setHoraFinal(campoHrFinal.getText().toString());

                                horario.salvarQuinta();

                                dialog.dismiss();
                            }else{
                                Toast.makeText(Quinta.this,
                                        "Hora não foi preenchida!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(Quinta.this,
                                    "Hora não foi preenchida!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Quinta.this,
                                "Descrição não foi preenchida!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Quinta.this,
                            "Tarefa não foi preenchida!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void recuperarHoraio(){
        String emailUtilizador = auth.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64(emailUtilizador);

        horarioQuintaRef = firebaseRef.child("horario")
                .child(idUtilizador).child("quinta");

        valueEventListenerHorario = horarioQuintaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                horarios.clear();
                for(DataSnapshot dados: snapshot.getChildren()){
                    Horario horario = dados.getValue(Horario.class);
                    horario.setId(dados.getKey());
                    horarios.add(horario);
                }

                adapterHorario.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void atualizarTarefa(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(Quinta.this);
        LayoutInflater inflater = LayoutInflater.from(Quinta.this);
        View view = inflater.inflate(R.layout.update_horario, null);
        myDialog.setView(view);

        final AlertDialog dialog = myDialog.create();

        final EditText campoDisciplinaAtualizada = view.findViewById(R.id.EdtDisciplinaAtualizada);
        final EditText campoSalaAtualizada = view.findViewById(R.id.EdtSalaAtualizada);
        final EditText campoHrInicialAtualizada = view.findViewById(R.id.EdthoraInicialAtualizada);
        final EditText campoHrFinalAtualizada = view.findViewById(R.id.EdthoraFinalAtualizada);

        campoDisciplinaAtualizada.setText(disciplinaPreenchida);
        campoDisciplinaAtualizada.setSelection(disciplinaPreenchida.length());

        campoSalaAtualizada.setText(salaPreenchida);
        campoSalaAtualizada.setSelection(salaPreenchida.length());

        campoHrInicialAtualizada.setText(HrInicialPreenchida);
        campoHrInicialAtualizada.setSelection(HrInicialPreenchida.length());

        campoHrFinalAtualizada.setText(HrFinalPreenchida);
        campoHrFinalAtualizada.setSelection(HrFinalPreenchida.length());

        Button btnCancelar = view.findViewById(R.id.btnCancelarUpdate);
        Button btnAtualizar = view.findViewById(R.id.btnAtualizar);

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disciplinaPreenchida = campoDisciplinaAtualizada.getText().toString().trim();
                salaPreenchida = campoSalaAtualizada.getText().toString().trim();
                HrInicialPreenchida = campoHrInicialAtualizada.getText().toString().trim();
                HrFinalPreenchida = campoHrFinalAtualizada.getText().toString().trim();

                String disciplinaP = disciplinaPreenchida;
                String salaP = salaPreenchida;
                String HrInicialP = HrInicialPreenchida;
                String HrFinalP = HrFinalPreenchida;

                if (!disciplinaP.isEmpty()){
                    if (!salaP.isEmpty()){
                        if (!HrInicialP.isEmpty() && !HrFinalP.isEmpty()){
                            String emailUtilizador = auth.getCurrentUser().getEmail();
                            String idUtilizador = Base64Custom.codificarBase64(emailUtilizador);

                            HashMap hashDisciplina = new HashMap();
                            hashDisciplina.put("disciplina", disciplinaPreenchida);

                            HashMap hashSala = new HashMap();
                            hashSala.put("sala", salaPreenchida);

                            HashMap hashHrInicial = new HashMap();
                            hashHrInicial.put("horaInicial", HrInicialPreenchida);

                            HashMap hashHrFinal = new HashMap();
                            hashHrFinal.put("horaFinal", HrFinalPreenchida);

                            horarioQuintaRef = firebaseRef.child("horario")
                                    .child(idUtilizador).child("quinta");

                            horarioQuintaRef.child(horario.getId()).updateChildren(hashDisciplina);
                            horarioQuintaRef.child(horario.getId()).updateChildren(hashSala);
                            horarioQuintaRef.child(horario.getId()).updateChildren(hashHrInicial);
                            horarioQuintaRef.child(horario.getId()).updateChildren(hashHrFinal);
                            dialog.dismiss();
                        }else{
                            Toast.makeText(Quinta.this, "Digite a hora corretamente", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Quinta.this, "Digite a descrição", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(Quinta.this, "Digite a tarefa", Toast.LENGTH_LONG).show();
                }



            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarHoraio();
    }

    @Override
    public void onStop() {
        super.onStop();
        horarioQuintaRef.removeEventListener(valueEventListenerHorario);
    }

}