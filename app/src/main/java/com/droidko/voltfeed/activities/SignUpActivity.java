package com.droidko.voltfeed.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.Schema;
import com.droidko.voltfeed.ui.ConnectingDialog;
import com.droidko.voltfeed.utils.UiHelper;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends ActionBarActivity {

    private Context mContext;

    private EditText mUsername;
    private EditText mMail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mJoin;
    private TextView mToS;
    private Toolbar mToolbar;

    private boolean mActivityIsVisible;

    private FragmentManager mFragmentManager;
    private ConnectingDialog mConnectingDialogInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mContext = this;

        setUi();
        setListeners();
        initFragments();
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

    private void setUi() {
        mUsername = (EditText) findViewById(R.id.signup_username);
        mMail = (EditText) findViewById(R.id.signup_email);
        mPassword = (EditText) findViewById(R.id.login_password);
        mConfirmPassword = (EditText) findViewById(R.id.signup_password_confirm);
        mJoin = (Button) findViewById(R.id.signup_btn_join);
        mToS = (TextView) findViewById(R.id.tv_tos);
        mToolbar = UiHelper.setToolbar(
                this,
                R.id.main_toolbar,
                R.id.toolbar_title,
                getString(R.string.title_activity_sign_up),
                R.id.toolbar_logo,
                R.drawable.ic_topbar);
    }

    private void setListeners() {
        mJoin.setOnClickListener(mJoinClickListener);
        mToS.setOnClickListener(mTosClickListener);
    }

    private void initFragments() {
        mFragmentManager = getSupportFragmentManager();
        mConnectingDialogInstance = new ConnectingDialog();
    }

    private void blockUi() {
        if (mActivityIsVisible) {
            mJoin.setEnabled(false);
            mJoin.setTextColor(getResources().getColor(R.color.gray));
            mUsername.setEnabled(false);
            mMail.setEnabled(false);
            mPassword.setEnabled(false);
            mConfirmPassword.setEnabled(false);
            mConnectingDialogInstance.show(mFragmentManager, "Spinner_fragment_tag");
        }
    }

    private void unlockUi() {
        if (mActivityIsVisible) {
            mJoin.setEnabled(true);
            mJoin.setTextColor(getResources().getColor(R.color.white));
            mUsername.setEnabled(true);
            mMail.setEnabled(true);
            mPassword.setEnabled(true);
            mConfirmPassword.setEnabled(true);
            mConnectingDialogInstance.dismiss();
        }
    }

    private void doSignUp(String username, String email, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.put(Schema.USER_COL_EMAIL, email);
        user.signUpInBackground(mSignUpCallback);

        Log.d(Config.LOG_DEBUG, "(Parse) Sign slide_to_top request send");
        blockUi();
    }

    // ** ANONYMOUS CLASSES **

    View.OnClickListener mJoinClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String username = mUsername.getText().toString().trim();
            String mail = mMail.getText().toString().trim();
            String password = mPassword.getText().toString();
            String confirmPassword = mConfirmPassword.getText().toString();

            //Rule: Every field is required
            if ( TextUtils.isEmpty(username)
                    || TextUtils.isEmpty(mail)
                    || TextUtils.isEmpty(password)
                    || TextUtils.isEmpty(confirmPassword) ) {
                UiHelper.showToast(mContext, getString(R.string.login_require_all));
                return;
            }

            //Rule: must be a valid email adress
            if (!UiHelper.isValidEmail(mail)) {
                mMail.setError(getString(R.string.login_not_valid_email));
                return;
            }

            //Rule: passwords can't be too short
            if (password.length() < Config.USER_PASSWORD_MIN_LENGHT) {
                mPassword.setError(getResources()
                        .getString(R.string.login_password_too_short,
                                Config.USER_PASSWORD_MIN_LENGHT));
                return;
            }

            //Rule: The field "password" and "confirm password" must match
            if (!password.equals(confirmPassword)) {
                mConfirmPassword.setError(getString(R.string.signup_passwords_dont_match));
                return;
            }

            doSignUp(username, mail, password);
        }
    };

    View.OnClickListener mTosClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.ToS_URL)));
        }
    };

    SignUpCallback mSignUpCallback = new  SignUpCallback() {
        @Override
        public void done(ParseException e) {
            unlockUi();
            if (e == null) {
                UiHelper.showToast(mContext, getString(R.string.signup_user_created));
                UiHelper.startActivityClearStack(mContext, MainActivity.class);
            } else {
                UiHelper.showParseError(mContext, e);
            }
        }
    };

    // ** End of ANONYMOUS CLASSES **

}
