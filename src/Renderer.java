import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Renderer {
	
	private Window window;
	
	public Renderer(Window window) {
		this.window = window;
	}
	
	public void init() {
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
	}
	
//	public void render(AnimGameItem animGameItem, ShaderProgram shaderProgram) {
//		AnimatedFrame frame = animGameItem.getCurrentAnimation().getCurrentFrame();
//		shaderProgram.loadMatrices("jointsMatrix", frame.getJointMatrices());
//		
//		Mesh[] meshes = animGameItem.getMeshes();
//		
//		for(Mesh mesh : meshes) {
//			glActiveTexture(GL_TEXTURE0);
//			//glBindTexture(GL_TEXTURE_2D, mesh.getMaterial().getTexture().getTextureID());
//			glBindVertexArray(mesh.getVaoID());
//			glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
//			glBindVertexArray(0);
//		}
//
//		window.update();
//	}
	
	public void render(AnimGameItem_Test animGameItem, ShaderProgram shaderProgram) {
		AnimatedFrame frame = animGameItem.getCurrentFrame();
		shaderProgram.loadMatrices("jointsMatrix", frame.getJointMatrices());
		
		Matrix4f transformation = new Matrix4f().identity()
		.translate(new Vector3f(animGameItem.getPosition().x, animGameItem.getPosition().y, animGameItem.getPosition().z))
			.rotate((float) Math.toRadians(0), new Vector3f(1.0f, 0.0f, 0.0f))
			.rotate((float) Math.toRadians(0), new Vector3f(0.0f, 1.0f, 0.0f))
			.rotate((float) Math.toRadians(0), new Vector3f(1.0f, 0.0f, 0.0f))
			.scale(1.0f, 1.0f, 1.0f);
		shaderProgram.loadMatrix("transformation", transformation);
		
		Mesh[] meshes = animGameItem.getMeshes();
		
		for(Mesh mesh : meshes) {
			glActiveTexture(GL_TEXTURE0);
			//glBindTexture(GL_TEXTURE_2D, mesh.getMaterial().getTexture().getTextureID());
			glBindVertexArray(mesh.getVaoID());
			glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
			glBindVertexArray(0);
		}
	}
	
	public void render(GameItem gameItem, ShaderProgram shaderProgram) {
		Matrix4f transformation = new Matrix4f().identity()
		.translate(new Vector3f(gameItem.getPosition().x, gameItem.getPosition().y, gameItem.getPosition().z))
			.rotate((float) Math.toRadians(-90), new Vector3f(1.0f, 0.0f, 0.0f))
			.rotate((float) Math.toRadians(0), new Vector3f(0.0f, 1.0f, 0.0f))
			.rotate((float) Math.toRadians(0), new Vector3f(1.0f, 0.0f, 0.0f))
			.scale(1.0f, 1.0f, 1.0f);
		shaderProgram.loadMatrix("transformation", transformation);
		
		Mesh[] meshes = gameItem.getMeshes();
		
		for(Mesh mesh : meshes) {
			glActiveTexture(GL_TEXTURE0);
			//glBindTexture(GL_TEXTURE_2D, mesh.getMaterial().getTexture().getTextureID());
			glBindVertexArray(mesh.getVaoID());
			glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
			glBindVertexArray(0);
		}
	}
	
	public void update() {
		window.update();
	}
	
	public void cleanup() {
		
	}
}
