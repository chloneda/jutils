package com.chloneda.jutils.regex.multiregex;

import dk.brics.automaton.State;

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
class MultiState {

    private final State[] states;

    public MultiState(State[] states) {
        this.states = states;
    }

    public boolean isNull() {
        for (State state : this.states) {
            if (state != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultiState that = (MultiState) o;
        return Arrays.equals(states, that.states);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(states);
    }

    public MultiState step(char token) {
        State[] nextStates = new State[this.states.length];
        for (int c = 0; c < this.states.length; c++) {
            State prevState = this.states[c];
            if (prevState == null) {
                nextStates[c] = null;
            } else {
                nextStates[c] = this.states[c].step(token);
            }
        }
        return new MultiState(nextStates);
    }


    public int[] toAcceptValues() {
        List<Integer> acceptValues = new ArrayList<>();
        for (int stateId = 0; stateId < this.states.length; stateId++) {
            State curState = this.states[stateId];
            if ((curState != null) && (curState.isAccept())) {
                acceptValues.add(stateId);
            }
        }
        int[] acceptValuesArr = new int[acceptValues.size()];
        for (int c = 0; c < acceptValues.size(); c++) {
            acceptValuesArr[c] = acceptValues.get(c);
        }
        return acceptValuesArr;
    }

}
