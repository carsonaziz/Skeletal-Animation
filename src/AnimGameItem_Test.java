import java.util.Map;
import java.util.Optional;

import org.joml.Matrix4f;

public class AnimGameItem_Test extends GameItem {
	private Map<String, Animation_Test> animations;
	
	private Node_Test rootNode;
	private Matrix4f rootNodeTransformation;
	
	private Animation_Test currentAnimation;
	private Animator animator;
	
	public AnimGameItem_Test(Mesh[] meshes, Map<String, Animation_Test> animations, Node_Test rootNode, Matrix4f rootNodeTransformation) {
		super(meshes);
		this.animations = animations;
		this.rootNode = rootNode;
		this.rootNodeTransformation = rootNodeTransformation;
		this.animator = new Animator(this);
		Optional<Map.Entry<String, Animation_Test>> entry = animations.entrySet().stream().findFirst();
		currentAnimation = entry.isPresent() ? entry.get().getValue() : null;
		//currentAnimation = animations.get("src/res/finger/finger_wave.dae");
	}
	
	public void doAnimation() {
		animator.doAnimation(currentAnimation);
	}
	
	public void update(float interval) {
		animator.update(interval);
	}
	
	public Animation_Test getAnimation(String name) {
		return animations.get(name);
	}
	
	public Animation_Test getCurrentAnimation() {
		return currentAnimation;
	}
	
	public void setCurrentAnimation(Animation_Test currentAnimation) {
		this.currentAnimation = currentAnimation;
	}
	
	public Node_Test getRootNode() {
		return rootNode;
	}
	
	public Matrix4f getRootNodeTransformation() {
		return rootNodeTransformation;
	}
	
	public AnimatedFrame getCurrentFrame() {
		return animator.getCurrentAnimatedFrame();
	}
}
