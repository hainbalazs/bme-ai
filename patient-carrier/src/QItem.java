public class QItem {
    public int row;
    public int col;
    public int dist;
    public QItem parent = null;
    public QItem(int row, int col, int dist, QItem parent)
    {
        this.row = row;
        this.col = col;
        this.dist = dist;
        this.parent = parent;
    }
}
