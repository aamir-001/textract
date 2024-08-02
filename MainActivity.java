package com.example.imagetotext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.predictions.aws.AWSPredictionsPlugin;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    protected   BottomNavigationView bottomNavigationView;
    protected Toolbar myToolbar;
    private FrameLayout frameLayout;
    public static final String TAG = "MyAmplifyApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frame_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (Build.VERSION.SDK_INT >= 21)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.statusbar)); //status bar or the time bar at the top (see example image1)

            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.statusbar)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series  (see example image2)
        }

        addAmplify();

        loadFragment(new HomeFragment(),true);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "onInitializationComplete: ");
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){

                    case R.id.nav_home:
                        loadFragment(new HomeFragment(), false);
                        break;
                    case R.id.nav_result:
                        loadFragment(new ResultFragment(), false);
                        break;
                }
                return true;
            }
        });

    }

    protected Fragment loadFragment(Fragment fragment, boolean appInitialised) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (appInitialised){
            fragmentTransaction.add(R.id.frame_layout,fragment);
        }else {
            fragmentTransaction.replace(R.id.frame_layout,fragment);
        }
        fragmentTransaction.commit();
        return fragment;
    }


    private void addAmplify() {
        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSPredictionsPlugin plugins
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSPredictionsPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
    }

}