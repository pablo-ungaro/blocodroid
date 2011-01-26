package br.com.jawsys.blocodroid.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import br.com.jawsys.blocodroid.R;
import br.com.jawsys.blocodroid.services.AvisaBlocosProximosService;

public class Opcoes extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(
						new OnSharedPreferenceChangeListener() {

							@Override
							public void onSharedPreferenceChanged(
									SharedPreferences sharedPreferences,
									String key) {
								salvarOpcoes(sharedPreferences.getBoolean(key,
										Boolean.FALSE));
							}
						});
	}

	private void salvarOpcoes(boolean b) {
		if (b) {
			startService(new Intent(this, AvisaBlocosProximosService.class));
		} else {
			stopService(new Intent(this, AvisaBlocosProximosService.class));
		}
	}
}
