package helper.bind;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class MarshalHelper {

    public MarshalHelper() {
    }
    private static final String XML_HEADER = "<?xml version=\"1.0\"?>\n";

    /**
     * Hork in the DTD reference.
     * Because JAXB does not create xml docs w/ DTD, and legacy xml
     * (WCTP, for example), needs the DTD ref
     * @return XML Document
     **/
    public String marshalWithDocType(String xmlDoc, String rootNode, String systemId) {
        String unprocessed = xmlDoc;
        StringBuffer processed = new StringBuffer();

        // Replace the <?xml ... ?> tag
        FindSection s = new FindSection(unprocessed, "<?", "?>");
        if (s.found() && s.getSection().startsWith("xml")) {
            // Got an existing xml definition, write a new one...
            if (s.getSectionHead() != null) {
                processed.append(s.getSectionHead());
            }
            unprocessed = s.getSectionTail();
            //log.debug("Replaced XML Header");
        } else {
            //log.debug("Insert XML Header");
            // No XML header, add one in!
        }
        processed.append(XML_HEADER);

        // Replace the <!DOCTYPE ... > tag
        s = new FindSection(unprocessed, "<!DOCTYPE", ">");
        if (s.found()) {
            // Got an existing xml definition, write a new one...
            if (s.getSectionHead() != null) {
                processed.append(s.getSectionHead());
            }
            unprocessed = s.getSectionTail();
            //log.debug("Replaced DOCTYPE");
        } else {
            // No DOCTYPE, add one in!
            //log.debug("Inserted DOCTYPE");
        }
        processed.append("<!DOCTYPE ");
        processed.append(rootNode);
        processed.append(" SYSTEM \"");
        processed.append(systemId);
        processed.append("\">");

        // Add the rest of the stream in...
        processed.append(unprocessed);

        return processed.toString();
    }

    private class FindSection {

        private int start = -1;
        private int end = -1;
        private int contentStart = -1;
        private int contentEnd = -1;
        private String section = null;
        private String fullSection = null;
        private String source = null;
        private String head = null;
        private String tail = null;

        /** Creates a new instance of FindSection
         * @param source Text to be searched
         * @param startStr Start phrase to look for
         * @param endStr End phrase to look for
         */
        public FindSection(String source, String startStr, String endStr) {
            if (source == null || startStr == null || endStr == null) {
                throw new IllegalArgumentException("A null Value is not a valid argument to the FindSection routine");
            }
            this.source = source;
            start = source.indexOf(startStr);
            if (start != -1) {
                contentStart = start + startStr.length();
                contentEnd = source.indexOf(endStr, contentStart);
                if (found()) {
                    end = contentEnd + endStr.length();
                    section = source.substring(contentStart, contentEnd);
                    fullSection = startStr + section + endStr;
                }
            }

            // Calculate head and tail.
            if (!found()) {
                head = source;
            } else {
                head = source.substring(0, start);
            }
            if (!found()) {
                tail = null;
            } else {
                tail = source.substring(end);
            }

        }

        /** Was the section found in source
         * @return true if found
         */
        public boolean found() {
            return contentEnd != -1;
        }

        /** Get start position of section. This is the location where the 'start string' is found.
         * @return start postion of section, returns -1 if section not found
         */
        public int getStartPos() {
            return start;
        }

        /** Get end position of section. This is the location after the 'end' string's location.
         * @return end postion of section, returns -1 if section not found
         */
        public int getEndPos() {
            return end;
        }

        /** Get the found section, null if not found
         * @return The section text excluding the start and end strings
         */
        public String getSection() {
            return section;
        }

        /** Get the found section, null if not found
         * @return The section text including the start and end strings
         */
        public String getFullSection() {
            return fullSection;
        }

        /** This returns the text that preceeds the found section.
         * If there is no section found, this returns the source string.
         *
         * Note: getSectionHead() + getFullSection() + getSectionTail() == source
         * @return Header string, null if the section was the first thing in the source string
         */
        public String getSectionHead() {
            return head;
        }

        /** This returns the text that follows the found section.
         * If there is no section found, this returns null.
         *
         * Note: getSectionHead() + getFullSection() + getSectionTail() == source
         * @return Tail string, null if the section was not found or the last thing in the source string
         */
        public String getSectionTail() {
            return tail;
        }
    }
}
