package ObjectTrans;
import java.io.Serializable;
import java.util.LinkedList;

public class LinkedListTrans implements Serializable {

    private LinkedList<?> personList;
    private LinkedList<?> windowList;

    public LinkedListTrans(LinkedList<?> personList, LinkedList<?> windowList) {
        this.personList = personList;
        this.windowList = windowList;
    }

    public void setPersonList(LinkedList<?> personList) {
        this.personList = personList;
    }

    public void setWindowList(LinkedList<?> windowList) {
        this.windowList = windowList;
    }

    public LinkedList<?> getPersonList() {
        return this.personList;
    }

    public LinkedList<?> getWindowList() {
        return this.windowList;
    }
}
