
import static org.lwjgl.assimp.Assimp.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIQuatKey;
import org.lwjgl.assimp.AIQuaternion;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVectorKey;
import org.lwjgl.assimp.AIVertexWeight;

public class AnimMeshesLoader_Test extends StaticMeshesLoader {
	
	public static AnimGameItem_Test loadAnimGameItem(String resourcePath, String texturesDir) throws Exception {
		return loadAnimGameItem(resourcePath, texturesDir,
				aiProcess_GenSmoothNormals 
				| aiProcess_JoinIdenticalVertices 
				| aiProcess_Triangulate 
				| aiProcess_FixInfacingNormals 
				| aiProcess_LimitBoneWeights);
	}
	
	public static AnimGameItem_Test loadAnimGameItem(String resourcePath, String texturesDir, int flags) throws Exception {
		AIScene aiScene = aiImportFile(resourcePath, flags);
		
		if(aiScene == null) {
			throw new Exception("Error loading model");
		}
		
		int numMaterials = aiScene.mNumMaterials();
		PointerBuffer aiMaterials = aiScene.mMaterials();
		List<Material> materials = new ArrayList<Material>();
		
		for(int i = 0; i < numMaterials; i++) {
			AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
			processMaterial(aiMaterial, materials, texturesDir);
		}
		
		List<Bone> boneList = new ArrayList<Bone>();
		int numMeshes = aiScene.mNumMeshes();
		PointerBuffer aiMeshes = aiScene.mMeshes();
		Mesh[] meshes = new Mesh[numMeshes];
		
		for(int i = 0; i < numMeshes; i++) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
			Mesh mesh = processMesh(aiMesh, materials, boneList);
			meshes[i] = mesh;
		}
		
		AINode aiRootNode = aiScene.mRootNode();
		Matrix4f rootTransformation = AnimMeshesLoader_Test.toMatrix(aiRootNode.mTransformation());
		Node_Test rootNode = processNodesHierarchy(aiRootNode, null);
		Map<String, Animation_Test> animations = processAnimations(aiScene, boneList, rootNode, rootTransformation);
		AnimGameItem_Test item = new AnimGameItem_Test(meshes, animations, rootNode, rootTransformation);
		
		return item;
	}
	
	public static AnimGameItem_Test loadAnimGameItemWithMultipleAnimations(String resourcePath, String animation1Path, String animation2Path, String texturesDir) throws Exception {
		return loadAnimGameItemWithMultipleAnimations(resourcePath, animation1Path, animation2Path, texturesDir, aiProcess_GenSmoothNormals 
				| aiProcess_JoinIdenticalVertices 
				| aiProcess_Triangulate 
				| aiProcess_FixInfacingNormals 
				| aiProcess_LimitBoneWeights);
	}
	
	public static AnimGameItem_Test loadAnimGameItemWithMultipleAnimations(String resourcePath, String animation1Path, String animation2Path, String texturesDir, int flags) throws Exception {
		AIScene aiScene = aiImportFile(resourcePath, flags);
		
		if(aiScene == null) {
			throw new Exception("Error loading model");
		}
		
		int numMaterials = aiScene.mNumMaterials();
		PointerBuffer aiMaterials = aiScene.mMaterials();
		List<Material> materials = new ArrayList<Material>();
		
		for(int i = 0; i < numMaterials; i++) {
			AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
			processMaterial(aiMaterial, materials, texturesDir);
		}
		
		List<Bone> boneList = new ArrayList<Bone>();
		int numMeshes = aiScene.mNumMeshes();
		PointerBuffer aiMeshes = aiScene.mMeshes();
		Mesh[] meshes = new Mesh[numMeshes];
		
		for(int i = 0; i < numMeshes; i++) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
			Mesh mesh = processMesh(aiMesh, materials, boneList);
			meshes[i] = mesh;
		}
		
		AINode aiRootNode = aiScene.mRootNode();
		Matrix4f rootTransformation = AnimMeshesLoader_Test.toMatrix(aiRootNode.mTransformation());
		Node_Test rootNode = processNodesHierarchy(aiRootNode, null);
		Map<String, Animation_Test> animations = new HashMap<String, Animation_Test>();
		Animation_Test animation1 = loadAnimation(animation1Path, rootNode);
		Animation_Test animation2 = loadAnimation(animation2Path, rootNode);
		//ALL ANIMATIONS HAVE SAME NAME, SO THEY WILL BE PUT IN THE SAME SLOT OF THE MAP
		animations.put(animation1.getName(), animation1);
		animations.put(animation2.getName(), animation2);
		AnimGameItem_Test item = new AnimGameItem_Test(meshes, animations, rootNode, rootTransformation);
		
		return item;
	}
	
	private static Animation_Test loadAnimation(String resourcePath, Node_Test rootNode) throws Exception {
		return loadAnimation(resourcePath, rootNode, aiProcess_GenSmoothNormals 
				| aiProcess_JoinIdenticalVertices 
				| aiProcess_Triangulate 
				| aiProcess_FixInfacingNormals 
				| aiProcess_LimitBoneWeights);
	}
	
	private static Animation_Test loadAnimation(String resourcePath, Node_Test rootNode, int flags) throws Exception {
		AIScene aiScene = aiImportFile(resourcePath, flags);

		if(aiScene == null) {
			throw new Exception("Error loading animation");
		}
		
		AINode aiRootNode = aiScene.mRootNode();
		Matrix4f rootTransformation = AnimMeshesLoader_Test.toMatrix(aiRootNode.mTransformation());
		
		
		List<Bone> boneList = new ArrayList<Bone>();
		int numMeshes = aiScene.mNumMeshes();
		PointerBuffer aiMeshes = aiScene.mMeshes();
		for(int i = 0; i < numMeshes; i++) {
			AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
	        List<Integer> boneIds = new ArrayList<>();
	        List<Float> weights = new ArrayList<>();
	        
	        processBones(aiMesh, boneList, boneIds, weights);
		}
		
		AIAnimation aiAnimation = AIAnimation.create(aiScene.mAnimations().get(0));
		int numChannels = aiAnimation.mNumChannels();
		PointerBuffer aiChannels = aiAnimation.mChannels();
		for(int i = 0; i < numChannels; i++) {
			AINodeAnim aiNodeAnim = AINodeAnim.create(aiAnimation.mChannels().get(i));
			String name = aiNodeAnim.mNodeName().dataString();
			Node_Test node = rootNode.findByName(name);
			buildTransformationMatrices(aiNodeAnim, node);
		}
		
		Keyframe[] keyframes = orderKeyframes(boneList, rootNode, rootTransformation);
		return new Animation_Test(aiAnimation.mName().dataString(), keyframes, aiAnimation.mDuration());
	}
	
	private static Mesh processMesh(AIMesh aiMesh, List<Material> materials, List<Bone> boneList) {
		List<Float> vertices = new ArrayList<Float>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Integer> boneIds = new ArrayList<>();
        List<Float> weights = new ArrayList<>();
        
        processVertices(aiMesh, vertices);
        processNormals(aiMesh, normals);
        processTextCoords(aiMesh, textures);
        processIndices(aiMesh, indices);
        processBones(aiMesh, boneList, boneIds, weights);
        
     // Texture coordinates may not have been populated. We need at least the empty slots
        if ( textures.size() == 0) {
            int numElements = (vertices.size() / 3) * 2;
            for (int i=0; i<numElements; i++) {
                textures.add(0.0f);
            }
        }
        
        Mesh mesh = new Mesh(Utils.listToArray(vertices), 
        		Utils.listToArray(normals), Utils.listToArray(textures), 
        		Utils.listIntToArray(indices), Utils.listIntToArray(boneIds), 
        		Utils.listToArray(weights));
        Material material;
        
        int materialIdx = aiMesh.mMaterialIndex();
        if(materialIdx >= 0 && materialIdx < materials.size()) {
        	material = materials.get(materialIdx);
        } else {
        	material = new Material();
        }
        
        mesh.setMaterial(material);
        
        return mesh;
	}
	
	private static void processBones(AIMesh aiMesh, List<Bone> boneList, List<Integer> boneIds, List<Float> weights) {
		Map<Integer, List<VertexWeight>> weightSet = new HashMap<Integer, List<VertexWeight>>();
		int numBones = aiMesh.mNumBones();
		PointerBuffer aiBones = aiMesh.mBones();
		for(int i = 0; i < numBones; i++) {
			AIBone aiBone = AIBone.create(aiBones.get(i));
			int id = boneList.size();
			Bone bone = new Bone(id, aiBone.mName().dataString(), toMatrix(aiBone.mOffsetMatrix()));
			boneList.add(bone);
			
			int numWeights = aiBone.mNumWeights();
			AIVertexWeight.Buffer aiWeights = aiBone.mWeights();
			for(int j = 0; j < numWeights; j++) {
				AIVertexWeight aiWeight = aiWeights.get(j);
				VertexWeight vw = new VertexWeight(bone.getBoneId(), aiWeight.mVertexId(), aiWeight.mWeight());
				List<VertexWeight> vertexWeightList = weightSet.get(vw.getVertexId());
				
				if(vertexWeightList == null) {
					vertexWeightList = new ArrayList<VertexWeight>();
					weightSet.put(vw.getVertexId(),  vertexWeightList);
				}
				vertexWeightList.add(vw);
			}
		}
		
		int numVertices = aiMesh.mNumVertices();
		for(int i = 0; i < numVertices; i++) {
			List<VertexWeight> vertexWeightList = weightSet.get(i);
			int size = vertexWeightList != null ? vertexWeightList.size() : 0;
			for(int j = 0; j < Mesh.MAX_WEIGHTS; j++) {
				if(j < size) {
					VertexWeight vw = vertexWeightList.get(j);
					weights.add(vw.getWeight());
					boneIds.add(vw.getBoneId());
				} else {
					weights.add(0.0f);
					boneIds.add(0);
				}
			}
		}
	}
	
	private static Node_Test processNodesHierarchy(AINode aiNode, Node_Test parentNode) {
		String nodeName = aiNode.mName().dataString();
		Node_Test node = new Node_Test(nodeName, parentNode);
		
		int numChildren = aiNode.mNumChildren();
		PointerBuffer aiChildren = aiNode.mChildren();
		for(int i = 0; i < numChildren; i++) {
			AINode aiChildNode = AINode.create(aiChildren.get(i));
			Node_Test childNode = processNodesHierarchy(aiChildNode, node);
			node.addChild(childNode);
		}
		
		return node;
	}
	
	private static Map<String, Animation_Test> processAnimations(AIScene aiScene, List<Bone> boneList, Node_Test rootNode, Matrix4f rootTransformation) {
		Map<String, Animation_Test> animations = new HashMap<String, Animation_Test>();
		
		//Process all animations
		int numAnimations = aiScene.mNumAnimations();
		PointerBuffer aiAnimations = aiScene.mAnimations();
		for(int i = 0; i < numAnimations; i++) {
			AIAnimation aiAnimation = AIAnimation.create(aiAnimations.get(i));
			String animName = aiAnimation.mName().dataString();
			
			//Calculate transformation matrices for each node
			int numChannels = aiAnimation.mNumChannels();
			PointerBuffer aiChannels = aiAnimation.mChannels();
			for(int j = 0; j < numChannels; j++) {
				AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(j));
				String nodeName = aiNodeAnim.mNodeName().dataString();
				Node_Test node = rootNode.findByName(nodeName);
				buildTransformationMatrices(aiNodeAnim, node);
			}
			
			Keyframe[] keyframes = orderKeyframes(boneList, rootNode, rootTransformation);
			Animation_Test animation = new Animation_Test(aiAnimation.mName().dataString(), keyframes, aiAnimation.mDuration());
			animations.put(animation.getName(), animation);
		}
		return animations;
	}
	
	private static void buildTransformationMatrices(AINodeAnim aiNodeAnim, Node_Test node) {
		int numFrames = aiNodeAnim.mNumPositionKeys();
		AIVectorKey.Buffer positionKeys = aiNodeAnim.mPositionKeys();
		AIVectorKey.Buffer scalingKeys = aiNodeAnim.mScalingKeys();
		AIQuatKey.Buffer rotationKeys = aiNodeAnim.mRotationKeys();
		
		for(int i = 0; i < numFrames; i++) {
			AIVectorKey aiVecKey = positionKeys.get(i);
			AIVector3D vecPos = aiVecKey.mValue();
			System.out.println(vecPos.x() + " " + vecPos.y() + " " + vecPos.z());
			
			AIQuatKey quatKey = rotationKeys.get(i);
			AIQuaternion aiQuat = quatKey.mValue();
			Quaternionf quat = new Quaternionf(aiQuat.x(), aiQuat.y(), aiQuat.z(), aiQuat.w());
			
			Vector3f vecScale = new Vector3f(1, 1, 1);
			if(i < aiNodeAnim.mNumScalingKeys()) {
				aiVecKey = scalingKeys.get(i);
				vecScale = new Vector3f(aiVecKey.mValue().x(), aiVecKey.mValue().y(), aiVecKey.mValue().z());
				System.out.println(vecScale.x() + " " + vecScale.y() + " " + vecScale.z());
			}
			
			double timeStamp = aiVecKey.mTime();
			BoneTransformation boneTransformation = new BoneTransformation(new Vector3f(vecPos.x(), vecPos.y(), vecPos.z()), quat, new Vector3f(vecScale.x(), vecScale.y(), vecScale.z()));
			
			node.addTransformation(boneTransformation);
			node.addKeyTime(timeStamp);
		}
	}
	
	private static Keyframe[] orderKeyframes(List<Bone> boneList, Node_Test rootNode, Matrix4f rootTransformation) {
		//.getAnimationFrames() might need to be tweaked
		int numKeyframes = rootNode.getAnimationFrames();
		Keyframe[] keyframeList = new Keyframe[numKeyframes];
		for(int i = 0; i < numKeyframes; i++) {
			Keyframe keyframe = new Keyframe();
			keyframeList[i] = keyframe;
			
			int numBones = boneList.size();
			for(int j = 0; j < numBones; j++) {
				Bone bone = boneList.get(j);
				Node_Test node = rootNode.findByName(bone.getBoneName());
				node.addBoneId(j);
				node.addBoneOffsetMatrix(bone.getOffsetMatrix());
				BoneTransformation boneTransformation = Node_Test.getBoneTransformation(node, i);
				double keyTime = Node_Test.getKeyTime(node, i);
				keyframe.setBoneTransformation(j, boneTransformation);
				keyframe.setTimeStamp(keyTime);
			}
		}
		
		return keyframeList;
	}
	
	private static Matrix4f toMatrix(AIMatrix4x4 aiMatrix4x4) {
		Matrix4f result = new Matrix4f();
        result.m00(aiMatrix4x4.a1());
        result.m10(aiMatrix4x4.a2());
        result.m20(aiMatrix4x4.a3());
        result.m30(aiMatrix4x4.a4());
        result.m01(aiMatrix4x4.b1());
        result.m11(aiMatrix4x4.b2());
        result.m21(aiMatrix4x4.b3());
        result.m31(aiMatrix4x4.b4());
        result.m02(aiMatrix4x4.c1());
        result.m12(aiMatrix4x4.c2());
        result.m22(aiMatrix4x4.c3());
        result.m32(aiMatrix4x4.c4());
        result.m03(aiMatrix4x4.d1());
        result.m13(aiMatrix4x4.d2());
        result.m23(aiMatrix4x4.d3());
        result.m33(aiMatrix4x4.d4());

        return result;
	}
	
}
