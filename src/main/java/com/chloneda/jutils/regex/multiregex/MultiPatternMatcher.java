package com.chloneda.jutils.regex.multiregex;

import java.util.List;

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
public class MultiPatternMatcher {

    private final int[] NO_MATCH = {};
    private final String[] NO_MATCH_STRING = {};
    private final MultiPatternAutomaton automaton;
    private List<String> patterns;

    public MultiPatternMatcher(MultiPatternAutomaton automaton, List<String> patterns) {
        this.automaton = automaton;
        this.patterns = patterns;
    }

    public MultiPatternMatcher(MultiPatternAutomaton automaton) {
        this.automaton = automaton;
    }

    public String[] getPatterns(int... indexs) {
        String[] vals = new String[indexs.length];
        int offset = 0;
        for (int index : indexs) {
            vals[offset++] = patterns.get(index);
        }
        return vals;
    }

    public int[] match(CharSequence s) {
        int p = 0;
        final int l = s.length();
        for (int i = 0; i < l; i++) {
            p = this.automaton.step(p, s.charAt(i));
            if (p == -1) {
                return NO_MATCH;
            }
        }
        return this.automaton.accept[p];
    }

    public String[] matchString(CharSequence s) {
        int p = 0;
        final int l = s.length();
        for (int i = 0; i < l; i++) {
            p = this.automaton.step(p, s.charAt(i));
            if (p == -1) {
                return NO_MATCH_STRING;
            }
        }
        int[] indexs = this.automaton.accept[p];
        return getPatterns(indexs);
    }

}
