package br.com.jawsys.mobile.blocodroid.activities;

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
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;

public class Main extends Activity implements OnClickListener, OnTouchListener {

	private NotificationManager nManager;

	private ProgressDialog pd;

	private AlertDialog confirmaAtualizacao;

	private static final int ACTIVITY_OPCOES = 0;

	private static final int MENU_ATUALIZA = 1;

	protected static final int PROGRESS_DIALOG = 4;

	private static final int MENU_VER_MAPA = 2;

	private static final int MENU_CONFIG = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		try {
			new UpdateManager(this).inicializaDados();
		} catch (Exception e) {
			erroAoCarregarXML();
		}
		configBotoes();

		nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	private void erroAoCarregarXML() {
		AlertDialog erro = new AlertDialog.Builder(this)
				.setMessage(
						"Nao foi possível obter a lista de blocos. Tente mais tarde.")
				.setTitle("Erro :-(").setCancelable(true)
				.setIcon(android.R.drawable.ic_dialog_alert).create();
		erro.show();
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
		configBotao((Button) findViewById(R.id.botaoBloco));
		configBotao((Button) findViewById(R.id.botaoFavoritos));
		configBotao((Button) findViewById(R.id.botaoData));
		configBotao((Button) findViewById(R.id.botaoBairro));
	}
	
	private void configBotao(Button botao) {
		botao.setOnClickListener(this);
		botao.setOnTouchListener(this);
	}

	// Create Menu Option
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_CONFIG, 0, R.string.botaoOpcoes).setEnabled(false);
		menu.add(0, MENU_ATUALIZA, 1, R.string.botaoAtualizar);
		menu.add(0, MENU_VER_MAPA, 2, R.string.verMapa);

		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_CONFIG: {
			Intent intent = new Intent(this, Opcoes.class);
			startActivityForResult(intent, ACTIVITY_OPCOES);

			return super.onOptionsItemSelected(item);
		}
		case MENU_ATUALIZA: {
			atualizarDados();
			return super.onOptionsItemSelected(item);
		}
		case MENU_VER_MAPA: {
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
										try {
											atualizarDados();
										} catch (RuntimeException re) {
											erroAoCarregarXML();
										} catch (Exception e) {
											erroAoCarregarXML();
										}
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
		mostraAtividade(v);
	}

	private void mostraAtividade(View v) {
		switch (v.getId()) {
		case (R.id.botaoBloco):
		case(R.id.botaoFavoritos): {
			Intent intent = new Intent(v.getContext(), PorBlocos.class);
			intent.putExtra("favoritos", R.id.botaoFavoritos == v.getId());
			Main.this.startActivity(intent);
			break;
		}
		case (R.id.botaoBairro): {
			Intent intent = new Intent(v.getContext(), PorBairros.class);
			Main.this.startActivity(intent);
			break;
		}
		case (R.id.botaoData): {
			Intent intent = new Intent(v.getContext(), PorDatas.class);
			Main.this.startActivity(intent);
			break;
		}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vib.vibrate(35);
			((Button) v).setBackgroundDrawable(getResources().getDrawable(
					R.drawable.clickedrountedbox));
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			((Button) v).setBackgroundDrawable(getResources().getDrawable(
					R.drawable.rountedbox));
			mostraAtividade(v);
		}
		return true;
	}

}
