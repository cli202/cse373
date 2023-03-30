package problems;

import datastructures.LinkedIntList;
// Checkstyle will complain that this is an unused import until you use it in your code.
import datastructures.LinkedIntList.ListNode;

/**
 * See the spec on the website for example behavior.
 * REMEMBER THE FOLLOWING RESTRICTIONS:
 * - do not call any methods on the `LinkedIntList` objects.
 * - do not construct new `ListNode` objects for `reverse3` or `firstToLast`
 *      (though you may have as many `ListNode` variables as you like).
 * - do not construct any external data structures such as arrays, queues, lists, etc.
 * - do not mutate the `data` field of any node; instead, change the list only by modifying
 *      links between nodes.
 */

public class LinkedIntListProblems {

    /**
     * Reverses the 3 elements in the `LinkedIntList` (assume there are exactly 3 elements).
     */
    public static void reverse3(LinkedIntList list) {
        ListNode newFront = list.front.next.next;
        newFront.next = list.front.next;
        newFront.next.next = list.front;
        list.front.next = null;
        list.front = newFront;
    }

    /**
     * Moves the first element of the input list to the back of the list.
     */
    public static void firstToLast(LinkedIntList list) {
        if (list.front != null && list.front.next != null) {
            ListNode back = list.front;
            while (back.next != null) {
                back = back.next;
            }
            ListNode newFront = list.front.next;
            back.next = list.front;
            list.front.next = null;
            list.front = newFront;
        }
    }

    /**
     * Returns a list consisting of the integers of a followed by the integers
     * of n. Does not modify items of A or B.
     */
    public static LinkedIntList concatenate(LinkedIntList a, LinkedIntList b) {
        LinkedIntList result = new LinkedIntList();
        ListNode resultCurrent;
        boolean aIsNull = a.front == null;
        boolean bIsNull = b.front == null;
        if (!aIsNull) {
            result.front = new ListNode(a.front.data);
            resultCurrent = result.front;
            ListNode aCurrent = a.front;
            while (aCurrent.next != null) {
                resultCurrent.next = new ListNode(aCurrent.next.data);
                resultCurrent = resultCurrent.next;
                aCurrent = aCurrent.next;
            }
            if (!bIsNull) {
                resultCurrent.next = new ListNode(b.front.data);
                resultCurrent = resultCurrent.next;
                ListNode bCurrent = b.front;
                while (bCurrent.next != null) {
                    resultCurrent.next = new ListNode(bCurrent.next.data);
                    resultCurrent = resultCurrent.next;
                    bCurrent = bCurrent.next;
                }
            }
            // return result;
        }
        if (aIsNull && !bIsNull) {
            result.front = new ListNode(b.front.data);
            resultCurrent = result.front;
            ListNode bCurrent = b.front;
            while (bCurrent.next != null) {
                resultCurrent.next = new ListNode(bCurrent.next.data);
                resultCurrent = resultCurrent.next;
                bCurrent = bCurrent.next;
            }
        }
        return result;
    }
}
