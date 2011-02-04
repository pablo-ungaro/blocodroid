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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import br.com.jawsys.mobile.blocodroid.R;
import br.com.jawsys.mobile.blocodroid.db.Bloco;

public class ListaBlocosAdapter extends BaseAdapter {

	private List<Bloco> blocos;
	private Context context;

	public ListaBlocosAdapter(Context context, List<Bloco> blocos) {
		super();
		this.blocos = blocos;
		this.context = context;
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
