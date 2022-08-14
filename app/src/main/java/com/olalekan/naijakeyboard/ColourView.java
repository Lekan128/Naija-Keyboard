package com.olalekan.naijakeyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;


public class ColourView extends ConstraintLayout {

//    public OnClick mOnClick;

    private CardView colourCard;
    private ImageView checkImageView;

    public ColourView(@NonNull Context context) {
        super(context);
        initializeControls(context);
    }

//    private final OnClickListener mOnClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            mOnClick.textClicked(view);
//
////            Snackbar.make(view,"impress me", BaseTransientBottomBar.LENGTH_LONG).setDuration(500).show();
//        }
//    };

    public ColourView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeControls(context);

//        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BubbleView, 0,0);
//        try {
////            mTextView1.setTextColor(array.getColor(R.styleable.BubbleView_color, 0xE13D3D));
////            mTextView2.setText(array.getString(R.styleable.BubbleView_text));
////            mTextView1.setTextSize(array.getDimension(R.styleable.BubbleView_textSize, 30));
//        } finally {
//            array.recycle();
//        }
    }

    public ColourView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColourView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initializeControls(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.colour_view, this);

        colourCard = findViewById(R.id.colourCardView);
        checkImageView = findViewById(R.id.chosenImageView);

    }

    public void setChecked(){
        checkImageView.setVisibility(VISIBLE);
    }

    public void setUnchecked(){
        checkImageView.setVisibility(GONE);
    }

    public void setColour(int colour){
        colourCard.setCardBackgroundColor(colour);
//        colourCard.setBackgroundColor(colour);
//        colourCard.setForeground(new ColorDrawable(colour));
    }

//    public void setOnClickListener(OnClick onClick) {
//        mOnClick = onClick;
////        mOnClick.textClicked();
//    }
//
//    public interface OnClick{
//        void textClicked(View textView);
//    }
//
//    @Override
//    public boolean performClick() {
//        return super.performClick();
//    }
}
