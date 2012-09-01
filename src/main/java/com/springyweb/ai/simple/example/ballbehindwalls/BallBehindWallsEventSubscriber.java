package com.springyweb.ai.simple.example.ballbehindwalls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.inject.internal.guava.eventbus.Subscribe;
import com.springyweb.ai.simple.model.Actor;
import com.springyweb.ai.simple.model.robot.TrainableRobot;
import com.springyweb.ai.simple.model.robot.TwoWheelDrive;
import com.springyweb.ai.simple.model.robot.TwoWheeledRobot;
import com.springyweb.ai.simple.model.robot.event.AbstractRobotEventSubscriber;
import com.springyweb.ai.simple.model.robot.event.ItemCollectedEvent;
import com.springyweb.ai.simple.model.robot.event.PostRobotInitialisedEvent;
import com.springyweb.ai.simple.model.robot.event.PostRobotUpdateEvent;
import com.springyweb.ai.simple.model.robot.event.PreRobotUpdateEvent;
import com.springyweb.ai.simple.model.robot.event.RobotCollisionEvent;

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
		TrainableRobot robot = preRobotUpdateEvent.getRobot();
		TwoWheelDrive drive = (TwoWheelDrive)robot.getController().getDrive();
		leftWheelSpeed = drive.getLeftWheelSpeed();
		rightWheelSpeed = drive.getRightWheelSpeed();
    }
	
	@Subscribe
    public void handlePostRobotUpdateEvent(PostRobotUpdateEvent postRobotUpdateEvent) {
		TrainableRobot robot = postRobotUpdateEvent.getRobot();
		TwoWheelDrive drive = (TwoWheelDrive)robot.getController().getDrive();
		if(leftWheelSpeed > 0 && leftWheelSpeed == rightWheelSpeed && leftWheelSpeed==drive.getLeftWheelSpeed() && rightWheelSpeed == drive.getRightWheelSpeed()) {
			Long straightForwardTime = (Long)robot.getMetadataValue(BallBehindWallsMetaData.ROBOT_STRAIGHT_FORWARD_TIME);
			if(straightForwardTime < Long.MAX_VALUE) {
				straightForwardTime = straightForwardTime + 1;
				robot.addMetadata(BallBehindWallsMetaData.ROBOT_STRAIGHT_FORWARD_TIME, straightForwardTime);
			}
		}	
    }
	
	@Subscribe
	public void handleRobotCollisionEvent(RobotCollisionEvent robotCollisionEvent) {
		TrainableRobot robot = robotCollisionEvent.getRobot();
		Long collisions = (Long)robot.getMetadataValue(BallBehindWallsMetaData.ROBOT_COLLISIONS);
		if(collisions < Long.MAX_VALUE) {
			collisions = collisions + 1;
			robot.addMetadata(BallBehindWallsMetaData.ROBOT_COLLISIONS, collisions);
		}
	}
}	