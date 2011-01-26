package br.com.jawsys.blocodroid.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import br.com.jawsys.blocodroid.R;
import br.com.jawsys.blocodroid.db.Bloco;
import br.com.jawsys.blocodroid.db.DBAdapter;

public class PorBlocos extends Activity {

	private ListView lv1;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.porblocos);
		lv1 = (ListView) findViewById(R.id.ListView01);
		DBAdapter db = new DBAdapter(this);
		List<Bloco> blocos = db.listAllBlocos();
		lv1.setAdapter(new ListaBlocosAdapter(this, blocos));
	}

}