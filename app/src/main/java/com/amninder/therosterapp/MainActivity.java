package com.amninder.therosterapp;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    EditText name;
    Spinner eyeColor;
    Button pickDate, saveButton;
    TextView txtBirthday, txtPantSize, txtShirtSize, txtShoeSize;
    CheckBox chkbox;
    RadioGroup radioGroup;
    SeekBar pantSize, shirtSize, shoeSize;

    Calendar cal = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dp;
    int cDay = cal.get(Calendar.DAY_OF_MONTH), cMonth = cal.get(Calendar.MONTH), cYear = cal.get(Calendar.YEAR);

    String savedName, savedSize, savedEyeColor;
    int savedDay, savedMonth, savedYear, savedPantSize, savedShirtSize, savedShoeSize;
    Boolean savedCheckbox = false;

    SharedPreferences SP;
    SharedPreferences.Editor SPeditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------Centered Title-------------//
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        //-------------App Elements------------//
        name = (EditText) findViewById(R.id.name);
        eyeColor = (Spinner) findViewById(R.id.eyeColorSpinner);
        txtBirthday = (TextView) findViewById(R.id.txtBirthday);
        pickDate = (Button) findViewById(R.id.dateButton);
        chkbox = (CheckBox)findViewById(R.id.checkBox);
        saveButton = (Button)findViewById(R.id.saveButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        pantSize = (SeekBar)findViewById(R.id.pantSize);
        shirtSize = (SeekBar)findViewById(R.id.shirtSize);
        shoeSize = (SeekBar)findViewById(R.id.shoeSize);
        txtPantSize = (TextView)findViewById(R.id.txtPantSize);
        txtShirtSize = (TextView)findViewById(R.id.txtShirtSize);
        txtShoeSize = (TextView)findViewById(R.id.txtShoeSize);

        appStart();
        loadProfile();
    }

    protected void appStart() {

        //-------------Eye Color Spinner--------------//
        List<String> eyeColors = new ArrayList<>();
        eyeColors.add("Select");
        eyeColors.add("Hazel");
        eyeColors.add("Blue");
        eyeColors.add("Green");
        eyeColors.add("Black");
        eyeColors.add("Amber");
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, eyeColors);
        eyeColor.setAdapter(myAdapter);

        //-----------Birthday Picker----------//
        txtBirthday.setText(cDay + "/" + (cMonth + 1) + "/" + cYear);

        pickDate.setOnClickListener(this);

        dp = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                savedDay = day;
                savedMonth = month;
                savedYear = year;
                txtBirthday.setText(savedDay + "/" + (savedMonth + 1) + "/" + savedYear);
            }
        };

        //--------------Chechbox---------------//
        chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkbox.isChecked())
                    savedCheckbox = true;
                else
                    savedCheckbox = false;
            }
        });

        //-------------Radio Buttons--------------//
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                savedSize = String.valueOf(radioGroup.getCheckedRadioButtonId());
            }
        });

        //-------------Pant Size------------//

        pantSize.setOnSeekBarChangeListener(this);
        shirtSize.setOnSeekBarChangeListener(this);
        shoeSize.setOnSeekBarChangeListener(this);

        //--------------Save Button Click---------------//
        saveButton.setOnClickListener(this);
    }

    //------------Save Profile Data----------------//
    protected void savePrefs(){

        try {
            SPeditor.putString("name", savedName);
            SPeditor.putString("eyeColor", savedEyeColor);
            SPeditor.putInt("year",savedYear);
            SPeditor.putInt("month", savedMonth);
            SPeditor.putInt("day", savedDay);
            SPeditor.putBoolean("checkbox", savedCheckbox);
            SPeditor.putString("size", savedSize);
            SPeditor.putInt("pantSize", savedPantSize);
            SPeditor.putInt("shirtSize", savedShirtSize);
            SPeditor.putInt("shoeSize",savedShoeSize);

            SPeditor.apply();

            Toast.makeText(getApplication().getBaseContext(),"Profile Saved", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            System.out.print(e.toString());
            Toast.makeText(getApplication().getBaseContext(), e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    //------------Load Profile Data----------------//
    protected void loadProfile(){
        SP = getSharedPreferences("MyApp",MODE_PRIVATE);
        SPeditor = SP.edit();

        savedName = SP.getString("name", null);
        savedEyeColor = SP.getString("eyeColor",null);
        savedYear = SP.getInt("year", 0);
        savedMonth = SP.getInt("month", 0);
        savedDay = SP.getInt("day", 0);
        savedCheckbox = SP.getBoolean("checkbox", false);
        savedSize = SP.getString("size", null);
        savedPantSize =SP.getInt("pantSize", 0);
        savedShirtSize = SP.getInt("shirtSize",0);
        savedShoeSize = SP.getInt("shoeSize",0);

            name.setText(savedName);
            chkbox.setChecked(savedCheckbox);
            eyeColor.setSelection(Integer.decode(savedEyeColor));
            txtBirthday.setText(savedDay + "/" + (savedMonth + 1) + "/" + savedYear);
            radioGroup.check(Integer.decode(savedSize));
            pantSize.setProgress(savedPantSize);
            txtPantSize.setText(String.valueOf(savedPantSize));
            shirtSize.setProgress(savedShirtSize);
            txtShirtSize.setText(String.valueOf(savedShirtSize));
            shoeSize.setProgress(savedShoeSize - 4);
            txtShoeSize.setText(String.valueOf(savedShoeSize));

    }

    //--------------Seekbar progress change listener----------------//
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch(seekBar.getId()){

            case R.id.pantSize:
                savedPantSize = i;
                txtPantSize.setText(String.valueOf(savedPantSize));

                break;

            case R.id.shirtSize:
                savedShirtSize = i;
                txtShirtSize.setText(String.valueOf(savedShirtSize));

                break;

            case R.id.shoeSize:
                savedShoeSize = i + 4;
                txtShoeSize.setText(String.valueOf(savedShoeSize));

                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.dateButton:
                new DatePickerDialog(
                        MainActivity.this, dp, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                ).show();
                break;

            case R.id.saveButton:
                savedName = name.getText().toString();
                savedEyeColor = String.valueOf(eyeColor.getSelectedItemId());
                savedCheckbox = chkbox.isChecked();

                SP = getSharedPreferences("MyApp",MODE_PRIVATE);
                SPeditor = SP.edit();
                savePrefs();

                break;
        }
    }
}
