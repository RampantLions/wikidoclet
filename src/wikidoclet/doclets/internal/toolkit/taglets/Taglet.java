/*
 * @(#)Taglet.java	1.5 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wikidoclet.doclets.internal.toolkit.taglets;

import com.sun.javadoc.*;

/**
 * The interface for a custom tag used by Doclets. A custom
 * tag must implement this interface.  To be loaded and used by
 * doclets at run-time, the taglet must have a static method called
 * <code>register</code> that accepts a {@link java.util.Map} as an
 * argument with the following signature:
 * <pre>
 *   public void register(Map map)
 * </pre>
 * This method should add an instance of the custom taglet to the map
 * with the name of the taglet as the key.  If overriding a taglet,
 * to avoid a name conflict, the overridden taglet must be deleted from
 * the map before an instance of the new taglet is added to the map.
 * <p>
 * It is recommended that the taglet throw an exception when it fails
 * to register itself.  The exception that it throws is up to the user.
 * <p>
 * Here are two sample taglets: <br>
 * <ul>
 *     <li><a href="{@docRoot}/ToDoTaglet.java">ToDoTaglet.java</a>
 *         - Standalone taglet</li>
 *     <li><a href="{@docRoot}/UnderlineTaglet.java">UnderlineTaglet.java</a>
 *         - Inline taglet</li>
 * </ul>
 * <p>
 * For more information on how to create your own Taglets, please see the
 * <a href="{@docRoot}/overview.html">Taglet Overview</a>.
 *
 * @since 1.4
 * @author Jamie Ho
 */

public interface Taglet {

    /**
     * Return true if this <code>Taglet</code>
     * is used in field documentation.
     * @return true if this <code>Taglet</code>
     * is used in field documentation and false
     * otherwise.
     */
    public abstract boolean inField();

    /**
     * Return true if this <code>Taglet</code>
     * is used in constructor documentation.
     * @return true if this <code>Taglet</code>
     * is used in constructor documentation and false
     * otherwise.
     */
    public abstract boolean inConstructor();

    /**
     * Return true if this <code>Taglet</code>
     * is used in method documentation.
     * @return true if this <code>Taglet</code>
     * is used in method documentation and false
     * otherwise.
     */
    public abstract boolean inMethod();

    /**
     * Return true if this <code>Taglet</code>
     * is used in overview documentation.
     * @return true if this <code>Taglet</code>
     * is used in method documentation and false
     * otherwise.
     */
    public abstract boolean inOverview();

    /**
     * Return true if this <code>Taglet</code>
     * is used in package documentation.
     * @return true if this <code>Taglet</code>
     * is used in package documentation and false
     * otherwise.
     */
    public abstract boolean inPackage();

    /**
     * Return true if this <code>Taglet</code>
     * is used in type documentation (classes or
     * interfaces).
     * @return true if this <code>Taglet</code>
     * is used in type documentation and false
     * otherwise.
     */
    public abstract boolean inType();

    /**
     * Return true if this <code>Taglet</code>
     * is an inline tag. Return false otherwise.
     * @return true if this <code>Taglet</code>
     * is an inline tag and false otherwise.
     */
    public abstract boolean isInlineTag();

    /**
     * Return the name of this custom tag.
     * @return the name of this custom tag.
     */
    public abstract String getName();

    /**
     * Given the <code>Tag</code> representation of this custom
     * tag, return its TagletOutput representation, which is output
     * to the generated page.
     * @param tag the <code>Tag</code> representation of this custom tag.
     * @param writer a {@link TagletWriter} Taglet writer.
     * @throws IllegalArgumentException thrown when the method is not supported by the taglet.
     * @return the TagletOutput representation of this <code>Tag</code>.
     */
    public abstract TagletOutput getTagletOutput(Tag tag, TagletWriter writer) throws IllegalArgumentException;

    /**
     * Given a <code>Doc</code> object, check if it holds any tags of
     * this type.  If it does, return the string representing the output.
     * If it does not, return null.
     * @param holder a {@link Doc} object holding the custom tag.
     * @param writer a {@link TagletWriter} Taglet writer.
     * @throws IllegalArgumentException thrown when the method is not supported by the taglet.
     * @return the TagletOutput representation of this <code>Tag</code>.
     */
    public abstract TagletOutput getTagletOutput(Doc holder, TagletWriter writer) throws IllegalArgumentException;

}

