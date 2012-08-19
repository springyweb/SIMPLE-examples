package com.springyweb.ai.simple.example.ballbehindwalls;

import java.util.ArrayList;
import java.util.List;

import com.springyweb.ai.simple.model.robot.sensor.RayPointConfig;
import com.springyweb.ai.simple.model.robot.sensor.SonarSensorSegment;
import com.springyweb.ai.simple.model.robot.sensor.SonarSensorSegmentListProvider;

public class BallBehindWallsRobotContactSensorListProvider implements SonarSensorSegmentListProvider {
	
	private static final float SONAR_SENSOR_RAY_LENGTH = 1f;
	
	private static final int SONAR_SENSOR_START_DEGREE = 315;
	private static final int SONAR_SENSOR_END_DEGREE = 45;
	private static final int SONAR_SENSOR_INTERVAL_DEGREE = 5;

	@Override
	public List<SonarSensorSegment> get() {
		List<SonarSensorSegment> sonarSensorSegmets = new ArrayList<SonarSensorSegment>();
		sonarSensorSegmets.add(new SonarSensorSegment(new RayPointConfig(SONAR_SENSOR_RAY_LENGTH, SONAR_SENSOR_START_DEGREE, SONAR_SENSOR_END_DEGREE, SONAR_SENSOR_INTERVAL_DEGREE)));
		return sonarSensorSegmets;
	}
}