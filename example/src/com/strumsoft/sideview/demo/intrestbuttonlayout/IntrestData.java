package com.strumsoft.sideview.demo.intrestbuttonlayout;public class IntrestData {    private int mIndex;    private String mName;    private String mInterest;    private String mInterestID;    private boolean mIsValidInterest;    private boolean mIsFromInterest;    public IntrestData(String Interest) {        this(Interest, null, null, false);        mIndex = 0;    }    public IntrestData(String Interest, String interestID) {        this(Interest, null, interestID, false);        mIndex = 0;    }    public IntrestData(String Interest, String interestID, int index) {        this(Interest, null, interestID, false);        mIndex = index;    }    public IntrestData(String Interest, String name, String interestID, boolean isFromInterest) {        mInterest = Interest;        mName = name;        mInterestID = interestID;    }    public void refreshInterestInfo() {        // if refresh of name is needed    }    public int getIndex() {        return mIndex;    }    public String getName() {        return mName;    }    public String getInterest() {        return mInterest;    }    public boolean isFromInterest() {        return mIsFromInterest;    }    public boolean isValidInterest() {        return mIsValidInterest;    }}