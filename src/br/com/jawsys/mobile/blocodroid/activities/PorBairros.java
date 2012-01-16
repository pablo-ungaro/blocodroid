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

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

public class PorBairros extends Activity {

	private static final int MENU_CONFIG = 0;
	private ListView listaBlocosView;

	@Override
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.porbairros);

		DBAdapter db = new DBAdapter(this);
		List<String> bairros = db.listarBairros();

		listaBlocosView = (ListView) findViewById(R.id.listaBairros);
		listaBlocosView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, bairros));
		listaBlocosView.setTextFilterEnabled(true);
	}

	@Override
	public boolean onSearchRequested() {
		InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMgr.toggleSoftInput(0, 0);
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_CONFIG, 0, R.string.botaoBuscar).setIcon(
				R.drawable.filter);

		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return onSearchRequested();
	}
}
