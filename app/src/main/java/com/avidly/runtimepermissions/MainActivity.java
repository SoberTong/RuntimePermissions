package com.avidly.runtimepermissions;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_CODE = 10000;   //请求码
    private int request_times = 0;      //请求次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showPermissionSettingsDialog("为了可以使用附近的人等功能，获得更好的游戏体验，需要获取手机定位权限", 1);
                }else {
                    showPermissionSettingsDialog("需要获取手机定位权限", 0);
                }
            }else {
                Toast.makeText(this, "已经获取到定位权限", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "不需要获取运行时权限", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            request_times++;
            int grantResult = grantResults[0];
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "定位权限授权成功", Toast.LENGTH_LONG).show();
            }else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showPermissionSettingsDialog("由于您勾选不再提示授权，如需获得更好的游戏体验，请打开权限：设置－应用－捕鱼－权限－定位，谢谢！", 2);
                }else {
                    if (request_times < 2)
                        checkPermission();
                    if (request_times == 2)
                        showPermissionSettingsDialog("由于您已拒绝授予定位权限，游戏定位相关功能将无法正常使用。", 2);
                }
            }
        }
    }

    private void showPermissionSettingsDialog(String context, final int tag) {
        new AlertDialog.Builder(this).setCancelable(false).setTitle("温馨提示").
                setMessage(context).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ((tag == 0 || tag == 1) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                }
            }
        }).show();
    }
}
