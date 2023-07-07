package dto;

import model.Guess;
import model.HasId;
import model.Word;

public class GuessDTO implements HasId<Long> {

    private Guess guess;
    private Word word1;
    private Word word2;

    public GuessDTO(Guess guess, Word word1, Word word2) {
        this.guess = guess;
        this.word1 = word1;
        this.word2 = word2;
    }

    public Guess getGuess() {
        return guess;
    }

    public void setGuess(Guess guess) {
        this.guess = guess;
    }

    public Word getWord1() {
        return word1;
    }

    public void setWord1(Word word1) {
        this.word1 = word1;
    }

    public Word getWord2() {
        return word2;
    }

    public void setWord2(Word word2) {
        this.word2 = word2;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long aLong) {

    }
}
