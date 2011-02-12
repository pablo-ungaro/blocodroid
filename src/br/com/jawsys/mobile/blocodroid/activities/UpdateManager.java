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
import br.com.jawsys.mobile.blocodroid.db.Bloco;
import br.com.jawsys.mobile.blocodroid.db.CarregarXML;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

public class UpdateManager {

	private DBAdapter db;

	public UpdateManager(DBAdapter db) {
		this.db = db;
	}

	public void atualizarDados() throws Exception {
		CarregarXML xml = new CarregarXML();
		List<Bloco> blocos = xml.listarBlocos();
		List<ContentValues> listaCVs = new ArrayList<ContentValues>(blocos.size());
		for (Bloco b : blocos) {
			listaCVs.add(b.buildContentValues());
		}
		db.recriar();
		db.inserir(listaCVs);
	}

}
