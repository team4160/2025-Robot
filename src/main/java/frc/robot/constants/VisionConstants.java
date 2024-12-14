// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.constants;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.geometry.Rotation3d;
import java.util.Map;

public final class VisionConstants {
	private static final Map<String, Transform3d> CAMERA_TRANSFORMS =
			Map.of(
					"Front", 
					new Transform3d(
						new Translation3d(0.5, 0.0, 0.5),  // x forward, y left, z up
						new Rotation3d(0, 0, 0)),          // roll, pitch, yaw
					"Left", 
					new Transform3d(
						new Translation3d(0.0, 0.25, 0.5), 
						new Rotation3d(0, 0, Math.PI/2)),  // 90 degrees left
					"Right", 
					new Transform3d(
						new Translation3d(0.0, -0.25, 0.5),
						new Rotation3d(0, 0, -Math.PI/2))); // 90 degrees right

	public static Transform3d getCameraTransform(String cameraName) {
		return CAMERA_TRANSFORMS.getOrDefault(cameraName, new Transform3d());
	}
}
