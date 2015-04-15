package com.chaos.staerke.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chaos.staerke.R;
import com.chaos.staerke.widgets.VerticalSeekBar;

import butterknife.InjectView;

public class TrackingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener{

    @InjectView(R.id.weight)
    protected VerticalSeekBar weight;

    @InjectView(R.id.weightValue)
    protected TextView weightValue;

    @InjectView(R.id.repetitions)
    protected VerticalSeekBar repetitions;

    @InjectView(R.id.repetitionsValue)
    protected TextView repetitionsValue;

    @InjectView(R.id.sets)
    protected VerticalSeekBar sets;

    @InjectView(R.id.setsValue)
    protected TextView setsValue;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weight.setBounds(1, 280);
        weight.setOnSeekBarChangeListener(this);

        repetitions.setBounds(1, 10);
        repetitions.setOnSeekBarChangeListener(this);

        sets.setBounds(1, 10);
        sets.setOnSeekBarChangeListener(this);
    }

    @Override
    public int onCreateViewId() {
        return R.layout.activity_tracking;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()) {
            case R.id.weight:
                weightValue.setText(String.valueOf(progress));
                return;

            case R.id.repetitions:
                repetitionsValue.setText(String.valueOf(progress));
                return;

            case R.id.sets:
                setsValue.setText(String.valueOf(progress));
                return;

            default:
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
     public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_tracking, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
