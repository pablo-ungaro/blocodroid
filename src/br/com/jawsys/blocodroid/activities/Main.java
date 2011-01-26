package br.com.jawsys.blocodroid.activities;

import java.util.Calendar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.com.jawsys.blocodroid.R;
import br.com.jawsys.blocodroid.db.Bloco;
import br.com.jawsys.blocodroid.db.DBAdapter;

public class Main extends Activity implements OnClickListener {

	private NotificationManager nManager;

	private static final int ACTIVITY_OPCOES = 0;

	private static final int SECOND = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		new UpdateManager(this).inicializaDados();
		configBotoes();

		nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	protected void notificarBloco(Bloco bloco) {
		Intent intent = new Intent(this, Main.class);

		Notification notification = new Notification(R.drawable.icon, "Notify",
				3000);

		notification.setLatestEventInfo(Main.this,
				getString(R.string.app_name), bloco.getNome(), PendingIntent
						.getActivity(this.getBaseContext(), 0, intent,
								PendingIntent.FLAG_CANCEL_CURRENT));

		nManager.notify(0, notification);
	}

	private void configBotoes() {
		Button botao = (Button) findViewById(R.id.botaoBloco);
		botao.setOnClickListener(this);
		botao = (Button) findViewById(R.id.botaoData);
		botao.setOnClickListener(this);
		botao = (Button) findViewById(R.id.botaoBairro);
		botao.setOnClickListener(this);
	}

	// Create Menu Option
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuItem iOpcoes = menu.add(0, Menu.FIRST, 0, R.string.botaoOpcoes);
		MenuItem iAtualizar = menu.add(0, SECOND, 1, R.string.botaoAtualizar);

		iOpcoes.setIcon(R.drawable.ic_opcoes);
		iAtualizar.setIcon(R.drawable.ic_atualizar);

		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST: {
			Intent intent = new Intent(this, Opcoes.class);
			startActivityForResult(intent, ACTIVITY_OPCOES);

			return super.onOptionsItemSelected(item);
		}
		case SECOND: {
			atualizarDados();
			return super.onOptionsItemSelected(item);
		}
		}

		return true;
	}

	private void atualizarDados() {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case ACTIVITY_OPCOES:
			// Bundle bundle = intent.getExtras();
			/*
			 * title_raw = bundle.getString(SBooksDbAdapter.KEY_TITLE_RAW); if
			 * (title_raw != null) { Cursor cursor =
			 * mDbHelper.searchData(title_raw); String[] from = new String[] {
			 * SBooksDbAdapter.KEY_ROWID, SBooksDbAdapter.KEY_TITLE,
			 * SBooksDbAdapter.KEY_LYRICS }; int[] to = new int[] { R.id.id,
			 * R.id.title, R.id.lyrics }; SimpleCursorAdapter adapter = new
			 * SimpleCursorAdapter(this, R.layout.sbooks_row, cursor, from, to);
			 * setListAdapter(adapter);
			 */
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case (R.id.botaoBloco): {
			Intent intent = new Intent(v.getContext(), PorBlocos.class);
			Main.this.startActivity(intent);
			break;
		}
		case (R.id.botaoBairro): {
			Bloco b = new Bloco(new DBAdapter(this));
			b.setData(Calendar.getInstance().getTime());
			b.setNome("Carmelitas");
			notificarBloco(b);
			break;
		}
		case (R.id.botaoData): {
			break;
		}
		}
	}

}
