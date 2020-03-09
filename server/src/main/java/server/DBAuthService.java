package server;

public class DBAuthService implements AuthService {

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        return DataBaseQuery.selectNickname(login, password);
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        return DataBaseQuery.prepareRegistrationData(login, password, nickname);
    }
}
