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
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.Comment;
import no.nsd.qddt.exception.StackTraceFilter;
import no.nsd.qddt.utils.StringTool;
import org.joda.time.DateTime;

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
        document.add(new AreaBreak());
        document.add(new Paragraph()
            .setFont(bold)
            .setFontSize(24)
            .setTextAlignment(TextAlignment.CENTER)
            .add("QDDT pdf Report")
            .setPaddingBottom(60).setKeepTogether(false))
        .add(new Paragraph()
            .setFont(font)
            .setFontSize(18)
            .setTextAlignment(TextAlignment.CENTER)
            .add(toc.get(0).getValue().getKey().split("\t")[1])
            .setPaddingBottom(60))
        .add(new Paragraph()
            .add("Generated " + DateTime.now().toLocalDateTime().toString("EEEE d MMMM YYYY HH:mm:SS"))
            .setTextAlignment(TextAlignment.CENTER));

        document.add(new AreaBreak());
        Paragraph p = new Paragraph().setFont(bold)
                .add("Table of Content").setDestination("toc");
        document.add(p);
        toc.remove(0);
        List<TabStop> tabstops = new ArrayList<>();
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
        try {
            for (int i = 1; i <= tocPages; i++) {
                addPage(i, removePage(startToc+i));
            }
//            copyPagesTo(startToc+1, getNumberOfPages(), this, 1);
            } catch (Exception ex) {
            System.out.println(ex.getMessage());
            StackTraceFilter.println(ex.getStackTrace());
        }
        // add page labels
        getPage(1)
                .setPageLabel(PageLabelNumberingStyleConstants.LOWERCASE_ROMAN_NUMERALS, null, 1);
        getPage(2 + tocPages)
                .setPageLabel(PageLabelNumberingStyleConstants.DECIMAL_ARABIC_NUMERALS, null, 1);

    }

    public PdfFont getParagraphFont() {
        return paragraphFont;
    }

    private PdfOutline outline = null;

    public Document addHeader(AbstractEntityAudit element, String header) {

        Table table = new Table(UnitValue.createPercentArray(new float[]{20.0F,20.0F,20.0F,20.0F,20.0F})).setKeepTogether(true);
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
        outline = createOutline(outline, StringTool.CapString(element.getName()), element.getId().toString());
        String[] values = header.split(" ");
        String chapter = "";
        if (values.length > 1)
            chapter = values[1];
        AbstractMap.SimpleEntry<String, Integer> titlePage
                = new AbstractMap.SimpleEntry<>(
                chapter + "\t"  + StringTool.CapString(element.getName())
                , getNumberOfPages());
        Paragraph p =new Paragraph(element.getName()).setKeepTogether(true);
        p.setFontColor(Color.BLUE)
            .setFontSize(14)
            .setKeepWithNext(true)
            .setDestination(element.getId().toString())
            .setNextRenderer(new UpdatePageRenderer(p, titlePage));
        toc.add(new AbstractMap.SimpleEntry<>(element.getId().toString(),titlePage));
        return document.add(p);
    }

    public Document getTheDocument() {
        return this.document;
    }

    public Document addheader2(String header){
        return this.document.add(new Paragraph(header)
            .setWidthPercent(80)
            .setFontColor(Color.BLUE)
            .setKeepTogether(true));
    }

    public Document addParagraph(String value){
        return this.document.add(new Paragraph(value)
            .setWidthPercent(80)
            .setKeepTogether(true));
    }

    public Document addComments(Set<Comment> comments){
        Table table = new Table(UnitValue.createPercentArray(new float[]{20.0F,20.0F,20.0F,20.0F,20.0F}))
                .setKeepTogether(true).setWidthPercent(80).setPaddingBottom(30);
        for(Comment comment: comments.stream().filter(Comment::isPublic).collect(Collectors.toList())){
            addCommentRow(table,comment,0);
        }
        return this.document.add(table);
    }

    public Document addPadding() {
        return document.add(new Paragraph().setPaddingBottom(30).setKeepTogether(false));
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

        for(Comment subcomment: comment.getComments().stream().filter(Comment::isPublic).collect(Collectors.toList())){
            addCommentRow(table,subcomment,level+1);
        }
    }

    private PdfOutline createOutline(PdfOutline outline,  String title, String key) {
        if (outline ==  null) {
            outline = getOutlines(false);
            outline = outline.addOutline(title);
            outline.addDestination(PdfDestination.makeDestination(new PdfString(key)));
            return outline;
        }
        PdfOutline kid = outline.addOutline(title);
        kid.addDestination(PdfDestination.makeDestination(new PdfString(key)));
        return outline;
    }

    private class UpdatePageRenderer extends ParagraphRenderer {
        protected AbstractMap.SimpleEntry<String, Integer> entry;

        public UpdatePageRenderer(Paragraph modelElement, AbstractMap.SimpleEntry<String, Integer> entry) {
            super(modelElement);
            this.entry = entry;
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            LayoutResult result = super.layout(layoutContext);
            entry.setValue(layoutContext.getArea().getPageNumber());
            return result;
        }
    }
}
