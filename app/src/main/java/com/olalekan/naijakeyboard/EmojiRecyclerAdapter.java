package com.olalekan.naijakeyboard;

import static com.olalekan.naijakeyboard.KeyView.OPTIONS_DELAY_TIME;
import static com.olalekan.naijakeyboard.KeyView.PREVIEW_PADDING_LEFT_RIGHT;
import static com.olalekan.naijakeyboard.KeyView.PREVIEW_PADDING_TOP_BUTTOM;
import static com.olalekan.naijakeyboard.KeyView.PREVIEW_SHOW_TIME_DELAY;
import static com.olalekan.naijakeyboard.KeyView.PREVIEW_TEXT_SIZE;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.olalekan.naijakeyboard.defaults.AllRoundUseful;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import keyboard_data.EmojisData;

public class EmojiRecyclerAdapter extends RecyclerView.Adapter<EmojiRecyclerAdapter.EmojiRecyclerAdapterViewHolder> {


    private static final int EMOJI_TEXT_SIZE = PREVIEW_TEXT_SIZE;
    Context mContext;
    LayoutInflater mLayoutInflater;
    String[] emojis;
    String emojiType;
    Handler mainHandler = new Handler(Looper.getMainLooper());



    EmojiWasClicked mEmojiWasClicked = com.olalekan.naijakeyboard.MyInputMethod.getInstance();

    public EmojiRecyclerAdapter(Context context, String[] emojis, String type) {
        mContext = context;
        this.emojis = emojis;
        emojiType = type;
        mLayoutInflater = LayoutInflater.from(context);



    }

    @NonNull
    @Override
    public EmojiRecyclerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EmojiRecyclerAdapterViewHolder(mLayoutInflater.inflate(R.layout.single_emoji, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiRecyclerAdapterViewHolder holder, int position) {
        holder.emoji.setText(emojis[position]);
    }

    @Override
    public int getItemCount() {
        return emojis.length;
    }

    int pointerPositionX = 0;
    int pointerPositionY = 0;


    public class EmojiRecyclerAdapterViewHolder extends RecyclerView.ViewHolder{
        final TextView emoji;


        public EmojiRecyclerAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            emoji = itemView.findViewById(R.id.emojiTextView);

            emoji.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pointerPositionX = (int) motionEvent.getRawX();
                    pointerPositionY = (int) motionEvent.getRawY();
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
//                            showPreview(mContext, (TextView) view);
                            if(Objects.equals(emojiType, EmojisData.SHADES)){
                                showOptionsEmoji = (TextView) view;
                                shades = EmojisData.getInstance().getShades(getAdapterPosition());
                                mCountDownTimerToShowOptions.start();
                            }

                            return true;
                        case MotionEvent.ACTION_UP:
                            if(!showingOptions) {
                                AllRoundUseful.vibrateIfRequired(mContext);
//                                ((Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VIBRATION_TIME);
                                mEmojiWasClicked.emojiClicked(((TextView)view).getText().toString());
                            }
                            showingOptions = false;
                            mCountDownTimerToShowOptions.cancel();
                            return true;

                        case MotionEvent.ACTION_CANCEL:
                            mCountDownTimerToShowOptions.cancel();
                            return true;
                    }
                    return false;
                }
            });


//            emoji.setPadding(PREVIEW_PADDING_LEFT_RIGHT, PREVIEW_PADDING_TOP_BUTTOM,
//                    PREVIEW_PADDING_LEFT_RIGHT,PREVIEW_PADDING_TOP_BUTTOM);
//            emoji.setTextSize(EMOJI_TEXT_SIZE);

            //if type == shades
            //emoji.getText
            //get the position of the item clicked in the list
            //if type == shades then get the different shades with the position
            //show the shades
            //done

//            emoji.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    emojiKey.setDefaultValueForDummyPreviewKey(emojiKey.toString());
//                    if (emojiType.equals(EmojisData.SHADES)) {
//                        ArrayList<String> shades =
//                                EmojisData.getInstance().getShades(getAdapterPosition());
//                        emojiKey.setOptions(shades);
//                    }
//
//                    emojiKey.setOnClickListener(new KeyView.KeyWasClicked() {
//                        @Override
//                        public void onClick(KeyView key) {
//                            mEmojiWasClicked.emojiClicked(emojiKey);
//                        }
//                    });
//
//                }
//            });
//            getAdapterPosition();

        }
    }





    boolean showingOptions = false;

    CountDownTimer mCountDownTimerToShowOptions = new CountDownTimer(OPTIONS_DELAY_TIME, OPTIONS_DELAY_TIME) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            showOptions(mContext, showOptionsEmoji, shades);
            showingOptions = true;
            this.cancel();
        }
    };

    TextView showOptionsEmoji;
    ArrayList<String> shades;

//    View.OnTouchListener emojiTouch = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            switch (motionEvent.getAction()){
//                case MotionEvent.ACTION_DOWN:
//                    ((Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VIBRATION_TIME);
//                    showPreview(mContext, (TextView) view);
//                    if(Objects.equals(emojiType, EmojisData.SHADES)){
//                        showOptionsEmoji = (TextView) view;
//                        shades =
//                                EmojisData.getInstance().getShades(getAdapterPosition());
//                        emojiKey.setOptions(shades);
//                        mCountDownTimerToShowOptions.start();
//                    }
//
//                    return true;
//                case MotionEvent.ACTION_UP:
//                    //If key should not repeat then perform click
//                    //performClick is an overridden method
//                    if(!showingOptions) mEmojiWasClicked.emojiClicked(((TextView)view).getText().toString());
//                    showingOptions = false;
//                    mCountDownTimerToShowOptions.cancel();
//                    return true;
//            }
//            return false;
//        }
//    };

    private void showPreview(Context context, TextView emojiText){
        PopupWindow popupWindow = new PopupWindow(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.single_emoji, null);

//        LinearLayout parent = (LinearLayout) emojiText.getParent();

        TextView previewText = popupView.findViewById(R.id.emojiTextView);
        previewText.setText(emojiText.getText().toString());
        previewText.setPadding(PREVIEW_PADDING_LEFT_RIGHT, PREVIEW_PADDING_TOP_BUTTOM,
                PREVIEW_PADDING_LEFT_RIGHT,PREVIEW_PADDING_TOP_BUTTOM);
        previewText.setTextSize(PREVIEW_TEXT_SIZE);
//        popupCard.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 35));
//        popupCard.setLayoutParams(new Constraints.LayoutParams(LayoutParams.WRAP_CONTENT, 35));

//        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)popupCard.getLayoutParams();
//        layoutParams.width = 35;

        popupWindow.setContentView(popupView);

        popupWindow.setBackgroundDrawable(null);

        int popupPreviewY = popupView.getTop() - emojiText.getHeight();
        int popupPreviewX = emojiText.getLeft();


//        popupWindow.showAtLocation(emojiText, Gravity.NO_GRAVITY, emojiText.getLeft(), popupPreviewY);
        popupWindow.showAtLocation(emojiText, Gravity.NO_GRAVITY, popupPreviewX, popupPreviewY);

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


    private void showOptions(Context context, TextView emojiText, ArrayList<String> options){
        AllRoundUseful.vibrateIfRequired(context);
//        ((Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VIBRATION_TIME);

        PopupWindow optionsPopupWindow = new PopupWindow(context);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.multiple_emoji_options, null);

        int gridSpanCount = options.size()>1 ? 2 : options.size();

//        options.add("O");
//        options.add("A");

        setupOptionsRecyclerView( popupView, gridSpanCount, options);

        optionsPopupWindow.setContentView(popupView);

        optionsPopupWindow.setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

        optionsPopupWindow.setOutsideTouchable(true);
        optionsPopupWindow.setTouchable(true);


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
//        int popupPreviewY = emojiText.getTop() - gridSpanCount*emojiText.getHeight();
//        int popupPreviewX = emojiText.getLeft();
//        int popupPreviewY = pointerPositionY - ((View)emojiText.getParent().getParent()).getBottom();
//        int popupPreviewX = pointerPositionX;

        int popupPreviewY = pointerPositionY;
        int popupPreviewX = pointerPositionX;

        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if(emojiText.getContext() != null)
                optionsPopupWindow.showAtLocation(emojiText, Gravity.NO_GRAVITY, popupPreviewX, popupPreviewY);
            }
        }) ;

    }

    private void setupOptionsRecyclerView( View popupView, int gridSpanCount, ArrayList<String> options) {
        RecyclerView previewRecyclerView = popupView.findViewById(R.id.emojiOptionsRecyclerView);

        //if the options are more than 3 then the grid size should be 4,
        // else the grid size should be the same as the size of the options

//        GridLayoutManager gridLayoutManager = new GridLayoutManager(popupView.getContext(), gridSpanCount,
//                RecyclerView.HORIZONTAL, false);
//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

//        GridLayoutManager.SpanSizeLookup lookup = new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                return 0;
//            }
//        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);

        previewRecyclerView.setLayoutManager(linearLayoutManager);
        EmojiOptionsRecyclerAdapter previewRecyclerAdapter = new EmojiOptionsRecyclerAdapter(mContext, options);
        previewRecyclerView.setAdapter(previewRecyclerAdapter);
    }


    public void setEmojiClickListener(EmojiWasClicked emojiClickListener){
        mEmojiWasClicked = emojiClickListener;
    }

    interface EmojiWasClicked{
        void emojiClicked(String emoji);
    }
}
