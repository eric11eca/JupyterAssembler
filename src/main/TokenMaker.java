package main;

import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;

public class TokenMaker extends AbstractTokenMaker {
	@Override
	public void addToken(Segment segment, int start, int end, int tokenType, int startOffset) {
		if (tokenType == Token.IDENTIFIER) {
			int value = wordsToHighlight.get(segment, start, end);
			if (value != -1) {
				tokenType = value;
			}
		}
		super.addToken(segment, start, end, tokenType, startOffset);
	}

	@Override
	public Token getTokenList(Segment text, int startTokenType, int startOffset) {
		resetTokenList();
		char[] array = text.array;
		int offset = text.offset;
		int count = text.count;
		int end = offset + count;
		int newStartOffset = startOffset - offset;
		int currentTokenStart = offset;
		int currentTokenType = startTokenType;

		for (int i = offset; i < end; i++) {
			char c = array[i];
			switch (currentTokenType) {
			case Token.NULL:
				currentTokenStart = i;
				
				switch (c) {
				case ' ':
				case '\t':
					currentTokenType = Token.WHITESPACE;
					break;

				case '"':
					currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
					break;

				case '#':
					currentTokenType = Token.COMMENT_EOL;
					break;
				
				case ':':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;		
				case 'A':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'B':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'C':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'D':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'E':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'F':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'G':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'H':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'I':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'J':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'K':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'L':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'M':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'N':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'O':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'P':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'Q':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'R':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'S':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'T':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'U':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'V':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'W':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'X':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'Y':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'Z':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
					
				default:
					if (RSyntaxUtilities.isDigit(c)) {
						currentTokenType = Token.DATA_TYPE;
						break;
					} else if (Character.isUpperCase(c)) {
						currentTokenType = Token.ERROR_IDENTIFIER;
						break;
					} else if (RSyntaxUtilities.isLetter(c) || c == '/' || c == '_') {
						currentTokenType = Token.IDENTIFIER;
						break;
					}
					currentTokenType = Token.IDENTIFIER;
					break;
				}

				break;

			case Token.WHITESPACE:
				switch (c) {

				case ' ':
				case '\t':
					break;
				case 'A':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'B':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'C':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'D':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'E':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'F':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'G':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'H':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'I':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'J':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'K':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'L':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'M':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'N':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'O':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'P':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'Q':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'R':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'S':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'T':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'U':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'V':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'W':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'X':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'Y':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case 'Z':
					currentTokenType = Token.ERROR_IDENTIFIER;
					break;
				case '"':
					addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
					currentTokenStart = i;
					currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
					break;

				case '#':
					addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
					currentTokenStart = i;
					currentTokenType = Token.COMMENT_EOL;
					break;

				default:
					addToken(text, currentTokenStart, i - 1, Token.WHITESPACE, newStartOffset + currentTokenStart);
					currentTokenStart = i;

					if (RSyntaxUtilities.isDigit(c)) {
						currentTokenType = Token.DATA_TYPE;
						break;
					} else if (RSyntaxUtilities.isLetter(c) || c == '/' || c == '_') {
						currentTokenType = Token.IDENTIFIER;
						break;
					}

					currentTokenType = Token.IDENTIFIER;
				}

				break;

			default: 
			case Token.IDENTIFIER:
				switch (c) {

				case ' ':
				case '\t':
					addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
					currentTokenStart = i;
					currentTokenType = Token.WHITESPACE;
					break;

				case '"':
					addToken(text, currentTokenStart, i - 1, Token.IDENTIFIER, newStartOffset + currentTokenStart);
					currentTokenStart = i;
					currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
					break;

				default:
					if (RSyntaxUtilities.isLetterOrDigit(c) || c == '/' || c == '_') {
						break;
					}
				} 

				break;
			
			case Token.ERROR_IDENTIFIER:
				switch (c) {		
				case ' ':
				case '\t':
					addToken(text, currentTokenStart, i - 1, Token.ERROR_IDENTIFIER,
							newStartOffset + currentTokenStart);
					currentTokenStart = i;
					currentTokenType = Token.WHITESPACE;
					break;

				case '"':
					addToken(text, currentTokenStart, i - 1, Token.ERROR_IDENTIFIER,
							newStartOffset + currentTokenStart);
					currentTokenStart = i;
					currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
					break;

				default:
					if (RSyntaxUtilities.isLetter(c))
						break; 
					addToken(text, currentTokenStart, i - 1, Token.ERROR_IDENTIFIER,
							newStartOffset + currentTokenStart);
					i--;
					currentTokenType = Token.NULL;
				}
				break;

			case Token.DATA_TYPE:
				switch (c) {		
				case ' ':
				case '\t':
					addToken(text, currentTokenStart, i - 1, Token.DATA_TYPE,
							newStartOffset + currentTokenStart);
					currentTokenStart = i;
					currentTokenType = Token.WHITESPACE;
					break;

				case '"':
					addToken(text, currentTokenStart, i - 1, Token.DATA_TYPE,
							newStartOffset + currentTokenStart);
					currentTokenStart = i;
					currentTokenType = Token.LITERAL_STRING_DOUBLE_QUOTE;
					break;

				default:
					if (RSyntaxUtilities.isDigit(c))
						break; 
					addToken(text, currentTokenStart, i - 1, Token.DATA_TYPE,
							newStartOffset + currentTokenStart);
					i--;
					currentTokenType = Token.NULL;
				}
				break;

			case Token.COMMENT_EOL:
				i = end - 1;
				addToken(text, currentTokenStart, i, currentTokenType, newStartOffset + currentTokenStart);
				currentTokenType = Token.NULL;
				break;

			case Token.LITERAL_STRING_DOUBLE_QUOTE:
				if (c == '"') {
					addToken(text, currentTokenStart, i, Token.LITERAL_STRING_DOUBLE_QUOTE,
							newStartOffset + currentTokenStart);
					currentTokenType = Token.NULL;
				}
				break;
			}
		}

		switch (currentTokenType) {
		case Token.LITERAL_STRING_DOUBLE_QUOTE:
			addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
			break;

		case Token.NULL:
			addNullToken();
			break;

		default:
			addToken(text, currentTokenStart, end - 1, currentTokenType, newStartOffset + currentTokenStart);
			addNullToken();
		}
		return firstToken;
	}

	@Override
	public TokenMap getWordsToHighlight() {
		TokenMap tokenMap = new TokenMap();
		tokenMap.put("load", Token.MARKUP_TAG_NAME);
		tokenMap.put("store", Token.MARKUP_TAG_NAME);
		tokenMap.put("add", Token.MARKUP_TAG_NAME);
		tokenMap.put("sub", Token.MARKUP_TAG_NAME);
		tokenMap.put("or", Token.MARKUP_TAG_NAME);
		tokenMap.put("and", Token.MARKUP_TAG_NAME);
		tokenMap.put("andi", Token.MARKUP_TAG_NAME);
		tokenMap.put("addi", Token.MARKUP_TAG_NAME);
		tokenMap.put("ori", Token.MARKUP_TAG_NAME);
		tokenMap.put("loadi", Token.MARKUP_TAG_NAME);
		tokenMap.put("lui", Token.MARKUP_TAG_NAME);
		tokenMap.put("j", Token.MARKUP_TAG_NAME);
		tokenMap.put("jp", Token.MARKUP_TAG_NAME);
		tokenMap.put("bz", Token.MARKUP_TAG_NAME);
		tokenMap.put("bnz", Token.MARKUP_TAG_NAME);
		tokenMap.put("return", Token.MARKUP_TAG_NAME);
		tokenMap.put("slt", Token.MARKUP_TAG_NAME);
		tokenMap.put("sl", Token.MARKUP_TAG_NAME);
		tokenMap.put("sr", Token.MARKUP_TAG_NAME);
		tokenMap.put(".", Token.RESERVED_WORD);
		tokenMap.put("sp", Token.RESERVED_WORD);
		tokenMap.put("psp", Token.RESERVED_WORD);
		tokenMap.put("banksel", Token.RESERVED_WORD);
		tokenMap.put("syscall", Token.RESERVED_WORD);
		return tokenMap;
	}
}