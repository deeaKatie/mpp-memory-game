package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "guesses")
public class Guess implements HasId<Long> {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    private Integer position1;
    private Integer position2;

    public Guess() {
    }
    public Guess(Long id, Integer position1, Integer position2) {
        this.id = id;
        this.position1 = position1;
        this.position2 = position2;
    }

    public Guess(Integer position1, Integer position2) {
        this.position1 = position1;
        this.position2 = position2;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition1() {
        return position1;
    }

    public void setPosition1(Integer position1) {
        this.position1 = position1;
    }

    public Integer getPosition2() {
        return position2;
    }

    public void setPosition2(Integer position2) {
        this.position2 = position2;
    }
}
