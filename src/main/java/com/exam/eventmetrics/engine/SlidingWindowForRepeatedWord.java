package com.exam.eventmetrics.engine;


import com.exam.eventmetrics.exceptions.WrongActionException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
@Service
public class SlidingWindowForRepeatedWord {
    public static final int MAX_SIZE = 5;
    private TreeSet<Word> wordCounter;
    private Map<String, Word> cacheForWords;
    private int messageCount;
    private List<String> messages;

    public SlidingWindowForRepeatedWord() {
        this.wordCounter = new TreeSet<>(new CompareWord());
        cacheForWords = new HashMap<>();
        messageCount = 0;
        messages = new ArrayList<>(10);
    }

    public void addMessage(String message) {
        if(messageCount >= 10){
            removeMessage();
        }
        message=message.replaceAll("[^a-zA-Z0-9]", " ").trim().replaceAll(" +", " ");;
        //System.out.println("Message: "+message);
        Arrays.stream(message.split(" ")).forEach(this::addWord);
        messageCount+=1;
        messages.add(message);
    }

    public void removeMessage() throws WrongActionException {
        if(messageCount <0){
            log.error("Can't remove message");
            throw new WrongActionException("Can't remove message, Window is Empty");
        }
        String message = messages.remove(0);
        Arrays.stream(message.split(" ")).forEach(this::removeWord);
        messageCount-=1;
    }

    public void addWord(String word){
        Word expectedWord = cacheForWords.getOrDefault(word, null);
        if(expectedWord == null){
            expectedWord = new Word(word, 1);
            wordCounter.add(expectedWord);
        }else {
            expectedWord.setCount(expectedWord.getCount()+1);
        }
        cacheForWords.put(word, expectedWord);
    }

    public void removeWord(String word){
        Word expectedWord = cacheForWords.getOrDefault(word, null);
        if(expectedWord == null){
            log.error("No word exist in the Window");
        }else {
            int count = expectedWord.getCount();
            if(count == 1){
                cacheForWords.remove(word);
                wordCounter.remove(expectedWord);
            }else {
                expectedWord.setCount(expectedWord.getCount()-1);
            }
        }
    }

    public TreeSet<Word> calculateMostRepeatedWords(TreeSet<Word> copyWordCounter, int size){
        Word highestRank = copyWordCounter.stream().max(Comparator.comparing(Word::getCount)).orElse(new Word());
        return copyWordCounter
                .stream()
                .filter(word -> word.getCount() == highestRank.getCount())
                .limit(size)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public void printMostUsedWords(){
        TreeSet<Word> copyWordCounter = new TreeSet<>(wordCounter);
        int size=MAX_SIZE;
        while (size>0) {
            TreeSet<Word> words = calculateMostRepeatedWords(copyWordCounter,size);
            if(words.size()==0)
                break;
            size -= words.size();
            int curCount = words.first().getCount();
            words.forEach(word ->
                    System.out.println(word.getText() + "-->" + word.getCount()));
            Iterator<Word> iterator = copyWordCounter.iterator();
            copyWordCounter.removeIf(word -> word.getCount()==curCount);
        }
    }
}
