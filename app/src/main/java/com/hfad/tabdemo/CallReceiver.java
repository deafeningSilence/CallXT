package com.hfad.tabdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Sarala on 05-09-2016.
 */
public class CallReceiver extends PhonecallReceiver {
    SMSToast toast = new SMSToast();
    ESMSToast etoast = new ESMSToast();
    QSMSToast qtoast = new QSMSToast();

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {//call receiving time that is date
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur =ctx.getApplicationContext().getContentResolver().query(uriSMSURI, null, null, null,null);
        String sms = "";
        int position = -1;
        int position2 = -1;
        int position3 = -1;
        cur.moveToFirst();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String sdate = df.format(start);
        int rtmin = Integer.parseInt(sdate.substring(14,16));
        String s = cur.getString(cur.getColumnIndex("body")).substring(27).toLowerCase();
        List<String> ewords = new ArrayList<String>();
        ewords.add("urgent");//index 0
        ewords.add("immediately");
        ewords.add("!!");
        ewords.add("help");
        ewords.add("sos");
        ewords.add("accident");
        ewords.add("emergency");
        ewords.add("stolen");
        ewords.add("lost");
        ewords.add("asap");

        List<String> qwords = new ArrayList<String>();
        qwords.add("are");//index 0
        qwords.add("shall");
        qwords.add("will");
        qwords.add("can");

        List<String> twords = new ArrayList<String>();
        twords.add("when");//index 0
        twords.add("time");
        twords.add("date");

        for(int i=0; i<twords.size(); i++){
            int counter4 = -1; int counter5 = -1;
            counter4 = s.indexOf(twords.get(i));
            counter5 = s.indexOf(twords.get(i));
            if((counter4>=0)&&(counter5>=0)){
                position3++;
                break;

            }

        }

        for(int i=0; i<ewords.size(); i++){
            int counter = -1;
            counter = s.indexOf(ewords.get(i));

            if(counter>=0){
                position++;
                break;

            }

        }

        for(int i=0; i<qwords.size(); i++){
            int counter2 = -1; int counter3 = -1;
            counter2 = s.indexOf(qwords.get(i));
            counter3 = s.indexOf("?");
            if((counter2>=0) && (counter3>=0)){
                position2++;
                break;

            }

        }



        int msmin = Integer.parseInt(cur.getString(cur.getColumnIndex("body")).substring(22,24));
        if((cur.getString(cur.getColumnIndex("body")).substring(0,7).equals("CallXT:")) && (cur.getString(2).equals(number))&&(sdate.substring(0,14).equals(cur.getString(cur.getColumnIndex("body")).substring(8,22)))) {
            if ((msmin == rtmin) || (msmin == (rtmin - 1))) {
                if(position3>=0){
                    sms += cur.getString(cur.getColumnIndex("body")).substring(27) +"\n" + sdate.substring(0,19);
                    toast.showToast(ctx, sms);
                    toast.showToast(ctx, sms);
                    toast.showToast(ctx, sms);
                    toast.showToast(ctx, sms);
                    toast.showToast(ctx, sms);
                }
                else if (position2>=0){
                    sms += cur.getString(cur.getColumnIndex("body")).substring(27);
                    qtoast.showToast(ctx, sms);
                    qtoast.showToast(ctx, sms);
                    qtoast.showToast(ctx, sms);
                    qtoast.showToast(ctx, sms);
                    qtoast.showToast(ctx, sms);
                }
                else if(position>=0) {
                    sms += cur.getString(cur.getColumnIndex("body")).substring(27);
                    etoast.showToast(ctx, sms);
                    etoast.showToast(ctx, sms);
                    etoast.showToast(ctx, sms);
                    etoast.showToast(ctx, sms);
                    etoast.showToast(ctx, sms);
                }
                else {
                    sms += cur.getString(cur.getColumnIndex("body")).substring(27);
                    toast.showToast(ctx, sms);
                    toast.showToast(ctx, sms);
                    toast.showToast(ctx, sms);
                    toast.showToast(ctx, sms);
                    toast.showToast(ctx, sms);

                }
            }
        }
        /*else{
            cur.moveToNext();
            if((cur.getString(cur.getColumnIndex("body")).substring(0,7).equals("CallXT:")) && (cur.getString(2).equals(number))) {
                sms +=  "SUBJECT : " + cur.getString(cur.getColumnIndex("body")).substring(7);
                toast.showToast(ctx, sms);
                toast.showToast(ctx, sms);
                toast.showToast(ctx, sms);
                toast.showToast(ctx, sms);
                toast.showToast(ctx, sms);

            }
        }*/

    }


    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
    }

}