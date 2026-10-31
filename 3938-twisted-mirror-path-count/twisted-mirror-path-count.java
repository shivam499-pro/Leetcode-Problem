
class Solution {
    public int uniquePaths(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int MOD = 1_000_000_007;

        long[][] fromTop = new long[m][n];
        long[][] fromLeft = new long[m][n];

        // Start at (0, 0)
        long answer = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                long ways;

                if (i == 0 && j == 0) {
                    ways = 1;
                } else {
                    ways = (fromTop[i][j] + fromLeft[i][j]) % MOD;
                }

                // Destination reached
                if (i == m - 1 && j == n - 1) {
                    answer = ways;
                    continue;
                }

                if (grid[i][j] == 1) {
                    // Mirror:
                    // From top (moving down) -> turn right
                    if (i >= 0 && j + 1 < n) {
                        fromLeft[i][j + 1] =
                            (fromLeft[i][j + 1] + fromTop[i][j]) % MOD;
                    }

                    // From left (moving right) -> turn down
                    if (i + 1 < m) {
                        fromTop[i + 1][j] =
                            (fromTop[i + 1][j] + fromLeft[i][j]) % MOD;
                    }

                } else {
                    // Empty cell: choose right or down
                    if (j + 1 < n) {
                        fromLeft[i][j + 1] =
                            (fromLeft[i][j + 1] + ways) % MOD;
                    }

                    if (i + 1 < m) {
                        fromTop[i + 1][j] =
                            (fromTop[i + 1][j] + ways) % MOD;
                    }
                }
            }
        }

        return (int) answer;
    }
}
