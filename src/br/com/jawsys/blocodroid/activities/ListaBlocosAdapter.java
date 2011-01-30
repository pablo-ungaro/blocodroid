package br.com.jawsys.blocodroid.activities;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import br.com.jawsys.blocodroid.R;
import br.com.jawsys.blocodroid.db.Bloco;

public class ListaBlocosAdapter extends BaseAdapter {

	private List<Bloco> blocos;
	private Context context;

	public ListaBlocosAdapter(Context context, List<Bloco> blocos) {
		super();
		this.blocos = blocos;
		this.context = context;
	}

	public void setListaBlocos(List<Bloco> lista) {
		this.blocos = lista;
	}

	@Override
	public int getCount() {
		return blocos.size();
	}

	@Override
	public Bloco getItem(int position) {
		return (null == blocos) ? null : blocos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ListaBlocoItem lbi = null;
		if (null == convertView) {
			lbi = (ListaBlocoItem) View.inflate(context, R.layout.itembloco,
					null);
		} else {
			lbi = (ListaBlocoItem) convertView;
		}
		lbi.setBloco(blocos.get(position));
		return lbi;
	}

}
