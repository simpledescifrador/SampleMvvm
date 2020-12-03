package com.jimac.vetclinicapp.ui.authentication.login;

import static com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ViewState.Loading;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jimac.vetclinicapp.App;
import com.jimac.vetclinicapp.R;
import com.jimac.vetclinicapp.data.AppDataManager;
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ActionState.ProceedLogin;
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ActionState.SaveClientData;
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ActionState.SkipLogin;
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ViewState.FormError;
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ViewState.LoginFailure;
import com.jimac.vetclinicapp.ui.authentication.login.LoginViewModel.ViewState.LoginSuccess;
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationActivity;
import com.jimac.vetclinicapp.ui.base.BaseActivity;
import com.jimac.vetclinicapp.ui.main.MainActivity;
import com.jimac.vetclinicapp.utils.AppUtils;

public class LoginActivity extends BaseActivity<LoginViewModel> {

    private Button mButtonLoginSubmit, mButtonToRegister;

    private TextInputLayout mInputLayoutEmail, mInputLayoutPassword;

    private TextInputEditText mInputTextEmail, mInputTextPassword;

    private TextView mTextViewForgotPassword;

    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mViewModel.checkIfAlreadyLogin();
        setContentView(R.layout.activity_login);
        initViews();
        setUpObservers();
        registerListeners();
    }

    @NonNull
    @Override
    protected LoginViewModel getViewModel() {
        AppDataManager appDataManager = ((App) getApplication()).getAppDataManager();
        mViewModel = new ViewModelProvider(this, new LoginViewModel.ViewModelFactory(appDataManager))
                .get(LoginViewModel.class);
        return mViewModel;
    }

    private void initViews() {
        //TextInputLayout
        mInputLayoutEmail = findViewById(R.id.inputLayout_login_email);
        mInputLayoutPassword = findViewById(R.id.inputLayout_login_password);
        //TextInputText
        mInputTextEmail = findViewById(R.id.inputText_login_email);
        mInputTextPassword = findViewById(R.id.inputText_login_password);
        //TextView
        mTextViewForgotPassword = findViewById(R.id.textView_login_forgotPasswordLink);
        //Button
        mButtonLoginSubmit = findViewById(R.id.button_login_submit);
        mButtonToRegister = findViewById(R.id.button_login_toRegister);
    }

    private void registerListeners() {
        mButtonToRegister.setOnClickListener(v -> startActivity(new Intent(this, RegistrationActivity.class)));
        mButtonLoginSubmit.setOnClickListener(v -> mViewModel.validateForm(
                mInputTextEmail.getText().toString(),
                mInputTextPassword.getText().toString()
        ));
    }

    private void setUpObservers() {

        mViewModel.getViewState().observe(this, viewState -> {
            if (viewState instanceof Loading) {
                showLoading();
            } else {
                hideLoading();
            }

            if (viewState instanceof FormError) {
                FormError formError = (FormError) viewState;
                String emailError = formError.getEmailError();
                String passwordError = formError.getPasswordError();

                //Set Error
                mInputLayoutEmail.setError(emailError);
                mInputLayoutPassword.setError(passwordError);
            }

            if (viewState instanceof LoginSuccess) {
                showToast("Login Success", Toast.LENGTH_SHORT);
                toMain();
            } else if (viewState instanceof LoginFailure) {
                showToast(((LoginFailure) viewState).getErrorMessage(), Toast.LENGTH_SHORT);
            }

        });

        mViewModel.getActionState().observe(this, actionState -> {
            if (actionState instanceof ProceedLogin) {
                if (!AppUtils.isNetworkAvailable(this)) {
                    AppUtils.showDialog(this, "Message", "No Network Available");
                } else {
                    mViewModel.login(((ProceedLogin) actionState).getEmailAddress().trim(),
                            ((ProceedLogin) actionState).getPassword().trim());
                }
            } else if (actionState instanceof SaveClientData) {
                mViewModel.saveClientData(((SaveClientData) actionState).getClient());
            } else if (actionState instanceof SkipLogin) {
                toMain();
            }
        });
    }

    private void toMain() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }
}