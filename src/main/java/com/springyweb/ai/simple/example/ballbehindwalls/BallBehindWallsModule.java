package com.springyweb.ai.simple.example.ballbehindwalls;

import java.util.List;

import org.encog.engine.network.activation.ActivationBiPolar;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.pattern.FeedForwardPattern;
import org.jbox2d.dynamics.World;

import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.mycila.inject.jsr250.Jsr250Injector;
import com.springyweb.ai.simple.model.Ground;
import com.springyweb.ai.simple.model.collectable.Ball;
import com.springyweb.ai.simple.model.inactive.Wall;
import com.springyweb.ai.simple.model.robot.BasicTwoWheelMotor;
import com.springyweb.ai.simple.model.robot.RobotController;
import com.springyweb.ai.simple.model.robot.RobotScorer;
import com.springyweb.ai.simple.model.robot.TwoWheelMotor;
import com.springyweb.ai.simple.model.robot.TwoWheeledRobot;
import com.springyweb.ai.simple.model.robot.sensor.ContactSensor;
import com.springyweb.ai.simple.model.robot.sensor.ContactSensorRing;
import com.springyweb.ai.simple.model.robot.sensor.SensorRing;
import com.springyweb.ai.simple.model.robot.sensor.SonarSensorSegment;
import com.springyweb.ai.simple.module.AbstractInitializingRobotModule;

public class BallBehindWallsModule extends AbstractInitializingRobotModule {
	
	private static final String NAMED_ID = "id";
	private static final String NAMED_DENSITY = "density";
	private static final String NAMED_INIT_POSITION_X = "initPositionX";
	private static final String NAMED_INIT_POSITION_Y = "initPositionY";
	
	public static final int NETWORK_INPUT_NEURON_COUNT = 23;
	public static final int NETWORK_OUTPUT_NEURON_COUNT = 34;
	private static final int NETWORK_HIDDEN_LAYER_COUNT = 3;
	
	private static final String NAMED_BALL_RADIUS = "ballRadius";
	private static final String BALL_ID = "Ball";
	private static final float BALL_INIT_POSITION_X = 0f;
	private static final float BALL_INIT_POSITION_Y = 5f;
	private static final float BALL_RADIUS = 0.1f;
	
	private static final String NAMED_ROBOT_SCORER = "robotScorer";
	private static final String NAMED_ROBOT_CONTROLLER = "robotController";
	private static final String NAMED_INIT_ROBOT_ANGLE = "initRobotAngle";
	private static final String NAMED_ROBOT_RADIUS = "robotRadius";
	private static final String ROBOT1_NAME = "Robot1";
	private static final float ROBOT1_INIT_POSITION_X = 0f;
	private static final float ROBOT1_INIT_POSITION_Y = 1f;
	private static final double ROBOT1_INIT_ANGLE = 45d;
	private static final float ROBOT1_RADIUS = 0.3f;
	
	private static final String NAMED_WALL_HEIGHT = "wallHeight";
	private static final String NAMED_WALL_WIDTH = "wallWidth";
	
	private static final String WALL1_NAME = "wall1";
	private static final float WALL1_INIT_POSITION_X = 0f;
	private static final float WALL1_INIT_POSITION_Y = 2f;
	private static final float WALL1_HEIGHT = 0.5f;
	private static final float WALL1_WIDTH = 4f;
	
	private static final String WALL2_NAME = "wall2";
	private static final float WALL2_INIT_POSITION_X = -4f;
	private static final float WALL2_INIT_POSITION_Y = 4f;
	private static final float WALL2_HEIGHT = 2f;
	private static final float WALL2_WIDTH = 0.5f;
	
	private static final String NAMED_GROUND_LENGTH = "groundLength";
	private static final float GROUND_INIT_POSITION_X = 0f;
	private static final float GROUND_INIT_POSITION_Y = 0f;
	private static final float GROUND_LENGTH = 10f;
	
	private static final String NAMED_CONTACT_SENSOR_COUNT = "contactSensorCount";
	private static final int CONTACT_SENSOR_COUNT = 4;
	
	public BallBehindWallsModule(World world) {
		super(world);
	}
	
	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named(NAMED_DENSITY)).to(1f);
		configureGround();
		configureWalls();
		configureContactSensors();
        configureRobots();
        configureBall();
	}

	private void configureGround() {
		install(new PrivateModule() {		
			@Override
			protected void configure() {
				bind(Ground.class);
		        expose(Ground.class);
				bindConstant().annotatedWith(Names.named(NAMED_INIT_POSITION_X)).to(GROUND_INIT_POSITION_X);
		        bindConstant().annotatedWith(Names.named(NAMED_INIT_POSITION_Y)).to(GROUND_INIT_POSITION_Y);
		        bindConstant().annotatedWith(Names.named(NAMED_GROUND_LENGTH)).to(GROUND_LENGTH);
		        
			}
		});
	}
	
	private void configureWalls() {
		install(new PrivateModule() {		
			@Override
			protected void configure() {
				bind(Wall.class).annotatedWith(Names.named(WALL1_NAME)).to(Wall.class);
		        expose(Wall.class).annotatedWith(Names.named(WALL1_NAME));
				bindConstant().annotatedWith(Names.named(NAMED_INIT_POSITION_X)).to(WALL1_INIT_POSITION_X);
		        bindConstant().annotatedWith(Names.named(NAMED_INIT_POSITION_Y)).to(WALL1_INIT_POSITION_Y);
		        bindConstant().annotatedWith(Names.named(NAMED_WALL_WIDTH)).to(WALL1_WIDTH);
		        bindConstant().annotatedWith(Names.named(NAMED_WALL_HEIGHT)).to(WALL1_HEIGHT);
			}
		});
		
		install(new PrivateModule() {
			@Override
			protected void configure() {
				bind(Wall.class).annotatedWith(Names.named(WALL2_NAME)).to(Wall.class);
		        expose(Wall.class).annotatedWith(Names.named(WALL2_NAME));
				bindConstant().annotatedWith(Names.named(NAMED_INIT_POSITION_X)).to(WALL2_INIT_POSITION_X);
		        bindConstant().annotatedWith(Names.named(NAMED_INIT_POSITION_Y)).to(WALL2_INIT_POSITION_Y);
		        bindConstant().annotatedWith(Names.named(NAMED_WALL_WIDTH)).to(WALL2_WIDTH);
		        bindConstant().annotatedWith(Names.named(NAMED_WALL_HEIGHT)).to(WALL2_HEIGHT);
			}
		});
	}
	
	private void configureContactSensors() {
		install(new PrivateModule() {		
			@Override
			protected void configure() {
				bind(new TypeLiteral<SensorRing<ContactSensor>>(){}).to(ContactSensorRing.class);
		        expose(new TypeLiteral<SensorRing<ContactSensor>>(){});
				bindConstant().annotatedWith(Names.named(NAMED_CONTACT_SENSOR_COUNT)).to(CONTACT_SENSOR_COUNT);
			}
		});
	}

	private void configureRobots() {
		install(new PrivateModule() {
			@Override
			protected void configure() {
				bind(TwoWheeledRobot.class).annotatedWith(Names.named(ROBOT1_NAME)).to(TwoWheeledRobot.class);
		        expose(TwoWheeledRobot.class).annotatedWith(Names.named(ROBOT1_NAME));
		        bind(new TypeLiteral<List<SonarSensorSegment>>(){}).toProvider(new BallBehindWallsRobotSensorListProvider());
		        bindConstant().annotatedWith(Names.named(NAMED_INIT_POSITION_X)).to(ROBOT1_INIT_POSITION_X);
		        bindConstant().annotatedWith(Names.named(NAMED_INIT_POSITION_Y)).to(ROBOT1_INIT_POSITION_Y);
		        bindConstant().annotatedWith(Names.named(NAMED_ROBOT_RADIUS)).to(ROBOT1_RADIUS);
		        bindConstant().annotatedWith(Names.named(NAMED_INIT_ROBOT_ANGLE)).to(ROBOT1_INIT_ANGLE);
		        bind(RobotScorer.class).annotatedWith(Names.named(NAMED_ROBOT_SCORER)).to(BallBehindWallsRobotScorer.class);
		        bind(RobotController.class).annotatedWith(Names.named(NAMED_ROBOT_CONTROLLER)).to(BallBehindWallsRobotController.class);
		        bindConstant().annotatedWith(Names.named(NAMED_ID)).to(ROBOT1_NAME);
			}
		});
	}
	
	private void configureBall() {
		install(new PrivateModule() {		
			@Override
			protected void configure() {				
				bind(Ball.class);
		        expose(Ball.class);
				bindConstant().annotatedWith(Names.named(NAMED_INIT_POSITION_X)).to(BALL_INIT_POSITION_X);
		        bindConstant().annotatedWith(Names.named(NAMED_INIT_POSITION_Y)).to(BALL_INIT_POSITION_Y);
		        bindConstant().annotatedWith(Names.named(NAMED_BALL_RADIUS)).to(BALL_RADIUS);
		        bindConstant().annotatedWith(Names.named(NAMED_ID)).to(BALL_ID);
			}
		});
	}
	
	@Provides BasicNetwork provideNetwork() {
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(NETWORK_INPUT_NEURON_COUNT);
		pattern.addHiddenLayer(NETWORK_HIDDEN_LAYER_COUNT);
		pattern.setOutputNeurons(NETWORK_OUTPUT_NEURON_COUNT);
		pattern.setActivationFunction(new ActivationTANH());
		pattern.setActivationOutput(new ActivationBiPolar());
		BasicNetwork network = (BasicNetwork)pattern.generate();
		network.reset();
		return network;
	}
	
	@Provides TwoWheelMotor provideMotor() {
		return new BasicTwoWheelMotor();
	}

	@Override
	public TwoWheeledRobot init() {
		Jsr250Injector injector = getInjector();
		injector.getInstance(BallBehindWallsEventSubscriber.class);
		injector.getInstance(Ground.class);
		injector.getInstance(Ball.class);
		injector.getInstance(Key.get(Wall.class, Names.named(WALL1_NAME)));
		injector.getInstance(Key.get(Wall.class, Names.named(WALL2_NAME)));
	    return injector.getInstance(Key.get(TwoWheeledRobot.class, Names.named(ROBOT1_NAME)));
	}
}