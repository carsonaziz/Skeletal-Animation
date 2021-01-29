import java.util.List;

public class Animation_Test {
	
	private Keyframe[] keyframes;
	private String name;
	private double duration;
	
	public Animation_Test(String name, Keyframe[] keyframes, double duration) {
		this.name = name;
		this.keyframes = keyframes;
		this.duration = duration;
	}
	
	public String getName() {
		return name;
	}
	
	public Keyframe[] getKeyframes() {
		return keyframes;
	}
	
	public double getDuration() {
		return duration;
	}

}
