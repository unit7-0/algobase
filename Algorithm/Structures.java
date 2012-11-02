package Algorithm;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * File: DataStructures.java
 * User: unit7
 * Date: 06.08.12
 * Time: 14:36
 */

public class Structures {
    public static abstract class IntervalTree {
        public IntervalTree(Object[] data, Comparator comparator) {
            this.comparator = comparator;
            tree = new int[data.length << 2];
            buildTree(1, 0, data.length - 1, data);
        }

        public int getValue(int l, int r) {
            return getValue(1, 0, (tree.length >> 2) - 1, l, r);
        }

        abstract int getValue(int u, int tl, int tr, int l, int r);

        private void buildTree(int u, int l, int r, Object[] data) {
            if(l == r)  tree[u] = (Integer)data[l];
            else {
                int mid = l + r >> 1, u1 = u << 1, u2 = u1 + 1;
                buildTree(u1, l, mid, data);
                buildTree(u2, mid + 1, r, data);
                tree[u] = comparator.getValue(tree[u1], tree[u2]);
            }
        }

        public interface Comparator {
            public int getValue(int v1, int v2);
        }

        protected int[] tree;
        private Comparator comparator;
    }

    public static class DisjointSet {
        public DisjointSet(int n) {
            parent = new int[n];
            size = new int[n];
            for(int i = 0; i < n; size[i] = 1, parent[i] = i++);
        }

        public int findSet(int u) {
            return parent[u] = (u == parent[u] ? u : findSet(parent[u]));
        }

        public void unionSet(int u, int v) {
            u = findSet(u); v = findSet(v);
            if(u == v)  return;
            if(size[u] < size[v]) {
                u ^= v;
                v ^= u;
                u ^= v;
            }
            parent[v] = u;
            size[u] += size[v];
        }

        private int[] parent;
        private int[] size;
    }

    public static class Trie {
        public Trie(int len) {
            size = 0;
            self = new Edge[len];
            for(int i = 0; i < len; ++i)    self[i] = new Edge();
        }

        public void addString(String str) {
            int v = 0;
            for(int i = 0; i < str.length(); ++i) {
                if(!self[v].next.containsKey(str.charAt(i)))
                    self[v].next.put(str.charAt(i), ++size);
                v = self[v].next.get(str.charAt(i));
            }
            self[v].leaf = true;
        }

        public boolean findString(String str) {
            int v = 0;
            for(int i = 0; i < str.length(); ++i) {
                if(!self[v].next.containsKey(str.charAt(i)))    return false;
                v = self[v].next.get(str.charAt(i));
            }
            return self[v].leaf;
        }

        private int size;
        private Edge[] self;

        private class Edge {
            public Edge() {
                leaf = false;
                next = new HashMap<Character, Integer>();
            }

            public boolean leaf;
            public HashMap<Character, Integer> next;
        }
    }
}
