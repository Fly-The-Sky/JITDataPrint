package com.dicot.jitprint.activity.main;


import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dicot.jitprint.MyApp;
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

    @Override
    public void init() {
        super.init();
        x.view().inject(this);
        setActionTitle("打印便签功能");
        setBack();
    }

    @Override
    public void initData() {
        super.initData();
        isExistPrint();
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
            _printer = new DDPrinter(getApplicationContext());
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

    // 三寸文字打印
    public void print_text3() {
        MyDialog.showProgress(this, "提示", "正在打印...");
        try {
            if (!_printer.open(_printerAddress)) {
                MyDialog.dismissProgress();
                MyUtil.showToast(this, _printer.MessageError);
                return;
            }
            _printer.set_head_active(0);
            _printer.page_creat(72.0, 60.0, 1);
            _printer.draw_box(1.0, 1.0, 71.0, 96, 2);

            //            _printer.set_font_style(DDPrinter.FontName.FontLocal_GBK24x24, DDPrinter.StyleBlodItalic);
            //            _printer.draw_text(6.0, 5, 0, "打印文字", 0);
            //            _printer.draw_text(10.0, 10, 1, "www.ddmic.com", 0);
            //            _printer.draw_text(10.0, 15.0, 2, "丁丁", 0);
            //
            //            _printer.set_font_style(DDPrinter.FontName.FontLocal_GBK16x16, DDPrinter.StyleBlodItalic);
            //            _printer.draw_text(10.0, 20.0, 1, "www.ddmic.com", 0);
            //            _printer.draw_text(10.0, 25.0, 0, "丁丁", 0);
            //
            _printer.set_font_style(DDPrinter.FontName.FontDefault, DDPrinter.StyleBlodItalic);
            _printer.draw_text(10.0, 30.0, 32, mEdtPrintText.getText().toString(), 0);
            //
            //            _printer.set_font_style(DDPrinter.FontName.FontCustom, DDPrinter.StyleBlodItalic);
            //            _printer.draw_text(10.0, 45.0, 30, "www.ddmic.com", 0);
            //            _printer.draw_text(10.0, 50.0, 30, "丁丁", 0);
            //
            //            _printer.set_font_style(DDPrinter.FontName.FontSerif, DDPrinter.StyleBlodItalic);
            //            _printer.draw_text(10.0, 55.0, 32, "www.ddmic.com", 0);
            //            _printer.draw_text(10.0, 60.0, 54, "丁丁", 0);
            //
            //            _printer.set_font_style(DDPrinter.FontName.FontSans, DDPrinter.StyleBlodItalic);
            //            _printer.draw_text(10.0, 70.0, 28, "www.ddmic.com", 0);
            //            _printer.draw_text(10.0, 75.0, 28, "丁丁", 0);
            //
            //            _printer.set_font_style(DDPrinter.FontName.FontMonospace, DDPrinter.StyleBlodItalic);
            //            _printer.draw_text(10.0, 80.0, 32, "www.ddmic.com", 0);
            //            _printer.draw_text(10.0, 85.0, 54, "丁丁", 0);

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
