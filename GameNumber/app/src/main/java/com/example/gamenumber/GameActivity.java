package com.example.gamenumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {
    int begin,end,k;
    TextView mg;
    Button yes, no;

    enum Choice{
        Yes, No
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent i = getIntent();
        begin = i.getIntExtra("begin", 0);
        end = i.getIntExtra("end",100);
        k = (begin+end)/2;

        yes = findViewById(R.id.yes);
        no = findViewById(R.id.no);
        mg = findViewById(R.id.message);

        mg.setText("Ваше число меньше " + k + "?");

    }

    public void onClick(View v) {
        Choice ch = (v.getId() == R.id.yes) ? Choice.Yes : Choice.No;

        switch (ch) {
            case Yes:
                end = k;
                break;
            case No:
                begin = k;
                break;
        }

        if (begin + 1 == end) {
            mg.setText("Ваше число " + begin + " !");
            yes.setVisibility(View.GONE);
            no.setVisibility(View.GONE);
        } else {
            k = (begin + end) / 2;
            mg.setText("Ваше число меньше " + k + "?");
        }
    }

}