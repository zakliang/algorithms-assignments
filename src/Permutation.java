import edu.princeton.cs.algs4.StdIn;

/**
 * Created by zak on 1/3/17.
 */

public class Permutation {
    public static void main(String[] args) {
        int k = 0;
        if (args != null && args.length > 0) {
            k = Integer.parseInt(args[0]);
        }
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        int i = 0;
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();

            if (!queue.isEmpty() && i >= k) {
                queue.dequeue();
            }

            queue.enqueue(s);
        }

        i = 0;

        for (String s : queue) {
            if (i >= k) {
                break;
            }
            System.out.println(s);
            i++;
        }

    }
}