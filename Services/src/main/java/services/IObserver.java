package services;

import dto.GuessDTO;
import dto.LeaderboardDTO;
import model.Game;

public interface IObserver {
    void correctGuess(GuessDTO guessDTO);

    void wrongGuess(GuessDTO guessDTO);

    void wonGame(Game game);

    void lostGame(Game game);

    void updateLeaderboard(LeaderboardDTO leaderboardDTO);

//    void gameStarted(PlayersDTO players) throws ServiceException;
//
//    void sendToWaitingRoom();
//
//    void roundFinished(RoundEndDTO round);
//
//    void gameFinished(String userStatus);
}
