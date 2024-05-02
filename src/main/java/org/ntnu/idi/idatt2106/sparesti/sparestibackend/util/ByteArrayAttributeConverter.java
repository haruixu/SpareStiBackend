package org.ntnu.idi.idatt2106.sparesti.sparestibackend.util;

import com.yubico.webauthn.data.ByteArray;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Class used to save a handle in database inside user entity
 *
 * @author Yasin M.
 * @version 1.0
 * @since 28.4.24
 */
@Converter(autoApply = true)
public class ByteArrayAttributeConverter implements AttributeConverter<ByteArray, byte[]> {

    /**
     * Converts attribute to database column
     * @param attribute Array of bytes
     * @return Array of bytes
     */
    @Override
    public byte[] convertToDatabaseColumn(ByteArray attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getBytes();
    }

    /**
     * Converts database data to entity attribute
     * @param dbData Database data
     * @return Byte array
     */
    @Override
    public ByteArray convertToEntityAttribute(byte[] dbData) {
        if (dbData == null) {
            return null;
        }
        return new ByteArray(dbData);
    }
}
