/*
 * Copyright (c) 2014 Oleksandr Tyshkovets <olexandr.tyshkovets@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gnucash.android.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;

import org.gnucash.android.R;
import org.gnucash.android.ui.UxArgument;
import org.gnucash.android.ui.passcode.PasscodePreferenceActivity;

/**
 * Fragment for configuring passcode to the application
 * @author Oleksandr Tyshkovets <olexandr.tyshkovets@gmail.com>
 */
public class PasscodePreferenceFragment extends PreferenceFragment {

    private static final int REQUEST_CODE = 1;

    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_passcode_preferences);

        ActionBar actionBar = ((SherlockPreferenceActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.title_passcode_preferences);
    }

    @Override
    public void onResume() {
        super.onResume();

        editor = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit();


        findPreference(getString(R.string.key_enable_passcode))
                .setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        editor.putBoolean(UxArgument.ENABLED_PASSCODE, (Boolean) newValue);
                        editor.commit();
                        return true;
                    }
                });
        findPreference(getString(R.string.key_change_passcode))
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        startActivityForResult(
                                new Intent(getActivity(), PasscodePreferenceActivity.class), REQUEST_CODE);
                        return true;
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data!= null) {
            editor.putString(UxArgument.PASSCODE, data.getStringExtra(UxArgument.PASSCODE));
            editor.commit();
            Toast.makeText(getActivity(), R.string.toast_passcode_set, Toast.LENGTH_SHORT).show();
        }
    }

}
