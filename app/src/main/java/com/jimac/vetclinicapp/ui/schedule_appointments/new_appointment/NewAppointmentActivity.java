package com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialDatePicker.Builder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jimac.vetclinicapp.App;
import com.jimac.vetclinicapp.R;
import com.jimac.vetclinicapp.data.AppDataManager;
import com.jimac.vetclinicapp.data.models.Appointment;
import com.jimac.vetclinicapp.ui.base.BaseActivity;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ActionState.ProceedToSubmitAppointment;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewModelFactory;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.ChangeServicesList;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.Error;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.FormError;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.InitLoading;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.Loading;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.OnInitLoaded;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.SetAvailableTimeSlots;
import com.jimac.vetclinicapp.ui.schedule_appointments.new_appointment.NewAppointmentViewModel.ViewState.SetServiceDuration;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

public class NewAppointmentActivity extends BaseActivity<NewAppointmentViewModel> {

    private AutoCompleteTextView mAutoCompleteTextViewServices, mAutoCompleteTextViewPets;

    private Button mButtonSubmit, mButtonSearchSlots;

    private ChipGroup mChipGroupServiceCategory, mChipGroupTimeSlots;

    private Chip mChipPetGrooming, mChipClinic;

    private TextInputEditText mInputEditTextAppointmentDate, mInputEditTextNote, mInputEditTextAppointmentTime;

    private TextInputLayout mInputLayoutAppointmentDate, mInputLayoutNote, mInputLayoutServices, mInputLayoutPets,
            mInputLayoutAppointmentTime;

    private String mSetSelectedServiceType = "", mSetSelectedService = "";

    private TextView mTextViewNoSlotsAvailable, mTextViewServiceDuration;

    private NewAppointmentViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        setTitle("Schedule New Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        setUpObservers();
        registerListeners();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @NonNull
    @Override
    protected NewAppointmentViewModel getViewModel() {
        AppDataManager appDataManager = ((App) getApplication()).getAppDataManager();
        mViewModel = new ViewModelProvider(this, new ViewModelFactory(appDataManager))
                .get(NewAppointmentViewModel.class);
        return mViewModel;
    }

    private void initViews() {
        //TextInputLayout
        mInputLayoutServices = findViewById(R.id.inputLayout_newAppointment_subServiceType);
        mInputLayoutPets = findViewById(R.id.inputLayout_newAppointment_pets);
        mInputLayoutAppointmentDate = findViewById(R.id.inputLayout_newAppointment_date);
        mInputLayoutNote = findViewById(R.id.inputLayout_newAppointment_note);
        mInputLayoutAppointmentTime = findViewById(R.id.inputLayout_newAppointment_time);

        //TextEditText
        mInputEditTextAppointmentDate = findViewById(R.id.inputText_newAppointment_date);
        mInputEditTextNote = findViewById(R.id.inputText_newAppointment_note);
        mInputEditTextAppointmentTime = findViewById(R.id.inputText_newAppointment_time);

        //AutoComplete
        mAutoCompleteTextViewServices = findViewById(R.id.autoCompleteTextView_newAppointment_subServiceType);
        mAutoCompleteTextViewPets = findViewById(R.id.autoCompleteTextView_newAppointment_pets);

        //Chip
        mChipGroupServiceCategory = findViewById(R.id.chipGroup_newAppointment_serviceCategory);
        mChipGroupTimeSlots = findViewById(R.id.chipGroup_newAppointment_timeSlots);
        mChipPetGrooming = findViewById(R.id.chip_newAppointment_petGrooming);
        mChipClinic = findViewById(R.id.chip_newAppointment_clinic);

        //Button
        mButtonSubmit = findViewById(R.id.button_newAppointment_submit);
        mButtonSearchSlots = findViewById(R.id.button_newAppointment_searchSlots);

        //TextView
        mTextViewNoSlotsAvailable = findViewById(R.id.textView_newAppointment_noSlots);
        mTextViewServiceDuration = findViewById(R.id.textView_newAppointment_serviceDuration);

    }

    private void registerListeners() {
        mInputEditTextAppointmentDate.setOnClickListener(v -> {
            Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            CalendarConstraints.Builder ccb = new CalendarConstraints.Builder();
            CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward
                    .from(System.currentTimeMillis());
            ccb.setValidator(dateValidator);
            builder.setCalendarConstraints(ccb.build());
            MaterialDatePicker<Long> picker = builder.build();
            picker.addOnPositiveButtonClickListener(selection -> {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                        "yyyy/MM/dd");
                String dateText = simpleDateFormat.format(selection);
                mInputEditTextAppointmentDate.setText(dateText);
            });
            picker.addOnNegativeButtonClickListener(v1 -> {

            });
            picker.show(getSupportFragmentManager(), picker.toString());
        });

        mInputEditTextAppointmentTime.setOnClickListener(v -> {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(this,
                    (timePicker, selectedHour, selectedMinute) -> mInputEditTextAppointmentTime.setText(
                            String.format("%02d:%02d", selectedHour, selectedMinute)), hour, minute,
                    true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });

        mAutoCompleteTextViewServices.setOnItemClickListener(
                (parent, view, position, id) -> mViewModel
                        .onSelectedService(parent.getItemAtPosition(position).toString()));

        mChipGroupServiceCategory.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == mChipPetGrooming.getId()) {
                mSetSelectedServiceType = "Pet Grooming";
                mViewModel.onChangeServices(mSetSelectedServiceType);
            } else if (checkedId == mChipClinic.getId()) {
                mSetSelectedServiceType = "Clinic";
                mViewModel.onChangeServices(mSetSelectedServiceType);
            }
        });

        mButtonSearchSlots.setOnClickListener(v -> mViewModel
                .searchAvailableSlots(mSetSelectedServiceType, mAutoCompleteTextViewServices.getText().toString(),
                        mInputEditTextAppointmentDate.getText().toString()));

        mButtonSubmit.setOnClickListener(v -> {
            String serviceType = "";

            if (mChipPetGrooming.isChecked()) {
                serviceType = "Pet Grooming";
            } else if (mChipClinic.isChecked()) {
                serviceType = "Clinic";
            }

            String service = mAutoCompleteTextViewServices.getText().toString();
            String petName = mAutoCompleteTextViewPets.getText().toString();
            String appointmentDate = mInputEditTextAppointmentDate.getText().toString();

            String timeSlot = "10:00 AM - 11:00 AM"; //TODO : TEMP TIMESLOT
            String note = mInputEditTextNote.getText().toString();

            Appointment appointment = new Appointment();
            appointment.setServiceType(serviceType);
            appointment.setService(service);
            appointment.setPetName(petName);
            appointment.setAppointmentDate(appointmentDate);
            appointment.setTimeSlot(timeSlot);
            appointment.setNote(note);

            mViewModel.validateForm(appointment);
        });
    }

    private void setUpObservers() {
        mViewModel.init();
        mViewModel.getViewState().observe(this, viewState -> {
            if (viewState instanceof Loading) {
                showLoading();
            } else if (viewState instanceof InitLoading) {
                showLoading("Retrieving Data");
            } else {
                hideLoading();
            }

            if (viewState instanceof OnInitLoaded) {
                ArrayAdapter<String> petAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, ((OnInitLoaded) viewState).getPetNames());
                mAutoCompleteTextViewPets.setAdapter(petAdapter);

            } else if (viewState instanceof Error) {
                showToast(((Error) viewState).getErrorMessage(), Toast.LENGTH_SHORT);
            } else if (viewState instanceof ChangeServicesList) {
                mAutoCompleteTextViewServices.setText(null);
                ArrayAdapter<String> servicesAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        ((ChangeServicesList) viewState).getServices());
                mAutoCompleteTextViewServices.setAdapter(servicesAdapter);
            } else if (viewState instanceof FormError) {
                FormError error = (FormError) viewState;

                if (error.getServiceTypeError() != null) {
                    showToast(error.getServiceTypeError(), Toast.LENGTH_SHORT);
                }

                if (error.getServicesError() != null) {
                    mTextViewServiceDuration.setVisibility(View.GONE);
                }
                mInputLayoutServices.setError(error.getServicesError());
                mInputLayoutPets.setError(error.getPetError());
                mInputLayoutAppointmentDate.setError(error.getAppointmentDateError());

                if (error.getTimeSlotsError() != null) {
                    showToast(error.getTimeSlotsError(), Toast.LENGTH_SHORT);
                }
            } else if (viewState instanceof SetAvailableTimeSlots) {
                ArrayList<String> timeSlots = ((SetAvailableTimeSlots) viewState).getTimeSlots();
                Set<String> availableSlots = new LinkedHashSet<>(timeSlots);
                mChipGroupTimeSlots.removeAllViews();
                for (String timeSlot : availableSlots) {
                    Chip chip = (Chip) this.getLayoutInflater().inflate(R.layout.item_chip_slot, null, false);
                    chip.setText(timeSlot);
                    int paddingDp = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10,
                            getResources().getDisplayMetrics()
                    );
                    chip.setPadding(paddingDp, 0, paddingDp, 0);
                    chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    });
                    mChipGroupTimeSlots.addView(chip);
                }
                if (timeSlots.size() > 0) {
                    mTextViewNoSlotsAvailable.setVisibility(View.GONE);
                } else {
                    mTextViewNoSlotsAvailable.setVisibility(View.VISIBLE);
                }
            } else if (viewState instanceof SetServiceDuration) {
                mTextViewServiceDuration.setText(((SetServiceDuration) viewState).getDuration());
                mTextViewServiceDuration.setVisibility(View.VISIBLE);
            }
        });

        mViewModel.getActionState().observe(this, actionState -> {
            if (actionState instanceof ProceedToSubmitAppointment) {

            }
        });
    }

}