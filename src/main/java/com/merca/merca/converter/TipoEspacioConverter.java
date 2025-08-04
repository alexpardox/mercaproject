package com.merca.merca.converter;

import com.merca.merca.entity.Formulario;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TipoEspacioConverter implements AttributeConverter<Formulario.TipoEspacio, String> {

    @Override
    public String convertToDatabaseColumn(Formulario.TipoEspacio attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public Formulario.TipoEspacio convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        
        try {
            return Formulario.TipoEspacio.valueOf(dbData.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor de tipo de espacio no válido: " + dbData, e);
        }
    }
}
