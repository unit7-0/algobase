package Algorithm;

import java.util.*;

import static java.lang.Math.min;

/**
 * Created with IntelliJ IDEA.
 * File: Graph.java
 * User: unit7
 * Date: 06.08.12
 * Time: 14:36
 */

public class Graph {
    public static class ConnectedComponents {
        public static List[] getComponents(List<Integer>[] graph) {
            ArrayList<Integer>[] components = new ArrayList[graph.length];
            boolean[] used = new boolean[graph.length];
            int count = 0;

            Arrays.fill(used, false);
            for(int i = 0; i < components.length; ++i)  components[i] = new ArrayList<Integer>();
            for(int i = 0; i < graph.length; ++i) {
                if(!used[i])    dfs(i, graph, components[count++], used);
            }

            return components;
        }

        private static void dfs(int u, List<Integer>[] graph, List<Integer> components, boolean[] used) {
            used[u] = true;
            components.add(u);
            for(int i = 0; i < graph[u].size(); ++i) {
                int to = graph[u].get(i);
                if(!used[to])   dfs(to, graph, components, used);
            }
        }
    }

    public static long[] Dijkstra(int root, List<Pair<Integer, Long>>[] graph) {
        final long[] dist = new long[graph.length];
        Arrays.fill(dist, Long.MAX_VALUE / 2);
        dist[root] = 0;

        TreeSet<Integer> q = new TreeSet<Integer>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if(dist[o1] < dist[o2]) return -1;
                if(dist[o1] > dist[o2]) return 1;
                return o1.compareTo(o2);
            }
        });

        q.add(root);
        while(!q.isEmpty()) {
            int u = q.pollFirst();
            long d = dist[u];
            for(Pair<Integer, Long> p : graph[u]) {
                int to = p.getFirst();
                long newDist = p.getSecond() + d;
                if(dist[to] > newDist) {
                    q.remove(to);
                    dist[to] = newDist;
                    q.add(to);
                }
            }
        }
        return dist;
    }

    public static long[] FordBellman(int root, List<Pair<Integer, Long>>[] graph) {
        long[] dist = new long[graph.length];
        Arrays.fill(dist, Long.MAX_VALUE / 2);
        dist[root] = 0;

        boolean any = true;
        while(any) {
            any = false;
            for(int i = 0; i < graph.length; ++i) {
                for(Pair<Integer, Long> p : graph[i]) {
                    int to = p.getFirst();
                    long newDist = dist[i] + p.getSecond();
                    if(dist[to] > newDist) {
                        dist[to] = newDist;
                        any = true;
                    }
                }
            }
        }
        return dist;
    }

    public static long[][] FloydWarshall(long[][] graph) {
        long[][] dist = new long[graph.length][graph.length];
        for(int i = 0; i < graph.length; ++i)
            for(int j = 0; j < graph.length; ++j)
                dist[i][j] = graph[i][j];

        for(int k = 0; k < graph.length; ++k) {
            for(int i = 0; i < graph.length; ++i) {
                for(int j = 0; j < graph.length; ++j)
                    dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]);
            }
        }
        return dist;
    }

    public static List<Pair<Integer, Integer>> Kruskal(List<Pair<Long, Pair<Integer, Integer>>> edges, int n) {
        Structures.DisjointSet set = new Structures.DisjointSet(n);
        List<Pair<Integer, Integer>> result = new LinkedList<Pair<Integer, Integer>>();
        Collections.sort(edges);

        for(Pair<Long, Pair<Integer, Integer>> p : edges) {
            int first = p.getSecond().getFirst(), second = p.getSecond().getSecond();
            if(set.findSet(first) != set.findSet(second)) {
                set.unionSet(first, second);
                result.add(new Pair(first, second));
            }
        }
        return result;
    }

    public static class CycleGraph {
        public static List getCycle(ArrayList<Integer>[] graph) {
            int[] path = new int[graph.length];
            boolean[] used = new boolean[graph.length];

            List<Integer> result = new LinkedList<Integer>();
            Pair<Integer, Integer> startend = new Pair<Integer, Integer>(0, 0);
            if(dfs(0, graph, path, used, startend)) {
                for(int i = startend.getSecond(); i != startend.getFirst(); i = path[i])    result.add(i);
                result.add(startend.getFirst());
                Collections.reverse(result);
            }

            return result;
        }

        private static boolean dfs(int u, ArrayList<Integer>[] graph, int[] path, boolean[] used, Pair<Integer, Integer> startend) {
            used[u] = true;
            for(int v : graph[u]) {
                if(!used[v]) {
                    path[v] = u;
                    if(dfs(v, graph, path, used, startend))   return true;
                } else {
                    startend.setFirst(v);
                    startend.setSecond(u);
                    return true;
                }
            }
            return false;
        }
    }

    public static class Flows {
        public static class Dinic {
            public static int maxFlow(int[][] c, int start, int end) {
                int flow = 0;
                int[][] f = new int[c.length][c.length];
                int[] ptr = new int[c.length], net = new int[c.length];
                while(bfs(c, f, net, start, end)) {
                    int pushed;
                    Arrays.fill(ptr, -1);
                    for(;;) {
                        pushed = dfs((int)1e9, start, end, net, ptr, c, f);
                        if(pushed == 0) break;
                        flow += pushed;
                    }
                }
                return flow;
            }

            private static int dfs(int flow, int u, int end, int[] net, int[] ptr, int[][] c, int[][] f) {
                if(flow == 0 || u == end)   return flow;
                for(; ptr[u] < net.length; ++ptr[u]) {
                    if(net[ptr[u]] != net[u] + 1)   continue;
                    int pushed = dfs(min(flow, c[u][ptr[u]] - f[u][ptr[u]]), ptr[u], end, net, ptr, c, f);
                    if(pushed != 0) {
                        f[u][ptr[u]] += pushed;
                        f[ptr[u]][u] -= pushed;
                        return pushed;
                    }
                }
                return 0;
            }

            private static boolean bfs(int[][] c, int[][] f, int[] net, int start, int end) {
                Queue<Integer> q = new LinkedList<Integer>();
                q.add(start);
                Arrays.fill(net, -1);
                net[start] = 0;
                while(!q.isEmpty()) {
                    int from = q.poll();
                    for(int to = 0; to < c.length; ++to) {
                        if(net[to] == -1 && c[from][to] - f[from][to] > 0) {
                            net[to] = net[from] + 1;
                            q.add(to);
                        }
                    }
                }
                return net[end] != -1;
            }
        }
    }

    public static class LcaIntervalTree {
        public LcaIntervalTree(List<Integer>[] graph) {
            first = new int[graph.length];
            depth = new int[graph.length];
            used = new boolean[graph.length];
            list = new ArrayList<Integer>(graph.length << 1);

            dfs(0, 0, graph);
            lcaTree = new IntervalTree(list.toArray(), new Algorithm.Structures.IntervalTree.Comparator() {
                @Override
                public int getValue(int v1, int v2) {
                    if(depth[v1] < depth[v2])   return v1;
                    else    return v2;
                }
            });

            Arrays.fill(first, -1);
            for(int i = 0; i < list.size(); ++i) {
                int to = list.get(i);
                if(first[to] == -1) first[to] = i;
            }
        }

        public int getLca(int u1, int u2) {
            int l = first[u1], r = first[u2];
            if(l > r) {
                l ^= r;
                r ^= l;
                l ^= r;
            }
            return lcaTree.getValue(l, r);
        }

        private void dfs(int u, int h, List<Integer>[] graph) {
            used[u] = true;
            list.add(u);
            depth[u] = h;
            for(int to : graph[u]) {
                if(!used[to]) {
                    dfs(to, h + 1, graph);
                    list.add(u);
                }
            }
        }

        private class IntervalTree extends Structures.IntervalTree {
            public IntervalTree(Object[] data, Comparator comparator) {
                super(data, comparator);
            }

            @Override
            protected int getValue(int u, int tl, int tr, int l, int r) {
                if(tl > r || tr < l)    return -1;
                if(tl >= l && tr <= r)  return tree[u];
                else {
                    int mid = tl + tr >> 1, u1 = u << 1, u2 = u1 + 1;
                    int lca1 = getValue(u1, tl, mid, l, r),
                            lca2 = getValue(u2, mid + 1, tr, l, r);
                    if(lca1 == -1)  return lca2;
                    if(lca2 == -1)  return lca1;
                    return depth[lca1] < depth[lca2] ? lca1 : lca2;
                }
            }
        }

        private int[] depth;
        private int[] first;
        private boolean[] used;
        private IntervalTree lcaTree;
        private ArrayList<Integer> list;
    }

    public static class Khun {
        public static int[] getPairs(List<Integer>[] graph) {
            int[] mt = new int[graph.length], used = new int[graph.length];
            Arrays.fill(mt, -1);
            int color = 1;
            for(int i = 0; i < graph.length; ++i) {
                try_khun(i, graph, mt, used, color);
                ++color;
            }
            return mt;
        }

        private static boolean try_khun(int u, List<Integer>[] graph, int[] mt, int[] used, int color) {
            if(used[u] == color)    return false;
            used[u] = color;
            for(int i = 0; i < graph[u].size(); ++i) {
                int to = graph[u].get(i);
                if(mt[to] == -1 || try_khun(to, graph, mt, used, color)) {
                    mt[to] = u;
                    return true;
                }
            }
            return false;
        }
    }
}
