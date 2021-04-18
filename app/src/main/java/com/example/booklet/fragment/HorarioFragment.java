package com.example.booklet.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.booklet.R;
import com.example.booklet.activity.horario.Quarta;
import com.example.booklet.activity.horario.Quinta;
import com.example.booklet.activity.horario.Segunda;
import com.example.booklet.activity.horario.Sexta;
import com.example.booklet.activity.horario.Terca;
import com.example.booklet.utility.NetworkChangeListener;

public class HorarioFragment extends Fragment {

    public HorarioFragment() {
        // Required empty public constructor
    }

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

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

        ImageView imgSegunda = view.findViewById(R.id.imgSegunda);
        ImageView imgTerca = view.findViewById(R.id.imgTerca);
        ImageView imgQuarta = view.findViewById(R.id.imgQuarta);
        ImageView imgQuinta = view.findViewById(R.id.imgQuinta);
        ImageView imgSexta = view.findViewById(R.id.imgSexta);

        String segunda = String.valueOf(BtnSegunda.getText());
        String terca = String.valueOf(BtnTerca.getText());
        String quarta = String.valueOf(BtnQuarta.getText());
        String quinta = String.valueOf(BtnQuinta.getText());
        String sexta = String.valueOf(BtnSexta.getText());

        if (segunda.equals("Segunda")){
            imgSegunda.setImageResource(R.drawable.s);
        }

        if (terca.equals("Terça")){
            imgSegunda.setImageResource(R.drawable.t);
        }

        if (quarta.equals("Quarta")){
            imgSegunda.setImageResource(R.drawable.q);
        }

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

    @Override
    public void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    public void onStop() {
        requireActivity().unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}