
import java.util.*;

class Solution {
    public int shortestPath(int n, int[][] edges, String labels, int k) {

        List<int[]>[] graph = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            graph[e[0]].add(new int[]{e[1], e[2]});
        }

        int states = k + 1;

        long[] dist = new long[n * states];
        Arrays.fill(dist, Long.MAX_VALUE);

        PriorityQueue<long[]> pq = new PriorityQueue<>(
            (a, b) -> Long.compare(a[0], b[0])
        );

        int start = 1;

        dist[start] = 0;
        pq.offer(new long[]{0, start});

        while (!pq.isEmpty()) {

            long[] curr = pq.poll();

            long cost = curr[0];
            int state = (int) curr[1];

            if (cost != dist[state]) {
                continue;
            }

            int u = state / states;
            int count = state % states;

            if (u == n - 1) {
                return (int) cost;
            }

            for (int[] edge : graph[u]) {

                int v = edge[0];
                int weight = edge[1];

                int newCount;

                if (labels.charAt(u) == labels.charAt(v)) {
                    newCount = count + 1;
                } else {
                    newCount = 1;
                }

                if (newCount > k) {
                    continue;
                }

                int nextState = v * states + newCount;

                long newCost = cost + weight;

                if (newCost < dist[nextState]) {

                    dist[nextState] = newCost;

                    pq.offer(new long[]{
                        newCost, nextState
                    });
                }
            }
        }

        return -1;
    }
}
