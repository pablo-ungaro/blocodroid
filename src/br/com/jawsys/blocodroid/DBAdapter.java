package br.com.jawsys.blocodroid;

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

	private static final String DATABASE_NAME = "blocodroid";
	private static final String DATABASE_TABLE = "blocos";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table blocos (_id integer primary key autoincrement, "
			+ "nome text not null, bairro text not null, "
			+ "data date not null, hora number not null, endereco text not null);";

	public static final String TAG = "blocodroid";

	private final Context context;

	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM/yyyy");

	private DatabaseHelper dbHelper;

	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		dbHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public DBAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public void inserirBlocosTeste() {
		ContentValues initialValues = new ContentValues();
		initialValues.put("nome", "Bola Preta");
		initialValues.put("bairro", "Centro");
		Date date = new Date(2010, 1, 20);
		String dataFormatada = formataData(date);
		initialValues.put("data", dataFormatada);
		initialValues.put("hora", 15);
		initialValues.put("endereco", "Praça Mauá");

		db.insert(DATABASE_TABLE, null, initialValues);
	}

	public List<Bloco> listAllBlocos() {
		Cursor cursor = db.query(DATABASE_TABLE, null, null, null, null, null,
				"nome");
		if (cursor.getCount() < 1) {
			return Collections.emptyList();
		}

		List<Bloco> blocos = new ArrayList<Bloco>();
		while (cursor.moveToNext()) {
			blocos.add(new Bloco(cursor));
		}
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

	// ---insert a title into the database---
	/*
	 * public long insertTitle(String isbn, String title, String publisher) {
	 * ContentValues initialValues = new ContentValues();
	 * initialValues.put(KEY_ISBN, isbn); initialValues.put(KEY_TITLE, title);
	 * initialValues.put(KEY_PUBLISHER, publisher); return
	 * db.insert(DATABASE_TABLE, null, initialValues); }
	 */

	// ---deletes a particular title---
	/*
	 * public boolean deleteTitle(long rowId) { return db.delete(DATABASE_TABLE,
	 * KEY_ROWID + "=" + rowId, null) > 0; }
	 */

	// ---retrieves all the titles---
	/*
	 * public Cursor getAllTitles() { return db.query(DATABASE_TABLE, new
	 * String[] { KEY_ROWID, KEY_ISBN, KEY_TITLE, KEY_PUBLISHER }, null, null,
	 * null, null, null); }
	 */

	// ---retrieves a particular title---
	/*
	 * public Cursor getTitle(long rowId) throws SQLException { Cursor mCursor =
	 * db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_ISBN,
	 * KEY_TITLE, KEY_PUBLISHER }, KEY_ROWID + "=" + rowId, null, null, null,
	 * null, null); if (mCursor != null) { mCursor.moveToFirst(); } return
	 * mCursor; }
	 */

}
