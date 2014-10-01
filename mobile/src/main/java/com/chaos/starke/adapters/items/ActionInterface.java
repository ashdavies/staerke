package com.chaos.starke.adapters.items;

import android.view.LayoutInflater;
import android.view.View;

public interface ActionInterface {

    public enum Row {
        Header, Entry
    }

    public int getViewType();

    public View getView(LayoutInflater inflater, View view);

}