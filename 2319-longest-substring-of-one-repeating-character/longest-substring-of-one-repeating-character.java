class Solution {

    static class Node {
        int len;
        int prefix;
        int suffix;
        int best;

        Node(int len, int prefix, int suffix, int best) {
            this.len = len;
            this.prefix = prefix;
            this.suffix = suffix;
            this.best = best;
        }
    }

    char[] str;
    Node[] tree;

    public int[] longestRepeating(
            String s,
            String queryCharacters,
            int[] queryIndices) {

        str = s.toCharArray();

        int n = str.length;
        tree = new Node[4 * n];

        build(1, 0, n - 1);

        int[] answer = new int[queryIndices.length];

        for (int i = 0; i < queryIndices.length; i++) {

            int index = queryIndices[i];
            char ch = queryCharacters.charAt(i);

            // Update the actual character
            str[index] = ch;

            // Update segment tree
            update(1, 0, n - 1, index);

            // Root contains the answer for the entire string
            answer[i] = tree[1].best;
        }

        return answer;
    }

    private void build(int node, int left, int right) {

        if (left == right) {
            tree[node] = new Node(1, 1, 1, 1);
            return;
        }

        int mid = left + (right - left) / 2;

        build(node * 2, left, mid);
        build(node * 2 + 1, mid + 1, right);

        tree[node] = merge(
                tree[node * 2],
                tree[node * 2 + 1],
                mid
        );
    }

    private void update(int node, int left, int right, int index) {

        if (left == right) {
            tree[node] = new Node(1, 1, 1, 1);
            return;
        }

        int mid = left + (right - left) / 2;

        if (index <= mid) {
            update(node * 2, left, mid, index);
        } else {
            update(node * 2 + 1, mid + 1, right, index);
        }

        tree[node] = merge(
                tree[node * 2],
                tree[node * 2 + 1],
                mid
        );
    }

    private Node merge(Node left, Node right, int mid) {

        int len = left.len + right.len;

        int prefix = left.prefix;
        int suffix = right.suffix;

        int best = Math.max(left.best, right.best);

        // Check characters at the boundary
        if (str[mid] == str[mid + 1]) {

            // Entire left segment has same character
            if (left.prefix == left.len) {
                prefix = left.len + right.prefix;
            }

            // Entire right segment has same character
            if (right.suffix == right.len) {
                suffix = right.len + left.suffix;
            }

            // Combine suffix of left + prefix of right
            best = Math.max(
                    best,
                    left.suffix + right.prefix
            );
        }

        return new Node(len, prefix, suffix, best);
    }
}