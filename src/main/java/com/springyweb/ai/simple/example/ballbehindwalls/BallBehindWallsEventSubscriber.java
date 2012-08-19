package com.springyweb.ai.simple.example.ballbehindwalls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.inject.internal.guava.eventbus.Subscribe;
import com.springyweb.ai.simple.model.Actor;
import com.springyweb.ai.simple.model.robot.AbstractRobotEventSubscriber;
import com.springyweb.ai.simple.model.robot.ItemCollectedEvent;
import com.springyweb.ai.simple.model.robot.PostRobotInitialisedEvent;
import com.springyweb.ai.simple.model.robot.TwoWheeledRobot;

public class BallBehindWallsEventSubscriber extends AbstractRobotEventSubscriber {
	private static final Logger LOG = LoggerFactory.getLogger(BallBehindWallsEventSubscriber.class);
	
	@Subscribe
	public void handlePostInitialisation(PostRobotInitialisedEvent event) {
		TwoWheeledRobot robot = (TwoWheeledRobot)event.getRobot();
		robot.addMetadata(BallBehindWallsMetaData.BALL_COLLECTION_TIME, new Double(Double.MAX_VALUE));
	}
	
	@Subscribe
	public void handleItemCollected(ItemCollectedEvent event) {
		TwoWheeledRobot robot = (TwoWheeledRobot)event.getRobot();
		Actor collectedItem = event.getCollectedItem();
		LOG.info("Received ItemCollectedEvent: Robot: " + robot + " Item collected: " + collectedItem);
		Long stepCount = robot.getStepCount();
		robot.addMetadata(BallBehindWallsMetaData.BALL_COLLECTION_TIME, Double.valueOf(stepCount.doubleValue()));
	}
}