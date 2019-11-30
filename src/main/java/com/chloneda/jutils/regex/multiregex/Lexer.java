package com.chloneda.jutils.regex.multiregex;

import java.io.Reader;
import java.util.ArrayList;
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
public class Lexer<T extends Enum> {

    private final ArrayList<T> types = new ArrayList<>();
    private final ArrayList<String> patterns = new ArrayList<>();
    private transient MultiPatternAutomaton automaton = null;

    public Lexer<T> addRule(final T tokenType, final String pattern) {
        this.types.add(tokenType);
        this.patterns.add(pattern);
        this.automaton = null;
        return this;
    }

    public MultiPatternAutomaton getAutomaton() {
        if (this.automaton == null) {
            this.automaton = MultiPattern.of(patterns).makeAutomatonWithPrefix("");
        }
        return this.automaton;
    }

    public Scanner<T> scannerFor(final Reader reader) {
        return new Scanner<T>(this.getAutomaton(), reader, this.types);
    }

    public Scanner<T> scannerFor(final CharSequence seq) {
        return new Scanner<T>(this.getAutomaton(), seq, this.types);
    }

    public Iterable<Token<T>> scan(final CharSequence seq) {
        final Scanner<T> scanner = this.scannerFor(seq);
        scanner.nextUnchecked();
        return new Iterable<Token<T>>() {

            Token<T> next = Token.fromScanner(scanner);

            @Override
            public Iterator<Token<T>> iterator() {
                return new Iterator<Token<T>>() {
                    @Override
                    public boolean hasNext() {
                        return next != null;
                    }

                    @Override
                    public Token<T> next() {
                        Token<T> buff = next;
                        if (scanner.nextUnchecked()) {
                            next = Token.fromScanner(scanner);
                        } else {
                            next = null;
                        }
                        return buff;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

}
