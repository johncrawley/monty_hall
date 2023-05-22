package com.jacstuff.montyhallproblem;

public interface MainView {

    void displayConfirmChoiceText();
    void openDoor(int doorIndex, boolean isPrizeBehindDoor);
    void displayGameResultMessage(boolean wasGameWon);
    void updateStatistics(String switchChoiceStats, String keepChoiceStats);
    void setPlayButtonVisibility(boolean isVisible);
    void setDoorsEnabled(boolean isEnabled);
    void disableDoorAtIndex(int index);
}
