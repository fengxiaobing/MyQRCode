package com.example.rf.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.bj.qrcodelibrary.CaptureActivity;
import com.bj.qrcodelibrary.QRCodeIntent;
import com.example.rf.myapplication.util.ToastUtil;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 10;
    private int jumpCode = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void qrCodeScan(View v) {
        jumpCode = 0;
        requestPermission(CaptureActivity.class);
    }

    public void qrCodeCreate(View v) {
        jumpCode = 1;
//        requestPermission(CreateQRCodeActivity.class);
    }

    public void qrCodePicture(View v) {
        jumpCode = 2;
//        requestPermission(PictureIdentificationActivity.class);
    }

    /*权限申请,相机，存储，蜂鸣*/
    private void requestPermission(Class activity) {
        String[] permission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.VIBRATE,
                Manifest.permission.CAMERA,
        };
        boolean flag = true;
        for (String aPermission : permission) {
            if (ActivityCompat.checkSelfPermission(this, aPermission) != PackageManager.PERMISSION_GRANTED) {
                flag = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, aPermission)) {
                    ToastUtil.showToast(this, "您已禁止所需要权限，需要重新开启");
                } else {
                    ActivityCompat.requestPermissions(this, permission, REQUEST_CODE);
                }
            } else {
                flag &= flag;
            }
        }
        if (flag) {
            Intent intent = new Intent(this, activity);
            if (jumpCode == 0) {
                //预览框的宽高
                intent.putExtra(QRCodeIntent.FRAME_WIDTH, 200);
                intent.putExtra(QRCodeIntent.FRAME_HEIGHT, 180);
                intent.putExtra(QRCodeIntent.SET_RESULT, true);
                startActivityForResult(intent, 10);
            } else {
                startActivity(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            boolean flag = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    flag = false;
                } else {
                    flag &= flag;
                }
            }

            if (flag) {
                Intent intent = new Intent();
                switch (jumpCode) {
                    case 0:
                        intent.setClass(this, CaptureActivity.class);
                        break;
                    case 1:
//                        intent.setClass(this, CreateQRCodeActivity.class);
                        break;
                    case 2:
//                        intent.setClass(this, PictureIdentificationActivity.class);
                        break;
                    default:
                        break;
                }
                if (jumpCode >= 0 && grantResults.length > 0)
                    startActivity(intent);
            } else {
                ToastUtil.showToast(this, "您已禁止所需要权限，需要重新开启");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode) {
            //接收返回值
            if (data != null && !TextUtils.isEmpty(data.getStringExtra(QRCodeIntent.SCAN_RESULT))) {
                ToastUtil.showToast(this, data.getStringExtra(QRCodeIntent.SCAN_RESULT));
            }
        }
    }
}
