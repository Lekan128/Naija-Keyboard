package com.olalekan.naijakeyboard;

import static com.olalekan.naijakeyboard.MyKeyboardView.mCopiedWord;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.makeViewVisible;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.makeVisibilityToGone;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorDown;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorToTheLeft;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorToTheRight;
import static com.olalekan.naijakeyboard.defaults.AllRoundUseful.moveCursorUp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.olalekan.naijakeyboard.KeyView;
import com.olalekan.naijakeyboard.MyInputMethod;
import com.olalekan.naijakeyboard.MyKeyboardView;
import com.olalekan.naijakeyboard.R;
import com.olalekan.naijakeyboard.defaults.Constants;

public class NumberKeybardView extends LinearLayout implements KeyView.KeyWasClicked {

    /*OnKwyClicked is used to send the key clicked to MyInputMethod*/
    private MyKeyboardView.OnKeyClicked mOnKeyClicked = MyInputMethod.getInstance();


    KeyView mKeyViewNum1, mKeyViewNum2, mKeyViewNum3, mKeyViewNum4, mKeyViewNum5, mKeyViewNum6, mKeyViewNum7, mKeyViewNum8, mKeyViewNum9,  mKeyViewNum0,
            mKeyViewNumDel, mKeyViewNumGo, mKeyViewNumComma, mKeyViewNumSymbols, mKeyViewNumStar, mKeyViewNumHash,
            mKeyViewNumFullStop, mKeyViewNumSpace, mKeyViewNumPlus;
    View viewNumberKeyboardTextCopied;
    TextView textViewNumberKeyboardCopied;
    /*
     * When a word is copied viewNumberKeyboardTextCopied will be visible
     * And the textViewNumberKeyboardCopied will contain the copied word
     * (there are conditions to showing the copied word check clipboardManager instant)
     *
     * If the mCopiedWord is cleared
     * viewNumberKeyboardTextCopied is set to gone and
     * textViewNumberKeyboardCopied text is set to ""
     * ie removeCopiedWordAndViewFromNumberKeyboard()
     *
     * If the number keyboard is shown and there is no suggestion
     * viewNumberKeyboardTextCopied is set to gone and
     * textViewNumberKeyboardCopied text is set to ""
     *
     * when textViewNumberKeyboardCopied is clicked paste the letters and clear corrections
     * */


    public NumberKeybardView(Context context) {
        super(context);
        initializeControls(context);

    }

    public NumberKeybardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeControls(context);

    }

    public NumberKeybardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeControls(context);

    }

    private void initializeControls(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.number_keyboard_view, this);

        initialiseNumberKeyboard();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getContext().getString(R.string.input_method_preference), Context.MODE_PRIVATE);
        float keyOpacity = sharedPreferences.getFloat(getContext().getString(R.string.key_opacity_preference), Constants.DEFAULT_KEYBOARD_OPACITY_ALPHA);
        viewNumberKeyboardTextCopied.setAlpha(keyOpacity);
        viewNumberKeyboardTextCopied.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mCopiedWord.isEmpty()){
                    //if a word has been copied
                    /*
                     * When the copied word is > 10 not all the words are shown
                     * this is to make sure the actual copied word is shown
                     * */
                    mOnKeyClicked.SuggestionWasClicked(mCopiedWord, false);
                    MyInputMethod.getInstance().clearCopiedWord();
                }
            }
        });

        setNumberKeyboardOnClickListener();


    }

    public void showCopiedString(){
        makeViewVisible(viewNumberKeyboardTextCopied);
        textViewNumberKeyboardCopied.setText(mCopiedWord);

    }

    private void initialiseNumberKeyboard() {
        mKeyViewNum1 = findViewById(R.id.numKeyView1);
        mKeyViewNum2 = findViewById(R.id.numKeyView2);
        mKeyViewNum3 = findViewById(R.id.numKeyView3);
        mKeyViewNum4 = findViewById(R.id.numKeyView4);
        mKeyViewNum5 = findViewById(R.id.numKeyView5);
        mKeyViewNum6 = findViewById(R.id.numKeyView6);
        mKeyViewNum7 = findViewById(R.id.numKeyView7);
        mKeyViewNum8 = findViewById(R.id.numKeyView8);
        mKeyViewNum9 = findViewById(R.id.numKeyView9);
        mKeyViewNum0 = findViewById(R.id.numKeyView0);

        mKeyViewNumDel = findViewById(R.id.numKeyViewDel);
        mKeyViewNumGo = findViewById(R.id.numKeyViewGo);
        mKeyViewNumComma = findViewById(R.id.numKeyViewComma);
        mKeyViewNumSymbols = findViewById(R.id.numKeyViewSymbols);
        mKeyViewNumStar = findViewById(R.id.numKeyViewStar);
        mKeyViewNumHash = findViewById(R.id.numKeyViewHash);

        mKeyViewNumFullStop = findViewById(R.id.numKeyViewStop);
        mKeyViewNumSpace = findViewById(R.id.numKeyViewSpace);
        mKeyViewNumPlus = findViewById(R.id.numKeyViewPlus);

        viewNumberKeyboardTextCopied = findViewById(R.id.number_keyboard_text_copied);
        textViewNumberKeyboardCopied = findViewById(R.id.textViewSuggestion);

    }

    public void setToNumbersKeyboard(){
        if(mCopiedWord.isEmpty()) removeCopiedWordAndViewFromNumberKeyboard();
//        makeKeyboardVisible(numberKeyboard);
        setNumberKeyboardToNumbers();
    }

    public void removeCopiedWordAndViewFromNumberKeyboard(){
        makeVisibilityToGone(viewNumberKeyboardTextCopied);
        if(!textViewNumberKeyboardCopied.getText().toString().isEmpty()){
            //if the text in the number keyboard suggestion is now empty
            textViewNumberKeyboardCopied.setText("");
        }
    }

    private void setNumberKeyboardOnClickListener() {
        mKeyViewNum1.setOnClickListener(this);
        mKeyViewNum2.setOnClickListener(this);
        mKeyViewNum3.setOnClickListener(this);
        mKeyViewNum4.setOnClickListener(this);
        mKeyViewNum5.setOnClickListener(this);
        mKeyViewNum6.setOnClickListener(this);
        mKeyViewNum7.setOnClickListener(this);
        mKeyViewNum8.setOnClickListener(this);
        mKeyViewNum9.setOnClickListener(this);
        mKeyViewNum0.setOnClickListener(this);

        mKeyViewNumDel.setOnClickListener(this);
        mKeyViewNumGo.setOnClickListener(this);
        mKeyViewNumComma.setOnClickListener(this);
        mKeyViewNumSymbols.setOnClickListener(this);
        mKeyViewNumStar.setOnClickListener(this);
        mKeyViewNumHash.setOnClickListener(this);

        mKeyViewNumFullStop.setOnClickListener(this);
        mKeyViewNumSpace.setOnClickListener(this);
        mKeyViewNumPlus.setOnClickListener(this);
    }

    public void setNumberKeyboardToNumbers() {
        mKeyViewNum1.changeLetterTo("1");
        mKeyViewNum2.changeLetterTo("2");
        mKeyViewNum3.changeLetterTo("3");
        mKeyViewNum4.changeLetterTo("4");
        mKeyViewNum5.changeLetterTo("5");
        mKeyViewNum6.changeLetterTo("6");
        mKeyViewNum7.changeLetterTo("7");
        mKeyViewNum8.changeLetterTo("8");
        mKeyViewNum9.changeLetterTo("9");
        mKeyViewNum0.changeLetterTo("0");

        mKeyViewNumSymbols.changeLetterTo("*#N");
    }

    public void setNumberKeyboardToSymbols() {
        mKeyViewNum1.changeLetterTo("(");
        mKeyViewNum2.changeLetterTo(")");
        mKeyViewNum3.changeLetterTo("/");
        mKeyViewNum4.changeLetterTo("N");
        mKeyViewNum5.changeLetterTo(";");
        mKeyViewNum6.changeLetterTo("|");
        mKeyViewNum7.changeLetterTo("-");
        mKeyViewNum8.changeLetterTo("_");
        mKeyViewNum9.changeLetterTo("[");
        mKeyViewNum0.changeLetterTo("]");

        mKeyViewNumSymbols.changeLetterTo("123");
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
