using UnityEngine;
using UnityEditor;
using System.Collections;

[CustomEditor(typeof(JointController))]
public class JointControllerEditor : Editor {

	public override void OnInspectorGUI() {
		// Get the target component
		JointController jc = target as JointController;

		// Draw the inspector fields
		jc.motorSpeed = EditorGUILayout.FloatField("Motor Speed", jc.motorSpeed);
		jc.motorForce = EditorGUILayout.FloatField("Motor Force", jc.motorForce);
		EditorGUILayout.Separator();
		jc.axis = EditorGUILayout.Vector3Field("Angle Axis (other)", jc.axis);
		jc.minAngle = EditorGUILayout.FloatField("Minimum Angle", jc.minAngle);
		jc.maxAngle = EditorGUILayout.FloatField("Maximum Angle", jc.maxAngle);
		jc.maxAngleErr = EditorGUILayout.FloatField("Maximum Angle Error", jc.maxAngleErr);
		EditorGUILayout.Separator();
		jc.targetAngle = EditorGUILayout.FloatField("Target Angle", jc.targetAngle);
	}
}
