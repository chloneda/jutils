package com.chloneda.utils.regex.multiregex;

import java.util.List;

public class MultiPatternMatcher {

    private final int[] NO_MATCH = {};
    private final String[] NO_MATCH_STRING = {};
    private final MultiPatternAutomaton automaton;
    private List<String> patterns;

    public MultiPatternMatcher(MultiPatternAutomaton automaton,List<String> patterns) {
        this.automaton = automaton;
        this.patterns = patterns;
    }

    public MultiPatternMatcher(MultiPatternAutomaton automaton) {
        this.automaton = automaton;
    }

    public String[] getPatterns(int ...indexs){
        String []vals = new String[indexs.length];
        int offset = 0;
        for(int index:indexs){
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
        int []indexs = this.automaton.accept[p];
        return getPatterns(indexs);
    }

}
