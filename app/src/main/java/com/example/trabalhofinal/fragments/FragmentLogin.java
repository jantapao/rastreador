package com.example.trabalhofinal.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.trabalhofinal.R;
import com.google.android.material.textfield.TextInputEditText;

public class FragmentLogin extends Fragment {

    public FragmentLogin() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText usrInput = view.findViewById(R.id.usuarioinput);
        TextInputEditText senhaInput = view.findViewById(R.id.senhainput);

        Button entrarButton = view.findViewById(R.id.btnentrar);
        Button cadastroButton = view.findViewById(R.id.btncadastro);

        entrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validar Usuario no BD
            }
        });

        cadastroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cadastrar Usuario no Sistema
            }
        });
    }
}