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
package br.com.jawsys.mobile.blocodroid.db;

import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

public class Bloco {

	private Integer _id;
	private String nome;
	private String bairro;
	private String endereco;
	private Date data;
	private Boolean favorito = Boolean.FALSE;
	private DBAdapter db;

	public Bloco(DBAdapter db, Cursor cursor) {
		this.db = db;

		int columnIndex = cursor.getColumnIndex(DBAdapter.KEY_ROWID);
		if (columnIndex > -1) {
			_id = cursor.getInt(columnIndex);
		}

		columnIndex = cursor.getColumnIndex("nome");
		if (columnIndex > -1) {
			nome = cursor.getString(columnIndex);
		}

		columnIndex = cursor.getColumnIndex("bairro");
		if (columnIndex > -1) {
			bairro = cursor.getString(cursor.getColumnIndex("bairro"));
		}

		columnIndex = cursor.getColumnIndex("endereco");
		if (columnIndex > -1) {
			endereco = cursor.getString(cursor.getColumnIndex("endereco"));
		}

		columnIndex = cursor.getColumnIndex("favorito");
		if (columnIndex > -1) {
			String string = cursor.getString(cursor.getColumnIndex("favorito"));
			favorito = Boolean.valueOf("1".equals(string) ? true : false);
		}

		columnIndex = cursor.getColumnIndex("data");
		if (columnIndex > -1) {
			data = DBAdapter.parseDataForStorage(cursor.getString(cursor
					.getColumnIndex("data")));
		}
	}

	public Bloco(DBAdapter db) {
		this.db = db;
	}

	public Bloco() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return nome;
	}

	public Boolean isFavorito() {
		return favorito;
	}

	public void setFavorito(Boolean favorito) {
		this.favorito = favorito;
	}

	public void trocaFavorito(boolean isChecked) {
		setFavorito(isChecked);
		db.updateFavorito(getNome(), isFavorito());
	}

	public Integer getId() {
		return _id;
	}

	public ContentValues buildContentValues() {
		ContentValues cv = new ContentValues();
		cv.put("data", DBAdapter.formataDataStorage(getData()));
		cv.put("nome", getNome());
		cv.put("bairro", getBairro());
		cv.put("endereco", getEndereco());
		cv.put("favorito", isFavorito());
		cv.put("_id", getId());
		return cv;
	}

}
