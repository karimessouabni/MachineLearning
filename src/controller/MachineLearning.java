package controller;

import controller.confusion.Confusion;
import controller.parser.Parser;
import controller.validation.CrossValidation;
import model.*;

import java.util.ArrayList;

/**
 * 
 */
public class MachineLearning {
    private Dictionary dictionary = new Dictionary();
    private TrainingSet trainingSet = new TrainingSet();
    private ArrayList<String> languages = new ArrayList<>();

    public MachineLearning() throws Exception{
        // -- Data preparation
        // Parse the data to build the dictionary & build the training set
        parseData();

        // Init the model
        final Model model = new Model(languages.size(), dictionary.size());

        System.out.println("Step 2&3 : k-Cross validation & Learning");
        // -- k-Cross Validation (computes error rates)
        CrossValidation crossValidation = new CrossValidation(trainingSet, languages, 3, dictionary.size());
        // Launch threads of k-Cross Validation
        crossValidation.launch();

        // -- Learning
        // --- Training the model
        Computation learning = new Computation(model, languages, trainingSet.getTrainings()){
            @Override
            protected void processIteration(Integer i, Integer t, Integer realLang, Integer result) throws Exception{
                if (realLang != result) {
                    // -- Update matrix value
                    float value;
                    if (i == 0) value = 1;
                    else value = 1f / i;

                    // Adding 1/i to the real lang value
                    model.modify(realLang, trainings.get(t).getFeatures(), value);

                    // Sub 1/i to the prediction lang value
                    model.modify(result, trainings.get(t).getFeatures(), -1 * value);
                }
            }
            /*@Override
            protected void afterIteration(Integer i){
                System.out.println("Training full model, iteration "+i);
            }*/
        };
        learning.compute();
        

        // --- Confusion Matrix
        System.out.println("Step 4 : Confusion matrix");
        System.out.print("\tCompute confusion matrix...");
        Confusion confusion = new Confusion(trainingSet, learning.getModel(), languages);
        confusion.compute();
        System.out.println("done.");



        while (!crossValidation.isOver()){
            Thread.sleep(1000); // Sleep 1 second
        }

        System.out.println("Step 5 : Results");
        // Display confusion matrix
        confusion.display();

        //  -- Get cross validation result
        System.out.println("\n\tMax Error rate : "+crossValidation.getMaxErrorRate());
        System.out.println("\tMin Error rate : "+crossValidation.getMinErrorRate());


    }

    /**
     * Build the dictionary, get the languages, build the training set
     */
    private void parseData(){
        System.out.println("Step 1 : Data preparation");
        System.out.print("\tBuilding the dictionary...");
        Parser parser = new Parser(){

            @Override
            protected void parseWord(String word){
                dictionary.checkWord(word);
            }
        };
        parser.parse();


        languages =  parser.getLangs();

        // Set number of lang to the trainingSet
        trainingSet.setNumberLng(parser.getLangs().size());

        System.out.println("done.");
//        System.out.println(dictionary);

        System.out.print("\tParsing the training set...");
        
        
        
        /*
         * Build the training set, array of trainings , language + features' indexes
         */
        Parser parser2 = new Parser(){
            @Override
            protected void addLang(String lng){
                trainingSet.newLearningTest(lng); // to create a new trainingSet and assign a lang to it
            }
            @Override
            protected void parseWord(String word){
                trainingSet.addFeature(dictionary.getIndex(word)); // fill the training feature by words' dictionary index 
            }
            protected void endLine(){
                trainingSet.endLearningTest();
            }
        };
        parser2.parse();

        System.out.println("done.");
    }

    public static void main(String [] args) throws Exception
    {

        new MachineLearning();

    }
}
