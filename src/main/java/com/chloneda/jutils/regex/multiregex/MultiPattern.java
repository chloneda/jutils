package com.chloneda.jutils.regex.multiregex;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

import java.util.ArrayList;
import java.util.Arrays;
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
public class MultiPattern {

    private final List<String> patterns;

    private MultiPattern(List<String> patterns) {
        this.patterns = new ArrayList<>(patterns);
    }

    public static MultiPattern of(List<String> patterns) {
        return new MultiPattern(patterns);
    }

    public static MultiPattern of(String... patterns) {
        return new MultiPattern(Arrays.asList(patterns));
    }

    public MultiPatternAutomaton makeAutomatonWithPrefix(String prefix) {
        final List<Automaton> automata = new ArrayList<>();
        for (final String ptn : this.patterns) {
            final String prefixedPattern = prefix + ptn;
            final Automaton automaton = new RegExp(prefixedPattern).toAutomaton();
            automaton.minimize();
            automata.add(automaton);
        }
        return MultiPatternAutomaton.make(automata);
    }

    /**
     * Equivalent of Pattern.compile, but the result is only valid for pattern search.
     * The searcher will return the first occurrence of a pattern.
     * <p>
     * This operation is costly, make sure to cache its result when performing
     * search with the same patterns against the different strings.
     *
     * @return A searcher object
     */
    public MultiPatternSearcher searcher() {
        final MultiPatternAutomaton searcherAutomaton = makeAutomatonWithPrefix(".*");
        final List<Automaton> indidivualAutomatons = new ArrayList<>();
        for (final String pattern : this.patterns) {
            final Automaton automaton = new RegExp(pattern).toAutomaton();
            automaton.minimize();
            automaton.determinize();
            indidivualAutomatons.add(automaton);
        }
        return new MultiPatternSearcher(searcherAutomaton, indidivualAutomatons);
    }


    /**
     * Equivalent of Pattern.compile, but the result is only valid for full string matching.
     * <p>
     * If more than one pattern are matched, with a match ending at the same offset,
     * return all of the pattern ids in a sorted array.
     * <p>
     * This operation is costly, make sure to cache its result when performing
     * search with the same patterns against the different strings.
     *
     * @return A searcher object
     */
    public MultiPatternMatcher matcher() {
        final MultiPatternAutomaton matcherAutomaton = makeAutomatonWithPrefix("");
        return new MultiPatternMatcher(matcherAutomaton);
    }

}
