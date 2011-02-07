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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.preference.PreferenceManager;
import android.util.Log;

public class DBAdapter {

	public static final String KEY_ROWID = "_id";

	private static final String DBNAME = "blocodroid";

	private static final String DBTABLE = "blocos";

	private static final int DBVERSION = 22;

	private static final String DATABASE_CREATE = "create table blocos (_id integer primary key autoincrement, "
			+ "nome text not null, bairro text not null, favorito boolean not null, "
			+ "data integer not null, endereco text not null);";

	public static final String TAG = "blocodroid";

	private final Context context;

	private static final Locale ptBR = new Locale("pt", "BR");

	private static final SimpleDateFormat storageFormatter = new SimpleDateFormat(
			"yyyyMMddHH");

	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM/yyyy HH'hs'", ptBR);

	private static SimpleDateFormat formatadorSemHora = new SimpleDateFormat(
			"dd 'de' MMM, EEEE", ptBR);

	private DatabaseHelper dbHelper;

	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		dbHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DBNAME, null, DBVERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS " + DBTABLE);

			onCreate(db);
		}

	}

	private DBAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	private void close() {
		dbHelper.close();
	}

	public SortedMap<Object, List<Bloco>> listaAgrupadaPorData() {
		open();

		String from = "0";
		if (hideOld()) {
			from = storageFormatter.format(Calendar.getInstance().getTime());
		}

		Cursor cursor = db.query(true, DBTABLE, null, "data > ?",
				new String[] { from }, null, null, "nome", null);
		List<Bloco> allBlocos = montaListaBlocos(cursor);

		SortedMap<Object, List<Bloco>> agrupado = new TreeMap<Object, List<Bloco>>();

		for (Bloco bloco : allBlocos) {
			Date key = (Date) bloco.getData().clone();
			key.setHours(0);
			key.setMinutes(0);
			key.setSeconds(0);

			List<Bloco> blocos = agrupado.get(key);
			if (blocos == null) {
				blocos = new ArrayList<Bloco>();
				agrupado.put(key, blocos);
			}
			blocos.add(bloco);
		}

		return agrupado;
	}

	public List<Bloco> listaTodosBlocos() {
		open();

		String from = "0";
		if (hideOld()) {
			from = storageFormatter.format(Calendar.getInstance().getTime());
		}

		Cursor cursor = db.query(true, DBTABLE,
				new String[] { "nome, favorito" }, "data > ?",
				new String[] { from }, null, null, "nome", null);
		List<Bloco> blocos = montaListaBlocos(cursor);

		close();

		return blocos;
	}

	public static String formataData(Date date) {
		return formatter.format(date);
	}

	public static Date parseData(String data) {
		try {
			return formatter.parse(data);
		} catch (ParseException e) {
			return null;
		}
	}

	public void salvar(List<ContentValues> cv) {
		for (ContentValues c : cv) {
			salvar(c);
		}
	}

	public long salvar(ContentValues cv) {
		open();

		try {
			return _salvar(cv);
		} finally {
			close();
		}
	}

	private long _salvar(ContentValues cv) {
		Cursor c = db.query(DBTABLE, new String[] { KEY_ROWID }, KEY_ROWID
				+ "=" + cv.getAsInteger(KEY_ROWID), null, null, null, null,
				null);

		if (c.getCount() == 0) {
			cv.remove(KEY_ROWID);
			return db.insert(DBTABLE, null, cv);
		} else {
			db.update(DBTABLE, cv, KEY_ROWID + "=?",
					new String[] { cv.getAsString(KEY_ROWID) });
			return cv.getAsLong(KEY_ROWID);
		}
	}

	public void recriar() {
		open();
		dbHelper.onUpgrade(db, db.getVersion(), db.getVersion() + 1);
		close();
	}

	public void updateFavorito(String string, Boolean favorito) {
		open();

		ContentValues cv = new ContentValues();
		cv.put("favorito", favorito);
		db.update(DBTABLE, cv, "nome = ?", new String[] { string.toString() });

		close();
	}

	public List<Bloco> findByNome(String nome) {
		open();

		Cursor cursor = db.query(DBTABLE, null, "nome = ?",
				new String[] { nome }, null, null, null, null);

		List<Bloco> blocos = montaListaBlocos(cursor);

		close();

		return blocos;
	}

	private List<Bloco> montaListaBlocos(Cursor cursor) {
		if (cursor.getCount() < 1) {
			return Collections.emptyList();
		}

		List<Bloco> blocos = new ArrayList<Bloco>();
		while (cursor.moveToNext()) {
			Bloco bloco = new Bloco(this, cursor);
			blocos.add(bloco);
		}
		return blocos;
	}

	public Bloco findById(Integer id) {
		open();

		Cursor cursor = db.query(DBTABLE, null, KEY_ROWID + " = ?",
				new String[] { id.toString() }, null, null, null, null);

		List<Bloco> blocos = montaListaBlocos(cursor);

		close();

		return blocos.get(0);
	}

	public static CharSequence formataDataSemHora(Date data) {
		return formatadorSemHora.format(data);
	}

	public SortedMap<Object, List<Bloco>> listaAgrupadaPorBairro() {
		open();

		String from = "0";
		if (hideOld()) {
			from = storageFormatter.format(Calendar.getInstance().getTime());
		}

		Cursor cursor = db.query(true, DBTABLE, new String[] { "nome",
				"bairro", "endereco" }, "data > ?", new String[] { from },
				null, null, "nome", null);
		List<Bloco> allBlocos = montaListaBlocos(cursor);

		SortedMap<Object, List<Bloco>> agrupado = new TreeMap<Object, List<Bloco>>();

		for (Bloco bloco : allBlocos) {
			String key = bloco.getBairro();

			List<Bloco> blocos = agrupado.get(key);
			if (blocos == null) {
				blocos = new ArrayList<Bloco>();
				agrupado.put(key, blocos);
			}
			blocos.add(bloco);
		}

		return agrupado;
	}

	public List<Bloco> listarBlocosFavoritos() {
		open();

		String from = "0";
		if (hideOld()) {
			from = storageFormatter.format(Calendar.getInstance().getTime());
		}

		Cursor cursor = db.query(true, DBTABLE,
				new String[] { "nome, favorito" }, "favorito = ? and data > ?",
				new String[] { "1", from }, null, null, "nome", null);
		List<Bloco> blocos = montaListaBlocos(cursor);

		close();

		return blocos;
	}

	private boolean hideOld() {
		boolean hideOld = PreferenceManager
				.getDefaultSharedPreferences(context).getBoolean("hide_old",
						false);
		return hideOld;
	}

	private long intervalo() {
		Map<String, ?> all = PreferenceManager.getDefaultSharedPreferences(
				context).getAll();
		long intervalo = Long.parseLong(all.get("intervalo").toString());
		return intervalo;
	}

	public List<String> listarNomesFavoritos() {
		open();
		Cursor cursor = db.query(true, DBTABLE, new String[] { "nome" },
				"favorito = ?", new String[] { "1" }, null, null, "nome", null);

		List<String> favoritos = Collections.emptyList();
		if (cursor.getCount() > 0) {
			favoritos = new ArrayList<String>(cursor.getCount());

			while (cursor.moveToNext()) {
				favoritos.add(cursor.getString(0));
			}
		}

		close();

		return favoritos;
	}

	public void restaurarFavoritos(List<String> favoritos) {
		open();
		for (String nome : favoritos) {
			db.execSQL(
					"update " + DBTABLE + " set favorito = 1 where nome = ?",
					new String[] { nome });
		}
		close();
	}

	public boolean bancoExiste() {
		open();
		SQLiteStatement stmt = db.compileStatement("select count(*) from "
				+ DBTABLE);
		long count = stmt.simpleQueryForLong();
		close();

		return count > 0;
	}

	public static Date parseDataForStorage(String string) {
		try {
			return storageFormatter.parse(string);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formataDataStorage(Date data) {
		return storageFormatter.format(data);
	}

	public List<Bloco> listarProximosBlocos() {
		Calendar instance = Calendar.getInstance();
		Date hoje = instance.getTime();
		Date futuro = instance.getTime();
		futuro.setTime(futuro.getTime() + intervalo());

		String sHoje = formataDataStorage(hoje);
		String sFuturo = formataDataStorage(futuro);

		open();
		Cursor c = db.query(DBTABLE,
				new String[] { "nome, bairro, endereco, data" },
				"favorito = 1 and data > ? and data < ?", new String[] { sHoje,
						sFuturo }, null, null, null, null);
		List<Bloco> lista = montaListaBlocos(c);
		close();

		return lista;
	}

	public List<Bloco> listarBlocosHoje() {
		Calendar instance = Calendar.getInstance();
		Date hoje = instance.getTime();
		hoje.setHours(0);
		hoje.setMinutes(0);
		hoje.setSeconds(0);

		Date futuro = instance.getTime();
		futuro.setTime(hoje.getTime() + (23 * 60 * 60 * 1000) + (59 * 60 * 1000));

		String sHoje = formataDataStorage(hoje);
		String sFuturo = formataDataStorage(futuro);

		open();
		Cursor c = db.query(DBTABLE,
				new String[] { "nome, bairro, endereco, data" },
				"data > ? and data < ?", new String[] { sHoje,
						sFuturo }, null, null, null, null);
		List<Bloco> lista = montaListaBlocos(c);
		close();

		return lista;
	}
}
