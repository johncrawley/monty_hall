package com.jacstuff.montyhallproblem;

public class Statistics {

    private int gamesPlayedWithKeepChoice, gamesWonWithKeepChoice;
    private int gamesPlayedWithSwitchChoice, gamesWonWithSwitchChoice;


    public void addGameResult(boolean wasGameWon, boolean wasChoiceSwitched){
        if(wasGameWon){
            addGameWon(wasChoiceSwitched);
            return;
        }
        addGameLost(wasChoiceSwitched);
    }


    void addGameWon(boolean wasChoiceSwitched){
        if(wasChoiceSwitched){
            gamesPlayedWithSwitchChoice++;
            gamesWonWithSwitchChoice++;
            return;
        }
        gamesPlayedWithKeepChoice++;
        gamesWonWithKeepChoice++;
    }


    void addGameLost(boolean wasChoiceSwitched){
        if(wasChoiceSwitched){
            gamesPlayedWithSwitchChoice++;
            return;
        }
        gamesPlayedWithKeepChoice++;
    }


    void reset(){
        gamesPlayedWithKeepChoice = 0;
        gamesWonWithKeepChoice = 0;
        gamesPlayedWithSwitchChoice = 0;
        gamesWonWithSwitchChoice = 0;
    }


    String getKeepChoiceStats(){
        return gamesWonWithKeepChoice + " / " + gamesPlayedWithKeepChoice;
    }


    String getSwitchChoiceStats(){
        return gamesWonWithSwitchChoice + " / " + gamesPlayedWithSwitchChoice;
    }

}
