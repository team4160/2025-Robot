// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import edu.wpi.first.math.geometry.Transform3d;
import org.littletonrobotics.junction.LogTable;
import java.util.ArrayList;
import java.util.List;
import org.photonvision.targeting.PhotonTrackedTarget;

/**
 * Efficient camera state tracking using primitive fields and reusable objects. Mutable to avoid
 * object creation during updates.
 */
public class CameraState {

	// Basic state - primitives are more efficient
	public String cameraName;
	public boolean isOn;
	public boolean hasTargets;
	public Transform3d robotToCamera;
	public int pipelineIndex;
	public boolean isConnected;
	public double lastFrameTimestamp;
	
	// Target tracking
	private PhotonTrackedTarget bestTarget;
	private final List<PhotonTrackedTarget> trackedTargets = new ArrayList<>();

	public CameraState(String name, Transform3d robotToCamera) {
		this.cameraName = name;
		this.robotToCamera = robotToCamera;
		reset();
	}

	/** Reset state to defaults without creating new objects */
	public void reset() {
		isOn = false;
		hasTargets = false;
		pipelineIndex = 0;
		isConnected = false;
		lastFrameTimestamp = 0.0;
		bestTarget = null;
		trackedTargets.clear();
	}

	/** 
	 * Update all camera state information in one call
	 * @param connected Whether the camera is connected
	 * @param on Whether the camera is enabled
	 * @param hasTarget Whether the camera sees any targets
	 * @param timestamp The timestamp of the current frame
	 * @param targets List of tracked AprilTag targets (null if none)
	 */
	public void updateState(
			boolean connected,
			boolean on,
			boolean hasTarget,
			double timestamp,
			List<PhotonTrackedTarget> targets) {
		
		// Update connection status
		this.isConnected = connected;
		this.isOn = on;
		
		// Update target information
		this.hasTargets = hasTarget;

		// Update tracked targets
		trackedTargets.clear();
		if (targets != null && !targets.isEmpty()) {
			trackedTargets.addAll(targets);
			bestTarget = targets.get(0);  // First target is best target in PhotonVision
		} else {
			bestTarget = null;
		}
		
		// Update timing
		this.lastFrameTimestamp = timestamp;
		
		// Update pipeline index based on whether we have a valid target
		this.pipelineIndex = hasTarget ? 1 : 0;
	}

	/**
	 * Get the list of currently tracked AprilTags
	 * @return Unmodifiable list of tracked targets
	 */
	public List<PhotonTrackedTarget> getTrackedTargets() {
		return List.copyOf(trackedTargets);
	}

	/**
	 * Get the best tracked target
	 * @return The best target, or null if none
	 */
	public PhotonTrackedTarget getBestTarget() {
		return bestTarget;
	}

	/** Log camera state to the provided LogTable */
	public void toLog(LogTable table) {
		table.put(cameraName + "/IsOn", isOn);
		table.put(cameraName + "/HasTargets", hasTargets);
		table.put(cameraName + "/RobotToCamera", robotToCamera);
		table.put(cameraName + "/PipelineIndex", pipelineIndex);
		table.put(cameraName + "/IsConnected", isConnected);
		table.put(cameraName + "/LastFrameTimestamp", lastFrameTimestamp);
	}

	/** Load camera state from the provided LogTable */
	public void fromLog(LogTable table) {
		isOn = table.get(cameraName + "/IsOn", isOn);
		hasTargets = table.get(cameraName + "/HasTargets", hasTargets);
		robotToCamera = table.get(cameraName + "/RobotToCamera", robotToCamera);
		pipelineIndex = table.get(cameraName + "/PipelineIndex", pipelineIndex);
		isConnected = table.get(cameraName + "/IsConnected", isConnected);
		lastFrameTimestamp = table.get(cameraName + "/LastFrameTimestamp", lastFrameTimestamp);
	}
}
