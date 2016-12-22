package com.stroeer.configurationtestapp;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

public class LocationSettingsDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        ComponentName resolveActivity = intent.resolveActivity(getActivity().getPackageManager());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.diag_title_location_settings);

        if (resolveActivity != null) {
            builder.setMessage(R.string.diag_message_location_settings_open);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(android.R.string.no, null);
        } else {
            builder.setMessage(R.string.diag_message_location_settings_unavailable);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });
        }

        return builder.create();
    }
}
