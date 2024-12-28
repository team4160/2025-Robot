// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.constants;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;

public final class CameraConstants {
	public static final double MAXIMUM_AMBIGUITY = 0.25;

	public enum Camera {
		LEFT_CAM(
				"left",
				new Rotation3d(0, Math.toRadians(-24.094), Math.toRadians(30)),
				new Translation3d(
						Units.inchesToMeters(12.056), Units.inchesToMeters(10.981), Units.inchesToMeters(8.44)),
				VecBuilder.fill(4, 4, 8),
				VecBuilder.fill(0.5, 0.5, 1)),
		RIGHT_CAM(
				"right",
				new Rotation3d(0, Math.toRadians(-24.094), Math.toRadians(-30)),
				new Translation3d(
						Units.inchesToMeters(12.056),
						Units.inchesToMeters(-10.981),
						Units.inchesToMeters(8.44)),
				VecBuilder.fill(4, 4, 8),
				VecBuilder.fill(0.5, 0.5, 1)),
		CENTER_CAM(
				"center",
				new Rotation3d(0, Units.degreesToRadians(18), 0),
				new Translation3d(
						Units.inchesToMeters(-4.628),
						Units.inchesToMeters(-10.687),
						Units.inchesToMeters(16.129)),
				VecBuilder.fill(4, 4, 8),
				VecBuilder.fill(0.5, 0.5, 1));

		public final String name;
		public final Rotation3d rotation;
		public final Translation3d translation;
		public final Matrix<N3, N1> singleTagStdDevs;
		public final Matrix<N3, N1> multiTagStdDevs;

		Camera(
				String name,
				Rotation3d rotation,
				Translation3d translation,
				Matrix<N3, N1> singleTagStdDevs,
				Matrix<N3, N1> multiTagStdDevs) {
			this.name = name;
			this.rotation = rotation;
			this.translation = translation;
			this.singleTagStdDevs = singleTagStdDevs;
			this.multiTagStdDevs = multiTagStdDevs;
		}
	}
}
