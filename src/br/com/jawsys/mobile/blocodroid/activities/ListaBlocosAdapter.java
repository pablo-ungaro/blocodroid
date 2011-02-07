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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;

public class ListaBlocosAdapter extends BaseAdapter implements Filterable {

	private List<Bloco> blocos;
	private List<Bloco> blocosFiltrados;
	private Context context;
	private BlocosFilter filter;

	public ListaBlocosAdapter(Context context, List<Bloco> blocos) {
		super();
		this.blocos = blocos;
		this.blocosFiltrados = blocos;
		this.context = context;
	}

	@Override
	public int getCount() {
		return blocosFiltrados.size();
	}

	@Override
	public Bloco getItem(int position) {
		return (null == blocosFiltrados) ? null : blocosFiltrados.get(position);
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
		lbi.setBloco(blocosFiltrados.get(position));
		return lbi;
	}

	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new BlocosFilter();
		}

		return filter;
	}

	private class BlocosFilter extends Filter {
		private Object lock = new Object();

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				synchronized (lock) {
					blocosFiltrados = blocos;
					results.values = blocosFiltrados;
					results.count = blocosFiltrados.size();
				}
			} else {
				List<Bloco> newItems = new ArrayList<Bloco>();
				for (int i = 0; i <= blocosFiltrados.size() - 1; i++) {
					Bloco item = blocosFiltrados.get(i);
					if (item.getNome().toLowerCase()
							.contains(constraint.toString().toLowerCase())) {
						newItems.add(item);
					}
				}

				results.values = newItems;
				results.count = newItems.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			blocosFiltrados = (List<Bloco>) results.values;

			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

}
