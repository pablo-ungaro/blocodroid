/**
 * This file is part of BlocoDroid.
 *
 *  BlocoDroid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  BlocoDroid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with BlocoDroid.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.jawsys.mobile.blocodroid.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.services.AvisaBlocosProximosService;

public class Opcoes extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		findPreference("cenv").setOnPreferenceClickListener(
				new OnPreferenceClickListener() {
					@Override public boolean onPreferenceClick(Preference p) {
						Intent cenv = new Intent(Opcoes.this, Propaganda.class);
						cenv.putExtra("forcado", true);
						startActivity(cenv);
						return true;
					}
				});
		
		findPreference("email_erro").setOnPreferenceClickListener(
				new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent emailIntent = new Intent(Intent.ACTION_SEND);
						String[] recipients = new String[] {
								"brunoborges+blocodroid@gmail.com", "", };
						emailIntent.putExtra(
								android.content.Intent.EXTRA_EMAIL, recipients);
						emailIntent.putExtra(
								android.content.Intent.EXTRA_SUBJECT,
								"[BlocoDroid] Ajuste na Programação");
						emailIntent.setType("text/plain");
						startActivity(Intent.createChooser(emailIntent,
								"Enviar email..."));
						finish();
						return true;
					}
				});

		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(
						new OnSharedPreferenceChangeListener() {

							@Override
							public void onSharedPreferenceChanged(
									SharedPreferences sharedPreferences,
									String key) {

								salvarOpcoes(sharedPreferences.getBoolean(
										"notificar", Main.PADRAO_NOTIFICACAO));
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
