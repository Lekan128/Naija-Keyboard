package com.olalekan.naijakeyboard;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MyInputMethodTest {

    MyInputMethod mMyInputMethod;
    @Before
    public void setUp(){
        mMyInputMethod = new MyInputMethod();
    }
    @Test
    public void containLetters() {
        assertTrue(mMyInputMethod.containLetters("blasBla"));
        assertFalse(mMyInputMethod.containLetters("blas.Bla"));
    }
}