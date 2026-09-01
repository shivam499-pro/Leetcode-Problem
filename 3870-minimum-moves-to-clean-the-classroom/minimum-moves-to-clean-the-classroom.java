import java.util.*;

class Solution {
    public int minMoves(String[] classroom, int energy) {
        int m = classroom.length;
        int n = classroom[0].length();

        int sr = 0, sc = 0;
        List<int[]> litter = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char c = classroom[i].charAt(j);

                if (c == 'S') {
                    sr = i;
                    sc = j;
                } else if (c == 'L') {
                    litter.add(new int[]{i, j});
                }
            }
        }

        int k = litter.size();

        if (k == 0) {
            return 0;
        }

        int allCollected = (1 << k) - 1;

        // litterMask[r][c] = bit corresponding to litter at this cell
        int[][] litterMask = new int[m][n];

        for (int i = 0; i < k; i++) {
            int r = litter.get(i)[0];
            int c = litter.get(i)[1];
            litterMask[r][c] = 1 << i;
        }

        /*
         * State:
         * (row, col, remainingEnergy, collectedMask)
         */
        boolean[][][][] visited =
                new boolean[m][n][energy + 1][1 << k];

        Queue<State> queue = new ArrayDeque<>();

        queue.offer(new State(sr, sc, energy, 0));
        visited[sr][sc][energy][0] = true;

        int[][] dirs = {
            {1, 0},
            {-1, 0},
            {0, 1},
            {0, -1}
        };

        int moves = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();

            while (size-- > 0) {
                State cur = queue.poll();

                int r = cur.r;
                int c = cur.c;
                int e = cur.energy;
                int mask = cur.mask;

                if (mask == allCollected) {
                    return moves;
                }

                for (int[] dir : dirs) {
                    int nr = r + dir[0];
                    int nc = c + dir[1];

                    if (nr < 0 || nr >= m || nc < 0 || nc >= n) {
                        continue;
                    }

                    if (classroom[nr].charAt(nc) == 'X') {
                        continue;
                    }

                    // Cannot make a move with zero energy.
                    if (e == 0) {
                        continue;
                    }

                    int newEnergy = e - 1;

                    // Collect litter if present.
                    int newMask = mask | litterMask[nr][nc];

                    // Reset energy on R.
                    if (classroom[nr].charAt(nc) == 'R') {
                        newEnergy = energy;
                    }

                    if (!visited[nr][nc][newEnergy][newMask]) {
                        visited[nr][nc][newEnergy][newMask] = true;
                        queue.offer(
                            new State(nr, nc, newEnergy, newMask)
                        );
                    }
                }
            }

            moves++;
        }

        return -1;
    }

    static class State {
        int r;
        int c;
        int energy;
        int mask;

        State(int r, int c, int energy, int mask) {
            this.r = r;
            this.c = c;
            this.energy = energy;
            this.mask = mask;
        }
    }
}