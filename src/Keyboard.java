import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {
	private Window window;
	
	public Keyboard(Window window) {
		this.window = window;
	}

	public boolean isKeyPressed(int keyCode) {
		return glfwGetKey(window.getWindowHandle(), keyCode) == GLFW_PRESS;
	}
}
