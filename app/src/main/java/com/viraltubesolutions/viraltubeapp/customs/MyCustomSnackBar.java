package com.viraltubesolutions.viraltubeapp.customs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.viraltubesolutions.viraltubeapp.R;

/**
 * Created by Shashi on 10/5/2017.
 */

public class MyCustomSnackBar {
    public static final int LENGTH_SHORT = Snackbar.LENGTH_SHORT;

    public static Snackbar makeText(Context context, int resId, int duration) {
        Activity activity = (Activity) context;
        View layout;

        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content), context.getResources().getText(resId), duration);
        layout = snackbar.getView();
        int resID;

        layout.setBackgroundColor(context.getResources().getColor(R.color.snackbar_color));

        android.widget.TextView text = (android.widget.TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        text.setTextColor(context.getResources().getColor(R.color.snackbar_text));
        Typeface font = null;

        font = Typeface.createFromAsset(context.getAssets(), "fonts/MontserratAlternates-Regular.ttf");

        text.setTypeface(font);
        return snackbar;

    }

    public static Snackbar makeText(Context context, String message, int duration) {
        Activity activity = (Activity) context;
        View layout;
        Snackbar snackbar = Snackbar
                .make(activity.findViewById(android.R.id.content), message, duration);
        layout = snackbar.getView();
        layout.setBackgroundColor(context.getResources().getColor(R.color.snackbar_color));
        android.widget.TextView text = (android.widget.TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        text.setTextColor(context.getResources().getColor(R.color.snackbar_text));
        Typeface font = null;
        font = Typeface.createFromAsset(context.getAssets(), "fonts/MontserratAlternates-Regular.ttf");

        text.setTypeface(font);
        return snackbar;

    }

    public static Snackbar makeText(Context context, View view, String message, int duration) {
        View layout;
        Snackbar snackbar = Snackbar
                .make(view, message, duration);
        layout = snackbar.getView();
        layout.setBackgroundColor(context.getResources().getColor(R.color.snackbar_color));
        android.widget.TextView text = (android.widget.TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        text.setTextColor(context.getResources().getColor(R.color.snackbar_text));
        Typeface font = null;
        font = Typeface.createFromAsset(context.getAssets(), "fonts/MontserratAlternates-Regular.ttf");

        text.setTypeface(font);
        return snackbar;

    }

    public static Snackbar makeText(Context context, View view, int resId, int duration) {
        View layout;
        Snackbar snackbar = Snackbar.make(view, context.getResources().getText(resId), duration);
        layout = snackbar.getView();
        layout.setBackgroundColor(context.getResources().getColor(R.color.snackbar_color));
        android.widget.TextView text = (android.widget.TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        text.setTextColor(context.getResources().getColor(R.color.snackbar_text));
        Typeface font = null;
        font = Typeface.createFromAsset(context.getAssets(), "fonts/MontserratAlternates-Regular.ttf");

        text.setTypeface(font);
        return snackbar;

    }

}
