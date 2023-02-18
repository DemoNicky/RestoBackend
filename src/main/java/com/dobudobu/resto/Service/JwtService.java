package com.dobudobu.resto.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "5A7134743777217A25432A462D4A614E645267556B58703272357538782F413F";

    //mengekstrak username dari token
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    //extrak single claims dari jwt token
    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    //menggenerate/membuat jwt token
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
        return Jwts.builder()
                .setClaims(extraClaims)
                //subject berisi username/email
                .setSubject(userDetails.getUsername())
                //digunakan untuk membuat tanggal/waktu user di buat
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //digunakan untuk menyetel/mengatur kapan token tidak akan valid lagi(kadaluarsa)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                //digunakan untuk menentukan algoritma penandatanganan dan
                // kunci rahasia yang digunakan untuk menandatangani JWT
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                //digunakan untuk mmebuat dan mereturn token
                .compact();
    }

    //digunakan untuk memeriksa apakah token valid atau tidak
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    //digunakan untuk memeriksa apakah token sudah expired atau tidak
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    //digunakan untuk mengekstrak token
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                //signkey digunakan untuk mmebuat tanda tangan/signature dari jwt
                //yang di gunakan untuk memverivikasi pengirim jwt yang mengclaim
                //dan memastikan pesan tidak berubah
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
