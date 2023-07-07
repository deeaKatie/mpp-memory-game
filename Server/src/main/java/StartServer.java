import model.User;
import model.Word;
import repository.*;
import service.Service;
import services.IServices;
import utils.AbstractServer;
import utils.RpcConcurrentServer;
import utils.ServerException;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {


        //todo get rid of this if you want
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartServer.class.getResourceAsStream("server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException var21) {
            System.err.println("Cannot find server.properties " + var21);
            return;
        }

        IUserRepository userRepository = new UserDBRepository();
        IGameDBRepository gameDBRepository = new GameDBRepository();
        IWordDBRepository wordDBRepository = new WordDBRepository();
        IConfigurationDBRepository configurationDBRepository = new ConfigurationDBRepository();
        IGuessDBRepository guessDBRepository = new GuessDBRepository();


        // add words
//        Word w1 = new Word("apple");
//        Word w2 = new Word("pear");
//        Word w3 = new Word("juice");
//        Word w4 = new Word("venom");
//        Word w5 = new Word("bottle");
//        Word w6 = new Word("box");
//        Word w7 = new Word("pen");
//        Word w8 = new Word("cookie");
//        Word w9 = new Word("controller");
//        Word w10 = new Word("mouse");
//
//        wordDBRepository.add(w1);
//        wordDBRepository.add(w2);
//        wordDBRepository.add(w3);
//        wordDBRepository.add(w4);
//        wordDBRepository.add(w5);
//        wordDBRepository.add(w6);
//        wordDBRepository.add(w7);
//        wordDBRepository.add(w8);
//        wordDBRepository.add(w9);
//        wordDBRepository.add(w10);
//
//        User u1 = new User("ana", "ana");
//        User u2 = new User("bob", "bob");
//        User u3 = new User("alex", "alex");
//        User u4 = new User("mitro", "mitro");
//
//        userRepository.add(u1);
//        userRepository.add(u2);
//        userRepository.add(u3);
//        userRepository.add(u4);

        IServices service = new Service(userRepository, gameDBRepository,
                wordDBRepository, configurationDBRepository,
                guessDBRepository);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong  Port Number" + ex.getMessage());
            System.err.println("Using default port " + defaultPort);
        }

        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new RpcConcurrentServer(serverPort, service);
        try {
            server.start();
        } catch (ServerException ex) {
            System.err.println("Error starting the server" + ex.getMessage());
        } finally {
            try {
                server.stop();
            } catch (ServerException ex) {
                System.err.println("Error stopping server " + ex.getMessage());
            }

        }
    }
}
