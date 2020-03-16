package tw.nchumis.readnfctag;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tw.nchumis.readnfctag.util.MyNfcTag;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private  IntentFilter[] mReadTagFilters;

    private TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
        IntentFilter discovery = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mReadTagFilters = new IntentFilter[] { discovery };

        initViews();
        isNFCReady();
    }

    private void initViews() {
        show = findViewById(R.id.show);
    }

    private boolean isNFCReady() {
        if (nfcAdapter == null) {
            Toast.makeText(this, getResources().getString(R.string.no_nfc), Toast.LENGTH_LONG);
            return false;
        }
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            Toast.makeText(this, getResources().getString(R.string.open_nfc), Toast.LENGTH_LONG);
            return false;
        }

        return true;
    }

    private void readNfcTag(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if (tag != null) {

                MyNfcTag myTag = new MyNfcTag(tag);
                show.setText(myTag.getTagUUID());
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (isNFCReady())
            nfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mReadTagFilters, null);

    }

    @Override
    public void onPause() {
        super.onPause();

        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        readNfcTag(intent);
    }
}
