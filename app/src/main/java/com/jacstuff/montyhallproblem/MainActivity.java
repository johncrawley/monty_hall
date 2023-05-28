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

public class MainActivity extends AppCompatActivity implements MainView {

    private List<View> doors;
    private View door1Border, door2Border, door3Border;
    private List<View> doorBorders;
    private Button playAgainButton, resetStatsButton;
    private TextView statisticsTextView, keptChoiceStatsTextView, switchedChoiceStatsTextView;
    private TextView statusTextView;
    private MontyHallGame montyHallGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        montyHallGame = new MontyHallGame(this);
        initViews();
    }


    private void initViews(){
        initDoorBorders();
        initDoors();
        playAgainButton = setupButton(R.id.new_game_button, this::startNewGame);
        resetStatsButton = setupButton(R.id.reset_stats_button, this::resetStats);
        statisticsTextView = findViewById(R.id.statistics_text);
        keptChoiceStatsTextView = findViewById(R.id.keep_stat_wins_text);
        switchedChoiceStatsTextView = findViewById(R.id.switch_stat_wins_text);
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
        doors.add(setupDoor(R.id.door1, door1Border, 0));
        doors.add(setupDoor(R.id.door2, door2Border, 1));
        doors.add(setupDoor(R.id.door3, door3Border, 2));
    }


    private View setupDoor(int doorId, View doorBorder, int index){
        ImageView view = findViewById(doorId);
        view.setOnClickListener(v -> onClickDoor(doorBorder, index));
        return view;
    }


    private Button setupButton(int buttonId, Runnable runnable){
        Button button = findViewById(buttonId);
        button.setOnClickListener((v) -> runnable.run());
        return button;
    }


    private void onClickDoor(View borderView, int index){
        setSelected(borderView);
        montyHallGame.selectDoor(index);
    }


    private void setSelected(View borderView){
        deselectAllBorders();
        borderView.setBackgroundColor(getColor(R.color.selectedDoorBorder));
    }


    private void startNewGame(){
        montyHallGame.startNewGame();
        resetDoors();
        resetStatusText();
        playAgainButton.setVisibility(View.INVISIBLE);
    }



    private void resetStats(){
        montyHallGame.resetStats();
        resetStatsButton.setVisibility(View.INVISIBLE);
    }


    private void changeImageToPrize(View view){
        ImageView iv = (ImageView)view;
        iv.setImageResource(R.drawable.door_car);
    }


    private void changeImageToGoat(View view){
        ImageView iv = (ImageView)view;
        iv.setImageResource(R.drawable.door_goat);
    }


    @Override
    public void openDoor(int doorIndex, boolean containsPrize){
        View door = doors.get(doorIndex);
        door.setEnabled(false);
        if(containsPrize){
            changeImageToPrize(door);
            return;
        }
        changeImageToGoat(door);
    }


    @Override
    public void displayGameResultMessage(boolean wasGameWon) {
        if(wasGameWon){
            displayWinMessage();
            return;
        }
        displayLoseMessage();
    }


    @Override
    public void updateStatistics(String switchChoiceStats, String keepChoiceStats){
        String msg = "Success after switching choice: " + switchChoiceStats
                + "\n Success after keeping choice: " + keepChoiceStats;
        keptChoiceStatsTextView.setText(keepChoiceStats);
        switchedChoiceStatsTextView.setText(switchChoiceStats);
        statisticsTextView.setText(msg);
        resetStatsButton.setVisibility(View.VISIBLE);
    }


    @Override
    public void setPlayButtonVisibility(boolean isVisible) {
        playAgainButton.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
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


    @Override
    public void displayConfirmChoiceText(){
        statusTextView.setText(R.string.confirm_choice_status_text);
    }


    @Override
    public void disableDoorAtIndex(int index) {
        doors.get(index).setEnabled(false);
    }


    private void resetDoors(){
        deselectAllBorders();
        doors.forEach(this::resetDoor);
        setDoorsEnabled(true);
    }


    private void deselectAllBorders(){
        doorBorders.forEach(v -> v.setBackgroundColor(getColor(R.color.backgroundColor)));
    }


    public void setDoorsEnabled(boolean enabled){
        doors.forEach(door -> door.setEnabled(enabled));
    }


    private void resetDoor(View view){
        view.setEnabled(true);
        ImageView iv = (ImageView)view;
        iv.setImageResource(R.drawable.door);
    }

}