package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;

public class SecondTaskActivity extends AppCompatActivity {

    Button button;
    ProgressBar progressBar;
    TextView textView;

    private int remainingTasks;
    private boolean isDownloadSuccessful;
    private boolean isLoginSuccessful;

    final Object object = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_task);

        button = findViewById(R.id.start);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.resultTextView);

        button.setOnClickListener(l -> {
            remainingTasks = 2;
            progressBar.setVisibility(ProgressBar.VISIBLE);
            textView.setVisibility(View.INVISIBLE);
            new DownloadAsyncTask().execute();
            new LoginAsyncTask().execute();
        });
    }

    private class DownloadAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            Random random = new Random(System.currentTimeMillis());
            try {
                Thread.sleep((random.nextInt(4) + 2) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return random.nextBoolean();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isDownloadSuccessful = aBoolean;

            synchronized (object) {
                remainingTasks -= 1;
                if (remainingTasks == 0) {
                    onBothTasksDone();
                }
            }
        }
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            Random random = new Random(System.currentTimeMillis());
            try {
                Thread.sleep((random.nextInt(3) + 3) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return random.nextBoolean();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            isLoginSuccessful = aBoolean;

            synchronized (object) {
                remainingTasks -= 1;
                if (remainingTasks == 0) {
                    onBothTasksDone();
                }
            }
        }
    }

    private void onBothTasksDone() {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        if (isLoginSuccessful && isDownloadSuccessful) {
            textView.setText("Success!");
        } else {
            textView.setText("Failure..");
        }
    }
}
