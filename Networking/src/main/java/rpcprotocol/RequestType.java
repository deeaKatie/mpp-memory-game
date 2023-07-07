package rpcprotocol;

public enum RequestType {
    LOGIN,
    FETCH_LEADERBOARD,
    GUESS_MADE,
    LOGOUT;
    private RequestType() {
    }
}
