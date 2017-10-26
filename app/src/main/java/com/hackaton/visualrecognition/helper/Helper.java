package com.hackaton.visualrecognition.helper;

import android.os.Environment;

import com.hackaton.visualrecognition.app.App;

import java.io.File;

/**
 * Created by fatih.erol on 26.10.2017.
 */

public class Helper {

    public static void createDirectoryForPictures ()
    {
        App._dir = new File(
                Environment.getExternalStoragePublicDirectory (
                        Environment.DIRECTORY_PICTURES), "CameraAppDemo");
        if (!App._dir.exists ())
        {
            App._dir.mkdirs( );
        }
    }

}
