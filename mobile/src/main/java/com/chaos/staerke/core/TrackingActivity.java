package com.chaos.staerke.core;

import android.os.Bundle;

import com.chaos.staerke.R;
import com.chaos.staerke.ui.VerticalSeekBar;

import butterknife.InjectView;

public class TrackingActivity extends BaseActivity {

    @InjectView(R.id.weight)
    protected VerticalSeekBar weight;

    @InjectView(R.id.repetitions)
    protected VerticalSeekBar repetitions;

    @InjectView(R.id.sets)
    protected VerticalSeekBar sets;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weight.setBounds(1, 280);
        repetitions.setBounds(1, 50);
        sets.setBounds(1, 10);
    }

    @Override
    public int onCreateViewId() {
        return R.layout.activity_tracking;
    }
}
