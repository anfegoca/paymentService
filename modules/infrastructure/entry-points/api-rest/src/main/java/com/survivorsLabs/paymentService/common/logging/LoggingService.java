package com.survivorsLabs.muscleCoach.common.logging;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.survivorsLabs.muscleCoach.common.ConstantsHelper;
import com.survivorsLabs.muscleCoach.common.UtilsHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingService {

    public static final String QUOTES = "\"";
    public static final String REGEX_REPLACE_JSON_VALUE = "\".*?:.*?\".*?\"";
    public static final String TWO_DOTS = ":";
    public static final String ASTERISK = "*";
    private static final int MAX_CHARACTERS_TO_PRINT = 500;
    private static final int NAME_ALTERNATIVES_SIZE = 3;

    private final ObjectMapper mapper = new ObjectMapper();
    private final Environment environment;

    private final String[] hideFieldsNames = {
            "password",
            "otp",
            "Authorization",
            "AuthorizationApi",
            "user-token"
    };

    /**
     * Logging of requests
     *
     * @param request request
     * @param body    body
     * @throws JsonProcessingException error mapping json
     */
    public void logRequest(HttpServletRequest request, Object body) throws JsonProcessingException {
        UtilsHelper.assignCorrelative(environment.getProperty("component.name", ""));
        log.info(ConstantsHelper.EMPTY_STRING);
        log.info("STARTING TRANSACTION");

        var httpMethod = request.getMethod();
        var requestedUrl = request.getRequestURI();
        var queryParams = protectFields(mapper.writeValueAsString(request.getParameterMap()));
        var bodyProtected = protectFields(mapper.writeValueAsString(body), hideFieldsNames);

        var headers = new HashMap<String, String>();
        request.getHeaderNames().asIterator()
                .forEachRemaining(header -> headers.put(header, request.getHeader(header)));

        var headersProtected = protectFields(mapper.writeValueAsString(headers), hideFieldsNames);

        log.info("REQUEST: TYPE: [{}], URL: [{}], QUERY-PARAMS: [{}], HEADERS: [{}] BODY: [{}]",
                httpMethod, requestedUrl, queryParams, headersProtected, bodyProtected);
    }

    /**
     * Logging response
     *
     * @param response response
     * @param body     body
     * @throws JsonProcessingException error mapping json
     */
    public void logResponse(HttpServletResponse response, Object body) throws JsonProcessingException {
        log.info("RESPONSE: STATUS [{}]", response.getStatus());

        if (!(body instanceof InputStreamResource)) {
            String completeResponse = protectFields(mapper.writeValueAsString(body), hideFieldsNames);
            String shortResponse = completeResponse;
            if (completeResponse.length() > MAX_CHARACTERS_TO_PRINT) {
                shortResponse = completeResponse.substring(0, MAX_CHARACTERS_TO_PRINT)
                        .concat(" [Use debug level for view complete response]");
            }

            log.info("RESPONSE BODY [{}]", shortResponse);
            log.debug("RESPONSE BODY COMPLETE [{}]", completeResponse);
        }
        log.info("END TRANSACTION");
        log.info(ConstantsHelper.EMPTY_STRING);
    }

    private static String protectFields(String jsonTextI, String... protectFields) {
        var jsonText = jsonTextI;
        for (String protectField : protectFields) {
            for (var i = 0; i < NAME_ALTERNATIVES_SIZE; i++) {
                var field = getProtectedFieldByIteration(protectField, i);

                var regex = QUOTES + field + REGEX_REPLACE_JSON_VALUE;
                var m = Pattern.compile(regex).matcher(jsonText);
                while (m.find()) {
                    var match = m.group();
                    var matchParts = match.split(TWO_DOTS);

                    var value = matchParts[1];
                    var newValue = StringUtils.leftPad(ConstantsHelper.EMPTY_STRING, value.length(), ASTERISK);
                    if (value.startsWith(QUOTES) && value.endsWith(QUOTES)) {
                        newValue = QUOTES.concat(newValue).concat(QUOTES);
                    }

                    var replace = matchParts[0].concat(TWO_DOTS).concat(newValue);

                    jsonText = jsonText.replace(match, replace);
                }
            }
        }
        return jsonText;
    }

    private static String getProtectedFieldByIteration(String protectField, int iteration) {
        switch (iteration) {
            case 1:
                return protectField.toUpperCase(Locale.ROOT);
            case 2:
                return protectField.toLowerCase(Locale.ROOT);
            case 0:
            default:
                return protectField;
        }
    }
}