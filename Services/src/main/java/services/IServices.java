package services;

import dto.GuessDTO;
import dto.LeaderboardDTO;
import model.Guess;
import model.User;

public interface IServices {
    User checkLogIn(User user,IObserver client) throws ServiceException;
    void logout(User user) throws ServiceException;

    LeaderboardDTO fetchLeaderboard() throws ServiceException;

    void guessMade(GuessDTO guessdto) throws ServiceException;
}
