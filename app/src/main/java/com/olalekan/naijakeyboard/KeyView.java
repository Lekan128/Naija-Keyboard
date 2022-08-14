package com.olalekan.naijakeyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.olalekan.naijakeyboard.defaults.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/*
* Todo:
*  show preview
* Letter on click
* show preview boolean
* attributes : Height, showPreview, letterColour, previewColour, previewDelayTime
*  */
public class KeyView extends FrameLayout {
    public static final int PREVIEW_SHOW_TIME_DELAY = 250;
    public static final int PREVIEW_PADDING_LEFT_RIGHT = 30;
    public static final int PREVIEW_PADDING_TOP_BUTTOM = 10;
    public static final int PREVIEW_TEXT_SIZE = 25;
    CardView letterCard;
    TextView letterText;
//    int previewCard
    ImageView keyImage;
    //Set as defaults to reduce to reducee the code when making a new dummy key
    boolean showInEditable = true;
    boolean showImage = false;
    boolean keyShouldRepeat = false;
    boolean repeat = false;
    boolean shouldShowPreview = false;
    boolean showingOptions = false;
    //options are the other letters that can be shown after long press(251ms) of he key
    private ArrayList<String> options = new ArrayList<>();
    //Todo: List of options after long press of preview

    Handler mainHandler = new Handler(Looper.getMainLooper());


    SharedPreferences sharedPreferences;

    /*
    *Interface that is triggered after the detection of the touch event
    * of either the letterText
    * or the letterCard
    * */
    private KeyWasClicked mKeyWasClicked;

    public KeyWasClicked getKeyWasClicked() {
        return mKeyWasClicked;
    }

    public KeyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialiseControls(context);

        //Typed array used to get the attributes gotten from the xml view into the KeyView
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KeyView, 0, 0);

        try {
            letterText.setText(array.getString(R.styleable.KeyView_letter));

            letterCard.setCardBackgroundColor(array.getColor(R.styleable.KeyView_letterBackgroundColour, 0xFFFFFF));

            letterText.setTextColor(array.getColor(R.styleable.KeyView_letterColour, 0x000000));

            showInEditable = array.getBoolean(R.styleable.KeyView_showInEditable, true);

            showImage = array.getBoolean(R.styleable.KeyView_showImage, false);

            keyShouldRepeat = array.getBoolean(R.styleable.KeyView_keyShouldRepeat, false);

            shouldShowPreview = array.getBoolean(R.styleable.KeyView_shouldShowPreview, false);

            keyImage.setImageDrawable(array.getDrawable(R.styleable.KeyView_image));

        }finally {
            array.recycle();
        }

        //should the image of the key be shown or the text:
        if(showImage){
            keyImage.setVisibility(VISIBLE);
            letterText.setVisibility(GONE);
        } else {
            keyImage.setVisibility(GONE);
            letterText.setVisibility(VISIBLE);
        }
        //always called after there is a change that can affect how the view is shown or what it is showing
        invalidate();
        requestLayout();
    }

    public KeyView(@NonNull Context context) {
        super(context);
        initialiseControls(context);

//        setDefaultValues(letter);

    }

    //Todo: use to initialize programmatically created KeyViews
    public void setDefaultValues(String letter) {
        letterText.setText(letter);

        letterCard.setCardBackgroundColor(0xF5F5F5);

        letterText.setTextColor(0x000000);
        /*
         *ALL THESE ARE SET BY DEFAULT
         * showInEditable = true;
         *showImage = false;
         *keyShouldRepeat = false;
         *shouldShowPreview = false;
         *
         * */

        invalidate();
        requestLayout();
    }

    public void setDefaultValueForDummyPreviewKey(String letter){
        letterText.setText(letter);

        letterCard.setCardBackgroundColor(0xF5F5F5);

        letterText.setTextColor(0x000000);
        /*
         *ALL THESE ARE SET BY DEFAULT
         * showInEditable = true;
         *showImage = false;
         *keyShouldRepeat = false;
         *shouldShowPreview = false;
         *
         * */

    }

    /*initializing the view controllers*/
    void initialiseControls(Context context){
        //Todo: context changed to Context
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.key_view, this);

        sharedPreferences = getContext().
                getSharedPreferences(getContext().getString(R.string.input_method_preference), Context.MODE_PRIVATE);


        letterCard = findViewById(R.id.letterCard);
        letterText = findViewById(R.id.letterTextView);
        keyImage = findViewById(R.id.keyImage);

        float keyOpacity = sharedPreferences.getFloat(getContext().getString(R.string.key_opacity_preference), Constants.DEFAULT_KEYBOARD_OPACITY_ALPHA);

        letterCard.setAlpha(keyOpacity);

//        letterCard.setOnTouchListener(mOnTouchListener);
//        letterText.setOnTouchListener(mOnTouchListener);



    }

    public static final int VIBRATION_TIME = 50;
    public static final int OPTIONS_DELAY_TIME = 500;
    boolean keyDown = false;

    CountDownTimer mCountDownTimerToShowOptions = new CountDownTimer(OPTIONS_DELAY_TIME, OPTIONS_DELAY_TIME) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            showOptions(getContext(), KeyView.this);
            showingOptions = true;
            this.cancel();
        }
    };

//    public final OnTouchListener mOnTouchListener = new OnTouchListener() {
//        final int minimumMoveLength = Constants.MINIMUM_MOTION_LENGTH_FOR_MOVEMENT;
//        private float mDownX;
//        private float mDownY;
//        boolean moveActionPerformed = false;
//
//
//
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//
//            keyDown = false;
////            moveActionPerformed=false;
//
//            if (mKeyWasClicked == null){
//                Log.e("OnClick", "onClick: not working keyView character: "+ KeyView.this.letterText.getText().toString());
//                return false;
//            }
//            //Todo: fix the preview
//
//
//
//            switch (motionEvent.getAction()){
//                case MotionEvent.ACTION_DOWN:
//                    keyDown = true;
//                    vibrateIfRequired();
//                    mDownX = motionEvent.getX();
//                    mDownY = motionEvent.getY();
//
//                if(shouldShowPreview){
//                    mCountDownTimerToShowOptions.start();
//                    showPreview(getContext(), KeyView.this);
//                }
//
//                    if(keyShouldRepeat){
//                        repeat = true;
//                        //press the key repeatedly after some milliseconds
//                        //The amount of time delayed is in the method
//                        pressTheKeyRepeatedly();
//                    }
//                    return true;
//                case MotionEvent.ACTION_UP:
//                    /*
//                    * If key should NOT repeat and
//                    * it is NOT showing the key options popup
//                    * move action is NOT performed and then perform click
//                    *
//                    * performClick is an overridden method
//                    * */
//
//                    if(!keyShouldRepeat && !showingOptions && !moveActionPerformed){
//                        KeyView.this.performClick();
//                    }
//                    else {
////                        if(repeatThread!=null) repeatThread.interrupt();
//                        if(repeatCountDownTimer != null) repeatCountDownTimer.cancel();
//                        repeat = false;
//                    }
//                    moveActionPerformed = false;
//                    showingOptions = false;
//                    mCountDownTimerToShowOptions.cancel();
//                    return true;
//
//                case MotionEvent.ACTION_MOVE:
//                    float x = motionEvent.getX() - mDownX;
//                    float y = motionEvent.getY() - mDownY;
//
//                    if(minimumMoveLength <= Math.abs(x)){
//                        if(x>0){//x is positive
//                            //move cursor to the right
//                            moveActionPerformed = true;
//                            mKeyWasClicked.move(Constants.MOVE_RIGHT);
//                            mCountDownTimerToShowOptions.cancel();
////                            if(moveDownOrRight)
////                                moveCursorToTheRight(inputConnection);
//                        } else {
//                            //move vursor to the left
//                            moveActionPerformed = true;
//                            mKeyWasClicked.move(Constants.MOVE_LEFT);
//                            mCountDownTimerToShowOptions.cancel();
//
////                            if(moveUpOrLeft)
////                                moveCursorToTheLeft(inputConnection);
//                        }
////                            ++mMoveCursorX;
//                        mDownX = motionEvent.getX();
//                    }
//
//                    if(minimumMoveLength <= Math.abs(y)){
//                        if(y>0){//y is positive
//                            //move cursor down
//                            moveActionPerformed = true;
//                            mKeyWasClicked.move(Constants.MOVE_DOWN);
//                            mCountDownTimerToShowOptions.cancel();
//
////                            if(moveDownOrRight)
////                                moveCursorDown(inputConnection);
//                        }else {
//                            //move cursor up
//                            moveActionPerformed = true;
//                            mKeyWasClicked.move(Constants.MOVE_UP);
//                            mCountDownTimerToShowOptions.cancel();
//
////                            if(moveUpOrLeft)
////                                moveCursorUp(inputConnection);
//                        }
////                            ++mMoveCursorY;
//                        mDownY = motionEvent.getY();
//                    }
//
//                    return true;
//
//                case MotionEvent.ACTION_CANCEL:
//                    mCountDownTimerToShowOptions.cancel();
//
//
//            }
//            return false;
//        }
//    };

//    Thread repeatThread;

    private static final int DELETE_TIMER_LENGTH = 60000;//60 SECONDS
    private static final int DELETE_INTERVAL = 200;
    CountDownTimer repeatCountDownTimer = new CountDownTimer(DELETE_TIMER_LENGTH, DELETE_INTERVAL) {
        @Override
        public void onTick(long l) {
            if(repeat){
                mainHandler.post(keyClickedRunnable);
            }
        }

        @Override
        public void onFinish() {

        }
    };
    Runnable keyClickedRunnable = new Runnable() {
        @Override
        public void run() {
            mKeyWasClicked.onClick(KeyView.this);
        }
    };

    private void pressTheKeyRepeatedly(){
        /*
        * The runnable contains the repeating code
        * It is created in a new thread so it will not affect the main thread
        * */

//        Runnable keyCLickedRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mKeyWasClicked.onClick(KeyView.this);
//            }
//        };
//
//        Runnable repeatRunnable = new Runnable() {
//            @Override
//            public void run() {
//
//                while (repeat){
//                    mainHandler.post(keyCLickedRunnable);
//
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        };
//
//        repeatThread = new Thread(repeatRunnable);
//        repeatThread.start();
        repeatCountDownTimer.start();
    }

    private void showPreview(Context context, KeyView keyView){
        PopupWindow popupWindow = new PopupWindow(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.key_view, null);

        LinearLayout parent = (LinearLayout) keyView.getParent();

        TextView previewText = popupView.findViewById(R.id.letterTextView);
        CardView popupCard = popupView.findViewById(R.id.letterCard);
        previewText.setText(keyView.toString());
        previewText.setPadding(PREVIEW_PADDING_LEFT_RIGHT, PREVIEW_PADDING_TOP_BUTTOM,
                PREVIEW_PADDING_LEFT_RIGHT,PREVIEW_PADDING_TOP_BUTTOM);
        previewText.setTextSize(PREVIEW_TEXT_SIZE);
//        popupCard.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 35));
//        popupCard.setLayoutParams(new Constraints.LayoutParams(LayoutParams.WRAP_CONTENT, 35));

//        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)popupCard.getLayoutParams();
//        layoutParams.width = 35;

        popupWindow.setContentView(popupView);

        popupWindow.setBackgroundDrawable(null);

        int popupPreviewY = parent.getTop() - keyView.getHeight();
        int popupPreviewX = keyView.getLeft() + keyView.letterText.getLeft();


//        popupWindow.showAtLocation(keyView, Gravity.NO_GRAVITY, keyView.getLeft(), popupPreviewY);
        popupWindow.showAtLocation(keyView, Gravity.NO_GRAVITY, popupPreviewX, popupPreviewY);

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Todo: if the list has only one element
                        popupWindow.dismiss();
                        timer.cancel();
                    }
                });
            }
        };
        timer.schedule(timerTask, PREVIEW_SHOW_TIME_DELAY);

    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public ArrayList<String> getOptions(){
        return options!=null && !options.isEmpty() ? options : new ArrayList<>(List.of(KeyView.this.toString()));
    }

    private final PopupWindow optionsPopupWindow = new PopupWindow(getContext());

    public void dismissOptionsPopup(){
        optionsPopupWindow.dismiss();
    }


    private void showOptions(Context context, KeyView keyView){
        vibrateIfRequired();

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.multiple_key_preview, null);

        LinearLayout parent = (LinearLayout) keyView.getParent();

        int gridSpanCount = keyView.getOptions().size()>1 ? 2 : keyView.getOptions().size();

        setupOptionsRecyclerView(keyView, popupView, gridSpanCount);

        optionsPopupWindow.setContentView(popupView);

        optionsPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_curved_corners, null));
//        optionsPopupWindow.setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

        optionsPopupWindow.setOutsideTouchable(true);
//        optionsPopupWindow.setTouchable(true);


//        int multiplier = (keyView.getOptions().size()/4) + 1;

        /*
         * To get where the top of the popup will be:
         * It will be the top of the linear layout which is the (parent.getTop)
         * subtracted by the height of the key or group of keys,
         * so that the key will be moved over the the exact key
         * NOTE: view.getTop() will get the the y distance in pixels
         * from the top of the phone screen to the view
         * getLeft() from the Lest side of the screen to the left part of the view
         *
         * The grid span is the multiplier because the grid recyclerview is horizontal
         * It either has one element or two elements. For one Element then the
         * options are taken above the key height(-1*keyView.getHeight())
         * For two Element then the
         * options are taken above the key height(-2*keyView.getHeight())
         * */
        int popupPreviewY = parent.getTop() - gridSpanCount*keyView.getHeight();
        int popupPreviewX = keyView.getLeft() + keyView.letterText.getLeft();

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                optionsPopupWindow.showAtLocation(keyView, Gravity.NO_GRAVITY, popupPreviewX, popupPreviewY);
            }
        }) ;

    }

    private void vibrateIfRequired(){
        boolean vibrate = sharedPreferences.getBoolean(getContext().getString(R.string.vibration_preference), Constants.DEFAULT_VIBRATION);
        if(vibrate) ((Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VIBRATION_TIME);
    }

    private void setupOptionsRecyclerView(KeyView keyView, View popupView, int gridSpanCount) {
        RecyclerView previewRecyclerView = popupView.findViewById(R.id.previewRecyclerView);

        //if the options are more than 3 then the grid size should be 4,
        // else the grid size should be the same as the size of the options

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), gridSpanCount,
                RecyclerView.HORIZONTAL, false);
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

//        GridLayoutManager.SpanSizeLookup lookup = new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return 0;
//            }
//        };


        previewRecyclerView.setLayoutManager(gridLayoutManager);
        com.olalekan.naijakeyboard.PreviewRecyclerAdapter previewRecyclerAdapter = new com.olalekan.naijakeyboard.PreviewRecyclerAdapter(keyView);
        previewRecyclerView.setAdapter(previewRecyclerAdapter);
    }

    /*
    * If it should show the letter of the text in the editable(edit text)
    * ot it should perform a function
    * */
    public boolean showInEditable(){
        return showInEditable;
    }

    /*Switch the letter on the key*/
    public void changeLetterTo(String s){
        letterText.setText(s);
        invalidate();
        requestLayout();
    }

    public void changeImageDrawableToDrawableWithId(int drawableId){
        if(showImage) keyImage.setImageDrawable(ContextCompat.getDrawable(getContext(), drawableId));
//        keyImage.setBackgroundResource(drawableId);
//        keyImage.setImageResource(drawableId);
    }



    public void setOnClickListener(KeyWasClicked keyWasClicked){
        this.mKeyWasClicked = keyWasClicked;
    }

    @NonNull
    @Override
    public String toString() {
        return letterText.getText().toString();
    }


    @Override
    public boolean performClick() {
        mKeyWasClicked.onClick(this);
        return super.performClick();
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
//    }

    final int minimumMoveLength = Constants.MINIMUM_MOTION_LENGTH_FOR_MOVEMENT;
    private float mDownX;
    private float mDownY;
    boolean moveActionPerformed = false;
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {



        keyDown = false;
//            moveActionPerformed=false;

        if (mKeyWasClicked == null){
            Log.e("OnClick", "onClick: not working keyView character: "+ KeyView.this.letterText.getText().toString());
            return false;
        }
        //Todo: fix the preview



        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                keyDown = true;
                vibrateIfRequired();
                mDownX = motionEvent.getX();
                mDownY = motionEvent.getY();

                if(shouldShowPreview){
                    mCountDownTimerToShowOptions.start();
                    showPreview(getContext(), KeyView.this);
                }

                if(keyShouldRepeat){
                    repeat = true;
                    //press the key repeatedly after some milliseconds
                    //The amount of time delayed is in the method
                    pressTheKeyRepeatedly();
                }
                return true;
            case MotionEvent.ACTION_UP:
                /*
                 * If key should NOT repeat and
                 * it is NOT showing the key options popup
                 * move action is NOT performed and then perform click
                 *
                 * performClick is an overridden method
                 * */

                if(!keyShouldRepeat && !showingOptions && !moveActionPerformed){
                    KeyView.this.performClick();
                }
                else {
//                        if(repeatThread!=null) repeatThread.interrupt();
                    if(repeatCountDownTimer != null) repeatCountDownTimer.cancel();
                    repeat = false;
                }
                moveActionPerformed = false;
                showingOptions = false;
                mCountDownTimerToShowOptions.cancel();
                return true;

            case MotionEvent.ACTION_MOVE:
                float x = motionEvent.getX() - mDownX;
                float y = motionEvent.getY() - mDownY;

                if(minimumMoveLength <= Math.abs(x)){
                    if(x>0){//x is positive
                        //move cursor to the right
                        moveActionPerformed = true;
                        mKeyWasClicked.move(Constants.MOVE_RIGHT);
                        mCountDownTimerToShowOptions.cancel();
//                            if(moveDownOrRight)
//                                moveCursorToTheRight(inputConnection);
                    } else {
                        //move vursor to the left
                        moveActionPerformed = true;
                        mKeyWasClicked.move(Constants.MOVE_LEFT);
                        mCountDownTimerToShowOptions.cancel();

//                            if(moveUpOrLeft)
//                                moveCursorToTheLeft(inputConnection);
                    }
//                            ++mMoveCursorX;
                    mDownX = motionEvent.getX();
                }

                if(minimumMoveLength <= Math.abs(y)){
                    if(y>0){//y is positive
                        //move cursor down
                        moveActionPerformed = true;
                        mKeyWasClicked.move(Constants.MOVE_DOWN);
                        mCountDownTimerToShowOptions.cancel();

//                            if(moveDownOrRight)
//                                moveCursorDown(inputConnection);
                    }else {
                        //move cursor up
                        moveActionPerformed = true;
                        mKeyWasClicked.move(Constants.MOVE_UP);
                        mCountDownTimerToShowOptions.cancel();

//                            if(moveUpOrLeft)
//                                moveCursorUp(inputConnection);
                    }
//                            ++mMoveCursorY;
                    mDownY = motionEvent.getY();
                }

                return true;

            case MotionEvent.ACTION_CANCEL:
                mCountDownTimerToShowOptions.cancel();


        }
        return false;
    }

    /*
                Notify the caller that a click has been requested
                * by the user
                * It is called repeatedly when keyShouldRepeat==true
                * */
    interface KeyWasClicked {
        void onClick(KeyView key);
        void move(int MOVE_CONSTANT);
    }
}
