package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.SortNatural;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@AllArgsConstructor
@Table(name = "\"USER\"")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String firstName;

    @NotNull
    @Column(nullable = false)
    private String lastName;

    @NotNull
    @Column(nullable = false, unique = true)
    @NaturalId
    private String username;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false, unique = true)
    @NaturalId
    @Email
    private String email;

    @Setter @Embedded private UserConfig userConfig;

    @OneToMany(mappedBy = "user")
    @SortNatural
    private final Set<Goal> goals = new TreeSet<>();

    @OneToMany(mappedBy = "user")
    private final Set<Challenge> challenges = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ACCOUNT")
    private final Set<Account> accounts = new HashSet<>();

    // Unidirectional many-to-many
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_BADGE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "BADGE_ID"))
    private final Set<Badge> badges = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userConfig.getRole().name()));
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
