package com.leilei.guoshujinfu.mylearning.util.storage;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @Author: AqrLei
 * @Name MyLearning
 * @Description:
 * @Date: 2017/8/10
 */

public class StorageUtil {
    private static final SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
    private static final String STORAGEPATH = Environment.getExternalStorageState()+"/MyLearning";
    private static final String FILE_PREFIX = "mylearing_";




    public static File createImgFile() {

        String timeStamp = DATAFORMAT.format(new Date());
        String imageFileName =FILE_PREFIX + "JPEG_" + timeStamp + "_";
        File dir = new File(STORAGEPATH);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File image= new File(STORAGEPATH + "/" + imageFileName + ".jpg");
        return image;
    }
}
