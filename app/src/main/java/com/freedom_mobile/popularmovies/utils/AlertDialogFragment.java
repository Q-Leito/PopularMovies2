package com.freedom_mobile.popularmovies.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.freedom_mobile.popularmovies.R;

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.title))
                .setMessage(context.getString(R.string.error_message))
                .setPositiveButton(context.getString(android.R.string.ok), null);

        return builder.create();
    }
}
