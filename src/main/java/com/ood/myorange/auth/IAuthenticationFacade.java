package com.ood.myorange.auth;

import org.springframework.security.core.Authentication;

/**
 * Created by Chen on 3/18/20.
 */
public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
