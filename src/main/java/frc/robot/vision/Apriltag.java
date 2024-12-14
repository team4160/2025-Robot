// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import org.photonvision.targeting.PhotonTrackedTarget;

public class Apriltag {
	private final int id;
	private final String cameraName;
	private final Transform3d bestCameraToTarget;
	private final double ambiguity;
	private final double area;

	public Apriltag(PhotonTrackedTarget target, String cameraName) {
		this.id = target.getFiducialId();
		this.cameraName = cameraName;
		this.bestCameraToTarget = target.getBestCameraToTarget();
		this.ambiguity = target.getPoseAmbiguity();
		this.area = target.getArea();
	}

	public int getId() {
		return id;
	}

	public String getCameraName() {
		return cameraName;
	}

	public Translation3d getTranslation() {
		return bestCameraToTarget.getTranslation();
	}

	public Rotation3d getRotation() {
		return bestCameraToTarget.getRotation();
	}

	public double getAmbiguity() {
		return ambiguity;
	}

	public double getArea() {
		return area;
	}
}
