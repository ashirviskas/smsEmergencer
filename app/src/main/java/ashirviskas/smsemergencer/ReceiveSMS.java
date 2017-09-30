package ashirviskas.smsemergencer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnGeofencingTransitionListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geofencing.model.GeofenceModel;
import io.nlopez.smartlocation.geofencing.utils.TransitionGeofence;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

/**
 * Created by Matas on 2017-08-21.
 */

import android.widget.Toast;

import com.karlotoy.perfectune.instance.PerfectTune;

public class ReceiveSMS extends BroadcastReceiver {
    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
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
        int beep_freq = Integer.parseInt(prefs.getString("do_beep_frequency", "880").toLowerCase());
        int beep_length = Integer.parseInt(prefs.getString("do_beep_length", "xxx").toLowerCase());
        PerfectTune perfectTune = new PerfectTune();
        perfectTune.setTuneFreq(beep_freq);
        perfectTune.playTune();
        SystemClock.sleep(beep_length * 1000);
        perfectTune.stopTune();
    }
    /*public void sendLocationBack(Context context, String NumberToSend){
        SmartLocation.with(context).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() { ... });

    }*/


}
