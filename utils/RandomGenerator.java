package utils;

import java.util.Random;
public class RandomGenerator { private static Random rnd = new Random(); public static void init(long seed)
{ rnd = new Random(seed); } public static Random rnd(){ return rnd; } }