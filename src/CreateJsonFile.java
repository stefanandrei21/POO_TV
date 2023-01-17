import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.simple.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CreateJsonFile {
    private final List<Output> listToPrint;
    public CreateJsonFile(final List<Output> listToPrint) {
        this.listToPrint = listToPrint;
    }

    /**
     * metoda pentru creearea fisierului json
     * @throws IOException
     */
    public void work() throws IOException {
        List<Map<String, Object>> arrayToPrint = new ArrayList<Map<String, Object>>();
        JSONArray jsonArray = new JSONArray();
        for (Output out : listToPrint) {
            Map<String, Object> myObject = new LinkedHashMap<String, Object>();
            myObject.put("error", out.getError());
            myObject.put("currentMoviesList", out.getCurrentMovieList());
            JSONObject json = new JSONObject();
            json.put("error", out.getError());
            json.put("currentMoviesList", out.getCurrentMovieList());
            JSONObject newJson = new JSONObject();
            Map<String, Object> newObj = new LinkedHashMap<>();

            if (out.getCurrentUser() != null) {
                Map<String, Object> myObj3 = new LinkedHashMap<>();
                myObj3.put("name", out.getCurrentUser().getName());
                myObj3.put("password", out.getCurrentUser().getPassword());
                myObj3.put("accountType", out.getCurrentUser().getAccountType());
                myObj3.put("country", out.getCurrentUser().getCountry());
                if (out.getCurrentUser().getBalance() != null) {
                    myObj3.put("balance", out.getCurrentUser().getBalance().toString());
                }
                newObj.put("credentials", myObj3);
                newObj.put("tokensCount", out.getCurrentUser().getTokensCount());
                newObj.put("numFreePremiumMovies", out.getCurrentUser().getNumFreePremiumMovies());
                newObj.put("purchasedMovies", out.getCurrentUser().getPurchasedMovies());
                newObj.put("watchedMovies", out.getCurrentUser().getWatchedMovies());
                newObj.put("likedMovies", out.getCurrentUser().getLikedMovies());
                newObj.put("ratedMovies", out.getCurrentUser().getRatedMovies());
                newObj.put("notifications", out.getCurrentUser().getNotifications());
                json.put("currentUser", newJson);
                myObject.put("currentUser", newObj);

            } else {
                json.put("currentUser", out.getCurrentUser());
                myObject.put("currentUser", out.getCurrentUser());
            }
            arrayToPrint.add(myObject);
            jsonArray.add(myObject);
        }

        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.enable(SerializationFeature.INDENT_OUTPUT);
        File jsonFile = new File("results.out");
        mapper2.writeValue(jsonFile, jsonArray);
    }
    @Override
    public String toString() {
        return "CreateJsonFile{"
                + "listToPrint=" + listToPrint
                + '}';
    }
}
