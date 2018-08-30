package net.laschinski.ann_sandbox.ann;

public class AnnBuilder {
	int numberOfInputs;
	double alpha;
	double alphaDecay = 1.0d;
	double lambda = 0.0d;
	
	public AnnBuilder(int numberOfInputs, double alpha) {
		this.numberOfInputs = numberOfInputs;
		this.alpha = alpha;
	}
	
	public AnnBuilder alphaDecay(double alphaDecay) {
		this.alphaDecay = alphaDecay;
		return this;
	}
	
	public AnnBuilder lambda(double lambda) {
		this.lambda = lambda;
		return this;
	}
	
	public Ann buildAnn() {
		return new Ann(numberOfInputs, alpha, alphaDecay, lambda);
	}
}
