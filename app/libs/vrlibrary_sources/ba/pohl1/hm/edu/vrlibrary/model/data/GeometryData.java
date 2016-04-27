package ba.pohl1.hm.edu.vrlibrary.model.data;

/**
 * Created by Pohl on 10.04.2016.
 */
public class GeometryData {

    private float[] verticesArray;
    private float[] colorsArray;
    private short[] indicesArray;
    private float[] normalsArray;
    private float[] texArray;

    public GeometryData(short[] indicesArray, float[] verticesArray, float[] normalsArray, float[] texArray) {
        this.indicesArray = indicesArray;
        this.verticesArray = verticesArray;
        this.normalsArray = normalsArray;
        this.texArray = texArray;
    }

    public GeometryData(float[] verticesArray, float[] normalsArray) {
        this.verticesArray = verticesArray;
        this.normalsArray = normalsArray;
    }

    public short[] getIndicesArray() {
        return indicesArray;
    }

    public float[] getVerticesArray() {
        return verticesArray;
    }

    public float[] getNormalsArray() {
        return normalsArray;
    }

    public float[] getTexArray() {
        return texArray;
    }

    public void setTexArray(float[] texArray) {
        this.texArray = texArray;
    }

    public float[] getColorsArray() {
        return colorsArray;
    }

    public void setColorsArray(float[] colorsArray) {
        this.colorsArray = colorsArray;
    }
}
