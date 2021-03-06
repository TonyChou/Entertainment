// HTMLParser Library - A java-based parser for HTML
// http://htmlparser.org
// Copyright (C) 2006 Somik Raha
//
// Revision Control Information
//
// $URL: svn://svn.code.sf.net/p/htmlparser/code/trunk/parser/src/main/java/org/htmlparser/tags/BaseHrefTag.java $
// $Author: derrickoswald $
// $Date: 2011-04-25 17:39:12 +0800 (一, 25  4 2011) $
// $Revision: 74 $
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the Common Public License; either
// version 1.0 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// Common Public License for more details.
//
// You should have received a copy of the Common Public License
// along with this library; if not, the license is available from
// the Open Source Initiative (OSI) website:
//   http://opensource.org/licenses/cpl1.0.php

package org.htmlparser.tags;

import org.htmlparser.lexer.Page;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.ParserException;

/**
 * BaseHrefTag represents an &lt;Base&gt; tag.
 * It extends a basic tag by providing an accessor to the HREF attribute.
 */
public class BaseHrefTag
    extends
        TagNode
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * The set of names handled by this tag.
     */
    private static final String[] mIds = new String[] {"BASE"};

    /**
     * Create a new base tag.
     */
    public BaseHrefTag ()
    {
    }

    /**
     * Return the set of names handled by this tag.
     * @return The names to be matched that create tags of this type.
     */
    public String[] getIds ()
    {
        return (mIds);
    }

    /**
     * Get the value of the <code>HREF</code> attribute, if any.
     * @return The <code>HREF</code> value, with the leading and trailing whitespace removed, if any.
     */
    public String getBaseUrl()
    {
        String base;

        base = getAttribute ("HREF");
        if (base != null && base.length() > 0)
            base = base.trim ();
        base = (null == base) ? "" : base;
        
        return (base);
    }

    /**
     * Set the value of the <code>HREF</code> attribute.
     * @param base The new <code>HREF</code> value.
     */
    public void setBaseUrl (String base)
    {
        setAttribute ("HREF", base);
    }

    /**
     * Perform the meaning of this tag.
     * This sets the base URL to use for the rest of the page.
     * @exception ParserException If setting the base URL fails.
     */
    public void doSemanticAction () throws ParserException
    {
        Page page;
        String base;
        
        page = getPage ();
        if (null != page)
        {
            base = getBaseUrl ();
            if ((null != base) && !base.equals (""))
                page.setBaseUrl (base);
        }
    }
}
