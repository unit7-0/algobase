package Algorithm;

import java.util.Arrays;

import static java.lang.Math.sqrt;

/**
 * Created with IntelliJ IDEA.
 * File: Math.java
 * User: unit7
 * Date: 06.08.12
 * Time: 14:36
 */

public class Math {
    public static long binPow(long a, long n) {
        long ans = 1;
        for(; n > 0; n >>= 1, a *= a)   if((n & 1) > 0) ans *= a;
        return ans;
    }

    public static long gcd(long a, long b) {
        while(b > 0) {
            a %= b;
            a ^= b;
            b ^= a;
            a ^= b;
        }
        return a;
    }

    public static boolean[] getPrimes(int n) {
        boolean[] primes = new boolean[n + 1];
        Arrays.fill(primes, true);
        primes[0] = primes[1] = false;
        for(int i = 2; i <= sqrt(n); ++i) {
            if(primes[i]) {
                for(int j = i * i; j <= n; j += i)  primes[j] = false;
            }
        }
        return primes;
    }

    public static int[] getFibonacci(int n) {
        int[] fibo = new int[n];
        int a = 1, b = 1, c;
        for(int i = 0; i < n; ++i) {
            fibo[i] = a;
            c = a + b;
            a = b;
            b = c;
        }
        return fibo;
    }

    public static int getNthFibo(int n) {
        int a = 1, b = 1, c;
        for(int i = 0; i < n; ++i) {
            c = a + b;
            a = b;
            b = c;
        }
        return a;
    }

    public static Pair<Integer, Integer> gcdExtended(int a, int b) {
        if(a == b)  return new Pair<Integer, Integer>(0, 1);
        Pair<Integer, Integer> xy = gcdExtended(b % a, a);
        int x1 = xy.getFirst();
        xy.setFirst(xy.getSecond() - b / a * xy.getFirst());
        xy.setSecond(x1);
        return xy;
    }
}
