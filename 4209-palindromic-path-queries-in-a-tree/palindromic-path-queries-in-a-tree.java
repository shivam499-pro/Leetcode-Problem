
import java.util.*;

class Solution {
    int LOG;
    int[][] up;
    int[] depth, tin, tout, parent;
    int[] bit, mask;
    List<Integer>[] adj;
    int timer;

    public List<Boolean> palindromePath(
        int n, int[][] edges, String s, String[] queries
    ) {
        adj = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            adj[e[0]].add(e[1]);
            adj[e[1]].add(e[0]);
        }

        LOG = 1;
        while ((1 << LOG) <= n) {
            LOG++;
        }

        up = new int[LOG][n];
        depth = new int[n];
        tin = new int[n];
        tout = new int[n];
        parent = new int[n];
        mask = new int[n];

        int[] order = new int[n];
        int[] stack = new int[n];
        int[] size = new int[n];
        boolean[] visited = new boolean[n];

        int top = 0;
        int count = 0;
        timer = 0;

        stack[top++] = 0;
        visited[0] = true;
        parent[0] = 0;

        while (top > 0) {
            int u = stack[--top];

            tin[u] = ++timer;
            order[count++] = u;

            for (int v : adj[u]) {
                if (visited[v]) {
                    continue;
                }

                visited[v] = true;
                parent[v] = u;
                depth[v] = depth[u] + 1;
                stack[top++] = v;
            }
        }

        for (int i = 0; i < n; i++) {
            up[0][i] = parent[i];
        }

        for (int k = 1; k < LOG; k++) {
            for (int i = 0; i < n; i++) {
                up[k][i] = up[k - 1][up[k - 1][i]];
            }
        }

        Arrays.fill(size, 1);

        for (int i = n - 1; i > 0; i--) {
            int u = order[i];
            size[parent[u]] += size[u];
        }

        for (int i = 0; i < n; i++) {
            tout[i] = tin[i] + size[i] - 1;
        }

        bit = new int[n + 2];

        for (int i = 0; i < n; i++) {
            mask[i] = 1 << (s.charAt(i) - 'a');
            rangeXor(tin[i], tout[i], mask[i]);
        }

        List<Boolean> answer = new ArrayList<>();

        for (String q : queries) {
            String[] p = q.split(" ");

            int u = Integer.parseInt(p[1]);

            if (p[0].equals("update")) {
                int newMask = 1 << (p[2].charAt(0) - 'a');
                int delta = mask[u] ^ newMask;

                rangeXor(tin[u], tout[u], delta);
                mask[u] = newMask;

            } else {
                int v = Integer.parseInt(p[2]);
                int lca = getLCA(u, v);

                int pathMask = pointQuery(tin[u])
                             ^ pointQuery(tin[v])
                             ^ mask[lca];

                answer.add(
                    (pathMask & (pathMask - 1)) == 0
                );
            }
        }

        return answer;
    }

    void rangeXor(int l, int r, int value) {
        add(l, value);
        add(r + 1, value);
    }

    void add(int i, int value) {
        while (i < bit.length) {
            bit[i] ^= value;
            i += i & -i;
        }
    }

    int pointQuery(int i) {
        int result = 0;

        while (i > 0) {
            result ^= bit[i];
            i -= i & -i;
        }

        return result;
    }

    int getLCA(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }

        int diff = depth[u] - depth[v];

        for (int k = 0; k < LOG; k++) {
            if ((diff & (1 << k)) != 0) {
                u = up[k][u];
            }
        }

        if (u == v) {
            return u;
        }

        for (int k = LOG - 1; k >= 0; k--) {
            if (up[k][u] != up[k][v]) {
                u = up[k][u];
                v = up[k][v];
            }
        }

        return parent[u];
    }
}
