using UnityEngine;
using System.Collections;

[RequireComponent(typeof(ConfigurableJoint))]
public class JointController : MonoBehaviour {
	
	public float motorSpeed = .75f;
	public float motorForce = Mathf.Infinity;

	public float targetAngle = 0.0f;
	public float maxAngleErr = 1.0f;

	public float minAngle = .0f;
	public float maxAngle = 90.0f;

	public Vector3 axis = new Vector3(0, 0, 0);

	private ConfigurableJoint j;

	/**
	 * Called on initialization
	 */
	void Start() {
		// Get the hinge joint instance
		this.j = GetComponent<ConfigurableJoint>();

		// Lock the proper axis
		this.j.xMotion = ConfigurableJointMotion.Locked;
		this.j.yMotion = ConfigurableJointMotion.Locked;
		this.j.zMotion = ConfigurableJointMotion.Locked;
		this.j.angularXMotion = ConfigurableJointMotion.Limited;
		this.j.angularYMotion = ConfigurableJointMotion.Locked;
		this.j.angularZMotion = ConfigurableJointMotion.Locked;
	}

	/**
	 * Update, called once per frame at a fixed time
	 */
	void FixedUpdate() {
		// Set the proper joint drive mode
		JointDrive jd = this.j.angularXDrive;
		jd.mode = JointDriveMode.Velocity;
		jd.positionDamper = .0f;
		jd.positionSpring = .0f;
		jd.maximumForce = this.motorForce;
		this.j.angularXDrive = jd;

		// Check whether the joint is at target
		bool atTarget = IsAtTarget(false);

		if(atTarget) {
			// Lock the x axis rotation by setting the limits to the target position
			SoftJointLimit sjll = this.j.lowAngularXLimit;
			sjll.limit = GetProperTarget();
			this.j.lowAngularXLimit = sjll;
			SoftJointLimit sjlh = this.j.highAngularXLimit;
			sjlh.limit = GetProperTarget();
			this.j.highAngularXLimit = sjlh;

			// Reset the velocity
			this.j.targetAngularVelocity = new Vector3(0, 0, 0);

		} else {
			// Calculate the direction the joint should rotate to
			int dir = 1;
			if(GetAngleDifference() > 0)
				dir = -1;

			float absDiff = Mathf.Abs(GetAngleDifference());

			// Set the motor velocity
			this.j.targetAngularVelocity = new Vector3(this.motorSpeed * dir, 0, 0);

			// Set the proper limits
			SoftJointLimit sjll = this.j.lowAngularXLimit;
			sjll.limit = this.minAngle;
			this.j.lowAngularXLimit = sjll;
			SoftJointLimit sjlh = this.j.highAngularXLimit;
			sjlh.limit = this.maxAngle;
			this.j.highAngularXLimit = sjlh;
		}
	}

	public void SetTargetAngle(float targetAngle) {
		// Bound the target angle
		targetAngle = Mathf.Repeat(targetAngle, 360.0f);

		// Set the target angle
		this.targetAngle = targetAngle;
	}

	public float GetJointAngle() {
		return GetJointAngle(this.j);
	}

	public float GetJointAngle(ConfigurableJoint j) {
		Quaternion q1 = j.connectedBody.rotation;
		Quaternion q2 = j.gameObject.transform.rotation;

		float a1 = 0;
		if(this.axis.x != 0)
			a1 = Mathf.Repeat(q1.eulerAngles.x, 360.0f);
		else  if(this.axis.y != 0)
			a1 = Mathf.Repeat(q1.eulerAngles.y, 360.0f);
		else  if(this.axis.z != 0)
			a1 = Mathf.Repeat(q1.eulerAngles.z, 360.0f);

		float a2 = Mathf.Repeat(q2.eulerAngles.x, 360.0f) * -1;

		return Mathf.Repeat(a2 - a1, 360.0f);
	}

	public float GetAngleDifference() {
		return (Mathf.Repeat(GetProperTarget() - GetJointAngle() + 180, 360.0f) - 180.0f);
	}

	public float GetProperTarget() {
		return Mathf.Min(Mathf.Max(this.targetAngle, this.minAngle), this.maxAngle);
	}

	public bool IsAtTarget(bool exact) {
		// Check whether the angle has to be exact
		if(exact)
			return (GetAngleDifference() == 0.0f);

		// Check whether the angle is at the target position (angle error allowed)
		return (Mathf.Abs(GetAngleDifference()) <= this.maxAngleErr);
	}
}
