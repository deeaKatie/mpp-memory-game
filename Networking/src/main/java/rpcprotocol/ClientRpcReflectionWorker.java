package rpcprotocol;

import dto.GuessDTO;
import dto.LeaderboardDTO;
import model.Game;
import model.Guess;
import model.User;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class ClientRpcReflectionWorker implements Runnable, IObserver {
    private IServices service;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    private static Response okResponse = (new Response.Builder().type(ResponseType.OK)).build();

    public ClientRpcReflectionWorker(IServices service, Socket connection) {
        this.service = service;
        this.connection = connection;

        try {
            this.output = new ObjectOutputStream(connection.getOutputStream());
            this.output.flush();
            this.input = new ObjectInputStream(connection.getInputStream());
            this.connected = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run() {
        while (this.connected) {
            try {
                Object request = this.input.readObject();
                System.out.println(request);
                Response response = this.handleRequest((Request) request);
                System.out.println(response);
                if (response != null) {
                    this.sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        try {
            this.input.close();
            this.output.close();
            this.connection.close();
        } catch (IOException ex) {
            System.out.println("Error " + ex);
        }
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        this.output.writeObject(response);
        this.output.flush();
    }

    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + request.type();
        System.out.println("HandlerName " + handlerName);

        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            ex.printStackTrace();
        }

        return response;
    }

    private Response handleLOGIN(Request request) {
        System.out.println("WORKER -> LOGIN");
        User user = (User) request.data();

        try {
            User newUser = this.service.checkLogIn(user, this);
            return (new Response.Builder()).type(ResponseType.OK).data(newUser).build();
        } catch (ServiceException ex) {
            this.connected = false;
            return (new Response.Builder()).type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request) {
        System.out.println("WORKER -> LOGOUT");
        User user = (User) request.data();

        try {
            this.service.logout(user);
            this.connected = false;
            System.out.println("WORKER -> log out");
            return (new Response.Builder()).type(ResponseType.OK).build();
        } catch (ServiceException ex) {
            return (new Response.Builder()).type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }

    private Response handleFETCH_LEADERBOARD(Request request) {
        System.out.println("WORKER -> FETCH_LEADERBOARD");
        try {
            LeaderboardDTO leaderboardDTO = service.fetchLeaderboard();
            return (new Response.Builder()).type(ResponseType.OK).data(leaderboardDTO).build();
        } catch (Exception ex) {
            return (new Response.Builder()).type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }

    private Response handleGUESS_MADE(Request request) {
        System.out.println("WORKER -> GUESS_MADE");
        try {
            service.guessMade((GuessDTO) request.data());
            return (new Response.Builder()).type(ResponseType.OK).build();
        } catch (Exception ex) {
            return (new Response.Builder()).type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }

    @Override
    public void correctGuess(GuessDTO guessDTO) {
        System.out.println("WORKER -> CORECT_GUESS");
        try {
            sendResponse((new Response.Builder()).type(ResponseType.CORRECT_GUESS).data(guessDTO).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void wrongGuess(GuessDTO guessDTO) {
        System.out.println("WORKER -> WRONG_GUESS");
        try {
            sendResponse((new Response.Builder()).type(ResponseType.WRONG_GUESS).data(guessDTO).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void wonGame(Game game) {
        System.out.println("WORKER -> GAME_END_WON");
        try {
            sendResponse((new Response.Builder()).type(ResponseType.GAME_END_WON).data(game).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lostGame(Game game) {
        System.out.println("WORKER -> GAME_END_LOST");
        try {
            sendResponse((new Response.Builder()).type(ResponseType.GAME_END_LOST).data(game).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLeaderboard(LeaderboardDTO leaderboardDTO) {
        System.out.println("WORKER -> LEADERBOARD UPDATE");
        try {
        sendResponse((new Response.Builder()).type(ResponseType.LEADERBOARD_UPDATE).data(leaderboardDTO).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
