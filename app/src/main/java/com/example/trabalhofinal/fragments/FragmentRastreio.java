package com.example.trabalhofinal.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trabalhofinal.R;
import com.example.trabalhofinal.interfaces.OnJsonResponseListener;
import com.example.trabalhofinal.requests.RastreioEncomenda;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;

public class FragmentRastreio extends Fragment {
    private static final String ARG_PARAM1 = "codigo-rastreio";

    private String codigoDeRastreio;
    private RastreioEncomenda client;

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

        client = new RastreioEncomenda(getContext());
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

        txtCodigoRastreio.setText(codigoDeRastreio);
        resultadoCaptcha.setText("ABLUBLE");

        String urlImage = "https://rastreamento.correios.com.br/core/securimage/securimage_show.php";
        client.downloadImage(urlImage);

        ImageView imagemCaptcha = view.findViewById(R.id.imageView);

        // TODO - Exibir a Imagem na Tela

        Button btnBuscar = view.findViewById(R.id.btnbuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigoDeRastreio = txtCodigoRastreio.getText().toString();
                String valorCaptcha = resultadoCaptcha.getText().toString();
                String urlRastreio = String.format("https://rastreamento.correios.com.br/app/resultado.php?objeto=%s&captcha=%s&mqs=S", codigoDeRastreio, valorCaptcha);

                client.rastrearEncomenda(urlRastreio, new OnJsonResponseListener() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        // TODO -  Manipular a resposta JSON com sucesso

                        try {
                            String nome = jsonObject.getString("nome");
                            int idade = jsonObject.getInt("idade");

                            String mensagem = String.format("Nome: %s, Idade: %d", nome, idade);
                            System.out.println(mensagem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // TODO - Lidar com a falha na resposta JSON
                        System.out.println("Erro na resposta JSON: " + errorMessage);
                    }
                });
            }
        });
    }
}