class Solution {
    public boolean checkDivisibility(int n) {
        int real = n;
        int sum = 0;
        int pro = 1;
        while(n>0){
            int rem = n%10;
            sum += rem;
            pro *= rem;
            n /= 10;
        }
        return real % (sum + pro) == 0;
    }
}