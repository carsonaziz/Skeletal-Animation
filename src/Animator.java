import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;

public class Animator {
	
	private final AnimGameItem_Test animGameItem;
	
	private Animation_Test currentAnimation;
	private AnimatedFrame currentAnimatedFrame;
	private float animationTime;
	
	public Animator(AnimGameItem_Test animGameItem) {
		this.animGameItem = animGameItem;
	}
	
	public void doAnimation(Animation_Test animation) {
		this.animationTime = 0.0f;
		this.currentAnimation = animation;
	}
	
	public void update(float interval) {
		if(currentAnimation == null) {
			return;
		}
		
		increaseAnimationTime(interval);
		//get current and next keyframe and interpolate between them
		Map<Integer, Matrix4f> currentLocalPose = calculateCurrentAnimationPose();
		Map<Integer, Matrix4f> currentGlobalPose = new HashMap<Integer, Matrix4f>();
		buildGlobalPose(currentLocalPose, animGameItem.getRootNode(), animGameItem.getRootNodeTransformation(), currentGlobalPose);
		//buildGlobalPose(currentLocalPose, animGameItem.getRootNode(), new Matrix4f(), currentGlobalPose);
		currentAnimatedFrame = buildAnimatedFrame(currentGlobalPose);
	}
	
	private Map<Integer, Matrix4f> calculateCurrentAnimationPose() {
		Keyframe[] keyframes = getPreviousAndNextKeyframes();
		float progression = calculateProgression(keyframes[0], keyframes[1]);
		return interpolatePoses(keyframes[0], keyframes[1], progression);
	}
	
	private Keyframe[] getPreviousAndNextKeyframes() {
		Keyframe[] allKeyframes = currentAnimation.getKeyframes();
		Keyframe previousKeyframe = allKeyframes[0];
		Keyframe nextKeyframe = allKeyframes[0];
		for(int i = 1; i < allKeyframes.length; i++) {
			nextKeyframe = allKeyframes[i];
			if(nextKeyframe.getTimeStamp() > animationTime) {
				break;
			}
			previousKeyframe = allKeyframes[i];
		}
		
		return new Keyframe[] { previousKeyframe, nextKeyframe };
	}
	
	private void increaseAnimationTime(float interval) {
		animationTime += interval;
		if(animationTime > (float)currentAnimation.getDuration()) {
			this.animationTime %= currentAnimation.getDuration();
		}
	}
	
	private float calculateProgression(Keyframe previousKeyframe, Keyframe nextKeyframe) {
		float totalTime = nextKeyframe.getTimeStamp() - previousKeyframe.getTimeStamp();
		float currentTime = animationTime - previousKeyframe.getTimeStamp();
		return currentTime / totalTime;
	}
	
	private Map<Integer, Matrix4f> interpolatePoses(Keyframe previousKeyframe, Keyframe nextKeyframe, float progression) {
		Map<Integer, Matrix4f> currentLocalPose = new HashMap<Integer, Matrix4f>();
		for(int i = 0; i < previousKeyframe.getBoneTransformations().length; i++) {
			BoneTransformation previousBoneTransformation = previousKeyframe.getBoneTransformations()[i];
			BoneTransformation nextBoneTransformation = nextKeyframe.getBoneTransformations()[i];
			BoneTransformation currentBoneTransformation = BoneTransformation.interpolate(previousBoneTransformation, nextBoneTransformation, progression);
			currentLocalPose.put(i, currentBoneTransformation.createLocalTransformation());
		}
		
		return currentLocalPose;
	}
	
	private void buildGlobalPose(Map<Integer, Matrix4f> currentLocalPose, Node_Test node, Matrix4f parentTransformation, Map<Integer, Matrix4f> globalPose) {
		Matrix4f currentLocalTransformation = null;
		Matrix4f currentGlobalTransformation = null;
		
		if(node.getBoneId() != -1) {
			currentLocalTransformation = currentLocalPose.get(node.getBoneId());
			currentGlobalTransformation = parentTransformation.mul(currentLocalTransformation);
		} else {
			currentGlobalTransformation = parentTransformation;
		}
		
		for(Node_Test childNode : node.getChildren()) {
			buildGlobalPose(currentLocalPose, childNode, new Matrix4f(currentGlobalTransformation), globalPose);
		}
		
		if(node.getBoneId() != -1) {
			currentGlobalTransformation = currentGlobalTransformation.mul(node.getBoneOffsetMatrix());
			globalPose.put(node.getBoneId(), currentGlobalTransformation);
		}
		
	}
	
	
	private AnimatedFrame buildAnimatedFrame(Map<Integer, Matrix4f> currentGlobalPose) {
		AnimatedFrame animatedFrame = new AnimatedFrame();
		for(Map.Entry<Integer, Matrix4f> matrixEntry : currentGlobalPose.entrySet()) {
			int boneId = matrixEntry.getKey();
			Matrix4f matrix = matrixEntry.getValue();
			animatedFrame.setMatrix(boneId, matrix);
		}
		
		return animatedFrame;
	}
	
	public AnimatedFrame getCurrentAnimatedFrame() {
		return currentAnimatedFrame;
	}
	
	private Matrix4f copyMatrix(Matrix4f source) {
		Matrix4f matrix = new Matrix4f();
		matrix.m00(source.m00());
		matrix.m01(source.m01());
		matrix.m02(source.m02());
		matrix.m03(source.m03());

		matrix.m10(source.m10());
		matrix.m11(source.m11());
		matrix.m12(source.m12());
		matrix.m13(source.m13());

		matrix.m20(source.m20());
		matrix.m21(source.m21());
		matrix.m22(source.m22());
		matrix.m23(source.m23());

		matrix.m30(source.m30());
		matrix.m31(source.m31());
		matrix.m32(source.m32());
		matrix.m33(source.m33());
		
		return matrix;
	}
}
