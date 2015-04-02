package generator;

import generator.exception.NoSuchEdgeException;
import generator.exception.NoSuchNodeException;
import java.util.ArrayList;

public class ComplexNetwork<T extends Cloneable & Equalable> implements
	Graph<T> {
    private ArrayList<T> graphNodes;
    private ArrayList<Edge> graphEdges;

    protected class Edge implements Cloneable {
	private T outNode;
	private T inNode;
	private double weight;

	public Edge(T outNode, T inNode) {
	    this.outNode = copy(outNode);
	    this.inNode = copy(inNode);
	    weight = 1.0;
	}

	public T getOutNode() {
	    return outNode;
	}

	public T getInNode() {
	    return inNode;
	}

	public void setWeight(double weight) {
	    if (Double.compare(weight, 0.0) <= 0) {
		remove(outNode, inNode);
	    } else {
		this.weight = weight;
	    }
	}

	public double getWeight() {
	    return weight;
	}

	public void addWeight(double dWeight) {
	    weight += dWeight;
	    if (Double.compare(weight, 0.0) <= 0) {
		remove(outNode, inNode);
		/*
		 * тут дуже цікаво і не зрозуміло if(this == null)
		 * System.out.println("Wau");
		 */
	    }
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
		return true;
	    }
	    if ((this == null) || !(obj instanceof ComplexNetwork<?>.Edge)) {
		return false;
	    }
	    @SuppressWarnings("unchecked")
	    Edge compEdge = (Edge) obj;
	    return compEdge.getOutNode().equals(outNode)
		    && compEdge.getInNode().equals(inNode);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Edge clone() {
	    Edge clone = null;
	    try {
		clone = (Edge) super.clone();
		clone.outNode = copy(this.outNode);
		clone.inNode = copy(this.inNode);
	    } catch (CloneNotSupportedException e) {
		e.printStackTrace();
	    }
	    return clone;
	}
    }

    public ComplexNetwork() {
	graphNodes = new ArrayList<T>();
	graphEdges = new ArrayList<Edge>();
    }

    public ComplexNetwork(ArrayList<T> list) {
	this();
	if (list.size() > 0) {
	    T outNode = null;
	    for (T inNode : list) {
		if (outNode == null) {
		    outNode = list.get(0);
		    add(outNode);
		} else {
		    add(inNode);
		    if (!add(outNode, inNode)) {
			addWeight(outNode, inNode, 1);
		    }
		    outNode = inNode;
		}
	    }
	    renormalizationEdgesWeights();
	}
    }

    @Override
    public boolean add(T node) {
	if (graphNodes.contains(node)) {
	    return false;
	}
	graphNodes.add(copy(node));
	return true;
    }

    @Override
    public boolean add(T outNode, T inNode) {
	if (!graphNodes.contains(outNode) || !graphNodes.contains(inNode)) {
	    throw new NoSuchNodeException();
	}
	Edge tempEdge = new Edge(outNode, inNode);
	if (graphEdges.contains(tempEdge)) {
	    return false;
	}
	graphEdges.add(tempEdge);
	return true;
    }

    @Override
    public boolean remove(T node) {
	ArrayList<Edge> incidentEdges = getIncidentEdges(node);
	while (incidentEdges.size() != 0) {
	    graphEdges.remove(incidentEdges.get(0));
	    incidentEdges.remove(0);
	}
	return graphNodes.remove(node);
    }

    @Override
    public boolean remove(T outNode, T inNode) {
	return graphEdges.remove(new Edge(outNode, inNode));
    }

    @Override
    public boolean contains(T node) {
	return graphNodes.contains(node);
    }

    @Override
    public boolean contains(T outNode, T inNode) {
	return graphEdges.contains(new Edge(outNode, inNode));
    }

    /**
     * @throws NoSuchEdgeException if this edge is no contains in graph
     */
    @Override
    public void setWeight(T outNode, T inNode, double weight) {
	if (!contains(outNode, inNode)) {
	    throw new NoSuchEdgeException();
	}
	if (Double.compare(weight, 0.0) <= 0) {
	    remove(outNode, inNode);
	} else {
	    graphEdges.get(graphEdges.indexOf(new Edge(outNode, inNode)))
		    .setWeight(weight);
	}
    }

    /**
     * @throws NoSuchEdgeException if this edge is no contains in graph
     */
    @Override
    public double getWeight(T outNode, T inNode) {
	if (!contains(outNode, inNode)) {
	    throw new NoSuchEdgeException();
	}
	return graphEdges.get(graphEdges.indexOf(new Edge(outNode, inNode)))
		.getWeight();
    }

    /**
     * @throws NoSuchEdgeException if this edge is no contains in graph
     */
    @Override
    public void addWeight(T outNode, T inNode, double dWeight) {
	if (!contains(outNode, inNode)) {
	    throw new NoSuchEdgeException();
	}
	graphEdges.get(graphEdges.indexOf(new Edge(outNode, inNode)))
		.addWeight(dWeight);
    }

    @Override
    public void clear() {
	graphNodes = new ArrayList<T>();
	graphEdges = new ArrayList<Edge>();
    }

    public void buildComplexNetwork(ArrayList<T> list) {
	if (list.size() > 0) {
	    T outNode = null;
	    for (T inNode : list) {
		if (outNode == null) {
		    outNode = list.get(0);
		    add(outNode);
		} else {
		    add(inNode);
		    if (!add(outNode, inNode)) {
			addWeight(outNode, inNode, 1);
		    }
		    outNode = inNode;
		}
	    }
	    renormalizationEdgesWeights();
	}
    }

    public void renormalizationEdgesWeights() {
	for (T node : graphNodes) {
	    ArrayList<Edge> edges = getEdges(node);
	    double sumWeight = 0;
	    for (Edge edge : edges) {
		sumWeight += edge.getWeight();
	    }
	    for (Edge edge : edges) {
		edge.setWeight(edge.getWeight() / sumWeight);
	    }
	}
    }

    public ArrayList<T> getCopyOfGraphNodes() {
	ArrayList<T> copy = new ArrayList<T>();
	for (T temp : graphNodes) {
	    copy.add(copy(temp));
	}
	return copy;
    }

    /**
     * 
     * @return uniformly distributed random node
     */
    public T getRandomNode() {
	int size = graphNodes.size();
	double random = Math.random();
	double sum = 0;
	for (int i = 0; i < size; i++) {
	    sum += 1.0 / size;
	    if(sum > random) {
		return graphNodes.get(i);
	    }
	}
	return null;
    }

    /**
     * @param node
     * @return list of edge which contains specified node
     */
    protected ArrayList<Edge> getIncidentEdges(T node) {
	ArrayList<Edge> result = new ArrayList<Edge>();
	for (Edge edge : graphEdges) {
	    if (edge.inNode.equals(node) || edge.outNode.equals(node)) {
		result.add(edge);
	    }
	}
	return result;
    }

    /**
     * @param node
     * @return list of edge which contains specified node in the capacity of
     *         outNode
     */
    protected ArrayList<Edge> getEdges(T node) {
	ArrayList<Edge> result = new ArrayList<Edge>();
	for (Edge edge : graphEdges) {
	    if (edge.outNode.equals(node)) {
		result.add(edge);
	    }
	}
	return result;
    }

    @SuppressWarnings("unchecked")
    protected T copy(T node) {
	T clone = null;
	try {
	    clone = (T) node.getClass().getMethod("clone").invoke(node);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return clone;
    }

}