package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.data.AttestedCredentialData;
import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ByteArray;
import jakarta.persistence.*;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.util.ByteArrayAttributeConverter;

@Entity
@Getter
@NoArgsConstructor
public class Authenticator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column private String name;

    @Lob
    @Convert(converter = ByteArrayAttributeConverter.class)
    @Column(nullable = false)
    private ByteArray credentialId;

    @Lob
    @Column(nullable = false)
    @Convert(converter = ByteArrayAttributeConverter.class)
    private ByteArray publicKey;

    @ManyToOne private User user;

    @Column(nullable = false)
    private Long count;

    @Lob
    @Convert(converter = ByteArrayAttributeConverter.class)
    private ByteArray aaguid;

    public Authenticator(
            RegistrationResult result,
            AuthenticatorAttestationResponse response,
            User user,
            String name) {
        Optional<AttestedCredentialData> attestationData =
                response.getAttestation().getAuthenticatorData().getAttestedCredentialData();
        this.credentialId = result.getKeyId().getId();
        this.publicKey = result.getPublicKeyCose();
        this.aaguid = attestationData.get().getAaguid();
        this.count = result.getSignatureCount();
        this.name = name;
        this.user = user;
    }
}
