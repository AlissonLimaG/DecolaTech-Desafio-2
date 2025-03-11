package com.DecolaTech.D2.persistence.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.Objects;
import static java.time.ZoneOffset.UTC;
import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OffsetDateTimeConverter {

    public static OffsetDateTime toOffsetDateTime(final Timestamp value){
        return  Objects.nonNull(value)
                ? OffsetDateTime.ofInstant(value.toInstant(), UTC)
                : null;
    }

    public static Timestamp toTimestamp(final OffsetDateTime value){
        return nonNull(value) ?Timestamp.valueOf(value.atZoneSameInstant(UTC).toLocalDateTime()) :null;
    }

}
