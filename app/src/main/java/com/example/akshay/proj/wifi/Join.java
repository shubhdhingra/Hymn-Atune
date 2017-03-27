package com.example.akshay.proj.wifi;

import com.example.akshay.proj.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.util.List;

public class Join extends ListActivity {
    private ArrayAdapter<String> adapter;
    WifiManager mainWifi;
    List<ScanResult> wifiList;
    private String networkSSID;
    ScanResult result;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join);
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (mainWifi.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();
            mainWifi.setWifiEnabled(true);
        }

//        receiverWifi = new WifiReceiver();
//        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
        updateWifiList();
    }
    public void updateWifiList()
    {
        adapter=null;
        wifiList=mainWifi.getScanResults();
        if(wifiList==null)
        {
            Toast.makeText(getApplicationContext(),"Groups Unavaliable Refresh Again",Toast.LENGTH_LONG).show();
            return;
        }
        String [] wifiName = new String[wifiList.size()];
        for(int i=0;i<wifiList.size();i++)
        {
            wifiName[i]=wifiList.get(i).SSID;
        }
        adapter=new ArrayAdapter<>(this,R.layout.list_item_wifi,wifiName);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        networkSSID=(String)l.getItemAtPosition(position);
        final int pos=position;
        result=wifiList.get(pos);
        JoinWifiNetwork joinWifiNetwork = new JoinWifiNetwork();
        joinWifiNetwork.execute((Void) null);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Refresh");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        mainWifi.startScan();
        Toast.makeText(getApplicationContext(),"Starting Scan...",Toast.LENGTH_SHORT).show();
        updateWifiList();
        return super.onMenuItemSelected(featureId, item);
    }
    private class JoinWifiNetwork extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

            WifiManager wifiManager = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.addNetwork(conf);
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
                wifiManager.startScan();
            }

            int netId = wifiManager.addNetwork(conf);
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Joined to "+networkSSID,Toast.LENGTH_SHORT).show();
                    System.out.println("SUCCESS ");
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getApplicationContext(), Receiver.class);
                    startActivity(i);
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable,1000);
        }
    }
/*
    private class JoinWifiNetwork extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            WifiConfiguration wifiConfig = new WifiConfiguration();
            wifiConfig.SSID = String.format("\"%s\"", networkSSID);
            wifiConfig.BSSID=result.BSSID;
            WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
            int netId = wifiManager.addNetwork(wifiConfig);
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Joined to "+networkSSID,Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getApplicationContext(), Receiver.class);
                    startActivity(i);
                }
            };
            Handler handler = new Handler();
            handler.postDelayed(runnable,5000);
        }
    }
*/
}