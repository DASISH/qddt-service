package no.nsd.qddt;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Dag Østgulen Heradstveit
 * @author Stig Norland
 *
 * https://stackoverflow.com/questions/2143637/what-should-a-developer-know-before-building-an-api-for-a-community-based-website
 */
@SpringBootApplication
public class QDDT  {

    public static void main(String[] args) {

        SpringApplication.run(QDDT.class);
        System.out.println("QDDT ready");
    }

}
