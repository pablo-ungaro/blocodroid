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
	private static final int TEMPO_EXECUCAO = 10 * 60 * 1000;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		consultaProximosBlocos();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Cancel the persistent notification.
		shutdownTimer();
		nm.cancel(R.string.app_name);
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification(Bloco bloco) {
		final String nomeBloco = bloco.getNome();
		final String texto = bloco.getEndereco() + ", " + bloco.getBairro();

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.creep003,
				nomeBloco, bloco.getData().getTime());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// The PendingIntent to launch our activity if the user selects this
		// notification
		final Intent intent = new Intent(this, MostraBloco.class);
		intent.putExtra("nome", nomeBloco);
		intent.setAction("actionstring" + System.currentTimeMillis());
		final PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, nomeBloco, texto, contentIntent);

		nm.notify(nomeBloco, R.id.LinearLayout01, notification);
	}

	private void consultaProximosBlocos() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				List<Bloco> blocos = new DBAdapter(getApplicationContext())
						.listarProximosBlocos();
				for (Bloco b : blocos) {
					showNotification(b);
				}
			}
		}, 0, TEMPO_EXECUCAO);
	}

	private void shutdownTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

}
