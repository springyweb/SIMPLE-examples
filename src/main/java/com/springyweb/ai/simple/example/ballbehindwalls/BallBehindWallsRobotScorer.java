package com.springyweb.ai.simple.example.ballbehindwalls;

import com.springyweb.ai.simple.model.robot.Robot;
import com.springyweb.ai.simple.model.robot.RobotScorer;

public class BallBehindWallsRobotScorer implements RobotScorer {

	@Override
	public double getScore(Robot robot) {
		Double time = (Double)robot.getMetadataValue(BallBehindWallsMetaData.BALL_COLLECTION_TIME);
		double ret = Double.MAX_VALUE;
		if(time != null) {
			ret = time;
		}
		return ret;
	}
}