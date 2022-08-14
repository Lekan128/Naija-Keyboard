package com.olalekan.naijakeyboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.olalekan.naijakeyboard.defaults.Constants;

import java.util.Set;

public class PreferencesBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private SuggestionInputRecyclerAdapter mSuggestionInputRecyclerAdapter;
    private com.olalekan.naijakeyboard.ColourRecyclerAdapter mColourRecyclerAdapter;
    private SharedPreferences mSharedPreferences;
    private SwitchCompat mVibrationSwitch;
    private SeekBar mKeyOpacitySeekBar;
    final float MAX_VALUE_OF_SEEK_BAR = 100.0f;


    public PreferencesBottomSheetDialogFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preference_bottom_sheet_dialog, container, false);

        view.setBackground(getResources().getDrawable(R.drawable.rectangle_curved_corners));

        //setup suggestion recycler view
        RecyclerView suggestionInputRecyclerView = view.findViewById(R.id.defaultSuggestionsInputRecyclerView);
        GridLayoutManager suggestionRecyclerViewLayoutManager = new GridLayoutManager(getContext(), 3, RecyclerView.HORIZONTAL, false);
        mSuggestionInputRecyclerAdapter = new SuggestionInputRecyclerAdapter(getContext());
        suggestionInputRecyclerView.setLayoutManager(suggestionRecyclerViewLayoutManager);
        suggestionInputRecyclerView.setAdapter(mSuggestionInputRecyclerAdapter);

        //setup colour recycler view
        RecyclerView colourRecyclerView = view.findViewById(R.id.colurPreferenceRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, RecyclerView.HORIZONTAL, false);
        mColourRecyclerAdapter = new com.olalekan.naijakeyboard.ColourRecyclerAdapter(getContext());
        colourRecyclerView.setAdapter(mColourRecyclerAdapter);
        colourRecyclerView.setLayoutManager(layoutManager);


        mSharedPreferences = getContext().
                getSharedPreferences(getString(R.string.input_method_preference), Context.MODE_PRIVATE);

        onClickListeners(view);


        mVibrationSwitch = view.findViewById(R.id.vibrationSwitch);

        mKeyOpacitySeekBar = view.findViewById(R.id.keyOpacitySeekBar);
        //keyOpacity is a float from 0.0f to 1.0f
        float keyOpacity = mSharedPreferences.getFloat(getString(R.string.key_opacity_preference), Constants.DEFAULT_KEYBOARD_OPACITY_ALPHA);
        //seek bar progress is an int from 0 to 100 which is the set max
        int seekBarProgress = (int) (keyOpacity * MAX_VALUE_OF_SEEK_BAR);
        mKeyOpacitySeekBar.setProgress(seekBarProgress);

        boolean vibrate = mSharedPreferences.getBoolean(getString(R.string.vibration_preference), Constants.DEFAULT_VIBRATION);

        mVibrationSwitch.setChecked(vibrate);


        return view;
    }

    private void onClickListeners(View view) {
        Button saveButton = view.findViewById(R.id.saveButton);
        Button addButton = view.findViewById(R.id.addInputButton);
//        mVibrationSwitch = view.findViewById(R.id.vibrationSwitch);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View layout = inflater.inflate(R.layout.add_suggestion_edit_text_dialog, null);
                EditText suggestionEditText = layout.findViewById(R.id.suggestionAdditionEditText);
                AlertDialog.Builder alertDialog =  new AlertDialog.Builder(getContext())
                        .setView(layout);

                alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSuggestionInputRecyclerAdapter.addSuggestion(suggestionEditText.getText().toString());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });
    }

    private void save() {
        Set<String> newSuggestions = mSuggestionInputRecyclerAdapter.getSuggestion();
        int colour = mColourRecyclerAdapter.getColour();
        //seek bar usually returns an int, the max set for the seek bar is 100
        //that is why I am dividing by 100.0f
        float opacity = mKeyOpacitySeekBar.getProgress()/ MAX_VALUE_OF_SEEK_BAR;

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet(getString(R.string.saved_input_preference), newSuggestions);
        //colour will be 0 when no colour in the adapter has been chosen
        if(colour!= 0) editor.putInt(getString(R.string.colour_preference), colour);
        editor.putBoolean(getString(R.string.vibration_preference), mVibrationSwitch.isChecked());

        editor.putFloat(getString(R.string.key_opacity_preference), opacity);
        editor.commit();
        //The snack bar will only show if it is connected with a view from the main activity
        View textViewFromMainActivity = getActivity().findViewById(R.id.switchPreferenceTextView);
        Snackbar.make(textViewFromMainActivity,
                R.string.save_snackbar_string, BaseTransientBottomBar.LENGTH_LONG).setDuration(4000).show();
        PreferencesBottomSheetDialogFragment.this.dismiss();
    }


    @Override
    public void onPause() {
        super.onPause();
        save();
    }
}
