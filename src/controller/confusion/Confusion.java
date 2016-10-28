package controller.confusion;

import controller.Computation;
import model.Model;
import model.TrainingSet;

import java.util.ArrayList;

/**
 * Created by Thomas on 24/10/2016.
 */
public class Confusion {
    private Computation confusion;
    private Integer[][] confusionMatrix;
    private ArrayList<String> languages;
    private TrainingSet trainingSet = new TrainingSet();
    private Model model;

    public Confusion(TrainingSet trainingSet, Model model, ArrayList<String> languages) {
        this.languages = languages;
        this.trainingSet = trainingSet;
        this.model = model;
        init();
    }

    /**
     * Init the confusion matrix array and the confusion computation
     */
    private void init(){

        // -- Confusion Matrix
        confusionMatrix = new Integer[languages.size()][languages.size()];

        // Init confusion matrix
        for(int i=0;i<languages.size();i++){
            for(int j=0;j<languages.size();j++){
                confusionMatrix[i][j] = 0;
            }
        }

        confusion = new Computation(model, languages, trainingSet.getTrainings()){
            @Override
            protected void processIteration(Integer i, Integer t, Integer realLang, Integer result) throws Exception{
                confusionMatrix[realLang][result] += 1;
            }
            @Override
            protected boolean stopCondition(Integer iteration){
                if(iteration > 0) return true;
                return false;
            }
        };
    }

    /**
     * Compute the confusion matrix
     */
    public void compute(){
        confusion.compute();
    }

    /**
     * Display the confusion matrix
     */
    public void display(){
        System.out.println("\tConfusion Matrix: ");
        System.out.print("\t\t");
        for(int i=0;i<languages.size();i++){
            System.out.print(languages.get(i)+"\t");
        }
        System.out.print("\n\t");
        for(int i=0;i<languages.size();i++){
            System.out.print(languages.get(i)+"\t");
            for(int j=0;j<languages.size();j++){
                System.out.print(confusionMatrix[i][j]+"\t");
            }
            System.out.print("\n\t");
        }
    }
}
