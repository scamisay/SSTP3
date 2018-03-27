package ar.edu.itba.ss;

import ar.edu.itba.ss.domain.CollisionSystem;
import ar.edu.itba.ss.domain.printers.Printer;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.Scanner;

public class App {


    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        System.out.println("N: ");
        int n = sc.nextInt();
        System.out.println("DT2: ");
        double dt2 = sc.nextDouble();
        CollisionSystem pc = CollisionSystem.getInstance();
        pc.init(300,n,dt2,new RandomDataGenerator(new JDKRandomGenerator(1000)));
        pc.calculate(new Printer());



    }



}
