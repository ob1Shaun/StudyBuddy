package com.edricchan.studybuddy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edricchan.studybuddy.R;
import com.edricchan.studybuddy.SharedHelper;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CalendarFragment extends Fragment {
	private View fragmentView;
	/**
	 * The Android tag for use with {@link android.util.Log}
	 */
	private static final String TAG = SharedHelper.getTag(CalendarFragment.class);
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_calendar, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		fragmentView = view;
	}
}
