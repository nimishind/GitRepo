package com.strumsoft.sideview.demo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.strumsoft.sideview.SideView;

public class Home extends SherlockFragmentActivity {
    private Button mHalves;
    private Button mMaximizeMainContent;
    private Button mMaximizeDataContent;
    SideView sv;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.header_bg));
        actionBar.show();

        sv = (SideView) findViewById(R.id.sideviewdemo);

        Display display = this.getWindowManager().getDefaultDisplay();
        sv.setScreenWidth(display.getWidth());
        final ImageView imv = (ImageView) findViewById(R.id.mainimageView);

        mMaximizeMainContent = (Button) findViewById(R.id.btn_me);
        mMaximizeMainContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Hello ME", 500).show();
                imv.setImageResource(R.drawable.demo_two);
            }
        });

        mMaximizeMainContent = (Button) findViewById(R.id.btn_persona);
        mMaximizeMainContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Hello Persona", 500).show();
                imv.setImageResource(R.drawable.demo_one);
            }
        });
 
//        mMaximizeMainContent = (Button) findViewById(R.id.button1);
//        mMaximizeMainContent.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(Home.this, "Hello", 500).show();
//
//            }
//        });
        
        
        LayoutParams lpm = new LayoutParams(display.getWidth(), LayoutParams.FILL_PARENT);
        LinearLayout lmv = (LinearLayout) findViewById(R.id.data2);
        lmv.setLayoutParams(lpm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_opt_in_main_overview, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (sv != null) {
            Display display = this.getWindowManager().getDefaultDisplay();
            sv.setScreenWidth(display.getWidth());
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
