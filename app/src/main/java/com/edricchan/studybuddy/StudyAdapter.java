package com.edricchan.studybuddy;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


/***
 * Part of code came from the following sources:
 * <a href="https://guides.codepath.com/android/using-the-recyclerview">Using the recyclerview</a>
 *
 */
public class StudyAdapter extends RecyclerView.Adapter<StudyAdapter.Holder> {
	private List<TaskItem> mFeedItemList;
	private Context mContext;
	private FirebaseUser mUser;
	private FirebaseFirestore mFirestore;
	private View snackbarView;

	public StudyAdapter(Context context, List<TaskItem> feedItemList, FirebaseUser user, FirebaseFirestore fs, View view) {
		mFeedItemList = feedItemList;
		mContext = context;
		mUser = user;
		mFirestore = fs;
		snackbarView = view;
	}

	private Context getContext() {
		return mContext;
	}

	private boolean checkNonEmpty(@NonNull String var) {
		return var != null && var.length() != 0;
	}

	private boolean checkNonEmpty(List var) {
		return var != null && var.size() != 0;
	}

	private void updateDoneStatus(@NonNull TaskItem item, final MaterialButton button) {
		if (item.hasDone) {
			mFirestore.document("users/" + mUser.getUid() + "/todos/" + item.id).update("hasDone", false).addOnSuccessListener(new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void aVoid) {
					Snackbar.make(snackbarView, "Task marked as undone.", Snackbar.LENGTH_LONG).show();
					button.setText(R.string.action_mark_as_done);
				}
			});
		} else {
			mFirestore.document("users/" + mUser.getUid() + "/todos/" + item.id).update("hasDone", true).addOnSuccessListener(new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void aVoid) {
					Snackbar.make(snackbarView, "Task marked as done.", Snackbar.LENGTH_LONG).show();
					button.setText(R.string.action_mark_as_undone);
				}
			});
		}
	}

	@Override
	@NonNull
	public StudyAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View itemView = inflater.inflate(R.layout.recyclerview_item_row, parent, false);

		return new Holder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull StudyAdapter.Holder holder, final int position) {
		final TaskItem item = mFeedItemList.get(position);
		TextView itemTitle = holder.itemTitle;
		TextView itemDate = holder.itemDate;
		TextView itemContent = holder.itemContent;
		ChipGroup itemProjects = holder.itemProjects;
		ChipGroup itemTags = holder.itemTags;
		if (checkNonEmpty(item.title)) {
			itemTitle.setText(item.title);
		}
		if (item.dueDate != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
			String date = dateFormat.format(item.dueDate.toDate().getTime());
			itemDate.setText(date);
		}
		if (checkNonEmpty(item.content)) {
			itemContent.setText(item.content);
		}
		if (checkNonEmpty(item.projects)) {
			for (String project : item.projects) {
				Chip tempChip = new Chip(mContext);
				tempChip.setChipText(project);
				itemProjects.addView(tempChip);
			}
		}
		if (checkNonEmpty(item.tags)) {
			for (String tag : item.tags) {
				Chip tempChip = new Chip(mContext);
				tempChip.setChipText(tag);
				itemTags.addView(tempChip);
			}
		}
		final MaterialButton markAsDoneBtn = holder.markAsDoneBtn;
		markAsDoneBtn.setText(R.string.action_mark_as_done);
		markAsDoneBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				updateDoneStatus(item, markAsDoneBtn);
			}
		});
		MaterialButton deleteBtn = holder.deleteBtn;
		deleteBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				TaskItem temp = item;
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Delete todo?");
				builder.setPositiveButton(R.string.dialog_action_ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
						SharedHelper.removeTodo(item.id, mUser, mFirestore).addOnSuccessListener(new OnSuccessListener<Void>() {
							@Override
							public void onSuccess(Void aVoid) {
								notifyItemRemoved(position);
								Snackbar.make(snackbarView, "Todo was deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
									@Override
									public void onClick(View view1) {
										SharedHelper.addTodo(item, mUser, mFirestore).addOnFailureListener(new OnFailureListener() {
											@Override
											public void onFailure(@NonNull Exception e) {
												Snackbar.make(snackbarView, "Couldn't restore todo: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
											}
										});
									}
								}).show();
							}
						});
					}
				});
				builder.setNegativeButton(R.string.dialog_action_cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						dialogInterface.dismiss();
					}
				});
				builder.show();
			}
		});
	}

	@Override
	public int getItemCount() {
		return mFeedItemList.size();
	}

	public class Holder extends RecyclerView.ViewHolder {
		TextView itemTitle;
		TextView itemDate;
		TextView itemContent;
		ChipGroup itemProjects;
		ChipGroup itemTags;
		MaterialButton markAsDoneBtn;
		MaterialButton deleteBtn;

		public Holder(View view) {
			super(view);
			markAsDoneBtn = (MaterialButton) view.findViewById(R.id.itemMarkAsDone);
			deleteBtn = (MaterialButton) view.findViewById(R.id.itemDelete);
			itemTitle = (TextView) view.findViewById(R.id.itemTitle);
			itemDate = (TextView) view.findViewById(R.id.itemDate);
			itemContent = (TextView) view.findViewById(R.id.itemContent);
			itemProjects = (ChipGroup) view.findViewById(R.id.itemProjects);
			itemTags = (ChipGroup) view.findViewById(R.id.itemTags);
		}
	}
}