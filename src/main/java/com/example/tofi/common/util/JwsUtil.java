package com.example.tofi.common.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

public class JwsUtil {
    /**
     * {
     * "payload":
     * "eyJpc3MiOiJqb2UiLA0KICJleHAiOjEzMDA4MTkzODAsDQogImh0dHA6Ly9leGF
     * tcGxlLmNvbS9pc19yb290Ijp0cnVlfQ",
     * "protected":"eyJhbGciOiJFUzI1NiJ9",
     * "header":
     * {"kid":"e9bc097a-ce51-4036-9562-d2ade882db0d"},
     * "signature":
     * "DtEhU3ljbEg8L38VWAfUAqOyKAM6-Xx-F4GawxaepmXFCgfTjDxw5djxLa8IS
     * lSApmWQxfKTUJqPP3-Kg6NU1Q"
     * <p>
     * {
     * "payload":"<payload contents>",
     * "protected":"<integrity-protected header contents>",
     * "header":<non-integrity-protected header contents>,
     * "signature":"<signature contents>"
     * }
     * <p>
     * The HTTP header “x-jws signature” is added, which contains data allowing to check
     * whether the message has not been changed on way from the sender to the recipient.
     * <p>
     * JWS Detached generation algorithm is very simple:
     * a) Generate a standard JWS using compact serialization using HTTP body as a payload,
     * b) Turn the middle part (Payload) into an empty string,
     * c) Put final string in the HTTP header “x-jws signature”
     * <p>
     * The header contains parameters holding meta data about:
     * the signature (e.g. algorithm for signing, the id of the key pair used etc.)
     * the payload contains the actual content to be signed (full http body without headers)
     * the signature is the cipher text from enciphering the header and payload together. (x-jws-signature in header
     * <p>
     * Example of the JWS Detached string: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..t3VhQ7QsILDuV_HNFSMI-Fb2FoT7fuzalpS5AH8A9c0
     * <p>
     * Validation HTTP message with JWS Detached is simple too:
     * a) Get the HTTP header “x-jws signature”,
     * b) Get BASE64URL HTTP body
     * c) Put generate string b) into the Payload section
     * d) Validate JWS
     * /**
     * * сообщение jws-message в формате base64url, подписанное эцп
     * * Подписанное ЭЦП зарегистрированной в банке сообщение в формате JWS JSON Serialization
     * * представленное в формате base64url(jws-message). Дополнительная информация RFC 7515 раздел 3.2.
     */

    private JwsUtil() {
    }

    public static String generateDetachedJWS(String body, String publicKey) {
        // The payload to sign
        Payload payload = new Payload(body);
        // Create the JWS secured object for JSON serialisation
        JWSObjectJSON jwsObjectJSON = new JWSObjectJSON(payload);
        //выдадут ЭЦП либо публичный ключ
        RSAKey rsaJWK;
        try {
            // Generate signing key in JWK format (using from bank)
            // 2048-bit RSA signing key for RS256 alg
            rsaJWK = new RSAKeyGenerator(2048)
                    .algorithm(JWSAlgorithm.RS256)
                    .keyUse(KeyUse.SIGNATURE)
                    .keyID(publicKey)
                    .generate();

            // Apply the first signature using the RSA key
            jwsObjectJSON.sign(
                    new JWSHeader.Builder((JWSAlgorithm) rsaJWK.getAlgorithm())
                            .keyID(rsaJWK.getKeyID())
                            .build(),
                    new RSASSASigner(rsaJWK)
            );
        } catch (JOSEException e) {
            //TODO:need to handle exception
        }

        return jwsObjectJSON.serializeGeneral();
    }
}
