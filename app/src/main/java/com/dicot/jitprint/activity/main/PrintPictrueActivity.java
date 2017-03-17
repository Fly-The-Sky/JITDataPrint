package com.dicot.jitprint.activity.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dicot.jitprint.R;
import com.dicot.jitprint.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

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
    private ArrayList<String> mLstSelectPath;

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
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            String mSelectPath = mLstSelectPath.get(0);
            mTxtPrintPictruePath.setText(mSelectPath);
            x.image().bind(mImgPrintPictrue, mSelectPath);
        }
    }

}
