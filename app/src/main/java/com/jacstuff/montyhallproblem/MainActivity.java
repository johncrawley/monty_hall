package com.jacstuff.montyhallproblem;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private View door1, door2, door3;
    private View door1Border, door2Border, door3Border;
    private Button playAgainButton;
    private TextView statisticsTextView;
    private TextView statusTextView;
    private Statistics statistics;
    private Mode mode;
    private int prizeNumber = 1;
    private int selectedDoorNumber = 0;
    private enum Mode { SELECT_CHOICE, CONFIRM_CHOICE, RESULT}
    private List<View> doorBorders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statistics = new Statistics();
        initViews();
        mode = Mode.SELECT_CHOICE;

    }

    private void initViews(){

        door1Border = findViewById(R.id.door1Border);
        door2Border = findViewById(R.id.door2Border);
        door3Border = findViewById(R.id.door3Border);
        doorBorders = Arrays.asList(door1Border, door2Border, door3Border);
        deselectAllBorders();

        door1 = setupDoor(R.id.door1, door1Border, 1);
        door2 = setupDoor(R.id.door2, door2Border, 2);
        door3 = setupDoor(R.id.door3, door3Border, 3);



        playAgainButton = findViewById(R.id.new_game_button);
        playAgainButton.setOnClickListener(v -> startNewGame());
        Button resetStatsButton = findViewById(R.id.reset_stats_button);
        resetStatsButton.setOnClickListener(v -> resetStats());
        statisticsTextView = findViewById(R.id.statistics_text);
        statusTextView = findViewById(R.id.statusText);
    }


    private void deselectAllBorders(){
        doorBorders.forEach(v -> v.setBackgroundColor(getColor(R.color.backgroundColor)));
    }


    private View setupDoor(int doorId, View doorBorder, int tag){
        ImageView view = findViewById(doorId);
        view.setTag(tag);
        view.setOnClickListener(v -> onClickDoor(view, doorBorder));
        return view;
    }


    private void onClickDoor(ImageView doorView, View borderView){
        setSelected(borderView);
        selectChoice(doorView);
    }


    private void setSelected(View borderView){
        deselectAllBorders();
        borderView.setBackgroundColor(Color.BLUE);
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
        updateStatisticsView();
    }


    private void assignPrizeIndex(){
        prizeNumber = 1 + new Random(System.currentTimeMillis()).nextInt(3);
    }

    boolean hasSelectionSwitched;

    private void selectChoice(View view){
        if( mode == Mode.SELECT_CHOICE){
            hasSelectionSwitched = false;
            if(view.isEnabled()){
                selectedDoorNumber = (int) view.getTag();
                mode = Mode.CONFIRM_CHOICE;
                displayConfirmChoiceText();
                reduceChoices();
            }
        }
        else if( mode == Mode.CONFIRM_CHOICE){
            int newSelection = (int)view.getTag();
            if(newSelection != selectedDoorNumber){
                hasSelectionSwitched = true;
            }
            selectedDoorNumber = newSelection;
            if(prizeNumber == (int)view.getTag()){
                changeDoorToPrize(view);
            }
            else{
                changeDoorToFail(view);
            }
            mode = Mode.RESULT;
            calculateAndDisplayResult();
            playAgainButton.setVisibility(View.VISIBLE);
        }
    }


    private void changeDoorToPrize(View view){
        ImageView iv = (ImageView)view;
        iv.setImageResource(R.drawable.door_car);
    }


    private void changeDoorToFail(View view){
        ImageView iv = (ImageView)view;
        iv.setImageResource(R.drawable.door_goat);
    }


    private void reduceChoices(){
        List<View> doors = new LinkedList<>(Arrays.asList(door1, door2, door3));

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
        changeDoorToFail(doorToOpen);
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
                changeDoorToFail(v);
            }
        } );
    }


    private void calculateAndDisplayResult(){
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
        reset(door1);
        reset(door2);
        reset(door3);
    }


    private void reset(View view){
        view.setEnabled(true);
        ImageView iv = (ImageView)view;
        iv.setImageResource(R.drawable.door);
    }


}