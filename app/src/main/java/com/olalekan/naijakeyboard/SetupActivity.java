package com.olalekan.naijakeyboard;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.olalekan.naijakeyboard.defaults.AllRoundUseful;
import com.olalekan.naijakeyboard.defaults.Constants;

public class SetupActivity extends AppCompatActivity {

    private AnimatorSet mAnimatorSet;
    private Button mEnableKeyboardInSettingsButton;
    private Button mSelectInputMethodButton;
    boolean clickedEnabledKeyboardInStettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mEnableKeyboardInSettingsButton = findViewById(R.id.enableKeyboardInSettingsButton);
        mSelectInputMethodButton = findViewById(R.id.selectInputMethodButton);

        mEnableKeyboardInSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedEnabledKeyboardInStettings = true;
                //take the user to to settings to enable Native Keyboard
                startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));

            }
        });

        mSelectInputMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show select input method pannel to enable user choose an input method
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                inputManager.showInputMethodPicker();
                showNotification();
            }
        });

        TextView selectInputMethodTextView = findViewById(R.id.selectInputMethodTextView);
        TextView enableKeyboardInSettingsTextview = findViewById(R.id.enableKeyboardInSettingsTextView);



        String selectInputMethodString = "Select naija Keyboard in your \"Languages & input\" settings.";

        String enableKeyboardInSettingsString = "Select naija Keyboard as your default input Method";

//        SpannableString spannableString = new SpannableString(string);
//        spannableString.setSpan(new RelativeSizeSpan(1.5f), 7, 22, 0);
//        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 7, 22, 0);

        selectInputMethodTextView.setText(
                setStringInTheSelectedRangeSizeAndColour(selectInputMethodString, 7, 20, 1.2f, Color.BLACK)
        );
        enableKeyboardInSettingsTextview.setText(
                setStringInTheSelectedRangeSizeAndColour(enableKeyboardInSettingsString, 7, 20, 1.2f, Color.BLACK)
        );



        //animation

        mAnimatorSet = AllRoundUseful.getAnimatorSet(mAnimatorUpdateListener);

    }


    //required form API26+ if not the notificaiton will not show
    final String NOTIFICATION_CHANNEL_ID = "notification_channel_test";
    //notification id is a unique identifier for each notification
    //it can be used later to update and remove notifications
    static final int NOTIFICATION_ID = 1;
    private void showNotification() {
        Intent showPreferenceIntent = new Intent(this, MainActivity.class);
        showPreferenceIntent.putExtra(Constants.SHOW_PREFERENCES_INTENT_EXTRA, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, showPreferenceIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        createNotificationChannel(this);

        String notificationContentTitle = "Set Naija Keyboard Preferences";
        String notificationContentText = "Don't forget to set you preferences for Naija keyboard!";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notificationContentTitle)
                .setContentText(notificationContentText)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        builder.setContentIntent(pendingIntent)//intent will fire when the user taps on the notification
                .setAutoCancel(true);//meaning the notification will cancel itself when clicked

        Intent setupKeyboardIntent = new Intent(this, SetupActivity.class);
        //only use FLAG_MUTABLE if some functionality depends on the PendingIntent being mutable, e.g. if it needs to be used with inline replies or bubbles.
        PendingIntent setUpKeyboardPendingIntent = PendingIntent.getActivity(this, 1, setupKeyboardIntent, PendingIntent.FLAG_IMMUTABLE);
        //adding action button
        builder.addAction(R.drawable.ic_baseline_keyboard_24, "Set Up keyboard", setUpKeyboardPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

    }

    /*
     * NOTE: Notification PRIORITY is different form Notification IMPORTANCE
     * The importance is set for API 26+ and the priority is to support <API26
     * */
    private void createNotificationChannel(Context context) {
        //create a notification channel for API level 26+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence channelName = "Keyboard Preference Reminder";
            String description = "This notification reminds you to set your preferences for the keyboard";
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
            notificationChannel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /*
     * Make the sentence string from range int start to int end "proportion" times bigger and and "colour" colour
    * if proportion is 1.5 the text will be 50% bigger*/
    private SpannableString setStringInTheSelectedRangeSizeAndColour(String sentence, int start, int end, float proportion, int colour){
        SpannableString spannableString = new SpannableString(sentence);
        spannableString.setSpan(new RelativeSizeSpan(proportion), start, end, 0);
        spannableString.setSpan(new ForegroundColorSpan(colour), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }


    private final ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float animatedValue = (float) valueAnimator.getAnimatedValue();
            //if the user has not clicked on enable keyboard in settings
            if(!clickedEnabledKeyboardInStettings) mEnableKeyboardInSettingsButton.setTranslationY((float) Math.sin(animatedValue) * animatedValue);
            else mSelectInputMethodButton.setTranslationY((float) Math.sin(animatedValue) * animatedValue);

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        mAnimatorSet.start();
    }
}