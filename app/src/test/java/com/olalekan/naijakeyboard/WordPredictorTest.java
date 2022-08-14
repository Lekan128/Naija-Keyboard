package com.olalekan.naijakeyboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import suggestion_data.WordPredictor;


public class WordPredictorTest {
    HashMap<String, Integer> actualHashMap = new HashMap<>();
    String word;
    ArrayList<ArrayList<String>> arrayListArrayList;

    @Before
    public void setUp(){
        word = "big";
        actualHashMap.put("big", 1);
        actualHashMap.put("fat", 2);
        actualHashMap.put("blag", 3);
        actualHashMap.put("dig", 4);
        actualHashMap.put("bag", 5);

        ArrayList<String> arrayList0 = new ArrayList<>(Arrays.asList("", "big"));
        ArrayList<String> arrayList1 = new ArrayList<>(Arrays.asList("b", "ig"));
        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList("bi", "g"));
        ArrayList<String> arrayList3 = new ArrayList<>(Arrays.asList("big", ""));

        arrayListArrayList = new ArrayList<>(Arrays.asList(arrayList0, arrayList1, arrayList2, arrayList3));

    }

    @Test
    public void addToHashMap() {
        HashMap<String, Integer> expectedHashMap = new HashMap<>();
        expectedHashMap.put("big", 2);
        expectedHashMap.put("fat", 2);
        expectedHashMap.put("blag", 3);
        expectedHashMap.put("dig", 4);
        expectedHashMap.put("bag", 5);

        WordPredictor.addToHashMap(word, actualHashMap);
        assertEquals("simple addition of 1 to the value an element already present", expectedHashMap, actualHashMap);

//        Assertions.assertAll("Adding to hashMap",
//                ()-> Assertions.assertEquals(expectedHashMap, actualHashMap,
//                        "simple addition of 1 to the value an element already present"),
//                ()-> Assertions.assertEquals(expectedHashMap, actualHashMap,
//                        "adding an element not present yet to the Hashmap")
//        );

        WordPredictor.addToHashMap("notIn", actualHashMap);
        expectedHashMap.put("notIn", 1);

        assertEquals("adding an element not present yet to the Hashmap", expectedHashMap, actualHashMap);

        WordPredictor.addToHashMap("", actualHashMap);

        assertEquals("adding an empty string", expectedHashMap, actualHashMap);

        HashMap<String, Integer> switchedExpected = expectedHashMap;
        //expected is the new actual here
        expectedHashMap.put("bleed", 10);
        WordPredictor.addToHashMap("bleed", switchedExpected, 10);
        assertEquals(switchedExpected, expectedHashMap);

        expectedHashMap.replace("bleed", 20);
        //bleed was already 10 in switch expected
        WordPredictor.addToHashMap("bleed", switchedExpected, 10);
        assertEquals(switchedExpected, expectedHashMap);



    }

    @Test
    public void sum() {
        Set<Integer> actualSet = Set.of(4,5,10, 1);
        assertEquals(20.0, WordPredictor.sum(actualSet), 0);
        assertEquals(0, WordPredictor.sum(Set.of()), 0);
        //delta max difference between the expected and actual value to be considered equal
    }

    @Test
    public void sumOfElementValuesInHashMap() {
        /*
        * get the each element from the hashmap
        * and get the sum of all the  values of the element in the hashmap*/
        HashMap<String, Integer> hashMap = new HashMap<>(){{
            put("good", 10);
            put("big", 2);
            put("nice", 5);
            put("brittle", 6);
            put("s", 20);
        }};

        Set<String> actualSet = Set.of("good","big","brittle", "why");
        //good=10 | big=2 | brittle = 6 | why is not in hashmap put 0
        //expected = 10+2+6+0 = 18
        assertEquals(18, WordPredictor.sumOfElementValuesInHashMap(actualSet, hashMap), 0);
        assertEquals(0, WordPredictor.sumOfElementValuesInHashMap(Set.of(), hashMap), 0);
        //delta max difference between the expected and actual value to be considered equal
    }

    @Test
    public void p() {
        double expectedValue = 1.0/(1+2+3+4+5);

        assertEquals("sum without summation", expectedValue, WordPredictor.P(word, actualHashMap, null), 0);

        assertEquals("sum with summation", expectedValue, WordPredictor.P(word, actualHashMap,
                WordPredictor.sum(actualHashMap.values())), 0);

        assertEquals("empty word", 0, WordPredictor.P("", actualHashMap,
                WordPredictor.sum(actualHashMap.values())), 0 );

        assertEquals("hashmap does not contain the word / new hash map", 0, WordPredictor.P(word, new HashMap<>(),
                WordPredictor.sum(actualHashMap.values())), 0);

    }

    @Test
    public void correction() {
        word = "biag";

        assertEquals("bag", WordPredictor.correction(word, actualHashMap));
        assertEquals("", WordPredictor.correction(word, new HashMap<String, Integer>()));
        assertEquals("", WordPredictor.correction(word, null));

    }

    @Test
    public void candidates() {
        word = "biag";

        assertEquals(Set.of("bag", "big", "blag"), WordPredictor.candidates(word, actualHashMap));
        assertEquals(Set.of(""), WordPredictor.candidates("", actualHashMap));


    }

    @Test
    public void known() {
        assertEquals(Set.of("big", "bag"), WordPredictor.known(Set.of("big", "black", "bag"), actualHashMap));
        assertEquals(Set.of(), WordPredictor.known(Set.of(), actualHashMap));
        assertTrue(WordPredictor.known(Set.of(), actualHashMap).isEmpty());

    }

    @Test
    public void removeFirstNChars() {
        assertEquals("g", WordPredictor.removeFirstNChars(word, 2));
        assertEquals(word, WordPredictor.removeFirstNChars(word, 0));
        assertEquals("", WordPredictor.removeFirstNChars(word, 3));
        assertEquals("", WordPredictor.removeFirstNChars(word, 4));
//        Class<? extends Throwable> IndexOutOfBoundsException = new  java.lang.IndexOutOfBoundsException;
//        assertThrows(IndexOutOfBoundsException , WordPredictor.removeFirstNChars(word, -1));




    }

    @Test
    public void showFirstNChars() {
        assertEquals("bi", WordPredictor.showFirstNChars(word, 2));
        assertEquals("", WordPredictor.showFirstNChars(word, 0));
        assertEquals(word, WordPredictor.showFirstNChars(word, 3));
        assertEquals(word, WordPredictor.showFirstNChars(word, 4));
//
    }

    @Test
    public void splitTheWordIntoItsCharacters() {

        assertEquals(arrayListArrayList, WordPredictor.splitTheWordIntoItsCharacters(word));
    }

    @Test
    public void deletes() {
        assertEquals(new ArrayList<String>(Arrays.asList("ig", "bg", "bi")), WordPredictor.deletes(arrayListArrayList));
    }

    @Test
    public void transposes() {
        assertEquals(new ArrayList<String>(Arrays.asList("ibg", "bgi")), WordPredictor.transposes(arrayListArrayList));
    }

    @Test
    public void replaces() {
//        assertEquals(new ArrayList<String>(Arrays.asList("aig", "big", "bag", "bbg", "bia", "bib")), WordPredictor.replaces(arrayListArrayList));
    }

    @Test
    public void inserts() {
    }

    @Test
    public void edits1() {
    }

    @Test
    public void edits2() {
    }
}