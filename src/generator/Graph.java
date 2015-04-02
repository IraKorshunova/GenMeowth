package generator;

public interface Graph<T extends Cloneable & Equalable> {

    /**
     * @param node element to be appended to this graph
     * @return <tt>true</tt> if this graph changed as a result of the call
     */
    public boolean add(T node);

    /**
     * Default weight of edge outNode -> inNode is 1.0
     * 
     * @param outNode initial node for this edge
     * @param inNode final node for this edge
     * @return <tt>true</tt> if this graph changed as a result of the call
     * @throws NoSuchNodeException if outNode or inNode is no contains in graph.
     */
    public boolean add(T outNode, T inNode);

    /**
     * @param node element to be removed from this graph, if present
     * @return <tt>true</tt> if this graph contained the specified node
     */
    public boolean remove(T node);

    /**
     * @param outNode initial node for edge whose be removed from this graph, if
     *            present
     * @param inNode final node for this edge whose be removed from this graph,
     *            if present
     * @return <tt>true</tt> if this graph contained the specified edge
     */
    public boolean remove(T outNode, T inNode);

    /**
     * @param node element whose presence in this graph is to be tested
     * @return <tt>true</tt> if this graph contained the specified element
     */
    public boolean contains(T node);

    /**
     * @param outNode initial node for edge whose presence in this graph is to
     *            be tested
     * @param inNode final node for this edge whose presence in this graph is to
     *            be tested
     * @return <tt>true</tt> if this graph contained edge outNode -> inNode
     */
    public boolean contains(T outNode, T inNode);

    /**
     * If weight of edge outNode -> inNode is less or equal 0.0 this edge remove
     * from graph.
     * 
     * @param outNode initial node for edge whose change his weight
     * @param inNode final node for edge whose change his weight
     * @param weight setting value of weight for edge outNode -> inNode
     * @throws NoSuchEdgeException if this edge is no contains in graph
     */
    public void setWeight(T outNode, T inNode, double weight);

    /**
     * @param outNode initial node for edge
     * @param inNode final node for edge
     * @return value of weight for edge outNode -> inNode
     * @throws NoSuchEdgeException if this edge is no contains in graph
     */
    public double getWeight(T outNode, T inNode);

    /**
     * If weight of edge outNode -> inNode is less or equal 0.0 this edge remove
     * from graph.
     * 
     * @param outNode initial node for edge
     * @param inNode final node for edge
     * @param dWeight add weight value for edge outNode -> inNode
     * @throws NoSuchEdgeException if this edge is no contains in graph
     */
    public void addWeight(T outNode, T inNode, double dWeight);

    /**
     * Removes all of nodes and edges from this graph. The graph will be empty
     * after this call returns.
     */
    public void clear();
}