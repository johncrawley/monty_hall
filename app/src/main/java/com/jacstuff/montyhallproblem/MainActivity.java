package com.jacstuff.montyhallproblem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private List<View> doors;
    private View door1Border, door2Border, door3Border;
    private List<View> doorBorders;
    private Button playAgainButton, resetStatsButton;
    private TextView statisticsTextView;
    private TextView statusTextView;
    private Statistics statistics;
    private Mode mode;
    private int prizeNumber = 1;
    private int selectedDoorNumber = 0;
    private enum Mode { SELECT_CHOICE, CONFIRM_CHOICE, RESULT}
    boolean hasSelectionSwitched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statistics = new Statistics();
        initViews();
        mode = Mode.SELECT_CHOICE;

    }

    private void initViews(){
        initDoorBorders();
        initDoors();

        playAgainButton = setupButton(R.id.new_game_button, this::startNewGame);
        resetStatsButton = setupButton(R.id.reset_stats_button, this::resetStats);
        statisticsTextView = findViewById(R.id.statistics_text);
        statusTextView = findViewById(R.id.statusText);
    }


    private void initDoorBorders(){
        door1Border = findViewById(R.id.door1Border);
        door2Border = findViewById(R.id.door2Border);
        door3Border = findViewById(R.id.door3Border);
        doorBorders = Arrays.asList(door1Border, door2Border, door3Border);
        deselectAllBorders();
    }


    private void initDoors(){
        doors = new ArrayList<>(3);
        doors.add(setupDoor(R.id.door1, door1Border, 1));
        doors.add(setupDoor(R.id.door2, door2Border, 2));
        doors.add(setupDoor(R.id.door3, door3Border, 3));
    }


    private View setupDoor(int doorId, View doorBorder, int tag){
        ImageView view = findViewById(doorId);
        view.setTag(tag);
        view.setOnClickListener(v -> onClickDoor(view, doorBorder));
        return view;
    }


    private Button setupButton(int buttonId, Runnable runnable){
        Button button = findViewById(buttonId);
        button.setOnClickListener((v)->runnable.run());
        return button;
    }


    private void onClickDoor(ImageView doorView, View borderView){
        setSelected(borderView);
        selectDoor(doorView);
    }


    private void setSelected(View borderView){
        deselectAllBorders();
        borderView.setBackgroundColor(getColor(R.color.selectedDoorBorder));
    }


    private void startNewGame(){
        assignPrizeIndex();
        resetDoors();
        resetStatusText();
        mode = Mode.SELECT_CHOICE;
        playAgainButton.setVisibility(View.INVISIBLE);
    }


    private void resetStats(){
        statistics.reset();
        resetStatsButton.setVisibility(View.INVISIBLE);
        updateStatisticsView();
    }


    private void assignPrizeIndex(){
        prizeNumber = 1 + new Random(System.currentTimeMillis()).nextInt(3);
    }


    private void selectDoor(View view){
        switch (mode){
            case SELECT_CHOICE: initialChoice(view);break;
            case CONFIRM_CHOICE: confirmChoice(view);break;
            case RESULT: break;
        }
    }


    private void initialChoice(View view){
        hasSelectionSwitched = false;
        if(view.isEnabled()){
            selectedDoorNumber = (int) view.getTag();
            mode = Mode.CONFIRM_CHOICE;
            displayConfirmChoiceText();
            reduceChoices();
        }
    }


    private void confirmChoice(View view){
        int newSelection = (int)view.getTag();
        if(newSelection != selectedDoorNumber){
            hasSelectionSwitched = true;
        }
        selectedDoorNumber = newSelection;
        openDoor(view);
        calculateAndDisplayResult();
        playAgainButton.setVisibility(View.VISIBLE);
        setDoorsEnabled(false);
    }


    private void openDoor(View view){
        if(prizeNumber == (int)view.getTag()){
            changeImageToPrize(view);
        }
        else{
            changeImageToGoat(view);
        }
    }


    private void changeImageToPrize(View view){
        ImageView iv = (ImageView)view;
        iv.setImageResource(R.drawable.door_car);
    }


    private void changeImageToGoat(View view){
        ImageView iv = (ImageView)view;
        iv.setImageResource(R.drawable.door_goat);
    }


    private void reduceChoices(){
        List<View> doors = new ArrayList<>(this.doors);
        removeDoorSelectedByUserFrom(doors);
        if(selectedDoorNumber == prizeNumber){
            openOneOfTheOtherDoors(doors);
            return;
        }
        disableViewsThatDoNotContainPrize(doors);
    }


    private void openOneOfTheOtherDoors(List<View> doors){
        int viewIndexToDisable = new Random(System.currentTimeMillis()).nextInt(2);
        View doorToOpen = doors.get(viewIndexToDisable);
        doorToOpen.setEnabled(false);
        changeImageToGoat(doorToOpen);
    }


    private void removeDoorSelectedByUserFrom(List<View> choices){
        for(int i =0; i< choices.size(); i++){
            if((int)choices.get(i).getTag() == selectedDoorNumber){
                choices.remove(i);
                break;
            }
        }
    }


    private void disableViewsThatDoNotContainPrize(List<View> views){
        views.forEach((v)->{
            if((int)v.getTag() != prizeNumber){
                v.setEnabled(false);
                changeImageToGoat(v);
            }
        } );
    }


    private void calculateAndDisplayResult(){
        mode = Mode.RESULT;
        if(selectedDoorNumber == prizeNumber){
            displayWinMessage();
            statistics.addGameWon();
            statistics.addGameWon(hasSelectionSwitched);
            updateStatisticsView();
            return;
        }
        displayLoseMessage();
        statistics.addGameLost();
        statistics.addGameLost(hasSelectionSwitched);
        updateStatisticsView();
    }


    private void updateStatisticsView(){
        String msg = "Success after switching choice: " + statistics.getSwitchChoiceStats()
                + "\n Success after keeping choice: " + statistics.getKeepChoiceStats();
        statisticsTextView.setText(msg);
        resetStatsButton.setVisibility(View.VISIBLE);
    }


    private void displayWinMessage(){
        statusTextView.setText(R.string.success_status_text);
    }


    private void displayLoseMessage(){
        statusTextView.setText(R.string.fail_status_text);
    }


    private void resetStatusText(){
        statusTextView.setText(R.string.initial_status_text);
    }


    private void displayConfirmChoiceText(){
        statusTextView.setText(R.string.confirm_choice_status_text);
    }


    private void resetDoors(){
        deselectAllBorders();
        doors.forEach(this::resetDoor);
        setDoorsEnabled(true);
    }


    private void deselectAllBorders(){
        doorBorders.forEach(v -> v.setBackgroundColor(getColor(R.color.backgroundColor)));
    }


    private void setDoorsEnabled(boolean enabled){
        doors.forEach(door -> door.setEnabled(enabled));
    }


    private void resetDoor(View view){
        view.setEnabled(true);
        ImageView iv = (ImageView)view;
        iv.setImageResource(R.drawable.door);
    }


}