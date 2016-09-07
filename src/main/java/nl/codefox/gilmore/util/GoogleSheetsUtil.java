package nl.codefox.gilmore.util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential.Builder;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.PemReader;
import com.google.api.client.util.PemReader.Section;
import com.google.api.client.util.SecurityUtils;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.List;

public class GoogleSheetsUtil {
    /*
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * Go no futher. 
     * Everything beyond is terrible.
     * I hate google API's.
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     */

    static final Charset UTF_8 = Charset.forName("UTF-8");
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME =
            "Google Sheets API Java Quickstart";

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/sheets.googleapis.com-java-quickstart.json
     */
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    private static PrivateKey privateKeyFromPkcs8(String privateKeyPem) throws IOException {
        Reader reader = new StringReader(privateKeyPem);
        Section section = PemReader.readFirstSectionAndClose(reader, "PRIVATE KEY");
        if (section == null) {
            throw new IOException("Invalid PKCS8 data.");
        }
        byte[] bytes = section.getBase64DecodedBytes();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        try {
            KeyFactory keyFactory = SecurityUtils.getRsaKeyFactory();
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException exception) {
        } catch (InvalidKeySpecException exception) {
        }
        return null;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     */
    private static Credential authorize(InputStream is, HttpTransport transport, JsonFactory jsonFactory) throws IOException {
        JsonObjectParser parser = new JsonObjectParser(jsonFactory);
        GenericJson fileContents = parser.parseAndClose(is, UTF_8, GenericJson.class);
        String clientId = (String) fileContents.get("client_id");
        String clientEmail = (String) fileContents.get("client_email");
        String privateKeyPem = (String) fileContents.get("private_key");
        String privateKeyId = (String) fileContents.get("private_key_id");
        if (clientId == null || clientEmail == null || privateKeyPem == null || privateKeyId == null) {
            throw new IOException("Error reading service account credential from stream, "
                    + "expecting  'client_id', 'client_email', 'private_key' and 'private_key_id'.");
        }

        PrivateKey privateKey = privateKeyFromPkcs8(privateKeyPem);

        Builder credentialBuilder = new GoogleCredential.Builder()
                .setTransport(transport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(clientEmail)
                .setServiceAccountScopes(SCOPES)
                .setServiceAccountPrivateKey(privateKey)
                .setServiceAccountPrivateKeyId(privateKeyId);
        String tokenUri = (String) fileContents.get("token_uri");
        if (tokenUri != null) {
            credentialBuilder.setTokenServerEncodedUrl(tokenUri);
        }
        // Don't do a refresh at this point, as it will always fail before the scopes are added.
        return credentialBuilder.build();
    }

    /**
     * Build and return an authorized Sheets API client service.
     *
     * @return an authorized Sheets API client service
     */
    public static Sheets getSheetsService() throws IOException {
        InputStream is = GoogleSheetsUtil.class.getResourceAsStream("/project.json");

        Credential credential = authorize(is, HTTP_TRANSPORT, JSON_FACTORY);
        return new Sheets
                .Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


}
