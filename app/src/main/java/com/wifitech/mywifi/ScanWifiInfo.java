package com.wifitech.mywifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 20140804-01 on 2014/9/28.
 */
public class ScanWifiInfo {
    private String wSSID;
    private String wBSSID;
    private String wCapabilities;
    private int wFrequency;
    private int wLevel;
    private boolean isConnected;

    public ScanWifiInfo(ScanResult scanResult, WifiInfo wifiInfo) {
        wSSID = scanResult.SSID;
        wBSSID = scanResult.BSSID;
        wCapabilities = scanResult.capabilities;
        wFrequency = scanResult.frequency;
        wLevel = scanResult.level;

        System.out.println(scanResult.toString());

        if (wifiInfo.getSSID().equals(wSSID) && wifiInfo.getBSSID().equals(wBSSID)) {
            isConnected = true;
        } else {
            isConnected = false;
        }
    }

    public String getSSID() {
        return wSSID;
    }

    public String getBSSID() {
        return wBSSID;
    }

    public String getCapabilities() {
        return wCapabilities;
    }

    public int getFrequency() {
        return wFrequency;
    }

    public int getLevel() {
        return wLevel;
    }

    public void setConnectInfo(boolean b) {
        isConnected = b;
    }

    public boolean getConnectInfo() {
        return isConnected;
    }

    public ArrayList<String> getChild() {
        ArrayList<String> child = new ArrayList<String>();
        child.add("BSSID        " + wBSSID);
        child.add("信号等级   " + String.valueOf(wLevel));
        child.add("频率           " + wFrequency + " MHz");
        return child;
    }

    public String toString() {
        return "SSID: " + getSSID() + " | BSSID: " + getBSSID()
                + " | Capabilities: " + getCapabilities() + " | Frequency: " + getFrequency()
                + " | Level: " + getLevel();
    }
}
