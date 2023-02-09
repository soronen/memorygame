package model;

import java.util.ArrayList;

public interface IEngine {

    void suffleObjects();

    void setChosenObjectReady(MemoryObject object);

    int getNextScore();

    void compareObjects(ArrayList<MemoryObject> objectList);

    void setMemoryObjects();

    void addMemoryObjectsToList(Integer amount);

    void addToComparing(int i);

    void clearPair(ArrayList<MemoryObject> memoryList);

    void setPersonalScore();

    void clearStorage();

    int getTotalScore();
}
