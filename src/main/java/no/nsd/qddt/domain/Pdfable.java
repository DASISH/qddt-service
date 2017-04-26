package no.nsd.qddt.domain;

/**
 * Created by of on 27.01.2017.
 */
import com.itextpdf.layout.Document;
import java.io.ByteArrayOutputStream;

public interface Pdfable {

    ByteArrayOutputStream makePdf(Document document);

    void fillDoc(Document document);

}
