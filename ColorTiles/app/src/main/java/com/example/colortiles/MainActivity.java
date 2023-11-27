package com.example.colortiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int black, purple, k;
    private View[][] gameBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resources = getResources();
        black = resources.getColor(R.color.dark);
        purple = resources.getColor(R.color.purple);

        gameBoard = new View[4][4];
        populateGameBoard();
        randomizeTiles();
    }

    private void populateGameBoard() {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                String tileId = "t" + i + j;
                int resId = getResources().getIdentifier(tileId, "id", getPackageName());
                gameBoard[i][j] = findViewById(resId);
                gameBoard[i][j].setOnClickListener(view -> onTileClick(view));
            }
        }
    }

    private void randomizeTiles() {
        Random random = new Random();
        k = 0;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                if (random.nextBoolean()) {
                    alterTileColor(gameBoard[i][j]);
                }
            }
        }
    }

    private void alterTileColor(View v) {
        ColorDrawable colorDrawable = (ColorDrawable) v.getBackground();
        int currentColor = colorDrawable.getColor();
        if (currentColor == purple) {
            v.setBackgroundColor(black);
            k++;
        } else {
            v.setBackgroundColor(purple);
            k--;
        }
    }

    private void toggleAdjacentTiles(View clickedTile) {
        String tag = clickedTile.getTag().toString();
        int row = tag.charAt(0) - '0';
        int col = tag.charAt(1) - '0';

        alterTileColor(clickedTile);
        for (int i = 0; i < 4; ++i) {
            alterTileColor(gameBoard[row][i]);
            alterTileColor(gameBoard[i][col]);
        }
    }

    public void onTileClick(View v) {
        toggleAdjacentTiles(v);
        checkGameResult();
    }

    private void checkGameResult() {
        if (k == 16) {
            showToast("Темный цвет победил!");
        } else if (k == 0) {
            showToast("Фиолетовый цвет победил!");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
