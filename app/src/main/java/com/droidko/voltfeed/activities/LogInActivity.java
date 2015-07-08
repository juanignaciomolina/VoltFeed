package com.droidko.voltfeed.activities;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.VoltfeedApp;
import com.droidko.voltfeed.ui.ConnectingDialog;
import com.droidko.voltfeed.utils.UiHelper;
import com.parse.LogInCallback;
import com.parse.ParseUser;


public class LogInActivity extends VoltfeedActivity {

    private EditText mMail;
    private EditText mPassword;
    private Button mLogIn;
    private Button mSignUp;
    private TextView mToS;

    private boolean mActivityIsVisible;

    private ConnectingDialog mConnectingDialogInstance;

    @Override
    protected int layout() {
        return R.layout.activity_log_in;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //This goes in the onRestart() method in case the shared preferences data changed while the
        //user was signing slide_to_top on another activity
        //In any case, this should never happen in the app's normal flow.
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActivityIsVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mActivityIsVisible = false;
    }

    protected void setUi() {
        mMail = (EditText) findViewById(R.id.signup_username);
        mPassword = (EditText) findViewById(R.id.login_password);
        mLogIn = (Button) findViewById(R.id.login_btn_login);
        mSignUp = (Button) findViewById(R.id.login_btn_signup);
        mToS = (TextView) findViewById(R.id.tv_tos);
    }

    protected void setListeners() {
        mLogIn.setOnClickListener(logInClickListener);
        mSignUp.setOnClickListener(mSignUpClickListener);
        mToS.setOnClickListener(mTosClickListener);
    }

    @Override
    protected void populate() {
        //Get the stored values for the email and passwords fields (in case they exist)
        String prefEmail = VoltfeedApp.getLogInPreferences()
                .getString(Config.LOGIN_EMAIL_KEY, null);
        String prefPassword = VoltfeedApp.getLogInPreferences()
                .getString(Config.LOGIN_PASSWORD_KEY, null);
        if (prefEmail != null) mMail.setText(prefEmail);
        if (prefPassword != null) mPassword.setText(prefPassword);
    }

    @Override
    protected void init() {

    }

    protected void initFragments() {
        mConnectingDialogInstance = new ConnectingDialog();
    }

    private void blockUi() {
        if (mActivityIsVisible) {
            mLogIn.setEnabled(false);
            mLogIn.setTextColor(getResources().getColor(R.color.gray));
            mSignUp.setEnabled(false);
            mSignUp.setTextColor(getResources().getColor(R.color.gray));
            mMail.setEnabled(false);
            mPassword.setEnabled(false);
            mConnectingDialogInstance.show(getVoltfeedFragmentManager(), "Spinner_fragment_tag");
        }
    }

    private void unlockUi() {
        if (mActivityIsVisible) {
            mLogIn.setEnabled(true);
            mLogIn.setTextColor(getResources().getColor(R.color.black));
            mSignUp.setEnabled(true);
            mSignUp.setTextColor(getResources().getColor(R.color.white));
            mMail.setEnabled(true);
            mPassword.setEnabled(true);
            mConnectingDialogInstance.dismiss();
        }
    }

    private void doLogIn(String email, String password) {
        ParseUser.logInInBackground(email, password, mLogInCallback);
        Log.d(Config.LOG_DEBUG, "(Parse) Log in request send");
        blockUi();
    }

    // ** ANONYMOUS CLASSES **

    View.OnClickListener logInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String mail = mMail.getText().toString().trim();
            String password = mPassword.getText().toString();

            //Rule: Every field is required
            if (mail.isEmpty() || password.isEmpty()) {
                UiHelper.showToast(mContext, getString(R.string.login_require_all));
                return;
            }

            //Rule: must be a valid username, NOT an email
            if (UiHelper.isValidEmail(mail)) {
                mMail.setError(getString(R.string.login_not_valid_email));
                return;
            }

            VoltfeedApp.editLogInPreferences().putString(Config.LOGIN_EMAIL_KEY, mail);
            VoltfeedApp.editLogInPreferences().putString(Config.LOGIN_PASSWORD_KEY, password);
            //Note: we use apply() instead of commit() because apply() works in the background
            VoltfeedApp.editLogInPreferences().apply();

            doLogIn(mail, password);
        }
    };

    View.OnClickListener mSignUpClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, SignUpActivity.class));
        }
    };

    View.OnClickListener mTosClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.ToS_URL))); //ToS URL is stored in the Config class
        }
    };

    LogInCallback mLogInCallback = new LogInCallback() {
        @Override
        public void done(ParseUser parseUser, com.parse.ParseException e) {
            if (parseUser != null) {
                UiHelper.showToast(mContext, getString(R.string.login_welcome));
                UiHelper.startActivityClearStack(mContext, MainActivity.class);
            } else {
                Log.e(Config.LOG_ERROR, String.valueOf(e.getCode()) + ": " + e.getMessage());
                unlockUi();
                UiHelper.showParseError(mContext, e);
            }
        }
    };

    // ** End of ANONYMOUS CLASSES **

}
