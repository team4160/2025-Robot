// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import java.util.ArrayList;
import java.util.List;
import org.photonvision.targeting.PhotonTrackedTarget;

public class CameraState {
	// Basic state
	private boolean hasTarget = false;
	private boolean isConnected = false;
	private double lastFrameTimestamp = 0.0;

	// Target tracking
	private PhotonTrackedTarget bestApriltag = null;
	private final List<PhotonTrackedTarget> apriltags = new ArrayList<>();

	private String cameraName = "";

	public CameraState(String cameraName) {
		this.cameraName = cameraName;
	}

	public void updateState(
			boolean connected,
			boolean hasTarget,
			double lastFrameTimestamp,
			PhotonTrackedTarget bestApriltag,
			List<PhotonTrackedTarget> apriltags) {
		this.isConnected = connected;
		this.hasTarget = hasTarget;
		this.lastFrameTimestamp = lastFrameTimestamp;
		this.bestApriltag = bestApriltag;
		this.apriltags.clear();
		if (apriltags != null) {
			this.apriltags.addAll(apriltags);
		}
	}

	// Getters with additional logging-friendly methods
	public boolean getHasTarget() {
		return hasTarget;
	}

	public boolean getIsConnected() {
		return isConnected;
	}

	public double getLastFrameTimestamp() {
		return lastFrameTimestamp;
	}

	public List<PhotonTrackedTarget> getTrackedTargets() {
		return List.copyOf(apriltags);
	}

	public PhotonTrackedTarget getBestTarget() {
		return bestApriltag;
	}

	public String getCameraName() {
		return cameraName;
	}

	// Detailed logging methods
	public int getBestTargetId() {
		return bestApriltag != null ? bestApriltag.getFiducialId() : -1;
	}

	public double getBestTargetYaw() {
		return bestApriltag != null ? bestApriltag.getYaw() : 0.0;
	}

	public double getBestTargetPitch() {
		return bestApriltag != null ? bestApriltag.getPitch() : 0.0;
	}
}
