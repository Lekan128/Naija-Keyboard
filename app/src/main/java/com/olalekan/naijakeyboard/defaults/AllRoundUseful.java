package com.olalekan.naijakeyboard.defaults;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static com.olalekan.naijakeyboard.KeyView.VIBRATION_TIME;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import androidx.annotation.NonNull;

import com.olalekan.naijakeyboard.R;

import java.util.Locale;

public class AllRoundUseful {
    public static boolean DEFAULT_VIBRATION = false;
    static ValueAnimator mAnimator1;
    static ValueAnimator mAnimator2;
    static ValueAnimator mAnimator3;
    static ValueAnimator mAnimator4;

    public static void makeVisibilityToGone(View view){
        if(view.getVisibility() != GONE) view.setVisibility(GONE);
    }

    public static void makeViewVisible(View view){
        if(view.getVisibility() != VISIBLE) view.setVisibility(VISIBLE);
    }


    public static void moveCursorToTheLeft(@NonNull InputConnection inputConnection){
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT));
    }
    public static void moveCursorToTheRight(@NonNull InputConnection inputConnection){
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT));
    }
    public static void moveCursorUp(@NonNull InputConnection inputConnection){
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP));
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_UP));
    }
    public static void moveCursorDown(@NonNull InputConnection inputConnection){
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN));
        inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN));
    }

    public static void vibrateIfRequired(Context context){
        SharedPreferences sharedPreferences;
        sharedPreferences = context.
                getSharedPreferences(context.getString(R.string.input_method_preference), Context.MODE_PRIVATE);
        boolean vibrate = sharedPreferences.getBoolean(context.getString(R.string.vibration_preference), DEFAULT_VIBRATION);
        if(vibrate) ((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VIBRATION_TIME);
    }

    public static String capitaliseFirstLetterOf(String word){
        /*
        * String.substring(beginIndex) will return a string starting from the begin index to the end
        * begin index starts from zero from the first letter
        * "Bison".substring(2) will return "son"
        *
        * String.substring(beginIndex, endIndex) will return a string starting from the begin index
        * and end at the end index.
        * endIndex start from 1 as the first letter
        * "Olalekan".substring(1,5) will return "lale"
        *
        * word.subString(0,1) will return the first letter only
        * word.substring(1) will remove the first letter and return the remaining words
        * both methods throws an exception if: the index are out of bounds or word is empty or null
        * */

        if(word==null || word.isEmpty()) {
            return word;
        }

        return word.substring(0,1).toUpperCase(Locale.ROOT) + word.substring(1);


    }

    public static AnimatorSet getAnimatorSet(ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener){
        mAnimator1 = ValueAnimator.ofFloat(0, 100f);
        mAnimator2 = ValueAnimator.ofFloat(100f, -0f);
        mAnimator3 = ValueAnimator.ofFloat(-0f, -100f);
        mAnimator4 = ValueAnimator.ofFloat(-100, 0f);
        mAnimator1.addUpdateListener(mAnimatorUpdateListener);
        mAnimator2.addUpdateListener(mAnimatorUpdateListener);
        mAnimator3.addUpdateListener(mAnimatorUpdateListener);
        mAnimator4.addUpdateListener(mAnimatorUpdateListener);
        mAnimator1.setDuration(200);
        mAnimator2.setDuration(200);
        mAnimator3.setDuration(200);
        mAnimator4.setDuration(200);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(mAnimator2).after(mAnimator1);
        mAnimatorSet.play(mAnimator3).after(mAnimator2);
        mAnimatorSet.play(mAnimator4).after(mAnimator3);

        return mAnimatorSet;
    }
}
