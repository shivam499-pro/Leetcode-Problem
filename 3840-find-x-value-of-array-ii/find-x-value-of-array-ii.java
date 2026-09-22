class Solution {

    static class Node {
        int product;
        int[] count;

        Node(int k) {
            product = 1; // identity for multiplication
            count = new int[k];
        }
    }

    int n;
    int k;
    int[] treeProduct;
    int[] treeCount;

    public int[] resultArray(int[] nums, int k, int[][] queries) {

        this.n = nums.length;
        this.k = k;

        treeProduct = new int[4 * n];
        treeCount = new int[4 * n * k];

        build(1, 0, n - 1, nums);

        int[] answer = new int[queries.length];

        for (int q = 0; q < queries.length; q++) {

            int index = queries[q][0];
            int value = queries[q][1];
            int start = queries[q][2];
            int x = queries[q][3];

            // Persistent update
            update(1, 0, n - 1, index, value);

            // Query nums[start ... n-1]
            Node res = query(1, 0, n - 1, start, n - 1);

            answer[q] = res.count[x];
        }

        return answer;
    }
    void build(int node, int left, int right, int[] nums) {

        if (left == right) {

            int value = nums[left] % k;

            treeProduct[node] = value;

            treeCount[node * k + value] = 1;

            return;
        }

        int mid = (left + right) / 2;

        build(node * 2, left, mid, nums);
        build(node * 2 + 1, mid + 1, right, nums);

        merge(node);
    }
    void update(int node, int left, int right, int index, int value) {

        if (left == right) {

            int rem = value % k;

            treeProduct[node] = rem;

            // Clear old counts
            for (int r = 0; r < k; r++) {
                treeCount[node * k + r] = 0;
            }

            treeCount[node * k + rem] = 1;

            return;
        }

        int mid = (left + right) / 2;

        if (index <= mid) {
            update(node * 2, left, mid, index, value);
        } else {
            update(node * 2 + 1, mid + 1, right, index, value);
        }

        merge(node);
    }
    void merge(int node) {

        int leftNode = node * 2;
        int rightNode = node * 2 + 1;

        int leftProduct = treeProduct[leftNode];

        int rightProduct = treeProduct[rightNode];

        treeProduct[node] = (leftProduct * rightProduct) % k;

        for (int r = 0; r < k; r++) {
            treeCount[node * k + r] = 0;
        }
        for (int r = 0; r < k; r++) {

            treeCount[node * k + r] +=
                    treeCount[leftNode * k + r];
        }

        for (int r = 0; r < k; r++) {

            int rightCount =
                    treeCount[rightNode * k + r];

            int newRemainder =
                    (leftProduct * r) % k;

            treeCount[node * k + newRemainder] += rightCount;
        }
    }


    Node query(
            int node,
            int left,
            int right,
            int queryLeft,
            int queryRight) {

        if (queryRight < left || right < queryLeft) {
            return new Node(k);
        }

        if (queryLeft <= left && right <= queryRight) {

            Node result = new Node(k);

            result.product = treeProduct[node];

            for (int r = 0; r < k; r++) {

                result.count[r] =
                        treeCount[node * k + r];
            }

            return result;
        }

        int mid = (left + right) / 2;

        Node leftResult =
                query(node * 2, left, mid, queryLeft, queryRight);

        Node rightResult =
                query(node * 2 + 1, mid + 1, right,
                      queryLeft, queryRight);

        return mergeNodes(leftResult, rightResult);
    }

    Node mergeNodes(Node A, Node B) {

        Node result = new Node(k);

        result.product =
                (A.product * B.product) % k;

        for (int r = 0; r < k; r++) {

            result.count[r] += A.count[r];
        }


        for (int r = 0; r < k; r++) {

            int newRemainder =
                    (A.product * r) % k;

            result.count[newRemainder] += B.count[r];
        }

        return result;
    }
}