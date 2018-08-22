package no.nsd.qddt.domain.pdf;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import net.logstash.logback.encoder.org.apache.commons.io.IOUtils;
import no.nsd.qddt.domain.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.exception.StackTraceFilter;
import no.nsd.qddt.utils.StringTool;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class PdfReport extends PdfDocument {

    private static final long serialVersionUID = 1345354324653452L;

	  protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final List<AbstractMap.SimpleEntry<String,AbstractMap.SimpleEntry<String, Integer>>> toc = new ArrayList<>();

    public float width100;

    private PdfFont paragraphFont;
    private PdfFont font;
    private PdfFont bold;
    private Document document;

    public PdfReport(ByteArrayOutputStream outputStream) {
        super(new PdfWriter( outputStream));
        try {

            font = PdfFontFactory.createFont( StandardFonts.TIMES_ROMAN);
            bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            paragraphFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            getCatalog().setPageMode(PdfName.UseOutlines);
            document = new Document(this, PageSize.A4);
            width100 = PageSize.A4.getWidth() - document.getLeftMargin() - document.getRightMargin();
            document.setTextAlignment(TextAlignment.JUSTIFIED)
                    .setHyphenation(new HyphenationConfig("en", "uk", 3, 3))
                    .setFont(font)
                    .setFontSize(11);
        } catch (Exception ex) {
            LOG.error("PdfReport()",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
        }
    }

    public void createToc() throws IOException {

        InputStream inputStream = getClass().getResourceAsStream("../../../../../qddt.png");
        int startToc = getNumberOfPages();
        document.add(new AreaBreak());
        document.add(
            new Image( ImageDataFactory.createPng( IOUtils.toByteArray(inputStream)))
            .setHorizontalAlignment( HorizontalAlignment.CENTER ))
        .add(new Paragraph()
            .setFont(bold)
            .setFontSize(24)
            .setTextAlignment(TextAlignment.CENTER)
            .add("Pdf report")
            .setPaddingBottom(60).setKeepTogether(false))
        .add(new Paragraph()
            .setFont(font)
            .setFontSize(18)
            .setTextAlignment(TextAlignment.CENTER)
            .add(toc.get(0).getValue().getKey().split("\t")[1])
            .setPaddingBottom(60))
        .add(new Paragraph()
            .add("Generated " + DateTime.now().toString("EEEE d MMMM YYYY HH:mm:SS"))
            .setTextAlignment(TextAlignment.CENTER));

        document.add(new AreaBreak());
        Paragraph p = new Paragraph().setFont(bold)
                .add("Table of Content").setDestination("toc");
        document.add(p);
        // toc.remove(0);
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
                movePage(startToc+i, i );
            }
        } catch (Exception ex) {
            LOG.error("createToc",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                .map(a->a.toString())
                .forEach(LOG::info);
        }
        // add page labels, only visible electronically, not on print.
        getPage(1)
                .setPageLabel(PageLabelNumberingStyle.LOWERCASE_ROMAN_NUMERALS, null, 1);
        getPage(1 + tocPages)
                .setPageLabel(PageLabelNumberingStyle.DECIMAL_ARABIC_NUMERALS, "Page ", 1);

        setPageFooter( tocPages );
        close();
    }

    public PdfFont getParagraphFont() {
        return paragraphFont;
    }

    private PdfOutline outline = null;

    public Document addHeader(AbstractEntityAudit element, String header) {
        String[] values = header.split(" ");
        String chapter = "";
        if (values.length > 1) {
            chapter = values[1];
            document.add( new AreaBreak() );        //https://github.com/DASISH/qddt-client/issues/611
        }
        Table table = new Table(UnitValue.createPercentArray(new float[]{20.0F,20.0F,20.0F,20.0F,20.0F})).setKeepTogether(true);
        table.addCell(
            new Cell(4,3).add(
                new Paragraph(header)
                .setFontSize(25))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder( Border.NO_BORDER)
            .add(new Paragraph("_______________________________________________________")
            .setFontColor(ColorConstants.BLUE)));
        table.addCell(
            new Cell().add(new Paragraph( "Version"))
            .setFontSize(9)
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add(new Paragraph( element.getVersion().toString()))
            .setFontSize(9)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add(new Paragraph("Last Saved"))
            .setFontSize(9)
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add(new Paragraph(String.format("%1$TF %1$TT",  element.getModified())))
            .setFontSize(9)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add(new Paragraph("Last Saved By"))
            .setFontSize(9)
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add(new Paragraph(StringTool.CapString( element.getModifiedBy().getUsername())))
            .setFontSize(9)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add(new Paragraph("Agency"))
            .setFontSize(9)
            .setTextAlignment(TextAlignment.RIGHT)
            .setBorder(Border.NO_BORDER));
        table.addCell(
            new Cell().add(new Paragraph(element.getAgency().getName()))
            .setFontSize(9)
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder(Border.NO_BORDER));
        document.add(table);
        outline = createOutline(outline, StringTool.CapString(element.getName()), element.getId().toString());

        AbstractMap.SimpleEntry<String, Integer> titlePage
                = new AbstractMap.SimpleEntry<>(
                chapter + "\t"  + StringTool.CapString(element.getName())
                , getNumberOfPages());
        Paragraph p =new Paragraph(element.getName()).setKeepTogether(true);
        p.setFontColor(ColorConstants.BLUE)
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
        return addheader2( header,null );
    }

    public Document addheader2(String header, String rev){
        rev = rev==null?"":rev;
        return this.document.add(new Paragraph(header)
            .setMaxWidth(width100*0.8F)
            .setFontColor(ColorConstants.BLUE)
            .addTabStops(  new TabStop(width100*0.80F, TabAlignment.RIGHT ) )
            .add( new Tab())
            .add(  rev  )
            .setKeepTogether(true));
    }

    public Document addParagraph(String value){
        try {
            Paragraph para = new Paragraph(  );
            value = Arrays.asList(value.split( "\n" )).stream()
                .map( p -> "<p>" + p  + "</p>" )
                .collect(Collectors.joining(" "));
//            LOG.info( value );
            List<IElement> elements = HtmlConverter.convertToElements(value);
            for (IElement element : elements) {
                para.add( (IBlockElement)element);
            }
            para.setWidth(width100*0.8F).setPaddingBottom(15).setKeepTogether(true);
            this.document.add( para );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.document;
    }

    public Document addComments(Set<CommentJsonEdit> comments){
        Table table = new Table(UnitValue.createPercentArray(new float[]{20.0F,20.0F,20.0F,20.0F,20.0F}))
            .setKeepTogether(true)
            .setWidth(width100*0.8F)
            .setPaddingBottom(30);
        for(CommentJsonEdit comment: comments.stream().filter(CommentJsonEdit::isPublic).collect(Collectors.toList())){
            addCommentRow(table,comment,0);
        }
        return this.document.add(table);
    }


    public Document addPadding() {
        return document.add(new Paragraph().setPaddingBottom(30).setKeepTogether(false));
    }

    private void addCommentRow(Table table,CommentJsonEdit comment, int level){
        table.setBackgroundColor(new DeviceRgb(245, 245, 245))
            .addCell(new Cell(1,3)
                .setWidth(width100*0.7F)
                .setPaddingBottom(10)
                .setBorder(Border.NO_BORDER)
                .add(new Paragraph( comment.getComment()).setPaddingLeft(15*level)))
            .addCell(new Cell()
                .setWidth(width100*0.15F)
                .setPaddingBottom(10)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(new Paragraph( comment.getModifiedBy().getUsername())))
            .addCell(new Cell()
                .setWidth(width100*0.15F)
                .setPaddingBottom(10)
                .setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
                .add(new Paragraph( String.format("%1$TD %1$TT",  comment.getModified().toLocalDateTime()))));

        for(CommentJsonEdit subcomment: comment.getComments().stream().filter(CommentJsonEdit::isPublic).collect(Collectors.toList())){
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

    private void setPageFooter(int tocPages) {
        String[] pagelabels = getPageLabels();
        for(int i = tocPages+1; i < getNumberOfPages(); i++  ) {
            PdfPage page = getPage( i);
            page.setRotation( 0 );
            PdfCanvas canvas = new PdfCanvas(page);
            Rectangle pageSize = page.getPageSize();
            canvas.beginText();
            canvas.setFontAndSize(font , 5);
            canvas.moveText(0, (pageSize.getBottom() + document.getBottomMargin()) - (pageSize.getTop() + document.getTopMargin()) - 20)
                .showText(pagelabels[i])
                .endText()
                .release();

            LOG.info(pagelabels[i]);
        }
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
