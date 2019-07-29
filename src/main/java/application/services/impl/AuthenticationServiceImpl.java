package application.services.impl;

import application.model.User;
import application.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";
    private static long EXPIRATION_TIME = 3600000;

    @Override
    public String createJWT(User user) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXPIRATION_TIME);
        Key signingKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(SECRET_KEY), signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setId(user.getUsername())
                .setIssuedAt(now)
                .setSubject("authentication")
                .setIssuer("spring-application")
                .setExpiration(exp)
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }

    @Override
    public Claims decodeJWT(String jwt) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
    }
}
