package com.springyweb.ai.simple.example.ballbehindwalls;

import com.springyweb.ai.simple.model.robot.RobotScorer;
import com.springyweb.ai.simple.model.robot.TwoWheeledRobot;

public class BallBehindWallsRobotScorer implements RobotScorer {

	@Override
	public double getScore(TwoWheeledRobot robot) {
		Double time = (Double)robot.getMetadataValue(BallBehindWallsMetaData.BALL_COLLECTION_TIME);
		double ret = Double.MAX_VALUE;
		if(time != null) {
			ret = time;
		}
		return ret;
	}
}