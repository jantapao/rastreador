package com.example.trabalhofinal.requests;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.trabalhofinal.interfaces.OnJsonResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RastreioEncomenda {
    private OkHttpClient client;
    private Context context;

    public RastreioEncomenda(Context context) {
        this.context = context;
        client = createOkHttpClient();
    }

    private OkHttpClient createOkHttpClient() {
        CookieJar cookieJar = new CookieJar() {
            private final Map<String, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                return cookies != null ? cookies : new ArrayList<>();
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.cookieJar(cookieJar);

        return builder.build();
    }

    public void downloadImage(String url) {
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO - Lidar com o erro de requisição
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    if (bitmap != null) {
                        saveImageToInternalStorage(bitmap, "imagem.png");
                    } else {
                        // TODO - Lidar com a falha no download da imagem
                    }
                }
            }
        });
    }

    private void saveImageToInternalStorage(Bitmap bitmap, String fileName) {
        try {
            File directory = context.getFilesDir();
            File file = new File(directory, fileName);

            OutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rastrearEncomenda(String url, final OnJsonResponseListener listener) {
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        listener.onSuccess(jsonObject);
                    } catch (JSONException e) {
                        listener.onFailure(e.getMessage());
                    }
                } else {
                    listener.onFailure("Erro na resposta do servidor: " + response.code());
                }
            }
        });
    }
}
