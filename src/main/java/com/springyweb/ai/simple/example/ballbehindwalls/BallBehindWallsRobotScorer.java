package com.springyweb.ai.simple.example.ballbehindwalls;

import com.springyweb.ai.simple.model.robot.Robot;
import com.springyweb.ai.simple.model.robot.RobotScorer;

public class BallBehindWallsRobotScorer implements RobotScorer {
	
	@Override
	public double getScore(Robot robot) {
		
		Double ret = 0d;
		
		Long straightForwardTime = (Long) robot.getMetadataValue(BallBehindWallsMetaData.ROBOT_STRAIGHT_FORWARD_TIME);
		if(straightForwardTime != null) {
			ret += straightForwardTime; 
		}
		
		//Collisions are bad
		Long collisions = (Long) robot.getMetadataValue(BallBehindWallsMetaData.ROBOT_COLLISIONS);
		if(collisions != null) {
			ret += (-1 * collisions); 
		}
		
		//Ball collection is the greatest achievement and must be rewarded
		Double time = (Double)robot.getMetadataValue(BallBehindWallsMetaData.BALL_COLLECTION_TIME);
		if(time != null && time < Double.MAX_VALUE) {
			System.out.println("Ball was located");
			ret += time * 10000000;
		}
		
		return ret;
	}
}