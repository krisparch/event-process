package com.exam.eventmetrics.engine;

import java.util.Comparator;

public class CompareWord implements Comparator<Word> {
    @Override
    public int compare(Word word1, Word word2) {
        if(word1.getCount() > word2.getCount()){
            return 1;
        } else if(word1.getCount() < word2.getCount()){
            return -1;
        }else {
            return word1.getText().compareTo(word2.getText());
        }
    }
}
