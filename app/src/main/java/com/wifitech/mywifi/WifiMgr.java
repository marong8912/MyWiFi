package com.wifitech.mywifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rong.Ma on 2014/9/28.
 */
public class WifiMgr {
    private WifiManager wifiManager;
    private ArrayList<ScanWifiInfo> wifiList = new ArrayList<ScanWifiInfo>();
    private List<WifiConfiguration> wifiConfigurationList;

    /**
     * Constructor
     * @param context
     */
    public WifiMgr(Context context) {
        wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
    }


    public void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    public void closeWifi() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public void scanWifi() {
        List<ScanResult> scanResultList = wifiManager.getScanResults();
        wifiConfigurationList = wifiManager.getConfiguredNetworks();
        int iCount = scanResultList.size();
        int jCount = scanResultList.size();

        for (int i=0; i<iCount; i++) {
            int maxPos = 0;
            for (int j=0; j<jCount; j++) {
                if (scanResultList.get(maxPos).level < scanResultList.get(j).level)
                    maxPos = j;
            }
            ScanWifiInfo scanWifiInfo =
                    new ScanWifiInfo(scanResultList.get(maxPos), wifiManager.getConnectionInfo());
            scanResultList.remove(maxPos);
            wifiList.add(scanWifiInfo);
            jCount--;
        }
    }

    public static boolean isLocked(ScanWifiInfo wifiInfo) {
        if (wifiInfo.getCapabilities().contains("WEP") || wifiInfo.getCapabilities().contains("WPA"))
            return true;
        else
            return false;
    }

    public List<ScanWifiInfo> getWifiList() {
        return wifiList;
    }

    public void setListEmpty() {
        wifiList.clear();
    }

    public List<WifiConfiguration> GetWifiConfigList() {
        return wifiConfigurationList;
    }

    public boolean addNetwork(WifiConfiguration wifiConfig) {
        return wifiManager.enableNetwork(wifiManager.addNetwork(wifiConfig), true);
    }

    public WifiConfiguration createWifiInfo(ScanWifiInfo wifiInfo, String password) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\""+wifiInfo.getSSID() + "\"";
        System.out.println(config.SSID); //

        if (wifiInfo.getCapabilities().contains("WPA")) {
            config.preSharedKey = "\""+password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        } else if (wifiInfo.getCapabilities().contains("WEP")) {
            config.wepKeys[0]= "\""+password+"\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        return config;
    }
}
