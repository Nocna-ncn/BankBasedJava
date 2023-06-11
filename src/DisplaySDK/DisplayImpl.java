package DisplaySDK;

import java.util.LinkedList;

public class DisplayImpl implements Display {

    private String name1;
    private String name2;

    @Override
    public void getListName(String name1, String name2) {
        this.name1 = name1;
        this.name2 = name2;
    }

    @Override
    public void displayTable(LinkedList<?> personList, LinkedList<?> windowList) {
        System.out.println("=======================");
        System.out.println(name1 + "\t" + name2);
        int size = Math.max(personList.size(), windowList.size());
        Integer windowNumber = 0;

        String commant = "";

        for (int i = 0; i < size; i++) {
            String tempString1 = (i < personList.size() && personList.get(i) != null) ? personList.get(i).toString()
                    : "null";
            String tempString2 = (i < windowList.size() && windowList.get(i) != null) ? windowList.get(i).toString()
                    : "null";
            if (i < windowList.size()) {
                windowNumber = i + 1;

                if (tempString2.equals("null")) {
                    commant = windowNumber.toString() + " 空闲 ";
                } else if (tempString2.equals("-1")) {
                    commant = windowNumber.toString() + " 停止服务 ";                    
                } else {
                    commant = windowNumber.toString() + " 叫号: " + tempString2;
                }

                System.out.println(tempString1 + "\t" + commant);
            } else {
                System.out.println(tempString1 + "\t");
            }
        }

    }
}
