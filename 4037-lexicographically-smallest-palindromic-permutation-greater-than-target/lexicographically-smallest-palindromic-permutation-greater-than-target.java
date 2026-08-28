class Solution {
    public String lexPalindromicPermutation(String s, String target) {
        int n = s.length();
        int[] freq = new int[26];

        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }

        int odd = 0;
        int middle = -1;

        for (int i = 0; i < 26; i++) {
            if (freq[i] % 2 != 0) {
                odd++;
                middle = i;
            }
        }

        if (odd > 1) {
            return "";
        }

        int[] halfFreq = new int[26];

        for (int i = 0; i < 26; i++) {
            halfFreq[i] = freq[i] / 2;
        }

        int halfLen = n / 2;
        String targetHalf = target.substring(0, halfLen);

        String half = buildHalf(halfFreq, targetHalf);

        if (half == null) {
            return "";
        }

        String ans = makePalindrome(half, middle);

        if (ans.compareTo(target) > 0) {
            return ans;
        }

        half = nextPermutation(half);

        if (half == null) {
            return "";
        }

        return makePalindrome(half, middle);
    }

    private String buildHalf(int[] freq, String target) {
        int n = target.length();
        int[] remaining = freq.clone();
        boolean exact = true;

        for (int i = 0; i < n; i++) {
            int c = target.charAt(i) - 'a';

            if (remaining[c] == 0) {
                exact = false;
                break;
            }

            remaining[c]--;
        }

        if (exact) {
            return target;
        }

        for (int pos = n - 1; pos >= 0; pos--) {
            remaining = freq.clone();
            boolean valid = true;

            for (int i = 0; i < pos; i++) {
                int c = target.charAt(i) - 'a';

                if (remaining[c] == 0) {
                    valid = false;
                    break;
                }

                remaining[c]--;
            }

            if (!valid) {
                continue;
            }

            int current = target.charAt(pos) - 'a';

            for (int c = current + 1; c < 26; c++) {
                if (remaining[c] > 0) {
                    StringBuilder result = new StringBuilder();
                    result.append(target, 0, pos);
                    result.append((char) ('a' + c));
                    remaining[c]--;

                    for (int x = 0; x < 26; x++) {
                        while (remaining[x] > 0) {
                            result.append((char) ('a' + x));
                            remaining[x]--;
                        }
                    }

                    return result.toString();
                }
            }
        }

        return null;
    }

    private String nextPermutation(String s) {
        char[] arr = s.toCharArray();
        int i = arr.length - 2;

        while (i >= 0 && arr[i] >= arr[i + 1]) {
            i--;
        }

        if (i < 0) {
            return null;
        }

        int j = arr.length - 1;

        while (arr[j] <= arr[i]) {
            j--;
        }

        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;

        reverse(arr, i + 1, arr.length - 1);

        return new String(arr);
    }

    private void reverse(char[] arr, int left, int right) {
        while (left < right) {
            char temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    private String makePalindrome(String half, int middle) {
        StringBuilder result = new StringBuilder();
        result.append(half);

        if (middle != -1) {
            result.append((char) ('a' + middle));
        }

        result.append(new StringBuilder(half).reverse());

        return result.toString();
    }
}