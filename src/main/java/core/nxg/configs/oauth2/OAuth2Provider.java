package core.nxg.configs.oauth2;

public enum OAuth2Provider {

    LOCAL, GITHUB, GOOGLE, LINKEDIN;

    public int getPosition() {
        return ordinal() + 1;
    }
}
