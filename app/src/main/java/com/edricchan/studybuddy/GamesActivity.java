package com.edricchan.studybuddy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class GamesActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_games);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		RecyclerView recyclerView = findViewById(R.id.games_recycler_view);

	}
}
