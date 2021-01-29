import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram {
	private final String VERTEX_SHADER_PATH = "src/vertex_shader.vs";
	private final String FRAGMENT_SHADER_PATH = "src/fragment_shader.fs";
	
	private int programID;
	private int vertexID;
	private int fragmentID;
	private int texture1Location;
	private int texture2Location;
	
	public ShaderProgram() {
		vertexID = createShader(VERTEX_SHADER_PATH, GL_VERTEX_SHADER);
		fragmentID = createShader(FRAGMENT_SHADER_PATH, GL_FRAGMENT_SHADER);
		programID = createProgram(vertexID, fragmentID);
		
		
	}
	
	private int createProgram(int vertexID, int fragmentID) {
		int id = glCreateProgram();
		
		glAttachShader(id, vertexID);
		glAttachShader(id, fragmentID);
		glLinkProgram(id);
		
		if(glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			System.err.println(glGetProgramInfoLog(id, 512));
			System.exit(-1);
		}
		
		glDetachShader(id, vertexID);
		glDetachShader(id, fragmentID);
		glDeleteShader(vertexID);
		glDeleteShader(fragmentID);
		
		return id;
	}
	
	private int createShader(String fileName, int type) {
		StringBuilder source = null;
		try {
			source = FileHandler.loadResource(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, source);
		glCompileShader(shaderID);
		
		if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(shaderID, 512));
			System.exit(-1);
		}
		
		return shaderID;
	}
	
	public void use() {
		glUseProgram(programID);
	}
	
	//Gets and stores all uniform variable locations
	private void getUniformLocations() {
		texture1Location = glGetUniformLocation(programID, "texture1");
		texture2Location = glGetUniformLocation(programID, "texture2");
	}
	
	public void setTexture(int texture1, int texture2) {
		glUniform1i(texture1Location, texture1);
		glUniform1i(texture2Location, texture2);
	}
	
	public void loadMatrix(String name, Matrix4f matrix) {
		int location = glGetUniformLocation(programID, name);
		try(MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			matrix.get(fb);
			glUniformMatrix4fv(location, false, fb);
		}
	}
	
	public void loadMatrices(String name, Matrix4f[] matrices) {
		int location = glGetUniformLocation(programID, name);
		
		try(MemoryStack stack = MemoryStack.stackPush()) {
			int length = matrices != null ? matrices.length : 0;
			FloatBuffer fb = stack.mallocFloat(16 * length);
			
			for(int i = 0; i < length; i++) {
				matrices[i].get(16 * i, fb);
			}
			glUniformMatrix4fv(location, false, fb);
		}
	}
	
	public void loadVector3f(String name, Vector3f vec) {
		int location = glGetUniformLocation(programID, name);
		glUniform3f(location, vec.x, vec.y, vec.z);
	}
	
}
