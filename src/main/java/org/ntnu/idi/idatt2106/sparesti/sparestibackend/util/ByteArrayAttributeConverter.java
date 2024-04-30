package org.ntnu.idi.idatt2106.sparesti.sparestibackend.util;

import com.yubico.webauthn.data.ByteArray;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ByteArrayAttributeConverter implements AttributeConverter<ByteArray, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(ByteArray attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getBytes();
    }

    @Override
    public ByteArray convertToEntityAttribute(byte[] dbData) {
        if (dbData == null) {
            return null;
        }
        return new ByteArray(dbData);
    }
}
