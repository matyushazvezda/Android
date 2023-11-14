package com.example.gamenumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        EditText edBegin = findViewById(R.id.begin);
        EditText edEnd = findViewById(R.id.end);

        int begin = Integer.parseInt(edBegin.getText().toString()); //left
        int end = Integer.parseInt(edEnd.getText().toString()); //right

        Intent i = new Intent(getApplicationContext(), GameActivity.class);
        i.putExtra("begin", begin);
        i.putExtra("end", end);
        startActivity(i);
    }
}