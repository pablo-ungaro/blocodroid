package br.com.jawsys.mobile.blocodroid.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

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
import br.com.jawsys.mobile.blocodroid.db.DBAdapter;

public class PorDatasExpandableListAdapter extends BaseExpandableListAdapter {

	private SortedMap<Date, List<Bloco>> listaPorData;
	private List<Date> indices = new ArrayList<Date>();
	private PorDatas mContext;

	public PorDatasExpandableListAdapter(PorDatas porDatas) {
		mContext = porDatas;

		DBAdapter db = new DBAdapter(mContext);
		listaPorData = db.groupByDate();
		indices.addAll(listaPorData.keySet());
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		Date key = indices.get(groupPosition);
		return listaPorData.get(key).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final Bloco bloco = listaPorData.get(indices.get(groupPosition)).get(
				childPosition);

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.child_layout, null);
		}

		TextView nome = (TextView) convertView.findViewById(R.id.nome);
		nome.setText(bloco.getNome());

		TextView endereco = (TextView) convertView.findViewById(R.id.endereco);
		String endereco2 = bloco.getEndereco() + ", "
				+ bloco.getData().getHours() + "hs";
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
		Date key = indices.get(groupPosition);
		return listaPorData.get(key).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		Date key = indices.get(groupPosition);
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

		Date data = indices.get(groupPosition);

		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.group_layout, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.data);
		tv.setText(DBAdapter.formataDataSemHora(data));
		return convertView;
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
