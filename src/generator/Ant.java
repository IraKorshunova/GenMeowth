package generator;

import java.util.ArrayList;

public interface Ant<T> {

    /**
     * This method models movement of ants with recalculation quantity of
     * pheromone
     * 
     * @param stepsCount the quantity of steps which needs to be made
     * @exception IllegalArgumentException if steps count is negative
     */
    public void makeStep(int stepsCount);

    /**
     * This method models movement of ants on one step with recalculation
     * quantity of pheromone
     */
    public void makeStep();

    
    public T generateObject();

    public ArrayList<T> generateObjectsArray(int objectsCount);

}