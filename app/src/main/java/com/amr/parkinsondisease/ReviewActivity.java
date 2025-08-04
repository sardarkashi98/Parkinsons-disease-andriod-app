package com.amr.parkinsondisease;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ReviewActivity extends AppCompatActivity {

    private LinearLayout startContainer, resultContainer, questionsLayout;
    private ScrollView questionContainer;
    private TextView percentageTextView, statusTextView, factTextView;
    private TextView progressText, progressPercent;
    private Button startButton, submitButton, restartButton;
    private ProgressBar progressBar;

    private List<Question> questions;
    private Map<Integer, String> selectedOptions;
    private int totalScore = 0;
    private int answeredCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Initialize views
        startContainer = findViewById(R.id.startContainer);
        questionContainer = findViewById(R.id.questionContainer);
        resultContainer = findViewById(R.id.resultContainer);
        questionsLayout = findViewById(R.id.questionsLayout);
        percentageTextView = findViewById(R.id.percentageTextView);
        statusTextView = findViewById(R.id.statusTextView);
        factTextView = findViewById(R.id.factTextView);
        startButton = findViewById(R.id.startButton);
        submitButton = findViewById(R.id.submitButton);
        restartButton = findViewById(R.id.restartButton);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        progressPercent = findViewById(R.id.progressPercent);

        // Initialize questions and selected options map
        initializeQuestions();
        selectedOptions = new HashMap<>();

        // Start button click listener
        startButton.setOnClickListener(v -> {
            Animation fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
            startContainer.startAnimation(fadeOut);
            startContainer.setVisibility(View.GONE);

            Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            questionContainer.startAnimation(fadeIn);
            questionContainer.setVisibility(View.VISIBLE);

            showQuestions();
        });

        // Submit button click listener
        submitButton.setOnClickListener(v -> {
            if (validateAllQuestionsAnswered()) {
                calculateScore();
                showResult();
            } else {
                Toast.makeText(this, "Please answer all questions before submitting.", Toast.LENGTH_SHORT).show();
            }
        });

        // Restart button click listener
        restartButton.setOnClickListener(v -> {
            Animation fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
            resultContainer.startAnimation(fadeOut);
            resultContainer.setVisibility(View.GONE);

            Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            questionContainer.startAnimation(fadeIn);
            questionContainer.setVisibility(View.VISIBLE);

            totalScore = 0;
            answeredCount = 0;
            selectedOptions.clear();
            showQuestions();
        });
    }

    private void initializeQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("Do you experience tremors or shaking in your hands, arms, legs, or jaw?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you have difficulty with balance or coordination?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you feel stiffness in your limbs or trunk?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you have trouble walking or taking steps?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you notice a decrease in facial expressions?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you experience a soft or low voice when speaking?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you have difficulty writing or notice your handwriting becoming smaller?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you feel fatigued or unusually tired throughout the day?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you have trouble sleeping or experience restless nights?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you experience constipation or digestive issues?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you have difficulty swallowing or chewing food?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you notice a loss of smell or taste?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you feel depressed or anxious frequently?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you experience memory problems or confusion?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you have trouble concentrating or focusing on tasks?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you experience dizziness or lightheadedness?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you notice a change in your posture, such as stooping or leaning forward?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you have difficulty initiating movement or feel 'frozen' in place?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you experience muscle cramps or spasms?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you notice a decrease in your ability to perform fine motor tasks, like buttoning a shirt?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you feel a sense of slowness in your movements (bradykinesia)?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you experience urinary problems or incontinence?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you have trouble with speech, such as slurring or mumbling?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you feel a sense of stiffness or rigidity in your muscles?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
        questions.add(new Question("Do you experience a loss of automatic movements, like blinking or smiling?", new String[]{"Never", "Rarely", "Sometimes", "Often"}, "Often"));
    }

    private void showQuestions() {
        questionsLayout.removeAllViews();
        progressBar.setMax(questions.size());
        progressBar.setProgress(0);
        progressText.setText("Progress: 0/" + questions.size());
        progressPercent.setText("0%");
        answeredCount = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);

            // Create question item container
            LinearLayout questionItem = new LinearLayout(this);
            questionItem.setOrientation(LinearLayout.VERTICAL);
            questionItem.setBackgroundResource(R.drawable.question_item_bg);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 16);
            questionItem.setLayoutParams(params);
            questionItem.setPadding(16, 16, 16, 16);

            // Add question text with number
            LinearLayout questionHeader = new LinearLayout(this);
            questionHeader.setOrientation(LinearLayout.HORIZONTAL);
            questionHeader.setGravity(Gravity.CENTER_VERTICAL);

            // Question number
            TextView numberView = new TextView(this);
            numberView.setText(String.valueOf(i + 1));
            numberView.setTextColor(getResources().getColor(android.R.color.white));
            numberView.setTextSize(14);
            numberView.setBackgroundResource(R.drawable.question_number_bg);
            numberView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams numberParams = new LinearLayout.LayoutParams(
                    dpToPx(30), dpToPx(30));
            numberParams.setMargins(0, 0, 8, 0);
            numberView.setLayoutParams(numberParams);

            // Question text
            TextView questionView = new TextView(this);
            questionView.setText(question.getQuestion());
            questionView.setTextColor(getResources().getColor(android.R.color.white));
            questionView.setTextSize(18);
            questionView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            questionHeader.addView(numberView);
            questionHeader.addView(questionView);
            questionItem.addView(questionHeader);

            // Add radio buttons for options
            RadioGroup optionsGroup = new RadioGroup(this);
            optionsGroup.setOrientation(LinearLayout.VERTICAL);
            optionsGroup.setTag(i); // Store question index

            for (String option : question.getOptions()) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                radioButton.setTextColor(getResources().getColor(android.R.color.white));
                radioButton.setTextSize(16);
                radioButton.setButtonDrawable(null);
                radioButton.setBackgroundResource(R.drawable.radio_option_bg);
                radioButton.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12));
                radioButton.setGravity(Gravity.CENTER_VERTICAL);

                LinearLayout.LayoutParams radioParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                radioParams.setMargins(0, 8, 0, 0);
                radioButton.setLayoutParams(radioParams);

                // Check if this option was previously selected
                if (selectedOptions.containsKey(i) && selectedOptions.get(i).equals(option)) {
                    radioButton.setChecked(true);
                    radioButton.setBackgroundResource(R.drawable.radio_option_selected_bg);
                }

                radioButton.setOnClickListener(v -> {
                    // Update selected option
                    int questionIndex = (int) optionsGroup.getTag();
                    selectedOptions.put(questionIndex, option);

                    // Update UI for all options in this question
                    for (int j = 0; j < optionsGroup.getChildCount(); j++) {
                        View child = optionsGroup.getChildAt(j);
                        if (child instanceof RadioButton) {
                            RadioButton rb = (RadioButton) child;
                            if (rb.isChecked()) {
                                rb.setBackgroundResource(R.drawable.radio_option_selected_bg);
                            } else {
                                rb.setBackgroundResource(R.drawable.radio_option_bg);
                            }
                        }
                    }

                    // Update progress
                    updateProgress();
                });

                optionsGroup.addView(radioButton);
            }

            questionItem.addView(optionsGroup);
            questionsLayout.addView(questionItem);
        }
    }

    private void updateProgress() {
        answeredCount = selectedOptions.size();
        int progressPercentage = (int) (((float) answeredCount / questions.size()) * 100);

        progressBar.setProgress(answeredCount);
        progressText.setText("Progress: " + answeredCount + "/" + questions.size());
        progressPercent.setText(progressPercentage + "%");

        // Enable submit button when all questions are answered
        submitButton.setEnabled(answeredCount == questions.size());
    }

    private boolean validateAllQuestionsAnswered() {
        return answeredCount == questions.size();
    }

    private void calculateScore() {
        totalScore = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (selectedOptions.containsKey(i)) {
                String selectedOption = selectedOptions.get(i);
                switch (selectedOption) {
                    case "Never":
                        totalScore += 0;
                        break;
                    case "Rarely":
                        totalScore += 1;
                        break;
                    case "Sometimes":
                        totalScore += 2;
                        break;
                    case "Often":
                        totalScore += 3;
                        break;
                }
            }
        }
    }

    private void showResult() {
        int maxScore = questions.size() * 3;
        double percentage = (double) totalScore / maxScore * 100;
        percentageTextView.setText(String.format("%.1f%%", percentage));

        String status;
        if (percentage < 30) {
            status = "You are likely healthy. No significant symptoms detected.";
        } else if (percentage >= 30 && percentage < 70) {
            status = "You may have mild symptoms. Consider consulting a doctor.";
        } else {
            status = "You may have significant symptoms of Parkinson's. Please consult a doctor.";
        }
        statusTextView.setText(status);

        String[] facts = {
                "Parkinson's disease affects nearly 1 million people in the U.S. and 10 million worldwide.",
                "Men are 1.5 times more likely to develop Parkinson's than women.",
                "The average age of Parkinson's diagnosis is 60 years.",
                "Exercise has been shown to improve symptoms and slow disease progression.",
                "Parkinson's is caused by the loss of dopamine-producing cells in the brain.",
                "James Parkinson first described the disease in 1817.",
                "There is no cure for Parkinson's, but treatments can help manage symptoms.",
                "Parkinson's is the second most common neurodegenerative disorder after Alzheimer's.",
                "Deep brain stimulation is a surgical treatment option for advanced Parkinson's.",
                "Parkinson's can affect non-motor functions, such as mood and cognition."
        };
        String randomFact = facts[new Random().nextInt(facts.length)];
        factTextView.setText("Did you know? " + randomFact);

        Animation fadeOut = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        questionContainer.startAnimation(fadeOut);
        questionContainer.setVisibility(View.GONE);

        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        resultContainer.startAnimation(fadeIn);
        resultContainer.setVisibility(View.VISIBLE);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private static class Question {
        private String question;
        private String[] options;
        private String answer;

        public Question(String question, String[] options, String answer) {
            this.question = question;
            this.options = options;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String[] getOptions() {
            return options;
        }

        public String getAnswer() {
            return answer;
        }
    }
}