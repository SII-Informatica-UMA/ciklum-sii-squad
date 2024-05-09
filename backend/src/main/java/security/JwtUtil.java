package security;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//
//import org.springframework.stereotype.Component;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.jcodepoint.jwt.util.JksProperties;
//
//@Component
//public class JwtUtil {
//	private RSAPrivateKey privateKey;
//	private RSAPublicKey publicKey;
//
//	public JwtUtil(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
//		this.privateKey = privateKey;
//		this.publicKey = publicKey;
//	}
//
//
//	public String encode(String subject) {
//		return JWT.create()
//				.withSubject(subject)
//				.withExpiresAt(null)
//				.sign(Algorithm.RSA256(publicKey, privateKey) );
//	}
//
//}



import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import es.uma.informatica.sii.fitness.usuarios.entities.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.validity}")
    private long tokenValidity;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //for retrieveing any information from token we will need the secret key
//	private Claims getAllClaimsFromToken(String token) {
////		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//	}

    private Claims getAllClaimsFromToken(String token) {
        byte[] keyBytes = secret.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String generateToken(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, usuario.getId().toString());
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
//	private String doGenerateToken(Map<String, Object> claims, String subject) {
//
//		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//				.signWith(SignatureAlgorithm.HS512, secret).compact();
//	}

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        byte[] keyBytes = secret.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);



        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity * 1000))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}