package model;

import exception.RoundOutOfBoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Thomas on 30/09/2016.
 */
public class TrainingSet {
    private ArrayList<Training> trainings = new ArrayList<>();
    private Training currentTest = new Training();
    private Integer numberLng;

    public ArrayList<Training> getTrainings() {
        return trainings;
    }

    public Integer getNumberLng() {
        return numberLng;
    }

    public void setNumberLng(Integer numberLng) {
        this.numberLng = numberLng;
    }

    public void newLearningTest(String lng){
        currentTest = new Training();
        currentTest.setLanguage(lng);
    }
    public void addFeature(Integer feature){
        currentTest.addFeature(feature);
    }

    public void endLearningTest(){
        this.trainings.add(currentTest);
    }

    /**
     *
     * @param round iteration
     * @param ratio ex 10% test, 90% validation, ratio = 10
     * @return
     * @throws RoundOutOfBoundException
     */
    public Subset getSubset(Integer round, Integer ratio) throws RoundOutOfBoundException{ // every round generate a different validation 
        Subset subset = new Subset();
        Integer size_part = trainings.size()/ratio; // get 10% of the TrainingSet (trainins array )
        if(round < 0 || round >= ratio) throw new RoundOutOfBoundException("Round out of bound, try again with bound between 0 and "+(ratio-1));
        // Set validation training
        subset.setValidation(trainings.subList(round*size_part, (round+1)*size_part));

        // Set learning training
        List<Training> learning = (ArrayList<Training>)trainings.clone();
        learning.subList(round*size_part, (round+1)*size_part).clear();

        subset.setLearning(learning);
        return subset;
    }
}
