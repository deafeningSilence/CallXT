package com.hfad.tabdemo;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoneDialerFragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_PHONE_CALL = 100;
    private static final int PERMISSIONS_REQUEST_SMS_MESSAGE = 100;
    Button callz;
    Button Create;
    EditText subject;
    EditText phoneno;
    Button popUpDismiss;


    public PhoneDialerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_dialer, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();


        Create = (Button)getView().findViewById(R.id.btnDial);
        initializeView();
        Create.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopup();
                return false;
            }
        });


    }

    private void initializeView() {
        phoneno = (EditText)getView().findViewById(R.id.screen);
        int idList[] = {R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnDial, R.id.btnDel, R.id.btnStar,
                R.id.btnZero, R.id.btnHash};

        for (int d : idList) {
            View v = (View)getView().findViewById(d);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.btn1:
                            display("1");
                            break;
                        case R.id.btn2:
                            display("2");
                            break;
                        case R.id.btn3:
                            display("3");
                            break;
                        case R.id.btn4:
                            display("4");
                            break;
                        case R.id.btn5:
                            display("5");
                            break;
                        case R.id.btn6:
                            display("6");
                            break;
                        case R.id.btn7:
                            display("7");
                            break;
                        case R.id.btn8:
                            display("8");
                            break;
                        case R.id.btn9:
                            display("9");
                            break;
                        case R.id.btnZero:
                            display("0");
                            break;
                        case R.id.btnStar:
                            display("*");
                            break;
                        case R.id.btnHash:
                            display("#");
                            break;
                        case R.id.btnDial:

                            if (phoneno.getText().toString().isEmpty())
                                Toast.makeText(getActivity().getApplicationContext(), "Enter some digits", Toast.LENGTH_SHORT).show();
                            else
                                call();
                            break;
                        case R.id.btnDel:

                            if (phoneno.getText().toString().length() >= 1) {
                                String newScreen = phoneno.getText().toString().substring(0, phoneno.getText().toString().length() - 1);
                                phoneno.setText(newScreen);
                            }
                            break;
                        default:
                            break;
                    }

                }
            });
        }
    }

    public void display(String val) {
        phoneno.append(val);
    }


    private PopupWindow pw;
    private void showPopup() {
        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup)getView().findViewById(R.id.popup_1));

            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

            int width= dm.widthPixels;
            int height = dm.heightPixels;

            pw = new PopupWindow(layout,(int)(width*0.8),(int)(height*0.6), true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                pw.setElevation(15);
            }

            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);


            callz = (Button) layout.findViewById(R.id.call_message);
            subject=(EditText)layout.findViewById(R.id.subject) ;
            popUpDismiss=(Button)layout.findViewById(R.id.popUpDismissButton);
            callz.setOnClickListener(callmessage);
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
                Toast.makeText(getActivity().getApplicationContext(), "Enter some digits", Toast.LENGTH_SHORT).show();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(getActivity(), "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISSIONS_REQUEST_PHONE_CALL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                call();
            } else {
                Toast.makeText(getActivity(), "Sorry!!! Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void sendSMS() {
        String phoneNumber = phoneno.getText().toString();
        String msg = subject.getText().toString();
//        Date c = Calendar.getInstance().getTime();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

// Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        String reportDate = df.format(today);

        msg = "CallXT: " + reportDate+" " + msg;//report date is the sms sending date.
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, msg, null, null);
    }


}
