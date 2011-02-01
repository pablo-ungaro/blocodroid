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
