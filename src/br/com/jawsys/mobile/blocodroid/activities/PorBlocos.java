package br.com.jawsys.mobile.blocodroid.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

public class PorBlocos extends Activity {

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.porblocos);

		boolean favoritos = getIntent().getExtras().getBoolean("favoritos");

		DBAdapter db = new DBAdapter(this);
		List<Bloco> blocos = favoritos ? db.listBlocosFavoritos() : db.listPorBlocos();

		ListaBlocos lv1 = (ListaBlocos) findViewById(R.id.listaBlocos);
		lv1.setAdapter(new ListaBlocosAdapter(this, blocos));
	}

}
