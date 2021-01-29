import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BoneTransformation {
	
	private Vector3f position;
	private Quaternionf rotation;
	private Vector3f scaling;
	
	public BoneTransformation(Vector3f position, Quaternionf rotation, Vector3f scaling) {
		this.position = position;
		this.rotation = rotation;
		this.scaling = scaling;
	}
	
	protected static BoneTransformation interpolate(BoneTransformation frameA, BoneTransformation frameB, float progression) {
		Vector3f pos = interpolate(frameA.getPosition(), frameB.getPosition(), progression);
		Quaternionf rot = interpolate(frameA.getRotation(), frameB.getRotation(), progression);
		Vector3f scale = interpolate(frameA.getScaling(), frameB.getScaling(), progression);
		return new BoneTransformation(pos, rot, scale);
	}
	
	private static Vector3f interpolate(Vector3f start, Vector3f end, float progression) {
		float x = start.x + (end.x - start.x) * progression;
		float y = start.y + (end.y - start.y) * progression;
		float z = start.z + (end.z - start.z) * progression;
		return new Vector3f(x, y, z);
	}
	
	private static Quaternionf interpolate(Quaternionf start, Quaternionf end, float progression) {
		Quaternionf result = new Quaternionf(0, 0, 0, 1);
		float dot = start.w * end.w + start.x * end.x + start.y * end.y + start.z * end.z;
		float progressionI = 1.0f - progression;
		if(dot < 0) {
			result.w = progressionI * start.w + progression * -end.w;
			result.x = progressionI * start.x + progression * -end.x;
			result.y = progressionI * start.y + progression * -end.y;
			result.z = progressionI * start.z + progression * -end.z;
		} else {
			result.w = progressionI * start.w + progression * end.w;
			result.x = progressionI * start.x + progression * end.x;
			result.y = progressionI * start.y + progression * end.y;
			result.z = progressionI * start.z + progression * end.z;
		}
		result.normalize();
		return result;
	}
	
	protected Matrix4f createLocalTransformation() {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(position)
			.rotate(rotation)
			.scale(scaling);
		return matrix;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Quaternionf getRotation() {
		return rotation;
	}
	
	public Vector3f getScaling() {
		return scaling;
	}
}
