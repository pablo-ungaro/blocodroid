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

	public UpdateManager(Context main) {
		this.context = main;
	}

	public void inicializaDados() throws Exception {
		DBAdapter db = new DBAdapter(context);

		if (db.listPorBlocos().isEmpty() == false) {
			return;
		}

		atualizarDados();
	}

	public void atualizarDados() throws Exception {
		CarregarXML xml = new CarregarXML();
		List<Bloco> blocos = xml.listarBlocos();

		List<ContentValues> cvs = new ArrayList<ContentValues>(blocos.size());
		for (Bloco b : blocos) {
			cvs.add(b.buildContentValues());
		}
		DBAdapter db = new DBAdapter(context);
		db.recriar();

		db.salvar(cvs);
	}

	public void run() {
	}

}
