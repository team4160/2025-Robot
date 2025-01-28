// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.misc;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.nio.file.Paths;
import java.util.List;

public class SUB_Music extends SubsystemBase {
	private Orchestra orchestra;
	private List<TalonFX> instruments;
	// private static final int[] MOTOR_IDS = {10, 9}; // Add your motor IDs here
	private static final String MUSIC_FILE = "pigges.chrp";

	private TalonFX motorOne = new TalonFX(10, "canivore");
	private TalonFX motorTwo = new TalonFX(9, "canivore");

	public SUB_Music() {
		orchestra = new Orchestra();
		orchestra.addInstrument(motorOne);
		orchestra.addInstrument(motorTwo);

		// Load the music file from deploy directory
		String deployPath = Paths.get(Filesystem.getDeployDirectory().getPath(), MUSIC_FILE).toString();
		orchestra.loadMusic(deployPath);

		orchestra.play();
	}

	@Override
	public void periodic() {
		// This method will be called once per scheduler run
	}
}
