package vn.iostar.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> responseBuilder(Object data, HttpStatus httpStatus, String message) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", message);
        resp.put("status", httpStatus);
        resp.put("data", data);

        return new ResponseEntity<>(resp, httpStatus);
    }
}
