package com.example.amanthakur.arduinosms;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver;
    Button engineOn,engineOff,hornOn,hornOff,sosOn;
    String smstext;
    String phoneno;
    TextView secruinoNo;
    ImageView imageView1,imageView2;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    int uri;
    int flag = 0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        phoneno = sharedPreferences.getString("phoneno",null);
        secruinoNo = (TextView) findViewById(R.id.textviewsecruinono);
        secruinoNo.setText("+91"+phoneno);
        engineOn = (Button) findViewById(R.id.engineon);
        engineOff = (Button) findViewById(R.id.engineoff);
        hornOn = (Button) findViewById(R.id.hornon);
        hornOff = (Button) findViewById(R.id.hornoff);
        sosOn = (Button) findViewById(R.id.soson);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Initializing...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        imageView1 = (ImageView) findViewById(R.id.engineindicator);
        imageView2 = (ImageView) findViewById(R.id.hornindicator);

        engineOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Sending ENGINE ON request....");
                progressDialog.show();
                smstext = "#A.engine on*";
                sendSMSMessage();
                uri = R.drawable.circlegreen;
                flag = 1;
            }
        });
        engineOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Sending ENGINE OFF request....");
                progressDialog.show();
                smstext = "#A.engine off*";
                sendSMSMessage();
                uri = R.drawable.circlered;
                flag = 2;
            }
        });
        hornOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Sending HORN ON request....");
                progressDialog.show();
                smstext = "#A.horn on*";
                sendSMSMessage();
                uri = R.drawable.circlegreen;
                flag = 3;
            }
        });
        hornOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Sending HORN OFF request....");
                progressDialog.show();
                smstext = "#A.horn off*";
                sendSMSMessage();
                uri = R.drawable.circlered;
                flag = 4;
            }
        });
        sosOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Sending SOS ON request....");
                progressDialog.show();
                smstext = "#A.emergency*";
                sendSMSMessage();
                flag = 5;


            }
        });





    }




    protected void sendSMSMessage() {



        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            progressDialog.dismiss();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

        else {

            PendingIntent piSent=PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
            PendingIntent piDelivered=PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneno, null, smstext, piSent, piDelivered);

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        progressDialog.setMessage("Completing the request...");
        progressDialog.show();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PendingIntent piSent=PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
                    PendingIntent piDelivered=PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneno, null, smstext, piSent, piDelivered);
                    progressDialog.setMessage("Waiting for conformation...");

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            "REQUEST faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }




    public void onResume() {
        super.onResume();
        smsSentReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                // TODO Auto-generated method stub
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        progressDialog.setMessage("REQUEST has been sent...waiting for delivery");
                        progressDialog.show();
                        Toast.makeText(getBaseContext(), "REQUEST has been sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        progressDialog.setMessage("Generic Failure");
                        Toast.makeText(getBaseContext(), "Generic Failure", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        progressDialog.setMessage("No Service");
                        Toast.makeText(getBaseContext(), "No Service", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        progressDialog.setMessage("Null PDU");
                        Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        progressDialog.setMessage("Radio Off");
                        Toast.makeText(getBaseContext(), "Radio Off", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        break;
                    default:
                        break;
                }


            }
        };
        smsDeliveredReceiver=new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                // TODO Auto-generated method stub
                progressDialog.setMessage("Waiting for delivery conformation...");
                progressDialog.show();
                switch(getResultCode()) {
                    case Activity.RESULT_OK:

                        Toast.makeText(getBaseContext(), "REQUEST Delivered", Toast.LENGTH_SHORT).show();
                        if(flag == 1 || flag == 2){
                            imageView1.setImageResource(uri);
                        }

                        else if(flag == 3 || flag == 4){
                            imageView2.setImageResource(uri);
                        }
                        else if(flag== 5){
                            imageView1.setImageResource(R.drawable.circlered);
                            imageView2.setImageResource(R.drawable.circlegreen);
                        }
                        progressDialog.setMessage("Request has been delivered successfully !");
                        progressDialog.show();


                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "REQUEST not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }
                progressDialog.dismiss();
            }
        };
        registerReceiver(smsSentReceiver, new IntentFilter("SMS_SENT"));
        registerReceiver(smsDeliveredReceiver, new IntentFilter("SMS_DELIVERED"));
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.logout:
                getApplicationContext().getSharedPreferences("MyPrefs", 0).edit().clear().commit();
                Intent intent = new Intent(MainActivity.this,SplashScreen.class);
                finish();
                startActivity(intent);
                Toast.makeText(MainActivity.this,"Logged Out",Toast.LENGTH_LONG).show();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
