package com.chaos.staerke.adapters.items;

import android.view.LayoutInflater;
import android.view.View;

public interface ActionInterface {
    int getViewType();

    View getView(final LayoutInflater inflater, final View view);

    enum Row {
        Header, Entry
    }
}
