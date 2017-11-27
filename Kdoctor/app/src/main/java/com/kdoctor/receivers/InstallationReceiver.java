package com.kdoctor.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kdoctor.configuration.Kdoctor;
import com.kdoctor.sql.DbManager;

import java.io.File;

/**
 * Created by INI\huy.trinh on 01/11/2017.
 */

public class InstallationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case "android.intent.action.PACKAGE_ADDED":
                //clearApplicationData();
                //context.deleteDatabase(DbManager.DATABASE_NAME+".db");
                break;
            case "android.intent.action.PACKAGE_REMOVED":
                clearApplicationData();
                context.deleteDatabase(DbManager.DATABASE_NAME);
                trimCache(context);
                Toast.makeText(context, "Gỡ cài đặt Kdoctor hoàn tất...",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void clearApplicationData() {
        File cacheDirectory = Kdoctor.getInstance().getCacheDir();
        File applicationDirectory = new File(cacheDirectory.getParent());
        if (applicationDirectory.exists()) {
            String[] fileNames = applicationDirectory.list();
            for (String fileName : fileNames) {
                if (!fileName.equals("lib")) {
                    deleteFile(new File(applicationDirectory, fileName));
                }
            }
        }
    }

    public static boolean deleteFile(File file) {
        boolean deletedAll = true;
        if (file != null) {
            if (file.isDirectory()) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    deletedAll = deleteFile(new File(file, children[i])) && deletedAll;
                }
            } else {
                deletedAll = file.delete();
            }
        }

        return deletedAll;
    }

    public static void trimCache(Context context) {
        File dir = context.getCacheDir();
        if(dir!= null && dir.isDirectory()){
            File[] children = dir.listFiles();
            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                File temp;
                for (int i = 0; i < children.length; i++) {
                    temp = children[i];
                    temp.delete();
                }
            }

        }

    }
}
