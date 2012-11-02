package Algorithm;

/**
 * Created with IntelliJ IDEA.
 * File: Pair.java
 * User: unit7
 * Date: 06.08.12
 * Time: 14:36
 */

public class Pair<E extends Comparable, V extends Comparable> implements Comparable<Pair<E, V>> {
        public Pair(E first, V second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int compareTo(Pair<E, V> other) {
            if(first.equals(other.first))   return second.compareTo(other.second);
            return first.compareTo(other.first);
        }

        @Override
        public boolean equals(Object other) {
            Pair<E, V> obj = (Pair)other;
            return (first == obj.first && second == obj.second);
        }

        public E getFirst() {
            return first;
        }

        public V getSecond() {
            return second;
        }

        public void setFirst(E val) {
            first = val;
        }

        public void setSecond(V val) {
            second = val;
        }

        private E first;
        private V second;
    }
