package model;

import java.util.ArrayList;

public interface IEngine {

    void suffleObjects();

    ArrayList<MemoryObject> getSuffledObjects();

    void setChosenObjectReady(MemoryObject object);

    int getNextScore();

    CompareResultType compareObjects(ArrayList<MemoryObject> objectList);

    void setMemoryObjects();

    void addMemoryObjectsToList(Integer amount);

    void addToComparing(int i);

    void clearPair(ArrayList<MemoryObject> memoryList);

    void setPersonalScore();

    int getTotalScore();
}
