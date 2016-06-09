package ba.pohl1.hm.edu.vrlibrary.model;

import java.util.ArrayList;
import java.util.List;

import ba.pohl1.hm.edu.vrlibrary.maths.Matrix4x4;
import ba.pohl1.hm.edu.vrlibrary.model.data.GeometryData;
import ba.pohl1.hm.edu.vrlibrary.physics.CollisionManager;
import ba.pohl1.hm.edu.vrlibrary.physics.animation.AnimationHandler;
import ba.pohl1.hm.edu.vrlibrary.physics.focus.FocusManager;
import ba.pohl1.hm.edu.vrlibrary.rendering.ColorMaterial;
import ba.pohl1.hm.edu.vrlibrary.rendering.Material;
import ba.pohl1.hm.edu.vrlibrary.rendering.RendererManager;
import ba.pohl1.hm.edu.vrlibrary.rendering.TransformationManager;
import ba.pohl1.hm.edu.vrlibrary.rendering.instancing.InstanceType;
import ba.pohl1.hm.edu.vrlibrary.rendering.instancing.InstancedColorMaterial;
import ba.pohl1.hm.edu.vrlibrary.rendering.instancing.InstancedMaterial;
import ba.pohl1.hm.edu.vrlibrary.rendering.instancing.InstancedRendererManager;
import ba.pohl1.hm.edu.vrlibrary.rendering.instancing.InstancedTextureMaterial;
import ba.pohl1.hm.edu.vrlibrary.util.CGUtils;
import ba.pohl1.hm.edu.vrlibrary.util.Shader;


/**
 * A {@link VRObject} which can contain a {@link Material} and holds a {@link List}
 * of {@link VRComponent VRComponents}.
 * <p>
 * Created by Pohl on 26.02.2016.
 */
public class VRComponent extends VRCollider {

    private static final Matrix4x4 transformMatrix = new Matrix4x4();

    protected List<VRComponent> children;
    protected VRComponent parent;
    protected Material material;
    private GeometryData geometryData;

    private float[] color;

    @Override
    public void dispose() {
        remove(false);
        if(getPostAnimationHandler() != null) {
            getPostAnimationHandler().dispose();
        }
        if(getPreAnimationHandler() != null) {
            getPreAnimationHandler().dispose();
        }
    }

    /**
     * Gets the component's color.
     *
     * @return the color
     */
    public float[] getColor() {
        return color;
    }

    /**
     * Sets the color of the {@link VRComponent}. The color buffer of the currently set {@link GeometryData} is
     * being updated.
     *
     * @param color the new color
     */
    public void setColor(final float[] color) {
        this.color = color;
        if(geometryData != null) {
            CGUtils.fillColors(geometryData.getColorsArray(), color);
        }
        if(getMaterial() instanceof ColorMaterial) {
            ((ColorMaterial) getMaterial()).updateColorBuffer();
        } else if(getMaterial() instanceof InstancedMaterial) {
            getMaterial().setColorBufferDirty(true);
        }
    }

    /**
     * Gets the {@link GeometryData}.
     *
     * @return the geometry data
     */
    public GeometryData getGeometryData() {
        return geometryData;
    }

    /**
     * Sets the {@link GeometryData}.
     *
     * @param geometryData the geometry data
     */
    public void setGeometryData(GeometryData geometryData) {
        this.geometryData = geometryData;
    }

    /**
     * Applies a {@link ColorMaterial} to the component.
     *
     * @param shader the shader to set
     */
    public void applyAsColorMaterial(final Shader shader) {
        final ColorMaterial material = new ColorMaterial(shader, getGeometryData());
        setMaterial(material);
    }

    /**
     * Applies a {@link InstancedColorMaterial} to the component.
     *
     * @param shader the shader to set
     */
    public void applyAsInstancedColorMaterial(final Shader shader) {
        final Material instancedMaterial = new InstancedColorMaterial(shader, InstanceType.CUBE_COLORED, this, geometryData);
        instancedMaterial.setIsInstanced(true);
        setMaterial(instancedMaterial);
    }

    public void applyAsInstancedTextureMaterial(final Shader shader, final int resId) {
        final Material material = new InstancedTextureMaterial(shader, resId, InstanceType.CUBE_TEXTURED, this, geometryData);
        material.setIsInstanced(true);
        setMaterial(material);
    }

    @Override
    public void setPostAnimationHandler(AnimationHandler postAnimationHandler) {
        super.setPostAnimationHandler(postAnimationHandler);
        TransformationManager.getInstance().add(this);
    }

    @Override
    public void setPreAnimationHandler(AnimationHandler preAnimationHandler) {
        super.setPreAnimationHandler(preAnimationHandler);
        TransformationManager.getInstance().add(this);
    }

    /**
     * Gets the currently used {@link Material}. Can be null.
     *
     * @return the current material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the new {@link Material} to use.
     *
     * @param material the new material
     */
    public void setMaterial(Material material) {
        if(this.material != null && this.material.isInstanced()) {
            InstancedRendererManager.getInstance().unregisterInstancedMaterial((InstancedMaterial) this.material);
        }
        this.material = material;
        if(material == null) {
            RendererManager.getInstance().remove(this);
        } else if(material.isInstanced()) {
            InstancedRendererManager.getInstance().registerInstancedMaterial((InstancedMaterial) material);
        } else {
            RendererManager.getInstance().add(this);
        }
    }

    /**
     * Gets whether there is a {@link Material} currently set.
     *
     * @return {@code true} if there is a material
     */
    public boolean hasMaterial() {
        return material != null;
    }

    /**
     * Adds the vararg of {@link VRComponent VRComponents}.
     *
     * @param children the components to add
     */
    public void add(VRComponent... children) {
        for(final VRComponent child : children) {
            getChildren().add(child);
            child.setParent(this);
        }
    }

    /**
     * Gets the parent of this {@link VRComponent}.
     *
     * @return the parent
     */
    public VRComponent getParent() {
        return parent;
    }

    /**
     * Sets the parent of this {@link VRComponent}.
     *
     * @param parent the parent
     */
    public void setParent(VRComponent parent) {
        this.parent = parent;
    }

    /**
     * Gets whether this {@link VRComponent} has a parent.
     *
     * @return {@code true} if this component has a parent
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Removes this component and its children from the component hierarchy.
     *
     * @param removeFromParent {@code true} if this component should also be removed from the parent
     */
    public void remove(final boolean removeFromParent) {
        if(removeFromParent) {
            if(parent != null) {
                parent.getChildren().remove(this);
            }
        }
        parent = null;
        for(final VRComponent child : getChildren()) {
            child.remove(false);
        }
        getChildren().clear();
        setMaterial(null);
        FocusManager.getInstance().unregister(this);
        CollisionManager.getInstance().remove(this);
    }

    @Override
    public void invalidate() {
        TransformationManager.getInstance().add(this);
        if(isDirty()) {
            return;
        }
        super.invalidate();
        for(final VRComponent child : getChildren()) {
            child.invalidate();
        }
    }

    /**
     * Gets the {@link List} of {@link VRComponent Childre}.
     *
     * @return the children
     */
    public List<VRComponent> getChildren() {
        if(children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    /**
     * Draw this component with the given projection matrix. Also {@link #draw(float[], float[])} will be called on
     * every child received by {@link #getChildren()}.
     *
     * @param view the view matrix
     * @param projection the projection matrix
     */
    public void draw(final float[] view, final float[] projection) {
        drawOnMaterial(getModelMatrix(), view, projection);
    }

    /**
     * Draws onto the currently set {@link Material}.
     *
     * @param modelMatrix the model matrix to get the MV and MVP matrix from
     * @param view        the view matrix
     * @param projection  the projection matrix
     */
    public void drawOnMaterial(final Matrix4x4 modelMatrix, final float[] view, final float[] projection) {
        getMaterial().draw(modelMatrix, view, projection);
    }

    /**
     * Performs a transformation on this {@link VRComponent}.
     *
     * @param delta the animation delta based on the current fps
     */
    public void transform(final float delta) {
        // Only if the component is dirty or has animations then we
        // need to perform transformations
        if(isDirty() || hasAnimation()) {
            if(hasParent()) {
                globalModelMatrix.set(getParent().getModelMatrix());
            } else {
                globalModelMatrix.identity();
            }
            animate(delta);
            doTransform();
        }
    }

    /**
     * Multiplies this {@link Matrix4x4} with the given one.
     *
     * @param modelMatrix the matrix to multiply with
     * @return this instance of {@link Matrix4x4}
     */
    public Matrix4x4 transform(final Matrix4x4 modelMatrix) {
        transformMatrix.set(modelMatrix);
        return modelMatrix.multiplyMM(transformMatrix, this);
    }

    /**
     * Animates and transforms this {@link VRComponent} based on the currently top element on the {@link MatrixStack}.
     */
    protected void doTransform() {
        if(isDirty()) {
            getBoundingBox().setUnmodifiedModelMatrix(getModelMatrix());
            transform(getModelMatrix());
            updateBounds(getModelMatrix());
        }
    }
}
