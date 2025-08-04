package com.merca.merca.converter;

import com.merca.merca.entity.Proveedor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ProveedorEstadoConverter implements AttributeConverter<Proveedor.Estado, String> {

    @Override
    public String convertToDatabaseColumn(Proveedor.Estado attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public Proveedor.Estado convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        
        try {
            return Proveedor.Estado.valueOf(dbData.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor de estado de proveedor no válido: " + dbData, e);
        }
    }
}
