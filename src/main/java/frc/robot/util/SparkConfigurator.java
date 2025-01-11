// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.util;

import com.revrobotics.spark.SparkMax;

public class SparkConfigurator {

	public static void configureSparkMaxForPID(SparkMax sparkMax, double p, double i, double d) {}

	/*
			SparkMaxConfig sparkMaxConfig = new SparkMaxConfig();
			ClosedLoopConfig closedLoopConfig = new ClosedLoopConfig();

			closedLoopConfig.apply(
					new MAXMotionConfig().
			)



			sparkMax.configure(

			new SparkMaxConfig().apply(
					new ClosedLoopConfig().apply
			),

			), SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kPersistParameters);
	}
			*/
}
