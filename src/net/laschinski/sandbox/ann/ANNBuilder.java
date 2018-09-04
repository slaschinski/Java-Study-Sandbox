package net.laschinski.sandbox.ann;

public class ANNBuilder {
	int numberOfInputs;
	double alpha;
	double alphaDecay = 1.0d;
	double lambda = 0.0d;
	
	public ANNBuilder(int numberOfInputs, double alpha) {
		this.numberOfInputs = numberOfInputs;
		this.alpha = alpha;
	}
	
	public ANNBuilder alphaDecay(double alphaDecay) {
		this.alphaDecay = alphaDecay;
		return this;
	}
	
	public ANNBuilder lambda(double lambda) {
		this.lambda = lambda;
		return this;
	}
	
	public ANN buildAnn() {
		return new ANN(numberOfInputs, alpha, alphaDecay, lambda);
	}
}
