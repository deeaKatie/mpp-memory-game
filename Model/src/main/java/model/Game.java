package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "games")
public class Game implements HasId<Long> {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    @OneToOne
    private Configuration configuration;
    @OneToOne
    private User player;
    private Integer points;
    @OneToMany
    private List<Guess> guesses;
    private Integer correctGuesses;
    private LocalDateTime dateStarted;
    private LocalDateTime dateEnded;


    public Game() {
        id = -1L;
        configuration = new Configuration();
        player = new User();
        points = 0;
        guesses = new ArrayList<>();
        correctGuesses = 0;
        dateStarted = LocalDateTime.now();
    }

    public Game(Long id, Configuration configuration, User player, Integer points, List<Guess> guesses, Integer correctGuesses, LocalDateTime dateStarted, LocalDateTime dateEnded) {
        this.id = id;
        this.configuration = configuration;
        this.player = player;
        this.points = points;
        this.guesses = guesses;
        this.correctGuesses = correctGuesses;
        this.dateStarted = dateStarted;
        this.dateEnded = dateEnded;
    }

    public Game(Configuration configuration, User player) {
        id = -1L;
        this.configuration = configuration;
        this.player = player;
        this.dateStarted = LocalDateTime.now();
        points = 0;
        guesses = new ArrayList<>();
        correctGuesses = 0;
        dateStarted = LocalDateTime.now();
    }

    private void addGuess(Guess newGuess) {
        guesses.add(newGuess);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public List<Guess> getGuesses() {
        return guesses;
    }

    public void setGuesses(List<Guess> guesses) {
        this.guesses = guesses;
    }

    public Integer getCorrectGuesses() {
        return correctGuesses;
    }

    public void setCorrectGuesses(Integer correctGuesses) {
        this.correctGuesses = correctGuesses;
    }

    public LocalDateTime getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(LocalDateTime dateStarted) {
        this.dateStarted = dateStarted;
    }

    public LocalDateTime getDateEnded() {
        return dateEnded;
    }

    public void setDateEnded(LocalDateTime dateEnded) {
        this.dateEnded = dateEnded;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
