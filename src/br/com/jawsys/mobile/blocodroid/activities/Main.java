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
package br.com.jawsys.mobile.blocodroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;
import br.com.jawsys.mobile.blocodroid.services.AvisaBlocosProximosService;

public class Main extends Activity implements OnClickListener, OnTouchListener {

	private static final String MAPA_BLOCOS_DIARIORIO = "http://goo.gl/maps/dJmE";

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
		configBotoes();

		boolean notificar = PreferenceManager.getDefaultSharedPreferences(this)
				.getBoolean("notificar", false);
		if (notificar) {
			startService(new Intent(this, AvisaBlocosProximosService.class));
		}

		runUpdateManager(true);
	}

	private void erroAoCarregarXML() {
		AlertDialog erro = new AlertDialog.Builder(this)
				.setMessage(
						"Nao foi possível obter a lista de blocos. Tente mais tarde.")
				.setTitle("Erro :-(").setCancelable(true)
				.setIcon(android.R.drawable.ic_dialog_alert).create();
		erro.show();
	}

	private void configBotoes() {
		configBotao((Button) findViewById(R.id.botaoBloco));
		configBotao((Button) findViewById(R.id.botaoFavoritos));
		configBotao((Button) findViewById(R.id.botaoData));
		configBotao((Button) findViewById(R.id.botaoBairro));
		configBotao((Button) findViewById(R.id.botaoProximidade));
	}

	private void configBotao(Button botao) {
		botao.setOnClickListener(this);
		botao.setOnTouchListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_CONFIG, 0, R.string.botaoOpcoes).setIcon(
				R.drawable.equalizer);
		menu.add(0, MENU_ATUALIZA, 1, R.string.botaoAtualizar).setIcon(
				R.drawable.sun);
		menu.add(0, MENU_VER_MAPA, 2, R.string.verMapa)
				.setIcon(R.drawable.flag);

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
					Uri.parse(MAPA_BLOCOS_DIARIORIO));
			startActivity(viewWholeMap);
		}
		}

		return true;
	}

	private void atualizarDados() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Deseja continuar?")
				.setCancelable(false)
				.setPositiveButton("Sim",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								runUpdateManager(false);
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
		case (R.id.botaoProximidade): {
			Intent intent = new Intent(v.getContext(), PorProximidade.class);
			startActivity(intent);
			break;
		}
		case (R.id.botaoBloco):
		case (R.id.botaoFavoritos): {
			Intent intent = new Intent(v.getContext(), PorBlocos.class);
			intent.putExtra("favoritos", R.id.botaoFavoritos == v.getId());
			startActivity(intent);
			break;
		}
		case (R.id.botaoBairro): {
			Intent intent = new Intent(v.getContext(), PorAgrupamento.class);
			intent.putExtra("tipo", "bairro");
			startActivity(intent);
			break;
		}
		case (R.id.botaoData): {
			Intent intent = new Intent(v.getContext(), PorAgrupamento.class);
			intent.putExtra("tipo", "data");
			startActivity(intent);
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
			onClick(v);
		}
		return true;
	}

	private void runUpdateManager(boolean initial) {
		if (new DBAdapter(this).bancoExiste() && initial) {
			return;
		}

		pd = ProgressDialog.show(Main.this, "BlocoDroid",
				"Atualizando. Aguarde...", true);

		Thread t = new Thread(new UpdateManager(Main.this) {
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

}
