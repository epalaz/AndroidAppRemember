package com.example.enespc.remembertry3;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.logging.Handler;


public class Send extends Activity implements NfcAdapter.CreateNdefMessageCallback {

    String name, email, username, userId, phone;
    TextView view;
    SenseCard card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        view = (TextView) findViewById(R.id.hello);

        FileInputStream ins = null;
        try {
            ins = openFileInput("SenseCard");
            ObjectInputStream os = new ObjectInputStream(ins);
            Object a = os.readObject();
            card = (SenseCard)a;
            os.close();
            ins.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        name = card.getName();
        email = card.getEmail();
        phone = card.getPhoneNumber();


        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            view.setText("Sorry this device does not have NFC.");
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        NdefRecord[] records = new NdefRecord[3];

        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", name.getBytes());
        NdefRecord ndefRecord1 = NdefRecord.createMime("text/plain", email.getBytes());
        NdefRecord ndefRecord3 = NdefRecord.createMime("text/plain", phone.getBytes());

        records[0]= ndefRecord;
        records[1]= ndefRecord1;
        records[2]= ndefRecord3;


        NdefMessage ndefMessage = new NdefMessage(records);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ndefMessage;
    }
}
