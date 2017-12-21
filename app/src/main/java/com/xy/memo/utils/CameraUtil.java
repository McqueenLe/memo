package com.xy.memo.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * 拍照后裁剪
 * @author xy 2017/12/5.
 */

public class CameraUtil {
    /**
     * 显示截取框
     *
     * @param file
     */
    public static void startPhotoZoom(Activity activity, Intent cutIntent, int mRequestCodeCutImage, File file, File mCutFile) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", cutIntent.getIntExtra("aspectX", 1));
        intent.putExtra("aspectY", cutIntent.getIntExtra("aspectY", 1));
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", cutIntent.getIntExtra("outputX", 100));
        intent.putExtra("outputY", cutIntent.getIntExtra("outputY", 100));
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCutFile));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, mRequestCodeCutImage);
    }

}
