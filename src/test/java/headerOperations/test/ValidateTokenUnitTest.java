package headerOperations.test;

import julio.cardGame.cardGameServer.http.Header;
import julio.cardGame.cardGameServer.http.HeaderParser;
import julio.cardGame.cardGameServer.http.HeadersValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValidateTokenUnitTest {

    @Test
    void generateAuthHeaderFromString() {

        //arange
        String headerName = "Authorization";
        String headerContent = "Bearer admin-mtcgToken";
        String rawHeader = headerName + ": " + headerContent;

        //act
        Header generatedHeader = new HeaderParser().parseHeader(rawHeader);

        //assert
        assertEquals(generatedHeader.getName(), headerName);
        assertEquals(generatedHeader.getValue(), headerContent);

    }

    @Test
    void generateContentHeaderFromString() {

        //arrange
        String headerName = "Content-Type";
        String headerContent = "application/json";
        String rawHeader = headerName + ": " + headerContent;

        //act
        Header generatedHeader = new HeaderParser().parseHeader(rawHeader);

        //assert
        assertEquals(generatedHeader.getName(), headerName);
        assertEquals(generatedHeader.getValue(), headerContent);

    }

    @Test
    void validateValidToken() {

        //arrange
        final String tokenValue = "Bearer julioB-mtcgToken";
        final List<Header> validToken = new ArrayList<>();
        validToken.add(
                new Header("Authorization", tokenValue)
        );

        //act
        String extractedToken = HeadersValidator.validateToken(validToken);

        //assert
        assertEquals(extractedToken, tokenValue);

    }

    @Test
    void validateInvalidToken() {

        //arrange
        final String tokenValue = "Bearer admin-mtcgTaokena";
        final List<Header> validToken = new ArrayList<>();
        validToken.add(
                new Header("Authorization", tokenValue)
        );

        //act
        String extractedToken = HeadersValidator.validateToken(validToken);

        //assert
        assertNotEquals(extractedToken, tokenValue);
        assertNull(extractedToken);

    }

    @Test
    void validateAdminToken() {

        //arrange
        final String tokenValue = "Bearer admin-mtcgToken";
        final List<Header> validToken = new ArrayList<>();
        validToken.add(
                new Header("Authorization", tokenValue)
        );

        //act
        String extractedToken = HeadersValidator.validateToken(validToken);
        boolean isAdmin = HeadersValidator.checkAdmin(extractedToken);

        //assert
        assertEquals(extractedToken, tokenValue);
        assert(isAdmin);

    }



}
