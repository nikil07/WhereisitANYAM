package com.androidworks.nikhil.whereisitanyam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.androidworks.nikhil.whereisitanyam.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectTimeActivity extends AppCompatActivity {

    @BindView(R.id.hourPicker)
    NumberPicker hourPicker;
    @BindView(R.id.minutePicker)
    NumberPicker minutePicker;
    @BindView(R.id.bt_find)
    Button findTime;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
        ButterKnife.bind(this);
        minutePicker.setEnabled(false);
        initPickers();

        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldHour, int newHour) {
                hour = newHour;
                minutePicker.setEnabled(true);
            }
        });

        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldMinute, int newMinute) {
                minute = newMinute;
            }
        });

        findTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hour > 0 && minute > 0) {
                    Intent intent = new Intent(SelectTimeActivity.this,MainActivity.class);
                    intent.putExtra(Constants.SELECTED_HOUR, hour);
                    intent.putExtra(Constants.SELECTED_MINUTE, minute);
                    startActivity(intent);
                   // Toast.makeText(SelectTimeActivity.this, "Hour " + hour + "minute " + minute, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SelectTimeActivity.this, "Please select appropriate hour", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initPickers() {
        hourPicker.setMinValue(1);
        hourPicker.setMaxValue(12);
        minutePicker.setMinValue(1);
        minutePicker.setMaxValue(60);
    }
}
