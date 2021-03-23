package com.example.booklet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booklet.R;
import com.example.booklet.model.Tarefa;

import java.util.List;

public class AdapterTarefa extends RecyclerView.Adapter<AdapterTarefa.MyViewHolder> {

    List<Tarefa> tarefas;
    Context context;

    public AdapterTarefa(List<Tarefa> tarefas, Context context) {
        this.tarefas = tarefas;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tarefa, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tarefa tarefa = tarefas.get(position);

        holder.titulo.setText(tarefa.getTarefa());
        holder.descricao.setText(String.valueOf(tarefa.getDescricao()));
        holder.data.setText(tarefa.getData());
    }


    @Override
    public int getItemCount() {
        return tarefas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, descricao, data;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.tarefaTv);
            descricao = itemView.findViewById(R.id.descricaoTv);
            data = itemView.findViewById(R.id.dataTv);
        }

    }

}
