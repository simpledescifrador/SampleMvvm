package com.jimac.vetclinicapp.ui.pet.registration;

import android.Manifest.permission;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jimac.vetclinicapp.App;
import com.jimac.vetclinicapp.R;
import com.jimac.vetclinicapp.data.AppDataManager;
import com.jimac.vetclinicapp.data.models.Pet;
import com.jimac.vetclinicapp.ui.base.BaseActivity;
import com.jimac.vetclinicapp.ui.main.MainActivity;
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ActionState.ProceedToPetRegistration;
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.ChangeBreedList;
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.FormError;
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.InitLoading;
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.Loading;
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.OnLoadedPetData;
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.PetRegistrationFailure;
import com.jimac.vetclinicapp.ui.pet.registration.PetRegistrationViewModel.ViewState.PetRegistrationSuccess;
import com.jimac.vetclinicapp.utils.AppUtils;
import com.jimac.vetclinicapp.utils.ImageUtils;
import com.jimac.vetclinicapp.utils.PermissionUtils;
import com.jimac.vetclinicapp.utils.RequestCodes;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PetRegistration extends BaseActivity<PetRegistrationViewModel> {

    private AutoCompleteTextView mAutoCompleteTextViewSpecies, mAutoCompleteTextViewBreed, mAutoCompleteTextViewSize;

    private Button mButtonBrowse, mButtonSubmit;

    private ImageButton mImageButtonClearUploadImage;

    private ImageUtils mImageUtils;

    private TextInputEditText mInputEditTextPetName, mInputEditTextAge, mInputEditTextHeight, mInputEditTextWeight,
            mInputEditTextDescription;

    private TextInputLayout mInputLayoutSpecies, mInputLayoutBreed, mInputLayoutSize, mInputLayoutPetName,
            mInputLayoutAge, mInputLayoutHeight, mInputLayoutWeight,
            mInputLayoutDescription;

    private PermissionUtils mPermissionUtils;

    private RadioButton mRadioButtonGenderFemale;

    private RadioButton mRadioButtonGenderMale;

    private RadioGroup mRadioGroupGender;

    private String mSelectedImageBase64 = "";

    private TextView mTextViewUploadImageSelected, mTextViewImageSize;

    private PetRegistrationViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_registration);
        initViews();
        setUpObservers();
        registerListeners();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCodes.OPEN_GALLERY:
                    if (data.getData() != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            mImageUtils.startImageCrop(selectedImage);
                        }
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    Uri imageUriResultCrop = UCrop.getOutput(data);
                    mTextViewUploadImageSelected
                            .setText(imageUriResultCrop.getPath());
                    mSelectedImageBase64 = mImageUtils
                            .convertImageToBase64(mTextViewUploadImageSelected.getText().toString());
                    mImageButtonClearUploadImage.setVisibility(View.VISIBLE);
                    try {
                        mTextViewImageSize.setText(
                                mImageUtils.formatFileSize(mImageUtils.getImageSizeInKb(imageUriResultCrop)));
                        mTextViewImageSize.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @NonNull
    @Override
    protected PetRegistrationViewModel getViewModel() {
        AppDataManager appDataManager = ((App) getApplication()).getAppDataManager();
        mViewModel = new ViewModelProvider(this, new PetRegistrationViewModel.ViewModelFactory(appDataManager))
                .get(PetRegistrationViewModel.class);
        return mViewModel;
    }

    private void initViews() {
        setTitle("Add Pet");
        //TextInputLayout
        mInputLayoutPetName = findViewById(R.id.inputLayout_petReg_petName);
        mInputLayoutSpecies = findViewById(R.id.inputLayout_petReg_species);
        mInputLayoutBreed = findViewById(R.id.inputLayout_petReg_breed);
        mInputLayoutAge = findViewById(R.id.inputLayout_petReg_age);
        mInputLayoutSize = findViewById(R.id.inputLayout_petReg_size);
        mInputLayoutHeight = findViewById(R.id.inputLayout_petReg_height);
        mInputLayoutWeight = findViewById(R.id.inputLayout_petReg_weight);
        mInputLayoutDescription = findViewById(R.id.inputLayout_petReg_description);

        //TextInputText
        mInputEditTextPetName = findViewById(R.id.inputText_petReg_petName);
        mInputEditTextAge = findViewById(R.id.inputText_petReg_age);
        mInputEditTextHeight = findViewById(R.id.inputText_petReg_height);
        mInputEditTextWeight = findViewById(R.id.inputText_petReg_weight);
        mInputEditTextDescription = findViewById(R.id.inputText_petReg_description);

        //RadioGroup
        mRadioGroupGender = findViewById(R.id.radioGroup_petReg_gender);

        //Radio Button
        mRadioButtonGenderMale = findViewById(R.id.radioButton_petReg_genderMale);
        mRadioButtonGenderFemale = findViewById(R.id.radioButton_petReg_genderFemale);

        //AutoCompleteTextView
        mAutoCompleteTextViewSpecies = findViewById(R.id.autoCompleteTextView_petReg_species);
        mAutoCompleteTextViewBreed = findViewById(R.id.autoCompleteTextView_petReg_breed);
        mAutoCompleteTextViewSize = findViewById(R.id.autoCompleteTextView_petReg_size);

        //TextView
        mTextViewUploadImageSelected = findViewById(R.id.textView_petReg_uploadPath);
        mTextViewImageSize = findViewById(R.id.textView_petReg_imageSize);

        //Button
        mButtonBrowse = findViewById(R.id.button_petReg_browse);
        mButtonSubmit = findViewById(R.id.button_petReg_submit);

        //ImageButton
        mImageButtonClearUploadImage = findViewById(R.id.imageButton_petReg_clearUploadImage);

        mImageUtils = new ImageUtils(this);
        mPermissionUtils = new PermissionUtils(this);
    }

    private void registerListeners() {
        mViewModel.initPetData();
        mAutoCompleteTextViewSpecies.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSpecies = parent.getItemAtPosition(position).toString();
            mViewModel.loadBreeds(selectedSpecies);
        });
        mButtonBrowse.setOnClickListener(v -> {
            Dexter.withActivity(this)
                    .withPermissions(
                            permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionRationaleShouldBeShown(final List<PermissionRequest> permissions,
                                final PermissionToken token) {
                            token.continuePermissionRequest();
                        }

                        @Override
                        public void onPermissionsChecked(final MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                mImageUtils.getImageFromGallery();
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                mPermissionUtils.showSettingsDialog();
                            }
                        }
                    }).check();
        });

        mButtonSubmit.setOnClickListener(v -> {

            String petName = mInputEditTextPetName.getText().toString();
            String species = mAutoCompleteTextViewSpecies.getText().toString();
            String breed = mAutoCompleteTextViewBreed.getText().toString();
            String size = mAutoCompleteTextViewSize.getText().toString();
            String height = mInputEditTextHeight.getText().toString();
            String weight = mInputEditTextWeight.getText().toString();

            int age = -1;
            try {
                if (mInputEditTextAge.getText() != null) {
                    age = Integer.parseInt(mInputEditTextAge.getText().toString());
                }
            } catch (NumberFormatException e) {
                age = -1;
            }

            String gender;
            if (mRadioButtonGenderMale.isChecked()) {
                gender = "Male";
            } else {
                gender = "Female";
            }

            String imagePath = mSelectedImageBase64;
            String description = mInputEditTextDescription.getText().toString();

            Pet pet = new Pet();
            pet.setName(petName);
            pet.setSpecies(species);
            pet.setBreed(breed);
            pet.setAge(age);
            pet.setSize(size);
            pet.setHeight(height);
            pet.setWeight(weight);
            pet.setGender(gender);
            pet.setPetImageUrl(imagePath);
            pet.setDescription(description);

            mViewModel.validateForm(pet);
        });

        mImageButtonClearUploadImage.setOnClickListener(v -> {
            mImageButtonClearUploadImage.setVisibility(View.GONE);
            mSelectedImageBase64 = "";
            mTextViewUploadImageSelected.setText("No Image Selected");

            mTextViewImageSize.setVisibility(View.GONE);
            mTextViewImageSize.setText("");
        });
    }

    private void setUpObservers() {
        mViewModel.getViewState().observe(this, viewState -> {
            if (viewState instanceof Loading) {
                showLoading("Loading");
            } else if (viewState instanceof InitLoading) {
                showLoading("Getting Pet Data");
            } else {
                hideLoading();
            }

            if (viewState instanceof FormError) {
                FormError formError = (FormError) viewState;
                String petNameError = formError.getPetNameError();
                String speciesError = formError.getSpeciesError();
                String breedError = formError.getBreedError();
                String ageError = formError.getAgeError();
                String sizeError = formError.getSizeError();
                String heightError = formError.getHeightError();
                String weightError = formError.getWeightError();
                String descriptionError = formError.getDescriptionError();

                //Set Errors
                mInputLayoutPetName.setError(petNameError);
                mInputLayoutSpecies.setError(speciesError);
                mInputLayoutBreed.setError(breedError);
                mInputLayoutAge.setError(ageError);
                mInputLayoutSize.setError(sizeError);
                mInputLayoutHeight.setError(heightError);
                mInputLayoutWeight.setError(weightError);
                mInputLayoutDescription.setError(descriptionError);

            } else if (viewState instanceof OnLoadedPetData) {
                ArrayAdapter<String> speciesAdapter =
                        new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                                ((OnLoadedPetData) viewState).getSpecies());
                mAutoCompleteTextViewSpecies.setAdapter(speciesAdapter);
                ArrayAdapter<String> sizeAdapter =
                        new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                                ((OnLoadedPetData) viewState).getSize());
                mAutoCompleteTextViewSize.setAdapter(sizeAdapter);
            } else if (viewState instanceof ChangeBreedList) {
                mAutoCompleteTextViewBreed.setText(null);

                ArrayList<String> breeds = ((ChangeBreedList) viewState).getBreeds();
                ArrayAdapter<String> breedAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, breeds);
                mAutoCompleteTextViewBreed.setAdapter(breedAdapter);

            } else if (viewState instanceof PetRegistrationFailure) {
                showToast(((PetRegistrationFailure) viewState).getErrorMessage(), Toast.LENGTH_SHORT);
            } else if (viewState instanceof PetRegistrationSuccess) {
                showToast("Your Pet was Successfully Added.", Toast.LENGTH_SHORT);
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);
                finish();
            }
        });

        mViewModel.getActionState().observe(this, actionState -> {
            if (actionState instanceof ProceedToPetRegistration) {

                new Builder(this)
                        .setMessage("Proceed to Add Pet?")
                        .setTitle("Confirm")
                        .setPositiveButton("Okay", (dialog, which) -> {
                            if (!AppUtils.isNetworkAvailable(this)) {
                                AppUtils.showDialog(this, "Message", "No Network Available");
                            } else {
                                mViewModel.submitPetRegistration(((ProceedToPetRegistration) actionState).getPet());
                            }
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).create().show();
            }
        });
    }

}