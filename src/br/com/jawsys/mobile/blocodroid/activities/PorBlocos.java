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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

public class PorBlocos extends Activity {

	private ListaBlocos listaBlocosView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.porblocos);

		boolean favoritos = getIntent().getExtras().getBoolean("favoritos");

		DBAdapter db = new DBAdapter(this);
		List<Bloco> blocos = favoritos ? db.listarBlocosFavoritos() : db
				.listaTodosBlocos();

		if (!blocos.isEmpty() || !favoritos) {
			findViewById(R.id.nenhumFavorito).setVisibility(View.GONE);
		}

		listaBlocosView = (ListaBlocos) findViewById(R.id.listaBlocos);
		listaBlocosView.setAdapter(new ListaBlocosAdapter(this, blocos));
		listaBlocosView.setTextFilterEnabled(true);
	}

	@Override
	public boolean onSearchRequested() {
		InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMgr.toggleSoftInput(0, 0);
		return true;
	}

}
