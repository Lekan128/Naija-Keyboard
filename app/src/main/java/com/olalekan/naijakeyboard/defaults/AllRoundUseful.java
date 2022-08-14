package com.olalekan.naijakeyboard.defaults;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import androidx.annotation.NonNull;

public class AllRoundUseful {
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

}
