package lambda;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import core.MovieGenieAlexaCore;
import model.MovieSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MovieGenieSpeechlet implements Speechlet {

    private static final MovieGenieAlexaCore core = MovieGenieAlexaCore.getInstance();
    private static final Logger log = LoggerFactory.getLogger(MovieGenieSpeechlet.class);

    private static final String SLOT_GENRE = "genre";
    private static final String SLOT_ACTION = "action";

    private static final String INTENT_LAUNCH = "LaunchIntent";
    private static final String INTENT_FIRST_RESULT = "FirstResultIntent";
    private static final String INTENT_MOVIE_DETAIL = "MovieDetailIntent";

    private static final String ATTR_MOVIES = "AttrMovies";

    @Override
    public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getLaunchResponse();
    }

    @Override
    public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if (INTENT_LAUNCH.equals(intentName)) {
            return getLaunchResponse();
        }
        else if (INTENT_FIRST_RESULT.equals(intentName)) {
            return getFirstResultResponse(intent, session);
        }
        else if ("AMAZON.NextIntent".equals(intentName)) {
            return getNextResultResponse(session);
        }
        else if (INTENT_MOVIE_DETAIL.equals(intentName)) {
            return getMovieDetailResponse(intent, session);
        }
        else if ("AMAZON.CancelIntent".equals(intentName)) {
            return getGoodbyeResponse();
        }
        else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getLaunchResponse("You can ask the genius to find movies tailored to your taste. You can say things like 'Find me a Comedy'. Let's try it.");
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    @Override
    public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }

    private SpeechletResponse getLaunchResponse() {
        return getLaunchResponse("The genius is at your command. ");
    }

    private SpeechletResponse getLaunchResponse(String introText) {
        String speechText = introText+" Tell me what kind of movie do you want?";
        return askResponse(speechText);
    }

    private SpeechletResponse getFirstResultResponse(Intent intent, Session session) {

        // Validation
        Optional<String> optionalGenre = slotValue(intent, SLOT_GENRE);
        if (!optionalGenre.isPresent()) {
            return getLaunchResponse("Couldn't understand what type of movie you want. Let's try again. ");
        }

        String genre = optionalGenre.get();
        List<MovieSet> movies = core.queryMovies(genre);
        String speechText;
        if (movies.isEmpty()) {
            return getLaunchResponse("Couldn't find any movies. Let's try again.");
        }

        // Output
        speechText = "So you want a " + genre + "? ";
        MovieSet movie = movies.get(0);
        speechText += "What about " + movie.getTitle()+"?";
        setAttributeMovies(session, movies);

        return askResponse(speechText, cardTextFromMovie(movie));
    }

    private SpeechletResponse getNextResultResponse(Session session) {

        // Validation
        String speechText = "No other movies to suggest. Let's try a new search.";
        List<MovieSet> movies = core.toMovies(attribute(ATTR_MOVIES, session, String.class));
        if (movies.isEmpty() || movies.size() == 1) {
            removeAttributeMovies(session);
            return getLaunchResponse(speechText);
        }

        // Remove the top movie and set the current movie
        movies.remove(0);
        setAttributeMovies(session, movies);
        MovieSet movie = movies.get(0);
        speechText = "What do you think about "+movie.getTitle()+"?";

        return askResponse(speechText, cardTextFromMovie(movie));
    }

    private SpeechletResponse getMovieDetailResponse(Intent intent, Session session) {

        // Validation
        Optional<String> optionalAction = slotValue(intent, SLOT_ACTION);
        List<MovieSet> movies = core.toMovies(attribute(ATTR_MOVIES, session, String.class));
        if (!optionalAction.isPresent() || movies.isEmpty()) {
            return getLaunchResponse();
        }

        MovieSet movie = movies.get(0);
        String action = optionalAction.get();
        switch (action) {

            case "plot":
                return askResponse("Plot: "+movie.getPlot());

            case "rating":
                return askResponse("Rating: "+movie.getImdbRating() +" out of 10");

            case "year":
                return askResponse("Year: "+movie.getYear());

            default:
            case "title":
                return askResponse("Title: "+movie.getTitle());
        }
    }

    private SpeechletResponse getGoodbyeResponse() {

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText("The genius says goodbye!");

        SpeechletResponse response = SpeechletResponse.newTellResponse(speech);
        response.setShouldEndSession(true);
        return response;
    }

    private void setAttributeMovies(Session session, List<MovieSet> movies) {
        session.setAttribute(ATTR_MOVIES, core.toJson(movies));
    }

    private void removeAttributeMovies(Session session) {
        session.removeAttribute(ATTR_MOVIES);
    }

    private String cardTextFromMovie(MovieSet movie) {
        String result;
        result = movie.getTitle()+" ("+movie.getYear()+")" +"\n\n";
        result += movie.getImdbRating() + " / 10\n\n";
        result += movie.getGenre() + "\n\n";
        result += movie.getPlot() +"\n\n";
        return result;
    }

    private SpeechletResponse askResponse(String speechText) {
        return askResponse(speechText, Optional.empty());
    }

    private SpeechletResponse askResponse(String speechText, String cardText) {
        return askResponse(speechText, Optional.of(cardText));
    }
    private SpeechletResponse askResponse(String speechText, Optional<String> cardText) {

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        if (cardText.isPresent()) {
            // Create the Simple card content.
            SimpleCard card = new SimpleCard();
            card.setTitle("Movie Genius");
            card.setContent(cardText.get());
            return SpeechletResponse.newAskResponse(speech, commonReprompt(), card);
        }
        return SpeechletResponse.newAskResponse(speech, commonReprompt());
    }

    private Reprompt commonReprompt() {
        String repromptText = "Can you say that again?";
        Reprompt reprompt = new Reprompt();
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromptText);
        reprompt.setOutputSpeech(repromptSpeech);
        return reprompt;
    }

    private Optional<String> slotValue(Intent intent, String slotName) {
        if (intent == null) {
            return Optional.empty();
        }
        Map<String, Slot> slots = intent.getSlots();
        Slot slotGenre = slots.get(slotName);
        if (slotGenre == null || slotGenre.getValue() == null) {
            return Optional.empty();
        }
        return Optional.of(slotGenre.getValue());
    }

    private <T> Optional<T> attribute(String attrName, Session session, Class<T> type) {
        Object attr = session.getAttribute(attrName);
        if (attr == null) {
            return Optional.empty();
        }
        return Optional.of(type.cast(attr));
    }
}