package dto;

import model.HasId;

import javax.persistence.criteria.CriteriaBuilder;

public class LeaderboardItemDTO implements HasId<Long> {

    private String username;
    private Integer noOfPoints;
    private Integer gameTimeSeconds;

    public LeaderboardItemDTO(String username, Integer noOfPoints, Integer gameTimeSeconds) {
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

    public Integer getGameTimeSeconds() {
        return gameTimeSeconds;
    }

    public void setGameTimeSeconds(Integer gameTimeSeconds) {
        this.gameTimeSeconds = gameTimeSeconds;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long aLong) {

    }
}
