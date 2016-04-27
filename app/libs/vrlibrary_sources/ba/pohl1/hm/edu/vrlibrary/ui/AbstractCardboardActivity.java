package ba.pohl1.hm.edu.vrlibrary.ui;

import android.content.Context;
import android.opengl.GLES30;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import ba.pohl1.hm.edu.vrlibrary.R;
import ba.pohl1.hm.edu.vrlibrary.model.VRCamera;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.model.observer.VRObservable;
import ba.pohl1.hm.edu.vrlibrary.model.observer.VRObserver;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRDrawableNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.VRNavigator;
import ba.pohl1.hm.edu.vrlibrary.navigation.gamecontroller.GameControllerNavigator;
import ba.pohl1.hm.edu.vrlibrary.physics.CollisionManager;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusManager;
import ba.pohl1.hm.edu.vrlibrary.rendering.RendererManager;
import ba.pohl1.hm.edu.vrlibrary.rendering.TransformationManager;
import ba.pohl1.hm.edu.vrlibrary.rendering.instancing.InstancedRendererManager;
import ba.pohl1.hm.edu.vrlibrary.util.CGConstants;
import ba.pohl1.hm.edu.vrlibrary.util.CGOptions;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.util.FPSTimer;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;
import ba.pohl1.hm.edu.vrlibrary.util.Timer;
import ba.pohl1.hm.edu.vrlibrary.util.UIUtils;

import static ba.pohl1.hm.edu.vrlibrary.util.CGConstants.Z_FAR;
import static ba.pohl1.hm.edu.vrlibrary.util.CGConstants.Z_NEAR;

/**
 * An abstract {@link CardboardActivity} which cares about important steps for
 * creating a custom scene for a virtual reality mobile application.
 * <p>Subclasses must implement following methods
 * <ul>
 * <li>{@link #getContentViewId()}</li>
 * <li>{@link #getCardboardViewId()}</li>
 * <li>{@link #createScene()}</li>
 * </ul>
 * </p>
 * <p>
 * Created by Pohl on 27.02.2016.
 */
public abstract class AbstractCardboardActivity extends CardboardActivity implements CardboardView.StereoRenderer, FPSTimer.FPSChangedListener {

    private static final String TAG = "CardboardActivity";

    private VRObservable<VRNavigator> navigatorProperty = new VRObservable<>();
    private VRObserver<VRNavigator> navigatorChangeListener;
    private VRObserver<Class<? extends VRNavigator>> navigatorTypeChangeListener;

    private VROptionsDialog optionsDialog;
    private FPSTimer timer = new FPSTimer();
    private Timer uiUpdateTimer = new Timer();

    private VRComponent scene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        CardboardGraphics.cardboardView = (CardboardView) findViewById(getCardboardViewId());
        CardboardGraphics.cardboardView.setRenderer(this);
        CardboardGraphics.cardboardView.setSettingsButtonEnabled(false);
        setCardboardView(CardboardGraphics.cardboardView);

        CollisionManager.getInstance().init(.5f, 1f, .5f, 400, 100, 400);
        timer.addFPSChangedListener(this);
        CardboardGraphics.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        initListeners();

        CGUtils.checkGLError(TAG, "onCreate");
    }

    @Override
    protected void onDestroy() {
        scene.dispose();
        removeListeners();
        FocusManager.getInstance().dispose();
        CollisionManager.getInstance().dispose();
        RendererManager.getInstance().dispose();
        InstancedRendererManager.getInstance().dispose();
        TransformationManager.getInstance().dispose();
        CGUtils.clearTextures();
        super.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(getNavigator() instanceof GameControllerNavigator) {
            ((GameControllerNavigator) getNavigator()).controllerEvent(event);
        }
        return super.dispatchKeyEvent(event);
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
        getOptionsDialog().show();
    }

    private VROptionsDialog getOptionsDialog() {
        if(optionsDialog == null) {
            optionsDialog = new VROptionsDialog(this);
        }
        return optionsDialog;
    }

    /**
     * This is called before every rendering call of {@link #onDrawEye(Eye)}. The given {@link HeadTransform}
     * represents the transformation of the smartphone itself. (Direction, Pitch, Yaw, Roll)
     *
     * @param headTransform the head for the smartphone's transformations
     */
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        timer.refresh();
        CardboardGraphics.camera.updateCamera(headTransform);
        if(navigatorProperty.isPresent()) {
            getNavigator().navigate(CardboardGraphics.camera, getMoveModifier() * timer.getDelta());
        }
        TransformationManager.getInstance().applyTransformations(timer.getDelta());
        InstancedRendererManager.getInstance().updateInstancedModels();
        CGUtils.checkGLError(TAG, "onNewFrame");
    }

    /**
     * Main method for draw actions. Provides the {@link Eye} to update the ViewModel.
     *
     * @param eye the eye to update the ViewModel
     */
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
        //light.update();

        // Retrieve projection matrix from eye
        float[] projection = eye.getPerspective(Z_NEAR, Z_FAR);

        // Update focus recognition
        FocusManager.getInstance().updateFocusTargets();

        // Draw all the objects
        RendererManager.getInstance().render(CardboardGraphics.camera.getView(), projection);
        InstancedRendererManager.getInstance().drawInstanced(CardboardGraphics.camera.getView(), projection);
        if(getNavigator() instanceof VRDrawableNavigator) {
            ((VRDrawableNavigator) getNavigator()).onDraw(CardboardGraphics.camera.getView(), projection);
        }
        CGUtils.checkGLError(TAG, "onDrawEye");
    }


    /**
     * This method is called whenever the cardboard navigate is engaged or the user
     * touches the screen.
     */
    @Override
    public void onCardboardTrigger() {
        if(navigatorProperty.isPresent()) {
            getNavigator().onCardboardTrigger();
        }
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        CardboardGraphics.context = getApplicationContext();
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        GLES30.glClearDepthf(100f);

        initShaders();

        CardboardGraphics.camera = new VRCamera();

        //light = new Light();
        scene = createScene();
        timer.reset();
        uiUpdateTimer.reset();

        if(navigatorProperty.isPresent() && CardboardGraphics.hasHUD()) {
            UIUtils.runInUIThread(new Runnable() {
                @Override
                public void run() {
                    CardboardGraphics.hud.show3DToast(getNavigator().getToast(), CGConstants.NAVIGATOR_TOAST_DURATION);
                }
            });
        }
        Log.i(TAG, "onSurfaceCreated");
    }

    @Override
    public void fpsUpdate(final int fps) {
        // Subclasses may override this to get the current fps
    }

    /**
     * Updates the user interface stuff if necessary. Subclasses don't need to care for the ui thread.
     */
    protected void updateUI() {
        // Intended to be empty
    }

    /**
     * Gets the modifier which controls how fast the camera moves.
     *
     * @return the move modifier for the camera
     */
    protected float getMoveModifier() {
        return CGConstants.MOVE_MODIFIER;
    }

    /**
     * Gets the content layout for this activity. The layout must define a cardboard view use
     * in {@link #getCardboardViewId()}.
     *
     * @return the layout for this activity
     */
    protected abstract int getContentViewId();

    /**
     * Gets the id for the cardboard view.
     *
     * @return the id for the cardboard view
     */
    protected abstract int getCardboardViewId();

    /**
     * Creates the main scene.
     *
     * @return the scene as a {@link VRComponent}
     */
    protected abstract VRComponent createScene();


    @Override
    public void onFinishFrame(Viewport viewport) {
        if(uiUpdateTimer.getElapsedTime() > 500L) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUI();
                }
            });
            uiUpdateTimer.reset();
        }
    }

    @Override
    public void onSurfaceChanged(int i, int i1) {
        // Don't need this
    }

    @Override
    public void onRendererShutdown() {
        CardboardGraphics.colorShader.dispose();
        CardboardGraphics.instancedColorShader.dispose();
        CardboardGraphics.instancedTextureShader.dispose();
        CardboardGraphics.gridShader.dispose();
        CardboardGraphics.textureShader.dispose();
        CardboardGraphics.crosshairShader.dispose();
    }

    /**
     * Initializes the shaders.
     */
    protected void initShaders() {
        CardboardGraphics.colorShader = new Shader(R.raw.color_vertex_shader, R.raw.color_fragment_shader);
        CardboardGraphics.instancedColorShader = new Shader(R.raw.instanced_color_vertex, R.raw.instanced_color_fragment);
        CardboardGraphics.instancedTextureShader = new Shader(R.raw.instanced_texture_vertex, R.raw.instanced_texture_fragment);
        CardboardGraphics.gridShader = new Shader(R.raw.grid_vertex, R.raw.grid_fragment);
        CardboardGraphics.textureShader = new Shader(R.raw.texture_light_vertex, R.raw.texture_light_fragment);
        CardboardGraphics.crosshairShader = new Shader(R.raw.crosshair_vertex, R.raw.crosshair_fragment);
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
                        if(navigatorProperty.isPresent() && CardboardGraphics.hasHUD()) {
                            CardboardGraphics.hud.show3DToast(getNavigator().getToast(), CGConstants.NAVIGATOR_TOAST_DURATION);
                        }
                    } catch(Exception e) {
                        Log.e(TAG, e.getMessage());
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
