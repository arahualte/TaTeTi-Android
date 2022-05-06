package com.example.tateti_distribuidas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean isPlayerOneTurn = true;
    private int roundCounter = 0;
    private int pointsPlayerOne;
    private int pointsPlayerTwo;

    private TextView textPointsPlayerOne;
    private TextView textPointsPlayerTwo;
    private String p1Draw = new String();
    private String p2Draw = new String();
    private String enabledMachine = new String();
    private int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textPointsPlayerOne = findViewById(R.id.text_p1);
        textPointsPlayerTwo = findViewById(R.id.text_p2);

        String drawSelected = getIntent().getStringExtra("draw");
        enabledMachine = getIntent().getStringExtra("enabledMachine");
        p1Draw = setP1DrawToPlay(p1Draw,drawSelected);
        p2Draw = setP2DrawToPlay(p1Draw);

        updatePlayerOneName();

        if (enabledMachine.equals("disable")){
            playerVsPlayer();
        } else {
            playerVsMachine();
        }

        Button resetBtn = findViewById(R.id.reset_button);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (!((Button) v).getText().toString().equals("")){
            return;
        }
        if (isPlayerOneTurn){
            ((Button) v).setText(p1Draw);
        } else {
            ((Button) v).setText(p2Draw);
        }

        roundCounter++;

        if (checkWinPositions()){
            if (isPlayerOneTurn){
                playerOneWins();
            } else {
                playerTwoWins();
            }
        } else if (roundCounter == 9){
            noOneWins();
        } else {
            isPlayerOneTurn = !isPlayerOneTurn; //if true, change to false. if false, change to true
        }
    }

    private void updatePlayerOneName(){
        String playerName = getIntent().getStringExtra("playerName");
        textPointsPlayerOne.setText(playerName+": 0");
    }

    private String setP1DrawToPlay(String p1, String drawSelected){
        Random random = new Random();
        int value = 0;

        if (drawSelected.equals("Random")){
            value = random.nextInt(5-1)+1;
            if (value < 3){
                p1 = "Play Os";
            } else {
                p1 = "Play Xs";
            }
        }

        if (drawSelected.equals("Play Xs") || p1.equals("Play Xs")){
            p1 = "X";
        } else if (drawSelected.equals("Play Os")  || p1.equals("Play Os")){
            p1 = "O";
        }
        return p1;
    }

    private String setP2DrawToPlay(String p1){
        String p2 = new String();
        if (p1.equals("X")){
            p2 = "O";
        } else {
            p2 = "X";
        }
        return p2;
    }

    /** Metodo de validacion de posiciones marcadas (si son iguales y distintas de vacio) */
    private boolean checkWinPositions() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        //posiciones verticales (columnas)
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        //posiciones horizontales (filas)
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        //diagonal izquierda a derecha
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        //diagonal derecha a izquierda
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void playerOneWins(){
        String playerName = getIntent().getStringExtra("playerName");
        pointsPlayerOne++;
        Toast.makeText(this, playerName+" is the winner!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void playerTwoWins(){
        pointsPlayerTwo++;
        Toast.makeText(this, "Player 2 is the winner!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void noOneWins(){
        Toast.makeText(this, "No one has win!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText(){
        String playerName = getIntent().getStringExtra("playerName");
        textPointsPlayerOne.setText(playerName+": "+pointsPlayerOne);
        textPointsPlayerTwo.setText("Player 2: "+pointsPlayerTwo);
    }

    private void resetBoard(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }

        roundCounter = 0;
        isPlayerOneTurn = true;
    }

    private void restartGame(){
        pointsPlayerOne = 0;
        pointsPlayerTwo = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCounter", roundCounter);
        outState.putInt("pointsPlayerOne", pointsPlayerOne);
        outState.putInt("pointsPlayerTwo", pointsPlayerTwo);
        outState.putBoolean("isPlayerOneTurn", isPlayerOneTurn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCounter = savedInstanceState.getInt("roundCounter");
        pointsPlayerOne = savedInstanceState.getInt("pointsPlayerOne");
        pointsPlayerTwo = savedInstanceState.getInt("pointsPlayerTwo");
        isPlayerOneTurn = savedInstanceState.getBoolean("isPlayerOneTurn");
    }

    public void machineTurn(){
        int r, c;
        Random random = new Random();
        r= (int)Math.floor(Math.random()*(2-0+1)+0);
        c= (int)Math.floor(Math.random()*(2-0+1)+0);

        String buttonID = "button_" + r  + c;
        int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
        ((Button) findViewById(resID)).setText(p2Draw);
        isPlayerOneTurn = true;
    }

    private void playerVsPlayer(){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                String buttonId = "button_"+ i + j;
                int resId = getResources().getIdentifier(buttonId,"id",getPackageName());
                buttons[i][j] = findViewById(resId);
                buttons[i][j].setOnClickListener(this);
            }
        }
    }

    private void playerVsMachine(){
        machineTurn();
        while (count<4){
        if (isPlayerOneTurn){
            for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    String buttonId = "button_"+ i + j;
                    int resId = getResources().getIdentifier(buttonId,"id",getPackageName());
                    buttons[i][j] = findViewById(resId);
                    buttons[i][j].setOnClickListener(this);
                }
            }
        } else {
            machineTurn();
        }
        count++;
        }

    }

}
