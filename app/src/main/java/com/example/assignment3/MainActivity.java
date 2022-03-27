package com.example.assignment3;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

Question[] questionBank = new Question[7];
int[] colourBank = new int[7];
Question currentQuestion;
int arrayIndex = 0;
int progressIndex = 0;
int totalQuestions = 7;
String[] contents = new String[2];
FileManager fileManager;

Button startButton;
Button trueButton;
Button falseButton;
ProgressBar questionBar;

int right;
int wrong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileManager = new FileManager();

        startButton = findViewById(R.id.button3);
        trueButton = findViewById(R.id.button);
        falseButton = findViewById(R.id.button2);
        questionBar = findViewById(R.id.progressBar);

        //questionBank initialization
        questionBank[0] = new Question(getResources().getString(R.string.question1),true);
        questionBank[1] = new Question(getResources().getString(R.string.question2),false);
        questionBank[2] = new Question(getResources().getString(R.string.question3),false);
        questionBank[3] = new Question(getResources().getString(R.string.question4),true);
        questionBank[4] = new Question(getResources().getString(R.string.question5),true);
        questionBank[5] = new Question(getResources().getString(R.string.question6),true);
        questionBank[6] = new Question(getResources().getString(R.string.question7),false);


        colourBank[0] = getResources().getColor(R.color.red);
        colourBank[1] = getResources().getColor(R.color.yellow);
        colourBank[2] = getResources().getColor(R.color.orange);
        colourBank[3] = getResources().getColor(R.color.teal_700);
        colourBank[4] = getResources().getColor(R.color.purple_200);
        colourBank[5] = getResources().getColor(R.color.purple_500);
        colourBank[6] = getResources().getColor(R.color.teal_700);



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initQuiz();
            }
        });
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quiz_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        switch (item.getItemId()) {
            case R.id.average:
                contents = fileManager.openResults(MainActivity.this);

                if(contents[0] == null || contents[0].equals("null")) {
                    builder.setMessage(R.string.openResultsError)
                            .setNegativeButton(R.string.okay, null);
                }
                else {
                    builder.setMessage(getResources().getString(R.string.openResultsMessage) + contents[0] + "/" + contents[1])
                            .setNegativeButton(R.string.okay, null);
                }
                AlertDialog averageDialog = builder.create();
                averageDialog.show();

                return true;
            case R.id.numOfQuest:
                NumberPicker numberPicker = new NumberPicker(MainActivity.this);
                numberPicker.setMaxValue(7);
                numberPicker.setMinValue(1);

                builder.setView(numberPicker)
                        .setTitle(R.string.changeNumTitle)
                        .setMessage(R.string.changeNumMessage)
                        .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                totalQuestions = numberPicker.getValue();
                                initQuiz();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            case R.id.delete:
                fileManager.deleteResults(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shuffleColours(int[] colourArray) {
        Random rand = new Random();
        for (int i = 0; i < colourArray.length; i++) {
            int randomIndex = rand.nextInt(colourArray.length);
            int temp = colourArray[randomIndex];
            colourArray[randomIndex] = colourArray[i];
            colourArray[i] = temp;
        }
    }

    public void initQuiz() {
        startButton.setText(getResources().getString(R.string.button3Restart));
        Collections.shuffle(Arrays.asList(questionBank));
        shuffleColours(colourBank);
        currentQuestion = questionBank[0];
        QuestionsFragment.question.setText(currentQuestion.text);
        QuestionsFragment.question.setBackgroundColor(colourBank[0]);
        progressIndex = 0;
        arrayIndex = 0;
        right = 0;
        wrong = 0;
        questionBar.setProgress(progressIndex);
    }

    public void checkAnswer(boolean value) {
        if(currentQuestion == null) {
            Toast.makeText(getApplicationContext(), R.string.trueFalseError, Toast.LENGTH_LONG).show();
            return;
        }
        else if (value == currentQuestion.answer) {
            right++;
            Toast.makeText(getApplicationContext(), R.string.right, Toast.LENGTH_SHORT).show();
        }
        else {
            wrong++;
            Toast.makeText(getApplicationContext(), R.string.wrong, Toast.LENGTH_SHORT).show();
        }
        arrayIndex++;
        if (arrayIndex < totalQuestions) {
            currentQuestion = questionBank[arrayIndex];
            QuestionsFragment.question.setText(currentQuestion.text);
            QuestionsFragment.question.setBackgroundColor(colourBank[arrayIndex]);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(getResources().getString(R.string.completionMessagePart1) + " " + right + " " +
                    getResources().getString(R.string.completionMessagePart2)  + " " + totalQuestions + " " +
                    getResources().getString(R.string.completionMessagePart3))
                    .setTitle(R.string.completionTitle)
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            fileManager.saveResults(MainActivity.this, right, totalQuestions);
                            initQuiz();
                        }
                    })
                    .setNegativeButton(R.string.okay,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            initQuiz();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        progressIndex += (100/totalQuestions);
        questionBar.setProgress(progressIndex);
    }

}