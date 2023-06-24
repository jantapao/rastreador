package com.example.trabalhofinal.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.trabalhofinal.R;
import com.google.android.material.textfield.TextInputEditText;

public class FragmentRastreio extends Fragment {
    private static final String ARG_PARAM1 = "codigo-rastreio";

    private String codigoDeRastreio;

    public FragmentRastreio() {
        // Required empty public constructor
    }

    public static FragmentRastreio newInstance(String param1, String param2) {
        FragmentRastreio fragment = new FragmentRastreio();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            codigoDeRastreio = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rastreio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText txtCodigoRastreio = view.findViewById(R.id.inputcodigo);
        TextInputEditText resultadoCaptcha = view.findViewById(R.id.textInputEditText);
        Button btnBuscar = view.findViewById(R.id.btnbuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Tu clicou pra buscar, né?", Toast.LENGTH_SHORT).show();
            }
        });

        txtCodigoRastreio.setText(codigoDeRastreio);
        resultadoCaptcha.setText("ABLUBLE");
    }
}