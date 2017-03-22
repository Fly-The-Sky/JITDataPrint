package com.dicot.jitprint.print;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dicot.jitprint.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@ContentView(R.layout.searchprints)
public class SearchPrinter extends Activity {
    int COD_UNKOWN = 0;
    int COD_PRINTER = 1;
    int COD_COMPUTER = 2;
    int COD_PHONE = 3;
    int COD_AUDIO = 4;

    private BluetoothAdapter mBtAdapter;
    @ViewInject(R.id.btn_search_close)
    private Button CloseButton;
    @ViewInject(R.id.btn_search_s)
    private Button searchButton;
    @ViewInject(R.id.txt_search_status)
    private TextView textStatus;
    @ViewInject(R.id.listView_search_print)
    private ListView _btDevicesListView;

    private List<String> _bluetoothDeviceList; // name,address,installed,cod
    private List<String> _printerList;
    private ThreadTimeOut _thTimeOut;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public String btname;
    static public String printerAddress;
    static public String printerName;
    static public BluetoothDevice device;
    public static BluetoothSocket btSocket;
    private int _timeOut;

    // private StatusBox _waitDialog ;
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        WindowManager wm = this.getWindowManager();
        int height = wm.getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams params = _btDevicesListView.getLayoutParams();
        params.height = height / 2;
        _btDevicesListView.setLayoutParams(params);
        _btDevicesListView.setOnItemClickListener(mDeviceClickListener);
        _bluetoothDeviceList = new ArrayList<String>();
        _printerList = new ArrayList<String>();
        printer_list_load();
        _timeOut = -1;
        _thTimeOut = new ThreadTimeOut();
        _thTimeOut.start();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter.isEnabled()) {
            search_printers();
        }
    }

    @Event(value = {R.id.btn_search_s, R.id.btn_search_close}, type = OnClickListener.class)
    private void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_s:
                if (Utils.isFastClick())
                    return;
                search_printers();
                break;
            case R.id.btn_search_close:
                if (Utils.isFastClick()) {
                    return;
                }
                setResult(Activity.RESULT_CANCELED, null);
                finish();

                unregisterReceiver(mReceiver);
                break;
        }
    }

    // 防止连续点击按钮
    public static class Utils {
        private static long lastClickTime;

        public synchronized static boolean isFastClick() {
            long time = System.currentTimeMillis();
            if (time - lastClickTime < 500) {
                return true;
            }
            lastClickTime = time;
            return false;
        }
    }

    private List<? extends Map<String, ?>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String strArray[] = new String[5];
        String pattern = ",";
        Pattern pat = Pattern.compile(pattern);
        for (int i = 0; i < _bluetoothDeviceList.size(); i++) {
            strArray = pat.split(_bluetoothDeviceList.get(i));
            Map<String, Object> map = new HashMap<String, Object>();
            if (Integer.valueOf(strArray[4]).intValue() > 0)
                map.put("text1", "已配对");
            if (Integer.valueOf(strArray[2]).intValue() > 0)
                map.put("text1", "已安装");
            int type = Integer.valueOf(strArray[3]).intValue();
            if (type == 0)
                map.put("img_pre", R.drawable.cod0);
            else if (type == 1)
                map.put("img_pre", R.drawable.cod1);
            else if (type == 2)
                map.put("img_pre", R.drawable.cod2);
            else if (type == 3)
                map.put("img_pre", R.drawable.cod3);
            else if (type == 4)
                map.put("img_pre", R.drawable.cod4);
            map.put("text", strArray[0]);
            map.put("text2", strArray[1]);
            list.add(map);
        }
        return list;
    }

    /**
     * 搜索打印机
     */
    private void search_printers() {
        String str = getString(R.string.PrinterSearching) + String.format("(%s*)", "");
        textStatus.setText(str);
        _timeOut = 10;
        _bluetoothDeviceList.clear();
        Message message = new Message();
        message.what = 1;
        handlerUIUpdata.sendMessage(message);// 发送消息
        String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(ACTION_PAIRING_REQUEST);
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, intent);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        mBtAdapter.startDiscovery();
    }

    private void back_last_activity() {
        Intent intent = new Intent();
        intent.putExtra("PrinterName", printerName);
        intent.putExtra("PrinterAddress", printerAddress);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    // ///////////////点击list相应
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            mBtAdapter.cancelDiscovery();
            String pattern = ",";
            Pattern pat = Pattern.compile(pattern);
            String str1 = _bluetoothDeviceList.get(arg2);
            String[] strArray = new String[5];
            strArray = pat.split(str1);
            printerAddress = strArray[1];
            printerName = strArray[0];
            int isInstalled = Integer.valueOf(strArray[2]).intValue();
            if (isInstalled > 0) {
                back_last_activity();
                Toast.makeText(getApplicationContext(), "打印机已经存在", Toast.LENGTH_SHORT).show();
                return;
            }
            BluetoothDevice btDev = mBtAdapter.getRemoteDevice(printerAddress);
            try {
                if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
                    btDev.createBond();
                } else if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
                    back_last_activity();
                    Toast.makeText(getApplicationContext(), "打印机已添加", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    void printer_list_load() {
        SharedPreferences mySP = getSharedPreferences("dding_printer_list", Activity.MODE_WORLD_READABLE);
        int count = mySP.getInt("PrinterCount", 0);
        if (count == 0)
            return;
        for (int i = 0; i < count; i++)
            _printerList.add(mySP.getString("Printer" + i, ""));
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 当发现设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                int type = COD_UNKOWN;
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BluetoothClass btc = device.getBluetoothClass();
                if (btc != null) {
                    int cod = btc.getDeviceClass();
                    int cod_maj = cod / 256;
                    int cod_min = cod % 256;
                    if (cod_maj == 0x06 && cod_min == 0x80)
                        type = COD_PRINTER; // printer
                    else if (cod_maj == 0x01)
                        type = COD_COMPUTER; // cpu
                    else if (cod_maj == 0x02)
                        type = COD_PHONE; // phone
                    else if (cod_maj == 0x04)
                        type = COD_AUDIO; // audio
                    else
                        type = COD_UNKOWN;
                }


                {
                    String strInstalled = "0";
                    String strCOD = String.valueOf(type);
                    String strBonded = "0";

                    String pattern = ",";
                    Pattern pat = Pattern.compile(pattern);
                    String[] strArray = new String[2];
                    for (int m = 0; m < _printerList.size(); m++) {
                        strArray = pat.split(_printerList.get(m));
                        if (strArray[1].equals(device.getAddress()))
                            strInstalled = "1";
                    }
                    if (device.getBondState() == device.BOND_BONDED)
                        strBonded = "1";
                    _bluetoothDeviceList.add(device.getName() + "," + device.getAddress() + "," + strInstalled + ","
                            + strCOD + "," + strBonded);
                    Message message = new Message();
                    message.what = 1;
                    handlerUIUpdata.sendMessage(message);// 发送消息
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        Log.d("long", "正在配对......");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.d("long", "完成配对");
                        // _waitDialog.Close();
                        back_last_activity();
                        break;
                    case BluetoothDevice.BOND_NONE:
                        // _waitDialog.Close();
                        Log.d("long", "取消配对");
                    default:
                        break;
                }
            } else if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
                // device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // try {Thread.sleep(500);} catch (InterruptedException e1) {}
                // try { device.setPin("0000".getBytes());} catch (Exception e) {}
            }
        }
    };

    /**
     * UI 数据更新
     */
    private void ui_update() {
        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.newfile,
                new String[]{"img_pre", "text", "text2", "text1"},
                new int[]{R.id.img_pre, R.id.text, R.id.text2, R.id.text1});
        _btDevicesListView.setAdapter(adapter);
    }

    Handler handlerUIUpdata = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ui_update();
                    break;
                case 2:
                    String str = getString(R.string.ButtonSearchText) + String.format("(%d)", _timeOut);
                    searchButton.setText(str);
                    break;
                case 3:
                    textStatus.setText(getString(R.string.SearchFinishMessage) + String.format(" (%s*)", ""));
                    searchButton.setText(R.string.ButtonSearchText);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    class ThreadTimeOut extends Thread {
        public void run() {
            while (true) {
                if (_timeOut > 0) {
                    Message msg = new Message();
                    msg.what = 2;
                    handlerUIUpdata.sendMessage(msg);
                    _timeOut--;
                } else if (_timeOut == 0) {
                    Message msg = new Message();
                    msg.what = 3;
                    handlerUIUpdata.sendMessage(msg);
                    _timeOut--;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 被销毁的时候调用
    public void onDestory() {
        setContentView(null);
        onCreate(null);
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

}
