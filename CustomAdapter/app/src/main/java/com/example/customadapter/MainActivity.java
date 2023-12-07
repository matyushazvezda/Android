    package com.example.customadapter;

    import androidx.appcompat.app.AppCompatActivity;

    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AbsListView;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.RadioButton;
    import android.widget.RadioGroup;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.util.ArrayList;

    public class MainActivity extends AppCompatActivity {

        UserListAdapter adapter;
        ListView listView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            listView = findViewById(R.id.list);

            ArrayList<User> users = loadUsersFromJson();
            adapter = new UserListAdapter(this, users);
            listView.setAdapter(adapter);

            Button sortByNameButton = findViewById(R.id.sortByNameButton);
            sortByNameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.sortByName();
                }
            });


            Button sortBySexButton = findViewById(R.id.sortBySexButton);
            sortBySexButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.sortBySex();
                }
            });

            Button sortByPhoneNumberButton = findViewById(R.id.sortByPhoneNumberButton);
            sortByPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.sortByPhoneNumber();
                }
            });

            Button addUserButton = findViewById(R.id.addUserButton);
            addUserButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddUserDialog();
                }
            });
        }

        private ArrayList<User> loadUsersFromJson() {
            ArrayList<User> users = new ArrayList<>();

            try {
                InputStream inputStream = getAssets().open("input.json");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                inputStream.close();

                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String phoneNumber = jsonObject.getString("phoneNumber");
                    String sexString = jsonObject.getString("sex");

                    Sex sex = Sex.UNKNOWN;
                    if (sexString.equals("MAN")) {
                        sex = Sex.MAN;
                    } else if (sexString.equals("WOMAN")) {
                        sex = Sex.WOMAN;
                    }

                    users.add(new User(name, phoneNumber, sex));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return users;
        }

        private void showAddUserDialog() {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_add_user, null);
            dialogBuilder.setView(dialogView);

            EditText editTextName = dialogView.findViewById(R.id.editTextName);
            EditText editTextPhone = dialogView.findViewById(R.id.editTextPhone);
            RadioGroup radioGroupSex = dialogView.findViewById(R.id.radioGroupSex);

            dialogBuilder.setTitle("Добавить пользователя");
            dialogBuilder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String name = editTextName.getText().toString();
                    String phone = editTextPhone.getText().toString();

                    int selectedId = radioGroupSex.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = dialogView.findViewById(selectedId);

                    Sex sex = null;
                    if (selectedRadioButton.getText().toString().equals("Мужской")) {
                        sex = Sex.MAN;
                    } else if (selectedRadioButton.getText().toString().equals("Женский")) {
                        sex = Sex.WOMAN;
                    } else if (selectedRadioButton.getText().toString().equals("Без пола")) {
                        sex = Sex.UNKNOWN;
                    }

                    User newUser = new User(name, phone, sex);
                    adapter.addUser(newUser);
                }
            });
            AlertDialog addUserDialog = dialogBuilder.create();
            addUserDialog.show();

        }
    }