package br.com.jawsys.blocodroid.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import br.com.jawsys.blocodroid.db.Bloco;
import br.com.jawsys.blocodroid.db.CarregarXML;
import br.com.jawsys.blocodroid.db.DBAdapter;

public class UpdateManager implements Runnable {

	private Context context;

	public UpdateManager(Context main) {
		this.context = main;
	}

	public void inicializaDados() {
		DBAdapter db = new DBAdapter(context);

		if (db.listPorBlocos().isEmpty() == false) {
			return;
		}

		atualizarDados(null);
	}

	public void atualizarDados(CallbackUpdateManager callbackUpdateManager) {
		if (callbackUpdateManager != null) {
			callbackUpdateManager.steps(5);
		}

		CarregarXML xml = null;
		try {
			xml = new CarregarXML();

			if (callbackUpdateManager != null) {
				callbackUpdateManager.progress();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Bloco> blocos = xml.listarBlocos();

		if (callbackUpdateManager != null) {
			callbackUpdateManager.progress();
		}

		if (callbackUpdateManager != null) {
			callbackUpdateManager.steps(blocos.size() + 5);
		}

		List<ContentValues> cvs = new ArrayList<ContentValues>(blocos.size());
		for (Bloco b : blocos) {
			cvs.add(b.buildContentValues());
			if (callbackUpdateManager != null) {
				callbackUpdateManager.progress();
			}
		}
		DBAdapter db = new DBAdapter(context);
		db.recriar();
		if (callbackUpdateManager != null) {
			callbackUpdateManager.progress();
		}

		db.salvar(cvs);
		if (callbackUpdateManager != null) {
			callbackUpdateManager.progress();
		}

	}

	public void run() {
	}

}
