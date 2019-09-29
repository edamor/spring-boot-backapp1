package com.zuitt.capstone3.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFilter implements Filter {

    @Value("${jwt.secret}")
    private String secretKey;

    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Origin","*");
        res.addHeader("Access-Control-Allow-Methods",
                "GET, OPTIONS, HEAD, PUT, POST, DELETE");
        res.addHeader("Access-Control-Allow-Headers","*");
        if (req.getMethod().equals("OPTIONS")) {
            res.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        if(req.getRequestURI().startsWith("/users")){
            String token = req.getHeader("x-auth-token");
            System.out.println(token);
            String user = null;

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token).getBody();
                user = claims.getSubject();
            } catch (JwtException | ClassCastException | IllegalArgumentException e) {
                user = "ERROR";
            }

            if (user.equals("ERROR") ){
                res.setStatus(400);
                response.getOutputStream().print("Token is invalid");
                return;
            }
        }
        chain.doFilter(request,response);
    }


}
