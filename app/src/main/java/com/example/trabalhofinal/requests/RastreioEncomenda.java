package com.example.trabalhofinal.requests;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RastreioEncomenda {
    private RastreioEncomenda() {
        throw new IllegalStateException("Utility Class");
    }

    public static void downloadImage(String url, Context context) {
        Request request = new Request.Builder().url(url).build();

        new OkHttpClient.Builder().build().newCall(request).enqueue(new Callback() {
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

    public static String rastrearEncomenda(String url, Context context) {
        Request request = new Request.Builder().url(url).build();

        Future<String> future = Executors.newSingleThreadExecutor().submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    Response response = new OkHttpClient.Builder().build().newCall(request).execute();

                    if (response.isSuccessful()) {
                        String HTML = response.body().string();

                        Document page = Jsoup.parse(HTML);
                        Element cardHeader = page.select("div.card-header").first();

                        return cardHeader.text();
                    } else {
                        // TODO - Tratar a Resposta Incorreta
                    }
                } catch (IOException e) {
                    Toast.makeText(context, "Houve um erro na requisição", Toast.LENGTH_SHORT).show();
                }

                return null;
            }
        });

        String resultadoRastreamento;

        try {
            resultadoRastreamento = future.get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO - Tratar a Exception
            resultadoRastreamento = null;
        }

        return resultadoRastreamento;
    }
}
