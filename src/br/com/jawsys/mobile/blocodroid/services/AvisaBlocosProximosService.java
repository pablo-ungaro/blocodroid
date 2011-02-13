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
package br.com.jawsys.mobile.blocodroid.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.activities.MostraBloco;
import br.com.jawsys.mobile.blocodroid.db.Bloco;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

public class AvisaBlocosProximosService extends Service {

	private NotificationManager nm;
	private Timer timer = new Timer();
	private DBAdapter dbAdapter;
	private static final int TEMPO_EXECUCAO = 10 * 60 * 1000;
	private static final int TEMPO_INICIAL = 5 * 60 * 1000;
	private static final int UNIKE_KEY = R.id.botaoBairro;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		dbAdapter = new DBAdapter(getApplicationContext());

		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				List<Bloco> blocos = dbAdapter.listarProximosBlocos();
				for (Bloco b : blocos) {
					showNotification(b);
				}
			}
		}, TEMPO_INICIAL, TEMPO_EXECUCAO);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		shutdownTimer();
		nm.cancel(UNIKE_KEY);
	}

	private void showNotification(Bloco bloco) {
		final String nomeBloco = bloco.getNome();
		final String texto = bloco.getEndereco() + ", " + bloco.getBairro();

		Notification notification = new Notification(R.drawable.creep003,
				nomeBloco, bloco.getData().getTime());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		final Intent intent = new Intent(this, MostraBloco.class);
		intent.putExtra("nome", nomeBloco);
		intent.setAction("actionstring" + System.currentTimeMillis());
		final PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(this, nomeBloco, texto, contentIntent);

		nm.notify(nomeBloco, UNIKE_KEY, notification);
	}

	private void shutdownTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

}
