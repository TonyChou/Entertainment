package com.union.fmdouban;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.union.fmdouban.ui.fragment.FMPlayerFragment;

public class MainActivity extends AppCompatActivity {
    FMPlayerFragment channelFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragment();

    }

    private void setFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        channelFragment = FMPlayerFragment.newInstance();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, channelFragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
