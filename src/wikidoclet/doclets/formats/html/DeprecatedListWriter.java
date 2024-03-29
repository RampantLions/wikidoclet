/*
 * @(#)DeprecatedListWriter.java	1.34 05/11/30
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.formats.html;

import wikidoclet.doclets.internal.toolkit.util.DeprecatedAPIListBuilder;
import wikidoclet.doclets.internal.toolkit.util.*;
import java.io.*;

/**
 * Generate File to list all the deprecated classes and class members with the
 * appropriate links.
 *
 * @see java.util.List
 * @author Atul M Dambalkar
 */
public class DeprecatedListWriter extends SubWriterHolderWriter {

    private static final String[] ANCHORS = new String[] {
        "interface", "class", "enum", "exception", "error", "annotation_type",
         "field", "method", "constructor", "enum_constant",
        "annotation_type_member"
    };

    private static final String[] HEADING_KEYS = new String[] {
        "doclet.Deprecated_Interfaces", "doclet.Deprecated_Classes",
        "doclet.Deprecated_Enums", "doclet.Deprecated_Exceptions",
        "doclet.Deprecated_Errors",
        "doclet.Deprecated_Annotation_Types",
        "doclet.Deprecated_Fields",
        "doclet.Deprecated_Methods", "doclet.Deprecated_Constructors",
        "doclet.Deprecated_Enum_Constants",
        "doclet.Deprecated_Annotation_Type_Members"
    };

    private AbstractMemberWriter[] writers;

    private ConfigurationImpl configuration;

    /**
     * Constructor.
     *
     * @param filename the file to be generated.
     */
    public DeprecatedListWriter(ConfigurationImpl configuration,
                                String filename) throws IOException {
        super(configuration, filename);
        this.configuration = configuration;
        NestedClassWriterImpl classW = new NestedClassWriterImpl(this);
        writers = new AbstractMemberWriter[]
            {classW, classW, classW, classW, classW, classW,
            new FieldWriterImpl(this),
            new MethodWriterImpl(this),
            new ConstructorWriterImpl(this),
            new EnumConstantWriterImpl(this),
            new AnnotationTypeOptionalMemberWriterImpl(this, null)};
    }

    /**
     * Get list of all the deprecated classes and members in all the Packages
     * specified on the Command Line.
     * Then instantiate DeprecatedListWriter and generate File.
     *
     * @param configuration the current configuration of the doclet.
     */
    public static void generate(ConfigurationImpl configuration) {
        String filename = "deprecated-list.html";
        try {
            DeprecatedListWriter depr =
                   new DeprecatedListWriter(configuration, filename);
            depr.generateDeprecatedListFile(
                   new DeprecatedAPIListBuilder(configuration.root));
            depr.close();
        } catch (IOException exc) {
            configuration.standardmessage.error(
                        "doclet.exception_encountered",
                        exc.toString(), filename);
            throw new DocletAbortException();
        }
    }

    /**
     * Print the deprecated API list. Separately print all class kinds and
     * member kinds.
     *
     * @param deprapi list of deprecated API built already.
     */
    protected void generateDeprecatedListFile(DeprecatedAPIListBuilder deprapi)
             throws IOException {
        writeHeader();

        bold(configuration.getText("doclet.Contents"));
        ul();
        for (int i = 0; i < DeprecatedAPIListBuilder.NUM_TYPES; i++) {
            writeIndexLink(deprapi, i);
        }
        ulEnd();
        println();

        for (int i = 0; i < DeprecatedAPIListBuilder.NUM_TYPES; i++) {
            if (deprapi.hasDocumentation(i)) {
                writeAnchor(deprapi, i);
                writers[i].printDeprecatedAPI(deprapi.getList(i),
                    HEADING_KEYS[i]);
            }
        }
        printDeprecatedFooter();
    }

    private void writeIndexLink(DeprecatedAPIListBuilder builder,
            int type) {
        if (builder.hasDocumentation(type)) {
            li();
            printHyperLink("#" + ANCHORS[type],
                configuration.getText(HEADING_KEYS[type]));
            println();
        }
    }

    private void writeAnchor(DeprecatedAPIListBuilder builder, int type) {
        if (builder.hasDocumentation(type)) {
            anchor(ANCHORS[type]);
        }
    }

    /**
     * Print the navigation bar and header for the deprecated API Listing.
     */
    protected void writeHeader() {
        printHtmlHeader(configuration.getText("doclet.Window_Deprecated_List"),
            null, true);
        printTop();
        navLinks(true);
        hr();
        center();
        h2();
        boldText("doclet.Deprecated_API");
        h2End();
        centerEnd();

        hr(4, "noshade");
    }

    /**
     * Print the navigation bar and the footer for the deprecated API Listing.
     */
    protected void printDeprecatedFooter() {
        hr();
        navLinks(false);
        printBottom();
        printBodyHtmlEnd();
    }

    /**
     * Highlight the word "Deprecated" in the navigation bar as this is the same
     * page.
     */
    protected void navLinkDeprecated() {
        navCellRevStart();
        fontStyle("NavBarFont1Rev");
        boldText("doclet.navDeprecated");
        fontEnd();
        navCellEnd();
    }
}
