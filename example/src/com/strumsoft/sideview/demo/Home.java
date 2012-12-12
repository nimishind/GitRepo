package com.strumsoft.sideview.demo;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.widget.Button;

import com.strumsoft.sideview.SideView;

public class Home extends Activity {
    private Button mHalves;
    private Button mMaximizeMainContent;
    private Button mMaximizeDataContent;
    SideView sv;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        sv = (SideView) findViewById(R.id.sideviewdemo);

        Display display = this.getWindowManager().getDefaultDisplay();
        sv.setScreenWidth(display.getWidth());

        // mMaximizeMainContent = (Button)findViewById(R.id.maximize_Main);
        // mMaximizeMainContent.setOnClickListener( new OnClickListener() {
        // @Override public void onClick(View v) {
        // ((SplitView)findViewById(R.id.split_view)).maximizeMainContent();
        // }
        // 
        // });
        //
        // mMaximizeDataContent = (Button)findViewById(R.id.maximize_Data);
        // mMaximizeDataContent.setOnClickListener( new OnClickListener() {
        // @Override public void onClick(View v) {
        // ((SplitView)findViewById(R.id.split_view)).maximizeDataContent();
        // }
        //
        // });
        //
        // mHalves = (Button)findViewById(R.id.halves);
        // mHalves.setOnClickListener( new OnClickListener() {
        // @Override public void onClick(View v) {
        // ((SplitView)findViewById(R.id.split_view)).setMainContentSize(200);
        // }
        //
        // });

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
