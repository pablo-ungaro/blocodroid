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

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.activities.Main;

public class AvisaBlocosProximosService extends Service {

	private Long counter = 0L;
	private NotificationManager nm;
	private Timer timer = new Timer();
	private final Calendar time = Calendar.getInstance();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Toast.makeText(this, "Service created at " + time.getTime(),
				Toast.LENGTH_LONG).show();
		showNotification();
		incrementCounter();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Cancel the persistent notification.
		shutdownCounter();
		nm.cancel(R.string.app_name);
		Toast.makeText(
				this,
				"Service destroyed at " + time.getTime() + "; counter is at: "
						+ counter, Toast.LENGTH_LONG).show();
		counter = null;
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text = getText(R.string.app_name);

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.icon, text,
				System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, Main.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, getText(R.string.app_name), text,
				contentIntent);

		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		nm.notify(R.string.app_name, notification);
	}

	private void incrementCounter() {
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				counter++;
			}
		}, 0, 1000L);
	}

	private void shutdownCounter() {
		if (timer != null) {
			timer.cancel();
		}
	}
}
