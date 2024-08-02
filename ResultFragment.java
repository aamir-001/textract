package com.example.imagetotext;

import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.predictions.result.IdentifyDocumentTextResult;
import com.google.android.material.textfield.TextInputEditText;

public class ResultFragment extends Fragment {

    protected static IdentifyDocumentTextResult identifyDocumentTextResult;
    TextInputEditText result_text_box;
    Button copy_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        result_text_box = view.findViewById(R.id.result_text_view);
        copy_button = view.findViewById(R.id.copyButton);
//        result_text_box.
        identifyDocumentTextResult = null;
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        result_text_box.setText(String.valueOf(""));

        try {
            identifyDocumentTextResult = HomeFragment.text_result;
            String res = String.valueOf(identifyDocumentTextResult.getRawLineText());
            res = res.substring(1,res.length() - 1);
            result_text_box.setText(res);
            Log.d("MyAmplifyApp", "--------->>>" + res);
            copy_button.setEnabled(true);
        } catch (Exception e) {
//            result_text_box
        }

        copy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("Copied Text", result_text_box.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(requireContext(), "Text Copied!" , Toast.LENGTH_SHORT).show();

            }
        });



        return view;
    }
}