package com.example.tateti_distribuidas;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText name;
    private Switch btnSwitch;
    private String enabledMachine = "disable";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        name = findViewById(R.id.TextPlayerName);
        radioGroup = findViewById(R.id.radioGroup);
        btnSwitch = findViewById(R.id.btnSwitch);

        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    enabledMachine = "enable";
                }else{
                    enabledMachine = "disable";
                }
            }
        });


        Button start = findViewById(R.id.btnStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = name.getText().toString();
                String radioBtnSelected = checkButton(v);

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.putExtra("playerName",playerName);
                intent.putExtra("draw",radioBtnSelected);
                intent.putExtra("enabledMachine",enabledMachine);
                startActivity(intent);
            }
        });
    }

    public String checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        return (radioButton.getText().toString());
    }
}