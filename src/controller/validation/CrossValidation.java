package controller.validation;

import model.Model;
import model.Subset;
import model.TrainingSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 */
public class CrossValidation {
	private Integer numberVal;
	private ArrayList<Validation> validations = new ArrayList<>();
	private ArrayList<Thread> threadsCreateValidation = new ArrayList<Thread>();
	private ArrayList<Thread> threadsValidation = new ArrayList<Thread>();
	private Integer numberFeatures;
	private static final Integer ratio = 10;
	private static final Integer itMax = 15;

	public CrossValidation(TrainingSet trainingSet, ArrayList<String> language,
			Integer number_validation, Integer number_features) {
		this.numberVal = number_validation;
		this.numberFeatures = number_features;
		try {
			init(trainingSet, language);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Init the validation thread
	 * 
	 * @param trainingSet
	 * @throws Exception
	 */
	private void init(TrainingSet trainingSet, ArrayList<String> language)
			throws Exception {
		
		ExecutorService execute = Executors.newSingleThreadExecutor();
        List<Future<Subset>> list = new ArrayList<Future<Subset>>();

		for (int i = 0; i < numberVal; i++) {
			Future<Subset> future = execute.submit(new TrainingSet(i));
			list.add(future);
		}

		for(Future<Subset> fut : list){
            try {
                //print the return value of Future, notice the output delay in console
                // because Future.get() waits for task to get completed
//                System.out.println("Subeset --> "+fut.get());
                TrainingSet.getSubsetsTab().add(fut.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        //shut down the executor service now
		execute.shutdown();

		

		System.out.println("\t\t\t TrainingSet.getSubsetsTab size of element = " + TrainingSet.getSubsetsTab().size());
		System.out.println("\t\t\t TrainingSet.getSubsetsTab.getLearining size of element = " + TrainingSet.getSubsetsTab().get(0).getLearning().size());

		for (int i = 0; i < numberVal; i++) {
			Validation validation = new Validation(TrainingSet.getSubsetsTab().get(i), new Model(trainingSet.getNumberLng(),numberFeatures), language, itMax);
			validations.add(validation);
			this.threadsValidation.add(new Thread(validation));
		}
	}

	/**
	 * Launch the validation thread
	 */
	public void launch() {
		for (Thread thread : threadsValidation) {
			thread.start();
		}
	}

	/**
	 * Is the tread of validation is over
	 * 
	 * @return
	 */
	public boolean isOver() {
		for (Thread thread : threadsValidation) {
			if (thread.isAlive())
				return false;
		}
		return true;
	}

	/**
	 * Get the max error rate
	 * 
	 * @return
	 */
	public float getMaxErrorRate() {
		float errorRate = 0;
		for (Validation validation : validations) {
			if (validation.getErrorRate() > errorRate)
				errorRate = validation.getErrorRate();
		}
		return errorRate;
	}

	/**
	 * Get the min error rate
	 * 
	 * @return
	 */
	public float getMinErrorRate() {
		float errorRate = 100;
		for (Validation validation : validations) {
			if (validation.getErrorRate() < errorRate)
				errorRate = validation.getErrorRate();
		}
		return errorRate;
	}

}
