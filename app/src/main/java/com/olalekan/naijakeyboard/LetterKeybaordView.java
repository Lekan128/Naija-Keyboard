package com.olalekan.naijakeyboard;

import static com.olalekan.naijakeyboard.MyKeyboardView.mCopiedWord;
import static com.olalekan.naijakeyboard.MyKeyboardView.mCopiedWordToBeShown;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.makeViewVisible;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.makeVisibilityToGone;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorDown;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorToTheLeft;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorToTheRight;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorUp;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.olalekan.naijakeyboard.defaults.AllRoundUseful;
import com.olalekan.naijakeyboard.defaults.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;

import suggestion_data.SuggestionData;
import suggestion_data.WordPredictor;

/*
 * THIS is where the letter keyboard layout is
 * and where all the layouts and functions for letter keyboard is handled
 *
 * It handles the layout of the keys with the R.layout.my_keyboard_view
 * This class sends the character in the button to the MyInputMethod class
 * it also send the boolean showInEditable
 * Through the OnKeyClicked interface from MyKeyboardView
 *
 * If the showInEditable is false then the character will not have a preview
 * also the character will have a function eg delete, space, next, symbols, etc
 */
public class LetterKeybaordView extends LinearLayout implements KeyView.KeyWasClicked {

    boolean shouldAutoCorrect = false;
    TextView correctionTextView;
//    private String mCopiedWord = "";

    /*
     * The executor is passed from MyInputMethod and it is used to create a thread
     * the mExecutor.execute(runnable) runs the runnable in a seperate thread
     * */
    private Executor mExecutor;

    //from the MyKeyboardView. It is important to get the executor
    public void setExecutor(Executor executor) {
        mExecutor = executor;
    }

    Context mContext = getContext();
    final SuggestionData suggestionData = SuggestionData.getInstance(mContext);

    MyInputMethod mMyInputMethod = MyInputMethod.getInstance();

    /*OnKwyClicked is used to send the key clicked to MyInputMethod*/
    private MyKeyboardView.OnKeyClicked mOnKeyClicked = mMyInputMethod;

    //keys
    KeyView mKeyView1, mKeyView2, mKeyView3, mKeyView4, mKeyView5, mKeyView6, mKeyView7, mKeyView8, mKeyView9, mKeyView0,
            mKeyViewQ, mKeyViewW, mKeyViewE, mKeyViewR, mKeyViewT, mKeyViewY, mKeyViewU, mKeyViewI, mKeyViewO, mKeyViewP,
            mKeyViewA, mKeyViewS, mKeyViewD, mKeyViewF, mKeyViewG, mKeyViewH, mKeyViewJ, mKeyViewK, mKeyViewL,
            mKeyViewZ, mKeyViewX, mKeyViewC, mKeyViewV, mKeyViewB, mKeyViewN, mKeyViewM,
            mKeyViewFullStop, mKeyViewSpace, mKeyViewDel, mKeyViewComma,
            mKeyViewCapsLock, mKeyViewSymbols, mKeyViewEnter,
            mKeyViewMic;

    RecyclerView suggestionRecyclerView;
    private com.olalekan.naijakeyboard.SuggestionRecyclerAdapter mSuggestionRecyclerAdapter;
    private ArrayList<String> mSuggestions = new ArrayList<>(Constants.DEFAULT_SUGGESTION_SET);
//            new ArrayList<>(Arrays.asList("Hey", "How are you?", "Comrade", "What's up?"));
    private ArrayList<String> mSuggestionCandidates;

    private StringBuilder wordClosestToCursor = new StringBuilder();

    //Constructors
    public LetterKeybaordView(Context context) {
        super(context);
        mContext = context;
        initializeControls(context);
    }

    public LetterKeybaordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeControls(context);
    }

    public LetterKeybaordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initializeControls(context);
    }

    private void initializeControls(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.letter_keyboard_view, this);

        correctionTextView = findViewById(R.id.correctionTextView);
        suggestionRecyclerView = findViewById(R.id.suggestionRecyclerView);
        clearCorrectionTextViewAndCorrectionString();

        initialiseNumbers();
        initialiseLetters();
        setLetterOptions();
        setUpRecyclerView();

        setNumberOptions();

        correctionTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String correctWord = correctionTextView.getText().toString();
                if (!correctWord.isEmpty()) {
                    mOnKeyClicked.SuggestionWasClicked(correctWord, true);
                    changeToDefaultSuggestions();
                    clearCorrectionTextViewAndCorrectionString();
                    clearCopiedWord();
                }
            }
        });

        setLetterOnClickListeners();

        ImageView emojiImageView = findViewById(R.id.emojiImageView);

        emojiImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MyInputMethod.getInstance().setToEmojiKeyboard();
//                emojiKeyboard.setVisibility(VISIBLE);
//                lettersKeyboard.setVisibility(GONE);
            }
        });

    }

    public void showCopiedString(){
        mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<>(List.of(mCopiedWordToBeShown)));
//        textViewNumberKeyboardCopied.setText(mCopiedWord);

    }

    public void setToLettersKeyboard(){
        turnOnAutoCorrect();
        //if you have not copied any word
        if(mCopiedWord.isEmpty()) changeToDefaultSuggestions();
        removeFromLetterVariations();
        clearCorrectionTextViewAndCorrectionString();
//        mKeyViewSymMode.changeLetterTo("1/4");
        //handled by turn of auto correct
//        makeVisibilityToVisible(suggestionRecyclerView);
//        makeKeyboardVisible(correctionTextView);

    }


    public void turnOffAutoCorrect() {
        shouldAutoCorrect = false;
//        setToLettersKeyboard();
//        changeKeyboardActionToGo();
        makeVisibilityToGone(correctionTextView);
        makeVisibilityToGone(suggestionRecyclerView);
    }

    public void turnOnAutoCorrect() {
        shouldAutoCorrect = true;
//        setToLettersKeyboard();
//        changeKeyboardActionToGo();
        makeViewVisible(correctionTextView);
        makeViewVisible(suggestionRecyclerView);
    }

    public void setToLetterEmail() {
        mKeyViewComma.changeLetterTo("@");
//        mKeyViewExclamation.changeLetterTo("@");
//        mKeyViewComma.changeLetterTo(".com");
//        changeKeyboardActionToGo();
    }

    public void setToLetterUri() {
        mKeyViewComma.changeLetterTo(".com");
//        mKeyViewExclamation.changeLetterTo("/");
//        mKeyViewComma.changeLetterTo(".com");
//        changeKeyboardActionToGo();
    }

    public void removeFromLetterVariations() {
        mKeyViewComma.changeLetterTo(",");
//        mKeyViewExclamation.changeLetterTo("!");
//        mKeyViewComma.changeLetterTo(",");
//        changeGoToEnter();
    }

    void changeEnterButtonTextAndDrawable(String text, int drawableId) {
        mKeyViewEnter.changeLetterTo(text);
//        mKeyViewSymEnter.changeLetterTo(text);
        mKeyViewEnter.changeImageDrawableToDrawableWithId(drawableId);
//        mKeyViewSymEnter.changeImageDrawableToDrawableWithId(drawableId);

    }

    void changeCapslockToBlue() {
        DrawableCompat.setTint(mKeyViewCapsLock.keyImage.getDrawable(), Color.BLUE);
    }

    void changeCapslockToBlack() {
        DrawableCompat.setTint(mKeyViewCapsLock.keyImage.getDrawable(), Color.BLACK);
    }

    private void initialiseNumbers(){
        //letter numbers
        mKeyView1 = findViewById(R.id.keyView1);
        mKeyView2 = findViewById(R.id.keyView2);
        mKeyView3 = findViewById(R.id.keyView3);
        mKeyView4 = findViewById(R.id.keyView4);
        mKeyView5 = findViewById(R.id.keyView5);
        mKeyView6 = findViewById(R.id.keyView6);
        mKeyView7 = findViewById(R.id.keyView7);
        mKeyView8 = findViewById(R.id.keyView8);
        mKeyView9 = findViewById(R.id.keyView9);
        mKeyView0 = findViewById(R.id.keyView0);
    }

    private void initialiseLetters() {
        mKeyViewQ = findViewById(R.id.keyViewQ);
        mKeyViewW = findViewById(R.id.keyViewW);
        mKeyViewE = findViewById(R.id.keyViewE);
        mKeyViewR = findViewById(R.id.keyViewR);
        mKeyViewT = findViewById(R.id.keyViewT);
        mKeyViewY = findViewById(R.id.keyViewY);
        mKeyViewU = findViewById(R.id.keyViewU);
        mKeyViewI = findViewById(R.id.keyViewI);
        mKeyViewO = findViewById(R.id.keyViewO);
        mKeyViewP = findViewById(R.id.keyViewP);


        mKeyViewA = findViewById(R.id.keyViewA);
        mKeyViewS = findViewById(R.id.keyViewS);
        mKeyViewD = findViewById(R.id.keyViewD);
        mKeyViewF = findViewById(R.id.keyViewF);
        mKeyViewG = findViewById(R.id.keyViewG);
        mKeyViewH = findViewById(R.id.keyViewH);
        mKeyViewJ = findViewById(R.id.keyViewJ);
        mKeyViewK = findViewById(R.id.keyViewK);
        mKeyViewL = findViewById(R.id.keyViewL);

        mKeyViewZ = findViewById(R.id.keyViewZ);
        mKeyViewX = findViewById(R.id.keyViewX);
        mKeyViewC = findViewById(R.id.keyViewC);
        mKeyViewV = findViewById(R.id.keyViewV);
        mKeyViewB = findViewById(R.id.keyViewB);
        mKeyViewN = findViewById(R.id.keyViewN);
        mKeyViewM = findViewById(R.id.keyViewM);

        mKeyViewSymbols = findViewById(R.id.keyViewSymbols);
        mKeyViewDel = findViewById(R.id.keyViewDel);

        mKeyViewCapsLock = findViewById(R.id.keyViewCapsLock);
        mKeyViewComma = findViewById(R.id.keyViewComma);
//        mKeyViewExclamation = findViewById(R.id.keyViewExclamationMark);
        mKeyViewSpace = findViewById(R.id.keyViewSpace);
//        mKeyViewComma = findViewById(R.id.keyViewComma);
        mKeyViewFullStop = findViewById(R.id.keyViewStop);
        mKeyViewEnter = findViewById(R.id.keyViewEnter);

        mKeyViewMic = findViewById(R.id.keyViewMic);
    }

    private void setUpRecyclerView() {
        //Todo: fix up recycler view code
//        suggestionRecyclerView = findViewById(R.id.suggestionRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,true);
        suggestionRecyclerView.setLayoutManager(linearLayoutManager);
        mSuggestionRecyclerAdapter = new com.olalekan.naijakeyboard.SuggestionRecyclerAdapter(getContext(), mSuggestions);
        suggestionRecyclerView.setAdapter(mSuggestionRecyclerAdapter);

        mSuggestionRecyclerAdapter.setRecyclerViewClickedListener(new com.olalekan.naijakeyboard.SuggestionRecyclerAdapter.RecyclerViewClicked() {
            @Override
            public void recyclerViewClicked(String suggestion) {
                AllRoundUseful.vibrateIfRequired(getContext());
                if(!mCopiedWord.isEmpty()){
                    //if a word has been copied
                    /*
                     * When the copied word is > 10 not all the words are shown
                     * this is to make sure the actual copied word is shown
                     * */
                    mOnKeyClicked.SuggestionWasClicked(mCopiedWord, false);
                }else {
                    mOnKeyClicked.SuggestionWasClicked(suggestion, true);
                }
                changeToDefaultSuggestions();
                clearCopiedWord();
            }
        });

    }

    private void clearCorrectionTextViewAndCorrectionString(){
        clearCorrectionTextView();
        clearCorrectionString();
//        if(!correctionTextView.getText().toString().isEmpty()) correctionTextView.setText("");
//        if(!correction.isEmpty()) correction = "";
    }

    private void clearCorrectionTextView(){
        if(!correctionTextView.getText().toString().isEmpty()) correctionTextView.setText("");
    }

    private void clearCorrectionString(){
        if(!correction.isEmpty()) correction = "";
    }

    private void clearSwitchedWord(){
        if(!switchedWord.isEmpty())switchedWord = "";

    }

    private void setCorrection(String correction){
        correctionTextView.setText(correction);
    }

    private void clearCopiedWord(){
        MyInputMethod.getInstance().clearCopiedWord();
//        if(!mCopiedWord.isEmpty())mCopiedWord = "";
//        removeCopiedWordAndViewFromNumberKeyboard();
    }

    public void setDefaultSuggestions(Set<String> suggestions){
        mSuggestions = new ArrayList<String>(suggestions);
        if(mSuggestionRecyclerAdapter != null){
            mSuggestionRecyclerAdapter.changeSuggestions(mSuggestions);
        }
    }

    private void changeToDefaultSuggestions(){
        //If email address change suggestion to email suggestions
        //if uri change to uri suggestions
        //default if usual suggestions
        mSuggestionRecyclerAdapter.changeSuggestions(mSuggestions);
    }

    private void setLetterOnClickListeners() {
        mKeyView1.setOnClickListener(this);
        mKeyView2.setOnClickListener(this);
        mKeyView3.setOnClickListener(this);
        mKeyView4.setOnClickListener(this);
        mKeyView5.setOnClickListener(this);
        mKeyView6.setOnClickListener(this);
        mKeyView7.setOnClickListener(this);
        mKeyView8.setOnClickListener(this);
        mKeyView9.setOnClickListener(this);
        mKeyView0.setOnClickListener(this);

        mKeyViewQ.setOnClickListener(this);
        mKeyViewW.setOnClickListener(this);
        mKeyViewE.setOnClickListener(this);
        mKeyViewR.setOnClickListener(this);
        mKeyViewT.setOnClickListener(this);
        mKeyViewY.setOnClickListener(this);
        mKeyViewU.setOnClickListener(this);
        mKeyViewI.setOnClickListener(this);
        mKeyViewO.setOnClickListener(this);
        mKeyViewP.setOnClickListener(this);

        mKeyViewA.setOnClickListener(this);
        mKeyViewS.setOnClickListener(this);
        mKeyViewD.setOnClickListener(this);
        mKeyViewF.setOnClickListener(this);
        mKeyViewG.setOnClickListener(this);
        mKeyViewH.setOnClickListener(this);
        mKeyViewJ.setOnClickListener(this);
        mKeyViewK.setOnClickListener(this);
        mKeyViewL.setOnClickListener(this);

        mKeyViewZ.setOnClickListener(this);
        mKeyViewX.setOnClickListener(this);
        mKeyViewC.setOnClickListener(this);
        mKeyViewV.setOnClickListener(this);
        mKeyViewB.setOnClickListener(this);
        mKeyViewN.setOnClickListener(this);
        mKeyViewM.setOnClickListener(this);

        mKeyViewSymbols.setOnClickListener(this);
        mKeyViewDel.setOnClickListener(this);

        mKeyViewCapsLock.setOnClickListener(this);
        mKeyViewComma.setOnClickListener(this);
//        mKeyViewExclamation.setOnClickListener(this);
        mKeyViewSpace.setOnClickListener(this);
//        mKeyViewComma.setOnClickListener(this);
        mKeyViewFullStop.setOnClickListener(this);
        mKeyViewEnter.setOnClickListener(this);

        mKeyViewMic.setOnClickListener(this);
    }

    public void setButtonsToSmallLetters(){
        mKeyViewQ.changeLetterTo("q");
        mKeyViewW.changeLetterTo("w");
        mKeyViewE.changeLetterTo("e");
        mKeyViewR.changeLetterTo("r");
        mKeyViewT.changeLetterTo("t");
        mKeyViewY.changeLetterTo("y");
        mKeyViewU.changeLetterTo("u");
        mKeyViewI.changeLetterTo("i");
        mKeyViewO.changeLetterTo("o");
        mKeyViewP.changeLetterTo("p");

        mKeyViewA.changeLetterTo("a");
        mKeyViewS.changeLetterTo("s");
        mKeyViewD.changeLetterTo( "d");
        mKeyViewF.changeLetterTo("f");
        mKeyViewG.changeLetterTo("g");
        mKeyViewH.changeLetterTo("h");
        mKeyViewJ.changeLetterTo("j");
        mKeyViewK.changeLetterTo("k");
        mKeyViewL.changeLetterTo("l");

        mKeyViewZ.changeLetterTo("z");
        mKeyViewX.changeLetterTo("x");
        mKeyViewC.changeLetterTo("c");
        mKeyViewV.changeLetterTo("v");
        mKeyViewB.changeLetterTo("b");
        mKeyViewN.changeLetterTo("n");
        mKeyViewM.changeLetterTo("m");
    }

    public void setButtonsToCapitalLetters(){
        mKeyViewQ.changeLetterTo("Q");
        mKeyViewW.changeLetterTo("W");
        mKeyViewE.changeLetterTo("E");
        mKeyViewR.changeLetterTo("R");
        mKeyViewT.changeLetterTo("T");
        mKeyViewY.changeLetterTo("Y");
        mKeyViewU.changeLetterTo("U");
        mKeyViewI.changeLetterTo("I");
        mKeyViewO.changeLetterTo("O");
        mKeyViewP.changeLetterTo("P");

        mKeyViewA.changeLetterTo("A");
        mKeyViewS.changeLetterTo("S");
        mKeyViewD.changeLetterTo( "D");
        mKeyViewF.changeLetterTo("F");
        mKeyViewG.changeLetterTo("G");
        mKeyViewH.changeLetterTo("H");
        mKeyViewJ.changeLetterTo("J");
        mKeyViewK.changeLetterTo("K");
        mKeyViewL.changeLetterTo("L");

        mKeyViewZ.changeLetterTo("Z");
        mKeyViewX.changeLetterTo("X");
        mKeyViewC.changeLetterTo("C");
        mKeyViewV.changeLetterTo("V");
        mKeyViewB.changeLetterTo("B");
        mKeyViewN.changeLetterTo("N");
        mKeyViewM.changeLetterTo("M");
    }

    public void setNumberOptions(){
        mKeyView1.setOptions(new ArrayList<>(Arrays.asList("①", "¹", "₁", "❶", "½", "⅓", "¼", "⅕", "⅙", "⅐", "⅛", "⅑", "⅒", "⅟")));
        mKeyView2.setOptions(new ArrayList<>(Arrays.asList("②", "²", "₂", "❷", "⅔", "⅖")));
        mKeyView3.setOptions(new ArrayList<>(Arrays.asList("③", "³", "₃", "❸", "¾", "⅗", "⅜")));
        mKeyView4.setOptions(new ArrayList<>(Arrays.asList("④", "⁴", "₄", "❹", "⅘")));
        mKeyView5.setOptions(new ArrayList<>(Arrays.asList("⑤", "⁵", "₅", "❺", "⅚", "⅝")));
        mKeyView6.setOptions(new ArrayList<>(Arrays.asList("⑥", "⁶", "₆", "❻")));
        mKeyView7.setOptions(new ArrayList<>(Arrays.asList("⑦", "⁷", "₇", "❼")));
        mKeyView8.setOptions(new ArrayList<>(Arrays.asList("⑧", "⁸", "₈", "❽")));
        mKeyView9.setOptions(new ArrayList<>(Arrays.asList("⑨", "⁹", "₉", "❾")));
        mKeyView0.setOptions(new ArrayList<>(Arrays.asList("⓪", "⁰", "₀", "⓿", "↉")));

    }

    public void setLetterOptions(){
        mKeyViewQ.setOptions(new ArrayList<>(Arrays.asList("ʠ", "Ɋ", "q̇", "q̃", "Ꝙ", "ꝙ")));
        mKeyViewW.setOptions(new ArrayList<>(Arrays.asList("Ẃ", "ẁ", "ẘ", "\uD835\uDD1A", "ʬ", "و", "ש")));
        mKeyViewE.setOptions(new ArrayList<>(Arrays.asList("\uD835\uDD3C", "∋", "\uD835\uDD08", "モ", "\uD808\uDC8A", "ẹ̀")));
        mKeyViewR.setOptions(new ArrayList<>(Arrays.asList("ŕ", "ℜ", "ʁ", "®", "ℝ", "ر")));
        mKeyViewT.setOptions(new ArrayList<>(Arrays.asList("ƫ", "Ŧ", "✝", "\uD835\uDD17", "₮", "ط", "ت")));
        mKeyViewY.setOptions(new ArrayList<>(Arrays.asList("Ұ", "\uD802\uDD05", "ƴ")));
        mKeyViewU.setOptions(new ArrayList<>(Arrays.asList("\uD835\uDD18", "Ǖ", "Ʊ", "⊍")));
        mKeyViewI.setOptions(new ArrayList<>(Arrays.asList("ⓘ", "ɨ", "ⰻ")));
        mKeyViewO.setOptions(new ArrayList<>(Arrays.asList("ɶ", "ѻ", "오", "⓪", "ⓞ", "ⓞ")));
        mKeyViewP.setOptions(new ArrayList<>(Arrays.asList("¶ ", "₱", "℗", "\uD835\uDD2D", "℘", "π")));

        mKeyViewA.setOptions(new ArrayList<>(Arrays.asList("α", "∆" , "\uD802\uDD00", "@", "Å", "ª", "\uD83C\uDD70")));
        mKeyViewS.setOptions(new ArrayList<>(Arrays.asList("Ş", "Ƨ", "\uD800\uDF14", "\uD835\uDD16", "ß", "س")));
        mKeyViewD.setOptions(new ArrayList<>(Arrays.asList("Ď", "δ", "\uD835\uDD07", "\uD835\uDD07")));
        mKeyViewF.setOptions(new ArrayList<>(Arrays.asList("ᵮ", "Ƒ", "Ⅎ", "ꟻ", "\uD800\uDF05")));
        mKeyViewG.setOptions(new ArrayList<>(Arrays.asList("ɣ", "\uD835\uDD0A", "Ⰳ", "ℊ", "ج", "غ")));
        mKeyViewH.setOptions(new ArrayList<>(Arrays.asList("Ħ", "\uD800\uDF37", "\uD802\uDD07")));
        mKeyViewJ.setOptions(new ArrayList<>(Arrays.asList("\uD835\uDD0D", "ʝ", "ⅉ")));
        mKeyViewK.setOptions(new ArrayList<>(Arrays.asList("ϰ", "\uD835\uDD0E", "Ҝ")));
        mKeyViewL.setOptions(new ArrayList<>(Arrays.asList("\uD835\uDD0F", "\uD835\uDD77", "\uD835\uDD29", "\uD835\uDD91", "Ł", "Լ", "ℒ", "ℓ", "ɮ", "ƚ", "L̅", "ل")));

        mKeyViewZ.setOptions(new ArrayList<>(Arrays.asList("ζ" + "\uD800\uDF06", "Ζ")));
        mKeyViewX.setOptions(new ArrayList<>(Arrays.asList("╳", "✕", "✗", "☒", "\uD835\uDD1B", "乂")));
        mKeyViewC.setOptions(new ArrayList<>(Arrays.asList("ç", "₢", "₡", "©", "ℭ")));
        mKeyViewV.setOptions(new ArrayList<>(Arrays.asList("Ṽ", "Ʋ", "Ʌ", "℣", "Ꮙ", "Ꮴ")));
        mKeyViewB.setOptions(new ArrayList<>(Arrays.asList("ℬ", "ب", "β", "Ḃ", "Ɓ", "Ƃ", "ȸ", "ƀ", "ᶀ", "ᵬ", "Ḇ", "Ɓ")));
        mKeyViewN.setOptions(new ArrayList<>(Arrays.asList("Ñ", "ɲ", "\uD835\uDD11", "ن")));
        mKeyViewM.setOptions(new ArrayList<>(Arrays.asList("ḿ", "ṁ", "ṁ", "\uD835\uDD10", "ᴹ", "م")));

        mKeyViewComma.setOptions(new ArrayList<>(Arrays.asList("?", "!", "'", "-", "/")));
    }

    private boolean isLetter(String s){
        String smallLetters = "abcdefghijklmnopqrstuvwxyz";
        String capitalLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for(int i = 0; i<smallLetters.length(); ++i){
            if (s.equals(Character.toString(smallLetters.charAt(i)))) return true;
            if (s.equals(Character.toString(capitalLetters.charAt(i)))) return true;
        }
        return false;

//        MyInputMethod myInputMethod = new MyInputMethod();
//        myInputMethod.getCurrentInputConnection();
    }

    private boolean isUsualSymbol(String s){
        String symbols = ",+-!.?/";

        for(int i = 0; i<symbols.length(); ++i){
            if (s.equals(Character.toString(symbols.charAt(i)))) return true;
        }
        return false;
    }

    String correction = ""; //the correction to be used to switch
    String previousCorrection = ""; //the correction that has already been used
    String switchedWord = ""; // the word that was switched for the correction
    String wordToBeAddedToSuggestions = "";
    String switchedWordToBeAddedToSuggestions = "";


    //called whenever the keyView is clicked
    /*
     * After space is pressed the best suggested word is inputted
     * After back space is pressed the switched word is inputted
     * typed word: word typed by user
     * suggestion word: word suggested by algorithm*/

    /*
     * After the user inputs a letter
     * correction will be changed
     * if the user inputs space:
     *       if there is a correction, it will be switched with the word the user typed
     *       if there is no correction then space will be inputted normally
     *
     * if the user press back space:
     *       space will be deleted
     *       if there is a switched word(ie correction word is on the screen)
     *            then the word on the edit text will be replaced with the switched word
     *              save the switched word
     *       if there is no switched word then the back space works as usual
     *
     * Every time anything apart from space, usual symbols or backspace is inputted then
     * correction and switched word will be cleared
     * and the word around the cursor will be checked for correction
     * */

    /*
     * Dont clear suggestion: after a new letter has been inputted
     *       after the word has been switched
     *
     * When delete is pressed to switched back a correction and the word is switched
     * we clear the correction and switched word and then we try to get new corrections and suggestions
     *
     * When space/ usualSymbol is pressed if there is a correction down effect it if conditions are met
     * put previousCorrection = Correction and clear the correction
     * previousCorrection will be used by delete to see if the correction is still there to be switched
     * IF I press space again the correction is gone
     * If there is no correction then put the word the user typed into the database
     * */

    @Override
    public void onClick(KeyView key) {
        String input = key.toString();
        clearCopiedWord();

        if(!deleteAllPossibleSuggestionCountdownRunning){
            startDeleteAllPossibleSuggestionCountdown();
        }

        if(!shouldAutoCorrect){
            mOnKeyClicked.keyClicked(key, input, key.showInEditable);
            return;
        }

        wordClosestToCursor.setLength(0);
//        MyInputMethod.getInstance
        com.olalekan.naijakeyboard.MyInputMethod myInputMethod = com.olalekan.naijakeyboard.MyInputMethod.getInstance();
        InputConnection inputConnection = myInputMethod.getCurrentInputConnection();
        String wordBeforeCursor = myInputMethod.getWordBeforeCursor(inputConnection);
        String wordAfterCursor = myInputMethod.getWordAfterCursor(inputConnection);

        if(myInputMethod.wordBeforeAndAfterCursorLengthIsNOTOk(wordBeforeCursor, wordAfterCursor)){
            //if the length of the word before and after cursor is bigger than
            // Constants.MAXIMUM_NUMBER_OF_LETTERS_TO_CHECK
            // then just pass the key and give the user the default suggestions
            mOnKeyClicked.keyClicked(key, input, key.showInEditable());
            changeToDefaultSuggestions();
            clearCorrectionTextViewAndCorrectionString();
            clearSwitchedWord();
            return;
        }



        /*
         * Append the words before and after the cursor with the new word in between
         * If the key can be shown in editable and it is not a usual symbol
         *
         * else don't append it*/
        if(key.showInEditable && !isUsualSymbol(input))
            wordClosestToCursor.append(wordBeforeCursor).append(input).append(wordAfterCursor);
        else wordClosestToCursor.append(wordBeforeCursor).append(wordAfterCursor);



        /*
         * send the key that was clicked to MyInputMethod
         * if the space, or usual symbol is not pressed.
         *
         * Space handles its own sending because it might input corrections
         * */
        if(!input.equals("Space") && !isUsualSymbol(input))mOnKeyClicked.keyClicked(key, input, key.showInEditable());

//        if(input.equals("Space") || input.equals("Del")){
//        }

//        mExecutor.execute(getCorrectionRunnable);
        mSuggestionCandidates = new ArrayList<>();

        if(input.equals("Del")){
            wordBeforeCursor = myInputMethod.getWordBeforeCursor(inputConnection);
            wordAfterCursor = myInputMethod.getWordAfterCursor(inputConnection);

            //THIS  handles null word closest to cursor
            if (myInputMethod.wordBeforeAndAfterCursorLengthIsNOTOk(wordBeforeCursor, wordAfterCursor)) {
                changeToDefaultSuggestions();
                clearCorrectionTextViewAndCorrectionString();
                clearSwitchedWord();
                wordClosestToCursor.setLength(0);
                return;
            }

            wordClosestToCursor.setLength(0);
            wordClosestToCursor.append(wordBeforeCursor).append(wordAfterCursor);

            /*
             * if a word was switched before
             * and the user is still on the word switch it back
             * This is to make sure that the word is only switched
             * when the cursor is near the word that was actually corrected
             * to prevent correcting when the user has already left the word
             * like when a highlighted text is required to be deleted
             * or you moved to a new edittext
             * */
            if (!switchedWord.isEmpty() && wordClosestToCursor.toString().toLowerCase(Locale.ROOT).equals(previousCorrection)) {
                mOnKeyClicked.SuggestionWasClicked(switchedWord, false);
                switchedWordToBeAddedToSuggestions = switchedWord;
                mExecutor.execute(addToSuggestionsRunnable);
                clearCorrectionTextViewAndCorrectionString();
                clearSwitchedWord();
                return;
            }

            //If the word closest to the cursor is less than 1 or just 1 letter
            //we don't have to look for suggestions or correction
            if (wordClosestToCursor.length() <= 1) {
                changeToDefaultSuggestions();
                wordClosestToCursor.setLength(0);
                clearCorrectionTextViewAndCorrectionString();
                clearSwitchedWord();
                return;
            }


            //what to do usually


//                    mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
//                    suggestionsThread.start();

            mExecutor.execute(getSuggestionsRunnable);
            mExecutor.execute(getCorrectionRunnable);
//                    if(suggestionData.isSuggestionsDoneLoading())
//                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));


            //if the get suggestion runnable has not changed the suggestion
            if (mSuggestionCandidates.isEmpty())
                mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));

            return;
        }

        if(input.equals("Space") || isUsualSymbol(input)){
            /*
             * if there is a correction down:
             *   set switched word to the word closest to the cursor
             *   then switch the word to the correction with space in front of it
             * */

            if(!correction.isEmpty()){
                //if the user inputs the correct word by himself no need to correct him
                if(correction.equals(wordClosestToCursor.toString().toLowerCase(Locale.ROOT))){
                    mOnKeyClicked.keyClicked(key, input, key.showInEditable());
                    changeToDefaultSuggestions();
                    clearCorrectionTextViewAndCorrectionString();
                    return;
//                    break;
                }

                switchedWord = wordClosestToCursor.toString();
                /*//if the first character of the switched word is capital
                //change the corrections first letter to capital
                if(Character.isUpperCase(switchedWord.charAt(0))){
                    correction = Constants.capitaliseFirstLetterOf(correction);
                }*/

                previousCorrection = correction;


                mOnKeyClicked.SuggestionWasClicked(correction, false);
//                mOnKeyClicked.keyClicked(key, input, key.showInEditable());

            } else {
                //send the word to the database
                wordToBeAddedToSuggestions = wordClosestToCursor.toString();
                mExecutor.execute(addToSuggestionsIfNotYetExcessiveInDatabaseRunnable);
            }

            mOnKeyClicked.keyClicked(key, input, key.showInEditable());

            changeToDefaultSuggestions();
            clearCorrectionTextViewAndCorrectionString();

//                mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);

            wordClosestToCursor.setLength(0);
//            break;
            return;
        }

        if (key.showInEditable && isLetter(input)) {
            /*
             * If you input a normal letter cleat the suggestion and correction
             * get new suggestion and correction
             * */
//                    word.append(wordBeforeCursor);
//                    word.append(input);
//                    word.append(wordAfterCursor);

//                    suggestionsThread.start();

            clearCorrectionTextViewAndCorrectionString();
            clearSwitchedWord();
            mExecutor.execute(getSuggestionsRunnable);
            mExecutor.execute(getCorrectionRunnable);

//                    if(suggestionData.isSuggestionsDoneLoading())
//                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));


            if (mSuggestionCandidates.isEmpty())
                mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));
//                    else mSuggestionRecyclerAdapter.changeSuggestions(mSuggestionCandidates);
//                    mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(word.toString().toLowerCase(Locale.ROOT))));
            return;

        } else {

            //if all the conditions above are not met do this
            //NOTE: MAKE SURE ALL THE OTHER CONDITIONS HAVE RETURN IN THEM TO AVOID ANY UNFORESEEN ERROR
            clearCorrectionTextViewAndCorrectionString();
            clearSwitchedWord();
            changeToDefaultSuggestions();
        }


//        switch (input){
//            case "Del":
//                wordBeforeCursor = myInputMethod.getWordBeforeCursor(inputConnection);
//                wordAfterCursor = myInputMethod.getWordAfterCursor(inputConnection);
//
//                //THIS  handles null word closest to cursor
//                if (myInputMethod.wordBeforeAndAfterCursorLengthIsNOTOk(wordBeforeCursor, wordAfterCursor)) {
//                    changeToDefaultSuggestions();
//                    clearCorrectionTextViewAndCorrectionString();
//                    clearSwitchedWord();
//                    wordClosestToCursor.setLength(0);
//                    break;
//                }
//
//                wordClosestToCursor.setLength(0);
//                wordClosestToCursor.append(wordBeforeCursor).append(wordAfterCursor);
//
//                /*
//                 * if a word was switched before
//                 * and the user is still on the word switch it back
//                 * This is to make sure that the word is only switched
//                 * when the cursor is near the word that was actually corrected
//                 * to prevent correcting when the user has already left the word
//                 * like when a highlighted text is required to be deleted
//                 * or you moved to a new edittext
//                 * */
//                if (!switchedWord.isEmpty() && wordClosestToCursor.toString().equals(previousCorrection)) {
//                    mOnKeyClicked.SuggestionWasClicked(switchedWord, false);
//                    switchedWordToBeAddedToSuggestions = switchedWord;
//                    mExecutor.execute(addToSuggestionsRunnable);
//                    clearCorrectionTextViewAndCorrectionString();
//                    clearSwitchedWord();
//                    break;
//                }
//
//                //If the word closest to the cursor is less than 1 or just 1 letter
//                //we don't have to look for suggestions or correction
//                if (wordClosestToCursor.length() <= 1) {
//                    changeToDefaultSuggestions();
//                    wordClosestToCursor.setLength(0);
//                    clearCorrectionTextViewAndCorrectionString();
//                    clearSwitchedWord();
//                    break;
//                }
//
//
//                //what to do usually
//
//
////                    mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
////                    suggestionsThread.start();
//
//                mExecutor.execute(getSuggestionsRunnable);
//                mExecutor.execute(getCorrectionRunnable);
////                    if(suggestionData.isSuggestionsDoneLoading())
////                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));
//
//
//                //if the get suggestion runnable has not changed the suggestion
//                if (mSuggestionCandidates.isEmpty())
//                    mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));
//
//                break;
////            case "Space":
////                /*
////                 * if there is a correction down:
////                 *   set switched word to the word closest to the cursor
////                 *   then switch the word to the correction with space in front of it
////                 * */
////
////                if(!correction.isEmpty()){
////                    //if the user inputs the correct word by himself no need to correct him
////                    if(correction.equals(wordClosestToCursor.toString().toLowerCase(Locale.ROOT))){
////                        mOnKeyClicked.keyClicked(key, input, key.showInEditable());
////                        changeToDefaultSuggestions();
////                        clearCorrectionTextViewAndCorrectionString();
////                        break;
////                    }
////
////                    switchedWord = wordClosestToCursor.toString();
////                    //if the first character of the switched word is capital
////                    //change the corrections first letter to capital
////                    if(Character.isUpperCase(switchedWord.charAt(0))){
////                        correction = Constants.capitaliseFirstLetterOf(correction);
////                    }
////
////                    previousCorrection = correction;
////
////                    mOnKeyClicked.SuggestionWasClicked(correction, false);
//////                mOnKeyClicked.keyClicked(key, input, key.showInEditable());
////
////                }
////
////                mOnKeyClicked.keyClicked(key, input, key.showInEditable());
////
////                changeToDefaultSuggestions();
////                clearCorrectionTextViewAndCorrectionString();
////
//////                mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
////
////                wordClosestToCursor.setLength(0);
////                break;
//
//            default:
//                if(input.equals("Space") || isUsualSymbol(input)){
//                    /*
//                     * if there is a correction down:
//                     *   set switched word to the word closest to the cursor
//                     *   then switch the word to the correction with space in front of it
//                     * */
//
//                    if(!correction.isEmpty()){
//                        //if the user inputs the correct word by himself no need to correct him
//                        if(correction.equals(wordClosestToCursor.toString().toLowerCase(Locale.ROOT))){
//                            mOnKeyClicked.keyClicked(key, input, key.showInEditable());
//                            changeToDefaultSuggestions();
//                            clearCorrectionTextViewAndCorrectionString();
//                            return;
////                    break;
//                        }
//
//                        switchedWord = wordClosestToCursor.toString();
//                        //if the first character of the switched word is capital
//                        //change the corrections first letter to capital
//                        if(Character.isUpperCase(switchedWord.charAt(0))){
//                            correction = Constants.capitaliseFirstLetterOf(correction);
//                        }
//
//                        previousCorrection = correction;
//
//
//                        mOnKeyClicked.SuggestionWasClicked(correction, false);
////                mOnKeyClicked.keyClicked(key, input, key.showInEditable());
//
//                    }
//
//                    mOnKeyClicked.keyClicked(key, input, key.showInEditable());
//
//                    changeToDefaultSuggestions();
//                    clearCorrectionTextViewAndCorrectionString();
//
////                mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
//
//                    wordClosestToCursor.setLength(0);
////            break;
//                    break;
//                }
//                if (key.showInEditable && isLetter(input)) {
//                    /*
//                     * If you input a normal letter cleat the suggestion and correction
//                     * get new suggestion and correction
//                     * */
////                    word.append(wordBeforeCursor);
////                    word.append(input);
////                    word.append(wordAfterCursor);
//
////                    suggestionsThread.start();
//
//                    clearCorrectionTextViewAndCorrectionString();
//                    clearSwitchedWord();
//                    mExecutor.execute(getSuggestionsRunnable);
//                    mExecutor.execute(getCorrectionRunnable);
//
////                    if(suggestionData.isSuggestionsDoneLoading())
////                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));
//
//
//                    if (mSuggestionCandidates.isEmpty())
//                        mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));
////                    else mSuggestionRecyclerAdapter.changeSuggestions(mSuggestionCandidates);
////                    mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(word.toString().toLowerCase(Locale.ROOT))));
//                    break;
//
//                } else {
//                    clearCorrectionTextViewAndCorrectionString();
//                    clearSwitchedWord();
//                    changeToDefaultSuggestions();
//                }
//
//
//        }


//        HashMap<String, Integer> suggestionHashMap;
//        if (suggestionData.isSuggestionsDoneLoading()){
//            suggestionHashMap = suggestionData.getSuggestionsHashMap();
//        } else suggestionHashMap = new HashMap<>();
//
//        WordPredictor.correction(word.toString(), suggestionData.getSuggestionsHashMap());


//        switch (input){
//            case "Del":
//                if(wordClosestToCursor.length() > 1){
////                    word.deleteCharAt(word.length()-1);
//                    wordBeforeCursor = myInputMethod.getWordBeforeCursor(inputConnection);
//                    wordAfterCursor = myInputMethod.getWordAfterCursor(inputConnection);
//
//                    if(myInputMethod.wordBeforeAndAfterCursorLengthIsNOTOk(wordBeforeCursor, wordAfterCursor)){
//                        changeToDefaultSuggestions();
//                        wordClosestToCursor.setLength(0);
//                        break;
//                    }
//
//                    wordClosestToCursor.setLength(0);
//                    wordClosestToCursor.append(wordBeforeCursor).append(wordAfterCursor);
////                    mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
////                    suggestionsThread.start();
//
//                    mExecutor.execute(getSuggestionsRunnable);
////                    if(suggestionData.isSuggestionsDoneLoading())
////                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));
//
//
//                    if(mSuggestionCandidates.isEmpty()) mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));
////                    else mSuggestionRecyclerAdapter.changeSuggestions(mSuggestionCandidates);
//                } else {
//                    changeToDefaultSuggestions();
//                    wordClosestToCursor.setLength(0);
//                }
//
//                break;
//            case "Space":
//                changeToDefaultSuggestions();
////                mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
//
//                wordClosestToCursor.setLength(0);
//                break;
//            default:
//                if (key.showInEditable && isLetter(input)) {
////                    word.append(wordBeforeCursor);
////                    word.append(input);
////                    word.append(wordAfterCursor);
//
////                    suggestionsThread.start();
//
//                    mExecutor.execute(getSuggestionsRunnable);
////                    if(suggestionData.isSuggestionsDoneLoading())
////                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));
//
//
//                    if(mSuggestionCandidates.isEmpty()) mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));
////                    else mSuggestionRecyclerAdapter.changeSuggestions(mSuggestionCandidates);
////                    mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(word.toString().toLowerCase(Locale.ROOT))));
//
//                }
//        }


    }

    @Override
    public void move(int MOVE_CONSTANT) {
        boolean canMoveUpOrLeft = false;
        boolean canMoveDownOrRight = false;
        com.olalekan.naijakeyboard.MyInputMethod myInputMethod = com.olalekan.naijakeyboard.MyInputMethod.getInstance();
        InputConnection inputConnection = myInputMethod.getCurrentInputConnection();

        CharSequence textBeforeCursor = inputConnection.getTextBeforeCursor(1, InputConnection.GET_TEXT_WITH_STYLES);
        CharSequence textAfterCursor = inputConnection.getTextAfterCursor(1, InputConnection.GET_TEXT_WITH_STYLES);

        if(textBeforeCursor != null && !textBeforeCursor.toString().isEmpty()){
            canMoveUpOrLeft = true;
        }
        if(textAfterCursor != null && !textAfterCursor.toString().isEmpty()){
            canMoveDownOrRight = true;
        }

        switch (MOVE_CONSTANT){
            case Constants.MOVE_LEFT:
                if(canMoveUpOrLeft)
                    moveCursorToTheLeft(inputConnection);
                break;

            case Constants.MOVE_UP:
                if(canMoveUpOrLeft)
                    moveCursorUp(inputConnection);
                break;

            case Constants.MOVE_RIGHT:
                if(canMoveDownOrRight)
                    moveCursorToTheRight(inputConnection);
                break;

            case Constants.MOVE_DOWN:
                if(canMoveDownOrRight)
                    moveCursorDown(inputConnection);
                break;
        }

    }

    Runnable getSuggestionsRunnable = new Runnable() {
        @Override
        public void run() {
            String wordToBeChecked = wordClosestToCursor.toString();
            if(wordToBeChecked.length()>15) return;
            if(suggestionData.isSuggestionsDoneLoading())
                mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordToBeChecked.toLowerCase(Locale.ROOT), suggestionData.getSuggestionsHashMap()));

            //if the wordToBeChecked is still the same as the word closest to the cursor then replace the suggestion
            if(wordToBeChecked.equals(wordClosestToCursor.toString())){
                suggestionRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!mSuggestionCandidates.isEmpty())
                        mSuggestionRecyclerAdapter.changeSuggestions(mSuggestionCandidates);

                    }
                });
            }
        }
    };

    Runnable getCorrectionRunnable = new Runnable() {
        @Override
        public void run() {
            String wordToBeChecked = wordClosestToCursor.toString();
            if(suggestionData.isSuggestionsDoneLoading())
                correction = WordPredictor.correction(wordToBeChecked, suggestionData.getSuggestionsHashMap());

//                mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionData.getSuggestionsHashMap()));

            //if the wordToBeChecked is still the same as the word closest to the cursor then replace the correction
            if(wordToBeChecked.equals(wordClosestToCursor.toString())){
                suggestionRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if(SuggestionData.suggestionsDoneLoading)
                            setCorrection(correction);

                    }
                });
            }
        }
    };

    Runnable addToSuggestionsRunnable = new Runnable() {
        @Override
        public void run() {
            if(!switchedWordToBeAddedToSuggestions.isEmpty() && SuggestionData.suggestionsDoneLoading){
                SuggestionData.addToSuggestions(switchedWordToBeAddedToSuggestions.toLowerCase(Locale.ROOT), getContext());
                switchedWordToBeAddedToSuggestions = "";
            }
        }
    };

    Runnable addToSuggestionsIfNotYetExcessiveInDatabaseRunnable = new Runnable() {
        @Override
        public void run() {
            /*
             * If the word is already in the database and it's value is not more than
             * Constants.MAX_NUMBER_OF_TIMES_A_WORD_SHOULD_BE_ADDED_TO_DATABASE
             *       add it to the database
             * If the word is not in the database/hashmap at all add it
             * */
//            String wordToBeAddedToSuggestions = wordClosestToCursor.toString();
            if(!wordToBeAddedToSuggestions.isEmpty() && suggestionData.isSuggestionsDoneLoading()){
                HashMap<String, Integer> hashMap = suggestionData.getSuggestionsHashMap();
                if(hashMap.containsKey(wordToBeAddedToSuggestions) && hashMap.get(wordToBeAddedToSuggestions) < Constants.MAX_NUMBER_OF_TIMES_A_WORD_SHOULD_BE_ADDED_TO_DATABASE){
                    SuggestionData.addToSuggestions(wordToBeAddedToSuggestions.toLowerCase(Locale.ROOT), getContext());
                }

                if(!hashMap.containsKey(wordToBeAddedToSuggestions)){
                    SuggestionData.addToSuggestions(wordToBeAddedToSuggestions.toLowerCase(Locale.ROOT), getContext());
                }
                wordToBeAddedToSuggestions = "";
            }
        }
    };

    boolean deleteAllPossibleSuggestionCountdownRunning = false;
    private final int FIVE_MINUTES = 300000;//IN MILLI SECONDS
    //helps delete all possible suggestions that were not added to the database every 5 minutes
    CountDownTimer deleteAllPossibleSuggestionsToBeAdded = new CountDownTimer(FIVE_MINUTES, FIVE_MINUTES) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            SuggestionData.possibleSuggestionsToBeAddedHashMap = new HashMap<>();
            Log.i("Suggestion countdown", "onFinish Count: All possible suggestions deleted");
            deleteAllPossibleSuggestionCountdownRunning = false;
        }
    };

    private void startDeleteAllPossibleSuggestionCountdown(){
        Log.i("Suggestion countdown", "onStart Count: All possible suggestions countdown started");
        deleteAllPossibleSuggestionsToBeAdded.start();
        deleteAllPossibleSuggestionCountdownRunning = true;
    }


}
