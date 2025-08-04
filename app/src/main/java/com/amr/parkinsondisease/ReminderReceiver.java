package com.amr.parkinsondisease;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Check if notification permission is granted
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                == PackageManager.PERMISSION_GRANTED) {

            String message = intent.getStringExtra("message");

            // Create Notification Channel for Android 8.0+
            createNotificationChannel(context);

            // Show a notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "snack_reminder")
                    .setSmallIcon(R.drawable.ic_notification) // Add your notification icon here
                    .setContentTitle("Snack Time!")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());

            // Show a toast
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case when permission is not granted
            Toast.makeText(context, "Notification permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to create a notification channel for Android 8.0+
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Snack Reminder Channel";
            String description = "Channel for snack reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("snack_reminder", name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
