package com.springyweb.ai.simple.example.ballbehindwalls;

import java.util.List;

import org.encog.ml.data.MLData;
import org.encog.neural.networks.BasicNetwork;

import com.springyweb.ai.simple.model.robot.AbstractRobotControllerSenseActTemplate;
import com.springyweb.ai.simple.model.robot.Drive;
import com.springyweb.ai.simple.model.robot.NeuralNetworkOutput;
import com.springyweb.ai.simple.model.robot.TwoWheelDrive;
import com.springyweb.ai.simple.model.robot.sensor.ContactSensor;
import com.springyweb.ai.simple.model.robot.sensor.ContactSensorResult;
import com.springyweb.ai.simple.model.robot.sensor.SensorBank;
import com.springyweb.ai.simple.model.robot.sensor.SonarSensor;
import com.springyweb.ai.simple.model.robot.sensor.SonarSensorArc;
import com.springyweb.ai.simple.model.robot.sensor.SonarSensorResult;

public class BallBehindWallsRobotController extends AbstractRobotControllerSenseActTemplate {
	
	@Override
	public NeuralNetworkOutput sense(SensorBank sensorBank, BasicNetwork network) {
		
		MLData input = createNeworkInput(23);
		
		//The first four neurons are contact sensors
		
		List<ContactSensor> contactSensors = sensorBank.getContactSensorRing().getSensors();
		int inputIndex;
		for(inputIndex = 0 ; inputIndex < contactSensors.size() ; inputIndex++) {
			ContactSensor contactSensor = contactSensors.get(inputIndex);
			ContactSensorResult contactSensorResult = contactSensor.sense();
			float inputData = contactSensorResult.isHit() ? 1f : 0f;
			input.setData(inputIndex, inputData);
		} 
		
		//The remaining 19 neurons are the robot sonar sensors
		
		List<SonarSensorArc> sonarSensorArcs = sensorBank.getSonarSensorArcs();
		for (SonarSensorArc sonarSensorArc : sonarSensorArcs) {
			List<SonarSensor> sensors = sonarSensorArc.getSensors();
			for (SonarSensor sonarSensor : sensors) {
				SonarSensorResult sonarResult = sonarSensor.sense();
				//addRayInfo(sonarResult.getRayInfo());
				float inputData = sonarResult.isHit() ? 1f : 0f;
				input.setData(inputIndex, inputData);
				inputIndex ++;
			}
		}
		
		return new NeuralNetworkOutput(network.compute(input));
	}

	@Override
	public void act(NeuralNetworkOutput networkOutput, Drive drive) {
		
		((TwoWheelDrive)drive).setLeftWheelSpeed(0.25f * (float) networkOutput.getNeuronValue(0));
		((TwoWheelDrive)drive).setRightWheelSpeed(0.25f * (float) networkOutput.getNeuronValue(1));
		
	}
}