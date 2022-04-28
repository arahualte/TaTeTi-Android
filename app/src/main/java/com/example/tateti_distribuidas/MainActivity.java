package com.example.tateti_distribuidas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean isPlayerOneTurn = true;
    private int roundCounter = 0;
    private int pointsPlayerOne;
    private int pointsPlayerTwo;

    private TextView textPointsPlayerOne;
    private TextView textPointsPlayerTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textPointsPlayerOne = findViewById(R.id.text_p1);
        textPointsPlayerTwo = findViewById(R.id.text_p2);

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                String buttonId = "button_"+ i + j;
                int resId = getResources().getIdentifier(buttonId,"id",getPackageName());
                buttons[i][j] = findViewById(resId);
                buttons[i][j].setOnClickListener(this);
            }
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
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("O");
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
            isPlayerOneTurn = !isPlayerOneTurn; //si es true, cambio a falso y viceversa
        }
    }

    /**
     * validacion de posiciones marcadas, si son iguales y distintas de vacio
     */
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
        pointsPlayerOne++;
        Toast.makeText(this, "Player 1 is the winner!", Toast.LENGTH_SHORT).show();
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
        textPointsPlayerOne.setText("Player 1: "+pointsPlayerOne);
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
}