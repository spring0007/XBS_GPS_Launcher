package com.sczn.wearlauncher.status.view;
import com.sczn.wearlauncher.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;



public class ShutDown extends StatusIconWithText {

	public ShutDown(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		setOnClickListener(onShutDownIcon);
	}
	private OnClickListener onShutDownIcon = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			// TODO Auto-generated method stu
			final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
			builder.setTitle(R.string.exit_dialog_title);
			builder.setMessage(R.string.exit_dialog_msg);
			builder.setCancelable(false);
			builder.setNegativeButton(R.string.exit_dialog_cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.setPositiveButton(R.string.exit_dialog_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					v.getContext().sendBroadcast(new Intent("com.werable.shutdown"));
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	};

}

