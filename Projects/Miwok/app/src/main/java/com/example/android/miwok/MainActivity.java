/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final boolean FRAGMENT_STYLE = true;
    private static final boolean DEBUG = true;
    //Keep the class name handy to log  data.
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(DEBUG)
            Log.i(LOG_TAG, " onCreate() ");
        super.onCreate(savedInstanceState);

        if( FRAGMENT_STYLE )
            createFragmentStyle();
        else
            createActivityStyle();
    }

    private void createFragmentStyle() {
        if(DEBUG)
            Log.i(LOG_TAG, " createFragmentStyle() ");
        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main_fragment);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        CategoryFragmentPagerAdapter categoryPagerAdapter =
                new CategoryFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext());

        viewPager.setAdapter(categoryPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void createActivityStyle() {
        if(DEBUG)
            Log.i(LOG_TAG, " createActivityStyle() ");
        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);

        TextView numbersView = (TextView) findViewById(R.id.numbers);
        TextView familyView = (TextView) findViewById(R.id.family);
        TextView colorsView = (TextView) findViewById(R.id.colors);
        TextView phrasesView = (TextView) findViewById(R.id.phrases);

        numbersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NumbersActivity.class);
                startActivity(i);
                if (DEBUG)
                    Toast.makeText(MainActivity.this, "Opening Numbers View", Toast.LENGTH_SHORT).show();
            }
        });

        familyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FamilyActivity.class);
                startActivity(i);
                if (DEBUG)
                    Toast.makeText(MainActivity.this, "Opening Family View", Toast.LENGTH_SHORT).show();
            }
        });

        colorsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ColorsActivity.class);
                startActivity(i);
                if (DEBUG)
                    Toast.makeText(MainActivity.this, "Opening Colors View", Toast.LENGTH_SHORT).show();
            }
        });

        phrasesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PhrasesActivity.class);
                startActivity(i);
                if (DEBUG)
                    Toast.makeText(MainActivity.this, "Opening Phrases View", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
