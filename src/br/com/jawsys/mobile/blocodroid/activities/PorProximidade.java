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

import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class PorProximidade extends MapActivity {

	private static GeoPoint GP_RIO = new GeoPoint((int) (-22.9021 * 1E6),
			(int) (-43.2223 * 1E6));

	private MapController mc;
	private MapView map;
	private MyLocationOverlay myLocationOverlay;
	private BlocoOverlay blocosOverlay;
	private List<Bloco> blocosHoje;
	protected ProgressDialog pd;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.porproximidade);
		getWindow().setFormat(PixelFormat.TRANSPARENT);

		map = (MapView) findViewById(R.id.mapa);
		map.setBuiltInZoomControls(true);
		map.setClickable(true);
		map.displayZoomControls(true);
		map.setDrawingCacheEnabled(true);

		List<Overlay> overlays = map.getOverlays();
		myLocationOverlay = new MyLocationOverlay(this, map);
		overlays.add(myLocationOverlay);

		mc = map.getController();
		mc.setZoom(13);
		mc.setCenter(GP_RIO);

		@SuppressWarnings("rawtypes")
		AsyncTask task = new AsyncTask() {
			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(PorProximidade.this);
				pd.setTitle(R.string.app_name);
				pd.setIcon(R.drawable.creep003);
				pd.setMessage("Procurando ...");

				showDialog(1);
				super.onPreExecute();
			}

			protected Object doInBackground(Object... params) {
				listaPosicaoBlocos();
				return null;
			}

			@Override
			protected void onPostExecute(Object result) {
				mostraRadar();
				handler.sendEmptyMessage(0);
			}
		};
		task.execute();
	}

	protected void mostraRadar() {
		List<Overlay> overlays = map.getOverlays();
		overlays.add(blocosOverlay);
		//mc.animateTo(blocosOverlay.getCenter());
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 1) {
			return pd;
		}

		return null;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (pd != null && pd.isShowing()) {
				try {
					pd.dismiss();
				} catch (Exception e) {
				}
			}
		}
	};

	private void listaPosicaoBlocos() {
		DBAdapter dbAdapter = new DBAdapter(this);
		blocosHoje = dbAdapter.listarBlocosHoje();

		if (!blocosHoje.isEmpty()) {
			Drawable marcador = getResources().getDrawable(R.drawable.blue);
			blocosOverlay = new BlocoOverlay(this, dbAdapter, marcador);
			for (Bloco bloco : blocosHoje) {
				blocosOverlay.add(bloco);
			}
		}

		if (blocosHoje.isEmpty()) {
			myLocationOverlay.runOnFirstFix(new Runnable() {
				public void run() {
					mc.animateTo(myLocationOverlay.getMyLocation());
				};
			});
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		myLocationOverlay.disableMyLocation();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
