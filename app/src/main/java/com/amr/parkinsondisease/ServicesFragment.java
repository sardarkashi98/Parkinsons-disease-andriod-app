package com.amr.parkinsondisease;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ServicesFragment extends Fragment {

    private TextView typingTextView;
    private CharSequence typingText = "Choose Detection Method ";
    private int typingIndex = 0;
    private Handler typingHandler = new Handler();
    private long typingDelay = 150; // Delay between each character

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        // Initialize buttons
        Button spiralButton = view.findViewById(R.id.spiralImageDetectionButton);
        Button waveButton = view.findViewById(R.id.waveImageDetectionButton);
        Button voiceButton = view.findViewById(R.id.voiceFrequencyButton);

        // Set click listeners for buttons
        spiralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Spiral Image Detection button click
                startActivity(new Intent(getActivity(), SpiralDetectionActivity.class));
            }
        });

        waveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Wave Image Detection button click
                startActivity(new Intent(getActivity(), WaveDetectionActivity.class));
            }
        });

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Voice Frequency Detection button click
                startActivity(new Intent(getActivity(), VoiceDetectionActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Floating Action Button (FAB)
        FloatingActionButton fab = view.findViewById(R.id.fab);

        // Set click listener for FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle FAB click event
                // Open ReviewActivity
                startActivity(new Intent(getActivity(), ReviewActivity.class));
            }
        });

        // Initialize Typing Effect TextView
        typingTextView = view.findViewById(R.id.typingTextView);
        startTypingEffect();
    }

    private void startTypingEffect() {
        if (typingIndex < typingText.length()) {
            typingTextView.setText(typingText.subSequence(0, typingIndex++));
            typingHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startTypingEffect();
                }
            }, typingDelay);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove any pending callbacks to avoid memory leaks
        typingHandler.removeCallbacksAndMessages(null);
    }
}