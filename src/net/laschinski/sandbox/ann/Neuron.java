package net.laschinski.sandbox.ann;

import java.util.ArrayList;

import net.laschinski.sandbox.utils.Random;

public class Neuron {
	public String activationFunction;
	private ArrayList<Double> inputs = new ArrayList<>();
	private ArrayList<Double> weights = new ArrayList<>();
	private ArrayList<Double> deltas = new ArrayList<>();
	private double bias;
	private double biasDelta;
	private double dotProduct;
	private double output;
	private double errorGradient;

	// Initialize bias and weights with random values
	public Neuron(int numberOfInputs, int layerSize, String activationFunction) {
		if (activationFunction == "") {
			activationFunction = "Sigmoid";
		}

		for (int i = 0; i < numberOfInputs; i++) {
			// weights.add(Random.Range(-0.1d, 0.1d));
			// weights.Add(Random.Range(layerSize, numberOfInputs) * Mathf.Sqrt(2.0f /
			// numberOfInputs));
			weights.add(NextGaussian());
		}
		bias = Random.Range(-0.1d, 0.1d);

		this.activationFunction = activationFunction;
	}

	public ArrayList<Double> getInputs() {
		return inputs;
	}

	public double getInput(int i) {
		return inputs.get(i);
	}

	public void setInputs(ArrayList<Double> inputs) {
		if (inputs.size() != weights.size()) {
			System.out.println("Number of inputs should be " + weights.size());
			this.inputs = new ArrayList<Double>();
		}
		this.inputs = new ArrayList<Double>(inputs);
	}

	public ArrayList<Double> getWeights() {
		return weights;
	}

	public double getWeight(int i) {
		return weights.get(i);
	}

	public int getWeightSize() {
		return weights.size();
	}

	public void setWeights(ArrayList<Double> newWeights) {
		if (newWeights.size() != weights.size()) {
			System.out.println("Number of weights should be " + weights.size());
		}
		weights = new ArrayList<Double>(newWeights);
	}

	public ArrayList<Double> getDeltas() {
		return deltas;
	}

	public void setDeltas(ArrayList<Double> newDeltas) {
		if (newDeltas.size() != weights.size()) {
			System.out.println("Number of deltas should be " + weights.size());
		}
		deltas = new ArrayList<Double>(newDeltas);
	}

	public void addToDeltas(ArrayList<Double> newDeltas) {
		if (newDeltas.size() != weights.size()) {
			System.out.println("Number of deltas should be " + weights.size());
		}
		for (int i = 0; i < newDeltas.size(); i++) {
			deltas.set(i, deltas.get(i) + newDeltas.get(i));
		}

	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	public void setBiasDelta(double biasDelta) {
		this.biasDelta = biasDelta;
	}

	public void addToBiasDelta(double biasDelta) {
		this.biasDelta += biasDelta;
	}

	public double getDotProduct() {
		return dotProduct;
	}

	public double getOutput() {
		return output;
	}

	public double getErrorGradient() {
		return errorGradient;
	}

	public void setErrorGradient(double errorGradient) {
		this.errorGradient = errorGradient;
	}

	public double CalculateOutput() {

		if (inputs.size() == 0) {
			System.out.println("No inputs set");
			output = 0;
			return output;
		}

		// lets start with the bias which should be added to the dot product
		dotProduct = bias;

		// step through every input
		for (int i = 0; i < inputs.size(); i++) {
			// calculate the product and add it to the dot product
			dotProduct += weights.get(i) * inputs.get(i);
		}

		// use activate function depending on settings to calculate the output
		output = ActivationFunction(dotProduct);

		return output;
	}

	public double CalculateErrorGradient(double error) {
		errorGradient = ActivationDerivative(dotProduct) * error;
		if (errorGradient > 1.0d) {
			System.out.println("Gradient clipped to 1 - was at " + errorGradient);
			errorGradient = 1.0d;
		} else if (errorGradient < -1.0d) {
			System.out.println("Gradient clipped to -1 - was at " + errorGradient);
			errorGradient = -1.0d;
		}
		return errorGradient;
	}

	public void ApplyDeltas(double lambda) {
		if (lambda != 0.0d) {
			// regulations
			for (int i = 0; i < weights.size(); i++) {
				// weights[i] -= Mathf.Sign((float)weights[i]) * lambda;
				weights.set(i, weights.get(i) - Math.signum(weights.get(i)) * lambda * weights.get(i) * weights.get(i));
				// weights[i] -= lambda * weights[i]; // L2 regularization
			}
			// bias -= Mathf.Sign((float)bias) * lambda;
			bias -= Math.signum(bias) * lambda * bias * bias;
			// bias -= lambda * bias; // L2 regularization
		}

		ArrayList<Double> newWeights = new ArrayList<Double>();
		for (int i = 0; i < weights.size(); i++) {
			newWeights.add(weights.get(i) + deltas.get(i));
		}
		setWeights(newWeights);

		setBias(bias + biasDelta);
	}

	public void ApplyDeltas() {
		double lambda = 0.0f;
		ApplyDeltas(lambda);
	}

	private double ActivationFunction(double dotProduct) {
		switch (activationFunction) {
		case "Linear":
			return dotProduct;
		case "ReLU":
			return ReLU(dotProduct);
		case "LeakyReLU":
			return LeakyReLU(dotProduct);
		case "Sigmoid":
			return Sigmoid(dotProduct);
		case "Step":
			return Step(dotProduct);
		case "TanH":
			return TanH(dotProduct);
		default:
			System.out.println("Unknown activation function used!");
			return 0;
		}
	}

	private double ActivationDerivative(double dotProduct) {
		switch (activationFunction) {
		case "Linear":
			return 1; // is always 1
		case "ReLU":
			return ReLUDeri(dotProduct);
		case "LeakyReLU":
			return LeakyReLUDeri(dotProduct);
		case "Sigmoid":
			return SigmoidDeri(dotProduct);
		case "Step":
			return SigmoidDeri(dotProduct); // should work
		case "TanH":
			return TanHDeri(dotProduct);
		default:
			System.out.println("Unknown activation function used!");
			return 0;
		}
	}

	private double Step(double value) {
		if (value < 0)
			return 0;
		else
			return 1;
	}

	private double Sigmoid(double value) {
		return 1.0d / (1.0d + Math.exp(-value));
	}

	private double SigmoidDeri(double value) {
		return Sigmoid(value) * (1.0d - Sigmoid(value));
	}

	private double TanH(double value) {
		return 2.0d / (1.0d + Math.exp(-2.0d * value)) - 1.0d;
	}

	private double TanHDeri(double value) {
		return 1 - Math.exp(value);
	}

	private double ReLU(double value) {
		return Math.max(0, value); // value < 0 ? 0 : value;
	}

	private double LeakyReLU(double value) {
		if (value > 0) {
			return value;
		} else {
			return value * 0.01;
		}
	}

	private double ReLUDeri(double value) {
		if (value > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	private double LeakyReLUDeri(double value) {
		if (value > 0) {
			return 1;
		} else {
			return 0.01;
		}
	}

	public static double NextGaussian() {
		double v1, v2, s;
		do {
			v1 = 2.0d * Random.Range(-1.0d, 1.0d);
			v2 = 2.0d * Random.Range(-1.0d, 1.0d);
			s = v1 * v1 + v2 * v2;
		} while (s >= 1.0d || s == 0d);

		s = Math.sqrt((-2.0d * Math.log(s)) / s);

		return v1 * s;
	}
}
