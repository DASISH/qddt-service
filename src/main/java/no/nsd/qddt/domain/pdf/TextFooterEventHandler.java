package no.nsd.qddt.domain.pdf;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PageLabelNumberingStyle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Stig Norland
 */
public class TextFooterEventHandler implements IEventHandler {
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected Document doc;

    public TextFooterEventHandler(Document doc) {
        this.doc = doc;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent pdfDoc = (PdfDocumentEvent) event;
        PdfPage page = pdfDoc.getPage();
        PdfDocument pdfDocument = pdfDoc.getDocument();
        int i = pdfDocument.getNumberOfPages();
        String[] labels = pdfDocument.getPageLabels();
        if (labels == null) {
            page.setPageLabel( PageLabelNumberingStyle.DECIMAL_ARABIC_NUMERALS, "Page ",1 );
            labels = pdfDocument.getPageLabels();
        }

//        if ( labels[i-1].contains( "Page" )) {
            PdfCanvas canvas = new PdfCanvas( pdfDoc.getPage() );
            canvas.beginText();
            try {
                canvas.setFontAndSize( PdfFontFactory.createFont( StandardFonts.TIMES_ROMAN ), 9 );
                canvas.moveText(page.getPageSize().getWidth() - (doc.getRightMargin()+10) , doc.getBottomMargin()-10 )
                    .showText( labels[i-1] )
                    .endText()
                    .release();
            } catch (Exception e) {
                e.printStackTrace();
                canvas.release();
            }
//        }
    }

}
