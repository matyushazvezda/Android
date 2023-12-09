package com.example.notedb;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

     ListView listView;
     EditText editTextNote;
     SimpleCursorAdapter adapter;
     SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view_notes);
        editTextNote = findViewById(R.id.edit_text_note);

        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        displayTotalNotes();
        displayNotes();
        displayLongestNote();

        Button addButton = findViewById(R.id.button_add_note);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        Button sortButton = findViewById(R.id.button_sort_notes);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSort();
            }
        });

        Button showRangeButton = findViewById(R.id.button_show_range);
        showRangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRangeDialog();
            }
        });
    }

    private void displayNotes() {
        Cursor cursor = db.rawQuery("SELECT * FROM notes", null);

        String[] columns = {"_id", "note_text", "note_date", "_id"};
        int[] toViews = {R.id.text_view_note, R.id.text_view_note_date, R.id.button_delete};

        adapter = new SimpleCursorAdapter(this, R.layout.list_item_note, cursor, columns, toViews, 0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int viewId = view.getId();

                if (viewId == R.id.text_view_note) {
                    int noteNumber = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    String noteText = cursor.getString(cursor.getColumnIndexOrThrow("note_text"));
                    ((TextView) view).setText("Note #" + noteNumber + ": " + noteText);
                    return true;
                } else if (viewId == R.id.text_view_note_date) {
                    String noteDate = cursor.getString(cursor.getColumnIndexOrThrow("note_date"));
                    ((TextView) view).setText("Date: " + noteDate);
                    return true;
                } else if (viewId == R.id.button_delete) {
                    Button deleteButton = (Button) view;
                    int noteId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteNoteById(noteId);
                            displayNotes();
                            displayTotalNotes();
                            displayLongestNote();
                        }
                    });
                    return true;
                }

                return false;
            }
        });

        listView.setAdapter(adapter);
    }

    private void deleteNoteById(int noteId) {
        db.delete("notes", "_id=?", new String[]{String.valueOf(noteId)});
    }


    protected void addNote() {
        String noteText = editTextNote.getText().toString();

        ContentValues values = new ContentValues();
        values.put("note_text", noteText);

        db.insert("notes", null, values);
        editTextNote.setText("");

        displayNotes();
        displayTotalNotes();
        displayLongestNote();
    }

    private void displayTotalNotes() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM notes", null);
        int totalNotes = 0;

        if (cursor != null) {
            cursor.moveToFirst();
            totalNotes = cursor.getInt(0);
            cursor.close();
        }

        TextView totalNotesTextView = findViewById(R.id.text_view_total_notes);
        totalNotesTextView.setText("| Total Notes: " + totalNotes);
    }

    private void displayLongestNote() {
        Cursor cursor = db.rawQuery("SELECT LENGTH(note_text) AS note_length FROM notes ORDER BY LENGTH(note_text) DESC LIMIT 1", null);

        int longestNoteLength = 0;

        if (cursor != null && cursor.moveToFirst()) {
            longestNoteLength = cursor.getInt(cursor.getColumnIndexOrThrow("note_length"));
            cursor.close();
        }

        TextView longestNoteLengthTextView = findViewById(R.id.text_view_longest_note);
        longestNoteLengthTextView.setText("Longest Note: " + longestNoteLength + " characters");
    }
    boolean isSortedByLength = false;
    private void toggleSort() {
        isSortedByLength = !isSortedByLength;

        if (isSortedByLength) {
            displayNotesSortedByLength();
        } else {
            displayNotes();
        }
    }

    private void displayNotesSortedByLength() {
        Cursor cursor = db.rawQuery("SELECT * FROM notes ORDER BY LENGTH(note_text) DESC", null);

        String[] columns = {"_id", "note_text", "note_date"};
        int[] toViews = {R.id.text_view_note, R.id.text_view_note_date};

        adapter = new SimpleCursorAdapter(this, R.layout.list_item_note, cursor, columns, toViews, 0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int viewId = view.getId();

                if (viewId == R.id.text_view_note) {
                    int noteNumber = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    String noteText = cursor.getString(cursor.getColumnIndexOrThrow("note_text"));
                    ((TextView) view).setText("Note #" + noteNumber + ": " + noteText);
                    return true;
                } else if (viewId == R.id.text_view_note_date) {
                    String noteDate = cursor.getString(cursor.getColumnIndexOrThrow("note_date"));
                    ((TextView) view).setText("Date: " + noteDate);
                    return true;
                }

                return false;
            }
        });
        listView.setAdapter(adapter);
    }

    private void showRangeDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_show_range, null);
        dialogBuilder.setView(dialogView);

        EditText startNoteEditText = dialogView.findViewById(R.id.edit_text_start_note);
        EditText endNoteEditText = dialogView.findViewById(R.id.edit_text_end_note);

        dialogBuilder.setTitle("Укажите границы заметок");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String startNoteStr = startNoteEditText.getText().toString();
                String endNoteStr = endNoteEditText.getText().toString();

                int startNote = startNoteStr.isEmpty() ? 0 : Integer.parseInt(startNoteStr);
                int endNote = endNoteStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(endNoteStr);

                displayNotesInRange(startNote, endNote);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void displayNotesInRange(int startNote, int endNote) {
        Cursor cursor = db.rawQuery("SELECT * FROM notes WHERE _id BETWEEN ? AND ?", new String[]{String.valueOf(startNote), String.valueOf(endNote)});

        String[] columns = {"_id", "note_text", "note_date"};
        int[] toViews = {R.id.text_view_note, R.id.text_view_note_date};

        adapter = new SimpleCursorAdapter(this, R.layout.list_item_note, cursor, columns, toViews, 0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int viewId = view.getId();

                if (viewId == R.id.text_view_note) {
                    int noteNumber = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    String noteText = cursor.getString(cursor.getColumnIndexOrThrow("note_text"));
                    ((TextView) view).setText("Note #" + noteNumber + ": " + noteText);
                    return true;
                } else if (viewId == R.id.text_view_note_date) {
                    String noteDate = cursor.getString(cursor.getColumnIndexOrThrow("note_date"));
                    ((TextView) view).setText("Date: " + noteDate);
                    return true;
                }

                return false;
            }
        });

        listView.setAdapter(adapter);
    }

}