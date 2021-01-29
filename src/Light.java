import org.joml.Vector3f;

public class Light extends GameItem {
	
	private Vector3f color;
	
	public Light(Mesh[] meshes, Vector3f color) {
		super(meshes);
		this.color = color;
	}
	
	public Vector3f getColor() {
		return color;
	}
}
