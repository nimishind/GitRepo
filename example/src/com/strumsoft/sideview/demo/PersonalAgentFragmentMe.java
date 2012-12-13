package com.strumsoft.sideview.demo;

import com.strumsoft.sideview.demo.intrestbuttonlayout.ViewIntrestButtonLayout;

import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class PersonalAgentFragmentMe extends android.support.v4.app.Fragment {

    private ViewIntrestButtonLayout mFromBubbleLayout;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_me, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mFromBubbleLayout = (ViewIntrestButtonLayout) v.findViewById(R.id.from_bubblelayout);
        // Inflate the layout for this fragment
        
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        int width = display.getHeight() < display.getWidth() ? display.getHeight() : display.getWidth(); //orientation is fix so width < height


        mFromBubbleLayout.setExpectedLayoutWidth((int) (width - .1 * width));
        mFromBubbleLayout.registerChildViewsFromOwnXML();
       

        mFromBubbleLayout.setType(ViewIntrestButtonLayout.INTEREST, "jjj");
        mFromBubbleLayout.addButton("Spa");

        mFromBubbleLayout.addButton("Restaurant");

        mFromBubbleLayout.addButton("Saloon");
        mFromBubbleLayout.addButton("swimming");

        mFromBubbleLayout.addButton("Saloon3");
        mFromBubbleLayout.addButton("swimming3");

        mFromBubbleLayout.addButton("Spa4");

        mFromBubbleLayout.addButton("Restaurant4");
        mFromBubbleLayout.addButton("Gym");
        mFromBubbleLayout.addButton("Saloon4");
        mFromBubbleLayout.addButton("hotels");
        mFromBubbleLayout.addButton("swimming5");
        mFromBubbleLayout.addButton("Spa");

        mFromBubbleLayout.addButton("Restaurant");

        mFromBubbleLayout.addButton("Saloon");
        mFromBubbleLayout.addButton("swimming");

        mFromBubbleLayout.addButton("Saloon3");
        mFromBubbleLayout.addButton("swimming3");

        mFromBubbleLayout.addButton("Spa4");

        mFromBubbleLayout.addButton("Restaurant4");
        mFromBubbleLayout.addButton("Gym");
        mFromBubbleLayout.addButton("Saloon4");
        mFromBubbleLayout.addButton("hotels");
        mFromBubbleLayout.addButton("swimming5");
        mFromBubbleLayout.addButton("Spa");

        mFromBubbleLayout.addButton("Restaurant");

        mFromBubbleLayout.addButton("Saloon");
        mFromBubbleLayout.addButton("swimming");

        mFromBubbleLayout.addButton("Saloon3");
        mFromBubbleLayout.addButton("swimming3");

        mFromBubbleLayout.addButton("Spa4");

        mFromBubbleLayout.addButton("Restaurant4");
        mFromBubbleLayout.addButton("Gym");
        mFromBubbleLayout.addButton("Saloon4");
        mFromBubbleLayout.addButton("hotels");
        mFromBubbleLayout.addButton("swimming5");
        
        mFromBubbleLayout.refreshDrawableState();
      

        super.onViewCreated(view, savedInstanceState);
    }
}