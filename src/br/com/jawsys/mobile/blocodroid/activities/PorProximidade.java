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

import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class PorProximidade extends MapActivity {

	private MapController mc;
	private MapView map;
	private MyLocationOverlay myLocationOverlay;
	private BlocoOverlay blocosOverlay;
	private List<Bloco> blocosHoje;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFormat(PixelFormat.TRANSPARENT);

		setContentView(R.layout.porproximidade);

		map = (MapView) findViewById(R.id.mapa);
		map.setBuiltInZoomControls(true);
		map.setClickable(true);
		map.displayZoomControls(true);

		mc = map.getController();
		mc.setZoom(12);

		List<Overlay> overlays = map.getOverlays();

		DBAdapter dbAdapter = new DBAdapter(this);
		blocosHoje = dbAdapter.listarBlocosHoje();
		if (!blocosHoje.isEmpty()) {
			Drawable marcador = getResources().getDrawable(R.drawable.blue);
			blocosOverlay = new BlocoOverlay(this, marcador);
			for (Bloco bloco : blocosHoje) {
				blocosOverlay.add(bloco);
			}

			overlays.add(blocosOverlay);
			mc.animateTo(blocosOverlay.getCenter());
		}

		myLocationOverlay = new MyLocationOverlay(this, map);
		overlays.add(myLocationOverlay);

		if (blocosHoje.isEmpty()) {
			myLocationOverlay.runOnFirstFix(new Runnable() {
				public void run() {
					mc.animateTo(myLocationOverlay.getMyLocation());
				};
			});
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
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
