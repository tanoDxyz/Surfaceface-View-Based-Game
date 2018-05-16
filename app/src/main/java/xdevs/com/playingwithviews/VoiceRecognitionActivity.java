package xdevs.com.playingwithviews;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

public class VoiceRecognitionActivity extends AppCompatActivity {

    private Button speecRecognizerButton;
    private SpeechRecognizer speechRecognizer;
    private TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this,GameActivity.class));
        finish();
        resultTextView = findViewById(R.id.resultTextView);


        speecRecognizerButton = findViewById(R.id.srecognizer);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                final ArrayList<String> strings = results.getStringArrayList(RESULTS_RECOGNITION);

                for(String str:strings) {
                    resultTextView.append(str + " , ");
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });



    }

    public void onSpeechRecognitionLaunced(View view) {
        promptSpeechInput();

    }

    public void onSpeechRecognitionClose(View view) {
        this.speechRecognizer.stopListening();
    }


    public void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }



    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        speechRecognizer.startListening(intent);


    }

    /**
     * Receiving speech input
     * */

}
