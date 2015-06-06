package com.droidko.voltfeed.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.droidko.voltfeed.Config;
import com.droidko.voltfeed.R;
import com.droidko.voltfeed.TrainingApp;
import com.droidko.voltfeed.api.SignUpService;
import com.droidko.voltfeed.entities.User;
import com.droidko.voltfeed.ui.ConnectingDialog;
import com.droidko.voltfeed.utils.InputCheckHelper;
import com.droidko.voltfeed.utils.UiHelper;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SignUpActivity extends ActionBarActivity {

    private Context mContext;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mPreferencesEditor;

    private EditText mMail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private Button mJoin;
    private TextView mToS;
    private Toolbar mToolbar;

    private SignUpService mSignUpService;

    private User mUser;

    private boolean mActivityIsVisible;

    private FragmentManager mFragmentManager;
    private ConnectingDialog mConnectingDialogInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mContext = this;

        initPreferences();
        setUi();
        setListeners();
        initApiConnection();
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
        mMail = (EditText) findViewById(R.id.et_email);
        mPassword = (EditText) findViewById(R.id.et_password);
        mConfirmPassword = (EditText) findViewById(R.id.et_password_confirm);
        mJoin = (Button) findViewById(R.id.btn_join);
        mToS = (TextView) findViewById(R.id.tv_tos);
        mToolbar = UiHelper.setToolbar(
                this,
                R.id.toolbar,
                R.id.toolbar_title,
                getString(R.string.title_activity_sign_up),
                R.id.toolbar_logo,
                R.drawable.topbarlogo);
    }

    private void initPreferences() {
        mPreferences = mContext.getSharedPreferences(Config.LOGIN_PREFERENCES_KEY, Context.MODE_PRIVATE);
        mPreferencesEditor = mPreferences.edit();
    }

    private void initApiConnection() {
        //Get a connection to the Parsi API by requesting it to the app level class
        mSignUpService = TrainingApp.getRestAdapter().create(SignUpService.class);
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
            mMail.setEnabled(true);
            mPassword.setEnabled(true);
            mConfirmPassword.setEnabled(true);
            mConnectingDialogInstance.dismiss();
        }
    }

    private void doSignUp(String email, String password) {
        mUser = new User();
        mUser.setUsername(email);
        mUser.setPassword(password);

        mSignUpService.signUp(mUser, mSignUpCallback);
        Log.d(Config.LOG_DEBUG, "(Retrofit) Log in request send");
        blockUi();
    }

    // ** ANONYMOUS CLASSES **

    View.OnClickListener mJoinClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String mail = mMail.getText().toString().trim();
            String password = mPassword.getText().toString();
            String confirmPassword = mConfirmPassword.getText().toString();

            //Rule: Every field is required
            if ( mail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ) {
                UiHelper.showToast(mContext, getString(R.string.login_require_all));
                return;
            }

            //Rule: must be a valid email adress
            if (!InputCheckHelper.isValidEmail(mail)) {
                mMail.setError(getString(R.string.login_not_valid_email));
                return;
            }

            //Regla: El campo "contraseña" y "confirmar contraseña" deben coincidir
            if (!password.equals(confirmPassword)) {
                mConfirmPassword.setError(getString(R.string.signup_passwords_dont_match));
                return;
            }

            doSignUp(mail, password);
        }
    };

    View.OnClickListener mTosClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.ToS_URL))); //Note: we use apply() instead of commit() because apply() works in the background
        }
    };

    Callback<User> mSignUpCallback = new Callback<User>() {
        @Override
        public void success(User user, Response response) {
            unlockUi();
            if (response.getStatus() == 201) { //Status 201: User created
                //Note: We can't do this.mUser = user because Parse dosn't return every attribute during the SignUp,
                //therefore some attributes would be missing.
                mUser.setCreatedAt(user.getCreatedAt());
                mUser.setObjectId(user.getObjectId());
                mUser.setSessionToken(user.getSessionToken());
                mPreferencesEditor.putString(Config.LOGIN_EMAIL_KEY, mUser.getUsername());
                mPreferencesEditor.putString(Config.LOGIN_PASSWORD_KEY, mUser.getPassword());
                mPreferencesEditor.putString(Config.LOGIN_SESSION_KEY, mUser.getSessionToken());
                mPreferencesEditor.apply();
                UiHelper.showToast(mContext, getString(R.string.signup_user_created));
                UiHelper.startActivityClearStack(mContext, MainActivity.class);
            }
            //There should be no situation where in spite of the response type being success the user has not been created.
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
                UiHelper.showToast(mContext, getString(R.string.login_unable_to_connect));
                return;
            }
            //Error 202: El usuario ya existe
            if (mUser.getCode().contains("202")) {
                UiHelper.showToast(
                        mContext,
                        getString(R.string.signup_invalid_username));
            }
        }
    };

    // ** End of ANONYMOUS CLASSES **

}
