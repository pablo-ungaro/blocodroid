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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import br.com.jawsys.mobile.blocodroid.R;

public class PorDatas extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				final Writer result = new StringWriter();
				final PrintWriter printWriter = new PrintWriter(result);
				e.printStackTrace(printWriter);
				String stacktrace = result.toString();
				printWriter.close();
				String filename = "stacktrace";
				writeToFile(stacktrace, filename);
			}

			private void writeToFile(String stacktrace, String filename) {
				try {
					new File("/sdcard/blocodroid/").mkdir();
					BufferedWriter bos = new BufferedWriter(new FileWriter(
							"/sdcard/blocodroid/" + filename));
					bos.write(stacktrace);
					bos.flush();
					bos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		setContentView(R.layout.pordatas);

		ExpandableListView epView = (ExpandableListView) findViewById(R.id.porDatasListView);

		ExpandableListAdapter mAdapter;
		mAdapter = new PorDatasExpandableListAdapter(this);

		epView.setAdapter(mAdapter);
	}
}
