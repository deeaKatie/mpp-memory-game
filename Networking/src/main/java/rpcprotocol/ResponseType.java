package rpcprotocol;

public enum ResponseType {
    OK,
    ERROR,
    CORRECT_GUESS,
    WRONG_GUESS,
    GAME_END_WON,
    GAME_END_LOST,
    LEADERBOARD_UPDATE;
    private ResponseType() {
    }
}
