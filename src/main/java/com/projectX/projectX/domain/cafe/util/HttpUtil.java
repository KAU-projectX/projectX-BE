package com.projectX.projectX.domain.cafe.util;

import com.projectX.projectX.domain.cafe.exception.CannotParseException;
import com.projectX.projectX.domain.cafe.exception.InvalidProtocolException;
import com.projectX.projectX.domain.cafe.exception.UnsupportedEncodingTypeException;
import com.projectX.projectX.domain.tour.exception.InvalidURIException;
import com.projectX.projectX.global.exception.ErrorCode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Slf4j
public class HttpUtil {

    public static JSONObject connectHttp(String uri, String headerName, String headerContent) {
        StringBuilder result = new StringBuilder();
        JSONParser jsonParser = new JSONParser();
        try {
            URL requestUrl = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestProperty(headerName, headerContent);

            BufferedReader br = new BufferedReader(
                new InputStreamReader(urlConnection.getInputStream()));
            result.append(br.readLine());

            int responseCode = urlConnection.getResponseCode();
            log.info("Response Code: " + responseCode);

            return (JSONObject) jsonParser.parse(result.toString());
        } catch (MalformedURLException e) {
            throw new InvalidURIException(ErrorCode.INVALID_URI_EXCEPTION);
        } catch (ProtocolException e) {
            throw new InvalidProtocolException(ErrorCode.INVALID_PROTOCOL_EXCEPTION);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingTypeException(ErrorCode.UNSUPPORTED_ENCODING_EXCEPTION);
        } catch (ParseException e) {
            throw new CannotParseException(ErrorCode.CANNOT_PARSE_JSON_EXCEPTION);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
