package com.olalekan.naijakeyboard.defaults;

import static com.olalekan.naijakeyboard.KeyView.VIBRATION_TIME;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Vibrator;

import com.olalekan.naijakeyboard.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/*
* Average length of word = 5chard
* Average adults use = 30,000words
* 1char = 1 byte
*
* Characters in total words of average adult = 5*30,000 = 150,000
* Total space for the each word = 150,000 * 8 = 1,200,000bytes
* which is 1.2mb
* If I save a words 50 times it will be 50*1.2 = 60mb
* */


public class Constants {
    //INTENT EXTRA
    public static final String SHOW_PREFERENCES_INTENT_EXTRA = "com.olalekan.naijakeyboard.SHOW_PREFERENCES_INTENT_EXTRA";

    public static final int MAX_NUMBER_OF_TIMES_A_WORD_SHOULD_BE_ADDED_TO_DATABASE = 50;
    public static final int MAX_LENGTH_OF_COPIED_WORD_THAT_CAN_BE_SHOWN = 10;

    public static final float DEFAULT_KEYBOARD_OPACITY_ALPHA = 1.0f;


    public final static int DEFAULT_COLOUR = Color.rgb(200, 200,200);
    public final static Set<String> DEFAULT_SUGGESTION_SET = new HashSet<>(Arrays.asList("Hey", "How are you?", "Comrade", "What's up?"));
    public final static String PREFERENCE_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG
            = "com.olalekan.nativekeyboard.PREFERENCE_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG";
    public final static String TEST_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG
            = "com.olalekan.nativekeyboard.TEST_BOTTOM_SHEET_DIALOG_FRAGMENT_TAG";

    public static boolean DEFAULT_VIBRATION = false;

    public static final ArrayList<Integer> COLOUR_ARRAY = new ArrayList<>(Arrays.asList(
            DEFAULT_COLOUR,
            Color.rgb(120, 114,114),
            Color.rgb(100, 150,200),
            Color.rgb(190, 190,250),
            Color.rgb(240, 225,225),
            Color.rgb(0, 0,102),
            Color.rgb(225, 153,225),
            Color.rgb(102, 0,102),
            Color.rgb(179, 255, 179),
            Color.rgb(0, 51, 0),
            Color.rgb(102, 51, 0),
            Color.rgb(255, 191, 128),
            Color.rgb(0, 51, 102),
            Color.rgb(230, 179, 179),
            Color.rgb(115, 115, 115),
            Color.rgb(255, 255, 179),
            Color.rgb(255, 255, 225)
//            Color.argb(0.875f, 128f , 128f , 159f ),
//
//            0xF0FFFF,0xff9999, 0xff66ff,
//            0xa6a6a6, 0x000066, 0xffffff00,
    ));


    public static final int MAXIMUM_NUMBER_OF_LETTERS_TO_CHECK = 15;



    //MOVE_CONSTANTS
    public static final int MINIMUM_MOTION_LENGTH_FOR_MOVEMENT = 80;
    public static final int MOVE_LEFT = 0;
    public static final int MOVE_UP = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_DOWN = 3;


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


    static ValueAnimator mAnimator1, mAnimator2, mAnimator3, mAnimator4;

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
