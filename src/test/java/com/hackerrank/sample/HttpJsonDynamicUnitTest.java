package com.hackerrank.sample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import javafx.util.Pair;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class HttpJsonDynamicUnitTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final MediaType CONTENT_TYPE_JSON = MediaType.APPLICATION_JSON_UTF8;
    private static final MediaType CONTENT_TYPE_TEXT = MediaType.TEXT_PLAIN;

    private static HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void getContext() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        assertNotNull(mockMvc);
    }

    @Autowired
    public void setConverters(HttpMessageConverter<?>[] converters) {
        mappingJackson2HttpMessageConverter = Stream.of(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull(mappingJackson2HttpMessageConverter);
    }

    List<String> httpJsonFiles = new ArrayList<>();
    Map<String, String> httpJsonAndTestname = new HashMap<>();
    Map<String, Long> executionTime = new HashMap<>();
    Map<String, Pair<Pair<String, String>, Pair<String, String>>> testFailures = new HashMap<>();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {};

    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        public Statement apply(Statement base, Description description) {
            return super.apply(base, description);
        }

        @Override
        protected void starting(Description description) {
            super.starting(description);
        }

        @Override
        protected void succeeded(Description description) {
            generateReportForProperExecution();
        }

        @Override
        protected void failed(Throwable e, Description description) {
            generateReportForRuntimeFailureExecution();
        }

        @Override
        protected void finished(Description description) {
            super.finished(description);
        }
    };

    @Test
    public void dynamicTests() {
        try {
            httpJsonFiles = Files.list(Paths.get("src/test/resources/testcases"))
                    .filter(Files::isRegularFile)
                    .map(f -> f.getFileName().toString())
                    .filter(f -> f.endsWith(".json"))
                    .collect(toList());
        } catch (IOException ex) {
            throw new Error("Error reading testcases directory: " + ex.getMessage());
        }

        if (!httpJsonFiles.isEmpty()) {
            List<String> testnames = new ArrayList<>();

            ClassPathResource resource = new ClassPathResource("testcases/description.txt");
            try (InputStream inputStream = resource.getInputStream()) {
                testnames = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines()
                        .collect(toList());
            } catch (IOException ex) {
                System.err.println("Error reading description.txt: " + ex.getMessage());
                throw new Error("Error reading description.txt: " + ex.getMessage());
            }

            if (!testnames.isEmpty()) {
                assertEquals(httpJsonFiles.size(), testnames.size());

                for (int i = 0; i < testnames.size(); i++) {
                    String[] testname = testnames.get(i).split(": ");
                    httpJsonAndTestname.put(testname[0], testname[1]);
                }

                AtomicInteger processedRequestCount = new AtomicInteger(1);

                httpJsonFiles.forEach(filename -> {
                    if (testFailures.containsKey(filename)) {
                        return;
                    }

                    final List<String> jsonStrings = new ArrayList<>();

                    ClassPathResource jsonResource = new ClassPathResource("testcases/" + filename);
                    try (InputStream inputStream = jsonResource.getInputStream()) {
                        List<String> lines = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                                .lines()
                                .collect(toList());

                        // Filter out empty lines and add validation
                        for (String line : lines) {
                            String trimmedLine = line.trim();
                            if (!trimmedLine.isEmpty()) {
                                jsonStrings.add(trimmedLine);
                            }
                        }
                    } catch (IOException ex) {
                        addTestFailure(filename,
                                new Pair(new Pair("File Read", "Cannot read file"),
                                        new Pair("testcases/" + filename, ex.getMessage())));
                        return;
                    }

                    if (jsonStrings.isEmpty()) {
                        addTestFailure(filename,
                                new Pair(new Pair("Empty File", "No valid JSON lines found"),
                                        new Pair("", "File is empty or contains only whitespace")));
                        return;
                    }

                    jsonStrings.forEach(jsonString -> {
                        if (testFailures.containsKey(filename)) {
                            return;
                        }

                        // Validate JSON before parsing
                        String trimmedJson = jsonString.trim();
                        if (trimmedJson.isEmpty()) {
                            addTestFailure(filename,
                                    new Pair(new Pair("Empty JSON", "Empty JSON line"),
                                            new Pair("", "Line is empty")));
                            return;
                        }

                        JsonNode jsonObject = null;
                        try {
                            jsonObject = OBJECT_MAPPER.readTree(trimmedJson);
                        } catch (JsonProcessingException ex) {
                            addTestFailure(filename,
                                    new Pair(new Pair("JSON Parse Error", ex.getMessage()),
                                            new Pair(trimmedJson, "")));
                            return;
                        }

                        if (jsonObject == null) {
                            addTestFailure(filename,
                                    new Pair(new Pair("JSON Parse", "Failed to parse JSON"),
                                            new Pair(trimmedJson, "")));
                            return;
                        }

                        JsonNode request = jsonObject.get("request");
                        JsonNode response = jsonObject.get("response");

                        if (request == null || response == null) {
                            addTestFailure(filename,
                                    new Pair(new Pair("Missing Fields", "request or response missing"),
                                            new Pair("request/response", trimmedJson)));
                            return;
                        }

                        try {
                            String method = request.get("method").asText();
                            String url = request.get("url").asText();
                            String body = request.get("body") != null ? request.get("body").toString() : "";
                            String statusCode = response.get("status_code").asText();

                            String requestID = Colors.BLUE_BOLD + String.format("Processing request %d ",
                                    processedRequestCount.get()) + Colors.RESET;
                            String requestMessage = String.format("%s %s", method, url);

                            if (method.charAt(0) == 'P') {
                                requestMessage = String.format("%s %s %s", method, url, body);
                            }

                            System.out.println(requestID + Colors.WHITE_BOLD + requestMessage + Colors.RESET);

                            processedRequestCount.set(processedRequestCount.incrementAndGet());

                            switch (method) {
                                case "POST":
                                    executePost(filename, url, body, request, statusCode, method);
                                    break;
                                case "PUT":
                                    executePut(filename, url, body, request, statusCode, method);
                                    break;
                                case "DELETE":
                                    executeDelete(filename, url, statusCode, method);
                                    break;
                                case "GET":
                                    executeGet(filename, url, response, statusCode, method);
                                    break;
                                default:
                                    addTestFailure(filename,
                                            new Pair(new Pair("Unknown Method", "Unsupported HTTP method"),
                                                    new Pair(method, "")));
                                    break;
                            }
                        } catch (Exception ex) {
                            addTestFailure(filename,
                                    new Pair(new Pair("Request Processing", ex.getMessage()),
                                            new Pair("", "")));
                        }
                    });

                    executionTime.put(filename, stopwatch.runtime(TimeUnit.MILLISECONDS));
                });
            }
        }
    }

    private void executePost(String filename, String url, String body, JsonNode request, String statusCode, String method) {
        MediaType contentType = MediaType.ALL;
        JsonNode headers = request.get("headers");
        if (headers != null && headers.has("Content-Type")) {
            String type = headers.get("Content-Type").asText();
            if (type.equals("application/json")) {
                contentType = CONTENT_TYPE_JSON;
            } else if (type.equals("text/plain")) {
                contentType = CONTENT_TYPE_TEXT;
            }
        }

        if (!contentType.equals(MediaType.ALL)) {
            try {
                ResultActions resultActions = mockMvc.perform(post(url)
                        .content(body)
                        .contentType(contentType));
                MockHttpServletResponse mockResponse = resultActions.andReturn().getResponse();

                validateStatusCode(filename, method + " " + url, statusCode, String.valueOf(mockResponse.getStatus()));
            } catch (Exception ex) {
                addTestFailure(filename,
                        new Pair(new Pair("POST Request", ex.getMessage()),
                                new Pair("", "")));
            }
        }
    }

    private void executePut(String filename, String url, String body, JsonNode request, String statusCode, String method) {
        MediaType contentType = MediaType.ALL;
        JsonNode headers = request.get("headers");
        if (headers != null && headers.has("Content-Type")) {
            String type = headers.get("Content-Type").asText();
            if (type.equals("application/json")) {
                contentType = CONTENT_TYPE_JSON;
            } else if (type.equals("text/plain")) {
                contentType = CONTENT_TYPE_TEXT;
            }
        }

        if (!contentType.equals(MediaType.ALL)) {
            try {
                ResultActions resultActions = mockMvc.perform(put(url)
                        .content(body)
                        .contentType(contentType));
                MockHttpServletResponse mockResponse = resultActions.andReturn().getResponse();

                validateStatusCode(filename, method + " " + url, statusCode, String.valueOf(mockResponse.getStatus()));
            } catch (Exception ex) {
                addTestFailure(filename,
                        new Pair(new Pair("PUT Request", ex.getMessage()),
                                new Pair("", "")));
            }
        }
    }

    private void executeDelete(String filename, String url, String statusCode, String method) {
        try {
            ResultActions resultActions = mockMvc.perform(delete(url));
            MockHttpServletResponse mockResponse = resultActions.andReturn().getResponse();

            validateStatusCode(filename, method + " " + url, statusCode, String.valueOf(mockResponse.getStatus()));
        } catch (Exception ex) {
            addTestFailure(filename,
                    new Pair(new Pair("DELETE Request", ex.getMessage()),
                            new Pair("", "")));
        }
    }

    private void executeGet(String filename, String url, JsonNode response, String statusCode, String method) {
        try {
            ResultActions resultActions = mockMvc.perform(get(url));
            MockHttpServletResponse mockResponse = resultActions.andReturn().getResponse();

            if (validateStatusCode(filename, method + " " + url, statusCode, String.valueOf(mockResponse.getStatus()))) {
                JsonNode expectedTypeNode = response.get("headers") != null ? response.get("headers").get("Content-Type") : null;
                if (expectedTypeNode != null) {
                    String expectedType = expectedTypeNode.asText();
                    if (mockResponse.containsHeader("content-type")) {
                        validateContentType(filename, method + " " + url, expectedType, mockResponse.getContentType());
                    }

                    if (statusCode.equals("200")) {
                        String responseBody = mockResponse.getContentAsString();
                        JsonNode expectedResponseBodyJson = response.get("body");

                        if (expectedType.equals("application/json")) {
                            try {
                                JsonNode responseBodyJson = OBJECT_MAPPER.readTree(responseBody);
                                validateJsonResponse(filename, method + " " + url, expectedResponseBodyJson, responseBodyJson);
                            } catch (JsonProcessingException ex) {
                                addTestFailure(filename,
                                        new Pair(new Pair("JSON Response Parse", ex.getMessage()),
                                                new Pair("", responseBody)));
                            }
                        } else if (expectedType.equals("text/plain")) {
                            validateTextResponse(filename, method + " " + url, expectedResponseBodyJson.toString(), responseBody);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            addTestFailure(filename,
                    new Pair(new Pair("GET Request", ex.getMessage()),
                            new Pair("", "")));
        }
    }

    private boolean validateStatusCode(String filename, String testcase, String expected, String found) {
        if (!expected.equals(found)) {
            String reason = "Status code mismatch";
            addTestFailure(filename, new Pair(new Pair(testcase, reason), new Pair(expected, found)));
            return false;
        }
        return true;
    }

    private boolean validateContentType(String filename, String testcase, String expected, String found) {
        if (!found.startsWith(expected)) {
            String reason = "Content type mismatch";
            addTestFailure(filename, new Pair(new Pair(testcase, reason), new Pair(expected, found)));
            return false;
        }
        return true;
    }

    private boolean validateTextResponse(String filename, String testcase, String expected, String found) {
        if (!expected.equals(found)) {
            String reason = "Response text does not match expected";
            addTestFailure(filename, new Pair(new Pair(testcase, reason), new Pair(expected, found)));
            return false;
        }
        return true;
    }

    private boolean validateJsonResponse(String filename, String testcase, JsonNode expected, JsonNode found) {
        try {
            List<JsonNode> expectedResponseJsonList = OBJECT_MAPPER.readValue(expected.toString(),
                    new TypeReference<List<JsonNode>>() {});

            List<JsonNode> responseBodyJsonList = OBJECT_MAPPER.readValue(found.toString(),
                    new TypeReference<List<JsonNode>>() {});

            if (expectedResponseJsonList.size() != responseBodyJsonList.size()) {
                String reason = "Response JSON array size mismatch";
                addTestFailure(filename, new Pair(new Pair(testcase, reason),
                        new Pair(String.valueOf(expectedResponseJsonList.size()),
                                String.valueOf(responseBodyJsonList.size()))));
                return false;
            } else {
                for (int i = 0; i < expectedResponseJsonList.size(); i++) {
                    JsonNode expectedJson = expectedResponseJsonList.get(i);
                    JsonNode foundJson = responseBodyJsonList.get(i);

                    if (!expectedJson.equals(foundJson)) {
                        String reason = String.format("Response JSON at index %d mismatch", i);
                        addTestFailure(filename, new Pair(new Pair(testcase, reason),
                                new Pair(expectedJson.toString(), foundJson.toString())));
                        return false;
                    }
                }
            }
        } catch (IOException ex) {
            if (!expected.equals(found)) {
                String reason = "Response JSON mismatch";
                addTestFailure(filename, new Pair(new Pair(testcase, reason),
                        new Pair(expected.toString(), found.toString())));
                return false;
            }
        }
        return true;
    }

    private void addTestFailure(String filename, Pair<Pair<String, String>, Pair<String, String>> failure) {
        if (testFailures.containsKey(filename)) {
            throw new Error("Should skip rest of test cases for: " + filename);
        }
        testFailures.put(filename, failure);
    }

    // generateReportForProperExecution() method remains the same...
    private void generateReportForProperExecution() {
        List<Long> executionTimeInSeconds = executionTime.keySet()
                .stream()
                .sorted()
                .map(filename -> executionTime.get(filename))
                .collect(toList());

        for (int i = 1; i < executionTimeInSeconds.size(); i++) {
            executionTime.put(httpJsonFiles.get(i),
                    (executionTimeInSeconds.get(i) < executionTimeInSeconds.get(i - 1))
                            ? 0L
                            : executionTimeInSeconds.get(i) - executionTimeInSeconds.get(i - 1));
        }

        final Set<String> failedTestFiles = testFailures.keySet();

        final String DASHES = "------------------------------------------------------------------------";
        final String ANSI_SUMMARY = DASHES + "\n" + Colors.BLUE_BOLD + "TEST SUMMARY\n" + Colors.RESET + DASHES;
        final String ANSI_RESULT = DASHES + "\n" + Colors.BLUE_BOLD + "TEST RESULT\n" + Colors.RESET + DASHES;
        final String ANSI_REPORT = DASHES + "\n" + Colors.BLUE_BOLD + "FAILURE REPORT %s\n" + Colors.RESET + DASHES;
        final String ANSI_FAILURE = Colors.RED_BOLD + "Failure" + Colors.RESET;
        final String ANSI_SUCCESS = Colors.GREEN_BOLD + "Success" + Colors.RESET;

        File reportFolder = new File("target/customReports");
        reportFolder.mkdirs();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("target/customReports/result.txt"))) {
            writer.write(Colors.WHITE_BOLD +
                    " _    _       _ _     _______        _     _____                       _   \n" +
                    "| |  | |     (_) |   |__   __|      | |   |  __ \\                     | |  \n" +
                    "| |  | |_ __  _| |_     | | ___  ___| |_  | |__) |___ _ __   ___  _ __| |_ \n" +
                    "| |  | | '_ \\| | __|    | |/ _ \\/ __| __| |  _  // _ \\ '_ \\ / _ \\| '__| __|\n" +
                    "| |__| | | | | | |_     | |  __/\\__ \\ |_  | | \\ \\  __/ |_) | (_) | |  | |_ \n" +
                    " \\____/|_| |_|_|\\__|    |_|\\___||___/\\__| |_|  \\_\\___| .__/ \\___/|_|   \\__|\n" +
                    "                                                     | |                   \n" +
                    "                                                     |_|                   " +
                    Colors.RESET);
            writer.newLine();

            writer.write(ANSI_SUMMARY);
            writer.newLine();
            writer.write("Tests: " + httpJsonFiles.size() + ", ");
            writer.write("Success: " + (httpJsonFiles.size() - failedTestFiles.size()) + ", ");
            if (!failedTestFiles.isEmpty()) {
                writer.write("Failure: " + failedTestFiles.size() + ", ");
            }
            writer.write("Total time: " + (executionTimeInSeconds.isEmpty() ? 0 : executionTimeInSeconds.get(executionTimeInSeconds.size() - 1)) / 1000.0f + "s");
            writer.newLine();
            writer.newLine();

            writer.write(ANSI_RESULT);
            writer.newLine();

            httpJsonFiles.forEach(filename -> {
                try {
                    if (failedTestFiles.contains(filename)) {
                        writer.write(Colors.WHITE_BOLD + filename + Colors.RESET + ANSI_FAILURE +
                                " (" + executionTime.getOrDefault(filename, 0L) / 1000.0f + "s)");
                    } else {
                        writer.write(Colors.WHITE_BOLD + filename + Colors.RESET + ANSI_SUCCESS +
                                " (" + executionTime.getOrDefault(filename, 0L) / 1000.0f + "s)");
                    }
                    writer.newLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            writer.newLine();

            if (!failedTestFiles.isEmpty()) {
                failedTestFiles.stream().sorted().forEachOrdered(filename -> {
                    Pair<Pair<String, String>, Pair<String, String>> report = testFailures.get(filename);
                    String testStep = report.getKey().getKey();
                    String reason = report.getKey().getValue();
                    String expected = report.getValue().getKey();
                    String found = report.getValue().getValue();

                    try {
                        writer.write(String.format(ANSI_REPORT, filename));
                        writer.newLine();
                        writer.write(Colors.WHITE_BOLD + "[Test Case]" + Colors.RESET + " " + testStep);
                        writer.newLine();
                        writer.write(Colors.WHITE_BOLD + "[   Reason]" + Colors.RESET + " " + Colors.RED_BOLD + reason + Colors.RESET);
                        writer.newLine();
                        writer.write(Colors.WHITE_BOLD + "[ Expected]" + Colors.RESET + " " + expected);
                        writer.newLine();
                        writer.write(Colors.WHITE_BOLD + "[    Found]" + Colors.RESET + " " + found);
                        writer.newLine();
                        writer.newLine();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // XML report generation (simplified, same logic as before)
        generateXmlReport(failedTestFiles);
    }

    private void generateXmlReport(Set<String> failedTestFiles) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("target/customReports/result.xml"))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            double totalTime = executionTime.values().stream().mapToLong(Long::longValue).sum() / 1000.0f;
            writer.write(String.format("<testsuite name=\"%s\" time=\"%.3f\" tests=\"%d\" errors=\"0\" skipped=\"0\" failures=\"%d\">\n",
                    this.getClass().getName(), totalTime, httpJsonFiles.size(), failedTestFiles.size()));

            httpJsonFiles.stream().sorted().forEachOrdered(filename -> {
                try {
                    if (!failedTestFiles.contains(filename)) {
                        writer.write(String.format("    <testcase name=\"%s\" classname=\"%s\" time=\"%.3f\"/>\n",
                                httpJsonAndTestname.getOrDefault(filename, filename),
                                this.getClass().getName(),
                                executionTime.getOrDefault(filename, 0L) / 1000.0f));
                    } else {
                        Pair<Pair<String, String>, Pair<String, String>> report = testFailures.get(filename);
                        String testStep = report.getKey().getKey();
                        String reason = report.getKey().getValue();
                        String expected = report.getValue().getKey();
                        String found = report.getValue().getValue();

                        writer.write(String.format("    <testcase name=\"%s\" classname=\"%s\" time=\"%.3f\">\n" +
                                        "        <failure>Step: %s\nReason: %s\nExpected: %s\nFound: %s</failure>\n" +
                                        "    </testcase>\n",
                                httpJsonAndTestname.getOrDefault(filename, filename),
                                this.getClass().getName(),
                                executionTime.getOrDefault(filename, 0L) / 1000.0f,
                                testStep, reason, expected, found));
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            writer.write("</testsuite>\n");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void generateReportForRuntimeFailureExecution() {
        File reportFolder = new File("target/customReports");
        reportFolder.mkdirs();

        Set<String> failedTestFiles = testFailures.keySet();

        final String DASHES = "------------------------------------------------------------------------";
        final String ANSI_SUMMARY = DASHES + "\n" + Colors.BLUE_BOLD + "TEST SUMMARY\n" + Colors.RESET + DASHES;
        final String ANSI_RESULT = DASHES + "\n" + Colors.BLUE_BOLD + "TEST RESULT\n" + Colors.RESET + DASHES;

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("target/customReports/result.txt"))) {
            writer.write(ANSI_SUMMARY);
            writer.newLine();
            writer.write(String.format("Tests: %d, Success: 0, Failure: %d, Total time: 0s",
                    httpJsonFiles.size(), failedTestFiles.size()));
            writer.newLine();
            writer.newLine();
            writer.write(ANSI_RESULT);
            writer.newLine();

            httpJsonFiles.forEach(filename -> {
                try {
                    writer.write(Colors.WHITE_BOLD + filename + Colors.RESET +
                            Colors.RED_BOLD + " Runtime Failure" + Colors.RESET);
                    writer.newLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        generateXmlReport(failedTestFiles);
    }

    private static class Colors {
        public static final String RESET = "\033[0m";
        public static final String RED = "\033[0;31m";
        public static final String GREEN = "\033[0;32m";
        public static final String BLUE = "\033[0;34m";
        public static final String WHITE = "\033[0;37m";
        public static final String RED_BOLD = "\033[1;31m";
        public static final String GREEN_BOLD = "\033[1;32m";
        public static final String BLUE_BOLD = "\033[1;34m";
        public static final String WHITE_BOLD = "\033[1;37m";
    }
}
