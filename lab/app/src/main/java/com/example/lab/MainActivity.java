package com.example.lab;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private TextView textViewTittle, textViewYear, textViewDirector, textViewGenre, textViewRating;
    private int k =0;
    private List<Integer> selectedMovieIndices = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTittle = findViewById(R.id.textView);
        textViewYear = findViewById(R.id.textView2);
        textViewDirector = findViewById(R.id.textView3);
        textViewGenre = findViewById(R.id.textView4);
        textViewRating = findViewById(R.id.textView5);
        //textView = findViewById(R.id.textView);
    }


    public void clickButton(View v) {
        AssetManager assetManager = getAssets();

        try {
            InputStream is = assetManager.open("movies.json");
            String js = convertStreamToString(is);

            Gson g = new Gson();
            ArrayList<Movie> movies = g.fromJson(js, new TypeToken<List<Movie>>() {}.getType());

            if (selectedMovieIndices.size() < movies.size()) {
                Random random = new Random();

                int moviesLeft = movies.size() - selectedMovieIndices.size();
                int randomIndex = random.nextInt(moviesLeft);

                for (int i = 0, j = 0; i < movies.size(); ++i) {
                    if (!selectedMovieIndices.contains(i)) {
                        if (j == randomIndex) {
                            randomIndex = i;
                            break;
                        }
                        ++j;
                    }
                }

                selectedMovieIndices.add(randomIndex);

                Movie movie = movies.get(randomIndex);
                String movieTitle = movie.getTitle();
                int movieYear = movie.getYear();
                String movieDirector = movie.getDirector();
                String movieGenre = movie.getGenre();
                double movieRating = movie.getRating();

                textViewTittle.setText(movieTitle);
                textViewYear.setText("Год: "+String.valueOf(movieYear));
                textViewDirector.setText("Режиссер: "+movieDirector);
                textViewGenre.setText("Жанр: "+movieGenre);
                textViewRating.setText("Рейтинг: "+String.valueOf(movieRating));
            } else {
                textViewTittle.setText("Все фильмы просмотрены");
                textViewYear.setText("");
                textViewDirector.setText("");
                textViewGenre.setText("");
                textViewRating.setText("");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private String convertStreamToString(InputStream is) {
        java.util.Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}