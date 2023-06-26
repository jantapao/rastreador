package com.example.trabalhofinal.requests;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.trabalhofinal.interfaces.OnJsonResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RastreioEncomenda {
    private RastreioEncomenda() {
        throw new IllegalStateException("Utility Class");
    }

    public static void downloadImage(OkHttpClient client, String url, Context context) {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO - Tratar a Falha na Requisição
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    if (bitmap != null) {
                        saveImageToInternalStorage(bitmap, "imagem-captcha.png", context);
                    } else {
                        // TODO - Lidar com a falha no download da imagem
                    }
                }
            }
        });
    }

    private static void saveImageToInternalStorage(Bitmap bitmap, String fileName, Context context) {
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

    public static void rastrearEncomenda(OkHttpClient client, String url, final OnJsonResponseListener listener) {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO - Tratar a Falha da Request
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
