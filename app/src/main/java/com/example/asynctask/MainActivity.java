package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String PROGRESS_FRAGMENT_TAG = "progress";

    FragmentManager fragmentManager;

    EditText editText;
    TextView textView;
    Button button;
    Button secondTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        secondTaskButton  = findViewById(R.id.button2);

        fragmentManager = getSupportFragmentManager();

        button.setOnClickListener(l -> {
            String userInput = editText.getText().toString();
            if (!userInput.isEmpty()) {
                textView.setVisibility(View.INVISIBLE);
                new ProgressAsyncTask().execute(Integer.parseInt(editText.getText().toString()));
            }
        });

        secondTaskButton.setOnClickListener(l -> {
            Intent intent = new Intent(MainActivity.this, SecondTaskActivity.class);
            startActivity(intent);
        });

    }

    private class ProgressAsyncTask extends AsyncTask<Integer, Integer, Void> {

        private ProgressFragment progressFragment;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressFragment = ProgressFragment.newInstance();
            progressFragment.show(fragmentManager, PROGRESS_FRAGMENT_TAG);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            while (integers[0] > 0) {
                publishProgress(integers[0]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                integers[0] -= 1;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressFragment.setText(String.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressFragment.dismiss();

            textView.setText("Completed");
            textView.setTextColor(Color.GREEN);
            textView.setVisibility(View.VISIBLE);
        }
    }
}
