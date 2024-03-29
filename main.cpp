#include <queue>
#include <cmath>
#include <memory.h>
#include <limits>
#include <vector>
#include <iostream>
#include <iterator>
#include <algorithm>

using namespace std;

typedef long long ll;

ll binpow(int a, int n);
ll gcd(ll a, ll b);

bool* getPrimes(int n);

int* Dijkstra(vector<pair<int, int> > *g, int n, int start);
int* FordBellman(vector<pair<int, int> > *g, int n, int start);
int** Floyd(int** g, int n);

class DSU {
public:
    DSU(size_t n);

    void unionSet(int u, int v);
    int findSet(int u);

private:
    vector<int> parent, size;
};

struct edge {
    edge(int a, int b, int weight): a(a), b(b), weight(weight) {
    }

    int a, b;
    int weight;

    friend bool operator <(const edge& first, const edge& second) {
        return first.weight < second.weight;
    }
};

class LCA {
public:
    LCA(vector<int>* g, int n, int start = 0);

    int getLca(int u, int v);

private:
    void dfs(int u, vector<int>* g, int d = 0);
    void buildTree(int u, int l, int r);

    int getMinLca(int u, int tl, int tr, int l, int r);

    vector<int> list, tree, depth, first;
    vector<bool> used;
};

void Kruskal(vector<edge> g, int n);

ll EdmondsKarp(int** c, int n, int start, int end);

class Dinic {
public:
    static int start, end;
    static vector<int> net, ptr;
    static vector<vector<int> > c, f;

    static bool bfs();
    static ll dfs(int u, ll flow);
    static ll dinic();
};

class Khun {
public:
  static int color;
  static vector<int> used;
  static vector<int> mt;

  static bool try_khun(vector<int> *g, int u);
  static vector<int> khun(vector<int>* g, int n);
};

class Treap {
public:
    Treap(int key, int val): key(key), self(val), size(1),
        priority(rand()), min(val), left(nullptr), right(nullptr) {
    }

    static Treap* merge(Treap* left, Treap* right);
    static void split(Treap* treap, Treap*& left, Treap*& right, int index);

    static void push(Treap* treap);
    static void update(Treap* treap);
    static int sizeOf(Treap* treap);
    static int minOf(Treap *treap);
private:
    Treap left, right;

    int key, priority;
    int min, self, size;
};

int main(int argc, char **argv) {

    return 0;
}



ll binpow(int a, int n) {
    ll res = 1;
    for(; n; a *= a, n >>= 1)   if(n & 1)   res *= a;
    return res;
}

ll gcd(ll a, ll b) {
    while(b) {
        a %= b;
        swap(a, b);
    }
    return a;
}

bool* getPrimes(int n) {
    bool* primes = new bool[n + 1];
    memset(primes, 1, n + 1);
    primes[0] = primes[1] = false;
    for(int i = 2; i <= sqrt(n); ++i) {
        if(primes[i]) {
            for(ll j = i * i; j <= n; j += i)
                primes[j] = false;
        }
    }
    return primes;
}

int* Dijkstra(vector<pair<int, int> > *g, int n, int start) {
    int* dist = new int[n];
    for(int i = 0; i < n; ++i)  dist[i] = numeric_limits<int>::max() / 2;
    priority_queue<pair<int, int>, vector<pair<int, int> >, greater<pair<int, int> > > q;
    q.push(make_pair(start, 0));
    dist[start] = 0;
    while(!q.empty()) {
        int v = q.top().first, d = q.top().second;
        q.pop();
        if(dist[v] < d) continue;
        for(int i = 0; i < g[v].size(); ++i) {
            if(dist[g[v][i].first] > d + g[v][i].second) {
                dist[g[v][i].first] = d + g[v][i].second;
                q.push(make_pair(g[v][i].first, dist[g[v][i].first]));
            }
        }
    }
    return dist;
}

int* FordBellman(vector<pair<int, int> > *g, int n, int start) {
    int* dist = new int[n];
    for(int i = 0; i < n; ++i)  dist[i] = numeric_limits<int>::max() / 2;
    dist[start] = 0;
    for(;;) {
        bool f = true;
        for(int i = 0; i < n; ++i) {
            for(int j = 0; j < g[i].size(); ++j) {
                int to = g[i][j].first;
                if(dist[to] > dist[i] + g[i][j].second) {
                    f = false;
                    dist[to] = dist[i] + g[i][j].second;
                }
            }
        }
        if(f)   break;
    }

    return dist;
}

int** Floyd(int **g, int n) {
    int** dist = new int*[n];
    for(int i = 0; i < n; ++i) {
        dist[i] = new int[n];
        for(int j = 0; j < n; ++j)  dist[i][j] = g[i][j] != -1 ? g[i][j] : numeric_limits<int>::max() / 2;
    }

    for(int k = 0; k < n; ++k) {
        for(int i = 0; i < n; ++i) {
            for(int j = 0; j < n; ++j) {
                dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j]);
            }
        }
    }

    return dist;
}

void Kruskal(vector<edge> g, int n) {
    sort(g.begin(), g.end());
    DSU dsu(n);
    for(int i = 0; i < g.size(); ++i) {
        int from = g[i].a, to = g[i].b;
        int u = dsu.findSet(from), v = dsu.findSet(to);
        if(u != v) {
            dsu.unionSet(u, v);
        }
    }
}

DSU::DSU(size_t n): size(vector<int>(n, 1)) {
    parent.resize(n);
    for(int i = 0; i < n; parent[i] = i++);
}

void DSU::unionSet(int u, int v) {
    if((u = findSet(u) == (v = findSet(v))))
        return;

    if(size[u] < size[v])
        swap(u, v);

    parent[v] = u;
    size[u] += size[v];
}

int DSU::findSet(int u) {
    return parent[u] = (u == parent[u] ? u : findSet(parent[u]));
}

LCA::LCA(vector<int> *g, int n, int start): depth(vector<int>(n)),
        first(vector<int>(n, -1)), used(vector<bool>(n)) {
    dfs(start, g);
    tree.resize(list.size() * 4);
    buildTree(1, 0, list.size() - 1);
    for(int i = 0; i < list.size(); ++i) {
        int to = list[i];
        if(first[to] == -1) first[to] = i;
    }
}

void LCA::dfs(int u, vector<int>* g, int d) {
    depth[u] = d;
    list.push_back(u);
    used[u] = true;
    for(int i = 0; i < g[u].size(); ++i) {
        int to = g[u][i];
        if(!used[to]) {
            dfs(to, g, d + 1);
            list.push_back(u);
        }
    }
}

void LCA::buildTree(int u, int l, int r) {
    if(l == r)  tree[u] = list[l];
    else {
        int mid = l + r >> 1, u1 = u << 1, u2 = u1 + 1;
        buildTree(u1, l, mid);
        buildTree(u2, mid + 1, r);
        tree[u] = tree[depth[tree[u1]] < depth[tree[u2]] ? u1 : u2];
    }
}

int LCA::getLca(int u, int v) {
    return getMinLca(1, 0, list.size() - 1, min(first[u], first[v]), max(first[u], first[v]));
}

int LCA::getMinLca(int u, int tl, int tr, int l, int r) {
    if(tl > r || tr < l)    return -1;
    if(tl >= l && tr <= r)  return tree[u];
    else {
        int mid = tl + tr >> 1, u1 = u << 1, u2 = u1 + 1;
        int lca1 = getMinLca(u1, tl, mid, l, r);
        int lca2 = getMinLca(u2, mid + 1, tr, l, r);
        if(lca1 == -1)  return lca2;
        if(lca2 == -1)  return lca1;
        return (depth[lca1] < depth[lca2] ? lca1 : lca2);
    }
}

ll EdmondsKarp(int** c, int n, int start, int end) {
    vector<vector<int> > f(vector<int>(n));
    for(;;) {
        queue<int> q;
        vector<int> from(n, -1);
        q.push(start);
        while(!q.empty()) {
            int v = q.front(); q.pop();
            for(int i = 0; i < n; ++i) {
                if(from[i] == -1 && f[v][i] < c[v][i]) {
                    q.push(i);
                    from[i] = v;
                }
            }
        }
        if(from[end] == -1) break;

        int cf = numeric_limits<int>::max();
        for(int cur = end; cur != start;) {
            int prev = from[cur];
            cf = min(cf, c[prev][cur] - f[prev][cur]);
            cur = prev;
        }
        for(int cur = end; cur != start;) {
            int prev = from[cur];
            f[prev][cur] += cf;
            f[cur][prev] -= cf;
            cur = prev;
        }
    }

    ll flow = 0;
    for(int i = 0; i < n; ++i)
        if(c[0][i])
            flow += f[0][i];

    return flow;
}

bool Dinic::bfs() {
    queue<int> q;
    q.push(start);
    fill(net, -1);
    while(!q.empty()) {
        int v = q.front(); q.pop();
        for(int to = 0; to < c.size(); ++to) {
            if(net[to] == -1 && f[v][to] < c[v][to]) {
                q.push(to);
                net[to] = net[v] + 1;
            }
        }
    }
    return net[end] != -1;
}

ll Dinic::dfs(int u, ll flow) {
    if(flow == 0 || u == end)   return flow;
    for(int& to = ptr[u]; to < c.size(); ++to) {
        if(net[to] != net[u] + 1)   continue;
        ll pushed = dfs(to, min(flow, c[u][to] - f[u][to]));
        if(pushed) {
            f[u][to] += pushed;
            f[to][u] -= pushed;
            return pushed;
        }
    }
    return 0;
}

ll Dinic::dinic() {
    ll flow = 0;
    for(;bfs();) {
        fill(ptr, 0);
        while(ll pushed = dfs(start, numeric_limits<int>::max()))
            flow += pushed;
    }
    return flow;
}

vector<int> Khun::khun(vector<int> *g, int n) {
    mt.resize(n);
    used.resize(n);

    for(int i = 0; i < n; ++i) {
        ++color;
        try_khun(g, i);
    }
}

bool Khun::try_khun(vector<int>* g, int u) {
    if(used[u] == color)    return false;
    used[u] = true;
    for(int i = 0; i < g[u].size(); ++i) {
        if(mt[g[u][i]] == -1 || try_khun(g, g[u][i])) {
            mt[g[u][i]] = u;
            return true;
        }
    }
    return false;
}

Treap* Treap::merge(Treap *left, Treap *right) {
    if(left == nullptr) return right;
    if(right == nullptr)    return left;

    push(left, right);
    Treap* t;
    if(left->priority < right->priority) {
        t = merge(left, right->left);
        right->left = t;
        t = right;
    } else {
        t = merge(left->right, right);
        left->right = t;
        t = left;
    }
    update(t);
    return t;
}

void Treap::split(Treap* treap, Treap*& left, Treap*& right, int index) {
    if(treap == nullptr) {
        left = right = nullptr;
        return;
    }

    push(treap);
    if(sizeOf(treap->left) < index) {
        split(treap->right, treap->right, right, sizeOf(treap->left) - index - 1);
        left = treap;
    } else {
        split(treap->left, left, treap->left, index);
        right = treap;
    }
    update(left);
    update(right);
}

void Treap::push(Treap *treap) {
    //>!
}

void Treap::update(Treap *treap) {
    if(treap == nullptr)    return;
    treap->size = sizeOf(treap->left) + sizeOf(treap->right) + 1;
    treap->min = min(treap->self, min(minOf(treap->left), minOf(treap->right)));
}

int Treap::minOf(Treap* treap) {
    if(treap == nullptr)    return numeric_limits<int>::max();
    return treap->min;
}

int Treap::sizeOf(Treap *treap) {
    if(treap == nullptr)    return 0;
    return treap->size;
}
