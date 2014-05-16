package com.msplearning.android.app;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import android.view.View;
import android.widget.Button;

import com.msplearning.android.app.generic.GenericAsyncAuthActivity;
import com.msplearning.android.app.rest.AppUserRestClient;
import com.msplearning.entity.AppUser;
import com.msplearning.entity.AppUserId;
import com.msplearning.entity.Teacher;

/**
 * The DashboardActivity class.
 *
 * @author Venilton Falvo Junior (veniltonjr)
 */
@EActivity(R.layout.activity_dashboard)
public class DashboardActivity extends GenericAsyncAuthActivity<MSPLearningApplication> {

	@ViewById(R.id.button_manage_view_educational_content)
	protected Button mButtonManageViewEducationalContent;
	@ViewById(R.id.button_access_requests)
	protected Button mButtonAccessRequests;
	@ViewById(R.id.button_edit_profile)
	protected Button mButtonEditProfile;
	@ViewById(R.id.button_info)
	protected Button mButtonInfo;

	@RestService
	protected AppUserRestClient mAppUserRestClient;

	@AfterViews
	protected void init() {
		if (super.getUser() == null) {
			this.resolveContentManagementCommonality(null);
		} else {
			this.findAppUserRelationship();
		}
	}

	@Background
	protected void findAppUserRelationship() {
		AppUserId appUserId = new AppUserId(super.getApp().getId(), super.getUser().getId());
		AppUser appUser = this.mAppUserRestClient.findById(appUserId).getEntity();
		if (appUser == null) {
			appUser = new AppUser();
			appUser.setId(appUserId);
			appUser = this.mAppUserRestClient.insert(appUser).getEntity();
		}
		this.resolveContentManagementCommonality(appUser);
	}

	@UiThread
	protected void resolveContentManagementCommonality(AppUser appUser) {
		if (appUser == null) {
			this.mButtonManageViewEducationalContent.setText(this.getString(R.string.action_view_educational_content));
		} else {
			boolean isTeacherOrAdmin = (super.getUser() instanceof Teacher) || appUser.isAdmin();

			this.mButtonManageViewEducationalContent.setText(this.getString(isTeacherOrAdmin ? R.string.action_manage_educational_content : R.string.action_view_educational_content));
			this.mButtonManageViewEducationalContent.setEnabled(appUser.isActive());
			if (appUser.isAdmin()) {
				this.mButtonAccessRequests.setVisibility(View.VISIBLE);
			}
		}
	}

	@Click(R.id.button_manage_view_educational_content)
	protected void onManageViewEducationalContent() {
		DisciplineListActivity_.intent(this).start();
		this.finish();
	}

	@Click(R.id.button_access_requests)
	protected void onAccessRequests() {
		AccessRequestsActivity_.intent(this).start();
	}

	@Click(R.id.button_edit_profile)
	protected void onEditProfile() {
		//TODO: Edit Profile Functionality.
	}

	@Click(R.id.button_info)
	protected void onInfo() {
		//TODO: Info Functionality.
	}
}
