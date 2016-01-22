
public class NeuralNode {
	public int objectType;
	public int keypress;
	
	public NeuralNode(int objectType, int keypress)
	{
		this.objectType = objectType;
		this.keypress = keypress;
	}
	
	public NeuralNode(NeuralNode neuralNode) {
		this.objectType = neuralNode.objectType;
		this.keypress = neuralNode.keypress;
	}

	public Boolean equals(NeuralNode target)
	{
		if (this.objectType == target.objectType && this.keypress == target.keypress)
			return true;
		else
			return false;
	}
}

