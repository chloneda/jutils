package com.chloneda.jutils.regex.multiregex;

import com.chloneda.jutils.regex.multiregex.Lexer;
import com.chloneda.jutils.regex.multiregex.ScanException;
import com.chloneda.jutils.regex.multiregex.Token;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Paul Masurel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
public class TokenizerTest {
    private static enum TOKEN {
        WHITESPACE,
        WORD,
        PUNCTUATION;
    }


    private static enum TEST {
        A,
        ABC,
        BCD,
        ABD,
        D;
    }


    @Test
    public  void testSimpleLexer() {
        final Lexer<TOKEN> lexer = new Lexer<>();
        lexer
                .addRule(TOKEN.WHITESPACE, " ")
                .addRule(TOKEN.WORD, "[a-zA-Z]+")
                .addRule(TOKEN.PUNCTUATION, "[,\\.\\!\\?]");
        final String txt = "Bonjour herve!";
        final Iterator<Token<TOKEN>> tokenIt =  lexer.scan(txt).iterator();
        Assert.assertTrue(tokenIt.hasNext());
        Assert.assertEquals(tokenIt.next(), new Token<>(TOKEN.WORD, "Bonjour"));
        Assert.assertTrue(tokenIt.hasNext());
        Assert.assertEquals(tokenIt.next(), new Token<>(TOKEN.WHITESPACE, " "));
        Assert.assertTrue(tokenIt.hasNext());
        Assert.assertEquals(tokenIt.next(), new Token<>(TOKEN.WORD, "herve"));
        Assert.assertTrue(tokenIt.hasNext());
        Assert.assertEquals(tokenIt.next(), new Token<>(TOKEN.PUNCTUATION, "!"));
        Assert.assertFalse(tokenIt.hasNext());
        Assert.assertEquals(tokenIt.next(), null);
    }


    @Test
    public  void testPriority() {
        final Lexer<TEST> lexer = new Lexer<>();
        lexer
                .addRule(TEST.ABC, "abc")
                .addRule(TEST.A, "a")
                .addRule(TEST.ABD, "abd")
                .addRule(TEST.D, "b?d")
                .addRule(TEST.BCD, "bcd");

        {
            final String txt = "abd";
            final Iterator<Token<TEST>> tokenIt =  lexer.scan(txt).iterator();
            Assert.assertTrue(tokenIt.hasNext());
            Assert.assertEquals(tokenIt.next(), new Token<>(TEST.A, "a"));
            Assert.assertTrue(tokenIt.hasNext());
            Assert.assertEquals(tokenIt.next(), new Token<>(TEST.D, "bd"));
            Assert.assertFalse(tokenIt.hasNext());
            Assert.assertEquals(tokenIt.next(), null);
        }
        {
            final String txt = "abcd";
            final Iterator<Token<TEST>> tokenIt =  lexer.scan(txt).iterator();
            Assert.assertTrue(tokenIt.hasNext());
            Assert.assertEquals(tokenIt.next(), new Token<>(TEST.ABC, "abc"));
            Assert.assertTrue(tokenIt.hasNext());
            Assert.assertEquals(tokenIt.next(), new Token<>(TEST.D, "d"));
            Assert.assertFalse(tokenIt.hasNext());
            Assert.assertEquals(tokenIt.next(), null);
        }
        {

            final String txt = "abce";
            try {
                final Iterator<Token<TEST>> tokenIt = lexer.scan(txt).iterator();
                Assert.assertTrue(tokenIt.hasNext());
                Assert.assertEquals(tokenIt.next(), new Token<>(TEST.ABC, "abc"));
                Assert.assertTrue(tokenIt.hasNext());
                Assert.assertEquals(tokenIt.next(), new Token<>(TEST.D, "d"));
            }
            catch (RuntimeException e) {
                final ScanException typedError = (ScanException)e.getCause();
                Assert.assertEquals(typedError.getOffset(), 3);
                Assert.assertEquals(typedError.getMessage(), "Could not find any token at (3):\"abc|e\"");
            }
        }
    }

}
