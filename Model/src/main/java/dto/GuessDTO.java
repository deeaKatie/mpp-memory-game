package dto;

import model.Guess;
import model.HasId;
import model.User;
import model.Word;

public class GuessDTO implements HasId<Long> {

    private User user;
    private Guess guess;
    private Word word1;
    private Word word2;

    public GuessDTO(User user, Guess guess, Word word1, Word word2) {
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long aLong) {

    }
}
