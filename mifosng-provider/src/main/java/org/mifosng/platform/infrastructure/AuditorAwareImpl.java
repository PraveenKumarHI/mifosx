package org.mifosng.platform.infrastructure;

import org.mifosng.platform.user.domain.AppUser;
import org.mifosng.platform.user.domain.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<AppUser> {

    @Autowired
    private AppUserRepository userRepository;

    @Override
    public AppUser getCurrentAuditor() {

        AppUser currentUser = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if (authentication != null) {
                currentUser = (AppUser) authentication.getPrincipal();
            } else {
                currentUser = this.retrieveSuperUser();
            }
        } else {
            currentUser = this.retrieveSuperUser();
        }
        return currentUser;
    }

    private AppUser retrieveSuperUser() {
        return this.userRepository.findOne(Long.valueOf("1"));
    }
}
