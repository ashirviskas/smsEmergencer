package ashirviskas.smsemergencer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS =1 ;
    EditText phoneNoETxt;
    EditText SmsETxt;
    Button sendBtn;
    Button grantPermissionsBtn;
    String phoneNo = "";
    String message = "";
    ReceiveSMS receiveSMS = new ReceiveSMS();
    private PermissionRequestErrorListener errorListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phoneNoETxt = (EditText)findViewById(R.id.etxt_sms_number);
        SmsETxt = (EditText)findViewById(R.id.etxt_sms_text);
        sendBtn = (Button) findViewById(R.id.btnSendSMS);
        grantPermissionsBtn = (Button)findViewById(R.id.btnGrantSMSpermissions);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                phoneNo = phoneNoETxt.getText().toString();
                message = SmsETxt.getText().toString();
                sendSMS();
            }
        });
        grantPermissionsBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                grantSMSReadSend();
            }

        });
    }
    protected void grantSMSReadSend()
    {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS)
                .withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                        //SmsManager smsManager = SmsManager.getDefault();
                        //smsManager.sendTextMessage(phoneNo, null, message, null, null);
                        /*Toast.makeText(getApplicationContext(),
                                "SMS send permission is granted", Toast.LENGTH_LONG).show();*/
                    }
                    /*@Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getApplicationContext(),
                                "No permission to send SMS", Toast.LENGTH_LONG).show();
                    }*/
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        for (PermissionRequest onePermission : permissions){
                            Toast.makeText(getApplicationContext(), onePermission.getName(), Toast.LENGTH_LONG).show();
                        }
                        /*Toast.makeText(getApplicationContext(),
                            "You need to turn on the SEND SMS permission manually", Toast.LENGTH_LONG).show();}*/
                    }
                }).check();


    }
    protected void sendSMS() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.SEND_SMS)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNo, null, message, null, null);
                        Toast.makeText(getApplicationContext(),
                                "SMS send permission is granted", Toast.LENGTH_LONG).show();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getApplicationContext(),
                                "No permission to send SMS", Toast.LENGTH_LONG).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {Toast.makeText(getApplicationContext(),
                            "You need to turn on the SEND SMS permission manually", Toast.LENGTH_LONG).show();}
                }).withErrorListener(errorListener).check();

    }

    public void btnSettings_onClick(View view) {
        Intent intent=new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }

    /*protected void sendSMSMessageP() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
                sendSMSMessage();
            }
        }
        else
        {
            sendSMSMessage();
        }
    }*/

    /*protected void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS();
        }
    }*/

    /*protected void sendSMSMessage(){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }*/
    /*public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_SMS);
        }
    }*/

    /*@Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            case MY_PERMISSIONS_REQUEST_READ_SMS:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS read permission enabled",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "No permission :/", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }*/

    }

