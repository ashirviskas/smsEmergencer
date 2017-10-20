package ashirviskas.smsemergencer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;



/**
 * Created by Matas on 2017-08-21.
 */

import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karlotoy.perfectune.instance.PerfectTune;
import com.google.android.gms.common.api.GoogleApiClient;

public class ReceiveSMS extends BroadcastReceiver {
    private SharedPreferences preferences;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onReceive(Context context, Intent intent) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            /* Get Messages */
            Object[] sms = (Object[]) intentExtras.get("pdus");

            for (int i = 0; i < sms.length; ++i) {
                /* Parse Each Message */
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String trigger_location = prefs.getString("send_location_trigger", "xxx").toLowerCase();
                String trigger_beep = prefs.getString("do_beep_trigger", "xxx").toLowerCase();
                boolean should_beep = prefs.getBoolean("do_beep_switch", false);

                String phone = smsMessage.getOriginatingAddress();
                String message = smsMessage.getMessageBody();
                Toast.makeText(context.getApplicationContext(), phone + ": " + message, Toast.LENGTH_SHORT).show();
                if (message.toLowerCase().contains(trigger_beep) && should_beep) {
                    do_beep(context, prefs);
                }
            }
        }
    }

    public void do_beep(Context context, SharedPreferences prefs) {


        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
        int beep_freq = Integer.parseInt(prefs.getString("do_beep_frequency", "880").toLowerCase());
        int beep_length = Integer.parseInt(prefs.getString("do_beep_length", "xxx").toLowerCase());
        PerfectTune perfectTune = new PerfectTune();
        perfectTune.setTuneFreq(beep_freq);
        perfectTune.playTune();
        SystemClock.sleep(beep_length * 1000);
        perfectTune.stopTune();
    }
    public void sendLocationBack(Context context, String NumberToSend){
//        Location lastloc = getLocation(context);
//        sendSMS("+37066254126",lastloc.getLongitude()+"  " + lastloc.getLatitude()+"");

    }
    /*public Location getLocation(Context context){
        Location loc = null;

        try {
            int x = 0;
            loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);;//.getResult();
        }
        catch (SecurityException e){
        }
        return loc;
    }*/
    public void sendSMS(String phoneNo, String message)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
    }


}
