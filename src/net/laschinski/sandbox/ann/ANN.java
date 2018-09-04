package net.laschinski.sandbox.ann;

import java.util.ArrayList;

public class ANN {

    /// <summary>
    /// Learning rate
    /// </summary>
    private double alpha;
    /// <summary>
    /// Learning rate decay (alpha *= alphaDecay)
    /// Also decays lambda by the same rate.
    /// </summary>
    private double alphaDecay;
    /// <summary>
    /// Decay of weights (weight -= weight * lambda)
    /// </summary>
    private double lambda;
    /// <summary>
    /// Number of expected inputs
    /// </summary>
    private int numInputs;
    /// <summary>
    /// A list containing all layer objects, which contain the neurons
    /// </summary>
    ArrayList<Layer> layers = new ArrayList<>();

    /// <summary>
    /// This constructor prepared the neural network by setting the hyperparameters, as well as the inputs
    /// and prepares the visualization of the neural net, if an inworld spawner for the neurons is given.
    /// Alpha indicates the learning rate, while it will be multiplied with alphaDecay after every learning
    /// cycle to gradually decay the learning rate. alphaDecay = 1.0 disables the decay.
    /// Lambda indicates a rate by which the neurons weights decay every learning cycle. This is a technique
    /// to combat overfitting and exploding gradients.
    /// </summary>
    /// <param name="numberOfInputs">An integer indicating the number of inputs to expect</param>
    /// <param name="alpha">A double representing the learning rate</param>
    /// <param name="alphaDecay">A double to reduce alpha (and lambda) over time by multiplication</param>
    /// <param name="lambda">A double to reduce weights by the amount of lambda multiplied with the actual weight</param>
    public ANN(int numberOfInputs, double alpha, double alphaDecay, double lambda)
    {
        this.numInputs = numberOfInputs;
        this.alpha = alpha;
        this.alphaDecay = alphaDecay;
        this.lambda = lambda;
    }

    /// <summary>
    /// Adds one layer to the neural network. It's an output layer as long as you don't add another layer.
    /// </summary>
    /// <param name="numberOfNeurons">An integer with the number of neurons this layer will contain</param>
    /// <param name="activationFunction">The name of the activation function to use. Defaults to ReLU.</param>
    public void AddLayer(int numberOfNeurons, String activationFunction) {
        int numberOfInputs;

        // on the first layer of calculating neurons we use the number of input neurons
        if (layers.size() == 0) {
            numberOfInputs = this.numInputs;
        }
        // on all the not-first layers we use the number of outputs of the previous layer
        else {
            numberOfInputs = layers.get(layers.size() - 1).neurons.size();
        }
        
        layers.add(new Layer(numberOfNeurons, numberOfInputs, activationFunction));
    }
    
    public void AddLayer(int numberOfNeurons) {
    	String activationFunction = "ReLU";
    	AddLayer(numberOfNeurons, activationFunction);
    }

    /// <summary>
    /// Calculates the ANN outputs for the given inputs.
    /// </summary>
    /// <param name="inputValues">A list of double values used as inputs</param>
    /// <returns>A list a double values calculated by the ANN</returns>
    public ArrayList<Double> Predict(ArrayList<Double> inputValues)
    {
        ArrayList<Double> inputs;
        ArrayList<Double> outputs = new ArrayList<>();

        if (inputValues.size() != numInputs)
        {
            System.out.println("Number of Inputs must be " + numInputs);
            return outputs; // empty list
        }

        // step through every layer
        for (int i = 0; i < layers.size(); i++)
        {
            // on first calculating layer we use the actual inputs
            if (i == 0)
            {
                inputs = new ArrayList<>(inputValues);
            }
            // on all the not-first layers we use the outputs of the previous layer as inputs
            else
            {
                // uses the outputs of the previous iteration as inputs
                inputs = new ArrayList<>(outputs);
                // since this is at least the second iteration, we need to clean up the outputs before calculating them again
                outputs.clear();
            }

            // step through every neuron in this layer
            for (int j = 0; j < layers.get(i).neurons.size(); j++)
            {
                // set inputs of particular neuron
                layers.get(i).neurons.get(j).setInputs(inputs);
                // calculate neuron output and add it to output list for this layer
                outputs.add(layers.get(i).neurons.get(j).CalculateOutput());
            }
        }
        
        return outputs;
    }

    /// <summary>
    /// Trains the ANN by giving inputs and the desired output values for those inputs.
    /// </summary>
    /// <param name="inputValues">A list of double values used as inputs</param>
    /// <param name="desiredOutputValues">A list of double values which mark the desired outputs</param>
    /// <returns>A list of double values which are the calculated output before training</returns>
    public ArrayList<Double> Train(ArrayList<Double> inputValues, ArrayList<Double> desiredOutputValues)
    {
        ArrayList<Double> outputs = Predict(inputValues);

        calculateGradients(outputs, desiredOutputValues);
        calculateDeltas(outputs, desiredOutputValues);

        UpdateWeights();

        return outputs;
    }

    /// <summary>
    /// Trains the ANN by giving a batch of input values and corresponding desired output values.
    /// All calculated deltas will be summed up before the weights are updated.
    /// </summary>
    /// <param name="inputValues">A list of a list containing doubles which are used as inputs</param>
    /// <param name="desiredOutputValues">A list of a list containing doubles which contain the desired output values</param>
    public void TrainBatch(ArrayList<ArrayList<Double>> inputValues, ArrayList<ArrayList<Double>> desiredOutputValues)
    {
        boolean sumDeltas = false;

        for (int i = 0; i < inputValues.size(); i++)
        {
            // after the first iteration the deltas are cleanly overwritten, so we can start to sum up now
            if (i > 0)
            {
                sumDeltas = true;
            }
            ArrayList<Double> outputs = Predict(inputValues.get(i));

            calculateGradients(outputs, desiredOutputValues.get(i));
            calculateDeltas(outputs, desiredOutputValues.get(i), sumDeltas);
        }

        UpdateWeights();
    }

    /// <summary>
    /// Actually updates the weights and reduces the alpha and lambda values if decay is set.
    /// </summary>
    public void UpdateWeights()
    {
        // step through every layer
        for (int i = 0; i < layers.size(); i++)
        {
            // step through every neuron in this layer
            for (int j = 0; j < layers.get(i).neurons.size(); j++)
            {
                layers.get(i).neurons.get(j).ApplyDeltas(lambda);
            }
        }

        // decay alpha and lambda on the same rate, to prevent smaller alpha than lambda
        // otherweise weights would decay faster than they are relearned, at some point
        alpha *= alphaDecay;
        lambda *= alphaDecay;
    }

    /// <summary>
    /// Calculates the gradients for gradient descent and saves them in the neuron objects.
    /// The gradient is calculated based on the desired outputs in contrast to the actual outputs.
    /// </summary>
    /// <param name="outputs">A list of doubles containing the actual outputs of the neural network</param>
    /// <param name="desiredOutputValues">A list of doubles containing the desired outputs of the neural network</param>
    private void calculateGradients(ArrayList<Double> outputs, ArrayList<Double> desiredOutputValues)
    {
        // step through every layer BACKWARDS
        for (int i = layers.size() - 1; i >= 0; i--)
        {
            // step through every neuron in this layer
            for (int j = 0; j < layers.get(i).neurons.size(); j++)
            {
                // neuron is at the output layer
                if (i == (layers.size() - 1))
                {
                    // calculate the error for one neuron
                    double error = desiredOutputValues.get(j) - outputs.get(j);
                    // calculate the error gradient
                    layers.get(i).neurons.get(j).CalculateErrorGradient(error);
                }
                // neuron is at any not-output layer
                else
                {
                    double errorGradSum = 0;
                    for (int p = 0; p < layers.get(i + 1).neurons.size(); p++)
                    {
                        // sum up the error gradients of -every neuron at the following layer- multiplied by -the weight for this neuron-
                        errorGradSum += layers.get(i + 1).neurons.get(p).getErrorGradient() * layers.get(i + 1).neurons.get(p).getWeight(j);
                    }
                    // caluclate the error gradient with regards to the error gradients of the connected neurons
                    layers.get(i).neurons.get(j).CalculateErrorGradient(errorGradSum);
                }
            }
        }
    }

    /// <summary>
    /// Calculates the deltas (differences) of current weights and corresponding gradients and saves them into
    /// the neuron objects.
    /// The boolean sumDeltas need to be set to false to get the actual deltas. This should be the case for online
    /// learning or the first dataset of a batch. For every other dataset of a batch sumDelta should be set to true.
    /// </summary>
    /// <param name="outputs">A list of doubles containing the actual outputs of the neural network</param>
    /// <param name="desiredOutputValues">A list of doubles containing the desired outputs of the neural network</param>
    /// <param name="sumDeltas">A boolean indicating whether deltas should be added to the sum or overwrite the sum</param>
    public void calculateDeltas(ArrayList<Double> outputs, ArrayList<Double> desiredOutputValues, boolean sumDeltas)
    {
        // step through every layer
        for (int i = 0; i < layers.size(); i++)
        {
            // step through every neuron in this layer
            for (int j = 0; j < layers.get(i).neurons.size(); j++)
            {
                // update all deltas of this neuron regarding the calculated gradients
                ArrayList<Double> deltas = new ArrayList<>();
                for (int k = 0; k < layers.get(i).neurons.get(j).getWeightSize(); k++)
                {
                    // neuron is at the output layer
                    if (i == (layers.size() - 1))
                    {
                        // on the output layer the error is used directly to calculate the delta
                        double error = desiredOutputValues.get(j) - outputs.get(j);
                        double delta = alpha * layers.get(i).neurons.get(j).getInput(k) * error;

                        deltas.add(delta);
                    }
                    // neuron is not at the output layer
                    else
                    {
                        // calculate delta of a neuron based on the gradient saved in the neuron object
                        double delta = alpha * layers.get(i).neurons.get(j).getErrorGradient() * layers.get(i).neurons.get(j).getInput(k);

                        deltas.add(delta);
                    }
                }

                // delta for the bias is always calculated by the gradient saved in the neuron object
                double biasDelta = alpha * layers.get(i).neurons.get(j).getErrorGradient();

                // overwrite old deltas
                if (!sumDeltas)
                {
                    layers.get(i).neurons.get(j).setDeltas(deltas);
                    layers.get(i).neurons.get(j).setBiasDelta(biasDelta);
                }
                // sum up new deltas and saved deltas
                else
                {
                    layers.get(i).neurons.get(j).addToDeltas(deltas);
                    layers.get(i).neurons.get(j).addToBiasDelta(biasDelta);
                }



            }
        }
    }
    
    public void calculateDeltas(ArrayList<Double> outputs, ArrayList<Double> desiredOutputValues) {
    	boolean sumDeltas = false;
    	calculateDeltas(outputs, desiredOutputValues, sumDeltas);
    }

    /*
     * Needs more adjustments to work in Java
     * 
    /// <summary>
    /// Convertes the ANN weights to an XML document which can be saved for later restore.
    /// </summary>
    /// <returns>An XDocument containing neuron weight values</returns>
    private XDocument brainToXml()
    {
        XElement brainXml = new XElement("Brain");
        brainXml.Add(new XElement("Inputs", numInputs));
        XDocument brainDoc = new XDocument(new XDeclaration("1.0", "UTF-16", null), brainXml);

        for (int i = 0; i < layers.Count; i++)
        {
            XElement layerXml = new XElement("Layer");
            layerXml.Add(new XElement("LayerID", i + 1));
            brainXml.Add(layerXml);

            for (int j = 0; j < layers[i].neurons.Count; j++)
            {
                XElement neuronXml = new XElement("Neuron");
                layerXml.Add(neuronXml);

                List<double> weights = layers[i].neurons[j].getWeights();
                for (int k = 0; k < weights.Count; k++)
                {
                    XElement weightXml = new XElement("Weight", weights[k]);
                    neuronXml.Add(weightXml);
                }
                XElement biasXml = new XElement("Bias", layers[i].neurons[j].getBias());
                neuronXml.Add(biasXml);
            }
        }

        return brainDoc;
    }

    /// <summary>
    /// Restores the ANN weights from an XML document.
    /// </summary>
    /// <param name="brainDoc">An XDocument containing neuron weight values</param>
    private void XmlToBrain(XDocument brainDoc)
    {
        XElement brainXml = brainDoc.Element("Brain");
        IEnumerable<XElement> layersEnum = brainXml.Elements("Layer");
        int i = 0;
        foreach (XElement layerElement in layersEnum)
        {
            IEnumerable<XElement> neuronsEnum = layerElement.Elements("Neuron");
            int j = 0;
            foreach (XElement neuronElement in neuronsEnum)
            {
                IEnumerable<XElement> weightsEnum = neuronElement.Elements("Weight");
                List<double> newWeights = new List<double>();
                foreach (XElement weightElement in weightsEnum)
                {
                    newWeights.Add(double.Parse(weightElement.Value));
                }
                layers[i].neurons[j].setWeights(newWeights);
                layers[i].neurons[j].setBias(double.Parse(neuronElement.Element("Bias").Value));
                j++;
            }
            i++;
        }
    }

    /// <summary>
    /// Saves a saved brain to a file (Work in progress)
    /// </summary>
    public void saveBrain()
    {
        brainToXml().Save("D:\\savedBrain.xml");
    }

    /// <summary>
    /// Loads a saved brain from a file (Work in progress)
    /// </summary>
    public void loadBrain()
    {
        XDocument xdocument = XDocument.Load("D:\\loadBrain.xml");
        XmlToBrain(xdocument);
    }*/
}
