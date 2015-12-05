package helper.util;

import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class RegexHelper {

    private static Logger logger = Logger.getLogger(RegexHelper.class.getName());
    private static Pattern PATTERN_FIND_ALL_NON_WORD = Pattern.compile("[^\\w]");

    /**
     * Escape the filenames, any non-word characters will be
     * replaced by an underscore.
     * @param string the string to untaint
     * @return string w/ non word characters replaced with underscore
     */
    public static String replaceNonWordWithUnderScore(String string) {
        return PATTERN_FIND_ALL_NON_WORD.matcher(string).replaceAll("_");
    }
}
