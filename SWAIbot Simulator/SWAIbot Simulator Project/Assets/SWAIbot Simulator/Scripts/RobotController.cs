using UnityEngine;
using System.Collections;

public class RobotController : MonoBehaviour {

	public Transform leftLegTopObj;
	public Transform rightLegTopObj;
	public Transform leftLegBottomObj;
	public Transform rightLegBottomObj;
	
	private JointController leftLegTop;
	private JointController rightLegTop;
	private JointController leftLegBottom;
	private JointController rightLegBottom;
	
	public bool invertLeftLegTop = false;
	public bool invertRightLegTop = false;
	public bool invertLeftLegBottom = false;
	public bool invertRightLegBottom = false;
	
	public float leftLegTopTarget = 0.0f;
	public float rightLegTopTarget = 0.0f;
	public float leftLegBottomTarget = 0.0f;
	public float rightLegBottomTarget = 0.0f;

	public Transform robotBody = null;
	public float respawnBellow = 0.05f;

	/**
	 * Called on initialization
	 */
	void Start() {
		// Get the joint controller instances
		this.leftLegTop = this.leftLegTopObj.GetComponent<JointController>();
		this.rightLegTop = this.rightLegTopObj.GetComponent<JointController>();
		this.leftLegBottom = this.leftLegBottomObj.GetComponent<JointController>();
		this.rightLegBottom = this.rightLegBottomObj.GetComponent<JointController>();
	}
	
	/**
	 * Update, called once per frame
	 */
	void Update() {
		this.leftLegTop.targetAngle = (!invertLeftLegTop) ? leftLegTopTarget : leftLegTopTarget * -1;
		this.rightLegTop.targetAngle = (!invertRightLegTop) ? rightLegTopTarget : rightLegTopTarget * -1;
		this.leftLegBottom.targetAngle = (!invertLeftLegBottom) ? leftLegBottomTarget : leftLegBottomTarget * -1;
		this.rightLegBottom.targetAngle = (!invertRightLegBottom) ? rightLegBottomTarget : rightLegBottomTarget * -1;

		if(robotBody.transform.localPosition.x < respawnBellow)
			robotBody.transform.position.Set(robotBody.transform.position.x, robotBody.transform.position.y + 4, robotBody.transform.position.z);
	}

	public float GetMotorAngle(int motor) {
		switch(motor) {
		case 1:
			return this.leftLegTop.GetJointAngle();
			break;
		case 2:
			return this.rightLegTop.GetJointAngle();
			break;
		case 3:
			return this.leftLegBottom.GetJointAngle();
			break;
		case 4:
			return this.rightLegBottom.GetJointAngle();
			break;
		default:
			return 0;
		}
	}

	public void SetMotorTargetAngle(int motor, int targetAngle) {
		switch(motor) {
		case 1:
			this.leftLegTopTarget = (float) targetAngle;
			break;
		case 2:
			this.rightLegTopTarget = (float) targetAngle;
			break;
		case 3:
			this.leftLegBottomTarget = (float) targetAngle;
			break;
		case 4:
			this.rightLegBottomTarget = (float) targetAngle;
			break;
		default:
			break;
		}
	}
}
