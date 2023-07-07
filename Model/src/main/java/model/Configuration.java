package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "configurations")
public class Configuration implements HasId<Long> {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    @OneToMany
    private List<Word> words;

    public Configuration() {
        words = new ArrayList<>();
    }
    public Configuration(Long id, List<Word> words) {
        this.id = id;
        this.words = words;
    }

    public Configuration(List<Word> words) {
        this.words = words;
    }

    public void addWord(Word word) {
        words.add(word);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }
}
