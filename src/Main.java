import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Vector;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;


public class Main {
	
	public static void main(String[] args) {
		Timer timer = new Timer();
		Window window = new Window();
		Renderer renderer = new Renderer(window);
		Camera camera = new Camera(new Vector3f(0, 4.5f, 10f), 0, 0, 0);
		Keyboard keyboard = new Keyboard(window);
		
		timer.init();
		window.init();
		
		//********Assimp Loader********//
		Mesh[] assimpMeshs = null;
		Texture texture = null;
		try {
			assimpMeshs = StaticMeshesLoader.load("src/res/light.obj", "src/res/character/");
			//texture = TextureCache.getInstance().getTexture("src/res/Sharktexture002.png");
			texture = TextureCache.getInstance().getTexture("src/res/character/CharacterTexture.png");
			//texture = TextureCache.getInstance().getTexture("src/res/creeper/creeper.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		GameItem gameItem = new GameItem(assimpMeshs);
		//Light light = new Light(assimpMeshs, new Vector3f(1.0f, 1.0f, 1.0f));
		//*****************************//
		
		//********Assimp Animated Loader********//
		AnimGameItem animGameItem = null;
		AnimGameItem_Test animGameItem_Test = null;
		try {
			//animGameItem = AnimMeshesLoader.loadAnimGameItem("src/res/finger/finger.dae", "src/res/finger");
			//animGameItem = AnimMeshesLoader.loadAnimGameItem("src/res/character/model.dae", "src/res/character");
			//animGameItem = AnimMeshesLoader.loadAnimGameItem("src/res/bob/boblamp.md5mesh", "src/res/bob/textures");
			animGameItem_Test = AnimMeshesLoader_Test.loadAnimGameItem("src/res/character/model.dae", "src/res/character");
			//animGameItem_Test = AnimMeshesLoader_Test.loadAnimGameItem("src/res/character/CharacterRunning.dae", "src/res/character");
			//animGameItem_Test = AnimMeshesLoader_Test.loadAnimGameItem("src/res/finger/finger_2.dae", "src/res/finger");
			//animGameItem_Test = AnimMeshesLoader_Test.loadAnimGameItem("src/res/finger/finger.fbx", "src/res/finger");
			//animGameItem_Test = AnimMeshesLoader_Test.loadAnimGameItem("src/res/bob/boblamp.md5mesh", "src/res/bob/textures");
			//animGameItem = AnimMeshesLoader.loadAnimGameItem("src/res/shark.dae", "src/res");
			//animGameItem_Test = AnimMeshesLoader_Test.loadAnimGameItemWithMultipleAnimations("src/res/finger/finger_2.dae", "src/res/finger/finger_wave.dae", "src/res/finger/finger_look_around.dae", "src/res/finger");
		} catch (Exception e) {
			e.printStackTrace();
		}
		animGameItem_Test.setPosition(3.0f, 0.0f, 0.0f);
		//**************************************//
		
		//********Matrices********//
		//Test variable//
		/////////////////
		Matrix4f transformation = new Matrix4f();
		
		
		
		Matrix4f projection = new Matrix4f();
		projection.setPerspective(45.0f, (float)1280/(float)720, 0.1f, 1500.0f);
		//************************//

		
		ShaderProgram shaderProgram = new ShaderProgram();
		shaderProgram.use();
		//shaderProgram.loadVector3f("color", light.getColor());
		shaderProgram.setTexture(0, 1);
		shaderProgram.loadMatrix("projection", projection);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		animGameItem_Test.doAnimation();
		
		float elapsedTime;
		float accumulator = 0f;
		float interval = 1f / 60;
		
		boolean running = true;
		
		while (running && !window.windowShouldClose()) {
			
			elapsedTime = timer.getElapsedTime();
			accumulator += elapsedTime;
			
			while(accumulator >= interval) {
				//********Update code********//
				float speed = interval * 20f;
				if(keyboard.isKeyPressed(GLFW_KEY_W)) {
					camera.increasePosition(0, 0, -speed);
				}
				if(keyboard.isKeyPressed(GLFW_KEY_S)) {
					camera.increasePosition(0, 0, speed);
				}
				if(keyboard.isKeyPressed(GLFW_KEY_A)) {
					camera.increasePosition(-speed, 0, 0);
				}
				if(keyboard.isKeyPressed(GLFW_KEY_D)) {
					camera.increasePosition(speed, 0, 0);
				}
				if(keyboard.isKeyPressed(GLFW_KEY_SPACE)) {
					camera.increasePosition(0, speed, 0);
				}
				if(keyboard.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
					camera.increasePosition(0, -speed, 0);
				}
				
				Matrix4f view = new Matrix4f();
				view.identity()
					.translate(new Vector3f(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z));
				shaderProgram.loadMatrix("view", view);
//				transformation.identity()
//					.translate(new Vector3f(0.0f, 0.0f, 0.0f))
//					//.rotate((float) Math.toRadians(-90), new Vector3f(1.0f, 0.0f, 0.0f))
//					//.rotate((float) Math.toRadians(rot), new Vector3f(0.0f, 1.0f, 0.0f))
//					//.rotate((float) Math.toRadians(rot), new Vector3f(1.0f, 0.0f, 0.0f))
//					.scale(1.0f, 1.0f, 1.0f);
//				shaderProgram.loadMatrix("transformation", transformation);
				//***************************//
				
				accumulator -= interval;
			}
			
			//Render code
			glEnable(GL_DEPTH_TEST);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			

			float loopSlot = 1f / 120;
			animGameItem_Test.update(loopSlot);
			renderer.render(animGameItem_Test, shaderProgram);
			//renderer.render(light, shaderProgram);
			renderer.update();

			//Sync to target fps
			double endTime = timer.getLastLoopTime() + loopSlot;
			while(timer.getTime() < endTime) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException ie) {
					
				}
			}
		}
		
		
		window.close();
		
		
	}
}
