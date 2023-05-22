package com.jacstuff.montyhallproblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MontyHallGame {

    private final Statistics statistics;
    private enum Mode { SELECT_CHOICE, CONFIRM_CHOICE, RESULT}
    private List<Door> doors;
    private Mode mode;
    private int doorIndexWithPrize = 1;
    private int selectedDoorNumber = 0;
    boolean hasSelectionSwitched;
    private final MainView view;


    public MontyHallGame(MainView view){
        statistics = new Statistics();
        mode = Mode.SELECT_CHOICE;
        this.view = view;
        startNewGame();
    }


    public void startNewGame(){
        assignPrizeToDoor();
        mode = Mode.SELECT_CHOICE;
    }


    public void selectDoor(int doorNumber){
        switch (mode){
            case SELECT_CHOICE: initialChoice(doorNumber);break;
            case CONFIRM_CHOICE: confirmChoice(doorNumber);break;
            case RESULT: break;
        }
    }


    public void resetStats(){
        statistics.reset();
        updateStatisticsView();
    }


    private void initialChoice(int doorIndexChosenByUser){
        hasSelectionSwitched = false;
        selectedDoorNumber = doorIndexChosenByUser;
        view.displayConfirmChoiceText();
        reduceChoices(doorIndexChosenByUser);
        mode = Mode.CONFIRM_CHOICE;
    }


    private void confirmChoice(int confirmedDoorIndex){
        if(confirmedDoorIndex != selectedDoorNumber){
            hasSelectionSwitched = true;
        }
        selectedDoorNumber = confirmedDoorIndex;
        view.openDoor(confirmedDoorIndex, doors.get(confirmedDoorIndex).containsPrize());
        calculateAndDisplayResult();
        view.setPlayButtonVisibility(true);
        view.setDoorsEnabled(false);
    }


    private void calculateAndDisplayResult(){
        mode = Mode.RESULT;
        boolean wasGameWon = selectedDoorNumber == doorIndexWithPrize;
        statistics.addGameResult(wasGameWon, hasSelectionSwitched);
        view.displayGameResultMessage(wasGameWon);
        updateStatisticsView();
    }


    private void updateStatisticsView(){
        view.updateStatistics(statistics.getSwitchChoiceStats(), statistics.getKeepChoiceStats());
    }


    private void reduceChoices(int doorIndexChosenByUser){
        List<Door> doors = new ArrayList<>(this.doors);
        removeDoorSelectedByUserFrom(doors);
        if(doorIndexChosenByUser == doorIndexWithPrize){
            openOneOfTheOtherDoors(doors);
            return;
        }
        openRemainingDoorsThatDoNotContainPrize(doors);
    }


    private void openOneOfTheOtherDoors(List<Door> doors){
        int viewIndexToDisable = new Random(System.currentTimeMillis()).nextInt(doors.size());
        Door doorToOpen = doors.get(viewIndexToDisable);
        view.openDoor(doorToOpen.getIndex(), false);
        doorToOpen.open();
    }


    private void removeDoorSelectedByUserFrom(List<Door> doors){
        doors.remove(selectedDoorNumber);
    }


    private void openRemainingDoorsThatDoNotContainPrize(List<Door> doors){
        doors.forEach(door -> {
            if(!door.containsPrize()){
                view.openDoor(door.getIndex(), false);
            }
        });
    }


    private void assignPrizeToDoor(){
        int NUMBER_OF_DOORS = 3;
        doorIndexWithPrize = new Random(System.currentTimeMillis()).nextInt(NUMBER_OF_DOORS);
        doors = new ArrayList<>();
        for(int i = 0; i< NUMBER_OF_DOORS; i++){
            boolean containsPrize = i  == doorIndexWithPrize;
            doors.add(new Door(i,containsPrize));
        }
    }


}
