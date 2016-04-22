package cg.edu.hm.pohl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import ba.pohl1.hm.edu.vrlibrary.base.CGOptions;
import ba.pohl1.hm.edu.vrlibrary.base.Shader;
import ba.pohl1.hm.edu.vrlibrary.base.manager.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.base.manager.CollisionManager;
import ba.pohl1.hm.edu.vrlibrary.base.manager.RendererManager;
import ba.pohl1.hm.edu.vrlibrary.base.manager.TransformationManager;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.model.observer.VRObservable;
import ba.pohl1.hm.edu.vrlibrary.model.observer.VRObserver;
import ba.pohl1.hm.edu.vrlibrary.navigation.TapNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRDrawableNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRNavigator;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusManager;
import cg.edu.hm.pohl.student.StudentScene;

import static ba.pohl1.hm.edu.vrlibrary.util.CGConstants.Z_FAR;
import static ba.pohl1.hm.edu.vrlibrary.util.CGConstants.Z_NEAR;

/**
 * Created by Pohl on 14.04.2016.
 */
public class AbstractCardboadActivity extends CardboardActivity implements CardboardView.StereoRenderer{

    public static Shader studentSceneShader;

    private VRRoom vrRoom;
    private StudentScene studentScene;

    private VROptionsDialog optionsDialog;

    private VRObservable<VRNavigator> navigatorProperty = new VRObservable<>();
    private VRObserver<VRNavigator> navigatorChangeListener;
    private VRObserver<Class<? extends VRNavigator>> navigatorTypeChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.student_scene);

        CardboardGraphics.cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        CardboardGraphics.cardboardView.setRenderer(this);
        CardboardGraphics.cardboardView.setSettingsButtonEnabled(false);
        setCardboardView(CardboardGraphics.cardboardView);

        CollisionManager.getInstance().init(.5f, 1f, .5f, 400, 100, 400);
        CardboardGraphics.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initListeners();
        setNavigator(new TapNavigator());
    }

    @Override
    public void onCardboardTrigger() {
        if(navigatorProperty.isPresent()) {
            getNavigator().onCardboardTrigger();
        }
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        CardboardGraphics.camera.updateCamera(headTransform);
        getNavigator().navigate(CardboardGraphics.camera, CGOptions.CAMERA_SPEED_MODIFIER * 0.025f);
        TransformationManager.getInstance().applyTransformations(1);
    }

    @Override
    public void onDrawEye(Eye eye) {
        // Setup GL options
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        if(CGOptions.CULLING_ENABLED) {
            GLES30.glEnable(GLES30.GL_CULL_FACE);
        } else {
            GLES30.glDisable(GLES30.GL_CULL_FACE);
        }
        GLES30.glCullFace(CGOptions.CULLING_MODE);
        GLES30.glFrontFace(CGOptions.FRONT_FACE);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        // Update camera and light
        CardboardGraphics.camera.updateCamera(eye);

        // Retrieve projection matrix from eye
        float[] projection = eye.getPerspective(Z_NEAR, Z_FAR);

        // Update focus recognition
        FocusManager.getInstance().updateFocusTargets();

        // Draw all the objects
        RendererManager.getInstance().render(CardboardGraphics.camera.getView(), projection);
        if(getNavigator() instanceof VRDrawableNavigator) {
            ((VRDrawableNavigator) getNavigator()).onDraw(CardboardGraphics.camera.getView(), projection);
        }
    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        CardboardGraphics.context = getApplicationContext();
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        studentSceneShader = new Shader(R.raw.vertex, R.raw.fragment);
        CardboardGraphics.gridShader = new Shader(R.raw.grid_vertex, R.raw.grid_fragment);
        CardboardGraphics.colorShader = new Shader(R.raw.color_vertex, R.raw.color_fragment);

        CardboardGraphics.camera = new VRCamera();
        CardboardGraphics.camera.translateY(1.2f);
        CardboardGraphics.camera.setCanMoveInY(false);
        vrRoom = new VRRoom();
        studentScene = new StudentScene();
    }

    @Override
    public void onRendererShutdown() {
        CardboardGraphics.colorShader.dispose();
        CardboardGraphics.gridShader.dispose();
    }

    @Override
    protected void onDestroy() {
        vrRoom.dispose();
        studentScene.dispose();
        FocusManager.getInstance().dispose();
        CollisionManager.getInstance().dispose();
        RendererManager.getInstance().dispose();
        removeListeners();
        super.onDestroy();
    }

    public VRNavigator getNavigator() {
        return navigatorProperty.get();
    }

    /**
     * Sets the {@link VRNavigator}.
     *
     * @param navigator the navigator to use
     */
    public void setNavigator(final VRNavigator navigator) {
        navigatorProperty.set(navigator);
    }

    /**
     * Opens the options dialog over the given view.
     *
     * @param view the view which opens the dialog
     */
    public void showOptions(final View view) {
        if(optionsDialog == null) {
            optionsDialog = new VROptionsDialog(view.getContext());
        }
        optionsDialog.show();
    }

    private VROptionsDialog getOptionsDialog() {
        if(optionsDialog == null) {
            optionsDialog = new VROptionsDialog(this);
        }
        return optionsDialog;
    }

    private void initListeners() {
        navigatorChangeListener = new VRObserver<VRNavigator>() {
            @Override
            public void onChange(VRNavigator oldValue, VRNavigator newValue) {
                if(oldValue != null) {
                    oldValue.dispose();
                }
                CGOptions.NAVIGATOR = newValue != null ? newValue.getClass() : null;
            }
        };
        navigatorProperty.addListener(navigatorChangeListener);
        navigatorTypeChangeListener = new VRObserver<Class<? extends VRNavigator>>() {
            @Override
            public void onChange(Class<? extends VRNavigator> oldValue, Class<? extends VRNavigator> newValue) {
                if(newValue != null) {
                    try {
                        setNavigator(newValue.newInstance());
                    } catch(Exception e) {
                        Log.e("", e.getMessage());
                    }
                }
            }
        };
        getOptionsDialog().getNavigatorClassProperty().addListener(navigatorTypeChangeListener);
    }

    private void removeListeners() {
        navigatorProperty.removeListener(navigatorChangeListener);
        getOptionsDialog().getNavigatorClassProperty().removeListener(navigatorTypeChangeListener);
    }
}
