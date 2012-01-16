package br.com.jawsys.mobile.blocodroid.activities;

import br.com.jawsys.mobile.blocodroid.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;

public class Propaganda extends Activity {

	private static final String SITE_CENV = "http://www.compreienaovou.com.br";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (deveMostrarCenv()) {
			setContentView(R.layout.cenv);
			CheckBox cb = (CheckBox) findViewById(R.id.cbNaoMostrar);
			cb.setChecked(naoMostrarCenv());
		} else {
			startActivity(new Intent(this, Main.class));
			finish();
		}
	}
	
	public void continuar(View v) {
		startActivity(new Intent(this, Main.class));
	}

	public void flagNaoMostrar(View v) {
		Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
		edit.putBoolean("naoMostrarCenv", ((CheckBox) v).isChecked());
		edit.commit();
	}
	
	private boolean naoMostrarCenv() {
		return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("naoMostrarCenv", false);
	}
	
	private boolean deveMostrarCenv() {
		if (getIntent().hasExtra("forcado")) {
			return true;
		}
		return !naoMostrarCenv();
	}
	
	public void mostraSiteCenvNewTicket(View v) {
		Uri uri = Uri.parse(SITE_CENV+"/ticket/new?ref=blocodroid");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void mostraSiteCenv(View v) {
		Uri uri = Uri.parse(SITE_CENV+"?ref=blocodroid");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}


}
