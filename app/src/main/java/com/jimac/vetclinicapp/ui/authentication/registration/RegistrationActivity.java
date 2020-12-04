package com.jimac.vetclinicapp.ui.authentication.registration;

import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jimac.vetclinicapp.App;
import com.jimac.vetclinicapp.R;
import com.jimac.vetclinicapp.data.AppDataManager;
import com.jimac.vetclinicapp.data.models.Client;
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ActionState.ProceedRegistration;
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ActionState.SaveClientData;
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ViewState.FormAgreementError;
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ViewState.FormError;
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ViewState.Loading;
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ViewState.RegistrationFailure;
import com.jimac.vetclinicapp.ui.authentication.registration.RegistrationViewModel.ViewState.RegistrationSuccess;
import com.jimac.vetclinicapp.ui.base.BaseActivity;
import com.jimac.vetclinicapp.ui.main.MainActivity;
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistration;
import com.jimac.vetclinicapp.utils.AppUtils;
import com.jimac.vetclinicapp.utils.Constants;

public class RegistrationActivity extends BaseActivity<RegistrationViewModel> {

    private boolean mAgreement = false;

    private Button mButtonRegister;

    private CheckBox mCheckBoxAgreement;

    private TextInputLayout mInputLayoutFirstName, mInputLayoutLastName, mInputLayoutAddress,
            mInputLayoutContactNumber, mInputLayoutEmail, mInputLayoutPassword;

    private TextInputEditText mInputTextFirstName, mInputTextLastName, mInputTextAddress,
            mInputTextContactNumber, mInputTextEmail, mInputTextPassword;

    private RadioButton mRadioButtonFemale;

    private RadioButton mRadioButtonMale;

    private RadioGroup mRadioGroupGender;

    private RegistrationViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        setUpObservers();
        registerListeners();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE_ADD_PET) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mainIntent);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @NonNull
    @Override
    protected RegistrationViewModel getViewModel() {
        AppDataManager appDataManager = ((App) getApplication()).getAppDataManager();
        mViewModel = new ViewModelProvider(this, new RegistrationViewModel.ViewModelFactory(appDataManager))
                .get(RegistrationViewModel.class);
        return mViewModel;
    }

    private void initViews() {
        setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //TextInputLayout Views
        mInputLayoutFirstName = findViewById(R.id.inputLayout_register_firstname);
        mInputLayoutLastName = findViewById(R.id.inputLayout_register_lastname);
        mInputLayoutAddress = findViewById(R.id.inputLayout_register_address);
        mInputLayoutContactNumber = findViewById(R.id.inputLayout_register_contactNumber);
        mInputLayoutEmail = findViewById(R.id.inputLayout_register_email);
        mInputLayoutPassword = findViewById(R.id.inputLayout_register_password);
        //TextInputEditText
        mInputTextFirstName = findViewById(R.id.inputText_register_firstname);
        mInputTextLastName = findViewById(R.id.inputText_register_lastname);
        mInputTextAddress = findViewById(R.id.inputText_register_address);
        mInputTextContactNumber = findViewById(R.id.inputText_register_contactNumber);
        mInputTextEmail = findViewById(R.id.inputText_register_email);
        mInputTextPassword = findViewById(R.id.inputText_register_password);
        //Radio Group
        mRadioGroupGender = findViewById(R.id.radioGroup_register_gender);
        //Radio Button
        mRadioButtonMale = findViewById(R.id.radioButton_register_genderMale);
        mRadioButtonFemale = findViewById(R.id.radioButton_register_genderFemale);
        //Checkbox
        mCheckBoxAgreement = findViewById(R.id.checkBox_register_agreement);
        //Button
        mButtonRegister = findViewById(R.id.button_register_submit);
    }

    private void registerListeners() {
        mCheckBoxAgreement.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mAgreement = isChecked;
        });

        mButtonRegister.setOnClickListener(v -> {
            String firstName = mInputTextFirstName.getText().toString();
            String lastName = mInputTextLastName.getText().toString();
            String address = mInputTextAddress.getText().toString();
            String contactNumber = mInputTextContactNumber.getText().toString();
            String email = mInputTextEmail.getText().toString();
            String password = mInputTextPassword.getText().toString();
            String gender;
            if (mRadioButtonMale.isChecked()) {
                gender = "Male";
            } else {
                gender = "Female";
            }

            Client client = new Client(
                    0,
                    "",
                    0,
                    firstName,
                    lastName,
                    address,
                    contactNumber,
                    gender,
                    email,
                    password
            );
            AppUtils.hideKeyboard(this);
            mViewModel.validateForm(client, mAgreement);
        });
    }

    private void setUpObservers() {
        mViewModel.getViewState().observe(this, viewState -> {
            if (viewState instanceof Loading) {
                showLoading();
            } else {
                hideLoading();
            }
            if (viewState instanceof FormError) {
                //Get Error Messages
                FormError formError = (FormError) viewState;

                String fNameError = formError.getFNameError();
                String lNameError = formError.getLNameError();
                String addressError = formError.getAddressError();
                String contactNumberError = formError.getCNumberError();
                String emailError = formError.getEmailError();
                String passwordError = formError.getPasswordError();

                //Set Error
                mInputLayoutFirstName.setError(fNameError);
                mInputLayoutLastName.setError(lNameError);
                mInputLayoutAddress.setError(addressError);
                mInputLayoutContactNumber.setError(contactNumberError);
                mInputLayoutEmail.setError(emailError);
                mInputLayoutPassword.setError(passwordError);

            } else if (viewState instanceof FormAgreementError) {
                showToast(((FormAgreementError) viewState).getErrorMessage(), Toast.LENGTH_SHORT);
            } else if (viewState instanceof RegistrationSuccess) {
                showToast("Registration Success", Toast.LENGTH_SHORT);
                Intent intent = new Intent(this, PetRegistration.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADD_PET);
            } else if (viewState instanceof RegistrationFailure) {
                showToast(((RegistrationFailure) viewState).getErrorMessage(), Toast.LENGTH_SHORT);
            }

        });

        mViewModel.getActionState().observe(this, actionState -> {
            if (actionState instanceof ProceedRegistration) {
                new Builder(this)
                        .setMessage("Proceed to register?")
                        .setTitle("Confirm")
                        .setPositiveButton("Okay", (dialog, which) -> {
                            if (!AppUtils.isNetworkAvailable(this)) {
                                AppUtils.showDialog(this, "Message", "No Network Available");
                            } else {
                                mViewModel.register(((ProceedRegistration) actionState).getClient());
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).create().show();
            } else if (actionState instanceof SaveClientData) {
                mViewModel.saveClientData(((SaveClientData) actionState).getClient());
            }
        });
    }
}