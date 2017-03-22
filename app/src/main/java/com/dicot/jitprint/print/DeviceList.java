package com.dicot.jitprint.print;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.dicot.jitprint.R;
import com.dicot.jitprint.utils.AppConst;
import com.dicot.jitprint.utils.PrefTool;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import asp.lib.bt.BluetoothState;

import static android.R.id.list;

@ContentView(R.layout.layout_cft)
public class DeviceList extends Activity {
    public static String gPin;
    public static String gName;
    static public String printerAddress;
    static public String printerName;
    @ViewInject(R.id.list_cft_view_printers)
    ListView _listViewPrinters;
    @ViewInject(R.id.btn_cft_add)
    Button _buttonAddPrinter;
    @ViewInject(R.id.btn_cft_back)
    Button _buttonCancel;
    private String m_PrinterDefaultAddress;
    private String m_PrinterDefaultName;
    private int _defaultPrinterIndex;
    private List<String> _printerList;
    private MyThread _threadUpdataUI;
    public static final String TAG = "menu";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        x.view().inject(this);

        _listViewPrinters.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                // final AdapterView.AdapterContextMenuInfo info =
                // (AdapterView.AdapterContextMenuInfo) menuInfo;
                menu.setHeaderTitle("设置");
                menu.add(0, 0, 0, "删除打印机");
                menu.add(0, 1, 0, "设置默认打印机");
            }
        });
        gPin = "0000";
        gName = "";

        _printerList = new ArrayList<String>();
        printer_list_load();
        _threadUpdataUI = new MyThread();
        _threadUpdataUI.run();
    }

    @Event(value = {R.id.btn_cft_add, R.id.btn_cft_back}, type = OnClickListener.class)
    private void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cft_add:
                if (Utils.isFastClick()) {
                    return;
                }
                show_search_printer_activity();
                break;
            case R.id.btn_cft_back:
                back_last_activity();
                finish();
                break;
        }
    }

    @Event(value = {R.id.list_cft_view_printers}, type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        _defaultPrinterIndex = position;
        printerSharedPrefSave(_printerList, _defaultPrinterIndex);
        printer_list_load();
        printerInfoSave(m_PrinterDefaultName, m_PrinterDefaultAddress);
        finish();
    }

    void printerInfoSave(String printerName, String printerAddress) {
        PrefTool prefTool = new PrefTool(this, AppConst.PrintPrefInfo.PrintDefault);
        prefTool.setStringSave(AppConst.PrintPrefInfo.PrintName, printerName);
        prefTool.setStringSave(AppConst.PrintPrefInfo.PrintAddress, printerAddress);
    }

    void printer_list_load() {
        SharedPreferences mySP1 = getSharedPreferences("dding_printer_list", Activity.MODE_WORLD_READABLE);
        int count = mySP1.getInt("PrinterCount", 0);
        int index = mySP1.getInt("DefaultPrinterIndex", 0);
        _defaultPrinterIndex = index;
        _printerList.clear();
        if (count == 0)
            return;
        if ((_defaultPrinterIndex + 1) > count)
            _defaultPrinterIndex = count - 1;
        for (int i = 0; i < count; i++)
            _printerList.add(mySP1.getString("Printer" + i, ""));
        String pattern = ",";
        Pattern pat = Pattern.compile(pattern);
        String str = _printerList.get(index);
        String[] strArray = new String[2];
        strArray = pat.split(str);
        m_PrinterDefaultName = strArray[0];
        m_PrinterDefaultAddress = strArray[1];
    }

    void printerSharedPrefSave(List list, int defaultPrinterIndex) {
        SharedPreferences.Editor editor1 = getSharedPreferences("dding_printer_list", Activity.MODE_WORLD_WRITEABLE)
                .edit();
        editor1.putInt("PrinterCount", list.size());
        editor1.putInt("DefaultPrinterIndex", defaultPrinterIndex);
        for (int i = 0; i < list.size(); i++) {
            editor1.remove("Printer" + i);
            editor1.putString("Printer" + i, (String) list.get(i));
        }
        editor1.commit();
    }

    /**
     * UI 数据刷新
     */
    private void ui_refresh() {
        SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.newfile,
                new String[]{"img_pre", "text", "text2", "text1"},
                new int[]{R.id.img_pre, R.id.text, R.id.text2, R.id.text1});
        _listViewPrinters.setAdapter(adapter);
    }

    private List<? extends Map<String, ?>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String strArray[] = new String[2];
        String pattern = ",";
        Pattern pat = Pattern.compile(pattern);

        for (int i = 0; i < _printerList.size(); i++) {
            strArray = pat.split(_printerList.get(i));
            Map<String, Object> map = new HashMap<String, Object>();
            if (i == _defaultPrinterIndex) {
                map.put("text1", "默认");
            }
            map.put("text", strArray[0]);
            map.put("text2", strArray[1]);
            map.put("img_pre", R.drawable.printer);
            list.add(map);
        }
        return list;
    }

    public class MyThread implements Runnable {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);// 发送消息
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ui_refresh();
            super.handleMessage(msg);
        }
    };

    public boolean onContextItemSelected(MenuItem item) {
        ContextMenuInfo info = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) info;
        if (item.getItemId() == 0) {
            if (contextMenuInfo.position == _defaultPrinterIndex)
                _defaultPrinterIndex = 0;
            else if (contextMenuInfo.position < _defaultPrinterIndex)
                _defaultPrinterIndex -= 1;

            _printerList.remove(contextMenuInfo.position);
            printerSharedPrefSave(_printerList, _defaultPrinterIndex);
            printer_list_load();
            _threadUpdataUI.run();
        }
        if (item.getItemId() == 1) {
            _defaultPrinterIndex = contextMenuInfo.position;
            printerSharedPrefSave(_printerList, _defaultPrinterIndex);
            printer_list_load();
            back_last_activity();
            printerInfoSave(m_PrinterDefaultName, m_PrinterDefaultAddress);
            _threadUpdataUI.run();
        }
        return super.onContextItemSelected(item);
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

    private void wait_ms(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    private void show_search_printer_activity() {
        Intent intent = new Intent(this, SearchPrinter.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    private void back_last_activity() {
        Intent intent = new Intent();
        intent.putExtra("PrinterName", m_PrinterDefaultName);
        intent.putExtra("PrinterAddress", m_PrinterDefaultAddress);
        setResult(Activity.RESULT_OK, intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            switch (resultCode) {
                case RESULT_OK:
                    Bundle b = data.getExtras(); // data为B中回传的Intent
                    m_PrinterDefaultName = b.getString("PrinterName");
                    m_PrinterDefaultAddress = b.getString("PrinterAddress");

                    String[] strArray = new String[2];
                    String pattern = ",";
                    Pattern pat = Pattern.compile(pattern);
                    for (int i = 0; i < _printerList.size(); i++) {
                        strArray = pat.split(_printerList.get(i));
                        if (strArray[1].equals(m_PrinterDefaultAddress))
                            _printerList.remove(i);
                    }
                    _printerList.add(m_PrinterDefaultName + "," + m_PrinterDefaultAddress);
                    _defaultPrinterIndex = _printerList.size() - 1;
                    printerSharedPrefSave(_printerList, _defaultPrinterIndex);
                    _threadUpdataUI.run();
                    break;
                default:
                    break;
            }
        }
    }
}
