package com.olalekan.naijakeyboard;

import static com.olalekan.naijakeyboard.defaults.Constants.DEFAULT_COLOUR;
import static com.olalekan.naijakeyboard.defaults.Constants.DEFAULT_SUGGESTION_SET;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.olalekan.naijakeyboard.defaults.Constants;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* This is the class the receives the click method from the MyKeyboardView
* That is why it implements the MyKeyboardView.OnClick
* When ever a key is pressed
*
* It also handles capital letters and capslock functionality*/

public class MyInputMethod extends InputMethodService implements MyKeyboardView.OnKeyClicked, com.olalekan.naijakeyboard.EmojiRecyclerAdapter.EmojiWasClicked {

    public static final int NEW_CURSOR_POSITION = 1;
    private MyKeyboardView mMyKeyboardView;
//    private SymbolsKeyboard mSymbolsInputView;

    boolean capitalLetters = true;
    boolean capsLock = false;
    private StringBuilder word;
    private static MyInputMethod mMyInputMethod;
    private ExecutorService mExecutorService;

    SharedPreferences mSharedPreferences ;


    public static MyInputMethod getInstance(){
        return mMyInputMethod;
    }

    @Override
    public View onCreateInputView() {
        //The executor creates thread(s)
        mExecutorService = Executors.newFixedThreadPool(2);
        mMyInputMethod = this;
        mMyKeyboardView = (MyKeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        //Important to give the executor to the keyboard
        mMyKeyboardView.setExecutor(mExecutorService);
//        mSymbolsInputView = (SymbolsKeyboard) getLayoutInflater().inflate(R.layout.symbols_keyboard, null);
        /*
        * second_keyboard_view point to the layout of the keyboard view I made: MYKeyboardView
        * The Layout of the keyboardView is populated in the MyKeyboardView class with the
        * my_keyboard_view layout
        * */

//        SpannableString
//        checkToMakeCapital(getCurrentInputConnection());

//        mLetterInputView.setOnClickListener(this);
//        inputView.setKeyboard

        mSharedPreferences = getApplicationContext()
                .getSharedPreferences(getApplicationContext()
                                .getString(R.string.input_method_preference),
                        Context.MODE_PRIVATE);

        return mMyKeyboardView;
    }

//    @Override
//    public Resources.Theme getTheme() {
//        Resources.Theme theme = super.getTheme();
//        theme.applyStyle(R.style.ATheme, false);
//        return super.getTheme();
//    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        mMyKeyboardView.setKeyboardColour(
                mSharedPreferences.getInt(getString(R.string.colour_preference), DEFAULT_COLOUR)
        );

        int inputType = info.inputType & InputType.TYPE_MASK_CLASS;
        int inputVariation = info.inputType & InputType.TYPE_MASK_VARIATION;
        int flag = info.inputType & InputType.TYPE_MASK_FLAGS;


        int editorActionButton = info.imeOptions & (EditorInfo.IME_MASK_ACTION | EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        switch (editorActionButton){
            case EditorInfo.IME_ACTION_GO:
                mMyKeyboardView.changeKeyboardActionToGo();
                break;

            case EditorInfo.IME_ACTION_NEXT:
                mMyKeyboardView.changeKeyboardActionToNext();
                break;

            case EditorInfo.IME_ACTION_SEARCH:
                mMyKeyboardView.changeKeyboardActionToSearch();
                break;

            case EditorInfo.IME_ACTION_DONE:
                mMyKeyboardView.changeKeyboardActionToDone();
                break;

            case EditorInfo.IME_ACTION_SEND:
                mMyKeyboardView.changeKeyboardActionToSend();
                break;

            default:
                mMyKeyboardView.changeKeyboardActionEnter();
                break;

        }

        switch (inputType){
            case InputType.TYPE_CLASS_TEXT:
                Log.i("InputType", "onStartInputView: Text");
                mMyKeyboardView.setToLettersKeyboard();
                resetCapslock();
                checkToMakeLettersCapital(getCurrentInputConnection());

                Set<String> mSuggestionsSet = mSharedPreferences
                        .getStringSet(getApplicationContext()
                                .getString(R.string.saved_input_preference),
                        DEFAULT_SUGGESTION_SET);

                mMyKeyboardView.setDefaultSuggestions(mSuggestionsSet);

//                if(flag == InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE ||
//                        flag == InputType.TYPE_TEXT_FLAG_MULTI_LINE){
//                    break;
//                }
//                if(flag == InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE
//                        || flag == InputType.TYPE_TEXT_FLAG_AUTO_CORRECT){
//                    mLetterInputView.turnOnAutoCorrect();
//                }

                switch (inputVariation){
                    case InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD:
                    case InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD:
                    case InputType.TYPE_TEXT_VARIATION_PASSWORD:
                        mMyKeyboardView.turnOffAutoCorrect();
                        break;

                    case InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS:
                    case InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS:
                        mMyKeyboardView.setToLetterEmail();
                        break;

                    case InputType.TYPE_TEXT_VARIATION_URI:
                        mMyKeyboardView.setToLetterUri();
                        break;

//                    case InputType.TYPE_TEXT_VARIATION_NORMAL:
//                    case InputType.TYPE_TEXT_VARIATION_FILTER:
//                    case InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT:
//                        mLetterInputView.setToLetterFilter();
//                        break;

                }
                break;



            case InputType.TYPE_CLASS_NUMBER:
                mMyKeyboardView.setToNumbersKeyboard();
                Log.i("InputType", "onStartInputView: number");
                break;

            case InputType.TYPE_CLASS_DATETIME:
            case InputType.TYPE_CLASS_PHONE:
            case InputType.TYPE_NUMBER_VARIATION_PASSWORD:
                mMyKeyboardView.setToNumbersKeyboard();
                break;

            default:
                mMyKeyboardView.setToLettersKeyboard();
                Log.i("InputType", "onStartInputView: default");


        }

    }

    /*
    * A method that will collect the type of keyboard
    * Set the type of keyboard in the onCreate input view
    *
    * Change keyboard type will receive the keyboard type and send it to onCreateInput view
    * Each type has a constant
    * */

    @Override
    public View onCreateCandidatesView() {
        setCandidatesViewShown(true);
//        Todo: you can use this for the suggestions
//        return (ConstraintLayout) getLayoutInflater().inflate(R.layout.hint_of_text, null);
        return super.onCreateCandidatesView();
    }

    @Override
    public void keyClicked(KeyView keyView, String letter, boolean sendToEditable) {

//        keyView.showPreview();
//
//        hidePreViewAfterDelayMilliSeconds(keyView, 500);
        InputConnection inputConnection = getCurrentInputConnection();


        switch (letter){
            case "Del":
                CharSequence selectedText = inputConnection.getSelectedText(InputConnection.GET_TEXT_WITH_STYLES);
                if (selectedText == null || selectedText.toString().isEmpty()){
                    inputConnection.deleteSurroundingText(1,0);
                } else{
                    inputConnection.commitText("", NEW_CURSOR_POSITION); //1==new cursor position
                }

                checkToMakeLettersCapital(inputConnection);

                break;
            case "Space":
                inputConnection.commitText(" ", NEW_CURSOR_POSITION); //1==new cursor position
                break;
            case "#sym":
//                mLetterInputView.initialiseSymbols(getApplicationContext());
                mMyKeyboardView.setToSymbolsKeyboard();

                break;
            case "Abc":
                mMyKeyboardView.setToLettersKeyboard();
                break;
            case "1/4":
                mMyKeyboardView.setToSecondSetOfSymbols();
                keyView.changeLetterTo("2/4");
                break;
            case "2/4":
                mMyKeyboardView.setToThirdSetOfSymbols();
                keyView.changeLetterTo("3/4");
                break;
            case "3/4":
                mMyKeyboardView.setToForthSetOfSymbols();
                keyView.changeLetterTo("4/4");
                break;
            case "4/4":
                mMyKeyboardView.setToFirstSetOfSymbols();
                keyView.changeLetterTo("1/4");
                break;
            case "123":
                mMyKeyboardView.setNumberKeyboardToNumbers();
                break;
            case "*#N":
                mMyKeyboardView.setNumberKeyboardToSymbols();
                break;

            case "caps":
                if(capitalLetters) {//if it is in capital letters
                    if (capsLock){
                        //if it is in capslock turn off capslock and change to small letter
                        capsLock = false;
                        changeToSmallLetters();
                        mMyKeyboardView.changeCapslockToBlack();
//                        DrawableCompat.setTint(keyView.keyImage.getDrawable(), Color.BLACK);
                    }else {
                        //if it is not in capslock leave it in capital letters and caplock = true
                        capsLock = true;
                        mMyKeyboardView.changeCapslockToBlue();
//                        DrawableCompat.setTint(keyView.keyImage.getDrawable(), Color.BLUE);
                    }
                }else  {
                    changeToCapitalLetters();
                }
                break;
            case "Ent":
//                inputConnection.performEditorAction(EditorInfo.IME_ACTION_GO);
//                inputConnection.endBatchEdit();
                inputConnection.commitText("\n", NEW_CURSOR_POSITION);
                if(!capsLock) changeToCapitalLetters();
                break;

            case "Go":
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_GO);
                break;
            case "Next":
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_NEXT);
                break;
            case "Search":
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_SEARCH);
                break;
            case "Done":
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_DONE);
                break;
            case "Send":
                inputConnection.performEditorAction(EditorInfo.IME_ACTION_SEND);
                break;

            case "Mic":
                //Show select input method pannel to enable user choose an input method
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                inputManager.showInputMethodPicker();
                break;

            default:
                if (sendToEditable) {
                    inputConnection.commitText(letter, NEW_CURSOR_POSITION);
                    checkToMakeLettersCapital(inputConnection);
                }
                else throw new IllegalStateException("Unexpected value: " + letter);
                break;
        }
    }

    private void resetCapslock(){
        mMyKeyboardView.changeCapslockToBlack();
        capitalLetters = true;
        capsLock = false;
    }

    /*
    * If there is a letter before the input: make the keyboard small letters
    * Else
    * Make the keyboard capital letters
    * */
    private void checkToMakeLettersCapital(InputConnection inputConnection) {
        if (capsLock) return;
        int numberOfTextBeforeCursor = 1;
        CharSequence charSequence = inputConnection.getTextBeforeCursor(numberOfTextBeforeCursor, InputConnection.GET_TEXT_WITH_STYLES);
        if (charSequence == null || charSequence.toString().isEmpty()) {
            changeToCapitalLetters();
        } else {
            changeToSmallLetters();
        }
    }

    @Override
    public void SuggestionWasClicked(String suggestion, boolean addSpace) {
//        Constants.vibrateIfRequired(getApplicationContext());
//        ((Vibrator)getSystemService(Context.VIBRATOR_SERVICE)).vibrate(VIBRATION_TIME);
        InputConnection inputConnection = getCurrentInputConnection();

        String wordBeforeCursor = getWordBeforeCursor(inputConnection);
        String wordAfterCursor = getWordAfterCursor(inputConnection);
        if(wordBeforeAndAfterCursorLengthIsNOTOk(wordBeforeCursor, wordAfterCursor)){
            //if the word is bigger then Constants.MAXIMUM_NUMBER_OF_LETTERS_TO_CHECK
            //Don't delete what was pressed
            String suggestionWithSpaceBeforeIt = " " + suggestion;
            inputConnection.commitText(suggestionWithSpaceBeforeIt, 1);
            return;
        }

        int beforeLength = wordBeforeCursor.length();
        int afterLength = wordAfterCursor.length();

        if(beforeLength>0){//word before the cursor is not empty, so null wount be returned
            //if the first character of the word be cleared starts with a capital letter
            //change the suggestion first letter to capital
            if(Character.isUpperCase(wordBeforeCursor.charAt(0))){
                suggestion = Constants.capitaliseFirstLetterOf(suggestion);
            }

        }

//        CharSequence selectedText = inputConnection.getSelectedText(InputConnection.GET_TEXT_WITH_STYLES);

        inputConnection.deleteSurroundingText(beforeLength, afterLength);
//        suggestion += " ";
        if(addSpace){
            String suggestionWithSpaceAfterIt = suggestion + " " ;
            inputConnection.commitText(suggestionWithSpaceAfterIt, 1); //1==new cursor position
        }else {
            inputConnection.commitText(suggestion, 1); //1==new cursor position

        }



//        inputConnection.commitCorrection(new CorrectionInfo(2, ))

//        if (selectedText == null || selectedText.toString().isEmpty()){
//            inputConnection.deleteSurroundingText(1,0);
//        } else{
//            inputConnection.commitText("", 1); //1==new cursor position
//        }
    }

    public String getWordBeforeCursor(InputConnection inputConnection){
        int i = 1;
        CharSequence word = "";
        //test is to check if the word contains letters before making it the word that will be sent to the user
        CharSequence test =  getCurrentInputConnection().getTextBeforeCursor(i, InputConnection.GET_TEXT_WITH_STYLES);
        test = test==null ? "" : test;

//        do {
//            test =  inputConnection.getTextBeforeCursor(i++, InputConnection.GET_TEXT_WITH_STYLES);
//        }while (containLetters(test) && test.length()==i-1);


        /*REASON FOR i == test.length()
        * When the cursor is at the beginning of the edit text the loop will go forever
        * because the word before cursor will always give something as an output
        *
        * If the only word in an edit text is Ball and the cursor is between Ba and ll
        * getTextBeforeCursor(++i, InputConnection.GET_TEXT_WITH_STYLES) will return Ba
        * even when i > 2, so when it is checking for more than 2 words
        * it will return only the two words it gets
        * */
        while (containLetters(test) && i == test.length() ){
//            ++i;
            word = test;
            test = getCurrentInputConnection().getTextBeforeCursor(++i, InputConnection.GET_TEXT_WITH_STYLES);
            if(i >= Constants.MAXIMUM_NUMBER_OF_LETTERS_TO_CHECK) return null;
        }

        return word.toString();
    }

    public String getWordAfterCursor(InputConnection inputConnection){
        int i = 1;
        CharSequence word = "";
        CharSequence test =  getCurrentInputConnection().getTextAfterCursor(i, InputConnection.GET_TEXT_WITH_STYLES);

        test = test==null ? "" : test;


//        do {
//            test =  inputConnection.getTextBeforeCursor(i++, InputConnection.GET_TEXT_WITH_STYLES);
//        }while (containLetters(test) && test.length()==i-1);


        /* REASON FOR i == test.length()
         * When the cursor is at the end of the edit text the loop will go forever
         * because the get word after cursor will always give something as an output
         *
         * If the only word in an edit text is Ball and the cursor is between Ba and ll
         * getTextAfterCursor(++i, InputConnection.GET_TEXT_WITH_STYLES) will return ll
         * even when i > 2, so when it is checking for more than 2 words
         * it will return only the two words it gets
         * */
        while (containLetters(test) && i == test.length()) {
//            ++i;
            word = test;
            test = getCurrentInputConnection().getTextAfterCursor(++i, InputConnection.GET_TEXT_WITH_STYLES);
            if(i>= Constants.MAXIMUM_NUMBER_OF_LETTERS_TO_CHECK) return null;
        }
        return word.toString();
    }

    public boolean wordBeforeAndAfterCursorLengthIsNOTOk(String wordBeforeCursor, String wordAfterCursor){
        /*
        * return true if the length before or after the cursor is null
        * ie the word before and after cursor length is not ok when
        * their length is bigger than Constants.MAXIMUM_NUMBER_OF_LETTERS_TO_CHECK
        * */
        return wordBeforeCursor == null || wordAfterCursor == null;
    }

    private void changeToSmallLetters() {
        mMyKeyboardView.setButtonsToSmallLetters();
        capitalLetters = false;
    }

    private void changeToCapitalLetters() {
        mMyKeyboardView.setButtonsToCapitalLetters();
        capitalLetters = true;
    }


    public boolean containLetters(CharSequence s){
        String lettters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


        for(int j = 0; j<s.length(); ++j){ // for each character in charSequence S

            if (!lettters.contains(Character.toString(s.charAt(j))))return false;

        }

        return true;
    }

    @Override
    public void emojiClicked(String emoji) {
        getCurrentInputConnection().commitText(emoji, NEW_CURSOR_POSITION);
    }

    public void setToLetterKeyboard(){
        mMyKeyboardView.setToLettersKeyboard();
    }
    public void setToEmojiKeyboard(){
        mMyKeyboardView.setToEmojiKeyboard();
    }

    public void clearCopiedWord(){
        mMyKeyboardView.clearCopiedWord();
    }
}
