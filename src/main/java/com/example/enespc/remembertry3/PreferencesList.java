package com.example.enespc.remembertry3;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


public class PreferencesList extends AppCompatActivity {

    Button confirmButton;
    CheckBox facebookBox, twitterBox, personalBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_list);
        confirmButton  = (Button) findViewById(R.id.confirm);
        facebookBox = (CheckBox) findViewById(R.id.facebookBox);
        twitterBox = (CheckBox) findViewById(R.id.twitterBox);
        personalBox = (CheckBox) findViewById(R.id.personalBox);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Burada NFC ile yollmammız gereken bilgileri memoryden çelip yollamamız lazım
                //!!!!!!Yapılacak!!!!!!
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preferences_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
