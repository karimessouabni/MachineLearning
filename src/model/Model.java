package model;

import com.sun.xml.internal.ws.binding.FeatureListUtil;

import exception.FeatureDoesntExistException;
import exception.LangDoesntExistException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 */
public class Model {
    private Float[][] matrix;
    private Integer numberLang;
    private Integer numberFeature;
    public Model(Integer number_lng, Integer number_feature){
        numberLang = number_lng;
        numberFeature = number_feature;

        matrix = new Float[number_feature][number_lng];
        for(int i=0;i<number_feature;i++){
            for(int j=0;j<number_lng;j++){
                matrix[i][j] = 0f;
            }
        }

    }

    public Float get(int index_feature, int index_lng){
        return matrix[index_feature][index_lng];
    }
    public void set(int index_feature, int index_lng, Float value){
        matrix[index_feature][index_lng] = value;
    }

    public Float[][] getMatrix() {
        return matrix;
    }

    public void modify(Integer lang, ArrayList<Integer> features, Float value) throws FeatureDoesntExistException, LangDoesntExistException{
        if(lang <0 || lang>=numberLang) throw new LangDoesntExistException("This lang doesn't exist");
        for(Integer feature:features){
            if(feature<0 || feature >= numberFeature) throw new FeatureDoesntExistException("This feature doesn't exit");
            matrix[feature][lang] += value;
            //System.out.println("new:"+matrix[feature][lang]);
        }

    }

	@Override
	public String toString() {
		String toshow="";
		 for(int i=0;i<this.numberFeature;i++){
	            for(int j=0;j<this.numberLang;j++){
	            	toshow += matrix[i][j]+" " ;
	            }
	            toshow += "\n" ;
	        }
		 return toshow;
	}
    
    
    
    
}
