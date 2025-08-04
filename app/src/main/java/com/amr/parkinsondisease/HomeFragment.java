package com.amr.parkinsondisease;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Load the dark mode preference from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("settings", getContext().MODE_PRIVATE);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode", false);

        // Apply the theme based on the saved preference
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); // Dark mode
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);  // Light mode
        }

        // Initialize the "Read More" button
        Button readMoreButton = view.findViewById(R.id.readMoreButton);
        readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openParkinsonsWebsite();
            }
        });

        // Initialize the "Logout" button
        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear user session or credentials
                clearUserSession();

                // Navigate to the login screen
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                startActivity(intent);

                // Finish the current activity
                requireActivity().finish();
            }
        });

        // Animate the cards sequentially
        animateCardsSequentially(view);

        return view;
    }

    // Method to animate cards sequentially
    private void animateCardsSequentially(View view) {
        // Get references to the cards
        CardView waveCard = view.findViewById(R.id.waveCard);
        CardView spiralCard = view.findViewById(R.id.spiralCard);
        CardView voiceCard = view.findViewById(R.id.voiceCard);

        // Create a list of cards for sequential animation
        CardView[] cards = {waveCard, spiralCard, voiceCard};

        // Loop through the cards and animate them with a delay
        for (int i = 0; i < cards.length; i++) {
            CardView card = cards[i];
            if (card != null) {
                card.setAlpha(0f); // Start with 0 alpha (fully transparent)
                card.setTranslationY(50f); // Start slightly below the final position

                // Animate the card with a delay
                card.animate()
                        .alpha(1f) // Fade in
                        .translationY(0f) // Move to the original position
                        .setDuration(500) // Animation duration
                        .setStartDelay(i * 200L) // Delay between animations
                        .start();
            }
        }
    }

    // Method to open Parkinson's website
    private void openParkinsonsWebsite() {
        // Define the URL for the Parkinson's disease information
        String url = "https://www.parkinson.org"; // You can replace this with any relevant Parkinson's website

        // Create an Intent to open the website
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        // Start the activity to open the browser
        startActivity(browserIntent);
    }

    // Method to clear user session or credentials
    private void clearUserSession() {
        // Example: Clear SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false); // Mark user as logged out
        editor.apply();

        // If you're using a backend server, invalidate the session token here
    }
}