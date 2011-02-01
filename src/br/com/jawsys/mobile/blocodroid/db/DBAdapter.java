package br.com.jawsys.mobile.blocodroid.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	public static final String KEY_ROWID = "_id";

	private static final String DBNAME = "blocodroid";

	private static final String DBTABLE = "blocos";

	private static final int DBVERSION = 20;

	private static final String DATABASE_CREATE = "create table blocos (_id integer primary key autoincrement, "
			+ "nome text not null, bairro text not null, favorito boolean not null, "
			+ "data text not null, endereco text not null);";

	public static final String TAG = "blocodroid";

	private final Context context;

	private static final Locale ptBR = new Locale("pt", "BR");

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

	public SortedMap<Date, List<Bloco>> groupByDate() {
		open();

		Cursor cursor = db.query(true, DBTABLE, null, null, null, null, null,
				"nome", null);
		List<Bloco> allBlocos = montaListaBlocos(cursor);

		SortedMap<Date, List<Bloco>> agrupado = new TreeMap<Date, List<Bloco>>();

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

	public List<Bloco> listPorBlocos() {
		open();

		Cursor cursor = db.query(true, DBTABLE,
				new String[] { "nome, favorito" }, null, null, null, null,
				"nome", null);
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
			blocos.add(new Bloco(this, cursor));
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

	public SortedMap<String, List<Bloco>> groupByBairro() {
		open();

		Cursor cursor = db.query(true, DBTABLE, null, null, null, null, null,
				"nome", null);
		List<Bloco> allBlocos = montaListaBlocos(cursor);

		SortedMap<String, List<Bloco>> agrupado = new TreeMap<String, List<Bloco>>();

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

	public List<Bloco> listBlocosFavoritos() {
		open();

		Cursor cursor = db.query(true, DBTABLE,
				new String[] { "nome, favorito" }, "favorito=?",
				new String[] { "1" }, null, null, "nome", null);
		List<Bloco> blocos = montaListaBlocos(cursor);

		close();

		return blocos;
	}
}
