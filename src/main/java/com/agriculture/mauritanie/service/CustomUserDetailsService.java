package com.agriculture.mauritanie.service;

import com.agriculture.mauritanie.entity.User;
import com.agriculture.mauritanie.entity.StatutEnum;
import com.agriculture.mauritanie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String telephone) throws UsernameNotFoundException {
        User user = userRepository.findByTelephoneAndStatut(telephone, StatutEnum.ACTIF)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + telephone));

        return new CustomUserPrincipal(user);
    }

    // Classe interne pour représenter l'utilisateur authentifié
    public static class CustomUserPrincipal implements UserDetails {
        private final User user;

        public CustomUserPrincipal(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authorities = new ArrayList<>();

            // Ajouter le rôle basé sur le type d'utilisateur
            String role = "ROLE_" + user.getClass().getSimpleName().toUpperCase();
            authorities.add(new SimpleGrantedAuthority(role));

            return authorities;
        }

        @Override
        public String getPassword() {
            return user.getMotDePasseHash();
        }

        @Override
        public String getUsername() {
            return user.getTelephone();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return user.getStatut() != StatutEnum.SUSPENDU;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return user.getStatut() == StatutEnum.ACTIF;
        }

        public User getUser() {
            return user;
        }

        public Long getUserId() {
            return user.getId();
        }
    }
}
