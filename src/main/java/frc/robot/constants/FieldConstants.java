// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.constants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class FieldConstants {

	public static final Pose2d BLUE_TOP_TOP_LEFT = new Pose2d(6, 3.940, Rotation2d.fromDegrees(-90));
	public static final Pose2d BLUE_TOP_TOP_RIGHT = new Pose2d(6, 3.620, Rotation2d.fromDegrees(-90));

	public static final Pose2d BLUE_TOP_LEFT_TOP =
			new Pose2d(5.586, 5.1, Rotation2d.fromDegrees(-30));
	public static final Pose2d BLUE_TOP_LEFT_BOTTOM =
			new Pose2d(5.308, 5.268, Rotation2d.fromDegrees(-30));
}
