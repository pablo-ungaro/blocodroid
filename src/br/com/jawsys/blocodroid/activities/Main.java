package br.com.jawsys.blocodroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.com.jawsys.blocodroid.R;
import br.com.jawsys.blocodroid.db.Bloco;

public class Main extends Activity implements OnClickListener {

	private NotificationManager nManager;

	private ProgressDialog pd;

	private AlertDialog confirmaAtualizacao;

	private static final int ACTIVITY_OPCOES = 0;

	private static final int SECOND = 2;

	protected static final int PROGRESS_DIALOG = 4;

	private static final int THIRD = 6;

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
		MenuItem iVerMapa = menu.add(0, THIRD, 2, R.string.verMapa);

		iOpcoes.setTitle(R.string.botaoOpcoes);
		iAtualizar.setTitle(R.string.botaoAtualizar);
		iVerMapa.setTitle(R.string.verMapa);

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
		case THIRD: {
			Intent viewWholeMap = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://goo.gl/maps/dJmE"));
			startActivity(viewWholeMap);
		}
		}

		return true;
	}

	private void atualizarDados() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Blocos marcados como Favoritos serão perdidos. Deseja continuar?")
				.setCancelable(false)
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								pd = ProgressDialog.show(Main.this, "",
										"Atualizando. Aguarde...", true);

								Thread t = new Thread(new UpdateManager(
										Main.this) {
									@Override
									public void run() {
										atualizarDados(null);
										handler.sendEmptyMessage(0);
									}
								});
								t.start();
							}
						})
				.setNegativeButton("Não",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		confirmaAtualizacao = builder.create();
		showDialog(0);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return confirmaAtualizacao;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case (R.id.botaoBloco): {
			Intent intent = new Intent(v.getContext(), PorBlocos.class);
			Main.this.startActivity(intent);
			break;
		}
		case (R.id.botaoBairro): {
			/*
			 * Bloco b = new Bloco(new DBAdapter(this));
			 * b.setData(Calendar.getInstance().getTime());
			 * b.setNome("Carmelitas"); notificarBloco(b);
			 */
			break;
		}
		case (R.id.botaoData): {
			Intent intent = new Intent(v.getContext(), PorDatas.class);
			Main.this.startActivity(intent);
			break;
		}
		}
	}

}
