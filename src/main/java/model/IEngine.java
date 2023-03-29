package model;

import java.util.ArrayList;

public interface IEngine {

    void suffleObjects();

    void setChosenObjectReady(MemoryObject object);

    void compareObjects(ArrayList<MemoryObject> objectList);

    void setMemoryObjects();

    void addMemoryObjectsToList(Integer amount);

    void addToComparing(int i);

    void clearPair(ArrayList<MemoryObject> memoryList);

    void setPersonalScore();

    int getTotalScore();

    void clearStorage();
    void endGame();
    void setReturnStatus(boolean returnStatus);
    int getActiveId();
    long getTimerTime();
    void stopTimer();

    ModeType getType();
}
