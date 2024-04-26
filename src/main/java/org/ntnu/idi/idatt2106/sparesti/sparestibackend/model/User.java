package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import lombok.*;
import org.hibernate.annotations.SortNatural;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "\"USER\"")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String firstName;

    @NotNull
    @Column(nullable = false)
    private String lastName;

    @NotNull
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Setter @Embedded private UserConfig userConfig;

    @OneToMany(
            mappedBy = "user",
            cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @SortNatural
    @JsonManagedReference
    @Setter(AccessLevel.NONE)
    private final Set<Goal> goals = new TreeSet<>();

    @OneToMany(mappedBy = "user")
    private final Set<Challenge> challenges = new HashSet<>();

    @AttributeOverride(name = "accNumber", column = @Column(name = "spending_acc_number"))
    @AttributeOverride(name = "balance", column = @Column(name = "spending_balance"))
    private Account spendingAccount = new Account();

    @AttributeOverride(name = "accNumber", column = @Column(name = "saving_acc_number"))
    @AttributeOverride(name = "balance", column = @Column(name = "saving_balance"))
    private Account savingAccount = new Account();

    // Unidirectional many-to-many
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_BADGE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "BADGE_ID"))
    @Setter(AccessLevel.NONE)
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
