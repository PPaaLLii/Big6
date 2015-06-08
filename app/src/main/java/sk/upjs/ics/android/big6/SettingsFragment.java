package sk.upjs.ics.android.big6;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.Toast;

import sk.upjs.ics.android.big6.provider.Big6ContentProvider;
import sk.upjs.ics.android.util.Defaults;

/**
 * Created by Pavol on 8. 6. 2015.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final int DELETE_TRAINING_HISTORY_TOKEN = 0;
    public static final int DELETE_PHOTOS_TOKEN = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        initializeSummaries();
    }

    private void initializeSummaries() {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        //for(String key : sharedPreferences.getAll().keySet()) {
            //onSharedPreferenceChanged(sharedPreferences, key);
        //}

        findPreference("deleteDatabaseButton").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Warning!")
                        .setMessage("This will delete all your Training History! \n Do you want to continue?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteAllHistory();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        })
                        .show();
                return true;
            }
        });

        findPreference("deletePhotosButton").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Warning!")
                        .setMessage("This will delete all your Photos in this application! \n Photos won't be deleted from phone! \n Do you want to continue?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteAllPhotos();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        })
                        .show();
                return true;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if("deleteDatabaseButton".equals(key)) {

        }

        if("deletePhotosButton".equals(key)) {

        }

        if(sharedPreferences != null) {
            switch (key) {
                case "reminderTime.hour":
                    Log.e(getClass().getName(), "reminderTime changed");
                    RemindTrainingSchedule.schedule(getActivity());
                    break;
                case "reminderTime.minute":
                    Log.e(getClass().getName(), "reminderTime changed");
                    RemindTrainingSchedule.schedule(getActivity());
                    break;
                case "selectedDays":
                    RemindTrainingSchedule.schedule(getActivity());
                    break;
                case "enableReminders":
                    RemindTrainingSchedule.schedule(getActivity());
            }
        }
    }

    private void deleteAllHistory() {
        //TODO leak?
        AsyncQueryHandler deleteHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                Toast.makeText(getActivity(), "Training history database was deleted", Toast.LENGTH_SHORT)
                        .show();
            }
        };
        Uri trainingsHistory = Big6ContentProvider.TRAINING_HISTORY_CONTENT_URI;
        //Log.w(getClass().getName(), "uri: "+trainingsHistory.toString());
        deleteHandler.startDelete(DELETE_TRAINING_HISTORY_TOKEN, Defaults.NO_COOKIE, trainingsHistory,
                Defaults.NO_SELECTION, Defaults.NO_SELECTION_ARGS);
    }

    private void deleteAllPhotos() {
        //TODO leak?
        AsyncQueryHandler deleteHandler = new AsyncQueryHandler(getActivity().getContentResolver()) {
            @Override
            protected void onDeleteComplete(int token, Object cookie, int result) {
                notifyPhotoActivityToClearAdapter();
                Toast.makeText(getActivity(), "Photos was deleted!", Toast.LENGTH_SHORT)
                        .show();
            }
        };
        Uri photos = Big6ContentProvider.PHOTO_URI_CONTENT_URI;
        //Log.w(getClass().getName(), "uri: "+photos.toString());
        deleteHandler.startDelete(DELETE_PHOTOS_TOKEN, Defaults.NO_COOKIE, photos,
                Defaults.NO_SELECTION, Defaults.NO_SELECTION_ARGS);
    }

    private void notifyPhotoActivityToClearAdapter() {
        //TODO: Implement!
    }
}
