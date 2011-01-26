package br.com.jawsys.blocodroid.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

	private static final int DBVERSION = 14;

	private static final String DATABASE_CREATE = "create table blocos (_id integer primary key autoincrement, "
			+ "nome text not null, bairro text not null, favorito boolean not null, "
			+ "data date not null, endereco text not null);";

	public static final String TAG = "blocodroid";

	private final Context context;

	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM/yyyy HH'hs'");

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

	public List<Bloco> listAllBlocos() {
		open();

		Cursor cursor = db.query(DBTABLE, null, null, null, null, null, "nome");
		if (cursor.getCount() < 1) {
			return Collections.emptyList();
		}

		List<Bloco> blocos = new ArrayList<Bloco>();
		while (cursor.moveToNext()) {
			blocos.add(new Bloco(this, cursor));
		}

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

}
