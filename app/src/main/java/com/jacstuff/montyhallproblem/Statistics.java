package com.jacstuff.montyhallproblem;

public class Statistics {


    private int gamesPlayed, gamesWon, gamesLost;

    void addGameWon(){
        gamesPlayed++;
        gamesWon++;
    }

    void addGameLost(){
        gamesPlayed++;
        gamesLost++;
    }

    void reset(){
        gamesPlayed = 0;
        gamesWon = 0;
        gamesLost = 0;
    }


    int getGamesPlayed(){
        return gamesPlayed;
    }

    int getGamesWon(){
        return gamesWon;
    }

    int getGamesLost(){
        return gamesLost;
    }
}
