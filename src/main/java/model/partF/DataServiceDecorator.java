
package model.partF;

/**
 *
 * @author Deneth
 */
public abstract class DataServiceDecorator implements DataService {
    protected DataService wrappee;

    public DataServiceDecorator(DataService wrappee) {
        this.wrappee = wrappee;
    }
}
