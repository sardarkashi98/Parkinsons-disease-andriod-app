package com.amr.parkinsondisease;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

public class ExerciseActivity extends AppCompatActivity {

    private Button exercise1StartButton, exercise1StopButton;
    private Button exercise2StartButton, exercise2StopButton;
    private Button exercise3StartButton, exercise3StopButton;
    private Button exercise4StartButton, exercise4StopButton;
    private Button exercise5StartButton, exercise5StopButton;
    private Button exercise6StartButton, exercise6StopButton;
    private Button exercise7StartButton, exercise7StopButton;

    private TextView exercise1DurationTextView, exercise2DurationTextView, exercise3DurationTextView, exercise4DurationTextView;
    private TextView exercise5DurationTextView, exercise6DurationTextView, exercise7DurationTextView;

    private ProgressBar exercise1ProgressBar, exercise2ProgressBar, exercise3ProgressBar, exercise4ProgressBar;
    private ProgressBar exercise5ProgressBar, exercise6ProgressBar, exercise7ProgressBar;

    private TextView headingTextView;
    private CountDownTimer exercise1Timer, exercise2Timer, exercise3Timer, exercise4Timer;
    private CountDownTimer exercise5Timer, exercise6Timer, exercise7Timer;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Initialize UI elements
        initializeViews();

        // Load GIF images using Glide
        loadGifImages();

        // Set click listeners for buttons
        setButtonListeners();

        // Animate the heading
        animateHeading();

        // Animate the cards
        animateCards();

        // Set Floating Action Button (FAB) click listener
        setFabClickListener();
    }

    /**
     * Initialize all UI elements.
     */
    private void initializeViews() {
        exercise1StartButton = findViewById(R.id.exercise1_start);
        exercise1StopButton = findViewById(R.id.exercise1_stop);
        exercise2StartButton = findViewById(R.id.exercise2_start);
        exercise2StopButton = findViewById(R.id.exercise2_stop);
        exercise3StartButton = findViewById(R.id.exercise3_start);
        exercise3StopButton = findViewById(R.id.exercise3_stop);
        exercise4StartButton = findViewById(R.id.exercise4_start);
        exercise4StopButton = findViewById(R.id.exercise4_stop);
        exercise5StartButton = findViewById(R.id.exercise5_start);
        exercise5StopButton = findViewById(R.id.exercise5_stop);
        exercise6StartButton = findViewById(R.id.exercise6_start);
        exercise6StopButton = findViewById(R.id.exercise6_stop);
        exercise7StartButton = findViewById(R.id.exercise7_start);
        exercise7StopButton = findViewById(R.id.exercise7_stop);

        exercise1DurationTextView = findViewById(R.id.exercise1_duration);
        exercise2DurationTextView = findViewById(R.id.exercise2_duration);
        exercise3DurationTextView = findViewById(R.id.exercise3_duration);
        exercise4DurationTextView = findViewById(R.id.exercise4_duration);
        exercise5DurationTextView = findViewById(R.id.exercise5_duration);
        exercise6DurationTextView = findViewById(R.id.exercise6_duration);
        exercise7DurationTextView = findViewById(R.id.exercise7_duration);

        exercise1ProgressBar = findViewById(R.id.exercise1_progress);
        exercise2ProgressBar = findViewById(R.id.exercise2_progress);
        exercise3ProgressBar = findViewById(R.id.exercise3_progress);
        exercise4ProgressBar = findViewById(R.id.exercise4_progress);
        exercise5ProgressBar = findViewById(R.id.exercise5_progress);
        exercise6ProgressBar = findViewById(R.id.exercise6_progress);
        exercise7ProgressBar = findViewById(R.id.exercise7_progress);

        headingTextView = findViewById(R.id.heading);
        fab = findViewById(R.id.fab);
    }

    /**
     * Load GIF images using Glide.
     */
    private void loadGifImages() {
        ShapeableImageView exercise1Image = findViewById(R.id.exercise1_image);
        ShapeableImageView exercise2Image = findViewById(R.id.exercise2_image);
        ShapeableImageView exercise3Image = findViewById(R.id.exercise3_image);
        ShapeableImageView exercise4Image = findViewById(R.id.exercise4_image);
        ShapeableImageView exercise5Image = findViewById(R.id.exercise5_image);
        ShapeableImageView exercise6Image = findViewById(R.id.exercise6_image);
        ShapeableImageView exercise7Image = findViewById(R.id.exercise7_image);

        Glide.with(this).load(R.drawable.balance212).into(exercise1Image);
        Glide.with(this).load(R.drawable.balance342).into(exercise2Image);
        Glide.with(this).load(R.drawable.balnce45).into(exercise3Image);
        Glide.with(this).load(R.drawable.balance1211).into(exercise4Image);
        Glide.with(this).load(R.drawable.balance1200).into(exercise5Image);
        Glide.with(this).load(R.drawable.balance130).into(exercise6Image);
        Glide.with(this).load(R.drawable.balance2311).into(exercise7Image);
    }

    /**
     * Set click listeners for buttons.
     */
    private void setButtonListeners() {
        // Exercise 1 Start Button
        exercise1StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise1Timer();
            }
        });

        // Exercise 1 Stop Button
        exercise1StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopExercise1Timer();
            }
        });

        // Exercise 2 Start Button
        exercise2StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise2Timer();
            }
        });

        // Exercise 2 Stop Button
        exercise2StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopExercise2Timer();
            }
        });

        // Exercise 3 Start Button
        exercise3StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise3Timer();
            }
        });

        // Exercise 3 Stop Button
        exercise3StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopExercise3Timer();
            }
        });

        // Exercise 4 Start Button
        exercise4StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise4Timer();
            }
        });

        // Exercise 4 Stop Button
        exercise4StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopExercise4Timer();
            }
        });

        // Exercise 5 Start Button
        exercise5StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise5Timer();
            }
        });

        // Exercise 5 Stop Button
        exercise5StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopExercise5Timer();
            }
        });

        // Exercise 6 Start Button
        exercise6StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise6Timer();
            }
        });

        // Exercise 6 Stop Button
        exercise6StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopExercise6Timer();
            }
        });

        // Exercise 7 Start Button
        exercise7StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise7Timer();
            }
        });

        // Exercise 7 Stop Button
        exercise7StopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopExercise7Timer();
            }
        });
    }

    /**
     * Start the timer for Exercise 1.
     */
    private void startExercise1Timer() {
        if (exercise1Timer != null) {
            exercise1Timer.cancel();
        }

        exercise1StartButton.setVisibility(View.GONE);
        exercise1StopButton.setVisibility(View.VISIBLE);

        final long totalDuration = 60000; // 60 seconds
        exercise1Timer = new CountDownTimer(totalDuration, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                exercise1DurationTextView.setText("Duration: " + secondsRemaining + " seconds");

                // Update progress bar
                int progress = (int) ((totalDuration - millisUntilFinished) * 100 / totalDuration);
                exercise1ProgressBar.setProgress(progress);
            }

            public void onFinish() {
                exercise1DurationTextView.setText("Duration: 0 seconds");
                exercise1ProgressBar.setProgress(100);
                exercise1StopButton.setVisibility(View.GONE);
                exercise1StartButton.setVisibility(View.VISIBLE);
                Toast.makeText(ExerciseActivity.this, "Stretching Exercises completed!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    /**
     * Stop the timer for Exercise 1.
     */
    private void stopExercise1Timer() {
        if (exercise1Timer != null) {
            exercise1Timer.cancel();
        }
        exercise1StopButton.setVisibility(View.GONE);
        exercise1StartButton.setVisibility(View.VISIBLE);
    }

    /**
     * Start the timer for Exercise 2.
     */
    private void startExercise2Timer() {
        if (exercise2Timer != null) {
            exercise2Timer.cancel();
        }

        exercise2StartButton.setVisibility(View.GONE);
        exercise2StopButton.setVisibility(View.VISIBLE);

        final long totalDuration = 900000; // 15 minutes (900,000 milliseconds)
        exercise2Timer = new CountDownTimer(totalDuration, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                exercise2DurationTextView.setText("Duration: " + minutes + " minutes " + seconds + " seconds");

                // Update progress bar
                int progress = (int) ((totalDuration - millisUntilFinished) * 100 / totalDuration);
                exercise2ProgressBar.setProgress(progress);
            }

            public void onFinish() {
                exercise2DurationTextView.setText("Duration: 0 minutes 0 seconds");
                exercise2ProgressBar.setProgress(100);
                exercise2StopButton.setVisibility(View.GONE);
                exercise2StartButton.setVisibility(View.VISIBLE);
                Toast.makeText(ExerciseActivity.this, "Walking Exercises completed!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    /**
     * Stop the timer for Exercise 2.
     */
    private void stopExercise2Timer() {
        if (exercise2Timer != null) {
            exercise2Timer.cancel();
        }
        exercise2StopButton.setVisibility(View.GONE);
        exercise2StartButton.setVisibility(View.VISIBLE);
    }

    /**
     * Start the timer for Exercise 3.
     */
    private void startExercise3Timer() {
        if (exercise3Timer != null) {
            exercise3Timer.cancel();
        }

        exercise3StartButton.setVisibility(View.GONE);
        exercise3StopButton.setVisibility(View.VISIBLE);

        final long totalDuration = 60000; // 60 seconds
        exercise3Timer = new CountDownTimer(totalDuration, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                exercise3DurationTextView.setText("Duration: " + secondsRemaining + " seconds");

                // Update progress bar
                int progress = (int) ((totalDuration - millisUntilFinished) * 100 / totalDuration);
                exercise3ProgressBar.setProgress(progress);
            }

            public void onFinish() {
                exercise3DurationTextView.setText("Duration: 0 seconds");
                exercise3ProgressBar.setProgress(100);
                exercise3StopButton.setVisibility(View.GONE);
                exercise3StartButton.setVisibility(View.VISIBLE);
                Toast.makeText(ExerciseActivity.this, "Balance Exercises completed!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    /**
     * Stop the timer for Exercise 3.
     */
    private void stopExercise3Timer() {
        if (exercise3Timer != null) {
            exercise3Timer.cancel();
        }
        exercise3StopButton.setVisibility(View.GONE);
        exercise3StartButton.setVisibility(View.VISIBLE);
    }

    /**
     * Start the timer for Exercise 4.
     */
    private void startExercise4Timer() {
        if (exercise4Timer != null) {
            exercise4Timer.cancel();
        }

        exercise4StartButton.setVisibility(View.GONE);
        exercise4StopButton.setVisibility(View.VISIBLE);

        final long totalDuration = 60000; // 60 seconds
        exercise4Timer = new CountDownTimer(totalDuration, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                exercise4DurationTextView.setText("Duration: " + secondsRemaining + " seconds");

                // Update progress bar
                int progress = (int) ((totalDuration - millisUntilFinished) * 100 / totalDuration);
                exercise4ProgressBar.setProgress(progress);
            }

            public void onFinish() {
                exercise4DurationTextView.setText("Duration: 0 seconds");
                exercise4ProgressBar.setProgress(100);
                exercise4StopButton.setVisibility(View.GONE);
                exercise4StartButton.setVisibility(View.VISIBLE);
                Toast.makeText(ExerciseActivity.this, "Strength Training completed!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    /**
     * Stop the timer for Exercise 4.
     */
    private void stopExercise4Timer() {
        if (exercise4Timer != null) {
            exercise4Timer.cancel();
        }
        exercise4StopButton.setVisibility(View.GONE);
        exercise4StartButton.setVisibility(View.VISIBLE);
    }

    /**
     * Start the timer for Exercise 5.
     */
    private void startExercise5Timer() {
        if (exercise5Timer != null) {
            exercise5Timer.cancel();
        }

        exercise5StartButton.setVisibility(View.GONE);
        exercise5StopButton.setVisibility(View.VISIBLE);

        final long totalDuration = 60000; // 60 seconds
        exercise5Timer = new CountDownTimer(totalDuration, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                exercise5DurationTextView.setText("Duration: " + secondsRemaining + " seconds");

                // Update progress bar
                int progress = (int) ((totalDuration - millisUntilFinished) * 100 / totalDuration);
                exercise5ProgressBar.setProgress(progress);
            }

            public void onFinish() {
                exercise5DurationTextView.setText("Duration: 0 seconds");
                exercise5ProgressBar.setProgress(100);
                exercise5StopButton.setVisibility(View.GONE);
                exercise5StartButton.setVisibility(View.VISIBLE);
                Toast.makeText(ExerciseActivity.this, "Exercise 5 completed!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    /**
     * Stop the timer for Exercise 5.
     */
    private void stopExercise5Timer() {
        if (exercise5Timer != null) {
            exercise5Timer.cancel();
        }
        exercise5StopButton.setVisibility(View.GONE);
        exercise5StartButton.setVisibility(View.VISIBLE);
    }

    /**
     * Start the timer for Exercise 6.
     */
    private void startExercise6Timer() {
        if (exercise6Timer != null) {
            exercise6Timer.cancel();
        }

        exercise6StartButton.setVisibility(View.GONE);
        exercise6StopButton.setVisibility(View.VISIBLE);

        final long totalDuration = 60000; // 60 seconds
        exercise6Timer = new CountDownTimer(totalDuration, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                exercise6DurationTextView.setText("Duration: " + secondsRemaining + " seconds");

                // Update progress bar
                int progress = (int) ((totalDuration - millisUntilFinished) * 100 / totalDuration);
                exercise6ProgressBar.setProgress(progress);
            }

            public void onFinish() {
                exercise6DurationTextView.setText("Duration: 0 seconds");
                exercise6ProgressBar.setProgress(100);
                exercise6StopButton.setVisibility(View.GONE);
                exercise6StartButton.setVisibility(View.VISIBLE);
                Toast.makeText(ExerciseActivity.this, "Exercise 6 completed!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    /**
     * Stop the timer for Exercise 6.
     */
    private void stopExercise6Timer() {
        if (exercise6Timer != null) {
            exercise6Timer.cancel();
        }
        exercise6StopButton.setVisibility(View.GONE);
        exercise6StartButton.setVisibility(View.VISIBLE);
    }

    /**
     * Start the timer for Exercise 7.
     */
    private void startExercise7Timer() {
        if (exercise7Timer != null) {
            exercise7Timer.cancel();
        }

        exercise7StartButton.setVisibility(View.GONE);
        exercise7StopButton.setVisibility(View.VISIBLE);

        final long totalDuration = 60000; // 60 seconds
        exercise7Timer = new CountDownTimer(totalDuration, 1000) {
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                exercise7DurationTextView.setText("Duration: " + secondsRemaining + " seconds");

                // Update progress bar
                int progress = (int) ((totalDuration - millisUntilFinished) * 100 / totalDuration);
                exercise7ProgressBar.setProgress(progress);
            }

            public void onFinish() {
                exercise7DurationTextView.setText("Duration: 0 seconds");
                exercise7ProgressBar.setProgress(100);
                exercise7StopButton.setVisibility(View.GONE);
                exercise7StartButton.setVisibility(View.VISIBLE);
                Toast.makeText(ExerciseActivity.this, "Exercise 7 completed!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    /**
     * Stop the timer for Exercise 7.
     */
    private void stopExercise7Timer() {
        if (exercise7Timer != null) {
            exercise7Timer.cancel();
        }
        exercise7StopButton.setVisibility(View.GONE);
        exercise7StartButton.setVisibility(View.VISIBLE);
    }

    /**
     * Animate the heading text view.
     */
    private void animateHeading() {
        headingTextView.setAlpha(0f);
        headingTextView.setTranslationY(-50f);

        headingTextView.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1000)
                .start();
    }

    /**
     * Animate the cards to fade in and scale up.
     */
    private void animateCards() {
        View[] cards = {
                findViewById(R.id.exercise1_card),
                findViewById(R.id.exercise2_card),
                findViewById(R.id.exercise3_card),
                findViewById(R.id.exercise4_card),
                findViewById(R.id.exercise5_card),
                findViewById(R.id.exercise6_card),
                findViewById(R.id.exercise7_card)
        };

        for (View card : cards) {
            card.setAlpha(0f);
            card.setScaleX(0.8f);
            card.setScaleY(0.8f);

            card.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(800)
                    .start();
        }
    }

    /**
     * Set Floating Action Button (FAB) click listener.
     */
    private void setFabClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Example: Navigate to a new activity or show a dialog
                Toast.makeText(ExerciseActivity.this, "FAB Clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel all timers to avoid memory leaks
        if (exercise1Timer != null) exercise1Timer.cancel();
        if (exercise2Timer != null) exercise2Timer.cancel();
        if (exercise3Timer != null) exercise3Timer.cancel();
        if (exercise4Timer != null) exercise4Timer.cancel();
        if (exercise5Timer != null) exercise5Timer.cancel();
        if (exercise6Timer != null) exercise6Timer.cancel();
        if (exercise7Timer != null) exercise7Timer.cancel();
    }
}