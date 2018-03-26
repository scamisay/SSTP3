package ar.edu.itba.ss;

import ar.edu.itba.ss.domain.DinamicaMolecularDeEsferasRigidas.ParticleContainer;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.Scanner;

public class App {


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("N: ");
        int n = sc.nextInt();
        ParticleContainer pc = ParticleContainer.getInstance();
        pc.init(30,100,new RandomDataGenerator(new JDKRandomGenerator(1000)));
        //pc.calculate();



    }



}
