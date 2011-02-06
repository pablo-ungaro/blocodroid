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

		Thread t = new Thread(new UpdateManager(this.getContext()) {
			@Override
			public void run() {
				bloco.trocaFavorito(isChecked);
				handler.sendEmptyMessage(0);
			}
		});
		t.start();
	}

}