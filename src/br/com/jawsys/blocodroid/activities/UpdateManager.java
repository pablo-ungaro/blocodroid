package br.com.jawsys.blocodroid.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import br.com.jawsys.blocodroid.db.Bloco;
import br.com.jawsys.blocodroid.db.CarregarXML;
import br.com.jawsys.blocodroid.db.DBAdapter;

public class UpdateManager {

	private Context context;

	public UpdateManager(Context main) {
		this.context = main;
	}

	protected void inicializaDados() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		if (sp.contains("versao")) {
			return;
		}

		DBAdapter db = new DBAdapter(context);
		CarregarXML xml = null;
		try {
			xml = new CarregarXML();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Bloco> blocos = xml.listarBlocos();
		List<ContentValues> cvs = new ArrayList<ContentValues>(blocos.size());
		for (Bloco b : blocos) {
			cvs.add(b.buildContentValues());
		}
		db.salvar(cvs);

		sp.edit().putInt("versao", xml.getVersao());
	}

}
