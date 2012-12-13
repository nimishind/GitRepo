package com.strumsoft.sideview.demo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.strumsoft.sideview.SideView;

public class Home extends SherlockFragmentActivity {

    private Button mMaximizeMainContent;

    private SideView sv;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.header_bg));
        actionBar.show();

        sv = (SideView) findViewById(R.id.sideviewdemo);
        Log.i("Nimish", "Setting main content Size" + sv);
        final ImageView imv = (ImageView) findViewById(R.id.mainimageView);

        mMaximizeMainContent = (Button) findViewById(R.id.btn_me);
        mMaximizeMainContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Hello ME", Toast.LENGTH_SHORT).show();
                imv.setImageResource(R.drawable.demo_two);
            }
        });

        mMaximizeMainContent = (Button) findViewById(R.id.btn_persona);
        mMaximizeMainContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Hello Persona", Toast.LENGTH_SHORT).show();
                imv.setImageResource(R.drawable.demo_one);
            }
        });

        // mMaximizeMainContent = (Button) findViewById(R.id.button1);
        // mMaximizeMainContent.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // Toast.makeText(Home.this, "Hello", 500).show();
        //
        // }
        // });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Nimish", "Setting main onStart Size" + sv);
        if (sv != null) {
            Display display = this.getWindowManager().getDefaultDisplay();
            sv.setScreenSize(display);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_opt_in_main_overview, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("Nimish", "Setting main onConfigurationChanged Size" + sv);
        if (sv != null) {
            Display display = this.getWindowManager().getDefaultDisplay();
            sv.setScreenSize(display);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (sv.isDataContentMaximized()) {
                sv.maximizeMainContent();
                return true;

            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
