package com.viraltubesolutions.viraltubeapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;

/**
 * Created by Shashi on 12/13/2017.
 */

public class VideoMerging {
    Context context;
    private static final int REQUEST_TAKE_GALLERY_VIDEO1 = 100,REQUEST_TAKE_GALLERY_VIDEO2 =200;
    private FFmpeg ffmpeg;
    private ProgressDialog progressDialog;
    private static final String TAG = "VideoMerging";
    private int choice = 0;
    private String filePath;



    public VideoMerging(Context context) {
        this.context = context;

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(null);
        progressDialog.setCancelable(false);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if(ffmpeg.isFFmpegCommandRunning())
                    ffmpeg.killRunningProcesses();
            }
        });
        loadFFMpegBinary();
    }

    /**
     * Load FFmpeg binary
     */
    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                Log.d(TAG, "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(context);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    showUnsupportedExceptionDialog();
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            showUnsupportedExceptionDialog();
        } catch (Exception e) {
            Log.d(TAG, "EXception no controlada : " + e);
        }
    }

    private void showUnsupportedExceptionDialog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
               builder .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Not Supported")
                .setMessage("Device Not Supported")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                        //dismiss
                    }
                })
                .create()
                .show();

    }

    /**
     * Command for concating reversed segmented videos
     */
    private void concatVideoCommand(String path1,String path2,String title) {

//        String mypath1="/storage/emulated/0/WhatsApp/Media/WhatsApp Video/VID-20171207-WA0002.mp4"; // whatsapp video
//        String mypath2="/storage/emulated/0/WhatsApp/Media/WhatsApp Video/VID-20171207-WA0000.mp4"; //whatsapp video
//
//        String viralvideo="/storage/emulated/0/Download/open-1.mp4"; //compressed video
//        String cameravideo="/storage/78DF-15FB/DCIM/Camera/VID_20170805_200937.mp4";

        File moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

        String filePrefix = title;
        String fileExtn = ".mp4";
        File dest = new File(moviesDir, filePrefix + fileExtn);
        int fileNo = 0;
        while (dest.exists()) {
            fileNo++;
            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
        }
        filePath = dest.getAbsolutePath();

        String[] complexCommand = new String[]{"-y", "-i", path1, "-i",path2, "-strict", "experimental", "-filter_complex",
                "[0:v]scale=640x360,setsar=1:1[v0];[1:v]scale=640x360,setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=2:v=1:a=1[outv][outa]",
                "-ab", "48000", "-ac", "2", "-ar", "22050", "-s", "640x360", "-vcodec", "libx264","-crf","27","-q","4","-preset", "ultrafast","-map", "[outv]", "-map", "[outa]", dest.getAbsolutePath()};
        execFFmpegBinary(complexCommand);

    }


    /**
     * Executing ffmpeg binary
     */
    private void execFFmpegBinary(final String[] command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.d(TAG, "FAILED with output : " + s);
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(String s) {
                    choice=1;
                    Log.d(TAG, "SUCCESS with output : " + s);

                    Log.d("finalfile", filePath + "");
                    Toast.makeText(context, "Successfully concatenated" + filePath, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                    if (choice == 1)
                        progressDialog.setMessage("progress : concatenating  videos  " + s);
                    Log.d(TAG, "progress : " + s);
                }

                @Override
                public void onStart() {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg " + command);

                        progressDialog.dismiss();

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

}
