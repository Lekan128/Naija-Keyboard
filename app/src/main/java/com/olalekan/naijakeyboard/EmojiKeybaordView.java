package com.olalekan.naijakeyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.olalekan.naijakeyboard.EmojiViewPagerAdapter;
import com.olalekan.naijakeyboard.MyInputMethod;
import com.olalekan.naijakeyboard.R;

public class EmojiKeybaordView extends LinearLayout {

    /*OnKwyClicked is used to send the key clicked to MyInputMethod*/

    public EmojiKeybaordView(Context context) {
        super(context);
        initializeControls(context);
    }

    public EmojiKeybaordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeControls(context);
    }

    public EmojiKeybaordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeControls(context);
    }

    private void initializeControls(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.emoji_keyboard_view, this);

        emojisConfiguration();
    }

    private void emojisConfiguration() {
        //setUp emoji view pager2, its adapter and emoji adapter
        //Make the emoji keyboard view visible
        ImageView keyboardImageView = findViewById(R.id.keyboardImageView);
        ImageView emojiImageView = findViewById(R.id.emojiImageView);
        ViewPager2 secondViewPager = findViewById(R.id.emojiViewPager);
        secondViewPager.setAdapter(new EmojiViewPagerAdapter(getContext()));

//        RecyclerView emojiNavigationRecyclerView = findViewById(R.id.enojiNavigationRecyclerView);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);


        keyboardImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setToLettersKeyboard();
            }
        });

    }

    public void setToLettersKeyboard(){
        MyInputMethod.getInstance().setToLetterKeyboard();
//        makeEmojiKeyboardVisible(emojiKeyboard);
    }

}
