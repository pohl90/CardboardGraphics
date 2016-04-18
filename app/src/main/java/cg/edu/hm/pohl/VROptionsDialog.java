package cg.edu.hm.pohl;

import android.app.Dialog;
import android.content.Context;
import android.opengl.GLES20;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Objects;

import ba.pohl1.hm.edu.vrlibrary.base.Options;

import static android.widget.ArrayAdapter.createFromResource;

/**
 * Created by Pohl on 03.03.2016.
 */
public class VROptionsDialog extends Dialog {

    // Texts
    private static final String TEXT_ENABLED = "On";
    private static final String TEXT_DISABLED = "Off";
    private static final String TEXT_CCW = "CCW";
    private static final String TEXT_CW = "CW";

    // Widgets
    private Switch focusingSwitch;
    private Switch collisionSwitch;
    private Switch cullingSwitch;
    private Spinner cullingChooser;
    private Switch frontFaceSwitch;
    private EditText animationSpeedText;
    private EditText cameraSpeedText;

    public VROptionsDialog(Context context) {
        super(context);
        setContentView(R.layout.options_dialog);
        setTitle("Options");
        setCancelable(true);

        focusingSwitch = (Switch) findViewById(R.id.focusingSwitch);
        collisionSwitch = (Switch) findViewById(R.id.collisionSwitch);
        cullingSwitch = (Switch) findViewById(R.id.cullingSwitch);
        cullingChooser = (Spinner) findViewById(R.id.cullingChooser);
        frontFaceSwitch = (Switch) findViewById(R.id.frontFaceSwitch);
        animationSpeedText = (EditText) findViewById(R.id.animationSpeedText);
        cameraSpeedText = (EditText) findViewById(R.id.cameraSpeedText);
        initOptionsListeners();
        initOptions();
        updateText();
    }

    private void updateText() {
        focusingSwitch.setText(focusingSwitch.isChecked() ? TEXT_ENABLED : TEXT_DISABLED);
        collisionSwitch.setText(collisionSwitch.isChecked() ? TEXT_ENABLED : TEXT_DISABLED);
        cullingSwitch.setText(cullingSwitch.isChecked() ? TEXT_ENABLED : TEXT_DISABLED);
        frontFaceSwitch.setText(frontFaceSwitch.isChecked() ? TEXT_CCW : TEXT_CW);
    }

    private void initOptionsListeners() {
        focusingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Options.FOCUSING_ENABLED = isChecked;
                updateText();
            }
        });
        collisionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Options.COLLISION_ENABLED = isChecked;
                updateText();
            }
        });
        cullingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Options.CULLING_ENABLED = isChecked;
                updateText();
            }
        });
        cullingChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final Object item = parent.getItemAtPosition(position);
                if(Objects.equals(item, "Front")) {
                    Options.CULLING_MODE = GLES20.GL_FRONT;
                } else if(Objects.equals(item, "Back")) {
                    Options.CULLING_MODE = GLES20.GL_BACK;
                } else {
                    Options.CULLING_MODE = GLES20.GL_FRONT_AND_BACK;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        frontFaceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Options.FRONT_FACE = GLES20.GL_CCW;
                } else {
                    Options.FRONT_FACE = GLES20.GL_CW;
                }
                updateText();
            }
        });
        animationSpeedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    Options.ANIMATION_SPEED_MODIFIER = Float.valueOf(s.toString());
                }
            }
        });
        cameraSpeedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    Options.CAMERA_SPEED_MODIFIER = Float.valueOf(s.toString());
                }
            }
        });
    }

    private void initOptions() {
        focusingSwitch.setChecked(Options.FOCUSING_ENABLED);
        collisionSwitch.setChecked(Options.COLLISION_ENABLED);
        cullingSwitch.setChecked(Options.CULLING_ENABLED);
        cullingChooser.setAdapter(createFromResource(getContext(), R.array.culling_array, R.layout.support_simple_spinner_dropdown_item));
        cullingChooser.setSelection(cullingModeToPos(Options.CULLING_MODE));
        frontFaceSwitch.setChecked(Options.FRONT_FACE == GLES20.GL_CCW);
        animationSpeedText.setText(Float.toString(Options.ANIMATION_SPEED_MODIFIER));
        cameraSpeedText.setText(Float.toString(Options.CAMERA_SPEED_MODIFIER));
    }

    private int cullingModeToPos(final int id) {
        if(id == GLES20.GL_FRONT) {
            return 1;
        }
        if(id == GLES20.GL_BACK) {
            return 0;
        }
        return 2;
    }
}
