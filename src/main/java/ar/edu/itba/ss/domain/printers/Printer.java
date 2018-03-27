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
            Files.write(Paths.get("output.xyz"), (particles.size()+"\n").getBytes(), StandardOpenOption.APPEND);
            Files.write(Paths.get("output.xyz"), (++c+"\n").getBytes(), StandardOpenOption.APPEND);
            for (Particle p:particles) {
                Files.write(Paths.get("output.xyz"), (p+"\n").getBytes(), StandardOpenOption.APPEND);
            }
        } catch (Exception e){
            System.out.println("Puto");

        }



    }
}
