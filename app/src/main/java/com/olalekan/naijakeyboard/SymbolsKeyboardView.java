package com.olalekan.naijakeyboard;

import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorDown;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorToTheLeft;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorToTheRight;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorUp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.olalekan.naijakeyboard.KeyView;
import com.olalekan.naijakeyboard.MyInputMethod;
import com.olalekan.naijakeyboard.MyKeyboardView;
import com.olalekan.naijakeyboard.R;
import com.olalekan.naijakeyboard.defaults.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class SymbolsKeyboardView extends LinearLayout implements KeyView.KeyWasClicked {

    /*OnKwyClicked is used to send the key clicked to MyInputMethod*/
    private MyKeyboardView.OnKeyClicked mOnKeyClicked = MyInputMethod.getInstance();

    KeyView mKeyViewSym1, mKeyViewSym2, mKeyViewSym3, mKeyViewSym4, mKeyViewSym5, mKeyViewSym6, mKeyViewSym7, mKeyViewSym8, mKeyViewSym9,  mKeyViewSym0,
            mKeyViewR2C1, mKeyViewR2C2, mKeyViewR2C3, mKeyViewR2C4, mKeyViewR2C5, mKeyViewR2C6, mKeyViewR2C7, mKeyViewR2C8, mKeyViewR2C9, mKeyViewR2C10,
            mKeyViewR3C1, mKeyViewR3C2, mKeyViewR3C3, mKeyViewR3C4, mKeyViewR3C5, mKeyViewR3C6, mKeyViewR3C7, mKeyViewR3C8, mKeyViewR3C9, mKeyViewR3C10,
            mKeyViewR4C1, mKeyViewR4C2, mKeyViewR4C3, mKeyViewR4C4, mKeyViewR4C5, mKeyViewR4C6, mKeyViewR4C7,
            mKeyViewSymFullStop, mKeyViewSymSpace, mKeyViewSymDel, mKeyViewSymQuestionMark,
            mKeyViewSymMode, mKeyViewLettters, mKeyViewSymEnter;

    public SymbolsKeyboardView(Context context) {
        super(context);
//        mContext = context;
        initializeControls(context);
    }

    public SymbolsKeyboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeControls(context);
    }

    public SymbolsKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeControls(context);
    }

    private void initializeControls(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.symbols_keyboard_view, this);

        initialiseSymbols();

        setNumberOptions();

        setSymbolsOnClickListeners();



    }

    void changeEnterButtonTextAndDrawable(String text, int drawableId){
//        mKeyViewEnter.changeLetterTo(text);
        mKeyViewSymEnter.changeLetterTo(text);
//        mKeyViewEnter.changeImageDrawableToDrawableWithId(drawableId);
        mKeyViewSymEnter.changeImageDrawableToDrawableWithId(drawableId);

    }

    private void initialiseNumbers(){
        //symbol numbers
        mKeyViewSym1 = findViewById(R.id.keyViewSym1);
        mKeyViewSym2 = findViewById(R.id.keyViewSym2);
        mKeyViewSym3 = findViewById(R.id.keyViewSym3);
        mKeyViewSym4 = findViewById(R.id.keyViewSym4);
        mKeyViewSym5 = findViewById(R.id.keyViewSym5);
        mKeyViewSym6 = findViewById(R.id.keyViewSym6);
        mKeyViewSym7 = findViewById(R.id.keyViewSym7);
        mKeyViewSym8 = findViewById(R.id.keyViewSym8);
        mKeyViewSym9 = findViewById(R.id.keyViewSym9);
        mKeyViewSym0 = findViewById(R.id.keyViewSym0);

    }

    private void initialiseSymbols(){
        mKeyViewSym1 = findViewById(R.id.keyViewSym1);
        mKeyViewSym2 = findViewById(R.id.keyViewSym2);
        mKeyViewSym3 = findViewById(R.id.keyViewSym3);
        mKeyViewSym4 = findViewById(R.id.keyViewSym4);
        mKeyViewSym5 = findViewById(R.id.keyViewSym5);
        mKeyViewSym6 = findViewById(R.id.keyViewSym6);
        mKeyViewSym7 = findViewById(R.id.keyViewSym7);
        mKeyViewSym8 = findViewById(R.id.keyViewSym8);
        mKeyViewSym9 = findViewById(R.id.keyViewSym9);
        mKeyViewSym0 = findViewById(R.id.keyViewSym0);

        mKeyViewR2C1 = findViewById(R.id.keyViewSymR2C1);
        mKeyViewR2C2 = findViewById(R.id.keyViewSymR2C2);
        mKeyViewR2C3 = findViewById(R.id.keyViewSymR2C3);
        mKeyViewR2C4 = findViewById(R.id.keyViewSymR2C4);
        mKeyViewR2C5 = findViewById(R.id.keyViewSymR2C5);
        mKeyViewR2C6 = findViewById(R.id.keyViewSymR2C6);
        mKeyViewR2C7 = findViewById(R.id.keyViewSymR2C7);
        mKeyViewR2C8 = findViewById(R.id.keyViewSymR2C8);
        mKeyViewR2C9 = findViewById(R.id.keyViewSymR2C9);
        mKeyViewR2C10 = findViewById(R.id.keyViewSymR2C10);


        mKeyViewR3C1 = findViewById(R.id.keyViewSymR3C1);
        mKeyViewR3C2 = findViewById(R.id.keyViewSymR3C2);
        mKeyViewR3C3 = findViewById(R.id.keyViewSymR3C3);
        mKeyViewR3C4 = findViewById(R.id.keyViewSymR3C4);
        mKeyViewR3C5 = findViewById(R.id.keyViewSymR3C5);
        mKeyViewR3C6 = findViewById(R.id.keyViewSymR3C6);
        mKeyViewR3C7 = findViewById(R.id.keyViewSymR3C7);
        mKeyViewR3C8 = findViewById(R.id.keyViewSymR3C8);
        mKeyViewR3C9 = findViewById(R.id.keyViewSymR3C9);
        mKeyViewR3C10 = findViewById(R.id.keyViewSymR3C10);

        mKeyViewR4C1 = findViewById(R.id.keyViewSymR4C1);
        mKeyViewR4C2 = findViewById(R.id.keyViewSymR4C2);
        mKeyViewR4C3 = findViewById(R.id.keyViewSymR4C3);
        mKeyViewR4C4 = findViewById(R.id.keyViewSymR4C4);
        mKeyViewR4C5 = findViewById(R.id.keyViewSymR4C5);
        mKeyViewR4C6 = findViewById(R.id.keyViewSymR4C6);
        mKeyViewR4C7 = findViewById(R.id.keyViewSymR4C7);

        mKeyViewLettters = findViewById(R.id.keyViewAbc);
        mKeyViewSymDel = findViewById(R.id.keyViewSymDel);

        mKeyViewSymMode = findViewById(R.id.keyViewSymMode);
        mKeyViewSymFullStop = findViewById(R.id.keyViewSymStop);
        mKeyViewSymSpace = findViewById(R.id.keyViewSymSpace);
        mKeyViewSymQuestionMark = findViewById(R.id.keyViewSymQuestionMark);
        mKeyViewSymEnter = findViewById(R.id.keyViewSymEnter);


    }

    public void resetSymbols(){
        mKeyViewSymMode.changeLetterTo("1/4");
        setToFirstSetOfSymbols();
    }

//    private void clearCorrectionTextViewAndCorrectionString(){
//        clearCorrectionTextView();
//        clearCorrectionString();
////        if(!correctionTextView.getText().toString().isEmpty()) correctionTextView.setText("");
////        if(!correction.isEmpty()) correction = "";
//    }

    private void setSymbolsOnClickListeners() {
        mKeyViewSym1.setOnClickListener(this);
        mKeyViewSym2.setOnClickListener(this);
        mKeyViewSym3.setOnClickListener(this);
        mKeyViewSym4.setOnClickListener(this);
        mKeyViewSym5.setOnClickListener(this);
        mKeyViewSym6.setOnClickListener(this);
        mKeyViewSym7.setOnClickListener(this);
        mKeyViewSym8.setOnClickListener(this);
        mKeyViewSym9.setOnClickListener(this);
        mKeyViewSym0.setOnClickListener(this);

        mKeyViewR2C1.setOnClickListener(this);
        mKeyViewR2C2.setOnClickListener(this);
        mKeyViewR2C3.setOnClickListener(this);
        mKeyViewR2C4.setOnClickListener(this);
        mKeyViewR2C5.setOnClickListener(this);
        mKeyViewR2C6.setOnClickListener(this);
        mKeyViewR2C7.setOnClickListener(this);
        mKeyViewR2C8.setOnClickListener(this);
        mKeyViewR2C9.setOnClickListener(this);
        mKeyViewR2C10.setOnClickListener(this);

        mKeyViewR3C1.setOnClickListener(this);
        mKeyViewR3C2.setOnClickListener(this);
        mKeyViewR3C3.setOnClickListener(this);
        mKeyViewR3C4.setOnClickListener(this);
        mKeyViewR3C5.setOnClickListener(this);
        mKeyViewR3C6.setOnClickListener(this);
        mKeyViewR3C7.setOnClickListener(this);
        mKeyViewR3C8.setOnClickListener(this);
        mKeyViewR3C9.setOnClickListener(this);
        mKeyViewR3C10.setOnClickListener(this);

        mKeyViewR4C1.setOnClickListener(this);
        mKeyViewR4C2.setOnClickListener(this);
        mKeyViewR4C3.setOnClickListener(this);
        mKeyViewR4C4.setOnClickListener(this);
        mKeyViewR4C5.setOnClickListener(this);
        mKeyViewR4C6.setOnClickListener(this);
        mKeyViewR4C7.setOnClickListener(this);

        mKeyViewLettters.setOnClickListener(this);
        mKeyViewSymDel.setOnClickListener(this);

        mKeyViewSymMode.setOnClickListener(this);
        mKeyViewSymFullStop.setOnClickListener(this);
        mKeyViewSymSpace.setOnClickListener(this);
        mKeyViewSymQuestionMark.setOnClickListener(this);
        mKeyViewSymEnter.setOnClickListener(this);
    }

    public void setNumberOptions() {
        //symbol numbers
        mKeyViewSym1.setOptions(new ArrayList<>(Arrays.asList("①", "¹", "₁", "❶", "½", "⅓", "¼", "⅕", "⅙", "⅐", "⅛", "⅑", "⅒", "⅟")));
        mKeyViewSym2.setOptions(new ArrayList<>(Arrays.asList("②", "²", "₂", "❷", "⅔", "⅖")));
        mKeyViewSym3.setOptions(new ArrayList<>(Arrays.asList("③", "³", "₃", "❸", "¾", "⅗", "⅜")));
        mKeyViewSym4.setOptions(new ArrayList<>(Arrays.asList("④", "⁴", "₄", "❹", "⅘")));
        mKeyViewSym5.setOptions(new ArrayList<>(Arrays.asList("⑤", "⁵", "₅", "❺", "⅚", "⅝")));
        mKeyViewSym6.setOptions(new ArrayList<>(Arrays.asList("⑥", "⁶", "₆", "❻")));
        mKeyViewSym7.setOptions(new ArrayList<>(Arrays.asList("⑦", "⁷", "₇", "❼")));
        mKeyViewSym8.setOptions(new ArrayList<>(Arrays.asList("⑧", "⁸", "₈", "❽")));
        mKeyViewSym9.setOptions(new ArrayList<>(Arrays.asList("⑨", "⁹", "₉", "❾")));
        mKeyViewSym0.setOptions(new ArrayList<>(Arrays.asList("⓪", "⁰", "₀", "⓿", "↉")));

    }

    public void setToFirstSetOfSymbols(){
        mKeyViewR2C1.changeLetterTo("+");
        mKeyViewR2C2.changeLetterTo("×");
        mKeyViewR2C3.changeLetterTo("÷");
        mKeyViewR2C4.changeLetterTo("=");
        mKeyViewR2C5.changeLetterTo("/");
        mKeyViewR2C6.changeLetterTo("_");
        mKeyViewR2C7.changeLetterTo("<");
        mKeyViewR2C8.changeLetterTo(">");
        mKeyViewR2C9.changeLetterTo("[");
        mKeyViewR2C10.changeLetterTo("]");

        mKeyViewR3C1.changeLetterTo("!");
        mKeyViewR3C2.changeLetterTo("@");
        mKeyViewR3C3.changeLetterTo("#");
        mKeyViewR3C4.changeLetterTo("₦");
        mKeyViewR3C5.changeLetterTo("%");
        mKeyViewR3C6.changeLetterTo("^");
        mKeyViewR3C7.changeLetterTo("&");
        mKeyViewR3C8.changeLetterTo("*");
        mKeyViewR3C9.changeLetterTo("(");
        mKeyViewR3C10.changeLetterTo(")");

        mKeyViewR4C1.changeLetterTo("-");
        mKeyViewR4C2.changeLetterTo("'");
        mKeyViewR4C3.changeLetterTo("\"");
        mKeyViewR4C4.changeLetterTo(":");
        mKeyViewR4C5.changeLetterTo(";");
        mKeyViewR4C6.changeLetterTo(",");
        mKeyViewR4C7.changeLetterTo("~");
    }

    public void setToSecondSetOfSymbols(){
        mKeyViewR2C1.changeLetterTo("₦");
        mKeyViewR2C2.changeLetterTo("$");
        mKeyViewR2C3.changeLetterTo("¥");
        mKeyViewR2C4.changeLetterTo("£");
        mKeyViewR2C5.changeLetterTo("€");
        mKeyViewR2C6.changeLetterTo("¢");
        mKeyViewR2C7.changeLetterTo("﷼");
        mKeyViewR2C8.changeLetterTo("₮");
        mKeyViewR2C9.changeLetterTo("₺");
        mKeyViewR2C10.changeLetterTo("₩");

        mKeyViewR3C1.changeLetterTo("\\");
        mKeyViewR3C2.changeLetterTo("|");
        mKeyViewR3C3.changeLetterTo("♩");
        mKeyViewR3C4.changeLetterTo("♪");
        mKeyViewR3C5.changeLetterTo("♫");
        mKeyViewR3C6.changeLetterTo("\uD80C\uDFE2");
        mKeyViewR3C7.changeLetterTo("༄");
        mKeyViewR3C8.changeLetterTo("⭒");
        mKeyViewR3C9.changeLetterTo("☽");
        mKeyViewR3C10.changeLetterTo("☪");

        mKeyViewR4C1.changeLetterTo("⁂");
        mKeyViewR4C2.changeLetterTo("†");
        mKeyViewR4C3.changeLetterTo("¡");
        mKeyViewR4C4.changeLetterTo("〝");
        mKeyViewR4C5.changeLetterTo("¿");
        mKeyViewR4C6.changeLetterTo("௹");
        mKeyViewR4C7.changeLetterTo("₴");

    }

    public void setToThirdSetOfSymbols(){
        mKeyViewR2C1.changeLetterTo("▪");
        mKeyViewR2C2.changeLetterTo("¤");
        mKeyViewR2C3.changeLetterTo("《");
        mKeyViewR2C4.changeLetterTo("》");
        mKeyViewR2C5.changeLetterTo("π");
        mKeyViewR2C6.changeLetterTo("〔");
        mKeyViewR2C7.changeLetterTo("〕");
        mKeyViewR2C8.changeLetterTo("❝");
        mKeyViewR2C9.changeLetterTo("❜");
        mKeyViewR2C10.changeLetterTo("∬");

        mKeyViewR3C1.changeLetterTo("√");
        mKeyViewR3C2.changeLetterTo("∑");
        mKeyViewR3C3.changeLetterTo("∪");
        mKeyViewR3C4.changeLetterTo("≤");
        mKeyViewR3C5.changeLetterTo("≦");
        mKeyViewR3C6.changeLetterTo("≧");
        mKeyViewR3C7.changeLetterTo("∮");
        mKeyViewR3C8.changeLetterTo("∞");
        mKeyViewR3C9.changeLetterTo("㏑");
        mKeyViewR3C10.changeLetterTo("μ");


        mKeyViewR4C1.changeLetterTo("꧁");
        mKeyViewR4C2.changeLetterTo("➺");
        mKeyViewR4C3.changeLetterTo("⇥");
        mKeyViewR4C4.changeLetterTo("➭");
        mKeyViewR4C5.changeLetterTo("░");
        mKeyViewR4C6.changeLetterTo("△");
        mKeyViewR4C7.changeLetterTo("⊖");
    }

    public void setToForthSetOfSymbols(){
        mKeyViewR2C1.changeLetterTo("▪");
        mKeyViewR2C2.changeLetterTo("¤");
        mKeyViewR2C3.changeLetterTo("《");
        mKeyViewR2C4.changeLetterTo("》");
        mKeyViewR2C5.changeLetterTo("℃");
        mKeyViewR2C6.changeLetterTo("₼");
        mKeyViewR2C7.changeLetterTo("✰");
        mKeyViewR2C8.changeLetterTo("✹");
        mKeyViewR2C9.changeLetterTo("✤");
        mKeyViewR2C10.changeLetterTo("❁");

        mKeyViewR3C1.changeLetterTo("°");
        mKeyViewR3C2.changeLetterTo("•");
        mKeyViewR3C3.changeLetterTo("○");
        mKeyViewR3C4.changeLetterTo("●");
        mKeyViewR3C5.changeLetterTo("□");
        mKeyViewR3C6.changeLetterTo("■");
        mKeyViewR3C7.changeLetterTo("♤");
        mKeyViewR3C8.changeLetterTo("∫");
        mKeyViewR3C9.changeLetterTo("◇");
        mKeyViewR3C10.changeLetterTo("♧");


        mKeyViewR4C1.changeLetterTo("♡");
        mKeyViewR4C2.changeLetterTo("♥");
        mKeyViewR4C3.changeLetterTo("❣");
        mKeyViewR4C4.changeLetterTo("ღ");
        mKeyViewR4C5.changeLetterTo("❥");
        mKeyViewR4C6.changeLetterTo("❦");
        mKeyViewR4C7.changeLetterTo("დ");
    }


    @Override
    public void onClick(KeyView key) {
        String input = key.toString();
        mOnKeyClicked.keyClicked(key, input, key.showInEditable);
//
//        clearCopiedWord();
//        if(!shouldAutoCorrect){
//            return;
//        }
//
//        wordClosestToCursor.setLength(0);
////        MyInputMethod.getInstance
//        com.olalekan.nijakeyboard.MyInputMethod myInputMethod = com.olalekan.nijakeyboard.MyInputMethod.getInstance();
//        InputConnection inputConnection = myInputMethod.getCurrentInputConnection();
//        String wordBeforeCursor = myInputMethod.getWordBeforeCursor(inputConnection);
//        String wordAfterCursor = myInputMethod.getWordAfterCursor(inputConnection);
//
//        if(myInputMethod.wordBeforeAndAfterCursorLengthIsNOTOk(wordBeforeCursor, wordAfterCursor)){
//            //if the length of the word before and after cursor is bigger than
//            // Constants.MAXIMUM_NUMBER_OF_LETTERS_TO_CHECK
//            // then just pass the key and give the user the default suggestions
//            mOnKeyClicked.keyClicked(key, input, key.showInEditable());
//            changeToDefaultSuggestions();
//            clearCorrectionTextViewAndCorrectionString();
//            clearSwitchedWord();
//            return;
//        }
//
//
//
//        /*
//         * Append the words before and after the cursor with the new word in between
//         * If the key can be shown in editable and it is not a usual symbol
//         *
//         * else don't append it*/
//        if(key.showInEditable && !isUsualSymbol(input))
//            wordClosestToCursor.append(wordBeforeCursor).append(input).append(wordAfterCursor);
//        else wordClosestToCursor.append(wordBeforeCursor).append(wordAfterCursor);
//
//
//
//        /*
//         * send the key that was clicked to MyInputMethod
//         * if the space, or usual symbol is not pressed.
//         *
//         * Space handles its own sending because it might input corrections
//         * */
//        if(!input.equals("Space") && !isUsualSymbol(input))mOnKeyClicked.keyClicked(key, input, key.showInEditable());
//
////        if(input.equals("Space") || input.equals("Del")){
////        }
//
////        mExecutor.execute(getCorrectionRunnable);
//        mSuggestionCandidates = new ArrayList<>();
//
//        if(input.equals("Del")){
//            wordBeforeCursor = myInputMethod.getWordBeforeCursor(inputConnection);
//            wordAfterCursor = myInputMethod.getWordAfterCursor(inputConnection);
//
//            //THIS  handles null word closest to cursor
//            if (myInputMethod.wordBeforeAndAfterCursorLengthIsNOTOk(wordBeforeCursor, wordAfterCursor)) {
//                changeToDefaultSuggestions();
//                clearCorrectionTextViewAndCorrectionString();
//                clearSwitchedWord();
//                wordClosestToCursor.setLength(0);
//                return;
//            }
//
//            wordClosestToCursor.setLength(0);
//            wordClosestToCursor.append(wordBeforeCursor).append(wordAfterCursor);
//
//            /*
//             * if a word was switched before
//             * and the user is still on the word switch it back
//             * This is to make sure that the word is only switched
//             * when the cursor is near the word that was actually corrected
//             * to prevent correcting when the user has already left the word
//             * like when a highlighted text is required to be deleted
//             * or you moved to a new edittext
//             * */
//            if (!switchedWord.isEmpty() && wordClosestToCursor.toString().toLowerCase(Locale.ROOT).equals(previousCorrection)) {
//                mOnKeyClicked.SuggestionWasClicked(switchedWord, false);
//                switchedWordToBeAddedToSuggestions = switchedWord;
//                mExecutor.execute(addToSuggestionsRunnable);
//                clearCorrectionTextViewAndCorrectionString();
//                clearSwitchedWord();
//                return;
//            }
//
//            //If the word closest to the cursor is less than 1 or just 1 letter
//            //we don't have to look for suggestions or correction
//            if (wordClosestToCursor.length() <= 1) {
//                changeToDefaultSuggestions();
//                wordClosestToCursor.setLength(0);
//                clearCorrectionTextViewAndCorrectionString();
//                clearSwitchedWord();
//                return;
//            }
//
//
//            //what to do usually
//
//
////                    mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
////                    suggestionsThread.start();
//
//            mExecutor.execute(getSuggestionsRunnable);
//            mExecutor.execute(getCorrectionRunnable);
////                    if(suggestionData.isSuggestionsDoneLoading())
////                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));
//
//
//            //if the get suggestion runnable has not changed the suggestion
//            if (mSuggestionCandidates.isEmpty())
//                mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));
//
//            return;
//        }
//
//        if(input.equals("Space") || isUsualSymbol(input)){
//            /*
//             * if there is a correction down:
//             *   set switched word to the word closest to the cursor
//             *   then switch the word to the correction with space in front of it
//             * */
//
//            if(!correction.isEmpty()){
//                //if the user inputs the correct word by himself no need to correct him
//                if(correction.equals(wordClosestToCursor.toString().toLowerCase(Locale.ROOT))){
//                    mOnKeyClicked.keyClicked(key, input, key.showInEditable());
//                    changeToDefaultSuggestions();
//                    clearCorrectionTextViewAndCorrectionString();
//                    return;
////                    break;
//                }
//
//                switchedWord = wordClosestToCursor.toString();
//                /*//if the first character of the switched word is capital
//                //change the corrections first letter to capital
//                if(Character.isUpperCase(switchedWord.charAt(0))){
//                    correction = Constants.capitaliseFirstLetterOf(correction);
//                }*/
//
//                previousCorrection = correction;
//
//
//                mOnKeyClicked.SuggestionWasClicked(correction, false);
////                mOnKeyClicked.keyClicked(key, input, key.showInEditable());
//
//            } else {
//                //send the word to the database
//                wordToBeAddedToSuggestions = wordClosestToCursor.toString();
//                mExecutor.execute(addToSuggestionsIfNotYetExcessiveInDatabaseRunnable);
//            }
//
//            mOnKeyClicked.keyClicked(key, input, key.showInEditable());
//
//            changeToDefaultSuggestions();
//            clearCorrectionTextViewAndCorrectionString();
//
////                mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
//
//            wordClosestToCursor.setLength(0);
////            break;
//            return;
//        }
//
//        if (key.showInEditable && isLetter(input)) {
//            /*
//             * If you input a normal letter cleat the suggestion and correction
//             * get new suggestion and correction
//             * */
////                    word.append(wordBeforeCursor);
////                    word.append(input);
////                    word.append(wordAfterCursor);
//
////                    suggestionsThread.start();
//
//            clearCorrectionTextViewAndCorrectionString();
//            clearSwitchedWord();
//            mExecutor.execute(getSuggestionsRunnable);
//            mExecutor.execute(getCorrectionRunnable);
//
////                    if(suggestionData.isSuggestionsDoneLoading())
////                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));
//
//
//            if (mSuggestionCandidates.isEmpty())
//                mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));
////                    else mSuggestionRecyclerAdapter.changeSuggestions(mSuggestionCandidates);
////                    mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(word.toString().toLowerCase(Locale.ROOT))));
//            return;
//
//        } else {
//
//            //if all the conditions above are not met do this
//            //NOTE: MAKE SURE ALL THE OTHER CONDITIONS HAVE RETURN IN THEM TO AVOID ANY UNFORESEEN ERROR
//            clearCorrectionTextViewAndCorrectionString();
//            clearSwitchedWord();
//            changeToDefaultSuggestions();
//        }
//
//
////        switch (input){
////            case "Del":
////                wordBeforeCursor = myInputMethod.getWordBeforeCursor(inputConnection);
////                wordAfterCursor = myInputMethod.getWordAfterCursor(inputConnection);
////
////                //THIS  handles null word closest to cursor
////                if (myInputMethod.wordBeforeAndAfterCursorLengthIsNOTOk(wordBeforeCursor, wordAfterCursor)) {
////                    changeToDefaultSuggestions();
////                    clearCorrectionTextViewAndCorrectionString();
////                    clearSwitchedWord();
////                    wordClosestToCursor.setLength(0);
////                    break;
////                }
////
////                wordClosestToCursor.setLength(0);
////                wordClosestToCursor.append(wordBeforeCursor).append(wordAfterCursor);
////
////                /*
////                 * if a word was switched before
////                 * and the user is still on the word switch it back
////                 * This is to make sure that the word is only switched
////                 * when the cursor is near the word that was actually corrected
////                 * to prevent correcting when the user has already left the word
////                 * like when a highlighted text is required to be deleted
////                 * or you moved to a new edittext
////                 * */
////                if (!switchedWord.isEmpty() && wordClosestToCursor.toString().equals(previousCorrection)) {
////                    mOnKeyClicked.SuggestionWasClicked(switchedWord, false);
////                    switchedWordToBeAddedToSuggestions = switchedWord;
////                    mExecutor.execute(addToSuggestionsRunnable);
////                    clearCorrectionTextViewAndCorrectionString();
////                    clearSwitchedWord();
////                    break;
////                }
////
////                //If the word closest to the cursor is less than 1 or just 1 letter
////                //we don't have to look for suggestions or correction
////                if (wordClosestToCursor.length() <= 1) {
////                    changeToDefaultSuggestions();
////                    wordClosestToCursor.setLength(0);
////                    clearCorrectionTextViewAndCorrectionString();
////                    clearSwitchedWord();
////                    break;
////                }
////
////
////                //what to do usually
////
////
//////                    mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
//////                    suggestionsThread.start();
////
////                mExecutor.execute(getSuggestionsRunnable);
////                mExecutor.execute(getCorrectionRunnable);
//////                    if(suggestionData.isSuggestionsDoneLoading())
//////                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));
////
////
////                //if the get suggestion runnable has not changed the suggestion
////                if (mSuggestionCandidates.isEmpty())
////                    mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));
////
////                break;
//////            case "Space":
//////                /*
//////                 * if there is a correction down:
//////                 *   set switched word to the word closest to the cursor
//////                 *   then switch the word to the correction with space in front of it
//////                 * */
//////
//////                if(!correction.isEmpty()){
//////                    //if the user inputs the correct word by himself no need to correct him
//////                    if(correction.equals(wordClosestToCursor.toString().toLowerCase(Locale.ROOT))){
//////                        mOnKeyClicked.keyClicked(key, input, key.showInEditable());
//////                        changeToDefaultSuggestions();
//////                        clearCorrectionTextViewAndCorrectionString();
//////                        break;
//////                    }
//////
//////                    switchedWord = wordClosestToCursor.toString();
//////                    //if the first character of the switched word is capital
//////                    //change the corrections first letter to capital
//////                    if(Character.isUpperCase(switchedWord.charAt(0))){
//////                        correction = Constants.capitaliseFirstLetterOf(correction);
//////                    }
//////
//////                    previousCorrection = correction;
//////
//////                    mOnKeyClicked.SuggestionWasClicked(correction, false);
////////                mOnKeyClicked.keyClicked(key, input, key.showInEditable());
//////
//////                }
//////
//////                mOnKeyClicked.keyClicked(key, input, key.showInEditable());
//////
//////                changeToDefaultSuggestions();
//////                clearCorrectionTextViewAndCorrectionString();
//////
////////                mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
//////
//////                wordClosestToCursor.setLength(0);
//////                break;
////
////            default:
////                if(input.equals("Space") || isUsualSymbol(input)){
////                    /*
////                     * if there is a correction down:
////                     *   set switched word to the word closest to the cursor
////                     *   then switch the word to the correction with space in front of it
////                     * */
////
////                    if(!correction.isEmpty()){
////                        //if the user inputs the correct word by himself no need to correct him
////                        if(correction.equals(wordClosestToCursor.toString().toLowerCase(Locale.ROOT))){
////                            mOnKeyClicked.keyClicked(key, input, key.showInEditable());
////                            changeToDefaultSuggestions();
////                            clearCorrectionTextViewAndCorrectionString();
////                            return;
//////                    break;
////                        }
////
////                        switchedWord = wordClosestToCursor.toString();
////                        //if the first character of the switched word is capital
////                        //change the corrections first letter to capital
////                        if(Character.isUpperCase(switchedWord.charAt(0))){
////                            correction = Constants.capitaliseFirstLetterOf(correction);
////                        }
////
////                        previousCorrection = correction;
////
////
////                        mOnKeyClicked.SuggestionWasClicked(correction, false);
//////                mOnKeyClicked.keyClicked(key, input, key.showInEditable());
////
////                    }
////
////                    mOnKeyClicked.keyClicked(key, input, key.showInEditable());
////
////                    changeToDefaultSuggestions();
////                    clearCorrectionTextViewAndCorrectionString();
////
//////                mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
////
////                    wordClosestToCursor.setLength(0);
//////            break;
////                    break;
////                }
////                if (key.showInEditable && isLetter(input)) {
////                    /*
////                     * If you input a normal letter cleat the suggestion and correction
////                     * get new suggestion and correction
////                     * */
//////                    word.append(wordBeforeCursor);
//////                    word.append(input);
//////                    word.append(wordAfterCursor);
////
//////                    suggestionsThread.start();
////
////                    clearCorrectionTextViewAndCorrectionString();
////                    clearSwitchedWord();
////                    mExecutor.execute(getSuggestionsRunnable);
////                    mExecutor.execute(getCorrectionRunnable);
////
//////                    if(suggestionData.isSuggestionsDoneLoading())
//////                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));
////
////
////                    if (mSuggestionCandidates.isEmpty())
////                        mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));
//////                    else mSuggestionRecyclerAdapter.changeSuggestions(mSuggestionCandidates);
//////                    mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(word.toString().toLowerCase(Locale.ROOT))));
////                    break;
////
////                } else {
////                    clearCorrectionTextViewAndCorrectionString();
////                    clearSwitchedWord();
////                    changeToDefaultSuggestions();
////                }
////
////
////        }
//
//
////        HashMap<String, Integer> suggestionHashMap;
////        if (suggestionData.isSuggestionsDoneLoading()){
////            suggestionHashMap = suggestionData.getSuggestionsHashMap();
////        } else suggestionHashMap = new HashMap<>();
////
////        WordPredictor.correction(word.toString(), suggestionData.getSuggestionsHashMap());
//
//
////        switch (input){
////            case "Del":
////                if(wordClosestToCursor.length() > 1){
//////                    word.deleteCharAt(word.length()-1);
////                    wordBeforeCursor = myInputMethod.getWordBeforeCursor(inputConnection);
////                    wordAfterCursor = myInputMethod.getWordAfterCursor(inputConnection);
////
////                    if(myInputMethod.wordBeforeAndAfterCursorLengthIsNOTOk(wordBeforeCursor, wordAfterCursor)){
////                        changeToDefaultSuggestions();
////                        wordClosestToCursor.setLength(0);
////                        break;
////                    }
////
////                    wordClosestToCursor.setLength(0);
////                    wordClosestToCursor.append(wordBeforeCursor).append(wordAfterCursor);
//////                    mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
//////                    suggestionsThread.start();
////
////                    mExecutor.execute(getSuggestionsRunnable);
//////                    if(suggestionData.isSuggestionsDoneLoading())
//////                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));
////
////
////                    if(mSuggestionCandidates.isEmpty()) mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));
//////                    else mSuggestionRecyclerAdapter.changeSuggestions(mSuggestionCandidates);
////                } else {
////                    changeToDefaultSuggestions();
////                    wordClosestToCursor.setLength(0);
////                }
////
////                break;
////            case "Space":
////                changeToDefaultSuggestions();
//////                mSuggestion = WordPredictor.correction(word.toString().toLowerCase(Locale.ROOT), suggestionHashMap);
////
////                wordClosestToCursor.setLength(0);
////                break;
////            default:
////                if (key.showInEditable && isLetter(input)) {
//////                    word.append(wordBeforeCursor);
//////                    word.append(input);
//////                    word.append(wordAfterCursor);
////
//////                    suggestionsThread.start();
////
////                    mExecutor.execute(getSuggestionsRunnable);
//////                    if(suggestionData.isSuggestionsDoneLoading())
//////                        mSuggestionCandidates = new ArrayList<>(WordPredictor.candidates(wordClosestToSursor.toString().toLowerCase(Locale.ROOT), suggestionHashMap));
////
////
////                    if(mSuggestionCandidates.isEmpty()) mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(wordClosestToCursor.toString())));
//////                    else mSuggestionRecyclerAdapter.changeSuggestions(mSuggestionCandidates);
//////                    mSuggestionRecyclerAdapter.changeSuggestions(new ArrayList<String>(Collections.singletonList(word.toString().toLowerCase(Locale.ROOT))));
////
////                }
////        }
//
//
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
}
