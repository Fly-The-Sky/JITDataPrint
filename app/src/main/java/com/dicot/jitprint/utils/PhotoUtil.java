package com.dicot.jitprint.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Hunter
 * Describe  图片的工具方法类
 * on 2017/3/11.
 */
public class PhotoUtil {


    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }


    /**
     * 图片压缩
     */
    public static File ImageCompression(String path) throws Exception {
        try {
            File outputFile = new File(path);
            long fileSize = outputFile.length();
            final long fileMaxSize = 120 * 1024;
            if (fileSize >= fileMaxSize) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                int height = options.outHeight;
                int width = options.outWidth;

                double scale = Math.sqrt((float) fileSize / fileMaxSize);
                options.outHeight = (int) (height / scale);
                options.outWidth = (int) (width / scale);
                options.inSampleSize = (int) (scale + 0.5);
                options.inJustDecodeBounds = false;

                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                outputFile = new File(PhotoUtil.createImageFile().getPath());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(outputFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fos);
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("", "压缩成功，图片大小：k " + outputFile.length());
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                } else {
                    File tempFile = outputFile;
                    outputFile = new File(PhotoUtil.createImageFile().getPath());
                    PhotoUtil.copyFileUsingFileChannels(tempFile, outputFile);
                }
            }
            return outputFile;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 创建新图片
     */
    public static Uri createImageFile() throws Exception {
        try {
            String imageFileName = DateUtil.getFormatFileTime();
            File storageDir = new File(AppConst.pathImages);  //新文件地址
            if (!storageDir.exists())
                storageDir.mkdirs();
            File image = null;
            try { /* prefix ,suffix,directory*/
                image = File.createTempFile(imageFileName, ".jpg", storageDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Uri.fromFile(image);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 复制图片
     *
     * @param source 源图片
     * @param dest
     * @throws Exception
     */
    public static void copyFileUsingFileChannels(File source, File dest) throws Exception {
        try {
            FileChannel inputChannel = null;
            FileChannel outputChannel = null;
            try {
                try {
                    inputChannel = new FileInputStream(source).getChannel();
                    outputChannel = new FileOutputStream(dest).getChannel();
                    outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } finally {
                try {
                    inputChannel.close();
                    outputChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 把图片保存到本地文件夹
     *
     * @param path
     * @param bitmap
     */
    public static void saveImageFile(String path, Bitmap bitmap) {
        try {
            File outputFile = new File(path);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                fos.flush();
                fos.close();
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把图片转换成字符串类型的方法
     *
     * @param pathUrl
     * @return
     */
    public static String toBype(String pathUrl) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(pathUrl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[20480];
        int count = 0;
        try {
            while ((count = fis.read(buffer)) >= 0) {
                baos.write(buffer, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] bytes = baos.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    /**
     * 缩放bitmap
     * @param oldBitmap 输入bitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap oldBitmap, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = oldBitmap.getWidth();
        int height = oldBitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(oldBitmap, 0, 0, width, height, matrix,
                true);
        return newbm;
    }
}
