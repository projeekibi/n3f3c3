package com.test.cb.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.view.View.OnClickListener;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class MainActivity extends Activity {

    final Context context = this;
    private SimpleCursorAdapter dataAdapter;
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    private Intent intent;
    private ListView kartListesi;
    public DBHelper dbHelper;
    private CustomList adapter;
    private List<Card> cards;
    private PopupWindow pwindo;


    public void createDatabase() {
        dbHelper = new DBHelper(getApplicationContext());
        SharedPreferences settings = getSharedPreferences("SQL", 0);
        boolean firstTime = settings.getBoolean("firstTime", true);
        Bitmap eruLogo = BitmapFactory.decodeResource(getResources(),
                R.drawable.eru_logo);

        Bitmap paso38 = BitmapFactory.decodeResource(getResources(),
                R.drawable.aktif38);

        if (firstTime) {
            dbHelper.insertCard(new Card("Erciyes Üniversitesi Öğrenci Kartı", "15A547A", convertBitmapToByte(eruLogo)));
            dbHelper.insertCard(new Card("Kayseri Toplu Taşıma Kartı (PASO)", "224AR77", convertBitmapToByte(paso38)));
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        createDatabase();
        PackageManager pm = context.getPackageManager();
        if(pm.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)) {
            Log.d("baslik","destekliyor.");
        }else {
            Log.d("baslik","desteklemez.");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Üstteki sekmeleri oluşturur.
        tabMenu();
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter != null) {
            // adapter exists and is enabled.
            if (!nfcAdapter.isEnabled()) {
                alertbox("Uyarı", "Lütfen telefonunuzun NFC özelliğini aktifleştirin.");
                //Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            }
        } else {
            alertbox("Uyarı", "Telefonunuz NFC teknolojisini desteklememektedir.");
            return;
        }

        //Kullanıcıya gösterilen ListView'a ulaşabilmek için onun bir referansını almak
        kartListesi = (ListView) findViewById(R.id.listView1);
        adapter = new CustomList(this, dbHelper.fetchAllCards());
        kartListesi.setAdapter(adapter);

        registerForContextMenu(kartListesi);

        kartListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                final Dialog dialog = new Dialog(context, R.style.Dialog);
                Cursor cursor = adapter.getCursor();
                cursor.moveToPosition(position);

                dialog.setContentView(R.layout.alert_design);
                dialog.setTitle(cursor.getString(cursor.getColumnIndex("kart_adi")));

                //adapter.
                TextView msgAlert = (TextView) dialog.findViewById(R.id.alert_message);
                msgAlert.setText("Lütfen telefonu kart okuyucuya yaklaştırın.");

                ImageView imgAlert = (ImageView) dialog.findViewById(R.id.alert_image);
                imgAlert.setImageResource(R.drawable.yaklastir);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        ImageView imgKartEkle = (ImageView) findViewById(R.id.imgViewKartEkle);
        imgKartEkle.setImageResource(R.drawable.kartekle);
        imgKartEkle.setY(400);

        TextView txtKartEkle = (TextView) findViewById(R.id.txtKartEkle);
        txtKartEkle.setText("Lütfen kartınızı telefona okutunuz.");


        intent = getIntent();
        handleIntent();
    }

    private void handleIntent() {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {

            Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            dialog_newCard(dumpTagData(tag));
        }
        return;
    }

    private byte[] convertBitmapToByte(Bitmap image) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte imageInByte[] = stream.toByteArray();
        return imageInByte;
    }

    private void dialog_newCard(String tagData) {

        final Dialog dialog_newCard = new Dialog(context, R.style.Dialog);
        dialog_newCard.setContentView(R.layout.alert_newcard);
        dialog_newCard.setTitle("Kart Ekle");

        TextView txtKartBilgileri = (TextView) dialog_newCard.findViewById(R.id.txtKartBilgileri);
        txtKartBilgileri.setText(tagData);

        Button btnKaydet = (Button) dialog_newCard.findViewById(R.id.btnKaydet);
        Button btnIptal = (Button) dialog_newCard.findViewById(R.id.btnIptal);


        btnKaydet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click

                final EditText input = (EditText) dialog_newCard.findViewById(R.id.edtKartAdi);

                Bitmap defaultCard = BitmapFactory.decodeResource(getResources(),
                        R.drawable.default_card);

                dbHelper.insertCard(new Card(input.getText().toString(), "224AR77", convertBitmapToByte(defaultCard)));

                adapter = new CustomList(MainActivity.this, dbHelper.fetchAllCards());

                kartListesi.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                dialog_newCard.dismiss();
            }
        });

        btnIptal.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog_newCard.dismiss();
            }
        });

        dialog_newCard.show();
    }


    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        sb.append("Kart ID(hex): ").append(getHex(id)).append("\n");
        sb.append("Kart ID (dec): ").append(getDec(id)).append("\n");
        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";

                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');
                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');
                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');
                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            }
        }
        return sb.toString();
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

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    public void tabMenu() {
        //XML 'deki tabHosta bağlantı
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        //Birinci Sekme
        TabHost.TabSpec spec = tabHost.newTabSpec("etiket1");
        spec.setContent(R.id.tabKartlarim);
        spec.setIndicator("Kartlarım");
        tabHost.addTab(spec);

        // İkinci sekme
        spec = tabHost.newTabSpec("etiket2");
        spec.setContent(R.id.tabKartEkle);
        spec.setIndicator("Kart Ekle");
        tabHost.addTab(spec);
    }


    public void alertbox(String title, String mymessage) {
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                .show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ct_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();

        Cursor cursor = adapter.getCursor();
        cursor.moveToPosition(info.position);
        //int kartID = cursor.getInt(cursor.getColumnIndex("_id"));

        switch (id) {
            case R.id.itemSil:
                /*-------------------------  Kart Silme Modülü - Ceylan --------------------------- */
                dbHelper.deleteCard(info.id);
                adapter = new CustomList(MainActivity.this, dbHelper.fetchAllCards());
                kartListesi.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                /*------------------------------------- ~ ----------------------------------------- */
            case R.id.itemAdDegistir:
                /*-------------------------  Kart Adı Değiştirme Modülü - Elif -------------------- */
                initiatePopupWindow(info.id);
                /*------------------------------------- ~ ----------------------------------------- */
            case R.id.itemGecmis:
                return true;
            case R.id.itemOzellik:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onContextItemSelected(item);

        }

    }

    /*------------------------------------- PopUp Window - Elif ---------------------------------- */
    public void initiatePopupWindow(final long kartID) {
        Button btnClosePopup;

        try {
            LayoutInflater inflater = (LayoutInflater) MainActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View pop = inflater.inflate(R.layout.screen_popup, null, false);
            /* Ceylan  - Benim telefonumda 400 * 600 olunca kaydet butonu görülmüyordu o yüzden ikisini de
                         ViewGroup.LayoutParams.WRAP_CONTENT olarak değiştirdim. */
            pwindo = new PopupWindow(pop, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

            pwindo.showAtLocation(pop, Gravity.CENTER, 0, 0);

            btnClosePopup = (Button) pop.findViewById(R.id.btn_close_popup);

            btnClosePopup.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {

                    //EditText ‘i onClick fonksiyonunun içine taşıdım - Ceylan
                    EditText yeniKartAdi = (EditText) pop.findViewById(R.id.edtKartAdiDegistir);
                    String yeniAd = yeniKartAdi.getText().toString();

                    // Kart adı değiştiriliyor.
                    dbHelper.changeCardName(kartID, yeniAd);

                    /* Değişim yapıldıktan sonra liste yenilemesi için aşağıdaki üç satır gereklidir.- Ceylan*/
                    adapter = new CustomList(MainActivity.this, dbHelper.fetchAllCards());
                    kartListesi.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    // Değişim yapıldı, liste yenilendi, şimdi de popup kapatılıyor.
                    pwindo.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Hata", e.getMessage());
        }
    }

    /*------------------------------------- ~ ----------------------------------------- */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Handle item selection
        switch (id) {
            case R.id.itemSil:


            case R.id.itemAdDegistir:
                return true;
            case R.id.itemGecmis:
                return true;
            case R.id.itemOzellik:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }


    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, nfcAdapter);

        super.onPause();
    }


    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

};
























