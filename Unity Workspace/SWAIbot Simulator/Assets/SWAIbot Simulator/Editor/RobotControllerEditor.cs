using UnityEngine;
using UnityEditor;
using System.Collections;

[CustomEditor(typeof(RobotController))]
public class RobotControllerEditor : Editor {
	
	public override void OnInspectorGUI() {
		// Get the target component
		RobotController rc = target as RobotController;
		
		// Draw the inspector fields
		rc.leftLegTopObj = EditorGUILayout.ObjectField("Left Leg Top", rc.leftLegTopObj, typeof(Transform)) as Transform;
		rc.rightLegTopObj = EditorGUILayout.ObjectField("Right Leg Top", rc.rightLegTopObj, typeof(Transform)) as Transform;
		rc.leftLegBottomObj = EditorGUILayout.ObjectField("Left Leg Bottom", rc.leftLegBottomObj, typeof(Transform)) as Transform;
		rc.rightLegBottomObj = EditorGUILayout.ObjectField("Right Leg Bottom", rc.rightLegBottomObj, typeof(Transform)) as Transform;
		
		EditorGUILayout.Separator();
		
		rc.invertLeftLegTop = EditorGUILayout.Toggle("Invert Left Leg Top", rc.invertLeftLegTop);
		rc.invertRightLegTop = EditorGUILayout.Toggle("Invert Right Leg Top", rc.invertRightLegTop);
		rc.invertLeftLegBottom = EditorGUILayout.Toggle("Invert Left Leg Bottom", rc.invertLeftLegBottom);
		rc.invertRightLegBottom = EditorGUILayout.Toggle("Invert Right Leg Bottom", rc.invertRightLegBottom);
		
		EditorGUILayout.Separator();
		
		rc.leftLegTopTarget = EditorGUILayout.FloatField("Left Leg Top Target", rc.leftLegTopTarget);
		rc.rightLegTopTarget = EditorGUILayout.FloatField("Right Leg Top Target", rc.rightLegTopTarget);
		rc.leftLegBottomTarget = EditorGUILayout.FloatField("Left Leg Bottom Bottom", rc.leftLegBottomTarget);
		rc.rightLegBottomTarget = EditorGUILayout.FloatField("Right Leg Bottom Bottom", rc.rightLegBottomTarget);
		
		EditorGUILayout.Separator();
		
		rc.robotBody = EditorGUILayout.ObjectField("Robot Body", rc.robotBody, typeof(Transform)) as Transform;
		rc.respawnBellow = EditorGUILayout.FloatField("Respawn Bellow", rc.respawnBellow);
	}
}