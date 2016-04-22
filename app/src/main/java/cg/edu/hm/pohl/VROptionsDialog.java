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

import ba.pohl1.hm.edu.vrlibrary.base.CGOptions;
import ba.pohl1.hm.edu.vrlibrary.model.observer.VRObservable;
import ba.pohl1.hm.edu.vrlibrary.navigation.ArrowNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.ArrowTapNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.GameControllerNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.LockedArrowNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.LockedArrowTapNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.TapNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRNavigator;

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
    private Spinner navigatorChooser;
    private Switch focusingSwitch;
    private Switch collisionSwitch;
    private Switch cullingSwitch;
    private Spinner cullingChooser;
    private Switch frontFaceSwitch;
    private EditText animationSpeedText;
    private EditText cameraSpeedText;
    private VRObservable<Class<? extends VRNavigator>> navigatorClassProperty = new VRObservable<>();

    public VROptionsDialog(Context context) {
        super(context);
        setContentView(R.layout.options_dialog);
        setTitle("Options");
        setCancelable(true);

        navigatorChooser = (Spinner) findViewById(R.id.navigatorChooser);
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

    @Override
    public void show() {
        navigatorChooser.setSelection(navigatorClassToPos(CGOptions.NAVIGATOR));
        super.show();
    }

    public VRObservable<Class<? extends VRNavigator>> getNavigatorClassProperty() {
        return navigatorClassProperty;
    }

    private void updateText() {
        focusingSwitch.setText(focusingSwitch.isChecked() ? TEXT_ENABLED : TEXT_DISABLED);
        collisionSwitch.setText(collisionSwitch.isChecked() ? TEXT_ENABLED : TEXT_DISABLED);
        cullingSwitch.setText(cullingSwitch.isChecked() ? TEXT_ENABLED : TEXT_DISABLED);
        frontFaceSwitch.setText(frontFaceSwitch.isChecked() ? TEXT_CCW : TEXT_CW);
    }

    private void initOptionsListeners() {
        navigatorChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final Class<? extends VRNavigator> navClass = posToNavigatorClass(position);
                navigatorClassProperty.set(navClass);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        focusingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CGOptions.FOCUSING_ENABLED = isChecked;
                updateText();
            }
        });
        collisionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CGOptions.COLLISION_ENABLED = isChecked;
                updateText();
            }
        });
        cullingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CGOptions.CULLING_ENABLED = isChecked;
                updateText();
            }
        });
        cullingChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final Object item = parent.getItemAtPosition(position);
                if(Objects.equals(item, "Front")) {
                    CGOptions.CULLING_MODE = GLES20.GL_FRONT;
                } else if(Objects.equals(item, "Back")) {
                    CGOptions.CULLING_MODE = GLES20.GL_BACK;
                } else {
                    CGOptions.CULLING_MODE = GLES20.GL_FRONT_AND_BACK;
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
                    CGOptions.FRONT_FACE = GLES20.GL_CCW;
                } else {
                    CGOptions.FRONT_FACE = GLES20.GL_CW;
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
                    CGOptions.ANIMATION_SPEED_MODIFIER = Float.valueOf(s.toString());
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
                    CGOptions.CAMERA_SPEED_MODIFIER = Float.valueOf(s.toString());
                }
            }
        });
    }

    private void initOptions() {
        navigatorChooser.setAdapter(createFromResource(getContext(), R.array.navigator_array, R.layout.support_simple_spinner_dropdown_item));
        focusingSwitch.setChecked(CGOptions.FOCUSING_ENABLED);
        collisionSwitch.setChecked(CGOptions.COLLISION_ENABLED);
        cullingSwitch.setChecked(CGOptions.CULLING_ENABLED);
        cullingChooser.setAdapter(createFromResource(getContext(), R.array.culling_array, R.layout.support_simple_spinner_dropdown_item));
        cullingChooser.setSelection(cullingModeToPos(CGOptions.CULLING_MODE));
        frontFaceSwitch.setChecked(CGOptions.FRONT_FACE == GLES20.GL_CCW);
        animationSpeedText.setText(Float.toString(CGOptions.ANIMATION_SPEED_MODIFIER));
        cameraSpeedText.setText(Float.toString(CGOptions.CAMERA_SPEED_MODIFIER));
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

    private int navigatorClassToPos(final Class<? extends VRNavigator> clazz) {
        if(Objects.equals(clazz, TapNavigator.class)) {
            return 0;
        } else if(Objects.equals(clazz, ArrowNavigator.class)) {
            return 1;
        } else if(Objects.equals(clazz, ArrowTapNavigator.class)) {
            return 2;
        } else if(Objects.equals(clazz, LockedArrowNavigator.class)) {
            return 3;
        } else if(Objects.equals(clazz, LockedArrowTapNavigator.class)) {
            return 4;
        } else if(Objects.equals(clazz, GameControllerNavigator.class)) {
            return 5;
        }
        return -1;
    }

    private Class<? extends VRNavigator> posToNavigatorClass(final int pos) {
        if(pos == 0) {
            return TapNavigator.class;
        } else if(pos == 1) {
            return ArrowNavigator.class;
        } else if(pos == 2) {
            return ArrowTapNavigator.class;
        } else if(pos == 3) {
            return LockedArrowNavigator.class;
        } else if(pos == 4) {
            return LockedArrowTapNavigator.class;
        } else if(pos == 5) {
            return GameControllerNavigator.class;
        }
        return null;
    }
}
