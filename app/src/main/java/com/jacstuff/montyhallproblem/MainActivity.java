package com.jacstuff.montyhallproblem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button choice1Button, choice2Button, choice3Button;
    private TextView statisticsTextView;
    private TextView statusTextView;

    private enum Mode { SELECT_CHOICE, CONFIRM_CHOICE, RESULT}

    private Mode mode;
    private int prizeNumber = 1;
    private int selectedNumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statistics = new Statistics();
        initViews();
        mode = Mode.SELECT_CHOICE;
    }


    private void initViews(){

        choice1Button = setupButton(R.id.choice1button, 1);
        choice2Button =  setupButton(R.id.choice2button, 2);
        choice3Button = setupButton( R.id.choice3button, 3);

        Button playAgainButton = findViewById(R.id.new_game_button);
        Button resetStatsButton = findViewById(R.id.reset_stats_button);
        statisticsTextView = findViewById(R.id.statistics_text);
        statusTextView = findViewById(R.id.statusText);
        setOnClickListenerFor(choice1Button, choice2Button, choice3Button, playAgainButton, resetStatsButton);
    }

    private Button setupButton(int id, int tagValue){
        Button button = findViewById(id);
        button.setTag(tagValue);
        return button;
    }

    private void setOnClickListenerFor(View ... views){
        for(View v : views){
            v.setOnClickListener(this);
        }
    }

    public void onClick(View v){
        int id = v.getId();
        if(id == R.id.choice1button){
            selectChoice(choice1Button);
        }
        else if(id == R.id.choice2button){
            selectChoice(choice2Button);
        }
        else if(id == R.id.choice3button){
            selectChoice(choice3Button);
        }
        else if(id == R.id.new_game_button) {
            startNewGame();
        }
        else if(id == R.id.reset_stats_button){
            resetStats();
        }
    }

    private void startNewGame(){
        assignPrizeIndex();
        resetChoiceButtons();
        resetStatusText();
        mode = Mode.SELECT_CHOICE;
    }

    private Statistics statistics;

    private void resetStats(){
        statistics.reset();
        updateStatisticsView();
    }


    private void assignPrizeIndex(){
        prizeNumber = 1 + new Random(System.currentTimeMillis()).nextInt(3);
    }


    private void selectChoice(Button button){
        if( mode == Mode.SELECT_CHOICE){
            if(button.isEnabled()){
                selectedNumber = (int) button.getTag();
                mode = Mode.CONFIRM_CHOICE;
                displayConfirmChoiceText();
                reduceChoices();
            }
        }
        else if( mode == Mode.CONFIRM_CHOICE){
            selectedNumber = (int)button.getTag();
            mode = Mode.RESULT;
            calculateAndDisplayResult();
        }
    }


    private void reduceChoices(){
        List<Button> choices = new LinkedList<>(Arrays.asList(choice1Button, choice2Button, choice3Button));
        for(int i =0; i< choices.size(); i++){
            Button button = choices.get(i);
            if((int)button.getTag() == selectedNumber){
                choices.remove(i);
                break;
            }
        }

        if(selectedNumber == prizeNumber){
            int buttonIndexToDisable = new Random(System.currentTimeMillis()).nextInt(2);
            Button button = choices.get(buttonIndexToDisable);
            button.setEnabled(false);
            return;
        }
        disableButtonsThatDontContainPrize(choices);
    }


    private void disableButtonsThatDontContainPrize(List<Button> buttonList){
        for(int i = 0; i< buttonList.size(); i++){
            Button button = buttonList.get(i);
            if((int)button.getTag() != prizeNumber){
                button.setEnabled(false);
            }
        }
    }


    private void calculateAndDisplayResult(){
        if(selectedNumber == prizeNumber){
            displayWinMessage();
            statistics.addGameWon();
            updateStatisticsView();
            return;
        }
        displayLoseMessage();
        statistics.addGameLost();
        updateStatisticsView();
    }


    private void updateStatisticsView(){
        String msg = " Success : " + statistics.getGamesWon() + " / " + statistics.getGamesPlayed();
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

    private void resetChoiceButtons(){
        reset(choice1Button);
        reset(choice2Button);
        reset(choice3Button);

    }

    private void reset(Button button){
        button.setEnabled(true);
        button.setText(R.string.question_mark);
    }


}