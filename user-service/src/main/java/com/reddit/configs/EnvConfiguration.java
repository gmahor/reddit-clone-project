package com.reddit.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
//@ConfigurationProperties
@Component
public class EnvConfiguration {

    @Value("${backendServerDetail}")
    private String backendServerDetail;

    @Value("${jwtKeyPath}")
    private String jwtKeyPath;

    @Value("${accessTokenExpiryTime}")
    private long accessTokenExpiryTime;

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${mailServerHost}")
    private String mailServerHost;

    @Value("${mailServerPort}")
    private int mailServerPort;

    @Value("${mailServerProtocol}")
    private String mailServerProtocol;

    @Value("${mailServerUsername}")
    private String mailServerUsername;

    @Value("${mailServerPassword}")
    private String mailServerPassword;

    @Value("${mailServerAuthActive}")
    private boolean mailServerAuthActive;

    @Value("${mailServerTlsActive}")
    private boolean mailServerTlsActive;

    @Value("${mailDebugActive}")
    private boolean mailDebugActive;

    @Value("${mailTemplatesPath}")
    private String mailTemplatesPath;

    @Value("${mailFromMailAddress}")
    private String mailFromMailAddress;

}
