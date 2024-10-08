package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.BioAuthRequest;

/**
 * Mapper interface for converting BioAuthRequest credentials to a String.
 *
 * @author Yasin M.
 * @version 1.0
 * @since 28.4.24
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class BioAuthMapper {

    /**
     * Maps a BioAuthRequest entity to a string
     * @param credential Request user credentials
     * @return String that represents the request
     * @throws JsonProcessingException Following JSON parsing errors
     */
    public String credentialToString(BioAuthRequest credential) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> credentialObject =
                mapper.readValue(credential.credential(), new TypeReference<>() {});

        JsonNode responseNode =
                mapper.convertValue(credentialObject.get("response"), JsonNode.class);

        ((ObjectNode) responseNode).remove("transports");

        credentialObject.put("response", responseNode);

        return mapper.writeValueAsString(credentialObject);
    }
}
