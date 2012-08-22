package com.springyweb.ai.simple.example.ballbehindwalls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationBiPolar;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.PersistBasicNetwork;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.EncogUtility;
import org.encog.util.simple.TrainingSetUtil;

public class TrainingTest {
	
	public static void main(final String args[]) {

		if (args.length == 0) {
			System.out.println("Usage:\n\nXORCSV [xor.csv]");
		} else {
			final MLDataSet trainingSet = TrainingSetUtil.loadCSVTOMemory(
					CSVFormat.ENGLISH, args[0], false, 23, 2);
			final BasicNetwork network = provideNetwork();

			System.out.println();
			System.out.println("Training Network");
			EncogUtility.trainToError(network, trainingSet, 0.01);

			System.out.println();
			System.out.println("Evaluating Network");
			EncogUtility.evaluate(network, trainingSet);
			
			System.out.println("Saving Network");
			File file = null;
			FileOutputStream fileOutputStream = null;
			try {
				  file = new File("trained-network");
				  file.createNewFile();
				  fileOutputStream = new FileOutputStream(file);
			      PersistBasicNetwork persist = new PersistBasicNetwork();
			      persist.save(fileOutputStream, network);
			    } catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
			      Encog.getInstance().shutdown();
			      IOUtils.closeQuietly(fileOutputStream);
			    }
		}
		Encog.getInstance().shutdown();
	}
	
	private static BasicNetwork provideNetwork() {
		FeedForwardPattern pattern = new FeedForwardPattern();
		pattern.setInputNeurons(23);
		pattern.addHiddenLayer(3);
		pattern.setOutputNeurons(2);
		pattern.setActivationFunction(new ActivationTANH());
		pattern.setActivationOutput(new ActivationBiPolar());
		BasicNetwork network = (BasicNetwork)pattern.generate();
		network.reset();
		return network;
	}
}