/*
 * @(#)LinkFactoryImpl.java	1.7 05/11/30
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.formats.html;

import wikidoclet.doclets.internal.toolkit.util.links.*;
import com.sun.javadoc.*;
import wikidoclet.doclets.internal.toolkit.*;
import wikidoclet.doclets.internal.toolkit.util.*;

/**
 * A factory that returns a link given the information about it.
 *
 * @author Jamie Ho
 * @since 1.5
 */
public class LinkFactoryImpl extends LinkFactory {

    private HtmlDocletWriter m_writer;

    public LinkFactoryImpl(HtmlDocletWriter writer) {
        m_writer = writer;
    }

    /**
     * {@inheritDoc}
     */
    protected LinkOutput getOutputInstance() {
        return new LinkOutputImpl();
    }

    /**
     * {@inheritDoc}
     */
    protected LinkOutput getClassLink(LinkInfo linkInfo) {
        LinkInfoImpl classLinkInfo = (LinkInfoImpl) linkInfo;
        boolean noLabel = linkInfo.label == null || linkInfo.label.length() == 0;
        ClassDoc classDoc = classLinkInfo.classDoc;
        //Create a tool tip if we are linking to a class or interface.  Don't
        //create one if we are linking to a member.
        String title =
            (classLinkInfo.where == null || classLinkInfo.where.length() == 0) ?
                getClassToolTip(classDoc,
                    classLinkInfo.type != null &&
                    !classDoc.qualifiedTypeName().equals(classLinkInfo.type.qualifiedTypeName())) :
            "";
        StringBuffer label = new StringBuffer(
            classLinkInfo.getClassLinkLabel(m_writer.configuration));
        classLinkInfo.displayLength += label.length();
        if (noLabel && classLinkInfo.excludeTypeParameterLinks) {
            label.append(getTypeParameterLinks(linkInfo).toString());
        }
        Configuration configuration = ConfigurationImpl.getInstance();
        LinkOutputImpl linkOutput = new LinkOutputImpl();
        if (classDoc.isIncluded()) {
            if (configuration.isGeneratedDoc(classDoc)) {
                String filename = pathString(classLinkInfo);
                if (linkInfo.linkToSelf ||
                		!(linkInfo.classDoc.name() + ".html").equals(m_writer.filename)) {
	                linkOutput.append(m_writer.getHyperLink(filename,
	                    classLinkInfo.where, label.toString(),
	                    classLinkInfo.isBold, classLinkInfo.styleName,
	                    title, classLinkInfo.target));
	                if (noLabel && !classLinkInfo.excludeTypeParameterLinks) {
	                    linkOutput.append(getTypeParameterLinks(linkInfo).toString());
	                }
	                return linkOutput;
                }
            }
        } else {
            String crossLink = m_writer.getCrossClassLink(
                classDoc.qualifiedName(), classLinkInfo.where,
                label.toString(), classLinkInfo.isBold, classLinkInfo.styleName,
                true);
            if (crossLink != null) {
                linkOutput.append(crossLink);
                if (noLabel && !classLinkInfo.excludeTypeParameterLinks) {
                    linkOutput.append(getTypeParameterLinks(linkInfo).toString());
                }
                return linkOutput;
            }
        }
        // Can't link so just write label.
        linkOutput.append(label.toString());
        if (noLabel && !classLinkInfo.excludeTypeParameterLinks) {
            linkOutput.append(getTypeParameterLinks(linkInfo).toString());
        }
        return linkOutput;
    }

    /**
     * {@inheritDoc}
     */
    protected LinkOutput getTypeParameterLink(LinkInfo linkInfo,
        Type typeParam) {
        LinkInfoImpl typeLinkInfo = new LinkInfoImpl(linkInfo.getContext(),
            typeParam);
        typeLinkInfo.excludeTypeBounds = linkInfo.excludeTypeBounds;
        typeLinkInfo.excludeTypeParameterLinks = linkInfo.excludeTypeParameterLinks;
        typeLinkInfo.linkToSelf = linkInfo.linkToSelf;
        LinkOutput output = getLinkOutput(typeLinkInfo);
        ((LinkInfoImpl) linkInfo).displayLength += typeLinkInfo.displayLength;
        return output;
    }

    /**
     * Given a class, return the appropriate tool tip.
     *
     * @param classDoc the class to get the tool tip for.
     * @return the tool tip for the appropriate class.
     */
    private String getClassToolTip(ClassDoc classDoc, boolean isTypeLink) {
        Configuration configuration = ConfigurationImpl.getInstance();
        if (isTypeLink) {
            return configuration.getText("doclet.Href_Type_Param_Title",
            classDoc.name());
        } else if (classDoc.isInterface()){
            return configuration.getText("doclet.Href_Interface_Title",
                Util.getPackageName(classDoc.containingPackage()));
        } else if (classDoc.isAnnotationType()) {
            return configuration.getText("doclet.Href_Annotation_Title",
                Util.getPackageName(classDoc.containingPackage()));
        } else if (classDoc.isEnum()) {
            return configuration.getText("doclet.Href_Enum_Title",
                Util.getPackageName(classDoc.containingPackage()));
        } else {
            return configuration.getText("doclet.Href_Class_Title",
                Util.getPackageName(classDoc.containingPackage()));
        }
    }

    /**
     * Return path to the given file name in the given package. So if the name
     * passed is "Object.html" and the name of the package is "java.lang", and
     * if the relative path is "../.." then returned string will be
     * "../../java/lang/Object.html"
     *
     * @param linkInfo the information about the link.
     * @param fileName the file name, to which path string is.
     */
    private String pathString(LinkInfoImpl linkInfo) {
        if (linkInfo.context == LinkInfoImpl.PACKAGE_FRAME) {
            //Not really necessary to do this but we want to be consistent
            //with 1.4.2 output.
            return linkInfo.classDoc.name() + ".html";
        }
        StringBuffer buf = new StringBuffer(m_writer.relativePath);
        buf.append(DirectoryManager.getPathToPackage(
            linkInfo.classDoc.containingPackage(),
            linkInfo.classDoc.name() + ".html"));
        return buf.toString();
    }
}

