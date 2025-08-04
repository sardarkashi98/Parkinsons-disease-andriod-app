package com.amr.parkinsondisease;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private boolean isEditMode = false;

    // UI Components
    private CircleImageView profileCircleImageView;
    private TextView usernameTextView;
    private Switch notificationsSwitch;
    private TextView editProfileTextView;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button saveButton;
    private TextView logoutTextView;
    private TextView languageTextView;
    private TextView contactEmailTextView;
    private TextView contactWhatsAppTextView;
    private LinearLayout chatBotOption;
    private LinearLayout nearbyHospitalsOption;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        initializeViews(view);
        loadUserSettings();
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        profileCircleImageView = view.findViewById(R.id.profileCircleImageView);
        usernameTextView = view.findViewById(R.id.usernameTextView);
        notificationsSwitch = view.findViewById(R.id.notificationsSwitch);
        editProfileTextView = view.findViewById(R.id.editProfileTextView);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        currentPasswordEditText = view.findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = view.findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
        saveButton = view.findViewById(R.id.saveButton);
        logoutTextView = view.findViewById(R.id.logoutTextView);
        languageTextView = view.findViewById(R.id.languageTextView);
        contactEmailTextView = view.findViewById(R.id.contactEmailTextView);
        contactWhatsAppTextView = view.findViewById(R.id.contactWhatsAppTextView);
        chatBotOption = view.findViewById(R.id.chatBotOption);
        nearbyHospitalsOption = view.findViewById(R.id.nearbyHospitalsOption);
    }

    private void loadUserSettings() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserSettings", getContext().MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "Muqdus Zafar");
        usernameTextView.setText(savedUsername);
        usernameEditText.setText(savedUsername);

        // Load email if exists
        String savedEmail = sharedPreferences.getString("email", "");
        emailEditText.setText(savedEmail);

        // Load profile image if exists
        String imageUriString = sharedPreferences.getString("profileImageUri", null);
        if (imageUriString != null) {
            try {
                Uri imageUri = Uri.parse(imageUriString);
                // Check if the URI is a file path or content URI
                if (imageUriString.startsWith("content://") || imageUriString.startsWith("file://")) {
                    try {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        profileCircleImageView.setImageBitmap(bitmap);
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (FileNotFoundException e) {
                        // File not found, use default image
                        profileCircleImageView.setImageResource(R.drawable.user_dp);
                    }
                } else {
                    // Try to load as file path
                    File imgFile = new File(imageUriString);
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        profileCircleImageView.setImageBitmap(bitmap);
                    } else {
                        profileCircleImageView.setImageResource(R.drawable.user_dp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                profileCircleImageView.setImageResource(R.drawable.user_dp);
            }
        } else {
            // No saved image, use default
            profileCircleImageView.setImageResource(R.drawable.user_dp);
        }

        // Load notification preference
        boolean notificationsEnabled = sharedPreferences.getBoolean("notificationsEnabled", true);
        notificationsSwitch.setChecked(notificationsEnabled);
    }

    private void setupClickListeners() {
        // Profile Image Click
        profileCircleImageView.setOnClickListener(v -> openImagePicker());

        // Edit Profile Click
        editProfileTextView.setOnClickListener(v -> toggleEditMode());

        // Save Button Click
        saveButton.setOnClickListener(v -> {
            saveProfileChanges();
            toggleEditMode(); // Exit edit mode after saving
        });

        // Notifications Switch
        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveNotificationPreference(isChecked);
            Toast.makeText(getContext(),
                    isChecked ? "Notifications enabled" : "Notifications disabled",
                    Toast.LENGTH_SHORT).show();
        });

        // Language Selection
        languageTextView.setOnClickListener(v -> showLanguageSelectionDialog());

        // Contact Options
        contactEmailTextView.setOnClickListener(v -> contactViaEmail());
        contactWhatsAppTextView.setOnClickListener(v -> contactViaWhatsApp());

        // Nearby Hospitals Option
        nearbyHospitalsOption.setOnClickListener(v -> openNearbyHospitalsMap());

        // Chat Bot Option
        chatBotOption.setOnClickListener(v -> launchChatBot());

        // Logout
        logoutTextView.setOnClickListener(v -> performLogout());
    }

    private void toggleEditMode() {
        isEditMode = !isEditMode;

        if (isEditMode) {
            // Show edit fields
            usernameEditText.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.VISIBLE);
            currentPasswordEditText.setVisibility(View.VISIBLE);
            newPasswordEditText.setVisibility(View.VISIBLE);
            confirmPasswordEditText.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);

            // Set current values
            usernameEditText.setText(usernameTextView.getText().toString());
        } else {
            // Hide edit fields
            usernameEditText.setVisibility(View.GONE);
            emailEditText.setVisibility(View.GONE);
            currentPasswordEditText.setVisibility(View.GONE);
            newPasswordEditText.setVisibility(View.GONE);
            confirmPasswordEditText.setVisibility(View.GONE);
            saveButton.setVisibility(View.GONE);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openNearbyHospitalsMap() {
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        startActivity(intent);
    }

    private void saveProfileChanges() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserSettings", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save username if changed
        String newUsername = usernameEditText.getText().toString();
        if (!newUsername.isEmpty()) {
            editor.putString("username", newUsername);
            usernameTextView.setText(newUsername);
        }

        // Save email if changed
        String newEmail = emailEditText.getText().toString();
        if (!newEmail.isEmpty()) {
            editor.putString("email", newEmail);
        }

        // Save password if changed and validated
        String currentPassword = currentPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (!currentPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
            if (newPassword.equals(confirmPassword)) {
                // TODO: Implement proper password change logic with validation
                // For now, just clear the fields
                currentPasswordEditText.setText("");
                newPasswordEditText.setText("");
                confirmPasswordEditText.setText("");
                Toast.makeText(getContext(), "Password updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        editor.apply();
        Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
    }

    private void saveNotificationPreference(boolean enabled) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserSettings", getContext().MODE_PRIVATE);
        sharedPreferences.edit()
                .putBoolean("notificationsEnabled", enabled)
                .apply();
    }

    private void showLanguageSelectionDialog() {
        final String[] languages = {"English"}; // Currently only English supported
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose Language")
                .setItems(languages, (dialog, which) -> {
                    String selectedLanguage = languages[which];
                    Toast.makeText(getContext(), "Selected: " + selectedLanguage, Toast.LENGTH_SHORT).show();
                    // TODO: Implement language change if needed
                })
                .show();
    }

    private void contactViaEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:sardarkashi991@gmail.com"));
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    private void contactViaWhatsApp() {
        String url = "https://api.whatsapp.com/send?phone=+923128882735";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void launchChatBot() {
        Intent intent = new Intent(getActivity(), ChatBotActivity.class);
        startActivity(intent);
    }

    private void performLogout() {
        // Clear user session
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        sharedPreferences.edit()
                .putBoolean("isLoggedIn", false)
                .apply();

        // Navigate to login screen
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    // Get the image from the URI
                    InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    profileCircleImageView.setImageBitmap(bitmap);
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    // Save the image URI
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserSettings", getContext().MODE_PRIVATE);
                    sharedPreferences.edit()
                            .putString("profileImageUri", imageUri.toString())
                            .apply();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}