package net.laschinski.ann_sandbox.ann;

import java.util.ArrayList;

public class Layer {

    public ArrayList<Neuron> neurons = new ArrayList<Neuron>();

    public Layer(int numberOfNeurons, int numberOfInputsPerNeuron, String activationFunctionName)
    {
        for (int i = 0; i < numberOfNeurons; i++)
        {
            neurons.add(new Neuron(numberOfInputsPerNeuron, numberOfNeurons, activationFunctionName));
        }
}
}
