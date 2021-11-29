package com.company;

// tnx to https://www.geeksforgeeks.org/genetic-algorithms/
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class Main {
     final static String GENES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOP"+
            "QRSTUVWXYZ 1234567890, .-;:_!\"#%&/()=?@${[]}";
     final static Random rand = new Random();
     final static String TARGET = "I love GeeksforGeeks";

    static char mutated_genes() {
        int len = GENES.length();
        Random rand = Main.rand;
        int r = rand.nextInt(len-1);
        return GENES.charAt(r);
    }

    static String create_gnome() {
        int len = TARGET.length();
        String gnome = "";
        for(int i = 0;i<len;i++)
            gnome += mutated_genes();
        return gnome;
    }

    public static void main(String[] args) {
        int POPULATION_SIZE=100;
        // current generation
        int generation = 0;

        Vector<Individual> population = new Vector<>();
        boolean found = false;

        // create initial population
        for(int i = 0;i<POPULATION_SIZE;i++)
        {
            String gnome = create_gnome();
            population.add(new Individual(gnome));
           // System.out.println(population);
        }

        while(! found)
        {
            // sort the population in increasing order of fitness score
            //sort(population.begin(), population.end());
            Collections.sort(population);
            //System.out.println(population.elementAt(1).fitness);
            // if the individual having lowest fitness score ie.
            // 0 then we know that we have reached to the target
            // and break the loop
            if(population.elementAt(0).fitness <= 0)
            {
                found = true;
                break;
            }

            // Otherwise generate new offsprings for new generation
            Vector<Individual> new_generation = new Vector<>();

            // Perform Elitism, that mean 10% of fittest population
            // goes to the next generation
            int s = (10*POPULATION_SIZE)/100;
            for(int i = 0;i<s;i++)
                new_generation.add(population.elementAt(i));

            // From 50% of fittest population, Individuals
            // will mate to produce offspring
            s = (90*POPULATION_SIZE)/100;
            Random rand = Main.rand;
            for(int i = 0;i<s;i++)
            {
                int len = population.size();
                int r = rand.nextInt(50);
                Individual parent1 = population.elementAt(r);
                r = rand.nextInt(50);
                Individual parent2 = population.elementAt(r);
                Individual offspring = parent1.mate(parent2);
                new_generation.add(offspring);
               // System.out.println("off:  "+offspring);
            }
            population = new_generation;
            if (generation==2)
            System.out.println(new_generation);
             print("Generation: " + generation + "\t");
            print("String: " +population.elementAt(0).chromosome );
            print("Fitness: " +population.elementAt(0).fitness );
           // print("Fitness111: " +population.elementAt(1).fitness );

            generation++;
        }
        print("Generation: " + generation + "\t");
        print("String: " + population.elementAt(0).chromosome +"\t");
        print("Fitness: "+ population.elementAt(0).fitness + "\n");
    }
    public static void print(String str){
        System.out.println(str);
    }
}

class Individual implements Comparable<Individual>
{
    String chromosome;
    int fitness;

    Individual(String chromosome) {
    this.chromosome = chromosome;
    fitness = cal_fitness();
}

    Individual mate(Individual par2) {
    // chromosome for offspring
    String child_chromosome = "";

    int len = chromosome.length();
    for(int i = 0;i<len;i++)
    {
        // random probability
        //Random ran=Main.rand;
        double p = Math.random();
       // System.out.println(p);
        // if prob is less than 0.45, insert gene
        // from parent 1
        if(p < 0.45)
            child_chromosome += chromosome.charAt(i);

            // if prob is between 0.45 and 0.90, insert
            // gene from parent 2
        else if(p < 0.90)
            child_chromosome += par2.chromosome.charAt(i);

            // otherwise insert random gene(mutate),
            // for maintaining diversity
        else
            child_chromosome += Main.mutated_genes();
    }

    // create new Individual(offspring) using
    // generated chromosome for offspring
    return new Individual(child_chromosome);
}

    int cal_fitness() {
    //int len = TARGET.size();
    int fitness = 0;

    for(char c : chromosome.toCharArray())
    {
        fitness +=(int)(c);

    }
        //System.out.println(fitness);
    return fitness;
}

    @Override
    public String toString() {
        return "Individual{" +
                "chromosome='" + chromosome + '\'' +
                ", fitness=" + fitness +
                '}';
    }

    @Override
    public int compareTo(Individual individual) {
        return this.cal_fitness() - individual.cal_fitness();
    }
}
