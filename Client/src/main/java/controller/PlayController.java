package controller;

import dto.GuessDTO;
import dto.LeaderboardDTO;
import dto.LeaderboardItemDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.*;
import services.IObserver;
import services.IServices;
import services.ServiceException;
import utils.MessageAlert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;


public class PlayController implements IObserver {

    ObservableList<LeaderboardItemDTO> modelLeaderboard = FXCollections.observableArrayList();
    private IServices service;
    private User loggedUser;

    @FXML
    Label usernameLabel;
    @FXML
    Label gameStatusLabel;
    @FXML
    Button logOutButton;
    @FXML
    ListView<LeaderboardItemDTO> leaderboardListView;
    @FXML
    Label leaderboardLabel;
    @FXML
    Label yourGameLabel;
    @FXML
    Button gameButton1;
    @FXML
    Button gameButton2;
    @FXML
    Button gameButton3;
    @FXML
    Button gameButton4;
    @FXML
    Button gameButton5;
    @FXML
    Button gameButton6;
    @FXML
    Button gameButton7;
    @FXML
    Button gameButton8;
    @FXML
    Button gameButton9;
    @FXML
    Button gameButton10;
    private Integer noOfClicks;
    private Integer positionButton1;
    private Integer positionButton2;
    private Button clickedButton1;
    private Button clickedButton2;
    private List<Button> notGuessedButtons;

    public void setService(IServices service) {
        this.service = service;
    }
    public void setUser(User user) {
        this.loggedUser = user;
    }
    public void initVisuals() {
        notGuessedButtons = new ArrayList<>();
        notGuessedButtons.add(gameButton1);
        notGuessedButtons.add(gameButton2);
        notGuessedButtons.add(gameButton3);
        notGuessedButtons.add(gameButton4);
        notGuessedButtons.add(gameButton5);
        notGuessedButtons.add(gameButton6);
        notGuessedButtons.add(gameButton7);
        notGuessedButtons.add(gameButton8);
        notGuessedButtons.add(gameButton9);
        notGuessedButtons.add(gameButton10);
        noOfClicks = 0;
        positionButton1 = 0;
        positionButton2 = 0;
        usernameLabel.setText("Hi, " + loggedUser.getUsername());
        gameStatusLabel.setVisible(false);
        hideAllButtons();
        try {
            LeaderboardDTO leaderboardDTO = service.fetchLeaderboard();
            modelLeaderboard.setAll(leaderboardDTO.getEntries());
            leaderboardListView.setItems(modelLeaderboard);
            System.out.println("CTRL -> Leaderboard received size: " + leaderboardDTO.getEntries().size());
        } catch (ServiceException ex) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error fetching data", ex.getMessage());
        }
    }

    @Override
    public void updateLeaderboard(LeaderboardDTO leaderboardDTO) {
        Platform.runLater(() ->{
                modelLeaderboard.setAll(leaderboardDTO.getEntries());
        leaderboardListView.setItems(modelLeaderboard);
        });

    }

    @FXML
    public void logOutHandler() throws IOException {
        System.out.println("Logging out!\n");
        try {
            service.logout(loggedUser);
        } catch (ServiceException ex) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error logging out", ex.getMessage());
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LogInView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) logOutButton.getScene().getWindow();
        LogInController logCtrl = fxmlLoader.getController();

        logCtrl.setService(service);
        stage.setScene(scene);
    }



    @FXML
    public void buttonPressed(ActionEvent actionEvent) {
        System.out.println("CRTL -> BTN CLICKED");
        noOfClicks++;
        if (noOfClicks == 2) {
            noOfClicks = 0;
            disableAllButtons();
            Button selectedButton = (Button) actionEvent.getSource();
            String buttonId = selectedButton.getId();
            positionButton2 = Integer.parseInt(buttonId.substring(10));
            clickedButton2 = selectedButton;
            System.out.println("pos1: " + positionButton1);
            System.out.println("pos2: " + positionButton2);

            Guess guess = new Guess(positionButton1, positionButton2);
            GuessDTO guessdto = new GuessDTO(loggedUser, guess, null, null);
            try {
                service.guessMade(guessdto);
            } catch (ServiceException ex) {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Error sending guess", ex.getMessage());
            }
        } else {
            // disable corespondant button
            Button selectedButton = (Button) actionEvent.getSource();
            selectedButton.setDisable(true);
            if (noOfClicks == 1) {
                String buttonId = selectedButton.getId();
                positionButton1 = Integer.parseInt(buttonId.substring(10));
                clickedButton1 = selectedButton;
            }
        }
    }

    @Override
    public void correctGuess(GuessDTO guessDTO) {
        Platform.runLater(() -> {
            gameStatusLabel.setText("Correct guess!");
            gameStatusLabel.setVisible(true);
            notGuessedButtons.remove(clickedButton1);
            notGuessedButtons.remove(clickedButton2);

            // show buttons text
            clickedButton1.setText(guessDTO.getWord1().getValue());
            clickedButton2.setText(guessDTO.getWord2().getValue());

            hideStatus();
        });

    }

    @Override
    public void wrongGuess(GuessDTO guessDTO) {
        Platform.runLater(() -> {
            gameStatusLabel.setText("Wrong guess!");
            gameStatusLabel.setVisible(true);

            // show buttons text
            clickedButton1.setText(guessDTO.getWord1().getValue());
            clickedButton2.setText(guessDTO.getWord2().getValue());

            hideStatus();
        });

    }

    @Override
    public void wonGame(Game game) {
        Platform.runLater(() -> {
            gameStatusLabel.setText("You won -> points: " + game.getPoints());
            gameStatusLabel.setVisible(true);

            // show buttons text
            notGuessedButtons.get(0).setText(game.getConfiguration().getWords().get(positionButton1-1).getValue());
            notGuessedButtons.get(1).setText(game.getConfiguration().getWords().get(positionButton2-1).getValue());
            notGuessedButtons.clear();


            showConfiguration(game.getConfiguration());
            hideStatus();
        });
    }

    @Override
    public void lostGame(Game game) {
        Platform.runLater(() -> {
            gameStatusLabel.setText("You lost -> points: " + game.getPoints());
            gameStatusLabel.setVisible(true);

            // show buttons text
            notGuessedButtons.get(0).setText(game.getConfiguration().getWords().get(positionButton1-1).getValue());
            notGuessedButtons.get(1).setText(game.getConfiguration().getWords().get(positionButton2-1).getValue());
            notGuessedButtons.clear();

            showConfiguration(game.getConfiguration());
            hideStatus();
        });
    }

    private void hideStatus() {
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                gameStatusLabel.setVisible(false);
                hideAllButtons();
                enableAllButtons();
            }
        }, 1000);
    }
    private void showConfiguration(Configuration config) {
        gameButton1.setText(config.getWords().get(0).getValue());
        gameButton2.setText(config.getWords().get(1).getValue());
        gameButton3.setText(config.getWords().get(2).getValue());
        gameButton4.setText(config.getWords().get(3).getValue());
        gameButton5.setText(config.getWords().get(4).getValue());
        gameButton6.setText(config.getWords().get(5).getValue());
        gameButton7.setText(config.getWords().get(6).getValue());
        gameButton8.setText(config.getWords().get(7).getValue());
        gameButton9.setText(config.getWords().get(8).getValue());
        gameButton10.setText(config.getWords().get(9).getValue());
    }

    private void hideAllButtons() {
        Platform.runLater(() -> {
            for (var btn : notGuessedButtons) {
                btn.setText("");
            }
        });
    }

    public void disableAllButtons() {
        gameButton1.setDisable(true);
        gameButton2.setDisable(true);
        gameButton3.setDisable(true);
        gameButton4.setDisable(true);
        gameButton5.setDisable(true);
        gameButton6.setDisable(true);
        gameButton7.setDisable(true);
        gameButton8.setDisable(true);
        gameButton9.setDisable(true);
        gameButton10.setDisable(true);
    }
    public void enableAllButtons() {
        Platform.runLater(() -> {
            for (var btn : notGuessedButtons) {
                btn.setDisable(false);
            }
        });
    }
}
