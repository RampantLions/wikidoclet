/*
 * @(#)DeprecatedAPIListBuilder.java	1.19 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.internal.toolkit.util;

import com.sun.javadoc.*;
import java.util.*;

/**
 * Build list of all the deprecated classes, constructors, fields and methods.
 *
 * @author Atul M Dambalkar
 */
public class DeprecatedAPIListBuilder {

    public static final int NUM_TYPES = 11;

    public static final int INTERFACE = 0;
    public static final int CLASS = 1;
    public static final int ENUM = 2;
    public static final int EXCEPTION = 3;
    public static final int ERROR = 4;
    public static final int ANNOTATION_TYPE = 5;
    public static final int FIELD = 6;
    public static final int METHOD = 7;
    public static final int CONSTRUCTOR = 8;
    public static final int ENUM_CONSTANT = 9;
    public static final int ANNOTATION_TYPE_MEMBER = 10;

    /**
     * List of deprecated type Lists.
     */
    private List deprecatedLists;


    /**
     * Constructor.
     *
     * @param root Root of the tree.
     */
    public DeprecatedAPIListBuilder(RootDoc root) {
        deprecatedLists = new ArrayList();
        for (int i = 0; i < NUM_TYPES; i++) {
            deprecatedLists.add(i, new ArrayList());
        }
        buildDeprecatedAPIInfo(root);
    }

    /**
     * Build the sorted list of all the deprecated APIs in this run.
     * Build separate lists for deprecated classes, constructors, methods and
     * fields.
     *
     * @param root Root of the tree.
     */
    private void buildDeprecatedAPIInfo(RootDoc root) {
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; i++) {
            ClassDoc cd = classes[i];
            if (Util.isDeprecated(cd)) {
                if (cd.isOrdinaryClass()) {
                    getList(CLASS).add(cd);
                } else if (cd.isInterface()) {
                    getList(INTERFACE).add(cd);
                } else if (cd.isException()) {
                    getList(EXCEPTION).add(cd);
                } else if (cd.isEnum()) {
                    getList(ENUM).add(cd);
                } else if (cd.isError()) {
                    getList(ERROR).add(cd);
                }else if (cd.isAnnotationType()) {
                    getList(ANNOTATION_TYPE).add(cd);
                }
            }
            composeDeprecatedList(getList(FIELD), cd.fields());
            composeDeprecatedList(getList(METHOD), cd.methods());
            composeDeprecatedList(getList(CONSTRUCTOR), cd.constructors());
            if (cd.isEnum()) {
                composeDeprecatedList(getList(ENUM_CONSTANT), cd.enumConstants());
            }
            if (cd.isAnnotationType()) {
                composeDeprecatedList(getList(ANNOTATION_TYPE_MEMBER),
                    ((AnnotationTypeDoc) cd).elements());
            }
        }
        sortDeprecatedLists();
    }

    /**
     * Add the members into a single list of deprecated members.
     *
     * @param list List of all the particular deprecated members, e.g. methods.
     * @param members members to be added in the list.
     */
    private void composeDeprecatedList(List list, MemberDoc[] members) {
        for (int i = 0; i < members.length; i++) {
            if (Util.isDeprecated(members[i])) {
                list.add(members[i]);
            }
        }
    }

    /**
     * Sort the deprecated lists for class kinds, fields, methods and
     * constructors.
     */
    private void sortDeprecatedLists() {
        for (int i = 0; i < NUM_TYPES; i++) {
            Collections.sort(getList(i));
        }
    }

    /**
     * Return the list of deprecated Doc objects of a given type.
     *
     * @param the constant representing the type of list being returned.
     */
    public List getList(int type) {
        return (List) deprecatedLists.get(type);
    }

    /**
     * Return true if the list of a given type has size greater than 0.
     *
     * @param type the type of list being checked.
     */
    public boolean hasDocumentation(int type) {
        return ((List) deprecatedLists.get(type)).size() > 0;
    }
}
