package br.com.jawsys.blocodroid.activities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ListaBlocos extends ListView {

	public ListaBlocos(Context context) {
		super(context);
		setLayout();
	}

	private void setLayout() {
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
	}

	public ListaBlocos(Context context, AttributeSet attrs) {
		super(context, attrs);
		setLayout();
	}

	public ListaBlocos(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setLayout();
	}

}
