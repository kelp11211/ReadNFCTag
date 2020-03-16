package tw.nchumis.readnfctag.util;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.nfc.tech.TagTechnology;

public class MyNfcTag {
    private Tag tag;
    private String[] techList;
    private byte[] tagId;
    private String tagUUID = "";

    public MyNfcTag(Tag tag) {
        this.tag = tag;

        if (tag != null) {
            tagId = tag.getId();
            techList = tag.getTechList();

            formatUUID();
        } else {
            tagId = null;
            techList = null;
        }
    }

    /**
     * 將byte[] 轉成 String
     */
    private void formatUUID() {
        for (int i = 0; i < tagId.length; i++) {
            tagUUID += Integer.toHexString(tagId[i] & 0xFF) + " ";
        }

        tagUUID = tagUUID.trim();
    }

    public TagTechnology getTagByFirstTech() {
        TagTechnology object = null;

        switch(techList[0]) {
            case "android.nfc.tech.Ndef" :
                object = Ndef.get(tag);
                break;
            case "android.nfc.tech.NfcA" :
                object = NfcA.get(tag);
                break;
            case "android.nfc.tech.MifareClassic" :
                object = MifareClassic.get(tag);
                break;
            case "android.nfc.tech.MifareUltralight" :
                object = MifareUltralight.get(tag);
                break;
            case "android.nfc.tech.NdefFormatable" :
                object = NdefFormatable.get(tag);
                break;
            default:
                break;
        }

        return object;
    }

    /**
     * 回傳tag的UUID
     * @return UUID of the RFID tag
     */
    public String getTagUUID() {
        return this.tagUUID;
    }

    @Override
    public String toString() {
        String str = "";

        str += tag.toString() + "\n";
        str += "\nTag UUID: \n";

        str += "長度 = " + tagId.length + "\n";
        for (int i = 0; i < tagId.length; i++) {
            str += Integer.toHexString(tagId[i] & 0xFF) + " ";
        }
        str += "\n";
        str += "\n技術規範資料\n";
        str += "長度 = " + techList.length + "\n";
        for (int i = 0; i < techList.length; i++) {
            str += techList[i] + "\n ";
        }

        return str;
    }
}
