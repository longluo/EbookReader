package com.longluo.android.ebookreader.network.litres;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.longluo.zlibrary.core.network.ZLNetworkManager;
import com.longluo.zlibrary.core.network.ZLNetworkException;
import com.longluo.zlibrary.core.resources.ZLResource;
import com.longluo.ebookreader.R;

public class UserRegistrationActivity extends RegistrationActivity {
	private TextView findTextView(int resourceId) {
		return (TextView)findViewById(resourceId);
	}

	private Button findButton(int resourceId) {
		return (Button)findViewById(resourceId);
	}

	private String getViewText(int resourceId) {
		return findTextView(resourceId).getText().toString().trim();
	}

	private void setViewText(int resourceId, String text) {
		findTextView(resourceId).setText(text);
	}

	private void setViewTextFromResource(int resourceId, String fbResourceKey) {
		setViewText(resourceId, myResource.getResource(fbResourceKey).getValue());
	}

	private void setErrorMessage(String errorMessage) {
		final TextView errorLabel = findTextView(R.id.lr_user_registration_error);
		errorLabel.setVisibility(View.VISIBLE);
		errorLabel.setText(errorMessage);
	}

	private void setErrorMessageFromResource(String resourceKey) {
		setErrorMessage(ZLResource.resource("dialog").getResource("networkError").getResource(resourceKey).getValue());
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		myResource = ZLResource.resource("dialog").getResource("litresUserRegistration");

		//Thread.setDefaultUncaughtExceptionHandler(new com.longluo.zlibrary.ui.android.library.UncaughtExceptionHandler(this));

		setContentView(R.layout.lr_user_registration);

		setTitle(myResource.getResource("title").getValue());
		setViewTextFromResource(R.id.lr_user_registration_login_text, "login");
		setViewTextFromResource(R.id.lr_user_registration_password_text, "password");
		setViewTextFromResource(R.id.lr_user_registration_confirm_password_text, "confirmPassword");
		setViewTextFromResource(R.id.lr_user_registration_email_text, "email");

		final TextView errorLabel = findTextView(R.id.lr_user_registration_error);
		errorLabel.setVisibility(View.GONE);
		errorLabel.setText("");

		final ZLResource buttonResource = ZLResource.resource("dialog").getResource("button");
		final View buttonsView = findViewById(R.id.lr_user_registration_buttons);
		final Button okButton = (Button)buttonsView.findViewById(R.id.ok_button);
		final Button cancelButton = (Button)buttonsView.findViewById(R.id.cancel_button);
		final View emailControl = findViewById(R.id.lr_user_registration_email_control);
		final TextView emailTextView = (TextView)emailControl.findViewById(R.id.lr_email_edit);

		okButton.setText(buttonResource.getResource("ok").getValue());
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String userName = getViewText(R.id.lr_user_registration_login);
				final String password = getViewText(R.id.lr_user_registration_password);
				final String confirmPassword = getViewText(R.id.lr_user_registration_confirm_password);
				final String email = emailTextView.getText().toString().trim();

				if (userName.length() == 0) {
					setErrorMessageFromResource("usernameNotSpecified");
					return;
				}
				if (!password.equals(confirmPassword)) {
					setErrorMessageFromResource("passwordsDoNotMatch");
					return;
				}
				if (password.length() == 0) {
					setErrorMessageFromResource("passwordNotSpecified");
					return;
				}
				if (email.length() == 0) {
					setErrorMessageFromResource("emailNotSpecified");
					return;
				}
				final int atPos = email.indexOf("@");
				if (atPos == -1 || email.indexOf(".", atPos) == -1) {
					setErrorMessageFromResource("invalidEMail");
					return;
				}

				final RegistrationNetworkRunnable runnable =
					new RegistrationNetworkRunnable(userName, password, email);
				final PostRunnable postRunnable = new PostRunnable() {
					public void run(ZLNetworkException exception) {
						if (exception == null) {
							reportSuccess(userName, password, runnable.XmlReader.Sid);
							finish();
						} else {
							setErrorMessage(exception.getMessage());
						}
					}
				};
				runWithMessage("registerUser", runnable, postRunnable);
			}
		});
		cancelButton.setText(buttonResource.getResource("cancel").getValue());
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		setupEmailControl(emailControl, null);
	}
}
