package com.chaos.starke.actions;

import android.view.LayoutInflater;
import android.view.View;

public interface ActionItem {

    public enum Row {
        Header, Entry
    }

    public int getViewType();

    public View getView(LayoutInflater inflater, View view);
}