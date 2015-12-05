package helper.util;

import play.test.*;
import org.junit.*;

/**
 * Confidential Information.
 * Copyright (C) 2003, 2004, 2005, 2006 Eric Link, All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 **/
public class RegexHelperTest extends UnitTest {

    @Test
    public void testReplaceNonWordWithUnderScore() {
        String in = null;
        in = "OK";
        assertEquals("OK", RegexHelper.replaceNonWordWithUnderScore(in));
        in = "This is a filename with spaces";
        assertEquals("This_is_a_filename_with_spaces", RegexHelper.replaceNonWordWithUnderScore(in));
        in = "OK.Yes.It.Is";
        assertEquals("OK_Yes_It_Is", RegexHelper.replaceNonWordWithUnderScore(in));
        in = " OK ";
        assertEquals("_OK_", RegexHelper.replaceNonWordWithUnderScore(in));
        in = "O?K";
        assertEquals("O_K", RegexHelper.replaceNonWordWithUnderScore(in));
    }
}
