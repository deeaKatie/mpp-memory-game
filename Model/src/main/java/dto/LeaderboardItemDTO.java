package dto;

import model.HasId;

import javax.persistence.criteria.CriteriaBuilder;

public class LeaderboardItemDTO implements HasId<Long> {

    private String username;
    private Integer noOfPoints;
    private Long gameTimeSeconds;

    public LeaderboardItemDTO(String username, Integer noOfPoints, Long gameTimeSeconds) {
        this.username = username;
        this.noOfPoints = noOfPoints;
        this.gameTimeSeconds = gameTimeSeconds;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getNoOfPoints() {
        return noOfPoints;
    }

    public void setNoOfPoints(Integer noOfPoints) {
        this.noOfPoints = noOfPoints;
    }

    public Long getGameTimeSeconds() {
        return gameTimeSeconds;
    }

    public void setGameTimeSeconds(Long gameTimeSeconds) {
        this.gameTimeSeconds = gameTimeSeconds;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long aLong) {

    }


    @Override
    public String toString() {
        return username + " got " + noOfPoints + " points in " + gameTimeSeconds + "seconds";
    }
}
