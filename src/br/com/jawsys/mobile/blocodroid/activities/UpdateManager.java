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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import br.com.jawsys.mobile.blocodroid.db.Bloco;
import br.com.jawsys.mobile.blocodroid.db.CarregarXML;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

public class UpdateManager implements Runnable {

	private Context context;
	private DBAdapter db;

	public UpdateManager(Context main) {
		this.context = main;
		db = new DBAdapter(context);
	}

	public void atualizarDados() throws Exception {
		List<String> favoritos = db.listarNomesFavoritos();

		CarregarXML xml = new CarregarXML();
		List<Bloco> blocos = xml.listarBlocos();
		List<ContentValues> cvs = new ArrayList<ContentValues>(blocos.size());
		for (Bloco b : blocos) {
			if (favoritos.contains(b.getNome())) {
				b.setFavorito(true);
			}
			cvs.add(b.buildContentValues());
		}
		db.recriar();
		db.salvar(cvs);
	}

	public void run() {
	}

}
