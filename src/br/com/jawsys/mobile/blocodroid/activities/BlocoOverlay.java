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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;
import br.com.jawsys.mobile.blocodroid.db.Bloco;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class BlocoOverlay extends ItemizedOverlay<OverlayItem> {

	private static final Locale pt_BR = new Locale("pt", "BR");
	private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context context;

	private int maxLatitude = Integer.MIN_VALUE;
	private int minLatitude = Integer.MAX_VALUE;
	private int maxLongitude = Integer.MIN_VALUE;
	private int minLongitude = Integer.MAX_VALUE;

	private int centerLongitude;
	private int centerLatitude;

	public BlocoOverlay(Context context, Drawable marker) {
		super(boundCenterBottom(marker));
		this.context = context;
	}

	public void add(Bloco bloco) {
		Geocoder geoCoder = new Geocoder(context, pt_BR);

		try {
			String endereco = bloco.getEndereco() + ", " + bloco.getBairro()
					+ ", Rio de Janeiro, Rio de Janeiro, Brazil";

			List<Address> addresses = geoCoder.getFromLocationName(endereco, 5);
			if (addresses.size() > 0) {
				int latitude = (int) (addresses.get(0).getLatitude() * 1E6);
				int longitude = (int) (addresses.get(0).getLongitude() * 1E6);

				computeCenterPosition(latitude, longitude);

				GeoPoint p = new GeoPoint(latitude, longitude);
				BlocoItem bi = new BlocoItem(bloco, p);
				mOverlays.add(bi);
				populate();
			}
		} catch (IOException e) {
		}
	}

	private void computeCenterPosition(int latitude, int longitude) {
		maxLatitude = Math.max(latitude, maxLatitude);
		minLatitude = Math.min(latitude, minLatitude);

		maxLongitude = Math.max(longitude, maxLongitude);
		minLongitude = Math.min(longitude, minLongitude);

		centerLongitude = ((maxLongitude - minLongitude) / 2) + minLongitude;
		centerLatitude = ((maxLatitude - minLatitude) / 2) + minLatitude;
	}

	@Override
	public GeoPoint getCenter() {
		return new GeoPoint(centerLatitude, centerLongitude);
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		OverlayItem blocoItem = mOverlays.get(index);

		if (blocoItem instanceof BlocoItem) {
			Intent mostraBloco = new Intent(context, MostraBloco.class);
			mostraBloco.putExtra("nome", blocoItem.getTitle());
			mostraBloco.putExtra("hidemap", true);
			context.startActivity(mostraBloco);
		} else {
			Toast.makeText(context, "Você está aqui!", Toast.LENGTH_LONG)
					.show();
		}

		return super.onTap(index);
	}

	public static class BlocoItem extends OverlayItem {
		public BlocoItem(Bloco bloco, GeoPoint ponto) {
			super(ponto, bloco.getNome(), bloco.getData().getHours() + "hs");
		}

	}

}
