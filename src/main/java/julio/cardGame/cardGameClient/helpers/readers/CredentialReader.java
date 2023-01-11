package julio.cardGame.cardGameClient.helpers.readers;

import julio.cardGame.cardGameServer.application.serverLogic.models.UserLoginDataModel;

import java.io.IOException;

public class CredentialReader extends InputReader {
    public CredentialReader() {
        super();
    }

    public UserLoginDataModel readCredentials() throws IOException {

        System.out.print("Please enter your username (no special chars): ");

        String uName = this.inputReader.readLine().trim();

        while (!clientInputValidator.validateUsername(uName)) {
            //System.out.printf("Invalid username, please try again: ");
            uName = this.inputReader.readLine();
        }

        System.out.print("Please enter your password: ");

        String pwd = this.inputReader.readLine().trim();

        while (!this.clientInputValidator.validatePwd(pwd)) {
            System.out.print("Invalid password, please try again: ");
            pwd = this.inputReader.readLine().trim();
        }

        return new UserLoginDataModel(uName, pwd);

    }
}
