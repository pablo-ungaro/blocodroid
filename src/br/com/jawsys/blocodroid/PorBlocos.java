package br.com.jawsys.blocodroid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class PorBlocos extends Activity {

	private DBAdapter db = null;

	private List<Bloco> blocos = null;

	private ProgressDialog dialogProgresso;

	private Runnable returnResBlocos = new Runnable() {

		@Override
		public void run() {
			if (blocos != null && blocos.size() > 0) {
				blocosAdapter.notifyDataSetChanged();
				for (int i = 0; i < blocos.size(); i++)
					blocosAdapter.add(blocos.get(i));
			}
			dialogProgresso.dismiss();
			blocosAdapter.notifyDataSetChanged();
		}
	};

	private BlocosAdapter blocosAdapter;

	public PorBlocos() {
		setContentView(R.layout.porblocos);

		db = new DBAdapter(this);
		blocos = new ArrayList<Bloco>();

		blocosAdapter = new BlocosAdapter(getApplicationContext(),
				R.layout.simpletv, blocos);

		ListView myListView = (ListView) findViewById(R.id.listBlocos);
		myListView.setFastScrollEnabled(true);
		myListView.setAdapter(blocosAdapter);

		listarPorBloco();
	}

	private void listarPorBloco() {
		Runnable listaBlocos = new Runnable() {
			@Override
			public void run() {
				db.open();
				PorBlocos.this.blocos = db.listAllBlocos();
				db.close();
				runOnUiThread(returnResBlocos);
			}
		};
		Thread thread = new Thread(null, listaBlocos, "MagentoBackground");
		thread.start();
		dialogProgresso = ProgressDialog.show(PorBlocos.this,
				"Por favor aguarde...", "Filtrando lista...", true);
	}

	class BlocosAdapter extends ArrayAdapter<Bloco> implements SectionIndexer {

		List<Bloco> myElements;
		HashMap<String, Integer> alphaIndexer;

		String[] sections;

		public BlocosAdapter(Context context, int textViewResourceId,
				List<Bloco> objects) {
			super(context, textViewResourceId, objects);
			myElements = objects;

			// here is the tricky stuff
			alphaIndexer = new HashMap<String, Integer>();
			// in this hashmap we will store here the positions for
			// the sections

			int i = 0;
			for (Bloco b : myElements) {
				alphaIndexer.put(String.valueOf(Character.toUpperCase(b
						.getNome().charAt(0))), i++);
			}

			Set<String> keys = alphaIndexer.keySet(); // set of letters ...sets
			// cannot be sorted...

			Iterator<String> it = keys.iterator();
			ArrayList<String> keyList = new ArrayList<String>(); // list can be
			// sorted

			while (it.hasNext()) {
				String key = it.next();
				keyList.add(key);
			}

			Collections.sort(keyList);

			sections = new String[keyList.size()]; // simple conversion to an
			// array of object
			keyList.toArray(sections);

			// ooOO00K !
		}

		@Override
		public int getPositionForSection(int section) {
			String letter = sections[section];
			return alphaIndexer.get(letter);
		}

		@Override
		public int getSectionForPosition(int position) {
			return 0;
		}

		@Override
		public Object[] getSections() {
			return sections; // to string will be called each object, to display
			// the letter
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.itembloco, null);
			}

			Bloco b = myElements.get(position);
			if (b != null) {
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if (tt != null) {
					tt.setText(b.getNome());
				}
				if (bt != null) {
					bt.setText("Bairro: " + b.getBairro());
				}
			}
			return v;
		}

	}

}
