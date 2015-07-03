package com.nfcbeamtest.cb.nfcpushmessage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcB;
import android.os.Parcelable;
import android.nfc.tech.TagTechnology;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.nio.charset.Charset;


public class MainActivity extends ActionBarActivity {

    final Context context = this;
    private Intent intent;
    private String action ;
    private NfcAdapter nfcAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        nfcAdapter = manager.getDefaultAdapter();
        if(!nfcAdapter.isEnabled()) {
            alertbox("Uyarı","Lütfen telefonunuzun NFC özelliğini aktifleştirin.");
        }

        intent = getIntent();
        handleIntent();
    }

    private void handleIntent() {
        action = intent.getAction();
        if(nfcAdapter.ACTION_TECH_DISCOVERED.equals(action)){
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] id = tagFromIntent.getId();
            Ndef mifareTag = Ndef.get(tagFromIntent);


            NdefRecord record = NdefRecord.createTextRecord("en","deneme");
            NdefMessage message = new NdefMessage(record);


            alertbox("Bilgi", "Writable!");


                        // mifareTag.authenticateSectorWithKeyA(0,id);


        }else if(nfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            alertbox("Bilgi","NDEF Discovered detected!");
        }else if(nfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            alertbox("Bilgi","Tag Discovered detected!");
        }

        NdefMessage attackNdefMessage = null;
        NdefRecord[] ndefRecords = new NdefRecord[10];
        //ndefRecords[0] = NdefRecord.createExternal();
        //attackNdefMessage = new NdefMessage(ndefRecords[0]);
        //nfcAdapter.setNdefPushMessage(attackNdefMessage,this);
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, nfcAdapter);
        super.onPause();
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /*@Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundNdefPush(this);
    }*/

    protected void alertbox(String title, String mymessage)
    {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton){}
                        })
                .show();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }


}
