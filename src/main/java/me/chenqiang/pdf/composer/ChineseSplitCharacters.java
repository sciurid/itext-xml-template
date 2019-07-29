package me.chenqiang.pdf.composer;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.font.otf.GlyphLine;
import com.itextpdf.layout.splitting.ISplitCharacters;

/**
 * @author Lancelot
 * 
 *         Acknowlegement: Lee_Fannie
 *         https://blog.csdn.net/Lee_fannie/article/details/51322148
 */
public class ChineseSplitCharacters implements ISplitCharacters {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChineseSplitCharacters.class);

	// line of text cannot start or end with this character
	protected static final char u2060 = '\u2060'; // - ZERO WIDTH NO BREAK SPACE

	// a line of text cannot start with any following characters
	protected static final char u30fb = '\u30fb'; // ・ - KATAKANA MIDDLE DOT
	protected static final char u2022 = '\u2022'; // • - BLACK SMALL CIRCLE (BULLET)
	protected static final char uff65 = '\uff65'; // ･ - HALFWIDTH KATAKANA MIDDLE DOT
	protected static final char u300d = '\u300d'; // 」 - RIGHT CORNER BRACKET
	protected static final char uff09 = '\uff09'; // ） - FULLWIDTH RIGHT PARENTHESIS
	protected static final char u0021 = '\u0021'; // ! - EXCLAMATION MARK
	protected static final char u0025 = '\u0025'; // % - PERCENT SIGN
	protected static final char u0029 = '\u0029'; // ) - RIGHT PARENTHESIS
	protected static final char u002c = '\u002c'; // , - COMMA
	protected static final char u002e = '\u002e'; // . - FULL STOP
	protected static final char u003f = '\u003f'; // ? - QUESTION MARK
	protected static final char u005d = '\u005d'; // ] - RIGHT SQUARE BRACKET
	protected static final char u007d = '\u007d'; // } - RIGHT CURLY BRACKET
	protected static final char uff61 = '\uff61'; // ｡ - HALFWIDTH IDEOGRAPHIC FULL STOP

	protected static final char uff70 = '\uff70'; // ｰ - HALFWIDTH KATAKANA-HIRAGANA PROLONGED SOUND MARK
	protected static final char uff9e = '\uff9e'; // ﾞ - HALFWIDTH KATAKANA VOICED SOUND MARK
	protected static final char uff9f = '\uff9f'; // ﾟ - HALFWIDTH KATAKANA SEMI-VOICED SOUND MARK
	protected static final char u3001 = '\u3001'; // 、 - IDEOGRAPHIC COMMA
	protected static final char u3002 = '\u3002'; // 。 - IDEOGRAPHIC FULL STOP
	protected static final char uff0c = '\uff0c'; // ， - FULLWIDTH COMMA
	protected static final char uff0e = '\uff0e'; // ． - FULLWIDTH FULL STOP
	protected static final char uff1a = '\uff1a'; // ： - FULLWIDTH COLON
	protected static final char uff1b = '\uff1b'; // ； - FULLWIDTH SEMICOLON
	protected static final char uff1f = '\uff1f'; // ？ - FULLWIDTH QUESTION MARK
	protected static final char uff01 = '\uff01'; // ！ - FULLWIDTH EXCLAMATION MARK
	protected static final char u309b = '\u309b'; // ゛ - KATAKANA-HIRAGANA VOICED SOUND MARK
	protected static final char u309c = '\u309c'; // ゜ - KATAKANA-HIRAGANA SEMI-VOICED SOUND MARK
	protected static final char u30fd = '\u30fd'; // ヽ - KATAKANA ITERATION MARK

	protected static final char u2019 = '\u2019'; // ’ - RIGHT SINGLE QUOTATION MARK
	protected static final char u201d = '\u201d'; // ” - RIGHT DOUBLE QUOTATION MARK
	protected static final char u3015 = '\u3015'; // 〕 - RIGHT TORTOISE SHELL BRACKET
	protected static final char uff3d = '\uff3d'; // ］ - FULLWIDTH RIGHT SQUARE BRACKET
	protected static final char uff5d = '\uff5d'; // ｝ - FULLWIDTH RIGHT CURLY BRACKET
	protected static final char u3009 = '\u3009'; // 〉 - RIGHT ANGLE BRACKET
	protected static final char u300b = '\u300b'; // 》 - RIGHT DOUBLE ANGLE BRACKET
	protected static final char u300f = '\u300f'; // 』 - RIGHT WHITE CORNER BRACKET
	protected static final char u3011 = '\u3011'; // 】 - RIGHT BLACK LENTICULAR BRACKET
	protected static final char u00b0 = '\u00b0'; // ° - DEGREE SIGN
	protected static final char u2032 = '\u2032'; // ′ - PRIME
	protected static final char u2033 = '\u2033'; // ″ - DOUBLE PRIME

	// a line of text cannot end with any following characters
	protected static final char u0024 = '\u0024'; // $ - DOLLAR SIGN
	protected static final char u0028 = '\u0028'; // ( - LEFT PARENTHESIS
	protected static final char u005b = '\u005b'; // [ - LEFT SQUARE BRACKET
	protected static final char u007b = '\u007b'; // { - LEFT CURLY BRACKET
	protected static final char u00a3 = '\u00a3'; // £ - POUND SIGN
	protected static final char u00a5 = '\u00a5'; // ¥ - YEN SIGN
	protected static final char u201c = '\u201c'; // “ - LEFT DOUBLE QUOTATION MARK
	protected static final char u2018 = '\u2018'; // ‘ - LEFT SINGLE QUOTATION MARK
	protected static final char u300a = '\u300a'; // 《 - LEFT DOUBLE ANGLE BRACKET
	protected static final char u3008 = '\u3008'; // 〈 - LEFT ANGLE BRACKET
	protected static final char u300c = '\u300c'; // 「 - LEFT CORNER BRACKET
	protected static final char u300e = '\u300e'; // 『 - LEFT WHITE CORNER BRACKET
	protected static final char u3010 = '\u3010'; // 【 - LEFT BLACK LENTICULAR BRACKET
	protected static final char u3014 = '\u3014'; // 〔 - LEFT TORTOISE SHELL BRACKET
	protected static final char uff62 = '\uff62'; // ｢ - HALFWIDTH LEFT CORNER BRACKET
	protected static final char uff08 = '\uff08'; // （ - FULLWIDTH LEFT PARENTHESIS
	protected static final char uff3b = '\uff3b'; // ［ - FULLWIDTH LEFT SQUARE BRACKET
	protected static final char uff5b = '\uff5b'; // ｛ - FULLWIDTH LEFT CURLY BRACKET
	protected static final char uffe5 = '\uffe5'; // ￥ - FULLWIDTH YEN SIGN
	protected static final char uff04 = '\uff04'; // ＄ - FULLWIDTH DOLLAR SIGN

	protected static final Set<Character> NO_BEGINNING = Set.of(u30fb, u2022, uff65, u300d, uff09, u0021, u0025, u0029, u002c,
			u002e, u003f, u005d, u007d, uff61, uff70, uff9e, uff9f, u3001, u3002, uff0c, uff0e, uff1a, uff1b, uff1f,
			uff01, u309b, u309c, u30fd, u2019, u201d, u3015, uff3d, uff5d, u3009, u300b, u300f, u3011, u00b0, u2032,
			u2033, u2060);

	protected static final Set<Character> NO_ENDING = Set.of(u0024, u0028, u005b, u007b, u00a3, u00a5, u201c, u2018, u3008, u300a,
			u300c, u300e, u3010, u3014, uff62, uff08, uff3b, uff5b, uffe5, uff04, u2060);

	@Override
	public boolean isSplitCharacter(GlyphLine text, int glyphPos) {
		// At the last glyph of line.
		if (glyphPos == text.size() - 1) {
			return true;
		}
		
		char [] current = text.get(glyphPos).getUnicodeChars();
		char [] next = text.get(glyphPos + 1).getUnicodeChars();
		
		char lastCurrent = current[current.length - 1];
		char firstNext = next[0];
		if(NO_BEGINNING.contains(firstNext) || NO_ENDING.contains(lastCurrent)) {
			LOGGER.debug("{}|{}", lastCurrent, firstNext);
			return false;
		}
		
		return true;
	}

}
