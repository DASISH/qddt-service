//package no.nsd.qddt.domain.instruction;
//
//import no.nsd.qddt.domain.classes.xml.XmlDDIFragmentBuilder;
//
//import java.util.Collections;
//
///**
// * @author Stig Norland
// */
//public class InstructionFragmentBuilder extends  XmlDDIFragmentBuilder<Instruction> {
//
//    protected final String xmlRef =
//        "%3$s<r:InterviewerInstructionReference>\n" +
//        "%3$s\t%2$s" +
//        "%3$s\t<r:TypeOfObject>Instruction</r:TypeOfObject>\n" +
//        "%3$s\t<InstructionAttachmentLocation><r:Value xml:space=\"default\">%1$s</r:Value></InstructionAttachmentLocation>\n"+
//        "%3$s</r:InterviewerInstructionReference>\n";
//
//    private final String xmlInstruction =
//        "%1$s"+
//            "\t\t\t<c:InstructionName>\n" +
//            "\t\t\t\t<r:String xml:lang=\"%4$s\">%2$s</r:String>\n" +
//            "\t\t\t</c:InstructionName>\n"+
//            "\t\t\t<r:Description>\n" +
//            "\t\t\t\t<r:Content xml:lang=\"%4$s\" isPlainText=\"false\"><![CDATA[%3$s]]></r:Content>\n" +
//            "\t\t\t</r:Description>\n" +
//            "\t\t</c:Instruction>\n";
//
//
//    public InstructionFragmentBuilder(Instruction entity) {
//        super(entity);
//    }
//
//
//    public String getXmlFragment() {
//        return String.format( xmlInstruction,
//            getXmlHeader(entity),
//            entity.getName(),
//            entity.getDescription(),
//            entity.getXmlLang());
//    }
//
//    @Override
//    public String getXmlEntityRef(int depth) {
//        return String.format( xmlRef, "PRE" , getXmlURN(entity)  , String.join("", Collections.nCopies(depth, "\t")) );
//    }
//}
