package norakomi.sovietposterart.Adapters;


public abstract class GridItem {

    public final long id;
    public final String title;
    public String url; // can't be final as some APIs use different serialized names
    public String dataSource;
    public int page;
    public float weight;
    public float weightBoost;

    public GridItem(long id,
                    String title,
                    String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    @Override
    public String toString() {
        return title;
    }

    /**
     * Equals check based on the id field
     */
    @Override
    public boolean equals(Object o) {
        return (o.getClass() == getClass() && ((GridItem) o).id == id);
    }
}
