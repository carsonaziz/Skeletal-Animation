import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

public class Node_Test {
	private final List<Node_Test> children;
	private final List<Double> keyTimes;
	private final List<BoneTransformation> transformations;
	private final String name;
	private int boneId = -1;
	private Matrix4f boneOffsetMatrix = null;
	private final Node_Test parent;
	
	public Node_Test(String name, Node_Test parent) {
		this.name = name;
		this.parent = parent;
		this.keyTimes = new ArrayList<Double>();
		this.transformations = new ArrayList<BoneTransformation>();
		this.children = new ArrayList<Node_Test>();
	}
	
//	public static Matrix4f getParentTransforms(Node_Test node, int framePos) {
//		if(node == null) {
//			return new Matrix4f();
//		} else {
//			Matrix4f parentTransform = new Matrix4f(getParentTransforms(node.getParent(), framePos));
//			List<Matrix4f> transformations = node.getTransformations();
//			Matrix4f nodeTransform;
//			int transfSize = transformations.size();
//			if(framePos < transfSize) {
//				nodeTransform = transformations.get(framePos);
//			} else if(transfSize > 0) {
//				nodeTransform = transformations.get(transfSize - 1);
//			} else {
//				nodeTransform = new Matrix4f();
//			}
//			return parentTransform.mul(nodeTransform);
//		}
//	}
	
	public void addChild(Node_Test node) {
		this.children.add(node);
	}
	
	public void addTransformation(BoneTransformation transformation) {
		transformations.add(transformation);
	}
	
	public void addKeyTime(double timeStamp) {
		keyTimes.add(timeStamp);
	}
	
	public static BoneTransformation getBoneTransformation(Node_Test node, int framePos) {
		return node.getTransformations().get(framePos);
	}
	
	public static double getKeyTime(Node_Test node, int framePos) {
		return node.getKeyTimes().get(framePos);
	}
	
	public Node_Test findByName(String targetName) {
		Node_Test result = null;
		if(this.name.equals(targetName)) {
			result = this;
		} else {
			for(Node_Test child : children) {
				result = child.findByName(targetName);
				if(result != null) {
					break;
				}
			}
		}
		return result;
	}
	
	public int getAnimationFrames() {
		int numFrames = this.transformations.size();
		for(Node_Test child : children) {
			int childFrame = child.getAnimationFrames();
			numFrames = Math.max(numFrames, childFrame);
		}
		return numFrames;
	}
	
	public List<Node_Test> getChildren() {
		return children;
	}
	
	public List<Double> getKeyTimes() {
		return keyTimes;
	}
	
	public List<BoneTransformation> getTransformations() {
		return transformations;
	}
	
	public String getName() {
		return name;
	}
	
	public Node_Test getParent() {
		return parent;
	}
	
	public void addBoneId(int boneId) {
		this.boneId = boneId;
	}
	
	public int getBoneId() {
		return boneId;
	}
	
	public void addBoneOffsetMatrix(Matrix4f boneOffsetMatrix) {
		this.boneOffsetMatrix = boneOffsetMatrix;
	}
	
	public Matrix4f getBoneOffsetMatrix() {
		return boneOffsetMatrix;
	}
}
