package Interfaces;

import Model.Square;
import Model.State;

public interface FieldHandler {

    void updateSquare(Square square);
    void  updateBoard();
    void changeState(State state);
}
