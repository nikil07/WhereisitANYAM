package com.androidworks.nikhil.whereisitanyam;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.androidworks.nikhil.whereisitanyam.utils.Constants;
import com.androidworks.nikhil.whereisitanyam.utils.DataBaseHelper;
import com.androidworks.nikhil.whereisitanyam.utils.DataStore;
import com.androidworks.nikhil.whereisitanyam.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    static ArrayList<TimeZoneItem> timezones = new ArrayList<>();
    private static final String TAG = "NIK " + MainActivity.class.getSimpleName();
    static ArrayList<String> times = new ArrayList<>();
    @BindView(R.id.bt_where_time)
    TextView etWhereTime;
    @BindView(R.id.tv_where)
    TextView tvWhere;
    @BindView(R.id.listView)
    ListView listView;
    int selectedMinute, selectedHour;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDB();
        if (getIntent().getExtras() != null) {
            selectedHour = getIntent().getExtras().getInt(Constants.SELECTED_HOUR);
            selectedMinute = getIntent().getExtras().getInt(Constants.SELECTED_MINUTE);
            Utils.showLog(TAG, "selected hour " + selectedHour + "Selected minute " + selectedMinute);
            initUI();
        }
    }

    private void initDB() {
        dataBaseHelper = new DataBaseHelper(this);
        File database = this.getDatabasePath(DataBaseHelper.DATABASE_NAME);
        if (!database.exists()) {
            dataBaseHelper.getReadableDatabase();
            copyDataBase(this);
        }
        if (!checkVersion()) {
            dataBaseHelper.getReadableDatabase();
            copyDataBase(this);
        }
        AddTimeZonesTask task = new AddTimeZonesTask();
        task.execute();
    }

    private boolean checkVersion() {
        PackageInfo pInfo;
        try {
            pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            int verCode = pInfo.versionCode;
            if (DataStore.getInstance(this).getVersion() == verCode)
                return true;
            else {
                DataStore.getInstance(this).storeVersion(verCode);
                return false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean copyDataBase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DataBaseHelper.DATABASE_NAME);
            String outFileName = DataBaseHelper.DATABASE_PATH + DataBaseHelper.DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void showClock(View view) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                selectedHour = hour;
                selectedMinute = minute;
                initUI();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void initUI() {
        String selectedMinuteString = String.valueOf(selectedMinute);
        String selectedHourString = String.valueOf(selectedHour);
        if (selectedMinute < 10)
            selectedMinuteString = 0 + String.valueOf(selectedMinute);
        if (selectedHour < 10)
            selectedHourString = 0 + String.valueOf(selectedHour);
        etWhereTime.setText(selectedHourString + ":" + selectedMinuteString);
        showPlaces();
    }

    private void showPlaces() {
        Log.d("NIK", "selected hour " + selectedHour);
        String selectedHourString = String.valueOf(selectedHour);
        if (selectedHour < 10)
            selectedHourString = "0" + selectedHour;
        Calendar c = Calendar.getInstance();
        times.clear();
        for (int i = 0; i < timezones.size(); i++) {
            TimeZoneItem item = timezones.get(i);
            TimeZone tz = TimeZone.getTimeZone(item.getTimezoneID());
            SimpleDateFormat sdfForPrint = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdf = new SimpleDateFormat("HH");
            sdf.setTimeZone(tz);
            sdfForPrint.setTimeZone(tz);
            Log.d("TAG", "test time " + sdf.format(c.getTime()) + "for city " + item.getCity());
            if (sdf.format(c.getTime()).contains(selectedHourString)) {
               // times.add("City with time near to " + sdfForPrint.format(c.getTime()) + " is " + item.getCity());
                times.add("Timezone with time near to " + sdfForPrint.format(c.getTime()) + " is " + item.getTimezone());
                Log.d("NIK", "City with time " + sdfForPrint.format(c.getTime()) + " " + item.getCity());
            }
        }
        initList();
    }

    private void initList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, times);
        listView.setAdapter(adapter);
    }

    class AddTimeZonesTask extends AsyncTask<Void, Void, ArrayList<TimeZoneItem>> {
        @Override
        protected ArrayList<TimeZoneItem> doInBackground(Void... params) {
            timezones = dataBaseHelper.getTimes();
            return dataBaseHelper.getTimes();
        }
    }
}


