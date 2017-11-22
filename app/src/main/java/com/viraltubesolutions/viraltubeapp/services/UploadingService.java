package com.viraltubesolutions.viraltubeapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Shashi on 10/27/2017.
 */

public class UploadingService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UploadingService() {
        super("UploadingService");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
