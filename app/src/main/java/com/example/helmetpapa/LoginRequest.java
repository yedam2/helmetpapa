package com.example.helmetpapa;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    //final static private String URL = "http://203.247.52.216:80/web/WEB-INF/views/Android/login.jsp";
    final static private String URL = "http://203.247.52.216:80/helmetpapa/Android/login.jsp";
    private Map<String, String> map;

    public LoginRequest(String userID, String userPW, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPW", userPW);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}