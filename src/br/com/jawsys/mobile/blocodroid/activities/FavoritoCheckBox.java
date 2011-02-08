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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import br.com.jawsys.mobile.blocodroid.db.Bloco;

public class FavoritoCheckBox extends CheckBox implements
		OnCheckedChangeListener {

	private Bloco bloco;
	private ProgressDialog pd;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();
		}
	};

	public FavoritoCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs, android.R.attr.starStyle);
		setOnCheckedChangeListener(this);
	}

	public void setBloco(Bloco bloco) {
		this.bloco = bloco;
		setChecked(bloco.isFavorito());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView,
			final boolean isChecked) {
		if (bloco == null || isChecked == bloco.isFavorito()) {
			return;
		}

		pd = ProgressDialog.show(this.getContext(), "",
				"Marcando como favorito. Aguarde...", true);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				bloco.trocaFavorito(isChecked);
				handler.sendEmptyMessage(0);
			}
		});
		t.start();
	}

}