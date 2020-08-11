package com.example.mark.disasterguard;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class SharedMethod {
    static private List<String> permissionsList = new ArrayList<String>();
    final static private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 200;

    //MarshMallow(API-23)之後要在 Runtime 詢問權限
    public static boolean needCheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 取得權限(for Android 6.0)
     *
     * @param activity
     * @param permission
     * @param permissionDescription
     */
    public static void getPermissions(final Activity activity, String[] permission, String[] permissionDescription) {
        List<String> permissionsNeeded = new ArrayList<String>();

        for(int i = 0; i < permission.length; i++) {
            if (!addPermission(activity, permission[i]))
                permissionsNeeded.add(permissionDescription[i]);
        }

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++) {
                    message = message + ", " + permissionsNeeded.get(i);
                }
                showDialog(activity, "", message,
                        "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                permissionsList.clear();
                            }
                        },
                        "Cancel", null);
                return;
            }
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            permissionsList.clear();
        }
    }

    private static boolean addPermission(Activity activity, String permission) {
        if (checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(activity, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 產生對話框(具有兩個選項按鈕)
     *
     * @param title     標題
     * @param message   文字
     * @param confirm1  確定文字1
     * @param listener1 按鈕監聽1
     * @param confirm2  確定文字2
     * @param listener2 按鈕監聽2
     */
    public static void showDialog(Activity activity, String title, String message,
                                  String confirm1, DialogInterface.OnClickListener listener1,
                                  String confirm2, DialogInterface.OnClickListener listener2) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle(title).setMessage(message)
                .setPositiveButton(confirm1, listener1)
                .setNegativeButton(confirm2, listener2)
                .show();
    }

}
