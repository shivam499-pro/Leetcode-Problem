class Solution {
    public boolean stoneGameIX(int[] stones) {

        int[] count = new int[3];

        for (int i = 0; i < stones.length; i++) {
            count[stones[i] % 3]++;
        }

        int zero = count[0];
        int one = count[1];
        int two = count[2];

        if (zero % 2 == 0) {
            return one > 0 && two > 0;
        }

        return Math.abs(one - two) > 2;
    }
}