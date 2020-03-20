package Race;

public abstract class Stage {
    protected int length;
    protected String description;

    public abstract void go(Car car);
}

