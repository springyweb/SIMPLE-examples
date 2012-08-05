package com.springyweb.ai.simple.example.ballbehindwalls;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.springyweb.ai.simple.model.Ground;
import com.springyweb.ai.simple.model.collectable.Ball;
import com.springyweb.ai.simple.model.inactive.Wall;
import com.springyweb.ai.simple.model.sensor.InclusiveFilteredProximitySensor;
import com.springyweb.ai.simple.model.sensor.Sensor;
import com.springyweb.ai.simple.model.sensor.SensorListProvider;

public class BallBehindWallsRobotSensorListProvider implements SensorListProvider {
	
	private static final float BALL_SENSOR_RAY_LENGTH = 20f;
	
	private static final int BALL_SENSOR_FRONT_LEFT_START_DEGREE = 0;
	private static final int BALL_SENSOR_FRONT_LEFT_END_DEGREE = 10;
	private static final int BALL_SENSOR_FRONT_LEFT_INTERVAL_DEGREE = 1;
	
	private static final int BALL_SENSOR_FRONT_RIGHT_START_DEGREE = 350;
	private static final int BALL_SENSOR_FRONT_RIGHT_END_DEGREE = 360;
	private static final int BALL_SENSOR_FRONT_RIGHT_INTERVAL_DEGREE = 1;
	
	private static final float OBSTACLE_SENSOR_RAY_LENGTH = 1f;
	
	private static final int OBSTACLE_SENSOR_LEFT_START_DEGREE = 0;
	private static final int OBSTACLE_SENSOR_LEFT_END_DEGREE = 180;
	private static final int OBSTACLE_SENSOR_LEFT_INTERVAL_DEGREE = 10;
	
	private static final int OBSTACLE_SENSOR_RIGHT_START_DEGREE = 180;
	private static final int OBSTACLE_SENSOR_RIGHT_END_DEGREE = 360;
	private static final int OBSTACLE_SENSOR_RIGHT_INTERVAL_DEGREE = 10;
	

	@Override
	public List<Sensor> get() {
		List<Sensor> sensors = new ArrayList<Sensor>();
		Set<Class<? extends Object>> includesTypes = new HashSet<Class<? extends Object>>();
		
		includesTypes = new HashSet<Class<? extends Object>>();
		includesTypes.add(Ball.class);
		sensors.add(new InclusiveFilteredProximitySensor(includesTypes, BALL_SENSOR_RAY_LENGTH, BALL_SENSOR_FRONT_LEFT_START_DEGREE, BALL_SENSOR_FRONT_LEFT_END_DEGREE, BALL_SENSOR_FRONT_LEFT_INTERVAL_DEGREE));
		sensors.add(new InclusiveFilteredProximitySensor(includesTypes, BALL_SENSOR_RAY_LENGTH, BALL_SENSOR_FRONT_RIGHT_START_DEGREE, BALL_SENSOR_FRONT_RIGHT_END_DEGREE, BALL_SENSOR_FRONT_RIGHT_INTERVAL_DEGREE));
		
		includesTypes = new HashSet<Class<? extends Object>>();
		includesTypes.add(Wall.class);
		includesTypes.add(Ground.class);
		sensors.add(new InclusiveFilteredProximitySensor(includesTypes, OBSTACLE_SENSOR_RAY_LENGTH, OBSTACLE_SENSOR_LEFT_START_DEGREE, OBSTACLE_SENSOR_LEFT_END_DEGREE, OBSTACLE_SENSOR_LEFT_INTERVAL_DEGREE));
		sensors.add(new InclusiveFilteredProximitySensor(includesTypes, OBSTACLE_SENSOR_RAY_LENGTH, OBSTACLE_SENSOR_RIGHT_START_DEGREE, OBSTACLE_SENSOR_RIGHT_END_DEGREE, OBSTACLE_SENSOR_RIGHT_INTERVAL_DEGREE));
						
		return sensors;
	}
}