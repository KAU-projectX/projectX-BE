package com.projectX.projectX.domain.cafe.util;

import com.projectX.projectX.domain.cafe.exception.CannotParseException;
import com.projectX.projectX.domain.cafe.exception.InvalidProtocolException;
import com.projectX.projectX.domain.cafe.exception.UnsupportedEncodingTypeException;
import com.projectX.projectX.domain.tour.exception.InvalidRequestException;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Slf4j
public class HttpUtil {

    public static JSONArray connectHttp(String uri, String[] jsonForm, String headerName,
        String headerType) {
        StringBuilder result = new StringBuilder();
        JSONParser jsonParser = new JSONParser();
        try {
            URL requestUrl = new URL(uri);
            HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.setRequestProperty(headerName, headerType);

            int responseCode = urlConnection.getResponseCode();
            log.info("Response Code: " + responseCode);

            BufferedReader br = new BufferedReader(
                new InputStreamReader(requestUrl.openStream(), "UTF-8"));
            result.append(br.readLine());

            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            for (int i = 0; i < jsonForm.length - 1; ++i) {
                jsonObject = (JSONObject) jsonObject.get(jsonForm[i]);
            }
            return (JSONArray) jsonObject.get(jsonForm[jsonForm.length - 1]);

        } catch (MalformedURLException e) {
            throw new InvalidURIException(ErrorCode.INVALID_URI_EXCEPTION);
        } catch (ProtocolException e) {
            throw new InvalidProtocolException(ErrorCode.INVALID_PROTOCOL_EXCEPTION);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedEncodingTypeException(ErrorCode.UNSUPPORTED_ENCODING_EXCEPTION);
        } catch (ParseException e) {
            throw new CannotParseException(ErrorCode.CANNOT_PARSE_JSON_EXCEPTION);
        } catch (Exception e) {
            throw new InvalidRequestException(ErrorCode.INVALID_REQUEST_EXCEPTION);
        }
    }

}
