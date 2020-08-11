package com.example.mark.disasterguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Random;

/**
 * Created by Jaison on 17/06/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";
    Random random=new Random(); //生成隨機數
    int ran_num = random.nextInt(3);

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
    String Knowledge_Ini ="逃生通道要隨時檢查是否清空";
    String Knowledge_ran0 = "逃生通道要隨時檢查是否清空";
    String Knowledge_ran1 = "滅火器必須保持足夠壓力";
    String Knowledge_ran2 = "逃生動線要熟練";
    switch(ran_num)
    {
        case(0):
            Knowledge_Ini = Knowledge_ran0;
            break;
        case(1):
            Knowledge_Ini = Knowledge_ran1;
            break;
        case(2):
            Knowledge_Ini = Knowledge_ran2;
            break;

    }
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        //Trigger the notification
        NotificationScheduler.showNotification(context, KnowledgeActivity.class,
                "防災小知識", Knowledge_Ini);
    }
}


