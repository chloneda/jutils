package com.chloneda.jutils.regex.multiregex;

import dk.brics.automaton.Automaton;

import dk.brics.automaton.DkBricsAutomatonHelper;
import dk.brics.automaton.State;

import java.util.*;

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
public class MultiPatternAutomaton {

    public final int[][] accept;
    final boolean[] atLeastOneAccept;
    private final int stride;
    private final int[] transitions;
    private final int[] alphabet;
    private final int nbPatterns;

    private MultiPatternAutomaton(final int[][] accept,
                                  final int[] transitions,
                                  final char[] points,
                                  final int nbPatterns) {
        this.accept = accept;
        this.transitions = transitions;
        this.alphabet = alphabet(points);
        this.stride = points.length;
        this.atLeastOneAccept = new boolean[accept.length];
        for (int i = 0; i < accept.length; i++) {
            this.atLeastOneAccept[i] = this.accept[i].length > 0;
        }
        this.nbPatterns = nbPatterns;
    }

    private static int[] alphabet(final char[] points) {
        final int[] alphabet = new int[Character.MAX_VALUE - Character.MIN_VALUE + 1];
        int i = 0;
        for (int j = 0; j <= (Character.MAX_VALUE - Character.MIN_VALUE); j++) {
            if (i + 1 < points.length && j == points[i + 1])
                i++;
            alphabet[j] = i;
        }
        return alphabet;
    }

    static MultiState initialState(List<Automaton> automata) {
        final State[] initialStates = new State[automata.size()];
        int c = 0;
        for (final Automaton automaton : automata) {
            initialStates[c] = automaton.getInitialState();
            c += 1;
        }
        return new MultiState(initialStates);
    }

    static MultiPatternAutomaton make(final List<Automaton> automata) {
        for (final Automaton automaton : automata) {
            automaton.determinize();
        }

        final char[] points = DkBricsAutomatonHelper.pointsUnion(automata);

        // states that are still to be visited
        final Queue<MultiState> statesToVisits = new LinkedList<>();
        final MultiState initialState = initialState(automata);
        statesToVisits.add(initialState);

        final List<int[]> transitionList = new ArrayList<>();

        final Map<MultiState, Integer> multiStateIndex = new HashMap<>();
        multiStateIndex.put(initialState, 0);

        while (!statesToVisits.isEmpty()) {
            final MultiState visitingState = statesToVisits.remove();
            assert multiStateIndex.containsKey(visitingState);
            final int[] curTransitions = new int[points.length];
            for (int c = 0; c < points.length; c++) {
                final char point = points[c];
                final MultiState destState = visitingState.step(point);
                if (destState.isNull()) {
                    curTransitions[c] = -1;
                } else {
                    final int destStateId;
                    if (!multiStateIndex.containsKey(destState)) {
                        statesToVisits.add(destState);
                        destStateId = multiStateIndex.size();
                        multiStateIndex.put(destState, destStateId);
                    } else {
                        destStateId = multiStateIndex.get(destState);
                    }
                    curTransitions[c] = destStateId;
                }
            }
            transitionList.add(curTransitions);
        }

        assert transitionList.size() == multiStateIndex.size();
        final int nbStates = multiStateIndex.size();

        final int[] transitions = new int[nbStates * points.length];
        for (int stateId = 0; stateId < nbStates; stateId++) {
            for (int pointId = 0; pointId < points.length; pointId++) {
                transitions[stateId * points.length + pointId] = transitionList.get(stateId)[pointId];
            }
        }

        final int[][] acceptValues = new int[nbStates][];
        for (final Map.Entry<MultiState, Integer> entry : multiStateIndex.entrySet()) {
            final Integer stateId = entry.getValue();
            final MultiState multiState = entry.getKey();
            acceptValues[stateId] = multiState.toAcceptValues();
        }

        return new MultiPatternAutomaton(acceptValues, transitions, points, automata.size());
    }

    public int step(final int state, final char c) {
        return transitions[(state * this.stride) + alphabet[c - Character.MIN_VALUE]];
    }

    public int getNbPatterns() {
        return this.nbPatterns;
    }

}
