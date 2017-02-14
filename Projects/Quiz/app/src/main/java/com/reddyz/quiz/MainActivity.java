package com.reddyz.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {


//    private String [] questionsArray = {
//            "Android is licensed under which open source licensing license?",
//            "Although most people’s first thought when they think of Android is Google, Android is not actually owned by Google. Who owns the Android platform?",
//            "As an Android programmer, what version of Android should you use as your minimum development target?",
//            "What was the first phone released that ran the Android OS?",
//            "What year was the Open Handset Alliance announced?",
//            "What part of the Android platform is open source?",
//            "When did Google purchase Android?",
//            "Android releases since 1.5 have been given nicknames derived how?",
//            "Which among these are NOT a part of Android’s native libraries?",
//            "Which one is not a nickname of a version of Android?"
//    };
//
//    private String [][] optionsArray ={
//            {"Gnu’s GPL", "Apache/MIT", "OSS", "Sourceforge"},
//            {"Oracle Technology", "Dalvik", "Open Handset Alliance", "Android is owned by Google"},
//            {"Versions 1.6 or 2.0", "Versions 1.0 or 1.1", "Versions 1.2 or 1.3", "Versions 2.3 or 3.0"},
//            {"Google gPhone", "T-Mobile G1", "Motorola Droid", "HTC Hero"},
//            {"2005", "2006", "2007", "2008"},
//            {"low-level Linux modules", "native libraries", "application framework", "complete applications"},
//            {"2007", "2005", "2008", "2010"},
//            {"Strange animals", "Desserts", "American States", "Chocolates"},
//            {"Webkit", "Dalvik", "OpenGL", "SQLite"},
//            {"Cupcake", "Gingerbread", "Honeycomb", "Muffin"}
//    };

    private String [] questionsArray;
    private String [] optionsArray;
    private static final int MAX_SCORE = 100;
    private int maxQuestions;

    private String [] answersActual = { "b","c","a","b","c","abcd","b","b","b","d"};
    private String [] inputAnswer;
    private int counter=0;
    boolean quizStarted=false;

    TextView counterTextView,questionTextView, resultsTextView;
    Button startNextButton, resetButton;
    CheckBox checkBoxOptionA, checkBoxOptionB, checkBoxOptionC, checkBoxOptionD;
    ScrollView resultScrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initApp();
    }

    /* Do all the custom app initalizations here */
    private void initApp() {
        questionsArray = getResources().getStringArray(R.array.questions_array);
        optionsArray = getResources().getStringArray(R.array.options_array);
        maxQuestions = questionsArray.length;
        Log.i("Quiz-Android-locale","Max Questions: "+String.valueOf(maxQuestions));
        inputAnswer = new String[maxQuestions];

        initializeXmlVars();

        //resultsTextView.setVisibility(View.INVISIBLE);
        resultScrollview.setVisibility(View.INVISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
        enableCheckBoxes(false);
    }

    private void enableCheckBoxes(boolean b) {
        checkBoxOptionA.setEnabled(b);
        checkBoxOptionB.setEnabled(b);
        checkBoxOptionC.setEnabled(b);
        checkBoxOptionD.setEnabled(b);
    }

    /* Reset all views and attributes to initial state */
    public void resetApp(View view) {
        //resultsTextView.setVisibility(View.INVISIBLE);
        resultScrollview.setVisibility(View.INVISIBLE);
        startNextButton.setEnabled(true);
        startNextButton.setText(R.string.start);
        resetButton.setVisibility(View.INVISIBLE);
        counter=0;
        quizStarted=false;

        resetCheckBoxes();
        questionTextView.setText(R.string.initial_note);
        counterTextView.setText(R.string.good_luck);
        //Reset inputAnswer array if needed?
    }

    /* clear all checkboxes and reset to initial state */
    private void resetCheckBoxes() {
        checkBoxOptionA.setChecked(false);
        checkBoxOptionA.setText(R.string.option_a);

        checkBoxOptionB.setChecked(false);
        checkBoxOptionB.setText(R.string.option_b);

        checkBoxOptionC.setChecked(false);
        checkBoxOptionC.setText(R.string.option_c);

        checkBoxOptionD.setChecked(false);
        checkBoxOptionD.setText(R.string.option_d);

        enableCheckBoxes(false);
    }

    /* connect xml views to access and refer here */
    private void initializeXmlVars() {
        counterTextView = (TextView) findViewById(R.id.counter_text);
        questionTextView = (TextView) findViewById(R.id.questions_text);
        resultsTextView = (TextView) findViewById(R.id.results_text);

        resultScrollview = (ScrollView) findViewById(R.id.result_scroll_view);

        startNextButton = (Button) findViewById(R.id.start_next_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        checkBoxOptionA = (CheckBox) findViewById(R.id.option_a_checkbox);
        checkBoxOptionB = (CheckBox) findViewById(R.id.option_b_checkbox);
        checkBoxOptionC = (CheckBox) findViewById(R.id.option_c_checkbox);
        checkBoxOptionD = (CheckBox) findViewById(R.id.option_d_checkbox);
    }

    /* process and save selected options to show results at the end */
    private boolean processSelectedOptions(int resultIndex)
    {
        String userSelectedOptions = "";
        //Process current user input answers
        if (checkBoxOptionA.isChecked())
            userSelectedOptions += "a";
        if (checkBoxOptionB.isChecked())
            userSelectedOptions += "b";
        if (checkBoxOptionC.isChecked())
            userSelectedOptions += "c";
        if (checkBoxOptionD.isChecked())
            userSelectedOptions += "d";

        if(userSelectedOptions.isEmpty()) {
            //none selected. toast msg to select atleast one answer
            return false;
        } else {
            inputAnswer[resultIndex] = userSelectedOptions;
            return true;
        }
    }

    /* Find out the results and show final results */
    private void showResults()
    {
        int score = 0 ;
        //resultsTextView.setVisibility(View.VISIBLE);
        resultScrollview.setVisibility(View.VISIBLE);

        String inputAnswerString="";
        for(int i = 0; i< maxQuestions; i++) {
            if(inputAnswer[i].equals(answersActual[i])) {
                score++;
            }
            inputAnswerString += inputAnswer[i] + "-";
        }

        String resultString="";
        resultString += getString(R.string.results_max_score, MAX_SCORE);
        resultString += " | \t" + getString(R.string.results_actual_score, score*(MAX_SCORE/maxQuestions));
        resultString += "\n" + getString(R.string.total_questions, maxQuestions);
        resultString += " | \t" + getString(R.string.correct_answers, score);
        resultString += "\n" + getString(R.string.selected_options, inputAnswerString);

        resultsTextView.setText(resultString);
        resetButton.setVisibility(View.VISIBLE);

    }

    /* Process Start/Next Button click */
    public void processQuiz(View view) {
        //Process the user selected options (if quiz started)
        if(quizStarted ) {
            boolean optionSelected = processSelectedOptions(counter);
            if(!optionSelected) {
                Toast.makeText(this, R.string.select_options, Toast.LENGTH_SHORT).show();
                return;
            }
            counter++;
        } else {
            startNextButton.setText(R.string.next);
            enableCheckBoxes(true);
            quizStarted = true;
        }

        //If all question done. disable Next Button and show results
        if(counter==maxQuestions) {
            Toast.makeText(this, R.string.toast_end_quiz, Toast.LENGTH_SHORT).show();
            startNextButton.setText(R.string.done);
            startNextButton.setEnabled(false);
            enableCheckBoxes(false);
            showResults();
            return;
        }

        //Setup Next set of Questions and Optional answers
        counterTextView.setText(String.valueOf(counter+1));
        setNextQuestionAndOptions(counter);
    }

    /* Set next question and corresponding options.
     * Note: options array is serialised 1-dimentional array with every 4 items being options for one question.
      *Hence, maintain correct order in sync with question index.*/
    private void setNextQuestionAndOptions(int questionIndex) {
        questionTextView.setText(questionsArray[questionIndex]);
        int optionIndex = questionIndex*4;

        checkBoxOptionA.setText(optionsArray[optionIndex]);
        checkBoxOptionA.setChecked(false);
        checkBoxOptionB.setText(optionsArray[optionIndex +1]);
        checkBoxOptionB.setChecked(false);
        checkBoxOptionC.setText(optionsArray[optionIndex + 2]);
        checkBoxOptionC.setChecked(false);
        checkBoxOptionD.setText(optionsArray[optionIndex + 3]);
        checkBoxOptionD.setChecked(false);
    }
}
