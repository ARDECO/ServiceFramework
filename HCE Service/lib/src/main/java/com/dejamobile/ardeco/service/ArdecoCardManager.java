package com.dejamobile.ardeco.service;

import android.os.RemoteException;
import android.util.Log;

import com.dejamobile.ConvertUtils;
import com.dejamobile.ardeco.card.APDU;
import com.dejamobile.ardeco.card.AbstractFile;
import com.dejamobile.ardeco.card.DedicatedFile;
import com.dejamobile.ardeco.card.ISOException;
import com.dejamobile.ardeco.card.MasterFile;
import com.dejamobile.ardeco.lib.ArdecoCallBack;
import com.dejamobile.ardeco.lib.Failure;

/**
 * Created by Sylvain on 18/06/2015.
 */
public class ArdecoCardManager {

    private final static String CREATE_DF_APDU_START = "00E000000E7800";
    private final static String CREATE_DF_APDU_END = "08004F44FF01031F11FF";

    private final static String CREATE_ID_FILE = "00e000010E000A000201000000000100000000";
    private final static String UPDATE_ID_FILE = "00d600000a";

    private static final String TAG = ArdecoCardManager.class.getCanonicalName();
    public static final int NB_DIGITS = 20;
    public static final String PADDING_DIGIT = "F";

    private static ArdecoCardManager instance;

    private ArdecoCardManager() {
    }

    public static ArdecoCardManager getInstance() {
        if (instance == null){
            instance = new ArdecoCardManager();
        }
        return instance;
    }

    public void createCommunity(String id, String signature, ArdecoCallBack callBack){

        String createDFApdu = CREATE_DF_APDU_START + id + CREATE_DF_APDU_END;

        Log.d(TAG, "Community Creation with id : " + id);

        createDF(callBack, createDFApdu);
    }

    private void createDF(ArdecoCallBack callBack, String createDFApdu) {
        try {
            AbstractFile df = MasterFile.getInstance().createFile(new APDU(ConvertUtils.hex2byte(createDFApdu)));
            checkDFStatus(df, callBack);
        }catch (ISOException e){
            try {
                callBack.onFailure(Failure.ILLEGAL_STATE);
            } catch (RemoteException re) {
                Log.w(TAG, re);
            }
        }
    }

    public void createService(short communityId, String servcieId, String signature, ArdecoCallBack callBack){

        String createDFApdu = CREATE_DF_APDU_START + servcieId + CREATE_DF_APDU_END;
        // Community Exists ?
        DedicatedFile df = (DedicatedFile) MasterFile.getInstance().getSibling(communityId);
        if (df != null) {
            createDF(callBack, createDFApdu);
        }else{
            try {
                callBack.onFailure(Failure.FILE_NOT_FOUND);
            } catch (RemoteException e) {
                Log.w(TAG, e);
            }
        }
    }

    public void createAndUpdateIdFile(DedicatedFile df, String data){

        Log.d(TAG, "Main Id : " + data);
        String paddedData = String.format("%1$-" + NB_DIGITS + "s", data);
        paddedData = paddedData.replace(" ", PADDING_DIGIT);
        Log.d(TAG, "Padded data : " + paddedData);

        if (null == df.getSibling(AbstractFile.EF_ICC_SN)) {
            df.createFile(new APDU(ConvertUtils.hex2byte(CREATE_ID_FILE)));
        }
        df.getSibling(AbstractFile.EF_ICC_SN).updateBinary(new APDU(ConvertUtils.hex2byte(UPDATE_ID_FILE + paddedData)));
    }

    private void checkDFStatus( AbstractFile df, ArdecoCallBack callBack) {
        if (df == null){
            try {
                callBack.onFailure(Failure.NOT_ALLOWED_OPERATION);
            } catch (RemoteException e) {
                Log.w(TAG, e);
            }
        }else{
            try {
                callBack.onSuccess();
            } catch (RemoteException e) {
                Log.w(TAG, e);
            }
        }
    }

}
