import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

//TODO Use MinMax Priority Queue
//TODO Use DropWizard for API
//TODO Play game sending keystrokes


public class Pool {
	public static Random randSeed = new Random();
	private static int neuralMapSize = 10;
	private static int nOfObjectTypes = 3;
	private static int nOfKeyTypes = 3;
	private static int bestSolutionsPoolSize = 2;
	public static ArtificialNeuralNetwork targetMap = 
			new ArtificialNeuralNetwork("random", neuralMapSize, nOfObjectTypes, nOfKeyTypes);
	private static Comparator<ArtificialNeuralNetwork> comparator = new NeuralNetworkComparator();
	private static PriorityQueue<ArtificialNeuralNetwork> bestSolutions;
	static int nOfProcesses;

	private static void initialize()
	{
		nOfProcesses = 0;
		bestSolutions = new PriorityQueue<ArtificialNeuralNetwork>(bestSolutionsPoolSize, comparator);
		for (int counter=0; counter<100; counter++)//while(bestSolutions.size()<bestSolutionsPoolSize)
		{
			addRandomNN();
		}
		
		

	}
	
	public static void main(String[] args) {
		initialize();
		
		
		double comparasionScore;
		double maxScore;
		
		comparasionScore = mutate();
		maxScore = comparasionScore;
		System.out.println("Number of iterations: "+ nOfProcesses + " " + "Mutate: " + comparasionScore);
		
		while (maxScore < 0.999)
		{
			//if (nOfProcesses%10000 == 0)
			//	System.out.println("Number of iterations: "+ nOfProcesses + " MaxScore: " + maxScore);
			
			comparasionScore = mutate();
			if (comparasionScore > maxScore)
			{
				maxScore = comparasionScore;
				System.out.println("Number of iterations: "+ nOfProcesses + " " + "Mutate: " + comparasionScore);
			}

			
			if (true)
			{
				comparasionScore = crossOver();
				if (comparasionScore > maxScore)
				{
					maxScore = comparasionScore;
					System.out.println("Number of iterations: "+ nOfProcesses + " " + "Cross Over: " + comparasionScore);
				}
			}

			if (maxScore<0)
			{
				comparasionScore = addRandomNN();
				if (comparasionScore > maxScore)
				{
					maxScore = comparasionScore;
					System.out.println("Number of iterations: "+ nOfProcesses + " " + "Random: " + comparasionScore);
				}
			}

		}

	}
	
	private static double addRandomNN() {
		nOfProcesses += 1;
		ArtificialNeuralNetwork product = new ArtificialNeuralNetwork("random", neuralMapSize, nOfObjectTypes, nOfKeyTypes);
		bestSolutions.add(product);
		return product.score;
	}

	private static double mutate() {
		nOfProcesses += 1;
		ArtificialNeuralNetwork aNN1 = getRandomAnnFromPool();
		ArtificialNeuralNetwork aNN2 = new ArtificialNeuralNetwork("random", neuralMapSize, nOfObjectTypes, nOfKeyTypes);
		
		ArtificialNeuralNetwork product = new ArtificialNeuralNetwork("mutate", neuralMapSize, nOfObjectTypes, nOfKeyTypes, aNN1, aNN2);
		bestSolutions.add(aNN1);
		bestSolutions.add(aNN2);
		bestSolutions.add(product);
		return product.score;
	}

	private static double crossOver() {
		nOfProcesses += 1;
		ArtificialNeuralNetwork aNN1 = getRandomAnnFromPool();
		ArtificialNeuralNetwork aNN2 = getRandomAnnFromPool();
		
		ArtificialNeuralNetwork product = new ArtificialNeuralNetwork("crossOver", neuralMapSize, nOfObjectTypes, nOfKeyTypes, aNN1, aNN2);
		bestSolutions.add(aNN1);
		bestSolutions.add(aNN2);
		bestSolutions.add(product);
		return product.score;
		
	}

	private static ArtificialNeuralNetwork getRandomAnnFromPool() {
		int counter = 0;
		ArtificialNeuralNetwork[] bestNSolutions = new ArtificialNeuralNetwork[bestSolutionsPoolSize];
		ArtificialNeuralNetwork randomlySelected = null;
		while (bestSolutions.size() > 1 && counter < bestSolutionsPoolSize)
		{
			bestNSolutions[counter] = bestSolutions.remove();//TODO getRandomAnn();
			
			counter += 1;
		}
		
		while (bestSolutions.size() > 10)//prevents memory leaks
			bestSolutions.remove();
		
		int randomIndex = randSeed.nextInt(counter);
		randomlySelected = bestNSolutions[randomIndex];
		
		counter=0;
		while (counter < bestSolutionsPoolSize)
		{
			if (counter != randomIndex)
				bestSolutions.add(bestNSolutions[counter]);
			counter += 1;
		}

		
		return randomlySelected;
	}

}
