package it.unimi.dsi.util;

public class XoRoShiRo128PlusPlusRandom {
    private long state0, state1;

    public XoRoShiRo128PlusPlusRandom() {
        this(System.nanoTime());
    }

    public XoRoShiRo128PlusPlusRandom(long seed) {
        setSeed(seed);
    }

    public void setSeed(long seed) {
        state0 = seed;
        state1 = splitMix64(seed + 0x9E3779B97F4A7C15L);
    }

    private long splitMix64(long z) {
        z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
        return z ^ (z >>> 31);
    }

    public long nextLong() {
        long s1 = state0;
        long s0 = state1;
        state0 = s0;
        s1 ^= s1 << 23;
        state1 = (s1 ^ s0 ^ (s1 >> 17) ^ (s0 >> 26));
        return state1 + s0;
    }
}
