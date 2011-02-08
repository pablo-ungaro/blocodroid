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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

public class MostraBloco extends Activity implements OnClickListener {

	private String bairro;
	private String endereco;
	private String nome;
	private DBAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.mostrabloco);

		Bundle extras = getIntent().getExtras();
		nome = extras.getString("nome");
		dbAdapter = new DBAdapter(this);
		final List<Bloco> blocos = dbAdapter.findByNome(nome);

		TextView nomeBloco = (TextView) findViewById(R.id.nomeBloco);
		nomeBloco.setText(nome);

		Button mostraMapa = (Button) findViewById(R.id.mostrarMapa);
		mostraMapa.setOnClickListener(this);

		boolean hidemap = extras.getBoolean("hidemap", false);
		mostraMapa.setVisibility(hidemap ? View.GONE : View.VISIBLE);

		endereco = blocos.get(0).getEndereco();
		bairro = blocos.get(0).getBairro();

		ListView lv = (ListView) findViewById(R.id.listaDatasHorarios);
		lv.setAdapter(new BaseAdapter() {
			@Override
			public int getCount() {
				return blocos.size();
			}

			@Override
			public Object getItem(int position) {
				return blocos.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Bloco bloco = blocos.get(position);

				ViewHolder holder;

				if (convertView == null) {
					convertView = View.inflate(MostraBloco.this,
							R.layout.mostrablocoitem, null);

					holder = new ViewHolder();
					holder.endereco = (TextView) convertView
							.findViewById(R.id.endereco);
					holder.data = (TextView) convertView
							.findViewById(R.id.data);

					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}

				holder.endereco.setText(bloco.getEndereco() + ", " + bairro);
				holder.data.setText(DBAdapter.formataData(bloco.getData()));

				return convertView;
			}
		});
	}

	static class ViewHolder {
		protected TextView data;
		protected TextView endereco;
	}

	@Override
	public void onClick(View v) {
		String enderecoFull = endereco;
		if (endereco.indexOf(bairro) == -1) {
			enderecoFull += ", " + bairro;
		}
		enderecoFull += ", Rio de Janeiro";

		Intent verMapa = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="
				+ enderecoFull));
		startActivity(verMapa);
	}

}
