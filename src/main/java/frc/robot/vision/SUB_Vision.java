// Copyright (c) 2024 - 2025 : FRC 2106 : The Junkyard Dogs
// https://www.team2106.org

// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package frc.robot.vision;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.vision.IO_VisionBase.VisionInputs;

import org.littletonrobotics.junction.Logger;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.photonvision.targeting.PhotonTrackedTarget;

public class SUB_Vision extends SubsystemBase {
	private final IO_VisionBase io;
	private final VisionInputs inputs = new VisionInputs();

	// Cache of best AprilTags (ID -> Tag)
	private final Map<Integer, Apriltag> currentTags = new HashMap<>();

	public SUB_Vision(IO_VisionBase io) {
		this.io = io;
	}

	@Override
	public void periodic() {
		io.updateInputs(inputs);
		Logger.processInputs("Vision", inputs);
		
		// Update current tags
		updateCurrentTags();
	}

	/**
	 * Updates the current tags map with the best (least ambiguous) tags from all cameras
	 */
	private void updateCurrentTags() {
		currentTags.clear();
		
		// Get all tags from all cameras
		List<Apriltag> allTags = List.of("Front", "Left", "Right").stream()
			.filter(this::isCameraWorking)  // Only use working cameras
			.flatMap(camera -> getTrackedTags(camera).stream())
			.collect(Collectors.toList());
			
		// Group tags by ID and keep the least ambiguous one
		allTags.forEach(tag -> {
			currentTags.compute(tag.getId(), (id, existingTag) -> {
				if (existingTag == null || tag.getAmbiguity() < existingTag.getAmbiguity()) {
					return tag;
				}
				return existingTag;
			});
		});
	}

	/**
	 * Get all currently visible AprilTags across all cameras, with duplicates removed
	 * @return List of unique AprilTags, keeping the least ambiguous when there are duplicates
	 */
	public List<Apriltag> getAllVisibleTags() {
		return List.copyOf(currentTags.values());
	}

	/**
	 * Get a specific AprilTag by ID if it's currently visible
	 * @param id The AprilTag ID to look for
	 * @return The AprilTag if visible, null if not found
	 */
	public Apriltag getTagById(int id) {
		return currentTags.get(id);
	}

	/**
	 * Get all tracked AprilTags from a specific camera
	 * @param camera The camera name ("Front", "Left", or "Right")
	 * @return List of AprilTags, empty if none found
	 */
	public List<Apriltag> getTrackedTags(String camera) {
		List<PhotonTrackedTarget> targets = switch (camera) {
			case "Front" -> inputs.frontCamera.getTrackedTargets();
			case "Left" -> inputs.leftCamera.getTrackedTargets();
			case "Right" -> inputs.rightCamera.getTrackedTargets();
			default -> List.of();
		};

		return targets.stream()
			.map(target -> new Apriltag(target, camera))
			.collect(Collectors.toList());
	}

	/**
	 * Get the best AprilTag from a specific camera
	 * @param camera The camera name ("Front", "Left", or "Right")
	 * @return The best AprilTag, or null if none found
	 */
	public Apriltag getBestTag(String camera) {
		PhotonTrackedTarget bestTarget = switch (camera) {
			case "Front" -> inputs.frontCamera.getBestTarget();
			case "Left" -> inputs.leftCamera.getBestTarget();
			case "Right" -> inputs.rightCamera.getBestTarget();
			default -> null;
		};

		return bestTarget != null ? new Apriltag(bestTarget, camera) : null;
	}

	/**
	 * Check if a camera is connected and enabled
	 * @param camera The camera name ("Front", "Left", or "Right")
	 * @return true if camera is working
	 */
	public boolean isCameraWorking(String camera) {
		return switch (camera) {
			case "Front" -> inputs.frontCamera.isConnected && inputs.frontCamera.isOn;
			case "Left" -> inputs.leftCamera.isConnected && inputs.leftCamera.isOn;
			case "Right" -> inputs.rightCamera.isConnected && inputs.rightCamera.isOn;
			default -> false;
		};
	}
}
