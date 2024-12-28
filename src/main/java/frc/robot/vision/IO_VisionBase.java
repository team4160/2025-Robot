// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import edu.wpi.first.math.geometry.Pose2d;
import java.util.Optional;
import org.littletonrobotics.junction.AutoLog;
import org.photonvision.EstimatedRobotPose;

public interface IO_VisionBase {
	@AutoLog
	public static class VisionInputs {
		public double timestamp;

		public boolean hasLeftTarget = false;
		public boolean hasRightTarget = false;
		public boolean hasCenterTarget = false;

		public double leftLatencyMS = 0.0;
		public double rightLatencyMS = 0.0;
		public double centerLatencyMS = 0.0;

		public double leftBestTargetID = -1.0;
		public double rightBestTargetID = -1.0;
		public double centerBestTargetID = -1.0;
	}

	public void updateInputs(VisionInputs inputs);

	public void updatePoseEstimation(Pose2d currentPose);

	public Optional<EstimatedRobotPose> getEstimatedGlobalPose();
}
