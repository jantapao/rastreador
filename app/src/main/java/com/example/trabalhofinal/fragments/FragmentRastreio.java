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
import com.example.trabalhofinal.requests.CustomCookieStore;
import com.example.trabalhofinal.requests.PersistentCookieJar;
import com.example.trabalhofinal.requests.RastreioEncomenda;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.CookieStore;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class FragmentRastreio extends Fragment {
    private static final String ARG_PARAM1 = "codigo-rastreio";

    private String codigoDeRastreio;
    private OkHttpClient rastreamento;

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

    private void exibieCookies(OkHttpClient session) {
        CookieJar cookieJar = session.cookieJar();
        List<Cookie> cookies = cookieJar.loadForRequest(HttpUrl.parse("https://rastreamento.correios.com.br"));

        for (Cookie cookie : cookies) {
            String name = cookie.name();
            String value = cookie.value();
            Log.d("COOKIES", name + "=" + value);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rastreamento = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            private final CustomCookieStore cookieStore = new CustomCookieStore();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.saveCookies(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                return cookieStore.loadCookies(url.host());
            }
        }).build();

        exibieCookies(rastreamento);

        Log.d("DEBUG", "Fazendo a Request do Catpcha");

        String urlImage = "https://rastreamento.correios.com.br/core/securimage/securimage_show.php";
        RastreioEncomenda.downloadImage(rastreamento, urlImage, requireContext());

        exibieCookies(rastreamento);

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
                String valorCaptcha = ((TextInputEditText) view.findViewById(R.id.textInputEditText)).getText().toString();

                String urlRastreio = String.format("https://rastreamento.correios.com.br/app/resultado.php?objeto=%s&captcha=%s&mqs=S", codigoDeRastreio, valorCaptcha);

                exibieCookies(rastreamento);

                RastreioEncomenda.rastrearEncomenda(rastreamento, urlRastreio, new OnJsonResponseListener() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Log.d("URL", urlRastreio);
                        Log.d("JSON", jsonObject.toString());

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