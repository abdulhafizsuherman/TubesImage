package com.d.tubesTranslatorImage;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.d.tubesTranslatorImage.yandexAPI.BASE_REQ_URL;

//import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

public class translateActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button translate, resetBtn;
    private ImageView imageView;
    private TextView mTextTranslated, backBtn;
    private EditText editText;
    private String mLanguageCodeFrom = "en";                //    Language Code (From)
    private String mLanguageCodeTo = "jp";                  // Language Code (To)
    volatile boolean activityRunning;                       //    To track status of current activity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRunning=true;
        setContentView(R.layout.translate_layout);
        mTextTranslated = findViewById(R.id.text_translated);
        resetBtn = findViewById(R.id.reset_button);

        imageView = findViewById(R.id.image_view);
        translate = findViewById(R.id.translate_button);
        backBtn = findViewById(R.id.back_button);
        editText = findViewById(R.id.edit_text);

        //Mengambil data text dari gambar ke ediText
        Bundle bn = getIntent().getExtras();
        String text = bn.getString("KEY_TEXT");
        editText.setText(String.valueOf(text));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(translateActivity.this, detectImageActivity.class);
                startActivity(i);
            }
        });




        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                new translateText().execute(input);
            }
        });

    }


    private class translateText extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... input) {
            Uri baseUri = Uri.parse(BASE_REQ_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendPath("translate")
                    .appendQueryParameter("key",getString(R.string.API_KEY))
                    .appendQueryParameter("lang",mLanguageCodeFrom+"-"+mLanguageCodeTo)
                    .appendQueryParameter("text",input[0]);
            Log.e("String Url ---->",uriBuilder.toString());
            return QueryUtils.fetchTranslation(uriBuilder.toString());
        }
        @Override
        protected void onPostExecute(String result) {
                mTextTranslated.setText(result);
        }
    }
 }











