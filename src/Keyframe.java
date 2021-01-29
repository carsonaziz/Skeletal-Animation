import java.util.Arrays;

import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Keyframe {
	
	public static final int MAX_JOINTS = 150;
	private float timeStamp;
	private final BoneTransformation[] boneTransformations;
	
	public Keyframe() {
		timeStamp = -1.0f;
		boneTransformations = new BoneTransformation[MAX_JOINTS];
		Arrays.fill(boneTransformations, new BoneTransformation(new Vector3f(1, 1, 1), new Quaternionf(0, 0, 0, 1), new Vector3f(1, 1, 1)));
	}
	
	public BoneTransformation[] getBoneTransformations() {
		return boneTransformations;
	}
	
	public void setBoneTransformation(int pos, BoneTransformation boneTransformation) {
		boneTransformations[pos] = boneTransformation;
	}
	
	protected float getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(double timeStamp) {
		this.timeStamp = (float)timeStamp;
	}
}
