import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ArtificialNeuralNetwork{
	public NeuralNode[][] contents;
	public int nOfObjectTypes;
	public int mapSize;
	public int nOfKeys;
	public double score;
	
	public ArtificialNeuralNetwork(String creationMethod, int size, int nOfObjectTypes, int nOfKeys){
		this.nOfObjectTypes = nOfObjectTypes;
		this.mapSize = size;
		this.nOfKeys = nOfKeys;
		this.contents = new NeuralNode[this.mapSize][this.mapSize];
		
		if (creationMethod.equals("random"))
			createNetworkRandomly();
		
		if (Pool.targetMap == null)
			this.score = 1;
		else
			this.calculateScore(Pool.targetMap);
	}
	
	public ArtificialNeuralNetwork(String creationMethod, int neuralMapSize,
			int nOfObjectTypes2, int nOfKeyTypes, ArtificialNeuralNetwork aNN1,
			ArtificialNeuralNetwork aNN2) {
		this.nOfObjectTypes = nOfObjectTypes;
		this.mapSize = neuralMapSize;
		this.nOfKeys = nOfKeys;
		this.contents = new NeuralNode[this.mapSize][this.mapSize];
		
		if (creationMethod.equals("crossOver"))
			crossOverTwoANNs(2, aNN1, aNN2);
		else if (creationMethod.equals("mutate"))
			crossOverTwoANNs(this.mapSize, aNN1, aNN2);
		
		
		this.calculateScore(Pool.targetMap);
		
	}

	private void crossOverTwoANNs(int crossOverFactor, ArtificialNeuralNetwork aNN1,
			ArtificialNeuralNetwork aNN2) {
		int crossOverSelection;
		for (int c1=0; c1<this.mapSize; c1++)
			for(int c2=0; c2<this.mapSize; c2++)
			{
				crossOverSelection = Pool.randSeed.nextInt(crossOverFactor);
				if (crossOverSelection != 0 && aNN1.contents[c1][c2] != null)
					this.contents[c1][c2] = new NeuralNode(aNN1.contents[c1][c2]);
				else if (crossOverSelection == 0 && aNN2.contents[c1][c2] != null)
					this.contents[c1][c2] = new NeuralNode(aNN2.contents[c1][c2]);
				else
					this.contents[c1][c2] = null;
			}
	}

	private void createNetworkRandomly() {
		
		int nOfNodesToBeCreated = Pool.randSeed.nextInt((this.mapSize*this.mapSize)-1)+1;
		List<NeuralNodeCoordinate> availableCoordinates = new ArrayList<NeuralNodeCoordinate>();
		
		for (int counter=0; counter<this.mapSize*this.mapSize; counter++)
		{
			availableCoordinates.add(new NeuralNodeCoordinate(counter/this.mapSize, counter%this.mapSize));
		}
		
		for (int counter=0; counter<nOfNodesToBeCreated; counter++)
		{
			int randomAvailableCoordinateIndex = Pool.randSeed.nextInt(availableCoordinates.size());
			NeuralNodeCoordinate CoordToBeInserted = availableCoordinates.get(randomAvailableCoordinateIndex);
			int objectType = Pool.randSeed.nextInt(this.nOfObjectTypes);
			int keyPress = Pool.randSeed.nextInt(nOfKeys);
			createNewNode(CoordToBeInserted, objectType, keyPress);
		}
		
	}

	private void createNewNode(NeuralNodeCoordinate coordToBeInserted,
			int objectType, int keypress) {
		this.contents[coordToBeInserted.x][coordToBeInserted.y] = new NeuralNode(objectType, keypress);
		
	}

	public double calculateScore(ArtificialNeuralNetwork targetMap)
	{
		this.score = neuralNetworkLikeness(targetMap);
		return this.score;
	}

	private double neuralNetworkLikeness(ArtificialNeuralNetwork targetMap) {
		double score = 0;
		int nodeCountInTargetMap = targetMap.nodeCount();
		
		for(int m=0; m<this.mapSize; m++)
			for(int n=0; n<this.mapSize; n++)
			{
				if (targetMap.contents[m][n]==null && this.contents[m][n] == null)
					continue;
				if (targetMap.contents[m][n]!=null && this.contents[m][n] != null)
				{
					if (this.contents[m][n].equals(targetMap.contents[m][n]))
						score += 1/((double) nodeCountInTargetMap);
				}
				else
					score -= 1/((double) nodeCountInTargetMap);
			}		
		
		return score;
	}

	private int nodeCount() {
		// TODO Auto-generated method stub
		int numberOfNodes = 0;
		for(int m=0; m<this.mapSize; m++)
			for(int n=0; n<this.mapSize; n++)
				if (this.contents[m][n] != null)
					numberOfNodes += 1;
		
		return numberOfNodes;
	}
}
