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

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;

public class BlocosAgrupadosExpandableListAdapter extends
		BaseExpandableListAdapter {

	private SortedMap<Object, List<Bloco>> listaAgrupada;
	private List<Object> indices = new ArrayList<Object>();
	private Context mContext;

	public BlocosAgrupadosExpandableListAdapter(Context context,
			SortedMap<Object, List<Bloco>> listaAgrupada) {
		this.mContext = context;
		this.listaAgrupada = listaAgrupada;
		this.indices.addAll(listaAgrupada.keySet());
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		Object key = indices.get(groupPosition);
		return listaAgrupada.get(key).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final Bloco bloco = listaAgrupada.get(indices.get(groupPosition)).get(
				childPosition);

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.filho, null);
		}

		FavoritoCheckBox checkFavorito = (FavoritoCheckBox) convertView
				.findViewById(R.id.checkFavorito);
		checkFavorito.setBloco(bloco);

		TextView nome = (TextView) convertView.findViewById(R.id.nome);
		nome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mostraBloco = new Intent(v.getContext(),
						MostraBloco.class);
				String nome = (String) ((TextView) v).getText();
				mostraBloco.putExtra("nome", nome);
				v.getContext().startActivity(mostraBloco);
			}
		});
		nome.setText(bloco.getNome());

		TextView endereco = (TextView) convertView.findViewById(R.id.endereco);
		String endereco2 = bloco.getEndereco();
		endereco.setText(endereco2);

		Button button = (Button) convertView.findViewById(R.id.mostrarMapa);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String endereco = bloco.getEndereco();
				String enderecoFull = endereco;
				String bairro = bloco.getBairro();
				if (endereco.indexOf(bairro) == -1) {
					enderecoFull += ", " + bairro;
				}
				enderecoFull += ", Rio de Janeiro";

				Intent verMapa = new Intent(Intent.ACTION_VIEW, Uri
						.parse("geo:0,0?q=" + enderecoFull));
				mContext.startActivity(verMapa);
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		Object key = indices.get(groupPosition);
		return listaAgrupada.get(key).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		Object key = indices.get(groupPosition);
		return key;
	}

	@Override
	public int getGroupCount() {
		return indices.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		Object key = indices.get(groupPosition);

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.grupo, null);
		}

		TextView tv = (TextView) convertView.findViewById(R.id.titulo);
		tv.setText(formataChaveGrupo(key));
		return convertView;
	}

	protected CharSequence formataChaveGrupo(Object key) {
		return key.toString();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
