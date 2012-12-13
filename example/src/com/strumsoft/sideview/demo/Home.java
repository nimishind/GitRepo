package com.strumsoft.sideview.demo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

import com.strumsoft.sideview.SideView;

public class Home extends SherlockFragmentActivity implements OnClickListener {

    private Button mMaximizeMainContent;

    private SideView sv;
    private ImageView img;
    private FrameLayout mFrameLayout;
    Fragment mLoadedFragment = null;

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

        img = (ImageView) findViewById(R.id.mainimageView);
        mFrameLayout = (FrameLayout) findViewById(R.id.dataFragment);

        mMaximizeMainContent = (Button) findViewById(R.id.btn_me);
        mMaximizeMainContent.setOnClickListener(this);
        mMaximizeMainContent = (Button) findViewById(R.id.btn_persona);
        mMaximizeMainContent.setOnClickListener(this);
        mMaximizeMainContent = (Button) findViewById(R.id.btn_click_fragment);
        mMaximizeMainContent.setOnClickListener(this);
        mMaximizeMainContent = (Button) findViewById(R.id.btn_me_fragment);
        mMaximizeMainContent.setOnClickListener(this);
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

    // {
    // newFragment = new PersonalAgentFragmentMe();
    // transaction = getSupportFragmentManager().beginTransaction();
    //
    // transaction.replace(R.id.tableMeFrame, newFragment);
    // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    // transaction.commit();
    // }
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

    @Override
    public void onClick(View v) {

        FragmentTransaction transaction;

        switch (v.getId()) {

        case R.id.btn_me:
            Toast.makeText(Home.this, "Hello ME", Toast.LENGTH_SHORT).show();
            if (mLoadedFragment != null) {
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(mLoadedFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
            mFrameLayout.setVisibility(View.GONE);

            img.setImageResource(R.drawable.demo_two);
            img.setVisibility(View.VISIBLE);
            break;
        case R.id.btn_persona:
            Toast.makeText(Home.this, "Hello Persona", Toast.LENGTH_SHORT).show();
            if (mLoadedFragment != null) {
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.remove(mLoadedFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
            }
            mFrameLayout.setVisibility(View.GONE);
            img.setImageResource(R.drawable.demo_one);
            img.setVisibility(View.VISIBLE);
            break;

        case R.id.btn_me_fragment:
            img.setImageDrawable(null);
            mFrameLayout.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);

            transaction = getSupportFragmentManager().beginTransaction();
            if (mLoadedFragment != null)
                transaction.remove(mLoadedFragment);
            mLoadedFragment = new PersonalAgentFragmentMe();
            transaction.replace(R.id.dataFragment, mLoadedFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();

            break;
        case R.id.btn_click_fragment:
            img.setImageDrawable(null);
            mFrameLayout.setVisibility(View.VISIBLE);
            img.setVisibility(View.GONE);

            transaction = getSupportFragmentManager().beginTransaction();
            if (mLoadedFragment != null)
                transaction.remove(mLoadedFragment);
            mLoadedFragment = new PersonalAgentFragmentClicks();
            transaction.replace(R.id.dataFragment, mLoadedFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
            break;

        }

    }
}
