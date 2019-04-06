package ru.sua.security;

public class Constants {
    public static final String TOKEN_PREFIX = "RROC ";

    static final long ACCESS_TOKEN_VALIDITY_SECONDS = 10 * 60;
    static final String SIGNING_KEY = "signer";
    static final String HEADER_STRING = "Authorization";
    static final String AUTHORITIES_KEY = "key";
}
