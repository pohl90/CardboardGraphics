package cg.edu.hm.pohl;

import android.os.Bundle;

import ba.pohl1.hm.edu.vrlibrary.base.Shader;
import ba.pohl1.hm.edu.vrlibrary.base.manager.CardboardGraphics;
import ba.pohl1.hm.edu.vrlibrary.model.VRComponent;
import ba.pohl1.hm.edu.vrlibrary.navigation.arrow.LockedArrowTapNavigator;
import ba.pohl1.hm.edu.vrlibrary.ui.AbstractCardboardActivity;
import ba.pohl1.hm.edu.vrlibrary.ui.hud.VRHud;
import cg.edu.hm.pohl.student.StudentScene;

/**
 * Created by Pohl on 14.04.2016.
 */
public class CardboardGraphicsActivity extends AbstractCardboardActivity {

    public static Shader studentSceneShader;

    private VRRoom vrRoom;
    private StudentScene studentScene;
    private VRHud overlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overlay = (VRHud) findViewById(R.id.hud_overlay);
    }

    @Override
    protected int getCardboardViewId() {
        return R.id.cardboard_view;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.student_scene;
    }

    @Override
    protected float getMoveModifier() {
        return 0.035f;
    }

    @Override
    protected VRComponent createScene() {
        vrRoom = new VRRoom();
        studentScene = new StudentScene();
        CardboardGraphics.camera.translateY(1.25f);
        CardboardGraphics.camera.setCanMoveInY(false);
        vrRoom.add(studentScene);
        setNavigator(new LockedArrowTapNavigator());
        return vrRoom;
    }

    @Override
    protected void initShaders() {
        super.initShaders();
        studentSceneShader = new Shader(R.raw.vertex, R.raw.fragment);
    }

    @Override
    public void onRendererShutdown() {
        super.onRendererShutdown();
        studentSceneShader.dispose();
    }

    @Override
    protected void onDestroy() {
        vrRoom.dispose();
        studentScene.dispose();
        super.onDestroy();
    }
}
