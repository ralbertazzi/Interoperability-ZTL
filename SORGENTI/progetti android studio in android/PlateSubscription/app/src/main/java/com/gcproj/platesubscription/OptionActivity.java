package com.gcproj.platesubscription;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OptionActivity extends AppCompatActivity {

    private EditText ip;
    private EditText port;
    private EditText smartSpace;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        SharedPreferences sp = getSharedPreferences("SIB_address", Context.MODE_PRIVATE);
        String savedIp = sp.getString("SIB_ip", "localhost");
        int savedPort = sp.getInt("SIB_port", 7702);
        String savedSmartSpace = sp.getString("SIB_smartspace", "default");

        ip = (EditText) findViewById(R.id.txtIp);
        port = (EditText) findViewById(R.id.txtPort);
        smartSpace = (EditText) findViewById(R.id.txtSmartSpace);
        btnSave = (Button) findViewById(R.id.btnSaveConfig);

        ip.setText(savedIp);
        port.setText(String.valueOf(savedPort));
        smartSpace.setText(savedSmartSpace);

        btnSave.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try{
                    //---get UI data
                    String ipAd = ip.getText().toString();
                    int portAd = Integer.parseInt(port.getText().toString());
                    String smSp = smartSpace.getText().toString();

                    System.out.println("IP = " + ipAd);
                    System.out.println("PORT = " + portAd);
                    System.out.println("SMARTSPACE = " + smSp);

                    SharedPreferences sp = getSharedPreferences("SIB_address", Context.MODE_PRIVATE);
                    SharedPreferences.Editor e = sp.edit();
                    e.putString("SIB_ip", ipAd);
                    e.putInt("SIB_port", portAd);
                    e.putString("SIB_smartspace", smSp);
                    e.commit();

                    Toast.makeText(getApplicationContext(), "SUCCESS - parameters updated",Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    System.out.println("errore NEL CATCH");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR - parameters not updated",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
