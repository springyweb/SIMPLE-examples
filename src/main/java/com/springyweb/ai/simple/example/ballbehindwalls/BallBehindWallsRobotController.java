package com.springyweb.ai.simple.example.ballbehindwalls;

import java.util.List;

import org.encog.ml.data.MLData;
import org.encog.neural.networks.BasicNetwork;

import com.springyweb.ai.simple.model.robot.AbstractRobotController;
import com.springyweb.ai.simple.model.robot.NeuralNetworkOutput;
import com.springyweb.ai.simple.model.robot.TwoWheeledRobot;
import com.springyweb.ai.simple.model.robot.sensor.ContactSensor;
import com.springyweb.ai.simple.model.robot.sensor.ContactSensorResult;
import com.springyweb.ai.simple.model.robot.sensor.SonarSensor;
import com.springyweb.ai.simple.model.robot.sensor.SonarSensorResult;
import com.springyweb.ai.simple.model.robot.sensor.SonarSensorSegment;

public class BallBehindWallsRobotController extends AbstractRobotController {
	
	@Override
	public NeuralNetworkOutput sense(TwoWheeledRobot robot) {
		
		//Get the contact sensors
		BasicNetwork network = robot.getNetwork();
		MLData input = createNeworkInput(23);
		
		//The first four neurons are contact sensors
		
		List<ContactSensor> contactSensors = robot.getContactSensorRing().getSensors();
		int inputIndex;
		for(inputIndex = 0 ; inputIndex < contactSensors.size() ; inputIndex++) {
			ContactSensor contactSensor = contactSensors.get(inputIndex);
			ContactSensorResult contactSensorResult = contactSensor.sense();
			float inputData = contactSensorResult.isHit() ? 1f : 0f;
			input.setData(inputIndex, inputData);
		} 
		
		//The remaining 19 neurons are the robot sonar sensors
		
		List<SonarSensorSegment> sonarSensorSegments = robot.getSonarSensorSegments();
		for (SonarSensorSegment sonarSensorSegment : sonarSensorSegments) {
			List<SonarSensor> sensors = sonarSensorSegment.getSensors();
			for (SonarSensor sonarSensor : sensors) {
				SonarSensorResult sonarResult = sonarSensor.sense();
				addRayInfo(sonarResult.getRayInfo());
				float inputData = sonarResult.isHit() ? 1f : 0f;
				input.setData(inputIndex, inputData);
				inputIndex ++;
			}
		}
		
		return new NeuralNetworkOutput(network.compute(input));
	}
	
	@Override
	public void actuate(NeuralNetworkOutput networkOutput,TwoWheeledRobot robot) {
		robot.getLeftWheel().setSpeed(0.25f * networkOutput.getNeuronValue(0));
		robot.getRightWheel().setSpeed(0.25f * networkOutput.getNeuronValue(1));
	}
}