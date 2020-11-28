package Model;

public class SelectionModel {
    private int selectedRow = -1;
    private int selectedColumn = -1;

    public SelectionModel() {
    }

    public void clear() {
        selectedRow = -1;
        selectedColumn = -1;
    }

    public boolean isEmpty() {
        return selectedRow == -1;
    }

    public int getRow() {
        return selectedRow;
    }

    public int getColumn() {
        return selectedColumn;
    }

    public void select(int row, int column) {
        selectedRow = row;
        selectedColumn = column;
    }
}
