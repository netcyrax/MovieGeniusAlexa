import core.MovieGenieAlexaCore;
import lambda.MovieGenieSpeechlet;

/**
 * Luncher to verify that core is working.
 */
public class Launcher {
    public Launcher() {
    }

    public static void main(String[] args){
        MovieGenieSpeechlet speechlet = new MovieGenieSpeechlet();
        System.out.println("Compiled fine.");
        System.out.println("");

        MovieGenieAlexaCore core = MovieGenieAlexaCore.getInstance();
        System.out.println(core.queryMovies("Comedy").toString());
    }
}
