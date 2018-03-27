package ar.edu.itba.ss.domain.printers;

import ar.edu.itba.ss.domain.Particle;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Printer {

    private int c = 0;
    public void print(List<Particle> particles) {
        //System.out.println(particles.size());
        try {
            Files.write(Paths.get("output.xyz"), ((particles.size()+2)+"\n").getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get("output.xyz"), (++c+"\n").getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get("output.xyz"), ("0 0 0 0 0.1 0.001\n" +
                    "0.5 0.5 0 0 0.1 0.001\n").getBytes(), StandardOpenOption.APPEND);
            for (Particle p:particles) {
                Files.write(Paths.get("output.xyz"), (p+"\n").getBytes(), StandardOpenOption.APPEND);
            }
        } catch (Exception e){
            System.out.println("No escribo nada :D");

        }



    }

    public Printer() {
        try {
            Files.write(Paths.get("output.xyz"), "".getBytes());
        } catch (Exception e) {
            System.out.println("No escribo nada :D");
        }
    }
}
