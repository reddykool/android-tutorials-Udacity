package com.reddyz.randomtest;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtVw = new TextView(this);
        txtVw.setText("Yooo Millionaire !!!");
        txtVw.setTextColor(Color.MAGENTA);
        txtVw.setGravity(Gravity.CENTER);
        txtVw.setTextSize(45);
        txtVw.setBackgroundColor(Color.YELLOW);
        txtVw.setTypeface(Typeface.DEFAULT_BOLD);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txtVw.setPadding(20, 20, 20, 20);
        //addContentView(txtVw, layoutParams);
    }

    public void printToLogs(View view) {

        String logTag = "RandomTest.java";
        // Find first menu item TextView and print the text to the logs
        TextView item1 = (TextView) findViewById(R.id.menu_item_1);
        Log.i(logTag, item1.getText().toString());

        // Find second menu item TextView and print the text to the logs
        TextView item2 = (TextView) findViewById(R.id.menu_item_2);
        Log.e(logTag, item2.getText().toString());

        // Find third menu item TextView and print the text to the logs
        TextView item3 = (TextView) findViewById(R.id.menu_item_3);
        Log.w(logTag, item3.getText().toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }
}
