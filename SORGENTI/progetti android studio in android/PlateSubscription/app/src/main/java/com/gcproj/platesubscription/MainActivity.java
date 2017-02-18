package com.gcproj.platesubscription;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Vector;

import ModelClasses.*;

import sofia_kp.KPICore;
import sofia_kp.SIBResponse;
import sofia_kp.SSAP_sparql_response;

public class MainActivity extends AppCompatActivity {

    // SIB instance
    private KPICore kpiCore = null;
    private SIBResponse resp = null;

    private EditText plate;
    private EditText ticket;
    private Spinner categories;
    private Button btn;

    private TicketValidator tv = new TicketValidator();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.item_First) {
            Intent intent = new Intent(this, OptionActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plate = (EditText) findViewById(R.id.txtPlate);
        ticket = (EditText) findViewById(R.id.txtTicket);
        categories = (Spinner) findViewById(R.id.spnCategory);
        ArrayAdapter<String> sa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, TicketValidator.getTypeValues());
        categories.setAdapter(sa);
        btn = (Button) findViewById(R.id.btnRegister);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String p = plate.getText().toString();
                    String c = categories.getSelectedItem().toString();
                    long t = Long.parseLong(ticket.getText().toString());

                    System.out.println("PLATE = " + p);
                    System.out.println("CATEGORY = " + c);
                    System.out.println("TICKET = " + t);

                    tv.setPlate(p);
                    tv.setCategory(c);
                    tv.setTicket(t);

                    // test UI data
                    if(!tv.isValid()){
                        System.out.println("errore su ticketValidator.isValid");
                        Toast.makeText(getApplicationContext(), "ERROR - invalid ticket",Toast.LENGTH_LONG).show();
                        plate.setText("");
                        ticket.setText("");
                    }else{
                        // insert triples by invoking an AsyncTask
                        SIBAsyncTask sat = new SIBAsyncTask();
                        sat.execute(tv);
                    }


                }catch (Exception e){
                    System.out.println("errore NEL CATCH");
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR - invalid parameters",Toast.LENGTH_LONG).show();
                    plate.setText("");
                    ticket.setText("");
                }

            }
        });

    }

    public class SIBAsyncTask extends AsyncTask<TicketValidator, Void, String>{ //input al worker thread, risultati intermedi, risultato che il thread restituisce

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){ //esegue prima di doInBackground sul thread della UI
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Sending pass...");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(TicketValidator... params) {

            SharedPreferences sp = MainActivity.this.getSharedPreferences("SIB_address", Context.MODE_PRIVATE);
            String ipAd = sp.getString("SIB_ip", "localhost");
            int portAd = sp.getInt("SIB_port", 7702);
            String smartSpace = sp.getString("SIB_smartspace", "default");

            // connection
            kpiCore = new KPICore(ipAd, portAd, smartSpace);

            // join
            resp = kpiCore.join();
            if (!resp.isConfirmed()) {
                System.err.println("Error joining the SIB");
                return "SIB join error";
            }

            //TODO sparql query to retreive vehicle from the given plate
            String query = "PREFIX  ns:<http://ztl#> SELECT ?s WHERE { ?s ns:hasPlate \""+ tv.getPlate() +"\" }";
            resp = kpiCore.querySPARQL(query);
            String vUri = null;
            if (!resp.isConfirmed())
                return "SPARQL query error!";

            Vector<String[]> row = null;
            SSAP_sparql_response sqr = resp.sparqlquery_results;

            while (sqr.hasNext()) {
                sqr.next();
                row = sqr.getRow();
                for (String[] r : row) {
                    vUri=r[2];
                }
            }

            if(vUri==null)
                return "SPARQL vehicle not found";

            // insert triples
            Pass currentPass = new Pass(tv.getDeadline(),tv.getTicket(),tv.getCategory()); //long deadline, long ticket, String type
            resp = kpiCore.insert(currentPass.toTriple());
            if(!resp.isConfirmed()){
                return "SIB insert error";
            }

            resp = kpiCore.insert(vUri,OntologyProvider.toTripleFormat("hasPass"), OntologyProvider.toTripleFormat("Pass_"+currentPass.getTicket()),"uri", "uri");
            if(!resp.isConfirmed()){
                return "SIB insert error";
            }

            // leave
            resp = kpiCore.leave();
            if (!resp.isConfirmed()) {
                return "SIB leave error";
            }

            return currentPass.toString();

        }

        @Override
        protected void onPostExecute(String result){ //esegue sul thread della grafica il risultato della doInBackground (es: modifca GUI)
            progressDialog.dismiss();

            plate.setText("");
            ticket.setText("");

            Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();

        }
    }

}
