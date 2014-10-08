package com.wifitech.mywifi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 20140804-01 on 2014/9/28.
 */
public class ExpListAdapter extends BaseExpandableListAdapter {
    private List<ScanWifiInfo> wifiInfoList;
    private Context context;
    private LayoutInflater inflater;

    public ExpListAdapter(Context context, List<ScanWifiInfo> wifiInfoList) {
        this.context = context;
        this.wifiInfoList = wifiInfoList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return wifiInfoList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return wifiInfoList.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        return wifiInfoList.get(i).getChild();
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int position, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_group, null);
        }
        ImageView imageWifiSignal = (ImageView)convertView.findViewById(R.id.imageWifiSignal);
        ImageView imageLock = (ImageView)convertView.findViewById(R.id.imageLock);
        TextView textSSID = (TextView)convertView.findViewById(R.id.textSSID);
        TextView textIsConnected = (TextView)convertView.findViewById(R.id.textIsConnected);
        Button buttonConnect = (Button)convertView.findViewById(R.id.button_Connect);

        final ScanWifiInfo wifiInfo = wifiInfoList.get(position);
        imageWifiSignal.setImageDrawable(setWifiSignalImage(wifiInfo));
        imageLock.setImageDrawable(setLockImage(wifiInfo));
        textSSID.setText(wifiInfo.getSSID());

        if (wifiInfo.getConnectInfo()) {
            buttonConnect.setVisibility(View.INVISIBLE);
            textIsConnected.setVisibility(View.VISIBLE);
        } else {
            textIsConnected.setVisibility(View.INVISIBLE);
            buttonConnect.setVisibility(View.VISIBLE);
            buttonConnect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Context context = view.getContext();
                    final EditText editText = new EditText(context);
                    final WifiMgr wifiMgr = new WifiMgr(context);
                    String password = null;
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    editText.setHint("请输入密码");
                    builder.setTitle("即将连接："+wifiInfo.getSSID()).setView(editText)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String password = editText.getText().toString();
                                    System.out.println(password);

                                    if (wifiMgr.addNetwork(wifiMgr.createWifiInfo(
                                            wifiInfo, password))) {
                                        Toast.makeText(context, "已连接 " + wifiInfo.getSSID(), Toast.LENGTH_LONG).show();
                                        for (int j=0; j< wifiInfoList.size(); j++) {
                                            wifiInfoList.get(j).setConnectInfo(false);
                                        }
                                        wifiInfo.setConnectInfo(true);
                                    } else {
                                        Toast.makeText(context, "密码错误，请重试", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                }
            });
        }

        notifyDataSetChanged();
        return convertView;
    }

    private Drawable setWifiSignalImage(ScanWifiInfo wifiInfo) {
        if (Math.abs(wifiInfo.getLevel())>100) {
            return context.getResources().getDrawable(R.drawable.wifi_1);
        } else if (Math.abs(wifiInfo.getLevel())>80) {
            return context.getResources().getDrawable(R.drawable.wifi_2);
        } else if (Math.abs(wifiInfo.getLevel())>60) {
            return context.getResources().getDrawable(R.drawable.wifi_3);
        } else if (Math.abs(wifiInfo.getLevel())>50) {
            return context.getResources().getDrawable(R.drawable.wifi_4);
        } else {
            return context.getResources().getDrawable(R.drawable.wifi_5);
        }
    }

    private Drawable setLockImage(ScanWifiInfo wifiInfo) {
        if (WifiMgr.isLocked(wifiInfo))
            return context.getResources().getDrawable(R.drawable.ic_lock);
        else
            return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ArrayList<String> child = (ArrayList<String>) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_child, null);
        }

        TextView textBSSID = (TextView) convertView.findViewById(R.id.textBSSID);
        TextView textLevel = (TextView) convertView.findViewById(R.id.textLevel);
        TextView textCapabilities = (TextView) convertView.findViewById(R.id.textCapabilities);

        textBSSID.setText("               "+child.get(0));
        textLevel.setText("               "+child.get(1));
        textCapabilities.setText("               "+child.get(2));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}
