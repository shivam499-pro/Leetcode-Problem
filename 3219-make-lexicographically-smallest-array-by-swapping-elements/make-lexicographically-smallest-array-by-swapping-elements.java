

class Solution {
    public int[] lexicographicallySmallestArray(int[] nums, int limit) {
        int n = nums.length;

        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, (a, b) -> Integer.compare(nums[a], nums[b]));

        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        for (int i = 1; i < n; i++) {
            if ((long) nums[indices[i]] - nums[indices[i - 1]] <= limit) {
                union(parent, indices[i], indices[i - 1]);
            }
        }

        Map<Integer, PriorityQueue<Integer>> positions = new HashMap<>();
        Map<Integer, PriorityQueue<Integer>> values = new HashMap<>();

        for (int i = 0; i < n; i++) {
            int root = find(parent, i);

            positions.computeIfAbsent(root, k -> new PriorityQueue<>()).offer(i);
            values.computeIfAbsent(root, k -> new PriorityQueue<>()).offer(nums[i]);
        }

        int[] result = new int[n];

        for (int i = 0; i < n; i++) {
            int root = find(parent, i);
            result[positions.get(root).poll()] = values.get(root).poll();
        }

        return result;
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }

    private void union(int[] parent, int a, int b) {
        a = find(parent, a);
        b = find(parent, b);

        if (a != b) {
            parent[b] = a;
        }
    }
}