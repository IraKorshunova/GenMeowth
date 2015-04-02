package generator;

import generator.exception.NoSuchNodeException;
import static java.lang.Math.*;
import java.util.ArrayList;

public class AntHill<T extends Cloneable & Equalable> extends ComplexNetwork<T>
	implements Ant<T> {

    private int countSilentAnts;
    private ArrayList<Double> antsPheromone;
    private double evaporationCoef;
    private ArrayList<T> positions;
    private T generatingAntPosition;
    public static int PATERNS_BUILDING = 0;
    public static int NATURALS_BUILDING = 1;

    /**
     * @param list
     * @param count
     * @throws IllegalArgumentException if transmission attributes is illegal
     */
    public AntHill(ArrayList<T> list, int count, int type) {
	if (count < 0 || (type != 0 && type != 1)) {
	    throw new IllegalArgumentException();
	}
	if (type == PATERNS_BUILDING) {
	    buildComplexNetwork(list);
	}
	if (type == NATURALS_BUILDING) {
	    for (T node : list) {
		add(node);
	    }
	    for (T outNode : list) {
		for (int i = list.indexOf(outNode); i < list.size(); i++) {
		    T inNode = list.get(i);
		    add(outNode, inNode);
		    add(inNode, outNode);
		    if (outNode.equals(inNode)) {
			setWeight(outNode, inNode, 1.0 / 4.0);
		    } else {
			setWeight(
				outNode,
				inNode,
				1.0 / abs(list.indexOf(inNode)
					- list.indexOf(outNode)));
			setWeight(
				inNode,
				outNode,
				1.0 / abs(list.indexOf(inNode)
					- list.indexOf(outNode)));
		    }
		}
	    }
	    renormalizationEdgesWeights();
	}

	evaporationCoef = 0.01;
	countSilentAnts = count;
	antsPheromone = new ArrayList<Double>(count);
	positions = new ArrayList<T>(count);
	if (list.size() != 0) {
	    generatingAntPosition = list.get(0);
	    for (int i = 0; i < count; i++) {
		antsPheromone.add(0.1);
		positions.add(getRandomNode());
	    }
	}
    }

    public int getCountSilentAnts() {
	return countSilentAnts;
    }

    /**
     * @param node position of ant
     * @param ant number of ant
     * @throws NoSuchNodeException if node is no contains in AntHill
     * @throws IllegalArgumentException if ant is negative
     */
    public void setSilentAntPosition(T node, int ant) {
	if (ant < 0 || ant > countSilentAnts) {
	    throw new IllegalArgumentException();
	}
	if (!contains(node)) {
	    throw new NoSuchNodeException();
	}
	positions.set(ant, node);
    }

    /**
     * @param node position of ant
     * @throws NoSuchNodeException if node is no contains in AntHill
     */
    public void setGeneratingAntPosition(T node) {
	if (!contains(node)) {
	    throw new NoSuchNodeException();
	}
	generatingAntPosition = node;
    }

    public double getEvaporationCoefficient() {
	return evaporationCoef;
    }

    /**
     * @param ant number of ant
     * @return
     * @throws IllegalArgumentException if ant is negative
     */
    public double getPheromone(int ant) {
	if (ant < 0 || ant > countSilentAnts) {
	    throw new IllegalArgumentException();
	}
	return antsPheromone.get(ant);
    }

    public double getPheromone(T outNode, T inNode) {
	return getWeight(outNode, inNode);
    }

    public void makeStep(int stepsCount) {
	for (int i = 0; i < stepsCount; i++) {
	    makeStep();
	    System.out.println(i);
	}
    }

    public void makeStep() {
	for (int i = 0; i < countSilentAnts; i++) {
	    T position = positions.get(i);
	    ArrayList<Edge> edges = getEdges(position);
	    double[] probabilities = new double[edges.size()];
	    for (int j = 0; j < probabilities.length; j++) {
		probabilities[j] = edges.get(j).getWeight();
	    }
	    double sum = 0;
	    double random = random();
	    for (int j = 0; j < probabilities.length; j++) {
		sum += probabilities[j];
		if (sum > random) {
		    positions.set(i, edges.get(j).getInNode());
		    edges.get(j).addWeight(
			    antsPheromone.get(i) - evaporationCoef);
		    renormalizationEdgesWeights();
		    break;
		}
	    }
	}
    }

    public T generateObject() {
	ArrayList<Edge> edges = getEdges(generatingAntPosition);
	while (edges.size() == 0) {
	    edges = getEdges(getRandomNode());
	}
	double[] probabilities = new double[edges.size()];
	for (int j = 0; j < probabilities.length; j++) {
	    probabilities[j] = edges.get(j).getWeight();
	}
	double sum = 0;
	double random = random();
	for (int j = 0; j < probabilities.length; j++) {
	    sum += probabilities[j];
	    if (sum > random) {
		generatingAntPosition = edges.get(j).getInNode();
		return edges.get(j).getOutNode();
	    }
	}

	return null;
    }

    public ArrayList<T> generateObjectsArray(int objectsCount) {
	ArrayList<T> result = new ArrayList<T>(objectsCount);
	for (int i = 0; i < objectsCount; i++) {
	    result.add(generateObject());
	}
	return result;
    }
}