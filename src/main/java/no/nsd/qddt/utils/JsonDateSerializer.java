package no.nsd.qddt.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author Stig Norland
 */
public class JsonDateSerializer extends JsonSerializer<Timestamp> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

    @Override
    public void serialize(Timestamp value, JsonGenerator g, SerializerProvider provider)
            throws IOException, JsonProcessingException {

//        if (useTimestamp(provider)) {
//            g.writeStartArray();
//            g.writeNumber(value.getYear());
//            g.writeNumber(value.getTime() getMonthValue());
//            g.writeNumber(value.getDayOfMonth());
//            g.writeNumber(value.getHour());
//            g.writeNumber(value.getMinute());
//            if (value.getSecond() > 0 || value.getNano() > 0) {
//                g.writeNumber(value.getSecond());
//                if(value.getNano() > 0) {
//                    if (provider.isEnabled(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS))
//                        g.writeNumber(value.getNano());
//                    else
//                        g.writeNumber(value.get(ChronoField.MILLI_OF_SECOND));
//                }
//            }
//            g.writeEndArray();
//        } else {
//            DateTimeFormatter dtf = _formatter;
//            if (dtf == null) {
//                dtf = _defaultFormatter();
//            }
//            g.writeString(value.format(dtf));
//        }

        String formattedDate = dateFormat.format(value);
        g.writeString(formattedDate);

    }


}
