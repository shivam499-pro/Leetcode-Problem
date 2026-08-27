class Solution {
    public String lexGreaterPermutation(String s, String target) {
        int n = s.length();
        int[] freq = new int[26];

        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }

        char[] ans = new char[n];

        // Try to keep prefix equal to target
        for (int i = 0; i < n; i++) {
            int t = target.charAt(i) - 'a';

            // First, try using target[i] to keep prefix equal.
            if (freq[t] > 0) {
                ans[i] = target.charAt(i);
                freq[t]--;
                continue;
            }

            // Cannot keep equality.
            // Find the smallest character greater than target[i].
            for (int c = t + 1; c < 26; c++) {
                if (freq[c] > 0) {
                    ans[i] = (char) ('a' + c);
                    freq[c]--;

                    // Fill remaining positions with smallest characters.
                    int pos = i + 1;

                    for (int x = 0; x < 26; x++) {
                        while (freq[x] > 0) {
                            ans[pos++] = (char) ('a' + x);
                            freq[x]--;
                        }
                    }

                    return new String(ans);
                }
            }

            // No character >= target[i] can make the current prefix work.
            // We need to backtrack and change an earlier position.
            break;
        }

        /*
         * The equal-prefix attempt failed.
         * Backtrack from right to left and increase the first
         * possible position.
         */
        freq = new int[26];
        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }

        for (int i = n - 1; i >= 0; i--) {

            // Characters before i must match target.
            boolean possible = true;

            for (int j = 0; j < i; j++) {
                int c = target.charAt(j) - 'a';

                if (freq[c] == 0) {
                    possible = false;
                    break;
                }

                freq[c]--;
            }

            if (!possible) {
                // Restore freq
                freq = new int[26];
                for (char c : s.toCharArray()) {
                    freq[c - 'a']++;
                }
                continue;
            }

            int t = target.charAt(i) - 'a';

            // Find smallest character > target[i]
            for (int c = t + 1; c < 26; c++) {
                if (freq[c] > 0) {
                    ans = target.toCharArray();
                    ans[i] = (char) ('a' + c);
                    freq[c]--;

                    int pos = i + 1;

                    for (int x = 0; x < 26; x++) {
                        while (freq[x] > 0) {
                            ans[pos++] = (char) ('a' + x);
                            freq[x]--;
                        }
                    }

                    return new String(ans);
                }
            }

            // Restore for next iteration
            freq = new int[26];
            for (char c : s.toCharArray()) {
                freq[c - 'a']++;
            }
        }

        return "";
    }
}