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

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

public class PorAgrupamento extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.poragrupamento);

		ExpandableListView epView = (ExpandableListView) findViewById(R.id.listViewAgrupada);

		Intent i = getIntent();
		final String tipo = i.getStringExtra("tipo");

		DBAdapter db = new DBAdapter(this);
		SortedMap<Object, List<Bloco>> listaAgrupada = tipo.equals("bairro") ? db
				.listaAgrupadaPorBairro() : db.listaAgrupadaPorData();
		ExpandableListAdapter mAdapter = new BlocosAgrupadosExpandableListAdapter(
				this, listaAgrupada) {
			@Override
			protected CharSequence formataChaveGrupo(Object key) {
				if (tipo.equals("data")) {
					return DBAdapter.formataDataSemHora((Date) key);
				}

				return super.formataChaveGrupo(key);
			}
		};

		epView.setAdapter(mAdapter);
	}
}
