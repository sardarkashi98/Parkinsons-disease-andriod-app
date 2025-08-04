package com.amr.parkinsondisease;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class DietPlanFragment extends Fragment {

    private static final String TAG = "DietPlanFragment";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PREFS_NAME = "ProfilePrefs";
    private static final String PROFILE_IMAGE_KEY = "profileImage";
    private static final int ALARM_PERMISSION_REQUEST_CODE = 100;

    private Button dietPlanButton, exerciseButton, changeProfileButton;
    private TextView dietPlanTextView;
    private EditText ageEditText;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private ImageView profileImageView, cameraIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diet_plan, container, false);

        // Initialize UI elements
        initializeViews(view);

        // Load the profile image if it exists
        loadProfileImage();

        // Set click listeners for buttons
        setButtonListeners();

        return view;
    }

    /**
     * Initialize all UI elements.
     */
    private void initializeViews(View view) {
        dietPlanButton = view.findViewById(R.id.btn_diet_plan);
        exerciseButton = view.findViewById(R.id.btn_exercise);
        changeProfileButton = view.findViewById(R.id.btn_change_profile);
        dietPlanTextView = view.findViewById(R.id.tv_diet_plan);
        ageEditText = view.findViewById(R.id.enter_age);
        genderRadioGroup = view.findViewById(R.id.gender_radio_group);
        maleRadioButton = view.findViewById(R.id.radio_male);
        femaleRadioButton = view.findViewById(R.id.radio_female);
        profileImageView = view.findViewById(R.id.profile_picture);
        cameraIcon = view.findViewById(R.id.camera_icon);

        // Check if any UI element is null
        if (dietPlanButton == null || exerciseButton == null || changeProfileButton == null ||
                dietPlanTextView == null || ageEditText == null || genderRadioGroup == null ||
                profileImageView == null || cameraIcon == null) {
            Log.e(TAG, "One or more UI elements not found in the layout.");
            Toast.makeText(getContext(), "Error: UI elements not found.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Load the profile image from SharedPreferences and display it.
     */
    private void loadProfileImage() {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String encodedImage = prefs.getString(PROFILE_IMAGE_KEY, null);

        if (encodedImage != null) {
            // Decode the Base64 string back to Bitmap
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Set click listeners for buttons.
     */
    private void setButtonListeners() {
        final Animation buttonClickAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click);

        // Diet Plan Button
        dietPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);
                generateDietPlan();
            }
        });

        // Camera Icon (for changing profile picture)
        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);
                selectImageFromGallery();
            }
        });

        // Change Profile Button
        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);
                selectImageFromGallery();
            }
        });

        // Exercise Button
        exerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClickAnimation);
                // Launch the ExerciseActivity
                Intent intent = new Intent(getActivity(), ExerciseActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Generate and display a personalized diet plan based on the current day, age, and gender.
     */
    private void generateDietPlan() {
        String ageStr = ageEditText.getText().toString().trim();
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();

        // Validate user inputs
        if (TextUtils.isEmpty(ageStr)) {
            Toast.makeText(getContext(), "Please enter your age.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedGenderId == -1) {
            Toast.makeText(getContext(), "Please select your gender.", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        String gender = maleRadioButton.isChecked() ? "Male" : "Female";

        // Get the appropriate diet plan based on age and gender
        String todayDietPlan = getDietPlanForAgeAndGender(age, gender);

        if (todayDietPlan != null) {
            String personalizedPlan = "Hello, " + gender + " aged " + age + "!\n" + todayDietPlan;
            dietPlanTextView.setText(personalizedPlan);
            setSnackReminder();
        } else {
            dietPlanTextView.setText("Error: Unable to generate the diet plan. Please try again.");
        }
    }

    /**
     * Get the appropriate diet plan based on age and gender.
     */
    private String getDietPlanForAgeAndGender(int age, String gender) {
        // Get the current day of the week
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // Sunday = 1, Monday = 2, ..., Saturday = 7
        int dayIndex = dayOfWeek - 1; // Convert to 0-based index (Sunday = 0, ..., Saturday = 6)

        // Select the appropriate diet plan based on age group and gender
        if (age < 60) {
            return getStandardDietPlan(gender, dayIndex);
        } else if (age >= 60 && age <= 80) {
            return getSeniorDietPlan(gender, dayIndex);
        } else {
            return getElderlyDietPlan(gender, dayIndex);
        }
    }

    /**
     * Get standard diet plan for adults under 60.
     */
    private String getStandardDietPlan(String gender, int dayIndex) {
        String[] maleDietPlans = {
                "Day 1 (Sunday):\n" +
                        "Breakfast: Paratha with yogurt and a cup of chai.\n" +
                        "Lunch: Daal (lentils) with boiled rice and a side of salad.\n" +
                        "Snack (4:00 PM): Pakoras with chai.\n" +
                        "Dinner: Chicken curry with roti and mixed vegetables.",
                "Day 2 (Monday):\n" +
                        "Breakfast: Omelette with whole wheat bread and a cup of chai.\n" +
                        "Lunch: Aloo gosht (potato and meat curry) with roti.\n" +
                        "Snack (4:00 PM): Samosa with chai.\n" +
                        "Dinner: Daal chawal (lentils with rice) and a side of spinach.",
                "Day 3 (Tuesday):\n" +
                        "Breakfast: Halwa puri with chana (chickpeas) and a cup of chai.\n" +
                        "Lunch: Chicken biryani with raita.\n" +
                        "Snack (4:00 PM): Biscuits with chai.\n" +
                        "Dinner: Vegetable curry with roti and yogurt.",
                "Day 4 (Wednesday):\n" +
                        "Breakfast: Toast with butter and jam, and a cup of chai.\n" +
                        "Lunch: Beef curry with roti and salad.\n" +
                        "Snack (4:00 PM): Pakoras with chai.\n" +
                        "Dinner: Daal mash (black lentils) with rice and a side of boiled eggs.",
                "Day 5 (Thursday):\n" +
                        "Breakfast: Paratha with yogurt and a cup of chai.\n" +
                        "Lunch: Chicken karahi with naan.\n" +
                        "Snack (4:00 PM): Samosa with chai.\n" +
                        "Dinner: Daal chawal (lentils with rice) and a side of fried fish.",
                "Day 6 (Friday):\n" +
                        "Breakfast: Chana chaat with a cup of chai.\n" +
                        "Lunch: Pulao with chicken or beef curry.\n" +
                        "Snack (4:00 PM): Biscuits with chai.\n" +
                        "Dinner: Vegetable curry with roti and yogurt.",
                "Day 7 (Saturday):\n" +
                        "Breakfast: Aloo paratha with yogurt and a cup of chai.\n" +
                        "Lunch: Daal (lentils) with boiled rice and a side of salad.\n" +
                        "Snack (4:00 PM): Pakoras with chai.\n" +
                        "Dinner: Chicken curry with roti and mixed vegetables."
        };

        String[] femaleDietPlans = {
                "Day 1 (Sunday):\n" +
                        "Breakfast: Oatmeal with fruits and a cup of green tea.\n" +
                        "Lunch: Grilled chicken with quinoa and a side of salad.\n" +
                        "Snack (4:00 PM): Nuts and a cup of herbal tea.\n" +
                        "Dinner: Fish curry with brown rice and steamed vegetables.",
                "Day 2 (Monday):\n" +
                        "Breakfast: Smoothie with spinach, banana, and almond milk.\n" +
                        "Lunch: Lentil soup with whole grain bread.\n" +
                        "Snack (4:00 PM): Greek yogurt with honey.\n" +
                        "Dinner: Grilled fish with sweet potatoes and broccoli.",
                "Day 3 (Tuesday):\n" +
                        "Breakfast: Avocado toast with a boiled egg.\n" +
                        "Lunch: Quinoa salad with chickpeas and vegetables.\n" +
                        "Snack (4:00 PM): Apple slices with peanut butter.\n" +
                        "Dinner: Stir-fried tofu with brown rice and mixed vegetables.",
                "Day 4 (Wednesday):\n" +
                        "Breakfast: Whole grain pancakes with maple syrup.\n" +
                        "Lunch: Grilled chicken salad with olive oil dressing.\n" +
                        "Snack (4:00 PM): Dark chocolate and almonds.\n" +
                        "Dinner: Baked salmon with asparagus and quinoa.",
                "Day 5 (Thursday):\n" +
                        "Breakfast: Scrambled eggs with whole grain toast.\n" +
                        "Lunch: Vegetable stir-fry with tofu.\n" +
                        "Snack (4:00 PM): Cottage cheese with pineapple.\n" +
                        "Dinner: Grilled shrimp with zucchini noodles.",
                "Day 6 (Friday):\n" +
                        "Breakfast: Chia pudding with berries.\n" +
                        "Lunch: Grilled turkey burger with a side salad.\n" +
                        "Snack (4:00 PM): Carrot sticks with hummus.\n" +
                        "Dinner: Baked chicken with roasted vegetables.",
                "Day 7 (Saturday):\n" +
                        "Breakfast: Smoothie bowl with granola and fruits.\n" +
                        "Lunch: Grilled salmon with quinoa and steamed broccoli.\n" +
                        "Snack (4:00 PM): Mixed nuts and dried fruits.\n" +
                        "Dinner: Vegetable curry with brown rice."
        };

        try {
            return gender.equals("Male") ? maleDietPlans[dayIndex] : femaleDietPlans[dayIndex];
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "Invalid day index: " + dayIndex, e);
            return null;
        }
    }

    /**
     * Get diet plan for seniors (60-80 years old).
     */
    private String getSeniorDietPlan(String gender, int dayIndex) {
        String[] maleDietPlans = {
                "Day 1 (Sunday):\n" +
                        "Breakfast: Soft scrambled eggs with whole wheat toast and herbal tea.\n" +
                        "Lunch: Masoor daal (red lentils) with soft rice and steamed vegetables.\n" +
                        "Snack (4:00 PM): Banana with a handful of almonds.\n" +
                        "Dinner: Baked fish with mashed potatoes and boiled carrots.",
                "Day 2 (Monday):\n" +
                        "Breakfast: Oatmeal with chopped dates and warm milk.\n" +
                        "Lunch: Chicken stew with soft roti and pumpkin puree.\n" +
                        "Snack (4:00 PM): Yogurt with honey and flaxseeds.\n" +
                        "Dinner: Moong daal (yellow lentils) with khichdi (rice and lentils).",
                "Day 3 (Tuesday):\n" +
                        "Breakfast: Besan chilla (gram flour pancake) with mint chutney.\n" +
                        "Lunch: Soft vegetable pulao with raita (yogurt side).\n" +
                        "Snack (4:00 PM): Stewed apple with cinnamon.\n" +
                        "Dinner: Baked chicken with soft sweet potato mash.",
                "Day 4 (Wednesday):\n" +
                        "Breakfast: Porridge with mashed banana and walnuts.\n" +
                        "Lunch: Fish curry with soft rice and spinach puree.\n" +
                        "Snack (4:00 PM): Chia seed pudding with milk.\n" +
                        "Dinner: Lentil soup with whole wheat bread.",
                "Day 5 (Thursday):\n" +
                        "Breakfast: Scrambled tofu with soft whole wheat toast.\n" +
                        "Lunch: Soft chicken biryani with cucumber raita.\n" +
                        "Snack (4:00 PM): Avocado mash on crackers.\n" +
                        "Dinner: Vegetable soup with soft roti.",
                "Day 6 (Friday):\n" +
                        "Breakfast: Semolina (sooji) upma with vegetables.\n" +
                        "Lunch: Soft keema (minced meat) with mashed potatoes.\n" +
                        "Snack (4:00 PM): Handful of roasted chickpeas.\n" +
                        "Dinner: Baked salmon with quinoa and steamed zucchini.",
                "Day 7 (Saturday):\n" +
                        "Breakfast: Poha (flattened rice) with peanuts and turmeric.\n" +
                        "Lunch: Soft vegetable curry with rice and yogurt.\n" +
                        "Snack (4:00 PM): Boiled egg with whole wheat toast.\n" +
                        "Dinner: Chicken soup with soft noodles."
        };

        String[] femaleDietPlans = {
                "Day 1 (Sunday):\n" +
                        "Breakfast: Oatmeal with flaxseeds and chopped prunes.\n" +
                        "Lunch: Soft vegetable curry with quinoa and yogurt.\n" +
                        "Snack (4:00 PM): Almond milk with whole grain crackers.\n" +
                        "Dinner: Baked fish with mashed sweet potatoes.",
                "Day 2 (Monday):\n" +
                        "Breakfast: Scrambled eggs with avocado on whole wheat toast.\n" +
                        "Lunch: Lentil soup with soft whole wheat bread.\n" +
                        "Snack (4:00 PM): Greek yogurt with blueberries.\n" +
                        "Dinner: Chicken stew with soft vegetables.",
                "Day 3 (Tuesday):\n" +
                        "Breakfast: Chia pudding with almond milk and honey.\n" +
                        "Lunch: Soft vegetable biryani with cucumber raita.\n" +
                        "Snack (4:00 PM): Handful of walnuts and raisins.\n" +
                        "Dinner: Baked salmon with mashed cauliflower.",
                "Day 4 (Wednesday):\n" +
                        "Breakfast: Besan cheela (gram flour pancake) with mint chutney.\n" +
                        "Lunch: Soft chicken curry with rice and spinach puree.\n" +
                        "Snack (4:00 PM): Stewed pears with cinnamon.\n" +
                        "Dinner: Moong daal (yellow lentils) with soft roti.",
                "Day 5 (Thursday):\n" +
                        "Breakfast: Porridge with mashed banana and almonds.\n" +
                        "Lunch: Fish curry with soft rice and carrot puree.\n" +
                        "Snack (4:00 PM): Yogurt with flaxseeds.\n" +
                        "Dinner: Vegetable soup with soft whole wheat bread.",
                "Day 6 (Friday):\n" +
                        "Breakfast: Poha (flattened rice) with vegetables and peanuts.\n" +
                        "Lunch: Soft keema (minced meat) with mashed potatoes.\n" +
                        "Snack (4:00 PM): Handful of roasted pumpkin seeds.\n" +
                        "Dinner: Baked chicken with quinoa and steamed vegetables.",
                "Day 7 (Saturday):\n" +
                        "Breakfast: Semolina (sooji) upma with vegetables.\n" +
                        "Lunch: Soft vegetable pulao with yogurt.\n" +
                        "Snack (4:00 PM): Boiled egg with whole wheat toast.\n" +
                        "Dinner: Lentil soup with soft noodles."
        };

        try {
            return gender.equals("Male") ? maleDietPlans[dayIndex] : femaleDietPlans[dayIndex];
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "Invalid day index: " + dayIndex, e);
            return null;
        }
    }

    /**
     * Get diet plan for elderly (above 80 years old).
     */
    private String getElderlyDietPlan(String gender, int dayIndex) {
        String[] maleDietPlans = {
                "Day 1 (Sunday):\n" +
                        "Breakfast: Warm milk with soaked almonds and mashed banana.\n" +
                        "Lunch: Soft khichdi (rice and lentils) with ghee and boiled vegetables.\n" +
                        "Snack (4:00 PM): Yogurt with honey and chia seeds.\n" +
                        "Dinner: Pureed vegetable soup with soft bread.",
                "Day 2 (Monday):\n" +
                        "Breakfast: Oatmeal with mashed dates and warm milk.\n" +
                        "Lunch: Soft chicken stew with mashed potatoes.\n" +
                        "Snack (4:00 PM): Stewed apple with cinnamon.\n" +
                        "Dinner: Moong daal (yellow lentils) soup with soft rice.",
                "Day 3 (Tuesday):\n" +
                        "Breakfast: Scrambled eggs with soft whole wheat toast.\n" +
                        "Lunch: Masoor daal (red lentils) with soft rice and pureed spinach.\n" +
                        "Snack (4:00 PM): Banana milkshake with protein powder.\n" +
                        "Dinner: Baked fish with mashed sweet potatoes.",
                "Day 4 (Wednesday):\n" +
                        "Breakfast: Besan cheela (gram flour pancake) with mint chutney.\n" +
                        "Lunch: Soft vegetable pulao with yogurt.\n" +
                        "Snack (4:00 PM): Handful of soaked almonds.\n" +
                        "Dinner: Chicken soup with soft noodles.",
                "Day 5 (Thursday):\n" +
                        "Breakfast: Porridge with mashed banana and walnuts.\n" +
                        "Lunch: Soft fish curry with mashed rice and carrots.\n" +
                        "Snack (4:00 PM): Yogurt with flaxseeds.\n" +
                        "Dinner: Lentil soup with soft whole wheat bread.",
                "Day 6 (Friday):\n" +
                        "Breakfast: Semolina (sooji) upma with vegetables.\n" +
                        "Lunch: Soft keema (minced meat) with mashed potatoes.\n" +
                        "Snack (4:00 PM): Chia seed pudding with milk.\n" +
                        "Dinner: Baked salmon with quinoa and steamed zucchini.",
                "Day 7 (Saturday):\n" +
                        "Breakfast: Poha (flattened rice) with milk and nuts.\n" +
                        "Lunch: Soft vegetable curry with rice and yogurt.\n" +
                        "Snack (4:00 PM): Boiled egg with soft toast.\n" +
                        "Dinner: Chicken soup with soft bread."
        };

        String[] femaleDietPlans = {
                "Day 1 (Sunday):\n" +
                        "Breakfast: Warm milk with soaked almonds and mashed banana.\n" +
                        "Lunch: Soft khichdi (rice and lentils) with ghee and boiled vegetables.\n" +
                        "Snack (4:00 PM): Yogurt with honey and chia seeds.\n" +
                        "Dinner: Pureed vegetable soup with soft bread.",
                "Day 2 (Monday):\n" +
                        "Breakfast: Oatmeal with mashed dates and warm milk.\n" +
                        "Lunch: Soft chicken stew with mashed potatoes.\n" +
                        "Snack (4:00 PM): Stewed apple with cinnamon.\n" +
                        "Dinner: Moong daal (yellow lentils) soup with soft rice.",
                "Day 3 (Tuesday):\n" +
                        "Breakfast: Scrambled eggs with soft whole wheat toast.\n" +
                        "Lunch: Masoor daal (red lentils) with soft rice and pureed spinach.\n" +
                        "Snack (4:00 PM): Banana milkshake with protein powder.\n" +
                        "Dinner: Baked fish with mashed sweet potatoes.",
                "Day 4 (Wednesday):\n" +
                        "Breakfast: Besan cheela (gram flour pancake) with mint chutney.\n" +
                        "Lunch: Soft vegetable pulao with yogurt.\n" +
                        "Snack (4:00 PM): Handful of soaked almonds.\n" +
                        "Dinner: Chicken soup with soft noodles.",
                "Day 5 (Thursday):\n" +
                        "Breakfast: Porridge with mashed banana and walnuts.\n" +
                        "Lunch: Soft fish curry with mashed rice and carrots.\n" +
                        "Snack (4:00 PM): Yogurt with flaxseeds.\n" +
                        "Dinner: Lentil soup with soft whole wheat bread.",
                "Day 6 (Friday):\n" +
                        "Breakfast: Semolina (sooji) upma with vegetables.\n" +
                        "Lunch: Soft keema (minced meat) with mashed potatoes.\n" +
                        "Snack (4:00 PM): Chia seed pudding with milk.\n" +
                        "Dinner: Baked salmon with quinoa and steamed zucchini.",
                "Day 7 (Saturday):\n" +
                        "Breakfast: Poha (flattened rice) with milk and nuts.\n" +
                        "Lunch: Soft vegetable curry with rice and yogurt.\n" +
                        "Snack (4:00 PM): Boiled egg with soft toast.\n" +
                        "Dinner: Chicken soup with soft bread."
        };

        try {
            return gender.equals("Male") ? maleDietPlans[dayIndex] : femaleDietPlans[dayIndex];
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "Invalid day index: " + dayIndex, e);
            return null;
        }
    }

    /**
     * Set a reminder for snack at 4:00 PM.
     */
    private void setSnackReminder() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Check for SCHEDULE_EXACT_ALARM permission (required for Android 12 and above)
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                // Request permission if not granted
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.SCHEDULE_EXACT_ALARM}, ALARM_PERMISSION_REQUEST_CODE);
                return;
            }
        }

        // Set the alarm for 4:00 PM
        Intent intent = new Intent(getContext(), ReminderReceiver.class);
        intent.putExtra("message", "It's snack time! Enjoy your healthy snack!");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 16); // 4:00 PM
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(getContext(), "Snack reminder set for 4:00 PM!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ALARM_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, set the reminder
                setSnackReminder();
            } else {
                Toast.makeText(getContext(), "Permission denied. Cannot set snack reminder.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Open the gallery to select an image.
     */
    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap); // Set the image to the ImageView

                // Store the image in SharedPreferences
                storeImageInPreferences(bitmap);
            } catch (IOException e) {
                Log.e(TAG, "Error loading image: " + e.getMessage(), e);
                Toast.makeText(getContext(), "Failed to load image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Store the selected image in SharedPreferences as a Base64 string.
     */
    private void storeImageInPreferences(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROFILE_IMAGE_KEY, encodedImage);
        editor.apply();

        Toast.makeText(getContext(), "Profile picture updated successfully!", Toast.LENGTH_SHORT).show();
    }
}