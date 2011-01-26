package br.com.jawsys.blocodroid.db;

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

		_id = cursor.getInt(cursor.getColumnIndex(DBAdapter.KEY_ROWID));
		nome = cursor.getString(cursor.getColumnIndex("nome"));
		bairro = cursor.getString(cursor.getColumnIndex("bairro"));
		endereco = cursor.getString(cursor.getColumnIndex("endereco"));
		String string = cursor.getString(cursor.getColumnIndex("favorito"));
		favorito = Boolean.valueOf("1".equals(string) ? true : false);
		data = DBAdapter.parseData(cursor.getString(cursor
				.getColumnIndex("data")));
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

	public void trocaFavorito() {
		setFavorito(!isFavorito());
		salvar();
	}

	public Integer getId() {
		return _id;
	}

	public void salvar() {
		ContentValues cv = buildContentValues();
		db.salvar(cv);
	}

	public ContentValues buildContentValues() {
		ContentValues cv = new ContentValues();
		cv.put("data", DBAdapter.formataData(getData()));
		cv.put("nome", getNome());
		cv.put("bairro", getBairro());
		cv.put("endereco", getEndereco());
		cv.put("favorito", isFavorito());
		cv.put("_id", getId());
		return cv;
	}

}
