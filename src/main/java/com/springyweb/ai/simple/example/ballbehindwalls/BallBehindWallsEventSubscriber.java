package com.springyweb.ai.simple.example.ballbehindwalls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.inject.internal.guava.eventbus.Subscribe;
import com.springyweb.ai.simple.model.Actor;
import com.springyweb.ai.simple.model.robot.AbstractRobotEventSubscriber;
import com.springyweb.ai.simple.model.robot.ItemCollectedEvent;
import com.springyweb.ai.simple.model.robot.PostRobotInitialisedEvent;
import com.springyweb.ai.simple.model.robot.PostRobotUpdateEvent;
import com.springyweb.ai.simple.model.robot.PreRobotUpdateEvent;
import com.springyweb.ai.simple.model.robot.Robot;
import com.springyweb.ai.simple.model.robot.RobotCollisionEvent;
import com.springyweb.ai.simple.model.robot.TwoWheeledRobot;

public class BallBehindWallsEventSubscriber extends AbstractRobotEventSubscriber {
	private static final Logger LOG = LoggerFactory.getLogger(BallBehindWallsEventSubscriber.class);
	private float leftWheelSpeed;
	private float rightWheelSpeed;
	
	@Subscribe
	public void handlePostInitialisation(PostRobotInitialisedEvent event) {
		TwoWheeledRobot robot = (TwoWheeledRobot)event.getRobot();
		robot.addMetadata(BallBehindWallsMetaData.BALL_COLLECTION_TIME, new Double(Double.MAX_VALUE));
		robot.addMetadata(BallBehindWallsMetaData.ROBOT_STRAIGHT_FORWARD_TIME, new Long(0l));
		robot.addMetadata(BallBehindWallsMetaData.ROBOT_COLLISIONS, new Long(0l));
	}
	
	@Subscribe
	public void handleItemCollected(ItemCollectedEvent event) {
		TwoWheeledRobot robot = (TwoWheeledRobot)event.getRobot();
		Actor collectedItem = event.getCollectedItem();
		LOG.info("Received ItemCollectedEvent: Robot: " + robot + " Item collected: " + collectedItem);
		Long stepCount = robot.getStepCount();
		robot.addMetadata(BallBehindWallsMetaData.BALL_COLLECTION_TIME, Double.valueOf(stepCount.doubleValue()));
	}
	
	@Subscribe
    public void handlePreRobotUpdateEvent(PreRobotUpdateEvent preRobotUpdateEvent) {
		Robot robot = preRobotUpdateEvent.getRobot();
		leftWheelSpeed = robot.getLeftWheelSpeed();
		rightWheelSpeed = robot.getRightWheelSpeed();
    }
	
	@Subscribe
    public void handlePostRobotUpdateEvent(PostRobotUpdateEvent postRobotUpdateEvent) {
		Robot robot = postRobotUpdateEvent.getRobot();
		if(leftWheelSpeed > 0 && leftWheelSpeed == rightWheelSpeed && leftWheelSpeed==robot.getLeftWheelSpeed() && rightWheelSpeed == robot.getRightWheelSpeed()) {
			Long straightForwardTime = (Long)robot.getMetadataValue(BallBehindWallsMetaData.ROBOT_STRAIGHT_FORWARD_TIME);
			if(straightForwardTime < Long.MAX_VALUE) {
				straightForwardTime = straightForwardTime + 1;
				robot.addMetadata(BallBehindWallsMetaData.ROBOT_STRAIGHT_FORWARD_TIME, straightForwardTime);
			}
		}	
    }
	
	@Subscribe
	public void handleRobotCollisionEvent(RobotCollisionEvent robotCollisionEvent) {
		Robot robot = robotCollisionEvent.getRobot();
		Long collisions = (Long)robot.getMetadataValue(BallBehindWallsMetaData.ROBOT_COLLISIONS);
		if(collisions < Long.MAX_VALUE) {
			collisions = collisions + 1;
			robot.addMetadata(BallBehindWallsMetaData.ROBOT_COLLISIONS, collisions);
		}
	}
}	