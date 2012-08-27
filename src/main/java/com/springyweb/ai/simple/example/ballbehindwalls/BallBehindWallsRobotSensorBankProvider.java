package com.springyweb.ai.simple.example.ballbehindwalls;

import java.util.ArrayList;
import java.util.List;

import com.springyweb.ai.simple.model.robot.sensor.BasicSensorBank;
import com.springyweb.ai.simple.model.robot.sensor.RayArcConfig;
import com.springyweb.ai.simple.model.robot.sensor.SensorBank;
import com.springyweb.ai.simple.model.robot.sensor.SensorBankProvider;
import com.springyweb.ai.simple.model.robot.sensor.SonarSensorArc;

public class BallBehindWallsRobotSensorBankProvider implements SensorBankProvider {
	
	private static final float SONAR_SENSOR_RAY_LENGTH = 1f;
	
	private static final int SONAR_SENSOR_START_DEGREE = 315;
	private static final int SONAR_SENSOR_END_DEGREE = 45;
	private static final int SONAR_SENSOR_INTERVAL_DEGREE = 5;

	@Override
	public SensorBank get() {
		SensorBank sensorBank = new BasicSensorBank();
		
		//Create sonar sensors
		List<SonarSensorArc> sonarSensorArcs = new ArrayList<SonarSensorArc>();
		sonarSensorArcs.add(new SonarSensorArc(new RayArcConfig(SONAR_SENSOR_RAY_LENGTH, SONAR_SENSOR_START_DEGREE, SONAR_SENSOR_END_DEGREE, SONAR_SENSOR_INTERVAL_DEGREE)));
		
		sensorBank.setSonarSensorArcs(sonarSensorArcs);
		return sensorBank;
	}
}