package suggestion_data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WordPredictor {
    static final String letters = "abcdefghijklmnopqrstuvwxyz";
    static final double PREDICTION_PROBABILITY = 0.7f;

    public static BufferedReader readFrom(String pathName) throws FileNotFoundException {
        File readFile = new File(pathName);
        FileReader fileReader = new FileReader(readFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        return bufferedReader;
    }

    public static FileWriter writeTo(String pathName) throws IOException {
        //remember to do fileWriter.flush() after writing or else it will not be written
        File writeFile = new File(pathName);
        //if append is true the file will be written in the end of the text document and not at the beginning
        return new FileWriter(writeFile, true);
    }

    public static void addToHashMap(String word, HashMap<String, Integer > hashMap){
        if(word.isEmpty()) return;
        if (hashMap.containsKey(word)){
//            hashMap.get(word);
            //replace the value with value += 1
            hashMap.put(word, hashMap.get(word) + 1);
        } else hashMap.put(word, 1);
    }

    public static void addToHashMap(String word, HashMap<String, Integer > hashMap, int times){
        if(word.isEmpty()) return;
        if (hashMap.containsKey(word)){
//            hashMap.get(word);
            //replace the value with value += 1
            hashMap.put(word, hashMap.get(word) + times);
        } else hashMap.put(word, times);
    }

    public static double sumOfElementValuesInHashMap(Set<String> words, HashMap<String, Integer> hashMap){
        /*
         * get the each element from the hashmap
         * and get the sum of all the  values of the element in the hashmap
         * */
        double sum = 0;
        for (String word: words) {
            if(hashMap.containsKey(word)){
                sum += hashMap.get(word);
            }
        }
        return sum;
    }

    public static double sum(Collection collection){
        int sum = 0;
        for (Object i : collection){
            sum = sum + (int) i;
        }
        return sum;
    }

    public static double P(String word, HashMap<String, Integer> hashMap, Double summation){
        if(word.isEmpty() || !hashMap.containsKey(word)) return 0;

        return summation == null ? hashMap.get(word)/sum(hashMap.values()) : hashMap.get(word)/summation;
       //meaning if summation == null return hashMap.get(word)/sum(hashMap.values()) else return hashMap.get(word)/summation
    }

    public static String correctionMain(String word,HashMap<String, Integer> hashMap){
        if(hashMap == null) return "";
        double sumOfHashMapValues = sum(hashMap.values());
        String actualWord = "";
        for (String potentialWord : candidates(word, hashMap)){

            double probabilityOfPotentialWord = P(potentialWord, hashMap, sumOfHashMapValues);
            double probabilityOfActualWord = P(actualWord, hashMap, sumOfHashMapValues);
            if(probabilityOfPotentialWord > probabilityOfActualWord){
                actualWord = potentialWord;
            }
        }
        return actualWord;
//        for (String potentialWord : candidates(word, hashMap)){

    }

    public static String correction(String word,HashMap<String, Integer> hashMap){
        if(hashMap == null) return "";
        Set<String> candidates = candidates(word, hashMap);
        double sumOfHashMapValues = sumOfElementValuesInHashMap(candidates, hashMap);
        double probabilityOfActualWord = 0;

        String actualWord = "";
        for (String potentialWord : candidates){

            double probabilityOfPotentialWord = P(potentialWord, hashMap, sumOfHashMapValues);
            probabilityOfActualWord = P(actualWord, hashMap, sumOfHashMapValues);
            if(probabilityOfPotentialWord > probabilityOfActualWord){
                actualWord = potentialWord;
                probabilityOfActualWord = probabilityOfPotentialWord;
            }
        }
        return probabilityOfActualWord> PREDICTION_PROBABILITY ? actualWord : "";
//        for (String potentialWord : candidates(word, hashMap)){

    }

    public static Set<String> candidates(String word, HashMap<String, Integer> hashMap){
        Set<String> candidates = known(Set.of(word), hashMap);

        if(!candidates.isEmpty()){
            return candidates;
        }

        candidates = known(edits1(word), hashMap);
        if (!candidates.isEmpty()){
            return known(edits1(word), hashMap);
        }

        candidates = known(edits2(word), hashMap);

        return !candidates.isEmpty() ? candidates : Set.of(word);
        //if candidates is not empty return candidates else return Set.of(word)
    }

    public static Set<String> known(Set<String> words, HashMap<String , Integer> hashMap){
//        return new HashSet<>().addAll(
//                String word;
//        for ( word : words){
//            if(hashMap.containsKey(word)){
//                new Set<String>()
//            }
//        }
//        )
        Set<String> set = new HashSet<>();
        for (String word : words){
            if(hashMap.containsKey(word)){
                set.add(word);
            }
        }
        return set;
    }

    //for each word look choose the one with the highest probability


    /*
    * String.substring(beginIndex) removes "beginIndex" characters from the string and returns the rest
    *meaning it starts the string from the "beginIndex"
    * eg "black".substring(2) returns "ack" //starts from 2
    *
    * String.substring(beginIndex, endIndex) starts the string from the "beginIndex"
    * and stops the string at ("endIndex" - 1)
    * e.g "black list".substring(1,7) returns "back l"
     */
    public static String removeFirstNChars(String word, int N){
//        N=-N;
        if(word == null || word.isEmpty()) return word;
        if(N==word.length() || N>word.length()) return "";

        return word.substring(N);

        //word.substring(0,N) returns a string from the 0th char of word
        //to the (N-1)th char of word. If word= black and N = 3
        //fistNChars will be  bla

    }

    public static String showFirstNChars(String word, int N){
//        N = N-1;
        if(word == null || N>word.length()) return word;
        if(N == 0) return "";

        return word.substring(0,N);
        //(0,1) is the first character
        //(0, world.length()) will be all the characters
    }

    public static ArrayList<ArrayList<String>> splitTheWordIntoItsCharacters(String word){
        ArrayList<ArrayList<String>> arrayListOfArray = new ArrayList<>();
        for(int i = 0; i<word.length()+1; ++i){
            ArrayList<String> inner = new ArrayList<>(Arrays.asList(showFirstNChars(word, i), removeFirstNChars(word, i)));
            arrayListOfArray.add(inner);
        }
        return arrayListOfArray;
        /*
        * for 'big' will return [('', 'big'), ('b', 'ig'), ('bi', 'g'), ('big', '')]
        * */
    }

    public static ArrayList<String> deletes (ArrayList<ArrayList<String>> arrayListArrayList){
        ArrayList<String > listOfDeletes = new ArrayList<>();
        for (ArrayList<String > arrayList : arrayListArrayList){
            String L = arrayList.get(0);
            String R = arrayList.get(1);
            if(!R.isEmpty()){
                String each = L + removeFirstNChars(R, 1);
                listOfDeletes.add(each);
            }

        }
        return listOfDeletes;

        /*
        * for big [('', 'big'), ('b', 'ig'), ('bi', 'g'), ('big', '')]
        * [1] = ''+ig [2] = 'b'+'g' [3]= 'bi'+'' . R is empty in [4]
        * deletes is ['ig', 'bg', 'bi'] for big
        *
        * deletes returns ['ig', 'bg', 'bi'] for big
        * it deletes the first letter from the second array,
        * adds it to The word in the first array then adds it to a list
        * */
    }

    public static ArrayList<String> transposes(ArrayList<ArrayList<String >> arrayListArrayList){
        ArrayList<String > listOfTranspose = new ArrayList<>();
        for (ArrayList<String > arrayList : arrayListArrayList){
            String L = arrayList.get(0);
            String R = arrayList.get(1);
            if(R.length()>1){
                String each = L + R.charAt(1) + R.charAt(0) + removeFirstNChars(R, 2);
                listOfTranspose.add(each);
            }

        }
        return listOfTranspose;

        /*
        *transposes for big [('', 'big'), ('b', 'ig'), ('bi', 'g'), ('big', '')]
        * will be [ ''+'i'+'b'+'g', 'b'+'g'+'i'+'']  That is all because of the condition len(R)>1
        *
        *  for big transposes will return ['ibg', 'bgi']
        * */

    }

    public static ArrayList<String> replaces(ArrayList<ArrayList<String>> arrayListArrayList){
        ArrayList<String > listOfReplace = new ArrayList<>();
        for (ArrayList<String > arrayList : arrayListArrayList){
            String L = arrayList.get(0);
            String R = arrayList.get(1);
            if(!R.isEmpty()){
                for (int i = 0; i<letters.length(); ++i){
                    String each = L + letters.charAt(i) + removeFirstNChars(R, 1);
                    listOfReplace.add(each);
                }
            }

        }
        return listOfReplace;

        /*
        * if String letter = "ab";
        * if the word is big splits = [('', 'big'), ('b', 'ig'), ('bi', 'g'), ('big', '')]
        * replaces = [''+'a'+'ig', ''+'b'+'ig', 'b'+'a'+'g', 'b'+'b'+'g', 'bi'+'a'+'', 'bi'+'b'+'']
        * then replaces will return [aig, big, bag, bbg, bia, bib] for big
        * */
    }

    public static ArrayList<String> inserts(ArrayList<ArrayList<String>> arrayListArrayList) {
        ArrayList<String> listOfInsert = new ArrayList<>();
        for (ArrayList<String> arrayList : arrayListArrayList) {
            String L = arrayList.get(0);
            String R = arrayList.get(1);
            for (int i = 0; i < letters.length(); ++i) {
                String each = L + letters.charAt(i) + R;
                listOfInsert.add(each);
            }

        }
        return listOfInsert;

        /*
        * "it dosent care if R is not empty, for each pair in split put
        * L+ 'eachLetterInTheAlphabet' + R into an array
        * it is just like replaces
        * say letters = "ab" and word = big
        * indert = [abig, bbig, baig, bbig, biag, bibg, biga, bigb]"
        * */

    }

    public static Set<String > edits1(String word){
        ArrayList<ArrayList<String>> split = splitTheWordIntoItsCharacters(word);
        ArrayList<String > edits = new ArrayList<>();
        edits.addAll(deletes(split));
        edits.addAll(transposes(split));
        edits.addAll(replaces(split));
        edits.addAll(inserts(split));
        return new HashSet<>(edits);
    }

    public static Set<String > edits2(String word){
        Set<String > set = new HashSet<>();
        for (String guess : edits1(word)){
            set.addAll(edits1(guess));
        }
        return set;
    }


}
