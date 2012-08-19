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
			input.setData(inputIndex, (contactSensorResult.isHit()) ? 1 : 0 );
		} 
		
		//The remaining 19 neurons are the robot sonar sensors
		
		List<SonarSensorSegment> sonarSensorSegments = robot.getSonarSensorSegments();
		for (SonarSensorSegment sonarSensorSegment : sonarSensorSegments) {
			List<SonarSensor> sensors = sonarSensorSegment.getSensors();
			for (SonarSensor sonarSensor : sensors) {
				SonarSensorResult sonarResult = sonarSensor.sense();
				addRayInfo(sonarResult.getRayInfo());
				input.setData(inputIndex, (sonarResult.isHit()) ? 1f : 0f);
				inputIndex ++;
			}
		}
		
		return new NeuralNetworkOutput(network.compute(input));
	}
	
	@Override
	public void actuate(NeuralNetworkOutput networkOutput,TwoWheeledRobot robot) {
		float leftWheelSpeed = 0.25f;
		float rightWheelSpeed = 0.25f;
		
		float leftTen = 0f;
		float middledTen = 0f;
		float rightTen = 0f;
		
		for (int i = 0; i < 10; i++) {
			leftTen += networkOutput.getNeuronValue(i);
		}
		
		for (int i = 10; i < 20; i++) {
			middledTen += networkOutput.getNeuronValue(i);
		}
		
		for (int i = 20; i < 30; i++) {
			rightTen += networkOutput.getNeuronValue(i);
		}
		
		float contact1 = networkOutput.getNeuronValue(30);
		float contact2 = networkOutput.getNeuronValue(31);
		float contact3 = networkOutput.getNeuronValue(32);
		float contact4 = networkOutput.getNeuronValue(33);
		
		float frontContact = contact1 + contact4;
		float rearContact = contact2 + contact3;
		
		if(frontContact > 0 || rearContact > 0) {
			robot.getLeftWheel().setSpeed(-0.25f);
			robot.getRightWheel().setSpeed(0.25f);
		} else { 
		
			if(middledTen < rightTen && middledTen < leftTen) {
				//Straight ahead
				leftWheelSpeed = 0.25f;
				rightWheelSpeed = 0.25f;
			} else if (leftTen < rightTen) {
				leftWheelSpeed = -0.25f;
				rightWheelSpeed = 0.25f;
			} else if (rightTen < leftTen) {
				leftWheelSpeed = 0.25f;
				rightWheelSpeed = -0.25f;
			} else {
				leftWheelSpeed = 0.25f;
				rightWheelSpeed = 0.25f;
			}
			
			robot.getLeftWheel().setSpeed(leftWheelSpeed);
			robot.getRightWheel().setSpeed(rightWheelSpeed);
			
			robot.getLeftWheel().setSpeed(0.25f);
			robot.getRightWheel().setSpeed(0.25f);
		}
	}
}