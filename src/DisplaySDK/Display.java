package DisplaySDK;

import java.util.LinkedList;

public interface Display {
    public void getListName(String name1, String name2);

    public void displayTable(LinkedList<?> personList, LinkedList<?> windowList);

}