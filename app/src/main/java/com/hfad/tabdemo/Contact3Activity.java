package com.hfad.tabdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Contact3Activity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 100;
    private static final int PERMISSIONS_REQUEST_SMS_MESSAGE = 100;
    Button ZButton;
    Button callButton;
    EditText subject;
    TextView phoneno;
    Button msgButton;
    Button popUpDismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact3);

        ZButton = (Button) findViewById(R.id.callButton);
        phoneno = (TextView)findViewById(R.id.phoneno) ;
        msgButton=(Button)findViewById(R.id.msgButton);

        ZButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (phoneno.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter some digits", Toast.LENGTH_SHORT).show();
                }
                else
                    call();
            }
        });



        ZButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
// TODO Auto-generated method stub
                showPopup();
                return false;
            }
        });

        msgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("sms:" + phoneno.getText().toString()));
                startActivity(smsIntent);
            }
        });
    }


    private PopupWindow pw;
    private void showPopup() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup_1));

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);

            int width= dm.widthPixels;
            int height = dm.heightPixels;

            pw = new PopupWindow(layout,(int)(width*0.8),(int)(height*0.6), true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                pw.setElevation(15);
            }
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            callButton = (Button) layout.findViewById(R.id.call_message);
            subject=(EditText)layout.findViewById(R.id.subject);
            callButton.setOnClickListener(callmessage);
            popUpDismiss=(Button)layout.findViewById(R.id.popUpDismissButton);
            popUpDismiss.setOnClickListener(popupdismiss);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private View.OnClickListener popupdismiss = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pw.dismiss();
        }
    };
    private View.OnClickListener callmessage = new View.OnClickListener() {
        public void onClick(View v) {
            if (phoneno.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Enter some digits", Toast.LENGTH_SHORT).show();
                pw.dismiss();
            }
            else {
                if (subject.getText().toString().isEmpty()) {
                    call();
                    pw.dismiss();
                }
                else {
                    send();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            call();  // this code will be executed after 8 seconds
                        }
                    }, 8000);
                    pw.dismiss();
                }
            }
        }
    };

    private void call() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_PHONE_CALL);
        } else {
            //Open call function
            // String number = "";
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:"+phoneno.getText()));
            startActivity(intent);
        }
    }

    private void send() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SMS_MESSAGE);
        } else {
            //Open sms function
            // String subject = "";
            sendSMS();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_SMS_MESSAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                sendSMS();
            } else {
                Toast.makeText(this, "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISSIONS_REQUEST_PHONE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                call();
            } else {
                Toast.makeText(this, "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void sendSMS() {
        String phoneNumber = phoneno.getText().toString();
        String msg=subject.getText().toString();
        msg = "CallXT: " + msg;
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, msg, null, null);
    }


}
