package com.olalekan.naijakeyboard;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.olalekan.naijakeyboard.defaults.AllRoundUseful;
import com.olalekan.naijakeyboard.defaults.Constants;

public class MainActivity extends AppCompatActivity {

    private AnimatorSet mAnimatorSet;
    private Button mSetUpKeyboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        showDisclaimerAlertIfFirstTimeOpeningApp();

        boolean showPreferences = intent.getBooleanExtra(Constants.SHOW_PREFERENCES_INTENT_EXTRA, false);
        if(showPreferences){
            showPreferences();
        }

        TextView switchPreferenceTextView = findViewById(R.id.switchPreferenceTextView);
        mSetUpKeyboardButton = findViewById(R.id.setupKeyboardButton);

        switchPreferenceTextView.setOnClickListener(preferenceOnClick);
        findViewById(R.id.switchPreferenceImageView).setOnClickListener(preferenceOnClick);

//        mSetUpKeyboardButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, com.olalekan.naijakeyboard.SetupActivity.class));
//            }
//        });
        //rewritten as a lambda expression
        mSetUpKeyboardButton.setOnClickListener(view->startActivity(new Intent(MainActivity.this, com.olalekan.naijakeyboard.SetupActivity.class)));


//        findViewById(R.id.testImageView).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                com.olalekan.naijakeyboard.TestBottomSheetDialogFragment testBottomSheetDialogFragment = new com.olalekan.naijakeyboard.TestBottomSheetDialogFragment();
//                testBottomSheetDialogFragment.show(getSupportFragmentManager(), Constants.TEST_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG);
//            }
//        });
        //Lambda:
        findViewById(R.id.testImageView).setOnClickListener(view -> {
            com.olalekan.naijakeyboard.TestBottomSheetDialogFragment testBottomSheetDialogFragment = new com.olalekan.naijakeyboard.TestBottomSheetDialogFragment();
            testBottomSheetDialogFragment.show(getSupportFragmentManager(), Constants.TEST_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG);
        });

        //animation
        mAnimatorSet = AllRoundUseful.getAnimatorSet(mAnimatorUpdateListener);

    }

    private void showDisclaimerAlertIfFirstTimeOpeningApp() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.disclaimer_preference), MODE_PRIVATE);
        boolean disclaimerSeen = sharedPreferences.getBoolean(getString(R.string.discalimer_seen), false);




        if(!disclaimerSeen){
            String message = "Naija keyboard will NOT collect any of your personal data, " +
                    "password or details";

            new AlertDialog.Builder(this)
                    .setTitle("Naija Keyboard")
                    .setMessage(message)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.discalimer_seen), true);
            editor.apply();
        }

    }

    private final ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float animatedValue = (float) valueAnimator.getAnimatedValue();
            mSetUpKeyboardButton.setTranslationY((float) Math.sin(animatedValue) * animatedValue);

        }
    };

//    private final View.OnClickListener preferenceOnClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            com.olalekan.naijakeyboard.PreferencesBottomSheetDialogFragment preferencesBottomSheetDialogFragment = new com.olalekan.naijakeyboard.PreferencesBottomSheetDialogFragment();
//            preferencesBottomSheetDialogFragment.show(getSupportFragmentManager(), Constants.PREFERENCE_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG);
//        }
//    };
  //rewritten as a lombda expression
    private final View.OnClickListener preferenceOnClick = view ->showPreferences();

    @Override
    protected void onResume() {
        super.onResume();
        mAnimatorSet.start();
    }

    public void showPreferences() {
        com.olalekan.naijakeyboard.PreferencesBottomSheetDialogFragment preferencesBottomSheetDialogFragment = new com.olalekan.naijakeyboard.PreferencesBottomSheetDialogFragment();
        preferencesBottomSheetDialogFragment.show(getSupportFragmentManager(), Constants.PREFERENCE_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG);
    }
}