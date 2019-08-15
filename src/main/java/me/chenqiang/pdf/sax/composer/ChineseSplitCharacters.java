package me.chenqiang.pdf.sax.composer;

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
	protected static final char U2060 = '\u2060'; // - ZERO WIDTH NO BREAK SPACE

	// a line of text cannot start with any following characters
	protected static final char U30FB = '\u30fb'; // ・ - KATAKANA MIDDLE DOT
	protected static final char U2022 = '\u2022'; // • - BLACK SMALL CIRCLE (BULLET)
	protected static final char UFF65 = '\uff65'; // ･ - HALFWIDTH KATAKANA MIDDLE DOT
	protected static final char U300D = '\u300d'; // 」 - RIGHT CORNER BRACKET
	protected static final char UFF09 = '\uff09'; // ） - FULLWIDTH RIGHT PARENTHESIS
	protected static final char U0021 = '\u0021'; // ! - EXCLAMATION MARK
	protected static final char U0025 = '\u0025'; // % - PERCENT SIGN
	protected static final char U0029 = '\u0029'; // ) - RIGHT PARENTHESIS
	protected static final char U002C = '\u002c'; // , - COMMA
	protected static final char U002E = '\u002e'; // . - FULL STOP
	protected static final char U003F = '\u003f'; // ? - QUESTION MARK
	protected static final char U005D = '\u005d'; // ] - RIGHT SQUARE BRACKET
	protected static final char U007D = '\u007d'; // } - RIGHT CURLY BRACKET
	protected static final char UFF61 = '\uff61'; // ｡ - HALFWIDTH IDEOGRAPHIC FULL STOP

	protected static final char UFF70 = '\uff70'; // ｰ - HALFWIDTH KATAKANA-HIRAGANA PROLONGED SOUND MARK
	protected static final char UFF9E = '\uff9e'; // ﾞ - HALFWIDTH KATAKANA VOICED SOUND MARK
	protected static final char UFF9F = '\uff9f'; // ﾟ - HALFWIDTH KATAKANA SEMI-VOICED SOUND MARK
	protected static final char U3001 = '\u3001'; // 、 - IDEOGRAPHIC COMMA
	protected static final char U3002 = '\u3002'; // 。 - IDEOGRAPHIC FULL STOP
	protected static final char UFF0C = '\uff0c'; // ， - FULLWIDTH COMMA
	protected static final char UFF0E = '\uff0e'; // ． - FULLWIDTH FULL STOP
	protected static final char UFF1A = '\uff1a'; // ： - FULLWIDTH COLON
	protected static final char UFF1B = '\uff1b'; // ； - FULLWIDTH SEMICOLON
	protected static final char UFF1F = '\uff1f'; // ？ - FULLWIDTH QUESTION MARK
	protected static final char UFF01 = '\uff01'; // ！ - FULLWIDTH EXCLAMATION MARK
	protected static final char U309B = '\u309b'; // ゛ - KATAKANA-HIRAGANA VOICED SOUND MARK
	protected static final char U309C = '\u309c'; // ゜ - KATAKANA-HIRAGANA SEMI-VOICED SOUND MARK
	protected static final char U30FD = '\u30fd'; // ヽ - KATAKANA ITERATION MARK

	protected static final char U2019 = '\u2019'; // ’ - RIGHT SINGLE QUOTATION MARK
	protected static final char U201D = '\u201d'; // ” - RIGHT DOUBLE QUOTATION MARK
	protected static final char U3015 = '\u3015'; // 〕 - RIGHT TORTOISE SHELL BRACKET
	protected static final char UFF3D = '\uff3d'; // ］ - FULLWIDTH RIGHT SQUARE BRACKET
	protected static final char UFF5D = '\uff5d'; // ｝ - FULLWIDTH RIGHT CURLY BRACKET
	protected static final char U3009 = '\u3009'; // 〉 - RIGHT ANGLE BRACKET
	protected static final char U300B = '\u300b'; // 》 - RIGHT DOUBLE ANGLE BRACKET
	protected static final char U300F = '\u300f'; // 』 - RIGHT WHITE CORNER BRACKET
	protected static final char U3011 = '\u3011'; // 】 - RIGHT BLACK LENTICULAR BRACKET
	protected static final char U00B0 = '\u00b0'; // ° - DEGREE SIGN
	protected static final char U2032 = '\u2032'; // ′ - PRIME
	protected static final char U2033 = '\u2033'; // ″ - DOUBLE PRIME

	// a line of text cannot end with any following characters
	protected static final char U0024 = '\u0024'; // $ - DOLLAR SIGN
	protected static final char U0028 = '\u0028'; // ( - LEFT PARENTHESIS
	protected static final char U005B = '\u005b'; // [ - LEFT SQUARE BRACKET
	protected static final char U007B = '\u007b'; // { - LEFT CURLY BRACKET
	protected static final char U00A3 = '\u00a3'; // £ - POUND SIGN
	protected static final char U00A5 = '\u00a5'; // ¥ - YEN SIGN
	protected static final char U201C = '\u201c'; // “ - LEFT DOUBLE QUOTATION MARK
	protected static final char U2018 = '\u2018'; // ‘ - LEFT SINGLE QUOTATION MARK
	protected static final char U300A = '\u300a'; // 《 - LEFT DOUBLE ANGLE BRACKET
	protected static final char U3008 = '\u3008'; // 〈 - LEFT ANGLE BRACKET
	protected static final char U300C = '\u300c'; // 「 - LEFT CORNER BRACKET
	protected static final char U300E = '\u300e'; // 『 - LEFT WHITE CORNER BRACKET
	protected static final char U3010 = '\u3010'; // 【 - LEFT BLACK LENTICULAR BRACKET
	protected static final char U3014 = '\u3014'; // 〔 - LEFT TORTOISE SHELL BRACKET
	protected static final char UFF62 = '\uff62'; // ｢ - HALFWIDTH LEFT CORNER BRACKET
	protected static final char UFF08 = '\uff08'; // （ - FULLWIDTH LEFT PARENTHESIS
	protected static final char UFF3B = '\uff3b'; // ［ - FULLWIDTH LEFT SQUARE BRACKET
	protected static final char UFF5B = '\uff5b'; // ｛ - FULLWIDTH LEFT CURLY BRACKET
	protected static final char UFFE5 = '\uffe5'; // ￥ - FULLWIDTH YEN SIGN
	protected static final char UFF04 = '\uff04'; // ＄ - FULLWIDTH DOLLAR SIGN

	protected static final Set<Character> NO_BEGINNING = Set.of(U30FB, U2022, UFF65, U300D, UFF09, U0021, U0025, U0029, U002C,
			U002E, U003F, U005D, U007D, UFF61, UFF70, UFF9E, UFF9F, U3001, U3002, UFF0C, UFF0E, UFF1A, UFF1B, UFF1F,
			UFF01, U309B, U309C, U30FD, U2019, U201D, U3015, UFF3D, UFF5D, U3009, U300B, U300F, U3011, U00B0, U2032,
			U2033, U2060);

	protected static final Set<Character> NO_ENDING = Set.of(U0024, U0028, U005B, U007B, U00A3, U00A5, U201C, U2018, U3008, U300A,
			U300C, U300E, U3010, U3014, UFF62, UFF08, UFF3B, UFF5B, UFFE5, UFF04, U2060);

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
