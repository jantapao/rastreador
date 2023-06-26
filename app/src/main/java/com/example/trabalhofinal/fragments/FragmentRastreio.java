package com.example.trabalhofinal.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.interfaces.OnJsonResponseListener;
import com.example.trabalhofinal.requests.RastreioEncomenda;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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

        String urlImage = "https://rastreamento.correios.com.br/core/securimage/securimage_show.php";
        RastreioEncomenda.downloadImage(urlImage, requireContext());

        ((TextInputEditText) view.findViewById(R.id.inputcodigo)).setText(codigoDeRastreio);

        File imageFile = new File(getContext().getFilesDir(), "imagem-captcha.png");

        if (imageFile.exists()) {
            ((ImageView) view.findViewById(R.id.imageView)).setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
        } else {
            Toast.makeText(requireContext(), "Não foi possível carregar o Captcha", Toast.LENGTH_SHORT).show();
        }

        Button btnBuscar = view.findViewById(R.id.btnbuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigoDeRastreio = ((TextInputEditText) view.findViewById(R.id.inputcodigo)).getText().toString();
                String urlRastreio = String.format("https://www.linkcorreios.com.br/?id=%s", codigoDeRastreio);

                RastreioEncomenda.rastrearEncomenda(urlRastreio);
            }
        });
    }
}