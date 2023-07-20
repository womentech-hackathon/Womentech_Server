package com.womentech.server.domain.converter;

import com.womentech.server.domain.Day;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.EnumSet;

@Converter
public class SetDayConverter implements AttributeConverter<EnumSet<Day>, String> {
    @Override
    public String convertToDatabaseColumn(EnumSet<Day> days) {
        if (days == null || days.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Day day : days) {
            sb.append(day.name()).append(",");
        }
        return sb.toString();
    }

    @Override
    public EnumSet<Day> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return EnumSet.noneOf(Day.class);
        }
        String[] dayNames = dbData.split(",");
        EnumSet<Day> days = EnumSet.noneOf(Day.class);
        for (String dayName : dayNames) {
            days.add(Day.valueOf(dayName));
        }
        return days;
    }
}