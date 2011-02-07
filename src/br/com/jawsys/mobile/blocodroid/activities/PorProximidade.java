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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class PorProximidade extends MapActivity {

	private MapController mc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.porproximidade);

		MapView map = (MapView) PorProximidade.this.findViewById(R.id.mapa);
		map.setBuiltInZoomControls(true);
		mc = map.getController();

		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		Location lastKnownLocation = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		GeoPoint geoPointCenter = new GeoPoint(
				(int) (lastKnownLocation.getLatitude() * 1E6),
				(int) (lastKnownLocation.getLongitude() * 1E6));

		Drawable marcador = getResources().getDrawable(R.drawable.blue);
		BlocoOverlay overlay = new BlocoOverlay(this, marcador);
		overlay.addPosicaoAtual(geoPointCenter);
		List<Bloco> blocosHoje = new DBAdapter(this).listarBlocosHoje();
		for (Bloco bloco : blocosHoje) {
			overlay.add(bloco);
		}
		map.getOverlays().add(overlay);
		mc.zoomToSpan(overlay.getLatSpanE6(), overlay.getLonSpanE6());
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
