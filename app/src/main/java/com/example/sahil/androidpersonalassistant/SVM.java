package com.example.sahil.androidpersonalassistant;

import android.util.Log;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.FileHandler;

import java.io.File;
import java.io.IOException;

import libsvm.LibSVM;

/**
 * Created by SAHIL on 16-04-2017.
 */

public class SVM {
    public float ACCURACY=0;
    Dataset dataset;
    String filename;
    Classifier svmClassifier;

    public SVM()   {
        svmClassifier = new LibSVM();
    }

    public Classifier train(String filename1, int classIndex) {
        filename=filename1;
        svmClassifier = new LibSVM();

        try {
            dataset = FileHandler.loadDataset(new File(filename), classIndex, ",");
            svmClassifier.buildClassifier(dataset);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return svmClassifier;
    }

    public String test(Instance instance)    {
        String result = "";
//        double[] values = new double[] {20, 1};
//        Instance instanceTest = new DenseInstance(values);
        Object predictedClassValue = svmClassifier.classify(instance);
        return predictedClassValue.toString();
    }
}
