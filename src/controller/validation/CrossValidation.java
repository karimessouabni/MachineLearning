package controller.validation;

import model.Model;
import model.TrainingSet;

import java.util.ArrayList;

/**
 * Created by Thomas on 19/10/2016.
 */
public class CrossValidation {
    private Integer numberVal;
    private ArrayList<Validation> validations;
	private ArrayList<Thread> threads;
    private Integer numberFeatures;
    private static final Integer ratio = 10;
    private static final Integer itMax = 15;

    public CrossValidation(TrainingSet trainingSet,  ArrayList<String> language, Integer number_validation, Integer number_features){
        this.numberVal = number_validation;
        this.numberFeatures = number_features;
        this.threads = new ArrayList<Thread>();
        this.validations = new ArrayList<>();

        try{
            init(trainingSet, language);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Init the validation thread
     * @param trainingSet
     * @throws Exception
     */
    private void init(TrainingSet trainingSet, ArrayList<String> language) throws Exception{
        for(int i=0;i<numberVal;i++){
            Validation validation = new Validation(trainingSet.getSubset(i, ratio), new Model(trainingSet.getNumberLng(), numberFeatures), language, itMax);
            validations.add(validation);
            this.threads.add(new Thread(validation));
        }
    }

    /**
     * Launch the validation thread
     */
    public void launch(){
       for(Thread thread : threads){
           thread.start();
       }
    }

    /**
     * Is the tread of validation is over
     * @return
     */
    public boolean isOver(){
        for(Thread thread : threads){
            if(thread.isAlive()) return false;
        }
        return true;
    }

    /**
     * Get the max error rate
     * @return
     */
    public float getMaxErrorRate(){
        float errorRate = 0;
        for(Validation validation : validations){
            if(validation.getErrorRate() > errorRate) errorRate = validation.getErrorRate();
        }
        return errorRate;
    }

    /**
     * Get the min error rate
     * @return
     */
    public float getMinErrorRate(){
        float errorRate = 100;
        for(Validation validation : validations){
            if(validation.getErrorRate()  < errorRate) errorRate = validation.getErrorRate();
        }
        return errorRate;
    }



}
