package server;

public class DbAuthService implements AuthService {

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        return new DataBase().selectNickname(login, password);
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        return new DataBase().prepareRegistrationData(login, password, nickname);
    }
}
