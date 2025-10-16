package com.familyfund.backend.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.familyfund.backend.modelo.Family;
import com.familyfund.backend.modelo.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")

public class UserDetailsImpl implements UserDetails {
  private Long id;

  private String email;

  private String nombre;
    private String photoUrl;  // <-- añadir aquí

  private Family family;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

public static UserDetailsImpl build(Usuario user) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRol().name()));
    return new UserDetailsImpl(
        user.getId(),
        user.getEmail(),
        user.getNombre(),
        user.getPhotoUrl(),    
        user.getFamily(),
        user.getPassword(),
        authorities);
}

  public Long getUserId() {
    return id;
  }

  @Override
  public String getUsername() {
    return email; // email como identificador único
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
