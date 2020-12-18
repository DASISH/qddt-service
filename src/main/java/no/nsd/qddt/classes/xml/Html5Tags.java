package no.nsd.qddt.classes.xml;

/**
 * @author Stig Norland
 */


public enum Html5Tags {
    abbr("Denotes abbreviations, along with the full forms"),
    address("Tag for specifying author’s contact details."),
    b("Makes text bold. Used to emphasize a point"),
    blockquote("Quotes often go into this tag. Is used in tandem with the <cite> tag."),
    code("his is used to display code snippets within a paragraph."),
    cite("Tag for citing author of a quote."),
    del("Pre-formatted, ‘monospace’ text laid out with whitespace inside the element intact"),
    dfn("Tag dedicated for definitions"),
    em("Another emphasis tag, but this displays text in italics"),
    i("display text in italics, but does not emphasize it like <em>"),
    ins("Denotes text that has been inserted into the webpage."),
    pre("Preformatted text"),
    q("Similar to <blockquote>, but for shorter quotes."),
    small("In HTML5, it often refers to redundant or invalid information."),
    strike("Another old tag, this is used to draw a line at the center of the text"),
    strong("Makes text bold. Used to emphasize a point"),
    sub("Used for writing a superscript"),
    sup("Used for writing a subscript"),
    table("Marks a table in a webpage."),
    thead("Specifies information pertaining to specific columns of the table"),
    tbody("The body of a table, where the data is held"),
    tfoot("Determines the footer of the table"),
    tr("Denotes a single row in a table"),
    th("The value of a heading of a table’s column"),
    td("A single cell of a table. Contains the actual value/data"),
    colgroup("Used for grouping columns together"),
    ol("Tag for ordered or numbered list of items"),
    ul("used for unordered list of items."),
    li("Individual item as part of a list"),
    dt("The definition of a single term inline with body content."),
    dd("The description for the defined term");

    private final String description;

    Html5Tags(String description) {
        this.description = description;
    }
}

