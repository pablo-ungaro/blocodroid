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
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;

public class ListaBlocoItem extends LinearLayout implements OnClickListener,
		OnCheckedChangeListener {

	private Bloco bloco;
	private CheckBox checkBloco;
	private TextView textoBloco;
	private ProgressDialog pd;

	public ListaBlocoItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setBloco(final Bloco bloco) {
		this.bloco = bloco;
		checkBloco.setChecked(bloco.isFavorito());
		checkBloco.setOnCheckedChangeListener(this);

		textoBloco.setText(bloco.getNome());
		textoBloco.setOnClickListener(this);
	}

	public Bloco getBloco() {
		return bloco;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView,
			final boolean isChecked) {
		if (getBloco().isFavorito() == isChecked) {
			return;
		}

		pd = ProgressDialog.show(this.getContext(), "",
				"Marcando como favorito. Aguarde...", true);

		Thread t = new Thread(new UpdateManager(this.getContext()) {
			@Override
			public void run() {
				getBloco().trocaFavorito(isChecked);
				handler.sendEmptyMessage(0);
			}
		});
		t.start();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		checkBloco = (CheckBox) findViewById(R.id.checkBloco);
		textoBloco = (TextView) findViewById(R.id.textoBloco);
	}

	public void onClick(View v) {
		Intent mostraBloco = new Intent(getContext(), MostraBloco.class);
		String nome = (String) ((TextView) v).getText();
		mostraBloco.putExtra("nome", nome);
		getContext().startActivity(mostraBloco);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			pd.dismiss();
		}
	};
}
