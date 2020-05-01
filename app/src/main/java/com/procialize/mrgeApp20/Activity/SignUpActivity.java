package com.procialize.mrgeApp20.Activity;

import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.procialize.mrgeApp20.R;

public class SignUpActivity extends AppCompatActivity {

    final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextInputLayout input_layout_email, input_layout_firstname, input_layoutlastname,
            input_layoutdesignation, input_layout_company, input_layout_city, input_layout_contact;
    TextInputEditText input_email, input_firstname, input_lastname, input_designation,
            input_company, input_city, input_mobile;
    Button signupbtn;
    ImageView profileIV;
    boolean emailbool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


        initializeView();


        input_email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


                if (s.toString().matches(emailPattern) && s.length() > 0) {
                    input_layout_email.setError(null);
                    emailbool = true;
                } else {
                    input_layout_email.setError("Please Enter Valid Email Id");
                    emailbool = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Firstname = input_firstname.getText().toString();
                String Lastname = input_lastname.getText().toString();
                String Designation = input_designation.getText().toString();
                String CompanyName = input_company.getText().toString();
                String City = input_city.getText().toString();
                String Mobile = input_mobile.getText().toString();


                if (emailbool == false) {
                    input_layout_email.setError("Enter Valid Email Id");
                    input_layout_firstname.setError(null);
                    input_layoutlastname.setError(null);
                    input_layoutdesignation.setError(null);
                    input_layout_company.setError(null);
                    input_layout_city.setError(null);
                    input_layout_contact.setError(null);

                } else if (Firstname.equals("")) {
                    input_layout_firstname.setError("Enter First Name");
                    input_layout_email.setError(null);
                    input_layoutlastname.setError(null);
                    input_layoutdesignation.setError(null);
                    input_layout_company.setError(null);
                    input_layout_city.setError(null);
                    input_layout_contact.setError(null);

                } else if (Lastname.equals("")) {
                    input_layoutlastname.setError("Enter Last Name");

                    input_layout_email.setError(null);
                    input_layout_firstname.setError(null);
                    input_layoutdesignation.setError(null);
                    input_layout_company.setError(null);
                    input_layout_city.setError(null);
                    input_layout_contact.setError(null);

                } else if (Designation.equals("")) {
                    input_layoutdesignation.setError("Enter Designation");

                    input_layout_email.setError(null);
                    input_layout_firstname.setError(null);
                    input_layoutlastname.setError(null);
                    input_layout_company.setError(null);
                    input_layout_city.setError(null);
                    input_layout_contact.setError(null);

                } else if (CompanyName.equals("")) {
                    input_layout_company.setError("Enter Company Name");

                    input_layout_email.setError(null);
                    input_layout_firstname.setError(null);
                    input_layoutlastname.setError(null);
                    input_layoutdesignation.setError(null);
                    input_layout_city.setError(null);
                    input_layout_contact.setError(null);

                } else if (City.equals("")) {
                    input_layout_city.setError("Enter City Name");

                    input_layout_email.setError(null);
                    input_layout_firstname.setError(null);
                    input_layoutlastname.setError(null);
                    input_layoutdesignation.setError(null);
                    input_layout_company.setError(null);
                    input_layout_contact.setError(null);
                } else if (Mobile.equals("")) {
                    input_layout_contact.setError("Enter Mobile Number");

                    input_layout_email.setError(null);
                    input_layout_firstname.setError(null);
                    input_layoutlastname.setError(null);
                    input_layoutdesignation.setError(null);
                    input_layout_company.setError(null);
                    input_layout_city.setError(null);
                } else {

                }
            }
        });
    }

    private void initializeView() {

        input_layout_email = findViewById(R.id.input_layout_email);
        input_layout_firstname = findViewById(R.id.input_layout_firstname);
        input_layoutlastname = findViewById(R.id.input_layoutlastname);
        input_layoutdesignation = findViewById(R.id.input_layoutdesignation);
        input_layout_company = findViewById(R.id.input_layout_company);
        input_layout_city = findViewById(R.id.input_layout_city);
        input_layout_contact = findViewById(R.id.input_layout_contact);


        input_email = findViewById(R.id.input_email);
        input_firstname = findViewById(R.id.input_firstname);
        input_lastname = findViewById(R.id.input_lastname);
        input_designation = findViewById(R.id.input_designation);
        input_company = findViewById(R.id.input_company);
        input_city = findViewById(R.id.input_city);
        input_mobile = findViewById(R.id.input_mobile);

        signupbtn = findViewById(R.id.signupbtn);

        profileIV = findViewById(R.id.profileIV);
    }

    @Override
    protected void onResume() {
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }
}
