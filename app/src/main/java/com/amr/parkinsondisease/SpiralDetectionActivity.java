package com.amr.parkinsondisease;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SpiralDetectionActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private TextView spiralDetectionHeading, spiralDetectionResult, confidenceTextView;
    private ImageView spiralImageView;
    private Button selectSpiralGalleryButton, captureSpiralCameraButton, startSpiralDetectionButton;
    private Interpreter tfliteInterpreter;
    private Bitmap selectedImageBitmap;
    private Animation slideUpAnimation, fadeInAnimation;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral_detection);

        // Initialize the UI components
        spiralDetectionHeading = findViewById(R.id.spiralDetectionHeading);
        spiralImageView = findViewById(R.id.spiralImageView);
        selectSpiralGalleryButton = findViewById(R.id.selectGalleryButton);
        captureSpiralCameraButton = findViewById(R.id.captureCameraButton);
        startSpiralDetectionButton = findViewById(R.id.startDetectionButton);
        spiralDetectionResult = findViewById(R.id.resultTextView);
        confidenceTextView = findViewById(R.id.confidenceTextView);

        // Load animations
        slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Load the TensorFlow Lite model
        try {
            tfliteInterpreter = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load model", Toast.LENGTH_SHORT).show();
        }

        // Set onClickListener for Select Image from Gallery button
        selectSpiralGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Set onClickListener for Capture Image button
        captureSpiralCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermissionAndCaptureImage();
            }
        });

        // Set onClickListener for Start Detection button
        startSpiralDetectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageBitmap != null) {
                    startDetection(selectedImageBitmap);
                } else {
                    Toast.makeText(SpiralDetectionActivity.this,
                            "Please select an image first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private void checkCameraPermissionAndCaptureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void startDetection(Bitmap bitmap) {
        // Resize the bitmap to the model's input size (128x128)
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 128, 128, true);

        // Convert the bitmap to a ByteBuffer
        ByteBuffer inputBuffer = convertBitmapToByteBuffer(resizedBitmap);

        // Run inference
        float[][] output = new float[1][1];
        tfliteInterpreter.run(inputBuffer, output);

        // Get the prediction result
        float predictionValue = output[0][0];
        float confidence = predictionValue * 100; // Convert to percentage

        // Debugging: print the prediction value to see what the model is returning
        Log.d("Prediction Value", "Output from model: " + predictionValue);

        String prediction;
        if (predictionValue > 0.5) {
            prediction = "Parkinson's Disease Detected";
            spiralDetectionResult.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            prediction = "Healthy (No Parkinson's Detected)";
            spiralDetectionResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            confidence = (1 - predictionValue) * 100; // For healthy case, show confidence of not having Parkinson's
        }

        // Display the result with animations
        spiralDetectionResult.setText(prediction);
        confidenceTextView.setText(String.format("Confidence: %.1f%%", confidence));

        spiralDetectionResult.setVisibility(View.VISIBLE);
        confidenceTextView.setVisibility(View.VISIBLE);

        spiralDetectionResult.startAnimation(fadeInAnimation);
        confidenceTextView.startAnimation(fadeInAnimation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_IMAGE && data != null) {
                // Handle gallery image selection
                Uri imageUri = data.getData();
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                            this.getContentResolver(), imageUri);
                    spiralImageView.setImageBitmap(selectedImageBitmap);
                    spiralDetectionResult.setVisibility(View.INVISIBLE);
                    confidenceTextView.setVisibility(View.INVISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle camera capture image
                File imgFile = new File(currentPhotoPath);
                if (imgFile.exists()) {
                    selectedImageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    spiralImageView.setImageBitmap(selectedImageBitmap);
                    spiralDetectionResult.setVisibility(View.INVISIBLE);
                    confidenceTextView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(
                getAssets().openFd("SPIRAL_kashi_model.tflite").getFileDescriptor());
        FileChannel fileChannel = fileInputStream.getChannel();
        long startOffset = getAssets().openFd("SPIRAL_kashi_model.tflite").getStartOffset();
        long declaredLength = getAssets().openFd("SPIRAL_kashi_model.tflite").getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 128 * 128 * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[128 * 128];
        bitmap.getPixels(intValues, 0, 128, 0, 0, 128, 128);
        int pixel = 0;
        for (int i = 0; i < 128; ++i) {
            for (int j = 0; j < 128; ++j) {
                final int val = intValues[pixel++];
                byteBuffer.putFloat(((val >> 16) & 0xFF) / 255.0f);
                byteBuffer.putFloat(((val >> 8) & 0xFF) / 255.0f);
                byteBuffer.putFloat((val & 0xFF) / 255.0f);
            }
        }
        return byteBuffer;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}