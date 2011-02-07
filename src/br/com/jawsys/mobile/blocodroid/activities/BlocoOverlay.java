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
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class BlocoOverlay extends ItemizedOverlay<OverlayItem> {

	private static final Locale pt_BR = new Locale("pt", "BR");
	private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context context;

	public BlocoOverlay(Context context, Drawable badge) {
		super(boundCenter(badge));
		this.context = context;
	}

	public void addPosicaoAtual(GeoPoint geoPoint) {
		OverlayItem overlayItem = new OverlayItem(geoPoint, "Posição atual",
				"Posição atual");
		mOverlays.add(overlayItem);
		populate();
	}

	public GeoPoint add(Bloco bloco) {
		Geocoder geoCoder = new Geocoder(context, pt_BR);

		GeoPoint p = null;

		try {
			String endereco = bloco.getEndereco() + ", " + bloco.getBairro()
					+ ", Rio de Janeiro, Rio de Janeiro, Brazil";

			List<Address> addresses = geoCoder.getFromLocationName(endereco, 5);
			if (addresses.size() > 0) {
				p = new GeoPoint((int) (addresses.get(0).getLatitude() * 1E6),
						(int) (addresses.get(0).getLongitude() * 1E6));
				BlocoItem bi = new BlocoItem(bloco, p, context.getResources()
						.getDrawable(R.drawable.red));
				mOverlays.add(bi);
				populate();
				return p;
			}
		} catch (IOException e) {
		}

		return p;
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
			Toast.makeText(context, "Você está aqui!", Toast.LENGTH_LONG);
		}

		return super.onTap(index);
	}

	public static class BlocoItem extends OverlayItem {
		private Drawable icone;

		public BlocoItem(Bloco bloco, GeoPoint ponto, Drawable icone) {
			super(ponto, bloco.getNome(), bloco.getData().getHours() + "hs");
			this.icone = icone;
		}

		@Override
		public Drawable getMarker(int stateBitset) {
			return icone;
		}
	}

}
