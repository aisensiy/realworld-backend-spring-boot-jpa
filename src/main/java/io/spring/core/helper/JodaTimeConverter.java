package io.spring.core.helper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;

@Converter
public class JodaTimeConverter implements AttributeConverter<DateTime, Timestamp> {
  @Override
  public Timestamp convertToDatabaseColumn(DateTime attribute) {
    if (attribute == null) {
      return null;
    }
    return new Timestamp(attribute.getMillis());
  }

  @Override
  public DateTime convertToEntityAttribute(Timestamp dbData) {
    if (dbData == null) {
      return null;
    }
    return new DateTime(dbData.getTime(), DateTimeZone.UTC);
  }
}
