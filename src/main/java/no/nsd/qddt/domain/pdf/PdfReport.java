package no.nsd.qddt.domain.pdf;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import no.nsd.qddt.domain.AbstractEntityAudit;

import java.io.ByteArrayOutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stig Norland
 */
public class PdfReport extends PdfDocument {

    private PdfFont chapterFont;
    private PdfFont paragraphFont;
    private PdfFont font;
    private PdfFont bold;
    private final List<AbstractMap.SimpleEntry<String,AbstractMap.SimpleEntry<String, Integer>>> toc = new ArrayList<>();
    private Document document;

    public PdfReport(ByteArrayOutputStream outputStream) {
        super(new PdfWriter( outputStream));
        try {
            font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
            chapterFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLDOBLIQUE);
            paragraphFont = PdfFontFactory.createFont(FontConstants.HELVETICA);
            getCatalog().setPageMode(PdfName.UseOutlines);
            document = new Document(this, PageSize.A4);
            document.setTextAlignment(TextAlignment.JUSTIFIED)
                    .setHyphenation(new HyphenationConfig("en", "uk", 3, 3))
                    .setFont(font)
                    .setFontSize(11);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void createToc() {
        int startToc = getNumberOfPages();
        Paragraph p = new Paragraph().setFont(bold)
                .add("Table of Contents").setDestination("toc");
        document.add(p);
        toc.remove(0);
        List<TabStop> tabstops = new ArrayList();
        tabstops.add(new TabStop(580, TabAlignment.RIGHT, new DottedLine()));
        for (AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, Integer>> entry : toc) {
            AbstractMap.SimpleEntry<String, Integer> text = entry.getValue();
            p = new Paragraph()
                    .addTabStops(tabstops)
                    .add(text.getKey())
                    .add(new Tab())
                    .add(String.valueOf(text.getValue()))
                    .setAction(PdfAction.createGoTo(entry.getKey()));
            document.add(p);
        }
        int tocPages = getNumberOfPages() - startToc;

        // reorder pages
        PdfPage page;
        for (int i = 0; i <= tocPages; i++) {
            page = removePage(startToc + i);
            addPage(i + 1, page);
        }

        // add page labels
        getPage(1)
                .setPageLabel(PageLabelNumberingStyleConstants.LOWERCASE_ROMAN_NUMERALS, null, 1);
        getPage(2 + tocPages)
                .setPageLabel(PageLabelNumberingStyleConstants.DECIMAL_ARABIC_NUMERALS, null, 1);

    }

    public void addTocElement(AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, Integer>> tocElement) {
        this.toc.add(tocElement);
    }

    public Document getTheDocument() {
        return document;
    }

    public PdfFont getChapterFont() {
        return chapterFont;
    }

    public PdfFont getParagraphFont() {
        return paragraphFont;
    }

    public PdfFont getFont() {
        return font;
    }

    public PdfFont getBold() {
        return bold;
    }

    public void addFooter(AbstractEntityAudit element) {
        List<TabStop> tabstops = new ArrayList();
        tabstops.add(new TabStop(100, TabAlignment.RIGHT));
        Paragraph p = new Paragraph()
                .addTabStops(tabstops)
                .add("Version")
                .add(new Tab())
                .add("Modified")
                .add(new Tab())
                .add("ModifiedBy")
                .add(new Tab())
                .add("Agency");
        document.add(p);
        p = new Paragraph()
                .addTabStops(tabstops)
                .add(element.getVersion().toString())
                .add(new Tab())
                .add(element.getModified().toLocalDate().toString())
                .add(new Tab())
                .add(element.getModifiedBy().getUsername())
                .add(new Tab())
                .add(element.getAgency().getName());
        document.add(p);
    }
}
