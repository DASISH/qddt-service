package no.nsd.qddt.classes.pdf;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.draw.DottedLine;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import javassist.Loader;
import no.nsd.qddt.classes.AbstractEntityAudit;
import no.nsd.qddt.domain.comment.CommentJsonEdit;
import no.nsd.qddt.exception.StackTraceFilter;
import no.nsd.qddt.utils.StringTool;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Stig Norland
 */
public class PdfReport extends PdfDocument {

    private static final long serialVersionUID = 1345354324653452L;

	  protected final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final List<SimpleEntry<String, SimpleEntry<String, Integer>>> toc = new ArrayList<>();

    public float width100;

    private PdfFont font;
    private PdfFont bold;
    private PdfFont chapterHeading;
    private final int sizeSmall = 9;
    private final int sizeNormal = 12;
    private final int sizeHeader2 = 14;
    private final int sizeHeader1 = 23;

    private final Style cellStyleLeft =  new Style().setFontSize(sizeSmall).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER).setPadding( 1.0F );
    private final Style cellStyleRight =  new Style().setFontSize(sizeSmall).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setPadding( 1.0F ).setPaddingRight( 4.0F );

    private Document document;
//    private PdfDocument pdfContent;

    public PdfReport(ByteArrayOutputStream outputStream) {
        super(new PdfWriter( outputStream).setSmartMode(true));
        try {

            font = PdfFontFactory.createFont( StandardFonts.TIMES_ROMAN);
            bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
            chapterHeading = PdfFontFactory.createFont(StandardFonts.COURIER);

//            pdfContent = new PdfDocument( new PdfWriter(dest) )

            initializeOutlines();

            getCatalog().setPageMode(PdfName.UseOutlines);
            document = new Document(this, PageSize.A4);
            width100 = PageSize.A4.getWidth() - document.getLeftMargin() - document.getRightMargin();
            document.setTextAlignment(TextAlignment.JUSTIFIED)
                    .setHyphenation(new HyphenationConfig("en", "uk", 3, 3))
                    .setFont(font)
                    .setFontSize(sizeNormal);

            addEventHandler( PdfDocumentEvent.START_PAGE, new TextFooterEventHandler(document));

        } catch (Exception ex) {
            LOG.error("PdfReport()",ex);
            StackTraceFilter.filter(ex.getStackTrace()).stream()
                    .map(a->a.toString())
                    .forEach(LOG::info);
        }
    }

    public void createToc() {
        URL url = getResource("qddt.png");
        int startToc = getNumberOfPages();
        getLastPage().setPageLabel(PageLabelNumberingStyle.LOWERCASE_ROMAN_NUMERALS, null, 1);
//        LOG.info( String.join( ", ", getPageLabels() ) );
        document.add(new AreaBreak( AreaBreakType.NEXT_PAGE));
        document.add(
            new Image( ImageDataFactory.create( url  ) )
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
        document.add(new Paragraph().setFont(bold).add("Table of Content").setDestination("toc"));
        List<TabStop> tabstops = new ArrayList<>();
        tabstops.add(new TabStop(580, TabAlignment.RIGHT, new DottedLine()));
        for (SimpleEntry<String, SimpleEntry<String, Integer>> entry : toc) {
            SimpleEntry<String, Integer> text = entry.getValue();
            document.add(new Paragraph()
                .setMultipliedLeading( 1.2F )
                .addTabStops(tabstops)
                .add(text.getKey())
                .add(new Tab())
                .add(String.valueOf(text.getValue()))
                .setAction(PdfAction.createGoTo(entry.getKey())));
        }
        int lastPage = getNumberOfPages();
        int tocPages = lastPage - startToc;

        try {
            for (int i = 0 ; i < tocPages; i++) {
                movePage(getLastPage(), 1 );
            }
        } catch (Exception ex) {
            LOG.error("createToc",ex);
        }
//        LOG.info( String.join( ", ", getPageLabels() ) );

        getPage(1).setPageLabel(PageLabelNumberingStyle.LOWERCASE_ROMAN_NUMERALS, null, 1);
        getPage(tocPages+1).setPageLabel(PageLabelNumberingStyle.DECIMAL_ARABIC_NUMERALS, "Page ", 1);
        getPage(startToc+1).setPageLabel(PageLabelNumberingStyle.DECIMAL_ARABIC_NUMERALS, "Page ", startToc-tocPages+1);
        document.flush();
        close();
    }

//    public PdfFont getParagraphFont() {
//        return paragraphFont;
//    }

    private PdfOutline outline = null;

    public Document addHeader(AbstractEntityAudit element, String header) {
        String[] values = header.split(" ");
        String chapter = "";
        if (values.length > 1) {
            chapter = values[1];
//            document.add( new AreaBreak(AreaBreakType.NEXT_AREA  ) );
            // document.add( new AreaBreak() );        //https://github.com/DASISH/qddt-client/issues/611
        }
        outline = createOutline(outline, StringTool.CapString(element.getName()), element.getId().toString());
        SimpleEntry<String, Integer> titlePage = new SimpleEntry<>( chapter + "\t"  + StringTool.CapString(element.getName()), getNumberOfPages());
        toc.add(new SimpleEntry<>(element.getId().toString(),titlePage));

        Paragraph p = new Paragraph(element.getName())
            .setFontColor(ColorConstants.BLUE)
            .setFontSize(sizeHeader1)
            .setMultipliedLeading( 1F )
//            .setWidth(width100*0.8F)
            .setDestination(element.getId().toString());

        Table table = new Table(UnitValue.createPercentArray(new float[]{68.0F,12.0F,20.0F}));
        table.addCell(
            new Cell(4,1).add(new Paragraph(header).setMultipliedLeading( 1F ).setFontSize(21).setFont( chapterHeading ))
            .setTextAlignment(TextAlignment.LEFT)
            .setBorder( Border.NO_BORDER)
            .add(new Paragraph("__________________________________________________________")
            .setFontColor(ColorConstants.BLUE)
            .setVerticalAlignment( VerticalAlignment.TOP ) )
        .add( p ))
        .addCell(new Cell().add(new Paragraph( "Version")).addStyle( cellStyleRight ) )
        .addCell(new Cell().add(new Paragraph( element.getVersion().toString())).addStyle( cellStyleLeft ))
        .addCell(new Cell().add(new Paragraph("Last Saved")).addStyle( cellStyleRight ))
        .addCell(new Cell().add(new Paragraph(String.format("%1$TF %1$TT",  element.getModified()))).addStyle( cellStyleLeft ))
        .addCell(new Cell().add(new Paragraph("Last Saved By")).addStyle( cellStyleRight ))
        .addCell(new Cell().add(new Paragraph(StringTool.CapString( element.getModifiedBy().getName()))).addStyle( cellStyleLeft ))
        .addCell(new Cell().add(new Paragraph("Agency")).addStyle( cellStyleRight ))
        .addCell(new Cell().add(new Paragraph(element.getAgency().getName())).addStyle( cellStyleLeft ))
        .setWidth(width100 );

        p.setNextRenderer(new UpdatePageRenderer(p, titlePage));
        Div div = new Div().add(table).setKeepTogether( true ).setKeepWithNext(true);


        return document.add(div);
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
            .setFontSize(sizeHeader2)
            .addTabStops(  new TabStop(width100*0.80F, TabAlignment.RIGHT ) )
            .add( new Tab())
            .add(  rev  )
            .setKeepWithNext(true));
    }
    private static final String HTML_PATTERN = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";

    public Document addParagraph(String value){
        try {
            Paragraph para = new Paragraph(  ).setWidth(width100*0.8F).setKeepTogether( false );
            value = Arrays.stream(value.split( "\n" ))
                .map( p -> p.matches( HTML_PATTERN) ? p : p  + "</br>" )
                .collect(Collectors.joining(" "));
//            LOG.info( value );
            List<IElement> elements = HtmlConverter.convertToElements(value);
            for (IElement element : elements) {
                para.add( (IBlockElement)element);
            }
            this.document.add( para );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.document;
    }

    public Document addComments(List<CommentJsonEdit> comments){
        Table table = new Table(UnitValue.createPercentArray(new float[]{25.0F,25.0F,25.0F,25.0F}))
            .setKeepTogether(true)
            .setWidth(width100*0.8F)
            .setPaddingBottom(15);
        for(CommentJsonEdit comment: comments.stream().filter(CommentJsonEdit::isPublic).collect(Collectors.toList())){
            addCommentRow(table,comment,0);
        }
        return this.document.add(table);
    }

    public Document addPadding() {
        return document.add(new Paragraph().setPaddingBottom(15).setKeepTogether(false));
    }

    private void addCommentRow(Table table,CommentJsonEdit comment, int level){
        table.setBackgroundColor(new DeviceRgb(245, 245, 245)).setBorder( Border.NO_BORDER )
            .addCell(new Cell(1,3)
                .setBorder( Border.NO_BORDER )
//                .setWidth(width100*0.8F)
                .setPaddingBottom( 10 )
                .setPaddingRight( 10 )
                .add(new Paragraph( comment.getComment()).setPaddingLeft(20*level)))
            .addCell(new Cell(1,1)
                .setBorder( Border.NO_BORDER )
    //                .setWidth(width100*0.20F)
                .add(new Paragraph( comment.getModifiedBy().getName() + "@"  + comment.getModifiedBy().getAgencyName() +
                    String.format(" - %1$TD %1$TT",  comment.getModified().toLocalDateTime()) ) ));

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


    private class UpdatePageRenderer extends ParagraphRenderer {
        protected SimpleEntry<String, Integer> entry;

        public UpdatePageRenderer(Paragraph modelElement, SimpleEntry<String, Integer> entry) {
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

    private URL getResource(String resource){

        URL url ;

        //Try with the Thread Context Loader.
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader != null){
            url = classLoader.getResource(resource);
            if(url != null){
                return url;
            }
        }

        //Let's now try with the classloader that loaded this class.
        classLoader = Loader.class.getClassLoader();
        if(classLoader != null){
            url = classLoader.getResource(resource);
            if(url != null){
                return url;
            }
        }

        LOG.info( "getResource failing soon..." );

        //Last ditch attempt. Get the resource from the classpath.
        return ClassLoader.getSystemResource(resource);
    }
}
