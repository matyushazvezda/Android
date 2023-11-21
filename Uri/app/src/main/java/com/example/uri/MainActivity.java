package com.example.uri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText userInput;
    TextView outputText;
    RadioGroup options;

    public enum DataType {
        PHONE_NUMBER,
        WEB_PAGE,
        GEO_POINT,
        WRONG
    }

    private DataType determineDataType(String input) {
        if (input.matches("(?:\\+7|8)\\d{10}")) {
            return DataType.PHONE_NUMBER;
        }
        if (input.matches("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")) {
            return DataType.GEO_POINT;
        }
        if (input.matches("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)")) {
            return DataType.WEB_PAGE;
        }
        return DataType.WRONG;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInput = findViewById(R.id.input);
        outputText = findViewById(R.id.output);
        options = findViewById(R.id.radio_group);
    }

    public void performAction(View view) {
        String input = userInput.getText().toString();
        DataType type = determineDataType(input);
        int selectedOptionId = options.getCheckedRadioButtonId();

        outputText.setText("");

        if (selectedOptionId == -1) {
            performDefaultAction(type, input);
        } else {
            performActionByOption(type, selectedOptionId, input);
        }
    }

    private void performDefaultAction(DataType type, String input) {
        switch (type) {
            case PHONE_NUMBER:
                outputText.setText("Осуществляю звонок по телефону");
                break;
            case GEO_POINT:
                openMaps(input);
                break;
            case WEB_PAGE:
                openWebPage(input);
                break;

            case WRONG:
                outputText.setText("Неверный ввод!");
                break;
            default:
                outputText.setText("Неверный ввод!");
                break;
        }
    }

    private void performActionByOption(DataType type, int selectedOptionId, String input) {
        if (selectedOptionId == R.id.radio_phoneNumber && type == DataType.PHONE_NUMBER) {
            outputText.setText("Осуществляю звонок по телефону");
        } else if (selectedOptionId == R.id.radio_geopoint && type == DataType.GEO_POINT) {
            openMaps(input);
        } else if (selectedOptionId == R.id.radio_webpage && type == DataType.WEB_PAGE) {
            openWebPage(input);
        } else {
            outputText.setText("Неверный ввод!");
        }
    }


    private void openMaps(String coordinates) {
        Uri mapUri = Uri.parse("geo:" + coordinates);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        try {
            startActivity(mapIntent);
        } catch (ActivityNotFoundException e) {
            outputText.setText("Приложение не найдено");
        }
    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

        try {
            startActivity(webIntent);
        } catch (ActivityNotFoundException e) {
            outputText.setText("Приложение не найдено");
        }
    }
}