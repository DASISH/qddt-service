package no.nsd.qddt.domain.pdf;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.exception.StackTraceFilter;

import java.io.ByteArrayOutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class PdfReport extends PdfDocument {


//    private PdfFont chapterFont;
    private PdfFont paragraphFont;
    private PdfFont font;
    private PdfFont bold;
    private final List<AbstractMap.SimpleEntry<String,AbstractMap.SimpleEntry<String, Integer>>> toc = new ArrayList<>();
    private Document document;
//    private Style style;

    public PdfReport(ByteArrayOutputStream outputStream) {
        super(new PdfWriter( outputStream));
        try {
            font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
            bold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
//            style = new Style();
//            chapterFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLDOBLIQUE);
            paragraphFont = PdfFontFactory.createFont(FontConstants.HELVETICA);
            getCatalog().setPageMode(PdfName.UseOutlines);
            document = new Document(this, PageSize.A4);
            document.setTextAlignment(TextAlignment.JUSTIFIED)
                    .setHyphenation(new HyphenationConfig("en", "uk", 3, 3))
                    .setFont(font)
                    .setFontSize(11);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            StackTraceFilter.println(ex.getStackTrace());
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


    public PdfFont getParagraphFont() {
        return paragraphFont;
    }

    public PdfFont getFont() {
        return font;
    }


    public Document addHeader(AbstractEntityAudit element, String header) {
        addTocElement(new AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, Integer>>(element.getName(),))
        Table table = new Table(5).setKeepTogether(true);
        table.addCell(
            new Cell(4,3).add(
                new Paragraph(header)
                .setFontSize(25))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER)
            .add(new Paragraph("_______________________________________________________")
            .setFontColor(Color.BLUE)));
        table.addCell(
            new Cell().add("Version")
            .setFontSize(9)
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add(element.getVersion().toString())
            .setFontSize(9)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add("Last Saved")
            .setFontSize(9)
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add(element.getModified().toLocalDate().toString())
            .setFontSize(9)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add("Last Saved By")
            .setFontSize(9)
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add(element.getModifiedBy().getUsername())
            .setFontSize(9)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add("Agency")
            .setFontSize(9)
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add(element.getAgency().getName())
            .setFontSize(9)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER));
        document.add(table);
        return document.add(
            new Paragraph(element.getName())
            .setFontColor(Color.BLUE)
            .setFontSize(14));
    }

    public Document getTheDocument() {
        return this.document;
    }

    public Document addParagraph(String value){
        return this.document.add(new Paragraph(value).setWidthPercent(80));
    }

    public Document addComments(Set<Comment> comments){
        Table table = new Table(5).setKeepTogether(true).setWidthPercent(80).setPaddingBottom(30);
        for(Comment comment: comments.stream().filter(c->c.isPublic()).collect(Collectors.toList())){
            addCommentRow(table,comment,0);
        }
        return this.document.add(table);
    }

    private void addCommentRow(Table table,Comment comment, int level){
        table.setBackgroundColor(new DeviceRgb(245, 245, 245))
            .addCell(new Cell(1,3)
                .setWidthPercent(70)
                .setPaddingBottom(10)
                .setBorder(Border.NO_BORDER)
                .add(comment.getComment()).setPaddingLeft(15*level))
            .addCell(new Cell()
                .setWidthPercent(15)
                .setPaddingBottom(10)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(comment.getModifiedBy().getUsername()))
            .addCell(new Cell()
                .setWidthPercent(15)
                .setPaddingBottom(10)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(comment.getModified().toLocalDate().toString()));

        for(Comment subcomment: comment.getComments().stream().filter(c->c.isPublic()).collect(Collectors.toList())){
            addCommentRow(table,subcomment,level+1);
        }
    }

}
