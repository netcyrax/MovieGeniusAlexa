package lambda;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;
import core.Config;

import java.util.HashSet;
import java.util.Set;

public class MovieGenieSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds.add(Config.appId());
    }

    public MovieGenieSpeechletRequestStreamHandler() {
        super(new MovieGenieSpeechlet(), supportedApplicationIds);
    }
}
