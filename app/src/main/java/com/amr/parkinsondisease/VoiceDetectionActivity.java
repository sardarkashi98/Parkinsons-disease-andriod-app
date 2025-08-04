package com.amr.parkinsondisease;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileNotFoundException;
import android.content.res.AssetFileDescriptor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VoiceDetectionActivity extends AppCompatActivity {

    private Interpreter tflite;
    private EditText[] inputFields = new EditText[22];
    private TextView resultTextView;
    private TextView confidenceTextView;
    private ExecutorService executorService;
    private Button predictButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_detection); // Ensure that your layout file name is correct

        // Initialize input fields
        for (int i = 0; i < 22; i++) {
            String editTextID = "input" + (i + 1);
            int resID = getResources().getIdentifier(editTextID, "id", getPackageName());
            inputFields[i] = findViewById(resID);
        }

        resultTextView = findViewById(R.id.resultText);
        confidenceTextView = findViewById(R.id.confidenceText);
        predictButton = findViewById(R.id.predictButton);

        // Initialize thread executor
        executorService = Executors.newSingleThreadExecutor();

        // Load model
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading model", Toast.LENGTH_LONG).show();
        }

        // Set button click listener
        predictButton.setOnClickListener(v -> validateAndPredict());
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd("voice_frequency_detection.tflite");
            FileInputStream fileInputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = fileInputStream.getChannel();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.getStartOffset(), fileDescriptor.getDeclaredLength());
        } catch (FileNotFoundException e) {
            throw new IOException("Model file not found", e);
        }
    }

    private void validateAndPredict() {
        for (EditText field : inputFields) {
            if (field.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please fill all fields before predicting.", Toast.LENGTH_LONG).show();
                return;
            }
        }
        predict();
    }

    private void predict() {
        // Disable button to prevent multiple clicks
        predictButton.setEnabled(false);
        predictButton.setText("Processing...");

        executorService.execute(() -> {
            float[][] input = new float[1][22];

            try {
                for (int i = 0; i < 22; i++) {
                    String text = inputFields[i].getText().toString().trim();
                    input[0][i] = Float.parseFloat(text);
                }
            } catch (NumberFormatException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Invalid input. Please enter valid numbers.", Toast.LENGTH_LONG).show();
                    predictButton.setEnabled(true);
                    predictButton.setText("Predict");
                });
                return;
            }

            float[][] output = new float[1][1];
            tflite.run(input, output);

            runOnUiThread(() -> {
                // Get the model's output
                float confidence = output[0][0];

                // Determine the result and confidence percentage
                String result;
                int confidencePercentage;

                if (confidence >= 0.5) {
                    result = "Parkinson's Disease Detected";
                    confidencePercentage = (int) (confidence * 100); // Confidence for Parkinson's
                } else {
                    result = "Healthy";
                    confidencePercentage = (int) ((1 - confidence) * 100); // Confidence for Healthy
                }

                // Show result and confidence
                resultTextView.setText("Result: " + result);
                confidenceTextView.setText("Confidence: " + confidencePercentage + "%");
                animateResultText();

                // Re-enable button
                predictButton.setEnabled(true);
                predictButton.setText("Predict");
            });
        });
    }

    private void animateResultText() {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);
        resultTextView.startAnimation(fadeIn);
        confidenceTextView.startAnimation(fadeIn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tflite != null) {
            tflite.close();
        }
        executorService.shutdown();
    }
}
