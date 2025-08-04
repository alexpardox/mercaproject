package com.merca.merca.converter;

import com.merca.merca.entity.Formulario;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FormularioEstadoConverter implements AttributeConverter<Formulario.Estado, String> {

    @Override
    public String convertToDatabaseColumn(Formulario.Estado attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public Formulario.Estado convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        
        try {
            return Formulario.Estado.valueOf(dbData.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor de estado de formulario no válido: " + dbData, e);
        }
    }
}
