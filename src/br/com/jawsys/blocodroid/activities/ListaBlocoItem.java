package br.com.jawsys.blocodroid.activities;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.jawsys.blocodroid.R;
import br.com.jawsys.blocodroid.db.Bloco;

public class ListaBlocoItem extends LinearLayout {

	private Bloco bloco;
	private CheckedTextView checkBloco;
	private SimpleDateFormat formatter = new SimpleDateFormat(
			"dd/MM EEEE HH:mm", new Locale("pt", "BR"));
	private TextView textoBloco;

	public ListaBlocoItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setBloco(Bloco bloco) {
		this.bloco = bloco;
		checkBloco.setText(bloco.getNome());
		checkBloco.setChecked(bloco.isFavorito());
		checkBloco.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ListaBlocoItem.this.bloco.trocaFavorito();
				checkBloco.setChecked(ListaBlocoItem.this.bloco.isFavorito());
			}

		});

		textoBloco.setText(bloco.getBairro() + ", "
				+ formatter.format(bloco.getData()) + "\n"
				+ bloco.getEndereco());
	}

	public Bloco getBloco() {
		return bloco;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		checkBloco = (CheckedTextView) findViewById(R.id.checkBloco);
		textoBloco = (TextView) findViewById(R.id.textoBloco);
	}

}
