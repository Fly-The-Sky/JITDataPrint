package com.dicot.jitprint.activity.main;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dicot.jitprint.R;
import com.dicot.jitprint.base.BaseActivity;
import com.dicot.jitprint.utils.AppConst;
import com.dicot.jitprint.utils.MyUtil;
import com.dicot.jitprint.utils.PrefTool;
import com.dicot.jitprint.utils.PressUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import asp.lib.printer.DDPrinter;
import asp.lib.printer.MyDialog;

/**
 * Created by Hunter
 * on 2017/3/13.
 * Describe 打印机打印功能模块
 */
@ContentView(R.layout.activity_print_signtext)
public class PrintTextActivity extends BaseActivity {

    @ViewInject(value = R.id.print_module_edt_text)
    private EditText mEdtPrintText;

    private DDPrinter _printer;
    private String _printerName;
    private String _printerAddress;
    String mTypeMessage;

    @Override
    public void init() {
        super.init();
        x.view().inject(this);
    }

    @Override
    public void initData() {
        super.initData();
        isExistPrint();
        Intent intent = getIntent();
        mTypeMessage = intent.getStringExtra("typeText");
        setActionTitle(mTypeMessage + "打印功能");
        setBack();
    }

    private void isExistPrint() {
        try {
            PrefTool prefTool = new PrefTool(this, AppConst.PrintPrefInfo.PrintDefault);
            _printerName = prefTool.getStringPerf(AppConst.PrintPrefInfo.PrintName, "");
            _printerAddress = prefTool.getStringPerf(AppConst.PrintPrefInfo.PrintAddress, "");
            Log.e("PrefTool", _printerName + "|" + _printerAddress);
            if (_printerAddress.isEmpty()) {
                MyUtil.showToast(this, "打印机不存在");
                finish();
            }
            _printer = new DDPrinter(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.print_module_btn_text}, type = View.OnClickListener.class)
    private void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.print_module_btn_text:
                try {
                    // 防止连续点击按钮
                    if (PressUtil.isFastDoubleClick()) {
                        return;
                    }
                    print_text3();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        _printer.destroy();
        super.onDestroy();
    }

    // 三寸文字打印
    public void print_text3() {
        MyDialog.showProgress(this, "提示", "正在打印...");
        try {
            if (!_printer.open(_printerAddress)) {
                MyDialog.dismissProgress();
                MyUtil.showToast(this, _printer.MessageError);
                return;
            }
            int height = _printer.get_text_box_height(32, 16, 72, mEdtPrintText.getText().toString()) / 8;
            _printer.set_head_active(0);
            _printer.page_creat(72.0, 25.0 + height, 1);
            _printer.set_font_style(DDPrinter.FontName.FontCustom, DDPrinter.StyleNormal);
            _printer.draw_text(30.0, 5.0, 48, mTypeMessage, 0);
            _printer.draw_line(0.0, 13.0, 72.0, 13.0, 1);
            _printer.draw_text_box(5.0, 15.0, 32, 16, 60, mEdtPrintText.getText().toString());
            _printer.draw_line(0.0, 17.0 + height, 72.0, 17.0 + height, 1);

            _printer.page_print(DDPrinter.MarkNone);
            if (_printer.get_state(5000) == DDPrinter.PrinterState.Error) {
                MyUtil.showToast(this, _printer.MessageError);
            }
            if (!_printer.close())
                MyUtil.showToast(this, _printer.MessageError);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyDialog.dismissProgress();
    }
}
