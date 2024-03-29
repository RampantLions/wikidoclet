/*
 * @(#)EnumConstantWriterImpl.java	1.8 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.formats.html;

import wikidoclet.doclets.internal.toolkit.*;
import wikidoclet.doclets.internal.toolkit.taglets.*;
import wikidoclet.doclets.internal.toolkit.util.*;
import com.sun.javadoc.*;

import java.io.*;

/**
 * Writes enum constant documentation in HTML format.
 *
 * @author Jamie Ho
 */
public class EnumConstantWriterImpl extends AbstractMemberWriter
    implements EnumConstantWriter, MemberSummaryWriter {

    private boolean printedSummaryHeader = false;

    public EnumConstantWriterImpl(SubWriterHolderWriter writer,
        ClassDoc classdoc) {
        super(writer, classdoc);
    }

    public EnumConstantWriterImpl(SubWriterHolderWriter writer) {
        super(writer);
    }

    /**
     * Write the enum constant summary header for the given class.
     *
     * @param classDoc the class the summary belongs to.
     */
    public void writeMemberSummaryHeader(ClassDoc classDoc) {
        printedSummaryHeader = true;
        writer.println("<!-- =========== ENUM CONSTANT SUMMARY =========== -->");
        writer.println();
        writer.printSummaryHeader(this, classDoc);
    }

    /**
     * Write the enum constant summary footer for the given class.
     *
     * @param classDoc the class the summary belongs to.
     */
    public void writeMemberSummaryFooter(ClassDoc classDoc) {
        writer.printSummaryFooter(this, classDoc);
    }

    /**
     * Write the inherited enum constant summary header for the given class.
     *
     * @param classDoc the class the summary belongs to.
     */
    public void writeInheritedMemberSummaryHeader(ClassDoc classDoc) {
        if(! printedSummaryHeader){
            //We don't want inherited summary to not be under heading.
            writeMemberSummaryHeader(classDoc);
            writeMemberSummaryFooter(classDoc);
            printedSummaryHeader = true;
        }
        writer.printInheritedSummaryHeader(this, classDoc);
    }

    /**
     * {@inheritDoc}
     */
    public void writeInheritedMemberSummary(ClassDoc classDoc,
        ProgramElementDoc enumConstant, boolean isFirst, boolean isLast) {
        writer.printInheritedSummaryMember(this, classDoc, enumConstant, isFirst);
    }

    /**
     * Write the inherited enum constant summary footer for the given class.
     *
     * @param classDoc the class the summary belongs to.
     */
    public void writeInheritedMemberSummaryFooter(ClassDoc classDoc) {
        writer.printInheritedSummaryFooter(this, classDoc);
    }

    /**
     * {@inheritDoc}
     */
    public void writeHeader(ClassDoc classDoc, String header) {
        writer.println();
        writer.println("<!-- ============ ENUM CONSTANT DETAIL =========== -->");
        writer.println();
        writer.anchor("enum_constant_detail");
        writer.printTableHeadingBackground(header);
        writer.println();
    }

    /**
     * {@inheritDoc}
     */
    public void writeEnumConstantHeader(FieldDoc enumConstant, boolean isFirst) {
        if (! isFirst) {
            writer.printMemberHeader();
            writer.println("");
        }
        writer.anchor(enumConstant.name());
        writer.h3();
        writer.print(enumConstant.name());
        writer.h3End();
    }

    /**
     * {@inheritDoc}
     */
    public void writeSignature(FieldDoc enumConstant) {
        writer.pre();
        writer.writeAnnotationInfo(enumConstant);
        printModifiers(enumConstant);
        writer.printLink(new LinkInfoImpl(LinkInfoImpl.CONTEXT_MEMBER,
            enumConstant.type()));
        print(' ');
        if (configuration().linksource) {
            writer.printSrcLink(enumConstant, enumConstant.name());
        } else {
            bold(enumConstant.name());
        }
        writer.preEnd();
        writer.dl();
    }

    /**
     * {@inheritDoc}
     */
    public void writeDeprecated(FieldDoc enumConstant) {
        print(((TagletOutputImpl)
            (new DeprecatedTaglet()).getTagletOutput(enumConstant,
            writer.getTagletWriterInstance(false))).toString());
    }

    /**
     * {@inheritDoc}
     */
    public void writeComments(FieldDoc enumConstant) {
        if (enumConstant.inlineTags().length > 0) {
            writer.dd();
            writer.printInlineComment(enumConstant);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void writeTags(FieldDoc enumConstant) {
        writer.printTags(enumConstant);
    }

    /**
     * {@inheritDoc}
     */
    public void writeEnumConstantFooter() {
        writer.dlEnd();
    }

    /**
     * {@inheritDoc}
     */
    public void writeFooter(ClassDoc classDoc) {
        //No footer to write for enum constant documentation
    }

    /**
     * {@inheritDoc}
     */
    public void close() throws IOException {
        writer.close();
    }

    public int getMemberKind() {
        return VisibleMemberMap.ENUM_CONSTANTS;
    }

    public void printSummaryLabel(ClassDoc cd) {
        writer.boldText("doclet.Enum_Constant_Summary");
    }

    public void printSummaryAnchor(ClassDoc cd) {
        writer.anchor("enum_constant_summary");
    }

    public void printInheritedSummaryAnchor(ClassDoc cd) {
    }   // no such

    public void printInheritedSummaryLabel(ClassDoc cd) {
        // no such
    }

    protected void writeSummaryLink(int context, ClassDoc cd, ProgramElementDoc member) {
        writer.bold();
        writer.printDocLink(context, (MemberDoc) member, member.name(), false);
        writer.boldEnd();
    }

    protected void writeInheritedSummaryLink(ClassDoc cd,
            ProgramElementDoc member) {
        writer.printDocLink(LinkInfoImpl.CONTEXT_MEMBER, (MemberDoc)member,
            member.name(), false);
    }

    protected void printSummaryType(ProgramElementDoc member) {
        //Not applicable.
    }

    protected void writeDeprecatedLink(ProgramElementDoc member) {
        writer.printDocLink(LinkInfoImpl.CONTEXT_MEMBER,
            (MemberDoc) member, ((FieldDoc)member).qualifiedName(), false);
    }

    protected void printNavSummaryLink(ClassDoc cd, boolean link) {
        if (link) {
            writer.printHyperLink("", (cd == null)?
                        "enum_constant_summary":
                        "enum_constants_inherited_from_class_" +
                        configuration().getClassName(cd),
                    configuration().getText("doclet.navEnum"));
        } else {
            writer.printText("doclet.navEnum");
        }
    }

    protected void printNavDetailLink(boolean link) {
        if (link) {
            writer.printHyperLink("", "enum_constant_detail",
                configuration().getText("doclet.navEnum"));
        } else {
            writer.printText("doclet.navEnum");
        }
    }
}


