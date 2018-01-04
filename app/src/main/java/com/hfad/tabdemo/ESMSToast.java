package com.hfad.tabdemo;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Aravind on 10/09/2016.
 */
public class ESMSToast {

    public void showToast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.etoast_sms, null);
        TextView text = (TextView) layout.findViewById(R.id.toast_sms_text);
        text.setText(message);
        Toast etoast = new Toast(context);
        etoast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        etoast.setDuration(Toast.LENGTH_LONG);
        etoast.setView(layout);
        etoast.show();
    }
}
