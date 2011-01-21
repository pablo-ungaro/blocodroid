package br.com.jawsys.blocodroid;

import java.util.Date;

import android.database.Cursor;

public class Bloco {

	private String nome;
	private String bairro;
	private String endereco;
	private Date data;
	private Integer hora;

	public Bloco(Cursor cursor) {
		nome = cursor.getString(cursor.getColumnIndex("nome"));
		bairro = cursor.getString(cursor.getColumnIndex("bairro"));
		endereco = cursor.getString(cursor.getColumnIndex("endereco"));
		hora = cursor.getInt(cursor.getColumnIndex("hora"));
		data = DBAdapter.parseData(cursor.getString(cursor
				.getColumnIndex("data")));
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

	public Integer getHora() {
		return hora;
	}

	public void setHora(Integer hora) {
		this.hora = hora;
	}

	@Override
	public String toString() {
		return nome;
	}

}
