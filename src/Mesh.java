import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Vector;

import org.joml.Vector3f;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;

public class Mesh {
	
	public static final int MAX_WEIGHTS = 4;
	private int vaoID;
	private int vertexCount;
	
	private Material material;
	
	public Mesh(float[] positions, float[] normals, float[] texCoords, int[] indices) {
		this(positions, normals, texCoords, indices, Mesh.createEmptyIntArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0), Mesh.createEmptyFloatArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0));
	}
	
	public Mesh(float[] positions, float[] normals, float[] texCoords, int[] indices, int[] jointIndices, float[] weights) {
		//Create and bind vao
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		//Store Vertex Count
		vertexCount = indices.length;
		
		//Load mesh data to attributes list
		storeFloatDataInAttributesList(0, 3, positions);
		storeFloatDataInAttributesList(1, 2, texCoords);
		storeFloatDataInAttributesList(2, 3, normals);
		storeFloatDataInAttributesList(3, 4, weights);
		storeIntDataInAttributesList(4, 4, jointIndices);
		storeIndicesInElementBuffer(indices);
		
		//Unbind vao
		glBindVertexArray(0);
	}
	
	private void storeFloatDataInAttributesList(int index, int size, float[] data) {
		FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
		
		//Create vertex buffer, bind vertex buffer and load data to vertex buffer
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
		//Assign vbo to attribute pointer
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(index);
		
		//Unbind VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private void storeIntDataInAttributesList(int index, int size, int[] data) {
		IntBuffer buffer = Utils.storeDataInIntBuffer(data);
		
		//Create vertex buffer, bind vertex buffer and load data to vertex buffer
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
		//Assign vbo to attribute pointer
		glVertexAttribPointer(index, size, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(index);
		
		//Unbind VBO
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private void storeIndicesInElementBuffer(int[] data) {
		IntBuffer buffer =  Utils.storeDataInIntBuffer(data);
		
		int eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}

    protected static float[] createEmptyFloatArray(int length, float defaultValue) {
        float[] result = new float[length];
        Arrays.fill(result, defaultValue);
        return result;
    }

    protected static int[] createEmptyIntArray(int length, int defaultValue) {
        int[] result = new int[length];
        Arrays.fill(result, defaultValue);
        return result;
    }
}
