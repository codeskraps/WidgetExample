package com.codeskraps;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ClickedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.clicked);
		
		Toast.makeText(getApplicationContext(), "We are in ClickActivity",
		Toast.LENGTH_SHORT).show();

	}
}
