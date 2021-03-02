package com.example.cbr_manager.ui.createreferral;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cbr_manager.R;
import com.example.cbr_manager.service.APIService;
import com.example.cbr_manager.service.referral.Referral;
import com.example.cbr_manager.service.referral.ServiceDetails.OrthoticServiceDetail;
import com.example.cbr_manager.service.referral.ServiceDetails.OtherServiceDetail;
import com.example.cbr_manager.service.referral.ServiceDetails.PhysiotherapyServiceDetail;
import com.example.cbr_manager.service.referral.ServiceDetails.ProstheticServiceDetail;
import com.example.cbr_manager.service.referral.ServiceDetails.ServiceDetail;
import com.example.cbr_manager.service.referral.ServiceDetails.WheelchairServiceDetail;
import com.example.cbr_manager.service.user.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateReferralActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 102;
    static final int REQUEST_CAMERA_USE = 101;
    int clientId = -1;
    private Integer userId = -1;
    private APIService apiService = APIService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_referral);
        clientId = getIntent().getIntExtra("CLIENT_ID", -1);
        setTitle("Create Referral");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getUserId();
        setupReferralServiceRadioGroup();
        setupPhysioLayout();
        setupWheelchairLayout();
        setupSubmission();
        setupCameraButtonListener();
    }

    private void getUserId() {
        if (apiService.isAuthenticated()) {
            apiService.userService.getCurrentUser().enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();
                        userId = user.getId();
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }

    private void setupCameraButtonListener() {
        Button cameraButton = findViewById(R.id.referralTakePhotoButton);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_USE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_USE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView referralImageView = findViewById(R.id.referralImageView);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            referralImageView.setImageBitmap(imageBitmap);
        }
    }

    private void setupSubmission() {
        Button submitButton = findViewById(R.id.referralSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gatherData();
//                onBackPressed();
            }
        });
    }

    private void gatherData() {
        RadioGroup selectedService = findViewById(R.id.createReferralServiceRadioGroup);
        int service = selectedService.getCheckedRadioButtonId();
        Referral referral = new Referral();

        if (service == R.id.referralPhysioRadioButton) {
            PhysiotherapyServiceDetail physiotherapyServiceDetail = new PhysiotherapyServiceDetail();
            String clientCondition;

            TextInputEditText physioOtherCondition = findViewById(R.id.referralOtherPhysio);

            String otherConditionDescription = "";
            Spinner referralPhysioDDL = findViewById(R.id.referralPhysioDDL);
            clientCondition = referralPhysioDDL.getSelectedItem().toString();

            if (clientCondition.equals("Other")) {
                otherConditionDescription = physioOtherCondition.getText().toString();
                physiotherapyServiceDetail.setOther_description(otherConditionDescription);
            }
            physiotherapyServiceDetail.setCondition(clientCondition);

            referral.setServiceDetail(physiotherapyServiceDetail);
            referral.setServiceType("Physiotherapy");

        } else if (service == R.id.referralProstheticRadioButton) {
            ProstheticServiceDetail prostheticServiceDetail = new ProstheticServiceDetail();
            RadioGroup aboveOrBelowKnee = findViewById(R.id.referralAboveOrBelowKnee);
            int getSelectedId = aboveOrBelowKnee.getCheckedRadioButtonId();
            RadioButton aboveOrBelow = (RadioButton) findViewById(getSelectedId);
            String getRadioText = "";
            if (aboveOrBelow != null) {
                getRadioText = aboveOrBelow.getText().toString();
            }

            prostheticServiceDetail.setKneeInjuryLocation(getRadioText);

            referral.setServiceDetail(prostheticServiceDetail);
            referral.setServiceType("Prosthetic");

        } else if (service == R.id.referralOrthoticRadioButton) {
            OrthoticServiceDetail orthoticServiceDetail = new OrthoticServiceDetail();
            RadioGroup aboveOrBelowElbow = findViewById(R.id.referralAboveOrBelowElbow);
            int getSelectedId = aboveOrBelowElbow.getCheckedRadioButtonId();
            RadioButton aboveOrBelow = (RadioButton) findViewById(getSelectedId);
            String getRadioText = "";

            if (aboveOrBelow != null) {
                getRadioText = aboveOrBelow.getText().toString();
            }

            orthoticServiceDetail.setElbowInjuryLocation(getRadioText);

            referral.setServiceDetail(orthoticServiceDetail);
            referral.setServiceType("Orthotic");

        } else if (service == R.id.referralWheelChairRadioButton) {
            WheelchairServiceDetail wheelchairServiceDetail = new WheelchairServiceDetail();
            String hipWidth;
            boolean isExisting = false;
            boolean isRepairable = false;

            TextInputEditText hipWidthEditText = findViewById(R.id.referralHipWidth);
            hipWidth = hipWidthEditText.getText().toString();
            wheelchairServiceDetail.setClientHipWidth(Float.parseFloat(hipWidth));

            RadioGroup usageExperience = findViewById(R.id.referralWheelChairUsageRadioGroup);
            if (usageExperience.getCheckedRadioButtonId() == R.id.referralWheelchairIntermediate) {
                wheelchairServiceDetail.setUsageExperience("Intermediate");
            } else {
                wheelchairServiceDetail.setUsageExperience("Basic");
            }

            RadioGroup isExistingWheelchair = findViewById(R.id.referralExistingWheelchairRadioGroup);
            if (isExistingWheelchair.getCheckedRadioButtonId() == R.id.referralExistingWheelchairYes) {
                isExisting = true;
            }

            RadioGroup isRepairableWheelchair = findViewById(R.id.referralCanRepairRadioGroup);
            if (isRepairableWheelchair.getCheckedRadioButtonId() == R.id.referralCanRepairYes) {
                isRepairable = true;
            }

            if (!hipWidth.isEmpty()) {
                wheelchairServiceDetail.setClientHipWidth(Float.parseFloat(hipWidth));
            }
            wheelchairServiceDetail.setClientHasExistingWheelchair(isExisting);
            wheelchairServiceDetail.setIsWheelChairRepairable(isRepairable);
            wheelchairServiceDetail.setUsageExperience("Basic");
            referral.setServiceDetail(wheelchairServiceDetail);
            referral.setServiceType("Wheelchair");

        } else if (service == R.id.referralOtherRadioButton) {
            OtherServiceDetail otherServiceDetail = new OtherServiceDetail();
            TextInputEditText otherServiceEditText = findViewById(R.id.referralOtherServiceDescription);
            String otherDescription = "";
            otherDescription = otherServiceEditText.getText().toString();
            otherServiceDetail.setDescription(otherDescription);

            referral.setServiceDetail(otherServiceDetail);
            referral.setServiceType("Other");
        }

        TextInputEditText referTo = findViewById(R.id.referralReferToEditText);
        String referToString = referTo.getText().toString();

        referral.setRefer_to(referToString);

        referral.setClient(new Integer(clientId));
        referral.setUserCreator(userId);
        if (apiService.isAuthenticated()) {
            Call<Referral> call = apiService.getReferralService().createReferral(referral);
            call.enqueue(new Callback<Referral>() {
                @Override
                public void onResponse(Call<Referral> call, Response<Referral> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CreateReferralActivity.this, "Referral successfully created!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(CreateReferralActivity.this, "Error creating referral.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Referral> call, Throwable t) {
                    Toast.makeText(CreateReferralActivity.this, "Failure connecting to server.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupWheelchairLayout() {
        RadioGroup existingChair = findViewById(R.id.referralExistingWheelchairRadioGroup);
        RadioGroup canRepair = findViewById(R.id.referralCanRepairRadioGroup);
        canRepair.setVisibility(View.GONE);

        TextView bringWheelChair = findViewById(R.id.referralBringWheelchairTextView);
        bringWheelChair.setVisibility(View.GONE);

        existingChair.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.referralExistingWheelchairYes) {
                    canRepair.setVisibility(View.VISIBLE);
                } else {
                    canRepair.setVisibility(View.GONE);
                    canRepair.clearCheck();
                    bringWheelChair.setVisibility(View.GONE);
                }
            }
        });

        canRepair.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.referralCanRepairYes) {
                    bringWheelChair.setVisibility(View.VISIBLE);
                } else {
                    bringWheelChair.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupPhysioLayout() {
        TextInputLayout referralPhysioOther = findViewById(R.id.referralPhysioOtherTextInputLayout);
        referralPhysioOther.setVisibility(View.GONE);
        Spinner spinner = findViewById(R.id.referralPhysioDDL);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("Other")) {
                    referralPhysioOther.setVisibility(View.VISIBLE);
                } else {
                    referralPhysioOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupReferralServiceRadioGroup() {
        TextInputLayout referralServiceOther = findViewById(R.id.referralDescribeOtherTextInputLayout);
        referralServiceOther.setVisibility(View.GONE);
        RadioGroup serviceRequired = findViewById(R.id.createReferralServiceRadioGroup);
        serviceRequired.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LinearLayout physioLayout = findViewById(R.id.referralPhysioLayout);
                LinearLayout prostheticLayout = findViewById(R.id.referralProstheticLayout);
                LinearLayout orthoticLayout = findViewById(R.id.referralOrthoticLayout);
                LinearLayout wheelchairLayout = findViewById(R.id.referralWheelchairLayout);

                if (checkedId == R.id.referralPhysioRadioButton) {
                    setAllGone();
                    physioLayout.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.referralProstheticRadioButton) {
                    setAllGone();
                    prostheticLayout.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.referralOrthoticRadioButton) {
                    setAllGone();
                    orthoticLayout.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.referralWheelChairRadioButton) {
                    setAllGone();
                    wheelchairLayout.setVisibility(View.VISIBLE);
                } else {
                    setAllGone();
                    referralServiceOther.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setAllGone() {
        LinearLayout physioLayout = findViewById(R.id.referralPhysioLayout);
        LinearLayout prostheticLayout = findViewById(R.id.referralProstheticLayout);
        LinearLayout orthoticLayout = findViewById(R.id.referralOrthoticLayout);
        LinearLayout wheelchairLayout = findViewById(R.id.referralWheelchairLayout);
        LinearLayout otherLayout = findViewById(R.id.referralOrthoticLayout);

        physioLayout.setVisibility(View.GONE);
        prostheticLayout.setVisibility(View.GONE);
        orthoticLayout.setVisibility(View.GONE);
        wheelchairLayout.setVisibility(View.GONE);
        otherLayout.setVisibility(View.GONE);

        TextInputLayout referralServiceOther = findViewById(R.id.referralDescribeOtherTextInputLayout);
        referralServiceOther.setVisibility(View.GONE);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}