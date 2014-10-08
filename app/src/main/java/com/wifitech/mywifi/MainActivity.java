package com.wifitech.mywifi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends Activity {
    private WifiMgr wifiMgr;
    private ExpandableListView expandableListView;
    private Context context = this;
    private ExpListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        wifiMgr = new WifiMgr(this);
        setWifiSwitch();
        wifiMgr.openWifi();
        wifiMgr.scanWifi();
        expandableListView = (ExpandableListView)findViewById(R.id.expandableListView);

        if (wifiMgr.getWifiList().isEmpty()) {
            Toast.makeText(this, R.string.no_wifi, Toast.LENGTH_LONG).show();
        } else {
            adapter = new ExpListAdapter(this, wifiMgr.getWifiList());
            expandableListView.setAdapter(adapter);
        }
    }

    private void setWifiSwitch() {
        Switch switchWifi = (Switch)findViewById(R.id.switch2);
        CompoundButton.OnCheckedChangeListener listener =
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            wifiMgr.openWifi();
                            adapter.notifyDataSetChanged();
                        } else {
                            wifiMgr.closeWifi();
                            Toast.makeText(context, "请打开Wi-Fi", Toast.LENGTH_LONG).show();
                            wifiMgr.setListEmpty();
                            adapter.notifyDataSetChanged();
                        }
                    }
                };
        switchWifi.setOnCheckedChangeListener(listener);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
