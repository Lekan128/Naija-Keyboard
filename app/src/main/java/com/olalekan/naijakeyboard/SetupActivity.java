package com.olalekan.naijakeyboard;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
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
                setStringInRangeSizeAndColour(selectInputMethodString, 7, 20, 1.2f, Color.BLACK)
        );
        enableKeyboardInSettingsTextview.setText(
                setStringInRangeSizeAndColour(enableKeyboardInSettingsString, 7, 20, 1.2f, Color.BLACK)
        );



        //animation

        mAnimatorSet = Constants.getAnimatorSet(mAnimatorUpdateListener);

    }

    /*
     * Make the sentence string from range start to end "proportion" times bigger and and "colour" colour
    * if proportion is 1.5 the text will be 50% bigger*/
    private SpannableString setStringInRangeSizeAndColour(String sentence, int start, int end, float proportion, int colour){
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