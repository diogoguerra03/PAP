package com.example.booklet.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booklet.R;
import com.example.booklet.adapter.AdapterTarefa;
import com.example.booklet.config.ConfiguracaoFirebase;
import com.example.booklet.helper.Base64Custom;
import com.example.booklet.helper.DateCustom;
import com.example.booklet.helper.RecyclerItemClickListener;
import com.example.booklet.model.Tarefa;
import com.example.booklet.utility.NetworkChangeListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TodoFragment extends Fragment {


    public TodoFragment() {
        // Required empty public constructor
    }

    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;

    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference tarefaRef;
    private ValueEventListener valueEventListenerTarefas;

    private AdapterTarefa adapterTarefa;
    private List<Tarefa> tarefas = new ArrayList<>();
    private Tarefa tarefa;

    private String tarefaPreenchida;
    private String descricaoPreenchida;
    private String dataPreenchida;

    private CheckBox CheckBoxRealizada;

    //PopUp calendario
    private ImageButton btnCalendario;
    private ImageButton BtnCalendarioUpDate;
    final Calendar myCalendar = Calendar.getInstance();
    private EditText campodata;
    private EditText campoDataAtualizada;
    private AlertDialog popAdd;
    private AlertDialog popUpdate;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        CheckBoxRealizada = view.findViewById(R.id.CheckBoxRealizada);

        adapterTarefa = new AdapterTarefa(tarefas, getActivity());

        //Configurar RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterTarefa);
        swipe();


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                tarefa = tarefas.get(position);

                tarefaPreenchida = tarefa.getTarefa();
                descricaoPreenchida = tarefa.getDescricao();
                dataPreenchida = tarefa.getData();

                atualizarTarefa();
            }

            @Override
            public void onLongItemClick(View view, int position) {
                tarefa = tarefas.get(position);
                Boolean realizada = tarefa.getRealizada();

                String emailUtilizador = auth.getCurrentUser().getEmail();
                String idUtilizador = Base64Custom.codificarBase64(emailUtilizador);

                if (realizada == false){
                    HashMap hashRealizada = new HashMap();
                    hashRealizada.put("realizada", true);

                    tarefaRef = firebaseRef.child("tarefa")
                            .child(idUtilizador);

                    tarefaRef.child(tarefa.getKey()).updateChildren(hashRealizada);
                    //txtRealizada.setText(tarefa.getRealizada().toString());
                    Toast.makeText(getActivity(), "Realizada", Toast.LENGTH_SHORT).show();

                }else{
                        HashMap hashRealizada = new HashMap();
                        hashRealizada.put("realizada", false);

                        tarefaRef = firebaseRef.child("tarefa")
                                .child(idUtilizador);

                        tarefaRef.child(tarefa.getKey()).updateChildren(hashRealizada);
                        //txtRealizada.setText(tarefa.getRealizada().toString());
                        Toast.makeText(getActivity(), "Nao realizada", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        floatingActionButton = view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarTarefa();
            }
        });
        return view;
    }

    public void swipe() {

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

    public void excluirTarefa(final RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle(R.string.excluirTarefa);
        alertDialog.setMessage(R.string.certezaExcluirTarefa);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton(R.string.confirmar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                tarefa = tarefas.get(position);

                String emailUtilizador = auth.getCurrentUser().getEmail();
                String idUtilizador = Base64Custom.codificarBase64(emailUtilizador);

                tarefaRef = firebaseRef.child("tarefa")
                        .child(idUtilizador);

                tarefaRef.child(tarefa.getKey()).removeValue();
                adapterTarefa.notifyItemRemoved(position);
            }
        });

        alertDialog.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),
                        R.string.cancelado,
                        Toast.LENGTH_SHORT).show();
                adapterTarefa.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void adicionarTarefa() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        final View myView = inflater.inflate(R.layout.input_file, null);
        myDialog.setView(myView);

        popAdd = myDialog.create();
        popAdd.setCancelable(false);

        final EditText campotarefa = myView.findViewById(R.id.tarefa);
        final EditText campodescricao = myView.findViewById(R.id.descricao);
        campodata = myView.findViewById(R.id.editData);
        Button botaoguardar = myView.findViewById(R.id.btnGuardar);
        Button botaocancelar = myView.findViewById(R.id.btnCancelarTarefa);
        btnCalendario = myView.findViewById(R.id.btnCalendario);

        campodata.setText(DateCustom.dataAtual());
        popAdd.show();

        botaocancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAdd.dismiss();
            }
        });

        btnCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });

        botaoguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoTarefa = campotarefa.getText().toString();
                String textoDescricao = campodescricao.getText().toString();
                String textoData = campodata.getText().toString();

                if (!textoTarefa.isEmpty()) {
                    if (!textoDescricao.isEmpty()) {
                        if (textoData.length() > 7) {

                            tarefa = new Tarefa();
                            String data = campodata.getText().toString();

                            tarefa.setTarefa(campotarefa.getText().toString());
                            tarefa.setDescricao(campodescricao.getText().toString());
                            tarefa.setData(data);
                            tarefa.setRealizada(false);
                            tarefa.salvar(data);

                            popAdd.dismiss();

                        } else {
                            Toast.makeText(getActivity(),
                                    R.string.preencherData,
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),
                                R.string.preencherDescricao,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),
                            R.string.preencherDisciplina,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void DateDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
            myCalendar.getTime();
        }

    };

    private void updateLabel() {
        campodata = popAdd.findViewById(R.id.editData);
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        campodata.setText(sdf.format(myCalendar.getTime()));
    }

    public void recuperarTarefas() {
        String emailUtilizador = auth.getCurrentUser().getEmail();
        String idUtilizador = Base64Custom.codificarBase64(emailUtilizador);

        tarefaRef = firebaseRef.child("tarefa")
                .child(idUtilizador);

        valueEventListenerTarefas = tarefaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tarefas.clear();
                for (DataSnapshot dados : snapshot.getChildren()) {
                    Tarefa tarefa = dados.getValue(Tarefa.class);
                    tarefa.setKey(dados.getKey());
                    tarefas.add(tarefa);
                }

                adapterTarefa.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void atualizarTarefa() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.update_tarefa, null);
        myDialog.setView(view);

        popUpdate = myDialog.create();

        final EditText campoTarefaAtualizada = view.findViewById(R.id.EditTarefaAtualizada);
        final EditText campoDescricaoAtualizada = view.findViewById(R.id.EditDescricaoAtualizada);
        campoDataAtualizada = view.findViewById(R.id.EditDataAtualizada);
        BtnCalendarioUpDate = view.findViewById(R.id.btnCalendarioUpdate);

        campoTarefaAtualizada.setText(tarefaPreenchida);
        campoTarefaAtualizada.setSelection(tarefaPreenchida.length());

        campoDescricaoAtualizada.setText(descricaoPreenchida);
        campoDescricaoAtualizada.setSelection(descricaoPreenchida.length());

        campoDataAtualizada.setText(dataPreenchida);
        campoDataAtualizada.setSelection(dataPreenchida.length());

        Button btnCancelar = view.findViewById(R.id.btnCancelarUpdate);
        Button btnAtualizar = view.findViewById(R.id.btnAtualizar);

        BtnCalendarioUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogUpdate();
            }
        });

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tarefaPreenchida = campoTarefaAtualizada.getText().toString().trim();
                descricaoPreenchida = campoDescricaoAtualizada.getText().toString().trim();
                dataPreenchida = campoDataAtualizada.getText().toString().trim();

                String tarefaP = tarefaPreenchida;
                String descricaoP = descricaoPreenchida;
                String dataP = dataPreenchida;

                if (!tarefaP.isEmpty()) {
                    if (!descricaoP.isEmpty()) {
                        if (dataP.length() > 7) {
                            String emailUtilizador = auth.getCurrentUser().getEmail();
                            String idUtilizador = Base64Custom.codificarBase64(emailUtilizador);
                            HashMap hashTarefa = new HashMap();
                            hashTarefa.put("tarefa", tarefaPreenchida);

                            HashMap hashDescricao = new HashMap();
                            hashDescricao.put("descricao", descricaoPreenchida);

                            HashMap hashData = new HashMap();
                            hashData.put("data", dataPreenchida);

                            HashMap hashRealizada = new HashMap();
                            hashRealizada.put("realizada", false);

                            tarefaRef = firebaseRef.child("tarefa")
                                    .child(idUtilizador);

                            tarefaRef.child(tarefa.getKey()).updateChildren(hashTarefa);
                            tarefaRef.child(tarefa.getKey()).updateChildren(hashDescricao);
                            tarefaRef.child(tarefa.getKey()).updateChildren(hashData);
                            tarefaRef.child(tarefa.getKey()).updateChildren(hashRealizada);
                            popUpdate.dismiss();
                        } else {
                            Toast.makeText(getActivity(), R.string.preencherData, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.preencherDescricao, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), R.string.preencherDisciplina, Toast.LENGTH_LONG).show();
                }


            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpdate.dismiss();
            }
        });

        popUpdate.show();
    }

    public void DateDialogUpdate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateUpdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener dateUpdate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabelUpdate();
            myCalendar.getTime();
        }

    };

    private void updateLabelUpdate() {
        campoDataAtualizada = popUpdate.findViewById(R.id.EditDataAtualizada);
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        campoDataAtualizada.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkChangeListener, filter);
        super.onStart();
        recuperarTarefas();
    }

    @Override
    public void onStop() {
        requireActivity().unregisterReceiver(networkChangeListener);
        super.onStop();
        tarefaRef.removeEventListener(valueEventListenerTarefas);
    }
}
