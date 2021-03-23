package com.example.booklet.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.booklet.R;
import com.example.booklet.activity.horario.Quarta;
import com.example.booklet.activity.horario.Quinta;
import com.example.booklet.activity.horario.Segunda;
import com.example.booklet.activity.horario.Sexta;
import com.example.booklet.activity.horario.Terca;

public class HorarioFragment extends Fragment {

    public HorarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_horarios, container, false);


        Button BtnSegunda = view.findViewById(R.id.BtnSegunda);
        Button BtnTerca = view.findViewById(R.id.BtnTerca);
        Button BtnQuarta = view.findViewById(R.id.BtnQuarta);
        Button BtnQuinta = view.findViewById(R.id.BtnQuinta);
        Button BtnSexta = view.findViewById(R.id.BtnSexta);

        BtnSegunda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Segunda.class);
                startActivity(intent);
            }
        });

        BtnTerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Terca.class);
                startActivity(intent);
            }
        });

        BtnQuarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Quarta.class);
                startActivity(intent);
            }
        });

        BtnQuinta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Quinta.class);
                startActivity(intent);
            }
        });

        BtnSexta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Sexta.class);
                startActivity(intent);
            }
        });

        return view;
    }
}