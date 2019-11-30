package com.chloneda.jutils.regex.multiregex;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;
import org.junit.Assert;
import org.junit.Test;

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
public class UnicodeTest {
    @Test
    public  void testAutomatonWithUnicode() {
        final RegExp regexp = new RegExp("([0-9]{2,4}年)?[0-9]{1,2}月[0-9]{1,2}日");
        final Automaton forwardAutomaton = regexp.toAutomaton();
        {
            final RunAutomaton runAutomaton = new RunAutomaton(forwardAutomaton);
            Assert.assertTrue(runAutomaton.run("1982年9月17日"));
            Assert.assertFalse(runAutomaton.run("1982年9月127日"));
        }
    }
}
