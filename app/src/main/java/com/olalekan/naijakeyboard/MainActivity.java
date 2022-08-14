package com.olalekan.naijakeyboard;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.olalekan.naijakeyboard.defaults.Constants;

public class MainActivity extends AppCompatActivity {

    private AnimatorSet mAnimatorSet;
    private Button mSetUpKeyboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        boolean showPreferences = intent.getBooleanExtra(Constants.SHOW_PREFERENCES_INTENT_EXTRA, false);

        TextView switchPreferenceTextView = findViewById(R.id.switchPreferenceTextView);
        mSetUpKeyboardButton = findViewById(R.id.setupKeyboardButton);

        switchPreferenceTextView.setOnClickListener(preferenceOnClick);
        mSetUpKeyboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, com.olalekan.naijakeyboard.SetupActivity.class));
            }
        });


        findViewById(R.id.testImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.olalekan.naijakeyboard.TestBottomSheetDialogFragment testBottomSheetDialogFragment = new com.olalekan.naijakeyboard.TestBottomSheetDialogFragment();
                testBottomSheetDialogFragment.show(getSupportFragmentManager(), Constants.TEST_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG);
            }
        });

        //animation
        mAnimatorSet = Constants.getAnimatorSet(mAnimatorUpdateListener);

    }

    private final ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float animatedValue = (float) valueAnimator.getAnimatedValue();
            mSetUpKeyboardButton.setTranslationY((float) Math.sin(animatedValue) * animatedValue);

        }
    };

    private final View.OnClickListener preferenceOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            com.olalekan.naijakeyboard.PreferencesBottomSheetDialogFragment preferencesBottomSheetDialogFragment = new com.olalekan.naijakeyboard.PreferencesBottomSheetDialogFragment();
            preferencesBottomSheetDialogFragment.show(getSupportFragmentManager(), Constants.PREFERENCE_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mAnimatorSet.start();
    }
}