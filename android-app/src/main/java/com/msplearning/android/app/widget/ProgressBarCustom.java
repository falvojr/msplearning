package com.msplearning.android.app.widget;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msplearning.android.app.R;

/**
 * The ProgressBarCustom class.
 * 
 * @author Venilton Falvo Junior (veniltonjr)
 */
@EViewGroup(R.layout.custom_progress_bar)
public class ProgressBarCustom extends LinearLayout {

	private String statusMessage;

	@ViewById(R.id.status_message)
	protected TextView mStatusMessageView;

	/**
	 * Constructor that sets the statusMessage attribute for lazy (
	 * {@link AfterViews}) attribution.
	 * 
	 * @param context
	 *            {@link Context} object
	 * @param attrs
	 *            {@link AttributeSet} object
	 */
	public ProgressBarCustom(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray customAttrs = null;
		try {
			customAttrs = this.getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBarCustom);
			String statusMessage = customAttrs.getString(R.styleable.ProgressBarCustom_status_message);
			if (statusMessage != null) {
				this.statusMessage = statusMessage;
			}
		} finally {
			if (customAttrs != null) {
				customAttrs.recycle();
			}
		}
	}

	@AfterViews
	public void init() {
		if (this.statusMessage != null) {
			this.mStatusMessageView.setText(this.statusMessage);
		}
	}

	/**
	 * Shows the progress UI and hides the form. On Honeycomb MR2 we have the
	 * ViewPropertyAnimator APIs, which allow for very easy animations. If
	 * available, use these APIs to fade-in the progress spinner else simply
	 * show and hide the relevant UI components.
	 */
	@UiThread
	public void showProgress(final boolean show, final View formView) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = this.getResources().getInteger(android.R.integer.config_shortAnimTime);

			this.setVisibility(View.VISIBLE);
			this.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					ProgressBarCustom.this.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});

			formView.setVisibility(View.VISIBLE);
			formView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					formView.setVisibility(show ? View.GONE : View.VISIBLE);
				}
			});
		} else {
			this.setVisibility(show ? View.VISIBLE : View.GONE);
			formView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
}