package com.example.imagetotext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.predictions.models.TextFormatType;
import com.amplifyframework.predictions.result.IdentifyDocumentTextResult;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.concurrent.CountDownLatch;

public class HomeFragment extends Fragment {

    Button open_camera_button;
    TextView open_camera_text;
    private LoadingDialog loadingDialog;
    static boolean wait_for_result;

    protected static IdentifyDocumentTextResult text_result;

    protected static Thread result_thread;
    AdView adView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        open_camera_button = view.findViewById(R.id.gallery_button);
        open_camera_text = view.findViewById(R.id.upload_text);
        loadingDialog = new LoadingDialog(getActivity());
        text_result = null;
        wait_for_result = false;

        AdRequest banner_add_request = new AdRequest.Builder().build();
        adView = view.findViewById(R.id.banner_add_view);
        adView.loadAd(banner_add_request);

        open_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_get_image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                try {
                    getImageActivityLauncher.launch(intent_get_image);
                }catch (Exception e){
                    Log.i("MyAmplifyApp", String.valueOf(e));
                }
            }
        });

        open_camera_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_get_image = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                try {
                    getImageActivityLauncher.launch(intent_get_image);
                }catch (Exception e){
                    Log.i("MyAmplifyApp", String.valueOf(e));
                }
            }
        });




        return view;
    }


    ActivityResultLauncher<Intent> getImageActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.i("MyAmplifyApp", "onActivityResult:"+ result);
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Log.i("MyAmplifyApp----", String.valueOf(data));
                        try {

//                            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//                            cropIntent.setDataAndType(data.getData(), "image/*");
//                            cropIntent.putExtra("crop", "true"); // Enable crop functionality
//                            someActivityResultLauncher.launch(cropIntent);
                            Bitmap img = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());



                            loadingDialog.show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    detectText(img);
                                }
                            }).start();

//                            detectText(img);

                            Log.d("MyAmplifyApp", "onActivityResult: ====");
                        }catch (Exception e){
                            Log.e("MyAmplifyApp", String.valueOf(e));
                        }

                    }
                }
            });



    public void detectText(Bitmap image) {
        final CountDownLatch latch = new CountDownLatch(1);

        Amplify.Predictions.identify(
                TextFormatType.ALL,
                image,
                result -> {

                    IdentifyDocumentTextResult identifyResult = (IdentifyDocumentTextResult) result;
                    loadingDialog.cancel();
                    Log.i("MyAmplifyApp", String.valueOf(identifyResult.getRawLineText()));
                    this.text_result = identifyResult;
                    latch.countDown(); // This will decrease the count of latch by 1

                },
                error ->{ Log.e("MyAmplifyApp", "Identify failed", error);
                latch.countDown(); // This will decrease the count of latch by 1

                });

        try {
            latch.await(); // This will make the current thread wait until the latch count reaches to 0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switchToResultFragment();
//                ResultFragment.identifyDocumentTextResult = text_result; 
            }
        });
        Log.d("MyAmplifyApp", "detectText: ------------------");
    }

    private void switchToResultFragment() {
//        ((MainActivity)getActivity()).loadFragment(new ResultFragment(),false);
         ((MainActivity)getActivity()).bottomNavigationView.setSelectedItemId(R.id.nav_result);
//        getActivity().getSupportFragmentManager().se
    }

}