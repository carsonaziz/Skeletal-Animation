import org.joml.Vector3f;

public class Camera {
	private Vector3f position;
	
	private float rotX;
	private float rotY;
	private float rotZ;
	
	public Camera() {
		this.position = new Vector3f(0, 0, 0);
		this.rotX = 0.0f;
		this.rotY = 0.0f;
		this.rotZ = 0.0f;
	}
	
	public Camera(Vector3f position, float rotX, float rotY, float rotZ) {
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
	}
	
	public void increasePosition(float x, float y, float z) {
		position.x += x;
		position.y += y;
		position.z += z;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getRotX() {
		return rotX;
	}
	
	public float getRotY() {
		return rotY;
	}
	
	public float getRotZ() {
		return rotZ;
	}
}
