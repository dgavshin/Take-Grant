package Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomName {

    public static String get() {
        BufferedReader      in;
        StringBuilder       response;
        String              inputLine;
        String              result;
        HttpURLConnection   connection;

        result = null;
        response = new StringBuilder("default");
        try {
            connection = (HttpURLConnection) new URL("https://genword.ru/generators/nicknames/").openConnection();
            connection.setRequestMethod("GET");
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }
        catch (IOException ignored) {}
        Pattern pattern = Pattern.compile("<span class=\"result\">(.+?)</span>");
        Matcher matcher = pattern.matcher(response.toString());
        if (matcher.find())
            result = response.substring(matcher.start(1), matcher.end(1));
        return (result);
    }
}
