package com.example.imagetotext;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.imagetotext.databinding.ActivitySplashScreenBinding;

public class SplashScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Set your splash screen layout (XML file)
        setContentView(R.layout.activity_splash_screen);

        // Delayed transition to the main activity (adjust the duration as needed)
        new android.os.Handler().postDelayed(() -> {
            // Start your main activity here
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the splash screen activity
        }, 1000); // 3000 milliseconds delay (adjust as desired)
    }

}