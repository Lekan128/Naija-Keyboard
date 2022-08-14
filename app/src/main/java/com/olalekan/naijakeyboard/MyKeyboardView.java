package com.olalekan.naijakeyboard;

import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.makeViewVisible;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.makeVisibilityToGone;

import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.olalekan.naijakeyboard.defaults.Constants;

import java.util.Set;
import java.util.concurrent.Executor;

/*
 * THIS is where the keyboard layout is
 * where the keyboard functions are
 * and where all the keyboards themselves are handled
 *
 *
 * It contains the OnKeyClicked interface that is used to send the key,
 * the letter and the showInEditable to MyInputMethod
 *
 * If the showInEditable is false then the character will not have a preview
 * also the character will have a function eg delete, space, next, symbols, etc
 * */

public class MyKeyboardView extends LinearLayout {

    SymbolsKeyboardView symbolsKeyboard;
    LetterKeybaordView lettersKeyboard;
    EmojiKeybaordView emojiKeyboard;
    NumberKeybardView numberKeyboard;

//    static MyKeyboardView instance;

//    public static MyKeyboardView getInstance(){
//        return instance;
//    }

    /*
     * There are conditions to show the copied word on the keyboard
     * Some words are too long so the mCopiedWordToBeShown will only contain
     * A part of those words (Todo:check clipboard manager)
     *
     * */
    public static String mCopiedWord = "";
    public static String mCopiedWordToBeShown = "";

    public void makeKeyboardVisible(@NonNull View view) {
        makeViewVisible(view);
//        view.setVisibility(VISIBLE);

        if (view != symbolsKeyboard) makeVisibilityToGone(symbolsKeyboard);
        if (view != lettersKeyboard) makeVisibilityToGone(lettersKeyboard);
        if (view != emojiKeyboard) makeVisibilityToGone(emojiKeyboard);
        if (view != numberKeyboard) makeVisibilityToGone(numberKeyboard);
    }

    /*
     * The executor is passed from MyInputMethod and it is used to create a thread
     * the mExecutor.execute(runnable) runs the runnable in a seperate thread
     * */
    private Executor mExecutor;

    public Executor getExecutor() {
        return mExecutor;
    }

    //from the input method. It is important to get the executor
    public void setExecutor(Executor executor) {
        //Todo: set the executor for letter keybaord
        mExecutor = executor;
        lettersKeyboard.setExecutor(executor);

    }

    Context mContext = getContext();

    LinearLayout KeyboardLinearLayout;

    //Constructor from MyKeyboardView
    public MyKeyboardView(Context context) {
        super(context);
        mContext = context;
        initializeControls(context);
    }

    public MyKeyboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeControls(context);
    }

    public MyKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initializeControls(context);
    }

    public void setKeyboardColour(int colour) {

        KeyboardLinearLayout.setBackgroundColor(colour);
//        lettersKeyboard.setBackgroundColor(colour);
//        symbolsKeyboard.setBackgroundColor(colour);
//        numberKeyboard.setBackgroundColor(colour);
//        emojiKeyboard.setBackgroundColor(colour);

    }

    private void initializeControls(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.my_keyboard_view, this);

//        instance = this;

        KeyboardLinearLayout = findViewById(R.id.keyboardLinearLayout);

        lettersKeyboard = findViewById(R.id.lettersKeyboard);
        symbolsKeyboard = findViewById(R.id.symbolsKeyboard);
        emojiKeyboard = findViewById(R.id.emojiKeyboard);
        numberKeyboard = findViewById(R.id.numberKeyboard);

        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                mCopiedWord = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
                //show the copied text for number keyboard and letter keyboard
                //makeViewVisible(viewNumberKeyboardTextCopied);
                if (mCopiedWord.length() > Constants.MAX_LENGTH_OF_COPIED_WORD_THAT_CAN_BE_SHOWN) {
                    final int NUMBER_OF_LETTERS_TO_BE_SHOWN = 8;
                    mCopiedWordToBeShown = mCopiedWord.substring(0, NUMBER_OF_LETTERS_TO_BE_SHOWN);
                    mCopiedWordToBeShown += "...";
//                    mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<>(List.of(substituteWord)));
//                    textViewNumberKeyboardCopied.setText(mCopiedWord);
                } else {
                    //Todo: for now
                    mCopiedWordToBeShown = mCopiedWord;
//                    mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<>(List.of(mCopiedWord)));
//                    textViewNumberKeyboardCopied.setText(mCopiedWord);
                }

                lettersKeyboard.showCopiedString();
                numberKeyboard.showCopiedString();
            }
        });

    }

    public void setToSymbolsKeyboard() {
        makeKeyboardVisible(symbolsKeyboard);
//        Done by set tto letter keyboard
//        Everytime letter keyboard is set symbols is reset
//        setToFirstSetOfSymbols();
    }

    public void setToLettersKeyboard() {
        lettersKeyboard.setToLettersKeyboard();
//        mKeyViewSymMode.changeLetterTo("1/4");
        symbolsKeyboard.resetSymbols();
//        setToFirstSetOfSymbols();
        makeKeyboardVisible(lettersKeyboard);

    }
    public void turnOffAutoCorrect() {
        lettersKeyboard.turnOffAutoCorrect();
    }

    public void setToEmojiKeyboard(){
        makeKeyboardVisible(emojiKeyboard);
    }

    public void setToNumbersKeyboard(){
        numberKeyboard.setToNumbersKeyboard();
//        if(mCopiedWord.isEmpty()) numberKeyboard.removeCopiedWordAndViewFromNumberKeyboard();
        makeKeyboardVisible(numberKeyboard);
//        numberKeyboard.setNumberKeyboardToNumbers();
    }

    public void setToLetterEmail(){
        lettersKeyboard.setToLetterEmail();
    }

    public void setToLetterUri(){
        lettersKeyboard.setToLetterUri();

    }

    public void changeKeyboardActionToGo(){
        lettersKeyboard.changeEnterButtonTextAndDrawable("Go", R.drawable.ic_baseline_navigate_next_24);
        symbolsKeyboard.changeEnterButtonTextAndDrawable("Go", R.drawable.ic_baseline_navigate_next_24);

    }

    public void changeKeyboardActionToNext(){
        lettersKeyboard.changeEnterButtonTextAndDrawable("Next", R.drawable.ic_baseline_navigate_next_24);
        symbolsKeyboard.changeEnterButtonTextAndDrawable("Next", R.drawable.ic_baseline_navigate_next_24);

    }

    public void changeKeyboardActionToSearch(){
        lettersKeyboard.changeEnterButtonTextAndDrawable("Search", R.drawable.ic_baseline_search_24);
        symbolsKeyboard.changeEnterButtonTextAndDrawable("Search", R.drawable.ic_baseline_search_24);

    }

    public void changeKeyboardActionToSend(){
//        for symbols keyboard
        lettersKeyboard.changeEnterButtonTextAndDrawable("Send", R.drawable.ic_baseline_send_24);
        symbolsKeyboard.changeEnterButtonTextAndDrawable("Send", R.drawable.ic_baseline_send_24);
    }

    public void changeKeyboardActionToDone(){
        lettersKeyboard.changeEnterButtonTextAndDrawable("Done", R.drawable.ic_baseline_done_24);
        symbolsKeyboard.changeEnterButtonTextAndDrawable("Done", R.drawable.ic_baseline_done_24);

    }

    public void changeKeyboardActionEnter(){
        lettersKeyboard.changeEnterButtonTextAndDrawable("Ent",R.drawable.ic_baseline_keyboard_return_24);
        symbolsKeyboard.changeEnterButtonTextAndDrawable("Ent",R.drawable.ic_baseline_keyboard_return_24);

    }

    public void changeCapslockToBlue(){
        lettersKeyboard.changeCapslockToBlue();
    }

    public void changeCapslockToBlack(){
        lettersKeyboard.changeCapslockToBlack();
    }

    //Todo: letter keyboard Number Keybaord
    public void clearCopiedWord(){
        if(!mCopiedWord.isEmpty())mCopiedWord = "";
        if(!mCopiedWordToBeShown.isEmpty())mCopiedWordToBeShown = "";
        numberKeyboard.removeCopiedWordAndViewFromNumberKeyboard();
        //Todo:
//        lettersKeyboard.changeToDefaultSuggestions();
    }

//    private void changeToDefaultSuggestions(){
//        //If email address change suggestion to email suggestions
//        //if uri change to uri suggestions
//        //default if usual suggestions
//        lettersKeyboard.changeToDefaultSuggestions();
//
////        mSuggestionRecyclerAdapter.changeSuggestions(mSuggestions);
//    }

    public void setDefaultSuggestions(Set<String> suggestions){
        lettersKeyboard.setDefaultSuggestions(suggestions);
    }

    public void setButtonsToSmallLetters(){
        lettersKeyboard.setButtonsToSmallLetters();
    }

    public void setButtonsToCapitalLetters(){
        lettersKeyboard.setButtonsToCapitalLetters();
    }

    public void setNumberKeyboardToNumbers() {
        numberKeyboard.setNumberKeyboardToNumbers();
    }

    public void setNumberKeyboardToSymbols() {
        numberKeyboard.setNumberKeyboardToSymbols();
    }
    public void setToFirstSetOfSymbols(){
        symbolsKeyboard.setToFirstSetOfSymbols();
    }

    public void setToSecondSetOfSymbols(){
        symbolsKeyboard.setToSecondSetOfSymbols();
    }

    public void setToThirdSetOfSymbols(){
        symbolsKeyboard.setToThirdSetOfSymbols();
    }

    public void setToForthSetOfSymbols(){
        symbolsKeyboard.setToForthSetOfSymbols();
    }

    /*
     * Sends the key and suggestion clicked ot MyInputMethod
     * */
    interface OnKeyClicked {
        void keyClicked(KeyView key, String letter, boolean sendToEditable);
        void SuggestionWasClicked(String suggestion, boolean addSpace);
    }

}
