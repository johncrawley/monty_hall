package com.jacstuff.montyhallproblem;

public class Statistics {


    private int gamesPlayed, gamesWon, gamesLost;
    private int gamesPlayedWithKeepChoice, gamesWonWithKeepChoice;
    private int gamesPlayedWithSwitchChoice, gamesWonWithSwitchChoice;

    void addGameWon(){
        gamesPlayed++;
        gamesWon++;
    }

    void addGameWon(boolean wasChoiceSwitched){
        gamesPlayed++;
        if(wasChoiceSwitched){
            gamesPlayedWithSwitchChoice++;
            gamesWonWithSwitchChoice++;
            return;
        }
        gamesPlayedWithKeepChoice++;
        gamesWonWithKeepChoice++;
    }


    void addGameLost(boolean wasChoiceSwitched){
        gamesPlayed++;
        if(wasChoiceSwitched){
            gamesPlayedWithSwitchChoice++;
            return;
        }
        gamesPlayedWithKeepChoice++;
    }


    void addGameLost(){
        gamesPlayed++;
        gamesLost++;
    }

    void reset(){
        gamesPlayed = 0;
        gamesWon = 0;
        gamesLost = 0;
        gamesPlayedWithKeepChoice = 0;
        gamesWonWithKeepChoice = 0;
        gamesPlayedWithSwitchChoice = 0;
        gamesWonWithSwitchChoice = 0;
    }


    int getGamesPlayed(){
        return gamesPlayed;
    }

    int getGamesWon(){
        return gamesWon;
    }

    String getKeepChoiceStats(){
        return gamesWonWithKeepChoice + " / " + gamesPlayedWithKeepChoice;
    }


    String getSwitchChoiceStats(){
        return gamesWonWithSwitchChoice + " / " + gamesPlayedWithSwitchChoice;
    }

    int getGamesLost(){
        return gamesLost;
    }
}
