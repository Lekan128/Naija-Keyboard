package suggestion_data;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

public class SuggestionData {
    private static final int TIMES_WORD_SHOULD_BE_WRITTEN_BEFORE_SEEN_AS_CORRECT = 2;

    private SuggestionData(Context context){}
    private static SuggestionData mSuggestionData;
    private static Context mContext;
    private static InputStream mInputStream;
    private static HashMap<String, Integer> suggestionsHashMap;
    public static HashMap<String, Integer> possibleSuggestionsToBeAddedHashMap = new HashMap<>();
    public static boolean suggestionsDoneLoading = false;
    private final static String ASSET_SORTED_FILE_NAME = "sorted.txt";
    private static final String INTERNAL_STORAGE_SUGGESTION_FILE_NAME = "suggestions";
    private static final int NUMBER_OF_TIMES_TO_ADD_SUGGESTION = 4;


    public static SuggestionData getInstance(Context context){
        mContext = context;
        try {
            mInputStream = context.getAssets().open(ASSET_SORTED_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getData();
        return mSuggestionData==null ? new SuggestionData(context) : mSuggestionData;
    }

    //Todo:
    /*
    * GetData once ie when suggestionHashMap is null
    * return new hashmap if suggestionDoneLoading is false
    * only return a hashmap is suggestion done loading is true
    * */



    private static boolean getData(){
        if(mInputStream == null) return false;

        if(suggestionsHashMap == null){
            suggestionsHashMap = new HashMap<>();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    readTextFile();
                    readSuggestionsFromInternalStorage(mContext);
                    suggestionsDoneLoading = true;
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();

//            while (thread.isAlive()){
//
//            }
        }


        return true;
//        String inputPath = "/Users/olalekan/AndroidStudioProjects/VideoApplication/app/src/main/assets/sorted.txt";
//        WordPredictor.
    }

    private static void readTextFile(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream));
            String line;


            while ((line = reader.readLine()) != null){
                Log.i("Suggestion Read", "Internal: " + line);


                WordPredictor.addToHashMap(line, suggestionsHashMap);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readSuggestionsFromInternalStorage(Context context) {
        FileInputStream inputStream;

        try {
            inputStream = context.openFileInput(INTERNAL_STORAGE_SUGGESTION_FILE_NAME);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);


            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null){
                Log.i("Suggestion Read", "Storage: " + line);


                WordPredictor.addToHashMap(line, suggestionsHashMap);
            }

            /*same as the one on top
            String line = bufferedReader.readLine();
            while (line!=null){
                WordPredictor.addToHashMap(line, suggestionsHashMap);
                line = bufferedReader.readLine();
            }*/

        } catch (IOException e) {
            if(e instanceof FileNotFoundException){
                String defaultAddedWord = "I";
                Log.i("Suggestion Write", "Storage: " + defaultAddedWord);
                //writing anything to the file will create the fiole
                writeSuggestionToInternalStorage(defaultAddedWord, 1, context);
                WordPredictor.addToHashMap(defaultAddedWord, suggestionsHashMap);
            }else e.printStackTrace();
        }

    }

    private static void writeSuggestionToInternalStorage(String word, int numberOfTimes, Context context) {
        word = wordWithNewLine(word);
        File file = new File(context.getFilesDir(), INTERNAL_STORAGE_SUGGESTION_FILE_NAME);
//        File file1 = g

        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file, true);
            //add it to database this number of times
            for(int i = 0; i<numberOfTimes; ++i){
                fileWriter.append(word);
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addToSuggestions(String word, Context context){
        //if the word has been typed TIMES_WORD_SHOULD_BE_WRITTEN_BEFORE_SEEN_AS_CORRECT times or more
        // add it to suggestions database and suggestion hashmap
        //if not add it to possible suggestions to be added hashmap
        //This helps to make sure every mistake the user corrects is not added to the suggestion
        if(possibleSuggestionsToBeAddedHashMap.containsKey(word)
                && possibleSuggestionsToBeAddedHashMap.get(word)
                >= TIMES_WORD_SHOULD_BE_WRITTEN_BEFORE_SEEN_AS_CORRECT){
            Log.i("Suggestion Write", "Storage: " + word);

            writeSuggestionToInternalStorage(word, NUMBER_OF_TIMES_TO_ADD_SUGGESTION, context);
            WordPredictor.addToHashMap(word, suggestionsHashMap, NUMBER_OF_TIMES_TO_ADD_SUGGESTION);
        }
        else {
            Log.i("Suggestion Write", "adding to possibleSuggestionsToBeAddedHashMap: " + word);
            WordPredictor.addToHashMap(word, possibleSuggestionsToBeAddedHashMap);
        }

    }

    private static String wordWithNewLine(String word){
        return word.toLowerCase(Locale.ROOT) + '\n';
    }

    public boolean isSuggestionsDoneLoading(){
        return suggestionsDoneLoading;
    }

    public HashMap<String, Integer> getSuggestionsHashMap() {
        if(!getData()) return null;
        return suggestionsDoneLoading ? suggestionsHashMap : new HashMap<>();
    }
}
