package com.dicot.jitprint.activity.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dicot.jitprint.MyApp;
import com.dicot.jitprint.R;
import com.dicot.jitprint.base.BaseActivity;
import com.dicot.jitprint.utils.AppConst;
import com.dicot.jitprint.utils.MyUtil;
import com.dicot.jitprint.utils.PhotoUtil;
import com.dicot.jitprint.utils.PrefTool;
import com.dicot.jitprint.utils.PressUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import asp.lib.printer.DDPrinter;
import asp.lib.printer.MyDialog;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by Hunter
 * on 2017/3/13.
 * Describe 打印机打印功能模块
 */
@ContentView(R.layout.activity_print_pictrue)
public class PrintPictrueActivity extends BaseActivity {
    private static final int REQUEST_IMAGE = 2;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    @ViewInject(value = R.id.print_pictrue_txt_name)
    private TextView mTxtPrintPictruePath;
    @ViewInject(value = R.id.print_pictrue_imageView)
    private ImageView mImgPrintPictrue;
    //    @ViewInject(R.id.print_btn_pictrue)
    //    private Button mBtnPrintPictrue;
    private ArrayList<String> mLstSelectPath;
    private String mSelectPath;


    private DDPrinter _printer;
    private String _printerName;
    private String _printerMac;

    @Override
    public void init() {
        super.init();
        x.view().inject(this);
        setActionTitle("打印图片功能");
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
            _printerMac = prefTool.getStringPerf(AppConst.PrintPrefInfo.PrintAddress, "");
            Log.e("PrefTool", _printerName + "|" + _printerMac);
            if (_printerMac.isEmpty()) {
                MyUtil.showToast(this, "打印机不存在");
                finish();
            }
            _printer = new DDPrinter(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.print_btn_pictrue}, type = View.OnClickListener.class)
    private void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.print_btn_pictrue:
                try {
                    // 防止连续点击按钮
                    if (PressUtil.isFastDoubleClick()) {
                        return;
                    }
                    print_picture3();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_pictrue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_main_pictrue) {
            pickImage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.mis_permission_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            //d调用第三方获取多图片的方法
            MultiImageSelector selector = MultiImageSelector.create(PrintPictrueActivity.this);
            selector.showCamera(true);
            selector.single();
            selector.start(PrintPictrueActivity.this, REQUEST_IMAGE);
        }
    }

    private void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.mis_permission_dialog_title)
                    .setMessage(rationale)
                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(PrintPictrueActivity.this, new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_READ_ACCESS_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            mLstSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            mSelectPath = mLstSelectPath.get(0);
            mTxtPrintPictruePath.setText(mSelectPath);
            x.image().bind(mImgPrintPictrue, mSelectPath);
        }
    }

    // 三寸图片打印
    public void print_picture3() {
        MyDialog.showProgress(this, "提示", "正在打印...");

        try {
            if (!_printer.open(_printerMac)) {
                MyDialog.dismissProgress();
                MyUtil.showToast(this, _printer.MessageError);
                return;
            }
            _printer.set_head_active(0);//选择两寸还是三寸打印头
            Bitmap bmpSrc = BitmapFactory.decodeFile(mSelectPath);
            _printer.page_creat(72.0, 78.0, 1);
            _printer.draw_box(1.0, 1.0, 71.0, 66.5, 2);
            //        _printer.set_font_file(DDPrinter.FontName.FontCustom, "assets/fonts/mengmeng-en.ttf");
            _printer.draw_bitmap(10.0, 5.0, PhotoUtil.zoomBitmap(bmpSrc, 400, 500), false);
            //        _printer.draw_bitmap(20.0, 30.0, PhotoUtil.zoomBitmap(bmpSrc, 200, 200), true);
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
