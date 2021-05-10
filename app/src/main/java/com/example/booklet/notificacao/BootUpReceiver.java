package com.example.booklet.notificacao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.booklet.fragment.TodoFragment;

public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, TodoFragment.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }

}
