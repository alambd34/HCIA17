package com.bk.hica17.model;

import com.bk.hica17.utils.Util;

import java.util.ArrayList;

/**
 * Created by Dell on 18-Nov-16.
 */
public class DataVoice {

    private static ArrayList<byte[]> dataVoice ;

    public synchronized static ArrayList<byte[]> getDataVoice() {
        if (dataVoice == null) {
            initDataVoice();
        }
        return dataVoice;
    }

    public static void initDataVoice() {
        dataVoice = Util.readVoice();
    }

}
