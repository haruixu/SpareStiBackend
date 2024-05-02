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

/**
 * Represents a registered authenticator for a user in a system that supports web authentication (WebAuthn).
 * This entity stores the necessary details required to verify an authenticator's identity and to
 * authenticate requests using WebAuthn protocol.
 *
 * <p>This class includes fields for storing the credential ID, public key, and other relevant data
 * from the WebAuthn registration process. It is associated with a {@link User} entity representing
 * the owner of the authenticator.</p>
 *
 * @author Y.A Marouga
 * @Entity Denotes that this class is a JPA entity.
 * @Getter Lombok annotation to generate getter methods for all fields.
 * @NoArgsConstructor Lombok annotation to generate a no-argument constructor.
 */
@Entity
@Getter
@NoArgsConstructor
public class Authenticator {

    /**
     * The unique identifier for this authenticator.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * A human-readable name for the authenticator, optionally provided during registration.
     */
    @Column private String name;

    /**
     * The credential ID issued by the authenticator upon registration, stored as a binary array.
     * This is crucial for identifying the authenticator in authentication operations.
     */
    @Lob
    @Convert(converter = ByteArrayAttributeConverter.class)
    @Column(nullable = false)
    private ByteArray credentialId;

    /**
     * The public key of the authenticator, encoded in COSE format, also stored as a binary array.
     * This key is used to verify the signatures provided by the authenticator during authentication.
     */
    @Lob
    @Column(nullable = false)
    @Convert(converter = ByteArrayAttributeConverter.class)
    private ByteArray publicKey;

    /**
     * The user to whom this authenticator is linked. This relationship is managed by JPA.
     */
    @ManyToOne private User user;

    /**
     * The signature count, used to validate the authenticity of the authentication response.
     * This helps to mitigate replay attacks.
     */
    @Column(nullable = false)
    private Long count;

    /**
     * The AAGUID of the authenticator, a globally unique identifier for the model of the authenticator,
     * stored as a binary array. This can help categorize the type of the authenticator.
     */
    @Lob
    @Convert(converter = ByteArrayAttributeConverter.class)
    private ByteArray aaguid;

    /**
     * Constructs a new Authenticator instance based on the WebAuthn registration result and the attestation response.
     * This constructor extracts relevant data from the registration and attestation process to populate the entity.
     *
     * @param result The result of the registration process containing the credential ID, public key, and signature count.
     * @param response The attestation response containing details about the credential data.
     * @param user The user to whom this authenticator will be linked.
     * @param name The name to be given to this authenticator.
     */
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
