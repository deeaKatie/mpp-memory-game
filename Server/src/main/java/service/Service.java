package service;

import dto.GuessDTO;
import dto.LeaderboardDTO;
import dto.LeaderboardItemDTO;
import exception.RepositoryException;
import model.*;
import repository.*;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IServices {

    private IUserRepository userRepository;
    private IGameDBRepository gameDBRepository;
    private IWordDBRepository wordDBRepository;
    private IGuessDBRepository guessDBRepository;
    private IConfigurationDBRepository configurationDBRepository;
    private Map<Long, IObserver> loggedClients; // key - id , val - observer
    private Map<Long, Game> games; // the ongoing games
    private final int defaultThreadsNo = 5;
    // private Map.Entry<Long, IObserver> client;

    public Service(IUserRepository userRepository,
                   IGameDBRepository gameDBRepository,
                   IWordDBRepository wordDBRepository,
                   IConfigurationDBRepository configurationDBRepository,
                   IGuessDBRepository guessDBRepository) {
        this.userRepository = userRepository;
        this.gameDBRepository = gameDBRepository;
        this.wordDBRepository = wordDBRepository;
        this.configurationDBRepository = configurationDBRepository;
        this.guessDBRepository = guessDBRepository;
        this.loggedClients = new ConcurrentHashMap<>();
        this.games = new ConcurrentHashMap<>();
    }

    public synchronized User checkLogIn(User user, IObserver client) throws ServiceException {
        User userToFind;
        System.out.println("SERVER -> USER CHECK LOGIN: " + user);
        try {
            System.out.println("+++++++++++++++ B4 repo!\n");
            userToFind = userRepository.findUserByUsername(user.getUsername());

        } catch (RepositoryException re) {
            throw new ServiceException(re.getMessage());
        }
        if (loggedClients.containsKey(userToFind.getId())) {
            throw new ServiceException("User already logged in.");
        }
        if (Objects.equals(userToFind.getPassword(), user.getPassword())) {
            user.setId(userToFind.getId());
            loggedClients.put(user.getId(), client);
            // start a new game for the logged player
            startGame(user);
            return user;
        } else {
            throw new ServiceException("Incorrect Password");
        }
    }

    public Configuration generateConfiguration() {
        System.out.println("SERVER.generateConfiguration");
        ArrayList<Word> words = (ArrayList<Word>) wordDBRepository.getAll();
        Configuration configuration = new Configuration();

        Random random = new Random();
        int noOfElems = 5;

        while (noOfElems > 0) {
            noOfElems--;
            Word word = words.get(random.nextInt(words.size()));
            configuration.addWord(word);
            configuration.addWord(word);
        }
        Collections.shuffle(configuration.getWords());

        System.out.println("SREVER -> CONFIGURATION");
        for (var word : configuration.getWords()) {
            System.out.println(word.getValue());
        }
        System.out.println("END");

        return configuration;
    }

    public void startGame(User user) {
        System.out.println("SERVER.startGame");
        // start a new game for the logged player
        Configuration configuration = generateConfiguration();
        configuration = configurationDBRepository.add(configuration);

        // save the new game in DB
        Game game = new Game(configuration, user);
        gameDBRepository.add(game);

        // save the new game locally
        games.put(user.getId(), game);
    }

    @Override
    public synchronized void logout(User user) throws ServiceException {
        System.out.println("SERVER.logout");
        if (loggedClients.containsKey(user.getId())) {
            loggedClients.remove(user.getId());
            games.remove(user.getId());
        } else{
            throw new ServiceException("User not logged in");
        }
    }

    @Override
    public LeaderboardDTO fetchLeaderboard() throws ServiceException {
        System.out.println("SERVER.fetchLeaderboard");
        ArrayList<Game> games;
        try {
            games = (ArrayList<Game>) gameDBRepository.getAll();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        LeaderboardDTO leaderboardDTO = new LeaderboardDTO();
        for (var game : games) {
            if (Objects.equals(game.getGameStatus(), "finalized")) {
                Long gameTimeSeconds = Duration.between(game.getDateStarted(), game.getDateEnded()).getSeconds();
                LeaderboardItemDTO item = new LeaderboardItemDTO(game.getPlayer().getUsername(), game.getPoints(), gameTimeSeconds);
                leaderboardDTO.addEntry(item);
            }
        }
        leaderboardDTO.getEntries().sort(Comparator.comparing(LeaderboardItemDTO::getNoOfPoints).reversed());
        return leaderboardDTO;
    }

    @Override
    public void guessMade(GuessDTO guessdto) throws ServiceException {
        System.out.println("SERVER -> Guess made");
        //save guess
        Guess guess = guessdto.getGuess();
        guess = guessDBRepository.add(guess);

        Game game = games.get(guessdto.getUser().getId());
        game.getGuesses().add(guess);

        //check if guess correct
        Configuration config = game.getConfiguration();
        Integer pos1 = guess.getPosition1();
        Integer pos2 = guess.getPosition2();
        String guessStatus = "";

        // PROCCESS THIS MOVE
        Boolean correctMove = false;
        guessdto.setWord1(config.getWords().get(pos1 - 1));
        guessdto.setWord2(config.getWords().get(pos2 - 1));
        if (config.getWords().get(pos1 - 1) == config.getWords().get(pos2 - 1)) {
            // correct guess
            System.out.println("SRV -> Correct guess");
            // add points
            game.setPoints(game.getPoints() + 3);
            // update correct guesses
            System.out.println("SRV -> Correct guesses b4: " + game.getCorrectGuesses());
            game.setCorrectGuesses(game.getCorrectGuesses() + 1);
            System.out.println("SRV -> Correct guesses after: " + game.getCorrectGuesses());
            correctMove = true;
        } else {
            // wrong guess
            System.out.println("SRV -> Wrong guess");
            // remove points
            game.setPoints(game.getPoints() - 2);
        }


        // game ended
        if (game.getCorrectGuesses() == 5 || game.getGuesses().size() == 10) {
            System.out.println("SRV -> Game ended");
            // update time
            game.setDateEnded(LocalDateTime.now());
            game.setGameFinished();
            if (game.getCorrectGuesses() == 5) {
                // game won
                var client = loggedClients.get(guessdto.getUser().getId());
                client.wonGame(game);
            } else if (game.getGuesses().size() == 10) {
                // game lost
                var client = loggedClients.get(guessdto.getUser().getId());
                client.lostGame(game);
            }

            // updateLeaderboard for everyone
            for (var client : loggedClients.entrySet()) {
                client.getValue().updateLeaderboard(fetchLeaderboard());
            }

        } else {
            // game still going
            System.out.println("SRV -> Game still going");

            var client = loggedClients.get(guessdto.getUser().getId());
            if (correctMove) {
                System.out.println("SRV -> Sending to worker");
                client.correctGuess(guessdto);
            } else {
                System.out.println("SRV -> Sending to worker");
                client.wrongGuess(guessdto);
            }
        }

        System.out.println("SRV -> Updating game");
        gameDBRepository.update(game, 0L);
        System.out.println("SRV -> Updated");
    }
}
