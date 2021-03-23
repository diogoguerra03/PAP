package com.example.booklet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.booklet.R;
import com.example.booklet.model.Horario;
import com.example.booklet.model.Tarefa;

import java.util.List;

public class AdapterHorario extends RecyclerView.Adapter<AdapterHorario.MyViewHolder> {

    List<Horario> horarios;
    Context context;

    public AdapterHorario(List<Horario> horarios, Context context) {
        this.horarios = horarios;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_horario, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Horario horario = horarios.get(position);

        holder.disciplina.setText(horario.getDisciplina());
        holder.sala.setText(String.valueOf(horario.getSala()));
        holder.horaInicial.setText(horario.getHoraInicial());
        holder.horaFinal.setText(horario.getHoraFinal());
    }


    @Override
    public int getItemCount() {
        return horarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView disciplina, sala, horaInicial, horaFinal;

        public MyViewHolder(View itemView) {
            super(itemView);

            disciplina = itemView.findViewById(R.id.disciplinaTv);
            sala = itemView.findViewById(R.id.salaTv);
            horaInicial = itemView.findViewById(R.id.horaInicialTv);
            horaFinal = itemView.findViewById(R.id.horaFinalTv);
        }

    }

}
