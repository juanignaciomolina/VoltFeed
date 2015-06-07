package com.droidko.voltfeed.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.VoltFeedApp;
import com.droidko.voltfeed.api.LogInService;
import com.droidko.voltfeed.api.ParseAPIHelper;
import com.droidko.voltfeed.entities.User;
import com.droidko.voltfeed.ui.ConnectingDialog;
import com.droidko.voltfeed.utils.InputCheckHelper;
import com.droidko.voltfeed.utils.UiHelper;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LogInActivity extends FragmentActivity {

    private Context mContext;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mPreferencesEditor;

    private EditText mMail;
    private EditText mPassword;
    private Button mLogIn;
    private Button mSignUp;
    private TextView mToS;

    private ParseAPIHelper mAPIHelper;
    private LogInService mLogInService;
    private RestAdapter mRestAdapter;

    private User mUser;

    private boolean mActivityIsVisible;

    private FragmentManager mFragmentManager;
    private ConnectingDialog mConnectingDialogInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mContext = this;

        initPreferences(); //Get ready the SharedPreferences
        setUi(); //findViewsById
        setListeners(); //Bindear listeners to buttons
        initUi(); //Load UI default values
        initApiConnection();
        initFragments(); //Get ready the fragmentManager and the fragments
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //This goes in the onRestart() method in case the shared preferences data changed while the
        //user was signing up on another activity
        //In any case, this should never happen in the app's normal flow.
        initUi();
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

    private void initPreferences() {
        mPreferences = mContext.getSharedPreferences(Config.LOGIN_PREFERENCES_KEY, Context.MODE_PRIVATE);
        mPreferencesEditor = mPreferences.edit();
    }

    private void setUi() {
        mMail = (EditText) findViewById(R.id.et_email);
        mPassword = (EditText) findViewById(R.id.et_password);
        mLogIn = (Button) findViewById(R.id.btn_login);
        mSignUp = (Button) findViewById(R.id.btn_signup);
        mToS = (TextView) findViewById(R.id.tv_tos);
    }

    private void setListeners() {
        mLogIn.setOnClickListener(logInClickListener);
        mSignUp.setOnClickListener(mSignUpClickListener);
        mToS.setOnClickListener(mTosClickListener);
    }

    private void initUi() {
        //Get the stored values for the email and passwords fields (in case they exist)
        String prefEmail = mPreferences.getString(Config.LOGIN_EMAIL_KEY, null);
        String prefPassword = mPreferences.getString(Config.LOGIN_PASSWORD_KEY, null);
        if (prefEmail != null) mMail.setText(prefEmail);
        if (prefPassword != null) mPassword.setText(prefPassword);
    }

    private void initApiConnection() {
        //Get a connection to the Parsi API by requesting it to the app level class
        mLogInService = VoltFeedApp.getRestAdapter().create(LogInService.class);
    }

    private void initFragments() {
        mFragmentManager = getSupportFragmentManager();
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
            mConnectingDialogInstance.show(mFragmentManager, "Spinner_fragment_tag");
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

            //Rule: must be a valid email adress
            if (!InputCheckHelper.isValidEmail(mail)) {
                mMail.setError(getString(R.string.login_not_valid_email));
                return;
            }

            mPreferencesEditor.putString(Config.LOGIN_EMAIL_KEY, mail);
            mPreferencesEditor.putString(Config.LOGIN_PASSWORD_KEY, password);
            mPreferencesEditor.apply(); //Note: we use apply() instead of commit() because apply() works in the background

            doLogIn(mail, password);
        }
    };

    private void doLogIn(String email, String password) {
        mUser = new User();
        mUser.setUsername(email);
        mUser.setPassword(password);

        mLogInService.logIn(email, password, mLogInCallback);
        Log.d(Config.LOG_DEBUG, "(Retrofit) Log in request send");
        blockUi();
    }

    // ** ANONYMOUS CLASSES **

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

    Callback<User> mLogInCallback = new Callback<User>() {
        @Override
        public void success(User user, Response response) {
            unlockUi();
            if (response.getStatus() == 200) { //Status 200: Log in OK
                mUser.setSessionToken(user.getSessionToken());
                mPreferencesEditor.putString(Config.LOGIN_SESSION_KEY, mUser.getSessionToken());
                mPreferencesEditor.apply();
                UiHelper.showToast(mContext, getString(R.string.login_welcome));
                UiHelper.startActivityClearStack(mContext, MainActivity.class);
            }
            //There should be no situation where in spite of the response type being success the user has not logged in.
            //If this happens for some strange reason, we let the user know that something went wrong.
            else {
                UiHelper.showToast(mContext, getString(R.string.error_connection_unknown));
                Log.e(Config.LOG_ERROR, "Unknown connection response: " + response.getStatus());
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.e(Config.LOG_ERROR, error.getMessage());
            unlockUi();
            mUser = (User) error.getBody();
            if (mUser == null) {
                UiHelper.showToast(
                        mContext,
                        getString(R.string.login_unable_to_connect));
                return;
            }
            //Error 101: Invalid username and/or password
            if (mUser.getCode().contains("101")) {
                UiHelper.showToast(
                        mContext,
                        getString(R.string.login_wrong_credentials));
            }
        }
    };

    // ** End of ANONYMOUS CLASSES **

}
