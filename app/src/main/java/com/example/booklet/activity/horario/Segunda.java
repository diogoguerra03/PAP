package com.example.booklet.activity.horario;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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

public class Segunda extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;

    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference horarioSegundaRef;
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
        setContentView(R.layout.activity_segunda);

        getSupportActionBar().setTitle("Segunda");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_seta_sair_branca_24);

        recyclerView = findViewById(R.id.recyclerViewSegunda);

        adapterHorario = new AdapterHorario(horarios, Segunda.this);

        //Configurar RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Segunda.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterHorario);
        swipe();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(Segunda.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

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



        floatingActionButton = findViewById(R.id.fabSegunda);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarHorario();
            }
        });
    }

    private void adicionarHorario(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(Segunda.this);
        LayoutInflater inflater = LayoutInflater.from(Segunda.this);

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

                                horario.salvarSegunda();

                                dialog.dismiss();
                            }else{
                                Toast.makeText(Segunda.this,
                                        "Hora não foi preenchida!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(Segunda.this,
                                    "Hora não foi preenchida!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Segunda.this,
                                "Descrição não foi preenchida!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Segunda.this,
                            "Tarefa não foi preenchida!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void recuperarHorario(){
        String emailUtilizador = auth.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64(emailUtilizador);

        horarioSegundaRef = firebaseRef.child("horario")
                .child(idUtilizador).child("segunda");

        valueEventListenerHorario = horarioSegundaRef.addValueEventListener(new ValueEventListener() {
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
        AlertDialog.Builder myDialog = new AlertDialog.Builder(Segunda.this);
        LayoutInflater inflater = LayoutInflater.from(Segunda.this);
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

                            horarioSegundaRef = firebaseRef.child("horario")
                                    .child(idUtilizador).child("segunda");

                            horarioSegundaRef.child(horario.getId()).updateChildren(hashDisciplina);
                            horarioSegundaRef.child(horario.getId()).updateChildren(hashSala);
                            horarioSegundaRef.child(horario.getId()).updateChildren(hashHrInicial);
                            horarioSegundaRef.child(horario.getId()).updateChildren(hashHrFinal);
                            dialog.dismiss();
                        }else{
                            Toast.makeText(Segunda.this, "Digite a hora corretamente", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(Segunda.this, "Digite a descrição", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(Segunda.this, "Digite a tarefa", Toast.LENGTH_LONG).show();
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

    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                excluirTarefa(viewHolder);

            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);

    }

    public void excluirTarefa(final RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Segunda.this);

        alertDialog.setTitle("Excluir aula");
        alertDialog.setMessage("Tem a certeza que deseja excluir a aula?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                horario = horarios.get(position);

                String emailUtilizador = auth.getCurrentUser().getEmail();
                String idUtilizador = Base64Custom.codificarBase64(emailUtilizador);

                horarioSegundaRef = firebaseRef.child("horario")
                        .child(idUtilizador).child("segunda");

                horarioSegundaRef.child(horario.getId()).removeValue();
                adapterHorario.notifyItemRemoved(position);
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Segunda.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                adapterHorario.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarHorario();
    }

    @Override
    public void onStop() {
        super.onStop();
        horarioSegundaRef.removeEventListener(valueEventListenerHorario);
    }

}