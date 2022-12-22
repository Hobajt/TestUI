package com.example.testui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    private TextView login;
    private TextView pwd;

    private TextView dateText;
    private Button dateBtn;

    private CheckBox tos;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. M. yyyy", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (TextView)findViewById(R.id.login);
        pwd = (TextView)findViewById(R.id.pwd);
        tos = (CheckBox) findViewById(R.id.consent);

        dateText = (TextView) findViewById(R.id.dateText);
        dateBtn = (Button) findViewById(R.id.datePick);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalDate date = getDateFromText();

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                                setTextFromDate(d, m, y);
                            }
                        },
                        date.getYear(), date.getMonthValue()-1, date.getDayOfMonth()
                );

                datePickerDialog.show();
            }
        });

        final Calendar c = Calendar.getInstance();
        int yy = c.get(Calendar.YEAR);
        int mm = c.get(Calendar.MONTH);
        int dd = c.get(Calendar.DAY_OF_MONTH);
        setTextFromDate(dd, mm, yy);
    }

    private LocalDate getDateFromText() {
        return LocalDate.parse(dateText.getText(), formatter);
    }

    private void setTextFromDate(int day, int month, int year) {
        LocalDate date = LocalDate.of(year, month+1, day);
        dateText.setText(date.format(formatter));
        //dateText.setText(String.format("%d. %d. %d", day, month+1, year));
    }

    public void OnBtnClick(View view) {
        //validate login format (6 <= len <= 32)
        int len = login.getText().length();
        if(len < 6 || len > 32) {
            errMsg("Nickname must be between 6 and 32 characters long.");
            return;
        }

        //validate password format (8 < len < 32)
        len = pwd.getText().length();
        if(len < 8 || len > 32) {
            errMsg("Password must be between 8 and 32 characters long.");
            return;
        }
        //validate password format (both characters and numbers)
        boolean nums = pwd.getText().toString().replaceAll("[^0-9]", "").length() > 0;
        boolean chars = pwd.getText().toString().toLowerCase().replaceAll("[^a-z]", "").length() > 0;
        if(!(nums && chars)) {
            errMsg("Password must contain both numbers and characters.");
        }

        //validate birthdate (>= 18 yo)
        long age = ChronoUnit.YEARS.between(getDateFromText(), LocalDate.now());
        //System.out.println("AGE: " + age);
        if(age < 18) {
            errMsg("You must be 18 years old in order to register.");
            return;
        }

        //validate ToS checkbox
        if(!tos.isChecked()) {
            errMsg("Need to agree to Terms of Service.");
            return;
        }

        Intent intent = new Intent(getBaseContext(), ContentActivity.class);
        startActivity(intent);
    }

    private void errMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}