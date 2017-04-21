package teamnine.AI;

import java.util.ArrayList;
import java.util.Comparator;

public class QuickSort <E> {
	
	public ArrayList sort (ArrayList <E> list, Comparator <E> comparator){
        //BASE CASE
        if(list.size()==1){
            return (list);
        }

        //RECURSIVE CALL
            int pivot = (int) Math.random()*list.size();
            E pivotElement = list.get(pivot);
            ArrayList smallerList = new ArrayList();
            ArrayList biggerList = new ArrayList();
            ArrayList equalList = new ArrayList();
            E thisElement;


            for (int i=0; i<list.size(); i++){
                thisElement = list.get(i);
                int result = comparator.compare(pivotElement, thisElement);

                if (result == 1){
                    biggerList.add(thisElement);
                }else if( result == 0){
                    equalList.add(thisElement);
                }else {
                    smallerList.add(thisElement);
                }
            }

            ArrayList newList = new ArrayList();

            if (!smallerList.isEmpty()) {
                newList.addAll(sort(smallerList, comparator));
            }
            if(!equalList.isEmpty()) {
                newList.addAll(equalList);
            }
            if(!biggerList.isEmpty()){
                newList.addAll(sort(biggerList, comparator));
            }

            return newList;
    }
}
