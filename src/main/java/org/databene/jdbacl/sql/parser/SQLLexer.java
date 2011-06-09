// $ANTLR 3.3 Nov 30, 2010 12:45:30 /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g 2011-06-08 18:35:17
 
	package org.databene.jdbacl.sql.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class SQLLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__61=61;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int T__73=73;
    public static final int T__74=74;
    public static final int T__75=75;
    public static final int T__76=76;
    public static final int T__77=77;
    public static final int T__78=78;
    public static final int T__79=79;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int T__84=84;
    public static final int T__85=85;
    public static final int T__86=86;
    public static final int T__87=87;
    public static final int T__88=88;
    public static final int T__89=89;
    public static final int T__90=90;
    public static final int T__91=91;
    public static final int T__92=92;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int T__95=95;
    public static final int T__96=96;
    public static final int T__97=97;
    public static final int T__98=98;
    public static final int T__99=99;
    public static final int T__100=100;
    public static final int T__101=101;
    public static final int T__102=102;
    public static final int T__103=103;
    public static final int T__104=104;
    public static final int T__105=105;
    public static final int T__106=106;
    public static final int T__107=107;
    public static final int T__108=108;
    public static final int T__109=109;
    public static final int T__110=110;
    public static final int T__111=111;
    public static final int T__112=112;
    public static final int T__113=113;
    public static final int T__114=114;
    public static final int T__115=115;
    public static final int T__116=116;
    public static final int T__117=117;
    public static final int T__118=118;
    public static final int T__119=119;
    public static final int T__120=120;
    public static final int T__121=121;
    public static final int T__122=122;
    public static final int T__123=123;
    public static final int T__124=124;
    public static final int T__125=125;
    public static final int T__126=126;
    public static final int T__127=127;
    public static final int T__128=128;
    public static final int T__129=129;
    public static final int T__130=130;
    public static final int T__131=131;
    public static final int T__132=132;
    public static final int T__133=133;
    public static final int T__134=134;
    public static final int T__135=135;
    public static final int T__136=136;
    public static final int T__137=137;
    public static final int INT=4;
    public static final int NOT=5;
    public static final int IDENTIFIER=6;
    public static final int STRING=7;
    public static final int QUOTED_NAME=8;
    public static final int PRIMARY=9;
    public static final int DEFAULT=10;
    public static final int COMMENT=11;
    public static final int CREATE_TABLE=12;
    public static final int DROP_TABLE=13;
    public static final int ALTER_TABLE=14;
    public static final int TABLE_NAME=15;
    public static final int COLUMN_NAME=16;
    public static final int SIZE=17;
    public static final int CREATE_SEQUENCE=18;
    public static final int DROP_SEQUENCE=19;
    public static final int NAME_LIST=20;
    public static final int CREATE_INDEX=21;
    public static final int INDEX_NAME=22;
    public static final int TABLE_DETAILS=23;
    public static final int COLUMN_SPEC=24;
    public static final int ORA_CONFIGS=25;
    public static final int COMMENT_TABLE=26;
    public static final int COMMENT_COLUMN=27;
    public static final int INVOCATION=28;
    public static final int ARGUMENTS=29;
    public static final int VALUE_LIST=30;
    public static final int OR=31;
    public static final int AND=32;
    public static final int XOR=33;
    public static final int EQ=34;
    public static final int BANGEQ=35;
    public static final int LTGT=36;
    public static final int GT=37;
    public static final int GE=38;
    public static final int LT=39;
    public static final int LE=40;
    public static final int IS=41;
    public static final int NULL=42;
    public static final int IN=43;
    public static final int BETWEEN=44;
    public static final int PLUS=45;
    public static final int SUB=46;
    public static final int STAR=47;
    public static final int SLASH=48;
    public static final int PERCENT=49;
    public static final int BARBAR=50;
    public static final int ROWID=51;
    public static final int NUMBER=52;
    public static final int VARCHAR2=53;
    public static final int NVARCHAR2=54;
    public static final int BYTE=55;
    public static final int CHAR=56;
    public static final int DATE=57;
    public static final int TIMESTAMP=58;
    public static final int BEFORE=59;
    public static final int WS=60;

    	@Override
    	public Token nextToken() {
    		while (true) {
    			state.token = null;
    			state.channel = Token.DEFAULT_CHANNEL;
    			state.tokenStartCharIndex = input.index();
    			state.tokenStartCharPositionInLine = input.getCharPositionInLine();
    			state.tokenStartLine = input.getLine();
    			state.text = null;
    			if ( input.LA(1)==CharStream.EOF ) {
    				return Token.EOF_TOKEN;
    			}
    			try {
    				mTokens();
    				if ( state.token==null ) {
    					emit();
    				}
    				else if ( state.token==Token.SKIP_TOKEN ) {
    					continue;
    				}
    				return state.token;
    			}
    			catch (RecognitionException re) {
    				reportError(re);
    				throw new RuntimeException(getClass().getSimpleName() + " error", re); // or throw Error
    			}
    		}
    	}



    // delegates
    // delegators

    public SQLLexer() {;} 
    public SQLLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public SQLLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "/Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g"; }

    // $ANTLR start "T__61"
    public final void mT__61() throws RecognitionException {
        try {
            int _type = T__61;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:38:7: ( ';' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:38:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__61"

    // $ANTLR start "T__62"
    public final void mT__62() throws RecognitionException {
        try {
            int _type = T__62;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:39:7: ( 'CREATE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:39:9: 'CREATE'
            {
            match("CREATE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__62"

    // $ANTLR start "T__63"
    public final void mT__63() throws RecognitionException {
        try {
            int _type = T__63;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:40:7: ( 'GLOBAL' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:40:9: 'GLOBAL'
            {
            match("GLOBAL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__63"

    // $ANTLR start "T__64"
    public final void mT__64() throws RecognitionException {
        try {
            int _type = T__64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:41:7: ( 'TEMPORARY' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:41:9: 'TEMPORARY'
            {
            match("TEMPORARY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__64"

    // $ANTLR start "T__65"
    public final void mT__65() throws RecognitionException {
        try {
            int _type = T__65;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:42:7: ( 'TABLE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:42:9: 'TABLE'
            {
            match("TABLE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__65"

    // $ANTLR start "T__66"
    public final void mT__66() throws RecognitionException {
        try {
            int _type = T__66;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:43:7: ( '(' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:43:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__66"

    // $ANTLR start "T__67"
    public final void mT__67() throws RecognitionException {
        try {
            int _type = T__67;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:44:7: ( ')' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:44:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__67"

    // $ANTLR start "T__68"
    public final void mT__68() throws RecognitionException {
        try {
            int _type = T__68;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:45:7: ( ',' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:45:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__68"

    // $ANTLR start "T__69"
    public final void mT__69() throws RecognitionException {
        try {
            int _type = T__69;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:46:7: ( 'NCHAR2' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:46:9: 'NCHAR2'
            {
            match("NCHAR2"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__69"

    // $ANTLR start "T__70"
    public final void mT__70() throws RecognitionException {
        try {
            int _type = T__70;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:47:7: ( 'BLOB' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:47:9: 'BLOB'
            {
            match("BLOB"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__70"

    // $ANTLR start "T__71"
    public final void mT__71() throws RecognitionException {
        try {
            int _type = T__71;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:48:7: ( 'CLOB' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:48:9: 'CLOB'
            {
            match("CLOB"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__71"

    // $ANTLR start "T__72"
    public final void mT__72() throws RecognitionException {
        try {
            int _type = T__72;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:49:7: ( 'NCLOB' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:49:9: 'NCLOB'
            {
            match("NCLOB"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__72"

    // $ANTLR start "T__73"
    public final void mT__73() throws RecognitionException {
        try {
            int _type = T__73;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:50:7: ( 'CONSTRAINT' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:50:9: 'CONSTRAINT'
            {
            match("CONSTRAINT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__73"

    // $ANTLR start "T__74"
    public final void mT__74() throws RecognitionException {
        try {
            int _type = T__74;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:51:7: ( 'KEY' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:51:9: 'KEY'
            {
            match("KEY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__74"

    // $ANTLR start "T__75"
    public final void mT__75() throws RecognitionException {
        try {
            int _type = T__75;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:52:7: ( 'ORGANIZATION' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:52:9: 'ORGANIZATION'
            {
            match("ORGANIZATION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__75"

    // $ANTLR start "T__76"
    public final void mT__76() throws RecognitionException {
        try {
            int _type = T__76;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:53:7: ( 'TABLESPACE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:53:9: 'TABLESPACE'
            {
            match("TABLESPACE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__76"

    // $ANTLR start "T__77"
    public final void mT__77() throws RecognitionException {
        try {
            int _type = T__77;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:54:7: ( 'PCTUSED' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:54:9: 'PCTUSED'
            {
            match("PCTUSED"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__77"

    // $ANTLR start "T__78"
    public final void mT__78() throws RecognitionException {
        try {
            int _type = T__78;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:55:7: ( 'PCTFREE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:55:9: 'PCTFREE'
            {
            match("PCTFREE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__78"

    // $ANTLR start "T__79"
    public final void mT__79() throws RecognitionException {
        try {
            int _type = T__79;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:56:7: ( 'INITRANS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:56:9: 'INITRANS'
            {
            match("INITRANS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__79"

    // $ANTLR start "T__80"
    public final void mT__80() throws RecognitionException {
        try {
            int _type = T__80;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:57:7: ( 'MAXTRANS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:57:9: 'MAXTRANS'
            {
            match("MAXTRANS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__80"

    // $ANTLR start "T__81"
    public final void mT__81() throws RecognitionException {
        try {
            int _type = T__81;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:58:7: ( 'COMPRESS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:58:9: 'COMPRESS'
            {
            match("COMPRESS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__81"

    // $ANTLR start "T__82"
    public final void mT__82() throws RecognitionException {
        try {
            int _type = T__82;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:59:7: ( 'ENABLE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:59:9: 'ENABLE'
            {
            match("ENABLE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__82"

    // $ANTLR start "T__83"
    public final void mT__83() throws RecognitionException {
        try {
            int _type = T__83;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:60:7: ( 'ROW' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:60:9: 'ROW'
            {
            match("ROW"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__83"

    // $ANTLR start "T__84"
    public final void mT__84() throws RecognitionException {
        try {
            int _type = T__84;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:61:7: ( 'MOVEMENT' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:61:9: 'MOVEMENT'
            {
            match("MOVEMENT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__84"

    // $ANTLR start "T__85"
    public final void mT__85() throws RecognitionException {
        try {
            int _type = T__85;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:62:7: ( 'DISABLE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:62:9: 'DISABLE'
            {
            match("DISABLE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__85"

    // $ANTLR start "T__86"
    public final void mT__86() throws RecognitionException {
        try {
            int _type = T__86;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:63:7: ( 'ON' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:63:9: 'ON'
            {
            match("ON"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__86"

    // $ANTLR start "T__87"
    public final void mT__87() throws RecognitionException {
        try {
            int _type = T__87;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:64:7: ( 'COMMIT' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:64:9: 'COMMIT'
            {
            match("COMMIT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__87"

    // $ANTLR start "T__88"
    public final void mT__88() throws RecognitionException {
        try {
            int _type = T__88;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:65:7: ( 'DELETE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:65:9: 'DELETE'
            {
            match("DELETE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__88"

    // $ANTLR start "T__89"
    public final void mT__89() throws RecognitionException {
        try {
            int _type = T__89;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:66:7: ( 'ROWS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:66:9: 'ROWS'
            {
            match("ROWS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__89"

    // $ANTLR start "T__90"
    public final void mT__90() throws RecognitionException {
        try {
            int _type = T__90;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:67:7: ( 'STORAGE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:67:9: 'STORAGE'
            {
            match("STORAGE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__90"

    // $ANTLR start "T__91"
    public final void mT__91() throws RecognitionException {
        try {
            int _type = T__91;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:68:7: ( 'INITIAL' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:68:9: 'INITIAL'
            {
            match("INITIAL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__91"

    // $ANTLR start "T__92"
    public final void mT__92() throws RecognitionException {
        try {
            int _type = T__92;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:69:7: ( 'NEXT' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:69:9: 'NEXT'
            {
            match("NEXT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__92"

    // $ANTLR start "T__93"
    public final void mT__93() throws RecognitionException {
        try {
            int _type = T__93;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:70:7: ( 'MINEXTENTS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:70:9: 'MINEXTENTS'
            {
            match("MINEXTENTS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__93"

    // $ANTLR start "T__94"
    public final void mT__94() throws RecognitionException {
        try {
            int _type = T__94;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:71:7: ( 'MAXEXTENTS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:71:9: 'MAXEXTENTS'
            {
            match("MAXEXTENTS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__94"

    // $ANTLR start "T__95"
    public final void mT__95() throws RecognitionException {
        try {
            int _type = T__95;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:72:7: ( 'PCTINCREASE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:72:9: 'PCTINCREASE'
            {
            match("PCTINCREASE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__95"

    // $ANTLR start "T__96"
    public final void mT__96() throws RecognitionException {
        try {
            int _type = T__96;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:73:7: ( 'BUFFER_POOL' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:73:9: 'BUFFER_POOL'
            {
            match("BUFFER_POOL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__96"

    // $ANTLR start "T__97"
    public final void mT__97() throws RecognitionException {
        try {
            int _type = T__97;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:74:7: ( 'PARTITION' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:74:9: 'PARTITION'
            {
            match("PARTITION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__97"

    // $ANTLR start "T__98"
    public final void mT__98() throws RecognitionException {
        try {
            int _type = T__98;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:75:7: ( 'BY' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:75:9: 'BY'
            {
            match("BY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__98"

    // $ANTLR start "T__99"
    public final void mT__99() throws RecognitionException {
        try {
            int _type = T__99;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:76:7: ( 'RANGE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:76:9: 'RANGE'
            {
            match("RANGE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__99"

    // $ANTLR start "T__100"
    public final void mT__100() throws RecognitionException {
        try {
            int _type = T__100;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:77:8: ( 'HASH' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:77:10: 'HASH'
            {
            match("HASH"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__100"

    // $ANTLR start "T__101"
    public final void mT__101() throws RecognitionException {
        try {
            int _type = T__101;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:78:8: ( 'LIST' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:78:10: 'LIST'
            {
            match("LIST"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__101"

    // $ANTLR start "T__102"
    public final void mT__102() throws RecognitionException {
        try {
            int _type = T__102;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:79:8: ( 'SUBPARTITION' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:79:10: 'SUBPARTITION'
            {
            match("SUBPARTITION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__102"

    // $ANTLR start "T__103"
    public final void mT__103() throws RecognitionException {
        try {
            int _type = T__103;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:80:8: ( 'PARTITIONS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:80:10: 'PARTITIONS'
            {
            match("PARTITIONS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__103"

    // $ANTLR start "T__104"
    public final void mT__104() throws RecognitionException {
        try {
            int _type = T__104;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:81:8: ( 'STORE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:81:10: 'STORE'
            {
            match("STORE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__104"

    // $ANTLR start "T__105"
    public final void mT__105() throws RecognitionException {
        try {
            int _type = T__105;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:82:8: ( 'VALUES' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:82:10: 'VALUES'
            {
            match("VALUES"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__105"

    // $ANTLR start "T__106"
    public final void mT__106() throws RecognitionException {
        try {
            int _type = T__106;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:83:8: ( 'LESS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:83:10: 'LESS'
            {
            match("LESS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__106"

    // $ANTLR start "T__107"
    public final void mT__107() throws RecognitionException {
        try {
            int _type = T__107;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:84:8: ( 'THAN' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:84:10: 'THAN'
            {
            match("THAN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__107"

    // $ANTLR start "T__108"
    public final void mT__108() throws RecognitionException {
        try {
            int _type = T__108;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:85:8: ( 'SUBPARTITIONS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:85:10: 'SUBPARTITIONS'
            {
            match("SUBPARTITIONS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__108"

    // $ANTLR start "T__109"
    public final void mT__109() throws RecognitionException {
        try {
            int _type = T__109;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:86:8: ( 'LOGGING' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:86:10: 'LOGGING'
            {
            match("LOGGING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__109"

    // $ANTLR start "T__110"
    public final void mT__110() throws RecognitionException {
        try {
            int _type = T__110;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:87:8: ( 'NOLOGGING' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:87:10: 'NOLOGGING'
            {
            match("NOLOGGING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__110"

    // $ANTLR start "T__111"
    public final void mT__111() throws RecognitionException {
        try {
            int _type = T__111;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:88:8: ( 'NOCOMPRESS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:88:10: 'NOCOMPRESS'
            {
            match("NOCOMPRESS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__111"

    // $ANTLR start "T__112"
    public final void mT__112() throws RecognitionException {
        try {
            int _type = T__112;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:89:8: ( 'CACHE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:89:10: 'CACHE'
            {
            match("CACHE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__112"

    // $ANTLR start "T__113"
    public final void mT__113() throws RecognitionException {
        try {
            int _type = T__113;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:90:8: ( 'NOCACHE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:90:10: 'NOCACHE'
            {
            match("NOCACHE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__113"

    // $ANTLR start "T__114"
    public final void mT__114() throws RecognitionException {
        try {
            int _type = T__114;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:91:8: ( 'NOPARALLEL' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:91:10: 'NOPARALLEL'
            {
            match("NOPARALLEL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__114"

    // $ANTLR start "T__115"
    public final void mT__115() throws RecognitionException {
        try {
            int _type = T__115;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:92:8: ( 'MONITORING' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:92:10: 'MONITORING'
            {
            match("MONITORING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__115"

    // $ANTLR start "T__116"
    public final void mT__116() throws RecognitionException {
        try {
            int _type = T__116;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:93:8: ( 'RETENTION' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:93:10: 'RETENTION'
            {
            match("RETENTION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__116"

    // $ANTLR start "T__117"
    public final void mT__117() throws RecognitionException {
        try {
            int _type = T__117;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:94:8: ( 'LOB' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:94:10: 'LOB'
            {
            match("LOB"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__117"

    // $ANTLR start "T__118"
    public final void mT__118() throws RecognitionException {
        try {
            int _type = T__118;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:95:8: ( 'AS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:95:10: 'AS'
            {
            match("AS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__118"

    // $ANTLR start "T__119"
    public final void mT__119() throws RecognitionException {
        try {
            int _type = T__119;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:96:8: ( 'CHUNK' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:96:10: 'CHUNK'
            {
            match("CHUNK"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__119"

    // $ANTLR start "T__120"
    public final void mT__120() throws RecognitionException {
        try {
            int _type = T__120;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:97:8: ( 'PCTVERSION' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:97:10: 'PCTVERSION'
            {
            match("PCTVERSION"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__120"

    // $ANTLR start "T__121"
    public final void mT__121() throws RecognitionException {
        try {
            int _type = T__121;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:98:8: ( 'INDEX' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:98:10: 'INDEX'
            {
            match("INDEX"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__121"

    // $ANTLR start "T__122"
    public final void mT__122() throws RecognitionException {
        try {
            int _type = T__122;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:99:8: ( 'ALTER' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:99:10: 'ALTER'
            {
            match("ALTER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__122"

    // $ANTLR start "T__123"
    public final void mT__123() throws RecognitionException {
        try {
            int _type = T__123;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:100:8: ( 'DROP' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:100:10: 'DROP'
            {
            match("DROP"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__123"

    // $ANTLR start "T__124"
    public final void mT__124() throws RecognitionException {
        try {
            int _type = T__124;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:101:8: ( 'CASCADE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:101:10: 'CASCADE'
            {
            match("CASCADE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__124"

    // $ANTLR start "T__125"
    public final void mT__125() throws RecognitionException {
        try {
            int _type = T__125;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:102:8: ( 'ADD' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:102:10: 'ADD'
            {
            match("ADD"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__125"

    // $ANTLR start "T__126"
    public final void mT__126() throws RecognitionException {
        try {
            int _type = T__126;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:103:8: ( 'USING' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:103:10: 'USING'
            {
            match("USING"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__126"

    // $ANTLR start "T__127"
    public final void mT__127() throws RecognitionException {
        try {
            int _type = T__127;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:104:8: ( 'FOREIGN' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:104:10: 'FOREIGN'
            {
            match("FOREIGN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__127"

    // $ANTLR start "T__128"
    public final void mT__128() throws RecognitionException {
        try {
            int _type = T__128;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:105:8: ( 'REFERENCES' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:105:10: 'REFERENCES'
            {
            match("REFERENCES"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__128"

    // $ANTLR start "T__129"
    public final void mT__129() throws RecognitionException {
        try {
            int _type = T__129;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:106:8: ( 'UNIQUE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:106:10: 'UNIQUE'
            {
            match("UNIQUE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__129"

    // $ANTLR start "T__130"
    public final void mT__130() throws RecognitionException {
        try {
            int _type = T__130;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:107:8: ( 'CHECK' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:107:10: 'CHECK'
            {
            match("CHECK"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__130"

    // $ANTLR start "T__131"
    public final void mT__131() throws RecognitionException {
        try {
            int _type = T__131;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:108:8: ( 'CONSTRAINTS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:108:10: 'CONSTRAINTS'
            {
            match("CONSTRAINTS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__131"

    // $ANTLR start "T__132"
    public final void mT__132() throws RecognitionException {
        try {
            int _type = T__132;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:109:8: ( 'INDEXTYPE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:109:10: 'INDEXTYPE'
            {
            match("INDEXTYPE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__132"

    // $ANTLR start "T__133"
    public final void mT__133() throws RecognitionException {
        try {
            int _type = T__133;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:110:8: ( 'SEQUENCE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:110:10: 'SEQUENCE'
            {
            match("SEQUENCE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__133"

    // $ANTLR start "T__134"
    public final void mT__134() throws RecognitionException {
        try {
            int _type = T__134;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:111:8: ( 'COMMENT' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:111:10: 'COMMENT'
            {
            match("COMMENT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__134"

    // $ANTLR start "T__135"
    public final void mT__135() throws RecognitionException {
        try {
            int _type = T__135;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:112:8: ( 'COLUMN' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:112:10: 'COLUMN'
            {
            match("COLUMN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__135"

    // $ANTLR start "T__136"
    public final void mT__136() throws RecognitionException {
        try {
            int _type = T__136;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:113:8: ( '.' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:113:10: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__136"

    // $ANTLR start "T__137"
    public final void mT__137() throws RecognitionException {
        try {
            int _type = T__137;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:114:8: ( 'TRIGGER' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:114:10: 'TRIGGER'
            {
            match("TRIGGER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__137"

    // $ANTLR start "CREATE_TABLE"
    public final void mCREATE_TABLE() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:286:22: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:286:23: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "CREATE_TABLE"

    // $ANTLR start "DROP_TABLE"
    public final void mDROP_TABLE() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:287:20: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:287:21: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "DROP_TABLE"

    // $ANTLR start "ALTER_TABLE"
    public final void mALTER_TABLE() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:288:21: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:288:22: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "ALTER_TABLE"

    // $ANTLR start "TABLE_NAME"
    public final void mTABLE_NAME() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:289:20: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:289:21: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "TABLE_NAME"

    // $ANTLR start "COLUMN_NAME"
    public final void mCOLUMN_NAME() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:290:21: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:290:22: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "COLUMN_NAME"

    // $ANTLR start "SIZE"
    public final void mSIZE() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:291:14: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:291:15: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "SIZE"

    // $ANTLR start "CREATE_SEQUENCE"
    public final void mCREATE_SEQUENCE() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:292:25: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:292:26: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "CREATE_SEQUENCE"

    // $ANTLR start "DROP_SEQUENCE"
    public final void mDROP_SEQUENCE() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:293:23: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:293:24: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "DROP_SEQUENCE"

    // $ANTLR start "NAME_LIST"
    public final void mNAME_LIST() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:294:19: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:294:20: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "NAME_LIST"

    // $ANTLR start "CREATE_INDEX"
    public final void mCREATE_INDEX() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:295:22: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:295:23: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "CREATE_INDEX"

    // $ANTLR start "INDEX_NAME"
    public final void mINDEX_NAME() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:296:20: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:296:21: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "INDEX_NAME"

    // $ANTLR start "TABLE_DETAILS"
    public final void mTABLE_DETAILS() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:297:23: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:297:24: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "TABLE_DETAILS"

    // $ANTLR start "COLUMN_SPEC"
    public final void mCOLUMN_SPEC() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:298:21: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:298:22: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "COLUMN_SPEC"

    // $ANTLR start "ORA_CONFIGS"
    public final void mORA_CONFIGS() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:299:21: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:299:22: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "ORA_CONFIGS"

    // $ANTLR start "COMMENT_TABLE"
    public final void mCOMMENT_TABLE() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:300:23: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:300:24: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "COMMENT_TABLE"

    // $ANTLR start "COMMENT_COLUMN"
    public final void mCOMMENT_COLUMN() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:301:24: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:301:25: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "COMMENT_COLUMN"

    // $ANTLR start "INVOCATION"
    public final void mINVOCATION() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:302:20: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:302:21: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "INVOCATION"

    // $ANTLR start "ARGUMENTS"
    public final void mARGUMENTS() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:303:19: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:303:20: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "ARGUMENTS"

    // $ANTLR start "VALUE_LIST"
    public final void mVALUE_LIST() throws RecognitionException {
        try {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:304:20: ()
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:304:21: 
            {
            }

        }
        finally {
        }
    }
    // $ANTLR end "VALUE_LIST"

    // $ANTLR start "OR"
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:306:4: ( 'OR' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:306:6: 'OR'
            {
            match("OR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OR"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:307:5: ( 'AND' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:307:7: 'AND'
            {
            match("AND"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "XOR"
    public final void mXOR() throws RecognitionException {
        try {
            int _type = XOR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:308:5: ( 'XOR' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:308:7: 'XOR'
            {
            match("XOR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "XOR"

    // $ANTLR start "EQ"
    public final void mEQ() throws RecognitionException {
        try {
            int _type = EQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:309:4: ( '=' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:309:6: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EQ"

    // $ANTLR start "BANGEQ"
    public final void mBANGEQ() throws RecognitionException {
        try {
            int _type = BANGEQ;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:310:8: ( '!=' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:310:10: '!='
            {
            match("!="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BANGEQ"

    // $ANTLR start "LTGT"
    public final void mLTGT() throws RecognitionException {
        try {
            int _type = LTGT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:311:6: ( '<>' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:311:8: '<>'
            {
            match("<>"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LTGT"

    // $ANTLR start "GT"
    public final void mGT() throws RecognitionException {
        try {
            int _type = GT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:312:4: ( '>' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:312:6: '>'
            {
            match('>'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GT"

    // $ANTLR start "GE"
    public final void mGE() throws RecognitionException {
        try {
            int _type = GE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:313:4: ( '>=' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:313:6: '>='
            {
            match(">="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GE"

    // $ANTLR start "LT"
    public final void mLT() throws RecognitionException {
        try {
            int _type = LT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:314:4: ( '<' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:314:6: '<'
            {
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LT"

    // $ANTLR start "LE"
    public final void mLE() throws RecognitionException {
        try {
            int _type = LE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:315:4: ( '<=' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:315:6: '<='
            {
            match("<="); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LE"

    // $ANTLR start "IS"
    public final void mIS() throws RecognitionException {
        try {
            int _type = IS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:316:4: ( 'IS' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:316:6: 'IS'
            {
            match("IS"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IS"

    // $ANTLR start "NOT"
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:317:5: ( 'NOT' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:317:7: 'NOT'
            {
            match("NOT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NOT"

    // $ANTLR start "NULL"
    public final void mNULL() throws RecognitionException {
        try {
            int _type = NULL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:318:7: ( 'NULL' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:318:9: 'NULL'
            {
            match("NULL"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NULL"

    // $ANTLR start "IN"
    public final void mIN() throws RecognitionException {
        try {
            int _type = IN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:319:4: ( 'IN' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:319:6: 'IN'
            {
            match("IN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IN"

    // $ANTLR start "BETWEEN"
    public final void mBETWEEN() throws RecognitionException {
        try {
            int _type = BETWEEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:320:9: ( 'BETWEEN' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:320:11: 'BETWEEN'
            {
            match("BETWEEN"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BETWEEN"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:321:6: ( '+' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:321:8: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "SUB"
    public final void mSUB() throws RecognitionException {
        try {
            int _type = SUB;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:322:5: ( '-' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:322:7: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SUB"

    // $ANTLR start "STAR"
    public final void mSTAR() throws RecognitionException {
        try {
            int _type = STAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:323:6: ( '*' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:323:8: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STAR"

    // $ANTLR start "SLASH"
    public final void mSLASH() throws RecognitionException {
        try {
            int _type = SLASH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:324:7: ( '/' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:324:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SLASH"

    // $ANTLR start "PERCENT"
    public final void mPERCENT() throws RecognitionException {
        try {
            int _type = PERCENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:325:9: ( '%' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:325:11: '%'
            {
            match('%'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PERCENT"

    // $ANTLR start "BARBAR"
    public final void mBARBAR() throws RecognitionException {
        try {
            int _type = BARBAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:326:8: ( '||' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:326:10: '||'
            {
            match("||"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BARBAR"

    // $ANTLR start "ROWID"
    public final void mROWID() throws RecognitionException {
        try {
            int _type = ROWID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:328:9: ( 'ROWID' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:328:11: 'ROWID'
            {
            match("ROWID"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ROWID"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:329:10: ( 'NUMBER' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:329:12: 'NUMBER'
            {
            match("NUMBER"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "VARCHAR2"
    public final void mVARCHAR2() throws RecognitionException {
        try {
            int _type = VARCHAR2;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:330:10: ( 'VARCHAR2' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:330:12: 'VARCHAR2'
            {
            match("VARCHAR2"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VARCHAR2"

    // $ANTLR start "NVARCHAR2"
    public final void mNVARCHAR2() throws RecognitionException {
        try {
            int _type = NVARCHAR2;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:331:11: ( 'NVARCHAR2' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:331:13: 'NVARCHAR2'
            {
            match("NVARCHAR2"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NVARCHAR2"

    // $ANTLR start "BYTE"
    public final void mBYTE() throws RecognitionException {
        try {
            int _type = BYTE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:332:8: ( 'BYTE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:332:10: 'BYTE'
            {
            match("BYTE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BYTE"

    // $ANTLR start "CHAR"
    public final void mCHAR() throws RecognitionException {
        try {
            int _type = CHAR;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:333:8: ( 'CHAR' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:333:10: 'CHAR'
            {
            match("CHAR"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "CHAR"

    // $ANTLR start "DATE"
    public final void mDATE() throws RecognitionException {
        try {
            int _type = DATE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:334:8: ( 'DATE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:334:10: 'DATE'
            {
            match("DATE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DATE"

    // $ANTLR start "TIMESTAMP"
    public final void mTIMESTAMP() throws RecognitionException {
        try {
            int _type = TIMESTAMP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:335:12: ( 'TIMESTAMP' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:335:14: 'TIMESTAMP'
            {
            match("TIMESTAMP"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TIMESTAMP"

    // $ANTLR start "BEFORE"
    public final void mBEFORE() throws RecognitionException {
        try {
            int _type = BEFORE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:337:10: ( 'BEFORE' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:337:12: 'BEFORE'
            {
            match("BEFORE"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BEFORE"

    // $ANTLR start "PRIMARY"
    public final void mPRIMARY() throws RecognitionException {
        try {
            int _type = PRIMARY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:338:10: ( 'PRIMARY' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:338:12: 'PRIMARY'
            {
            match("PRIMARY"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PRIMARY"

    // $ANTLR start "DEFAULT"
    public final void mDEFAULT() throws RecognitionException {
        try {
            int _type = DEFAULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:339:10: ( 'DEFAULT' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:339:12: 'DEFAULT'
            {
            match("DEFAULT"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEFAULT"

    // $ANTLR start "IDENTIFIER"
    public final void mIDENTIFIER() throws RecognitionException {
        try {
            int _type = IDENTIFIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:341:13: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )* )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:341:15: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:341:39: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' | '$' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='$'||(LA1_0>='0' && LA1_0<='9')||(LA1_0>='A' && LA1_0<='Z')||LA1_0=='_'||(LA1_0>='a' && LA1_0<='z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:
            	    {
            	    if ( input.LA(1)=='$'||(input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDENTIFIER"

    // $ANTLR start "INT"
    public final void mINT() throws RecognitionException {
        try {
            int _type = INT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:343:5: ( ( '0' .. '9' )+ )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:343:7: ( '0' .. '9' )+
            {
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:343:7: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:343:7: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:346:5: ( '--' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options {greedy=false; } : . )* '*/' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='-') ) {
                alt6=1;
            }
            else if ( (LA6_0=='/') ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:346:9: '--' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
                    {
                    match("--"); 

                    // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:346:14: (~ ( '\\n' | '\\r' ) )*
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( ((LA3_0>='\u0000' && LA3_0<='\t')||(LA3_0>='\u000B' && LA3_0<='\f')||(LA3_0>='\u000E' && LA3_0<='\uFFFF')) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:346:14: ~ ( '\\n' | '\\r' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop3;
                        }
                    } while (true);

                    // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:346:28: ( '\\r' )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0=='\r') ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:346:28: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 
                    _channel=HIDDEN;

                    }
                    break;
                case 2 :
                    // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:347:9: '/*' ( options {greedy=false; } : . )* '*/'
                    {
                    match("/*"); 

                    // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:347:14: ( options {greedy=false; } : . )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( (LA5_0=='*') ) {
                            int LA5_1 = input.LA(2);

                            if ( (LA5_1=='/') ) {
                                alt5=2;
                            }
                            else if ( ((LA5_1>='\u0000' && LA5_1<='.')||(LA5_1>='0' && LA5_1<='\uFFFF')) ) {
                                alt5=1;
                            }


                        }
                        else if ( ((LA5_0>='\u0000' && LA5_0<=')')||(LA5_0>='+' && LA5_0<='\uFFFF')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:347:42: .
                    	    {
                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    match("*/"); 

                    _channel=HIDDEN;

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:350:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:350:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:357:8: ( '\\'' (~ ( '\\\\' | '\\'' ) )* '\\'' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:357:11: '\\'' (~ ( '\\\\' | '\\'' ) )* '\\''
            {
            match('\''); 
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:357:16: (~ ( '\\\\' | '\\'' ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='\u0000' && LA7_0<='&')||(LA7_0>='(' && LA7_0<='[')||(LA7_0>=']' && LA7_0<='\uFFFF')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:357:18: ~ ( '\\\\' | '\\'' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='&')||(input.LA(1)>='(' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);

            match('\''); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "QUOTED_NAME"
    public final void mQUOTED_NAME() throws RecognitionException {
        try {
            int _type = QUOTED_NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:358:13: ( '\"' (~ ( '\\\\' | '\\\"' ) )* '\"' )
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:358:16: '\"' (~ ( '\\\\' | '\\\"' ) )* '\"'
            {
            match('\"'); 
            // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:358:20: (~ ( '\\\\' | '\\\"' ) )*
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='\u0000' && LA8_0<='!')||(LA8_0>='#' && LA8_0<='[')||(LA8_0>=']' && LA8_0<='\uFFFF')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:358:22: ~ ( '\\\\' | '\\\"' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop8;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "QUOTED_NAME"

    public void mTokens() throws RecognitionException {
        // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:8: ( T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | T__110 | T__111 | T__112 | T__113 | T__114 | T__115 | T__116 | T__117 | T__118 | T__119 | T__120 | T__121 | T__122 | T__123 | T__124 | T__125 | T__126 | T__127 | T__128 | T__129 | T__130 | T__131 | T__132 | T__133 | T__134 | T__135 | T__136 | T__137 | OR | AND | XOR | EQ | BANGEQ | LTGT | GT | GE | LT | LE | IS | NOT | NULL | IN | BETWEEN | PLUS | SUB | STAR | SLASH | PERCENT | BARBAR | ROWID | NUMBER | VARCHAR2 | NVARCHAR2 | BYTE | CHAR | DATE | TIMESTAMP | BEFORE | PRIMARY | DEFAULT | IDENTIFIER | INT | COMMENT | WS | STRING | QUOTED_NAME )
        int alt9=115;
        alt9 = dfa9.predict(input);
        switch (alt9) {
            case 1 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:10: T__61
                {
                mT__61(); 

                }
                break;
            case 2 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:16: T__62
                {
                mT__62(); 

                }
                break;
            case 3 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:22: T__63
                {
                mT__63(); 

                }
                break;
            case 4 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:28: T__64
                {
                mT__64(); 

                }
                break;
            case 5 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:34: T__65
                {
                mT__65(); 

                }
                break;
            case 6 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:40: T__66
                {
                mT__66(); 

                }
                break;
            case 7 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:46: T__67
                {
                mT__67(); 

                }
                break;
            case 8 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:52: T__68
                {
                mT__68(); 

                }
                break;
            case 9 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:58: T__69
                {
                mT__69(); 

                }
                break;
            case 10 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:64: T__70
                {
                mT__70(); 

                }
                break;
            case 11 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:70: T__71
                {
                mT__71(); 

                }
                break;
            case 12 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:76: T__72
                {
                mT__72(); 

                }
                break;
            case 13 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:82: T__73
                {
                mT__73(); 

                }
                break;
            case 14 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:88: T__74
                {
                mT__74(); 

                }
                break;
            case 15 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:94: T__75
                {
                mT__75(); 

                }
                break;
            case 16 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:100: T__76
                {
                mT__76(); 

                }
                break;
            case 17 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:106: T__77
                {
                mT__77(); 

                }
                break;
            case 18 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:112: T__78
                {
                mT__78(); 

                }
                break;
            case 19 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:118: T__79
                {
                mT__79(); 

                }
                break;
            case 20 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:124: T__80
                {
                mT__80(); 

                }
                break;
            case 21 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:130: T__81
                {
                mT__81(); 

                }
                break;
            case 22 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:136: T__82
                {
                mT__82(); 

                }
                break;
            case 23 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:142: T__83
                {
                mT__83(); 

                }
                break;
            case 24 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:148: T__84
                {
                mT__84(); 

                }
                break;
            case 25 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:154: T__85
                {
                mT__85(); 

                }
                break;
            case 26 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:160: T__86
                {
                mT__86(); 

                }
                break;
            case 27 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:166: T__87
                {
                mT__87(); 

                }
                break;
            case 28 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:172: T__88
                {
                mT__88(); 

                }
                break;
            case 29 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:178: T__89
                {
                mT__89(); 

                }
                break;
            case 30 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:184: T__90
                {
                mT__90(); 

                }
                break;
            case 31 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:190: T__91
                {
                mT__91(); 

                }
                break;
            case 32 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:196: T__92
                {
                mT__92(); 

                }
                break;
            case 33 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:202: T__93
                {
                mT__93(); 

                }
                break;
            case 34 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:208: T__94
                {
                mT__94(); 

                }
                break;
            case 35 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:214: T__95
                {
                mT__95(); 

                }
                break;
            case 36 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:220: T__96
                {
                mT__96(); 

                }
                break;
            case 37 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:226: T__97
                {
                mT__97(); 

                }
                break;
            case 38 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:232: T__98
                {
                mT__98(); 

                }
                break;
            case 39 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:238: T__99
                {
                mT__99(); 

                }
                break;
            case 40 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:244: T__100
                {
                mT__100(); 

                }
                break;
            case 41 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:251: T__101
                {
                mT__101(); 

                }
                break;
            case 42 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:258: T__102
                {
                mT__102(); 

                }
                break;
            case 43 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:265: T__103
                {
                mT__103(); 

                }
                break;
            case 44 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:272: T__104
                {
                mT__104(); 

                }
                break;
            case 45 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:279: T__105
                {
                mT__105(); 

                }
                break;
            case 46 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:286: T__106
                {
                mT__106(); 

                }
                break;
            case 47 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:293: T__107
                {
                mT__107(); 

                }
                break;
            case 48 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:300: T__108
                {
                mT__108(); 

                }
                break;
            case 49 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:307: T__109
                {
                mT__109(); 

                }
                break;
            case 50 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:314: T__110
                {
                mT__110(); 

                }
                break;
            case 51 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:321: T__111
                {
                mT__111(); 

                }
                break;
            case 52 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:328: T__112
                {
                mT__112(); 

                }
                break;
            case 53 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:335: T__113
                {
                mT__113(); 

                }
                break;
            case 54 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:342: T__114
                {
                mT__114(); 

                }
                break;
            case 55 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:349: T__115
                {
                mT__115(); 

                }
                break;
            case 56 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:356: T__116
                {
                mT__116(); 

                }
                break;
            case 57 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:363: T__117
                {
                mT__117(); 

                }
                break;
            case 58 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:370: T__118
                {
                mT__118(); 

                }
                break;
            case 59 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:377: T__119
                {
                mT__119(); 

                }
                break;
            case 60 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:384: T__120
                {
                mT__120(); 

                }
                break;
            case 61 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:391: T__121
                {
                mT__121(); 

                }
                break;
            case 62 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:398: T__122
                {
                mT__122(); 

                }
                break;
            case 63 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:405: T__123
                {
                mT__123(); 

                }
                break;
            case 64 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:412: T__124
                {
                mT__124(); 

                }
                break;
            case 65 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:419: T__125
                {
                mT__125(); 

                }
                break;
            case 66 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:426: T__126
                {
                mT__126(); 

                }
                break;
            case 67 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:433: T__127
                {
                mT__127(); 

                }
                break;
            case 68 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:440: T__128
                {
                mT__128(); 

                }
                break;
            case 69 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:447: T__129
                {
                mT__129(); 

                }
                break;
            case 70 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:454: T__130
                {
                mT__130(); 

                }
                break;
            case 71 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:461: T__131
                {
                mT__131(); 

                }
                break;
            case 72 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:468: T__132
                {
                mT__132(); 

                }
                break;
            case 73 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:475: T__133
                {
                mT__133(); 

                }
                break;
            case 74 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:482: T__134
                {
                mT__134(); 

                }
                break;
            case 75 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:489: T__135
                {
                mT__135(); 

                }
                break;
            case 76 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:496: T__136
                {
                mT__136(); 

                }
                break;
            case 77 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:503: T__137
                {
                mT__137(); 

                }
                break;
            case 78 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:510: OR
                {
                mOR(); 

                }
                break;
            case 79 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:513: AND
                {
                mAND(); 

                }
                break;
            case 80 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:517: XOR
                {
                mXOR(); 

                }
                break;
            case 81 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:521: EQ
                {
                mEQ(); 

                }
                break;
            case 82 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:524: BANGEQ
                {
                mBANGEQ(); 

                }
                break;
            case 83 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:531: LTGT
                {
                mLTGT(); 

                }
                break;
            case 84 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:536: GT
                {
                mGT(); 

                }
                break;
            case 85 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:539: GE
                {
                mGE(); 

                }
                break;
            case 86 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:542: LT
                {
                mLT(); 

                }
                break;
            case 87 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:545: LE
                {
                mLE(); 

                }
                break;
            case 88 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:548: IS
                {
                mIS(); 

                }
                break;
            case 89 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:551: NOT
                {
                mNOT(); 

                }
                break;
            case 90 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:555: NULL
                {
                mNULL(); 

                }
                break;
            case 91 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:560: IN
                {
                mIN(); 

                }
                break;
            case 92 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:563: BETWEEN
                {
                mBETWEEN(); 

                }
                break;
            case 93 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:571: PLUS
                {
                mPLUS(); 

                }
                break;
            case 94 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:576: SUB
                {
                mSUB(); 

                }
                break;
            case 95 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:580: STAR
                {
                mSTAR(); 

                }
                break;
            case 96 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:585: SLASH
                {
                mSLASH(); 

                }
                break;
            case 97 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:591: PERCENT
                {
                mPERCENT(); 

                }
                break;
            case 98 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:599: BARBAR
                {
                mBARBAR(); 

                }
                break;
            case 99 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:606: ROWID
                {
                mROWID(); 

                }
                break;
            case 100 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:612: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 101 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:619: VARCHAR2
                {
                mVARCHAR2(); 

                }
                break;
            case 102 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:628: NVARCHAR2
                {
                mNVARCHAR2(); 

                }
                break;
            case 103 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:638: BYTE
                {
                mBYTE(); 

                }
                break;
            case 104 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:643: CHAR
                {
                mCHAR(); 

                }
                break;
            case 105 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:648: DATE
                {
                mDATE(); 

                }
                break;
            case 106 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:653: TIMESTAMP
                {
                mTIMESTAMP(); 

                }
                break;
            case 107 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:663: BEFORE
                {
                mBEFORE(); 

                }
                break;
            case 108 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:670: PRIMARY
                {
                mPRIMARY(); 

                }
                break;
            case 109 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:678: DEFAULT
                {
                mDEFAULT(); 

                }
                break;
            case 110 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:686: IDENTIFIER
                {
                mIDENTIFIER(); 

                }
                break;
            case 111 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:697: INT
                {
                mINT(); 

                }
                break;
            case 112 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:701: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 113 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:709: WS
                {
                mWS(); 

                }
                break;
            case 114 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:712: STRING
                {
                mSTRING(); 

                }
                break;
            case 115 :
                // /Users/volker/Documents/databene/jdbacl/src/main/g/SQL.g:1:719: QUOTED_NAME
                {
                mQUOTED_NAME(); 

                }
                break;

        }

    }


    protected DFA9 dfa9 = new DFA9(this);
    static final String DFA9_eotS =
        "\2\uffff\3\45\3\uffff\21\45\1\uffff\1\45\2\uffff\1\143\1\145\1\uffff"+
        "\1\147\1\uffff\1\150\7\uffff\22\45\1\u0086\2\45\1\u008b\1\u008c"+
        "\3\45\1\u0092\1\u0093\23\45\1\u00ac\7\45\10\uffff\26\45\1\u00cc"+
        "\6\45\1\uffff\2\45\1\u00d5\1\45\2\uffff\5\45\2\uffff\5\45\1\u00e7"+
        "\17\45\1\u00f7\2\45\1\uffff\1\45\1\u00fb\1\u00fc\3\45\1\u0100\1"+
        "\45\1\u0102\10\45\1\u010c\3\45\1\u0110\4\45\1\u0115\4\45\1\uffff"+
        "\1\u011a\2\45\1\u011d\1\45\1\u011f\2\45\1\uffff\17\45\1\u0132\1"+
        "\45\1\uffff\6\45\1\u013a\1\u013b\3\45\1\u0140\1\u0141\1\u0142\1"+
        "\45\1\uffff\3\45\2\uffff\3\45\1\uffff\1\45\1\uffff\5\45\1\u0150"+
        "\1\45\1\u0152\1\u0153\1\uffff\2\45\1\u0157\1\uffff\3\45\1\u015b"+
        "\1\uffff\4\45\1\uffff\2\45\1\uffff\1\45\1\uffff\13\45\1\u016f\6"+
        "\45\1\uffff\1\u0176\1\u0177\5\45\2\uffff\1\45\1\u017e\2\45\3\uffff"+
        "\3\45\1\u0184\1\u0185\2\45\1\u0188\2\45\1\u018b\1\45\1\u018d\1\uffff"+
        "\1\45\2\uffff\1\u018f\2\45\1\uffff\2\45\1\u0194\1\uffff\4\45\1\u0199"+
        "\3\45\1\u019d\12\45\1\uffff\5\45\1\u01ad\2\uffff\3\45\1\u01b1\2"+
        "\45\1\uffff\3\45\1\u01b7\1\45\2\uffff\1\u01b9\1\45\1\uffff\2\45"+
        "\1\uffff\1\u01bd\1\uffff\1\u01be\1\uffff\2\45\1\u01c1\1\45\1\uffff"+
        "\2\45\1\u01c5\1\45\1\uffff\2\45\1\u01c9\1\uffff\1\45\1\u01cb\1\u01cc"+
        "\3\45\1\u01d0\1\45\1\u01d2\6\45\1\uffff\2\45\1\u01db\1\uffff\1\u01dc"+
        "\1\u01dd\2\45\1\u01e0\1\uffff\1\45\1\uffff\1\u01e2\1\45\1\u01e4"+
        "\2\uffff\2\45\1\uffff\3\45\1\uffff\3\45\1\uffff\1\45\2\uffff\3\45"+
        "\1\uffff\1\u01f1\1\uffff\1\45\1\u01f3\1\45\1\u01f5\4\45\3\uffff"+
        "\1\45\1\u01fb\1\uffff\1\u01fc\1\uffff\1\45\1\uffff\1\u01fe\1\45"+
        "\1\u0200\1\u0201\2\45\1\u0204\4\45\1\u020a\1\uffff\1\u020b\1\uffff"+
        "\1\45\1\uffff\2\45\1\u020f\2\45\2\uffff\1\u0213\1\uffff\1\u0214"+
        "\2\uffff\1\u0215\1\u0216\1\uffff\3\45\1\u021a\1\u021b\2\uffff\1"+
        "\u021c\1\u021d\1\u021e\1\uffff\1\u021f\1\45\1\u0221\4\uffff\1\u0222"+
        "\1\45\1\u0224\6\uffff\1\45\2\uffff\1\u0226\1\uffff\1\u0228\1\uffff"+
        "\1\u0229\2\uffff";
    static final String DFA9_eofS =
        "\u022a\uffff";
    static final String DFA9_minS =
        "\1\11\1\uffff\1\101\1\114\1\101\3\uffff\1\103\2\105\1\116\1\101"+
        "\1\116\1\101\1\116\2\101\1\105\1\101\1\105\1\101\1\104\1\116\1\117"+
        "\1\uffff\1\117\2\uffff\2\75\1\uffff\1\55\1\uffff\1\52\7\uffff\1"+
        "\105\1\117\1\114\1\103\1\101\1\117\1\115\1\102\1\101\1\111\1\115"+
        "\1\110\1\130\1\103\1\114\1\101\1\117\1\106\1\44\1\106\1\131\2\44"+
        "\1\124\1\122\1\111\2\44\1\130\2\116\1\101\1\127\1\116\1\106\1\123"+
        "\1\106\1\117\1\124\1\117\1\102\1\121\3\123\1\102\1\114\1\44\1\124"+
        "\2\104\2\111\2\122\10\uffff\1\101\1\102\1\123\1\115\1\125\1\110"+
        "\1\103\1\116\1\103\1\122\1\102\1\120\1\114\1\116\1\107\1\105\1\101"+
        "\1\117\1\124\1\117\2\101\1\44\1\114\1\102\1\122\1\102\1\106\1\105"+
        "\1\uffff\1\127\1\117\1\44\1\101\2\uffff\1\106\1\124\1\115\1\124"+
        "\1\105\2\uffff\2\105\1\111\1\105\1\102\1\44\1\107\2\105\1\101\1"+
        "\105\1\101\1\120\1\105\1\122\1\120\1\125\1\110\1\124\1\123\1\107"+
        "\1\44\1\125\1\103\1\uffff\1\105\2\44\1\116\1\121\1\105\1\44\1\124"+
        "\1\44\1\124\1\122\1\105\1\115\1\105\1\101\2\113\1\44\1\101\1\117"+
        "\1\105\1\44\1\107\1\123\1\122\1\102\1\44\1\107\1\115\1\103\1\122"+
        "\1\uffff\1\44\1\105\1\103\1\44\1\105\1\44\1\105\1\122\1\uffff\1"+
        "\116\1\123\1\122\1\116\1\105\1\111\1\101\1\111\1\130\1\122\1\130"+
        "\1\115\1\124\1\130\1\114\1\44\1\104\1\uffff\1\105\1\116\1\122\1"+
        "\102\1\124\1\125\2\44\2\101\1\105\3\44\1\111\1\uffff\1\105\1\110"+
        "\1\122\2\uffff\1\107\1\125\1\111\1\uffff\1\105\1\uffff\1\122\1\105"+
        "\1\124\2\116\1\44\1\104\2\44\1\uffff\1\114\1\122\1\44\1\uffff\1"+
        "\105\1\124\1\62\1\44\1\uffff\1\107\1\120\1\110\1\101\1\uffff\1\122"+
        "\1\110\1\uffff\1\122\1\uffff\2\105\1\111\2\105\1\103\1\122\1\124"+
        "\1\122\2\101\1\44\1\101\1\124\1\105\1\117\1\124\1\105\1\uffff\2"+
        "\44\1\124\1\105\1\114\1\105\1\114\2\uffff\1\107\1\44\1\122\1\116"+
        "\3\uffff\1\116\1\123\1\101\2\44\1\105\1\107\1\44\1\101\1\123\1\44"+
        "\1\124\1\44\1\uffff\1\105\2\uffff\1\44\1\101\1\120\1\uffff\1\122"+
        "\1\101\1\44\1\uffff\1\111\1\122\1\105\1\114\1\44\1\101\1\137\1\116"+
        "\1\44\1\132\1\104\1\105\1\122\1\123\1\111\1\131\1\116\1\114\1\131"+
        "\1\uffff\1\116\1\105\1\116\1\122\1\105\1\44\2\uffff\1\111\1\116"+
        "\1\105\1\44\1\124\1\105\1\uffff\1\124\1\103\1\107\1\44\1\122\2\uffff"+
        "\1\44\1\116\1\uffff\1\111\1\123\1\uffff\1\44\1\uffff\1\44\1\uffff"+
        "\1\122\1\101\1\44\1\115\1\uffff\1\116\1\105\1\44\1\114\1\uffff\1"+
        "\122\1\120\1\44\1\uffff\1\101\2\44\1\105\1\111\1\117\1\44\1\123"+
        "\1\44\1\120\1\123\1\116\1\124\1\111\1\116\1\uffff\1\117\1\103\1"+
        "\44\1\uffff\2\44\1\111\1\105\1\44\1\uffff\1\62\1\uffff\1\44\1\116"+
        "\1\44\2\uffff\1\131\1\103\1\uffff\1\120\1\107\1\123\1\uffff\1\105"+
        "\1\62\1\117\1\uffff\1\124\2\uffff\1\101\1\117\1\116\1\uffff\1\44"+
        "\1\uffff\1\105\1\44\1\124\1\44\1\116\1\124\1\116\1\105\3\uffff\1"+
        "\124\1\44\1\uffff\1\44\1\uffff\1\124\1\uffff\1\44\1\105\2\44\1\123"+
        "\1\114\1\44\1\117\1\111\1\123\1\116\1\44\1\uffff\1\44\1\uffff\1"+
        "\123\1\uffff\1\107\1\123\1\44\1\123\1\111\2\uffff\1\44\1\uffff\1"+
        "\44\2\uffff\2\44\1\uffff\1\114\1\117\1\105\2\44\2\uffff\3\44\1\uffff"+
        "\1\44\1\117\1\44\4\uffff\1\44\1\116\1\44\6\uffff\1\116\2\uffff\1"+
        "\44\1\uffff\1\44\1\uffff\1\44\2\uffff";
    static final String DFA9_maxS =
        "\1\174\1\uffff\1\122\1\114\1\122\3\uffff\1\126\1\131\1\105\2\122"+
        "\1\123\1\117\1\116\1\117\1\122\1\125\1\101\1\117\1\101\2\123\1\117"+
        "\1\uffff\1\117\2\uffff\1\76\1\75\1\uffff\1\55\1\uffff\1\52\7\uffff"+
        "\1\105\1\117\1\116\1\123\1\125\1\117\1\115\1\102\1\101\1\111\1\115"+
        "\1\114\1\130\1\124\1\115\1\101\1\117\1\106\1\172\1\124\1\131\2\172"+
        "\1\124\1\122\1\111\2\172\1\130\1\126\1\116\1\101\1\127\1\116\1\124"+
        "\1\123\1\114\1\117\1\124\1\117\1\102\1\121\3\123\1\107\1\122\1\172"+
        "\1\124\2\104\2\111\2\122\10\uffff\1\101\1\102\1\123\1\120\1\125"+
        "\1\110\1\103\1\116\1\103\1\122\1\102\1\120\1\114\1\116\1\107\1\105"+
        "\1\101\1\117\1\124\2\117\1\101\1\172\1\114\1\102\1\122\1\102\1\106"+
        "\1\105\1\uffff\1\127\1\117\1\172\1\101\2\uffff\1\126\1\124\1\115"+
        "\1\124\1\105\2\uffff\1\124\1\105\1\111\1\105\1\102\1\172\1\107\2"+
        "\105\1\101\1\105\1\101\1\120\1\105\1\122\1\120\1\125\1\110\1\124"+
        "\1\123\1\107\1\172\1\125\1\103\1\uffff\1\105\2\172\1\116\1\121\1"+
        "\105\1\172\1\124\1\172\1\124\1\122\1\111\1\115\1\105\1\101\2\113"+
        "\1\172\1\101\1\117\1\105\1\172\1\107\1\123\1\122\1\102\1\172\1\107"+
        "\1\115\1\103\1\122\1\uffff\1\172\1\105\1\103\1\172\1\105\1\172\1"+
        "\105\1\122\1\uffff\1\116\1\123\1\122\1\116\1\105\1\111\1\101\1\122"+
        "\1\130\1\122\1\130\1\115\1\124\1\130\1\114\1\172\1\104\1\uffff\1"+
        "\105\1\116\1\122\1\102\1\124\1\125\2\172\1\105\1\101\1\105\3\172"+
        "\1\111\1\uffff\1\105\1\110\1\122\2\uffff\1\107\1\125\1\111\1\uffff"+
        "\1\105\1\uffff\1\122\1\105\1\124\2\116\1\172\1\104\2\172\1\uffff"+
        "\1\114\1\122\1\172\1\uffff\1\105\1\124\1\62\1\172\1\uffff\1\107"+
        "\1\120\1\110\1\101\1\uffff\1\122\1\110\1\uffff\1\122\1\uffff\2\105"+
        "\1\111\2\105\1\103\1\122\1\124\1\122\2\101\1\172\1\101\1\124\1\105"+
        "\1\117\1\124\1\105\1\uffff\2\172\1\124\1\105\1\114\1\105\1\114\2"+
        "\uffff\1\107\1\172\1\122\1\116\3\uffff\1\116\1\123\1\101\2\172\1"+
        "\105\1\107\1\172\1\101\1\123\1\172\1\124\1\172\1\uffff\1\105\2\uffff"+
        "\1\172\1\101\1\120\1\uffff\1\122\1\101\1\172\1\uffff\1\111\1\122"+
        "\1\105\1\114\1\172\1\101\1\137\1\116\1\172\1\132\1\104\1\105\1\122"+
        "\1\123\1\111\1\131\1\116\1\114\1\131\1\uffff\1\116\1\105\1\116\1"+
        "\122\1\105\1\172\2\uffff\1\111\1\116\1\105\1\172\1\124\1\105\1\uffff"+
        "\1\124\1\103\1\107\1\172\1\122\2\uffff\1\172\1\116\1\uffff\1\111"+
        "\1\123\1\uffff\1\172\1\uffff\1\172\1\uffff\1\122\1\101\1\172\1\115"+
        "\1\uffff\1\116\1\105\1\172\1\114\1\uffff\1\122\1\120\1\172\1\uffff"+
        "\1\101\2\172\1\105\1\111\1\117\1\172\1\123\1\172\1\120\1\123\1\116"+
        "\1\124\1\111\1\116\1\uffff\1\117\1\103\1\172\1\uffff\2\172\1\111"+
        "\1\105\1\172\1\uffff\1\62\1\uffff\1\172\1\116\1\172\2\uffff\1\131"+
        "\1\103\1\uffff\1\120\1\107\1\123\1\uffff\1\105\1\62\1\117\1\uffff"+
        "\1\124\2\uffff\1\101\1\117\1\116\1\uffff\1\172\1\uffff\1\105\1\172"+
        "\1\124\1\172\1\116\1\124\1\116\1\105\3\uffff\1\124\1\172\1\uffff"+
        "\1\172\1\uffff\1\124\1\uffff\1\172\1\105\2\172\1\123\1\114\1\172"+
        "\1\117\1\111\1\123\1\116\1\172\1\uffff\1\172\1\uffff\1\123\1\uffff"+
        "\1\107\1\123\1\172\1\123\1\111\2\uffff\1\172\1\uffff\1\172\2\uffff"+
        "\2\172\1\uffff\1\114\1\117\1\105\2\172\2\uffff\3\172\1\uffff\1\172"+
        "\1\117\1\172\4\uffff\1\172\1\116\1\172\6\uffff\1\116\2\uffff\1\172"+
        "\1\uffff\1\172\1\uffff\1\172\2\uffff";
    static final String DFA9_acceptS =
        "\1\uffff\1\1\3\uffff\1\6\1\7\1\10\21\uffff\1\114\1\uffff\1\121\1"+
        "\122\2\uffff\1\135\1\uffff\1\137\1\uffff\1\141\1\142\1\156\1\157"+
        "\1\161\1\162\1\163\67\uffff\1\123\1\127\1\126\1\125\1\124\1\160"+
        "\1\136\1\140\35\uffff\1\46\4\uffff\1\116\1\32\5\uffff\1\133\1\130"+
        "\30\uffff\1\72\37\uffff\1\131\10\uffff\1\16\21\uffff\1\27\17\uffff"+
        "\1\71\3\uffff\1\101\1\117\3\uffff\1\120\1\uffff\1\13\11\uffff\1"+
        "\150\3\uffff\1\57\4\uffff\1\40\4\uffff\1\132\2\uffff\1\12\1\uffff"+
        "\1\147\22\uffff\1\35\7\uffff\1\77\1\151\4\uffff\1\50\1\51\1\56\15"+
        "\uffff\1\64\1\uffff\1\73\1\106\3\uffff\1\5\3\uffff\1\14\23\uffff"+
        "\1\75\6\uffff\1\143\1\47\6\uffff\1\54\5\uffff\1\76\1\102\2\uffff"+
        "\1\2\2\uffff\1\33\1\uffff\1\113\1\uffff\1\3\4\uffff\1\11\4\uffff"+
        "\1\144\3\uffff\1\153\17\uffff\1\26\3\uffff\1\34\5\uffff\1\55\1\uffff"+
        "\1\105\3\uffff\1\112\1\100\2\uffff\1\115\3\uffff\1\65\3\uffff\1"+
        "\134\1\uffff\1\21\1\22\3\uffff\1\154\1\uffff\1\37\10\uffff\1\31"+
        "\1\155\1\36\2\uffff\1\61\1\uffff\1\103\1\uffff\1\25\14\uffff\1\23"+
        "\1\uffff\1\24\1\uffff\1\30\5\uffff\1\111\1\145\1\uffff\1\4\1\uffff"+
        "\1\152\1\62\2\uffff\1\146\5\uffff\1\45\1\110\3\uffff\1\70\3\uffff"+
        "\1\15\1\20\1\63\1\66\3\uffff\1\74\1\53\1\42\1\67\1\41\1\104\1\uffff"+
        "\1\107\1\44\1\uffff\1\43\1\uffff\1\17\1\uffff\1\52\1\60";
    static final String DFA9_specialS =
        "\u022a\uffff}>";
    static final String[] DFA9_transitionS = {
            "\2\47\2\uffff\1\47\22\uffff\1\47\1\34\1\51\2\uffff\1\43\1\uffff"+
            "\1\50\1\5\1\6\1\41\1\37\1\7\1\40\1\31\1\42\12\46\1\uffff\1\1"+
            "\1\35\1\33\1\36\2\uffff\1\26\1\11\1\2\1\21\1\17\1\30\1\3\1\23"+
            "\1\15\1\45\1\12\1\24\1\16\1\10\1\13\1\14\1\45\1\20\1\22\1\4"+
            "\1\27\1\25\1\45\1\32\2\45\4\uffff\1\45\1\uffff\32\45\1\uffff"+
            "\1\44",
            "",
            "\1\55\6\uffff\1\56\3\uffff\1\53\2\uffff\1\54\2\uffff\1\52",
            "\1\57",
            "\1\61\3\uffff\1\60\2\uffff\1\62\1\64\10\uffff\1\63",
            "",
            "",
            "",
            "\1\65\1\uffff\1\66\11\uffff\1\67\5\uffff\1\70\1\71",
            "\1\75\6\uffff\1\72\10\uffff\1\73\3\uffff\1\74",
            "\1\76",
            "\1\100\3\uffff\1\77",
            "\1\102\1\uffff\1\101\16\uffff\1\103",
            "\1\104\4\uffff\1\105",
            "\1\106\7\uffff\1\110\5\uffff\1\107",
            "\1\111",
            "\1\113\3\uffff\1\114\11\uffff\1\112",
            "\1\120\3\uffff\1\116\3\uffff\1\115\10\uffff\1\117",
            "\1\123\16\uffff\1\121\1\122",
            "\1\124",
            "\1\126\3\uffff\1\125\5\uffff\1\127",
            "\1\130",
            "\1\133\7\uffff\1\132\1\uffff\1\134\4\uffff\1\131",
            "\1\136\4\uffff\1\135",
            "\1\137",
            "",
            "\1\140",
            "",
            "",
            "\1\142\1\141",
            "\1\144",
            "",
            "\1\146",
            "",
            "\1\146",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\151",
            "\1\152",
            "\1\155\1\154\1\153",
            "\1\156\17\uffff\1\157",
            "\1\162\3\uffff\1\161\17\uffff\1\160",
            "\1\163",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "\1\170",
            "\1\171\3\uffff\1\172",
            "\1\173",
            "\1\175\10\uffff\1\174\3\uffff\1\176\3\uffff\1\177",
            "\1\u0080\1\u0081",
            "\1\u0082",
            "\1\u0083",
            "\1\u0084",
            "\1\45\13\uffff\12\45\7\uffff\23\45\1\u0085\6\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "\1\u0088\15\uffff\1\u0087",
            "\1\u0089",
            "\1\45\13\uffff\12\45\7\uffff\6\45\1\u008a\23\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u008d",
            "\1\u008e",
            "\1\u008f",
            "\1\45\13\uffff\12\45\7\uffff\3\45\1\u0091\4\45\1\u0090\21\45"+
            "\4\uffff\1\45\1\uffff\32\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0094",
            "\1\u0096\7\uffff\1\u0095",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "\1\u009a",
            "\1\u009c\15\uffff\1\u009b",
            "\1\u009d",
            "\1\u009f\5\uffff\1\u009e",
            "\1\u00a0",
            "\1\u00a1",
            "\1\u00a2",
            "\1\u00a3",
            "\1\u00a4",
            "\1\u00a5",
            "\1\u00a6",
            "\1\u00a7",
            "\1\u00a9\4\uffff\1\u00a8",
            "\1\u00aa\5\uffff\1\u00ab",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u00ad",
            "\1\u00ae",
            "\1\u00af",
            "\1\u00b0",
            "\1\u00b1",
            "\1\u00b2",
            "\1\u00b3",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u00b4",
            "\1\u00b5",
            "\1\u00b6",
            "\1\u00b8\2\uffff\1\u00b7",
            "\1\u00b9",
            "\1\u00ba",
            "\1\u00bb",
            "\1\u00bc",
            "\1\u00bd",
            "\1\u00be",
            "\1\u00bf",
            "\1\u00c0",
            "\1\u00c1",
            "\1\u00c2",
            "\1\u00c3",
            "\1\u00c4",
            "\1\u00c5",
            "\1\u00c6",
            "\1\u00c7",
            "\1\u00c8",
            "\1\u00ca\15\uffff\1\u00c9",
            "\1\u00cb",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u00cd",
            "\1\u00ce",
            "\1\u00cf",
            "\1\u00d0",
            "\1\u00d1",
            "\1\u00d2",
            "",
            "\1\u00d3",
            "\1\u00d4",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u00d6",
            "",
            "",
            "\1\u00d8\2\uffff\1\u00d9\13\uffff\1\u00d7\1\u00da",
            "\1\u00db",
            "\1\u00dc",
            "\1\u00dd",
            "\1\u00de",
            "",
            "",
            "\1\u00e0\16\uffff\1\u00df",
            "\1\u00e1",
            "\1\u00e2",
            "\1\u00e3",
            "\1\u00e4",
            "\1\45\13\uffff\12\45\7\uffff\10\45\1\u00e6\11\45\1\u00e5\7"+
            "\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00e8",
            "\1\u00e9",
            "\1\u00ea",
            "\1\u00eb",
            "\1\u00ec",
            "\1\u00ed",
            "\1\u00ee",
            "\1\u00ef",
            "\1\u00f0",
            "\1\u00f1",
            "\1\u00f2",
            "\1\u00f3",
            "\1\u00f4",
            "\1\u00f5",
            "\1\u00f6",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u00f8",
            "\1\u00f9",
            "",
            "\1\u00fa",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u00fd",
            "\1\u00fe",
            "\1\u00ff",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0101",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0103",
            "\1\u0104",
            "\1\u0106\3\uffff\1\u0105",
            "\1\u0107",
            "\1\u0108",
            "\1\u0109",
            "\1\u010a",
            "\1\u010b",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u010d",
            "\1\u010e",
            "\1\u010f",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0111",
            "\1\u0112",
            "\1\u0113",
            "\1\u0114",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0116",
            "\1\u0117",
            "\1\u0118",
            "\1\u0119",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u011b",
            "\1\u011c",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u011e",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0120",
            "\1\u0121",
            "",
            "\1\u0122",
            "\1\u0123",
            "\1\u0124",
            "\1\u0125",
            "\1\u0126",
            "\1\u0127",
            "\1\u0128",
            "\1\u012a\10\uffff\1\u0129",
            "\1\u012b",
            "\1\u012c",
            "\1\u012d",
            "\1\u012e",
            "\1\u012f",
            "\1\u0130",
            "\1\u0131",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0133",
            "",
            "\1\u0134",
            "\1\u0135",
            "\1\u0136",
            "\1\u0137",
            "\1\u0138",
            "\1\u0139",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u013c\3\uffff\1\u013d",
            "\1\u013e",
            "\1\u013f",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0143",
            "",
            "\1\u0144",
            "\1\u0145",
            "\1\u0146",
            "",
            "",
            "\1\u0147",
            "\1\u0148",
            "\1\u0149",
            "",
            "\1\u014a",
            "",
            "\1\u014b",
            "\1\u014c",
            "\1\u014d",
            "\1\u014e",
            "\1\u014f",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0151",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\u0154",
            "\1\u0155",
            "\1\45\13\uffff\12\45\7\uffff\22\45\1\u0156\7\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "",
            "\1\u0158",
            "\1\u0159",
            "\1\u015a",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\u015c",
            "\1\u015d",
            "\1\u015e",
            "\1\u015f",
            "",
            "\1\u0160",
            "\1\u0161",
            "",
            "\1\u0162",
            "",
            "\1\u0163",
            "\1\u0164",
            "\1\u0165",
            "\1\u0166",
            "\1\u0167",
            "\1\u0168",
            "\1\u0169",
            "\1\u016a",
            "\1\u016b",
            "\1\u016c",
            "\1\u016d",
            "\1\45\13\uffff\12\45\7\uffff\23\45\1\u016e\6\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "\1\u0170",
            "\1\u0171",
            "\1\u0172",
            "\1\u0173",
            "\1\u0174",
            "\1\u0175",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0178",
            "\1\u0179",
            "\1\u017a",
            "\1\u017b",
            "\1\u017c",
            "",
            "",
            "\1\u017d",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u017f",
            "\1\u0180",
            "",
            "",
            "",
            "\1\u0181",
            "\1\u0182",
            "\1\u0183",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0186",
            "\1\u0187",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0189",
            "\1\u018a",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u018c",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\u018e",
            "",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0190",
            "\1\u0191",
            "",
            "\1\u0192",
            "\1\u0193",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\u0195",
            "\1\u0196",
            "\1\u0197",
            "\1\u0198",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u019a",
            "\1\u019b",
            "\1\u019c",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u019e",
            "\1\u019f",
            "\1\u01a0",
            "\1\u01a1",
            "\1\u01a2",
            "\1\u01a3",
            "\1\u01a4",
            "\1\u01a5",
            "\1\u01a6",
            "\1\u01a7",
            "",
            "\1\u01a8",
            "\1\u01a9",
            "\1\u01aa",
            "\1\u01ab",
            "\1\u01ac",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "",
            "\1\u01ae",
            "\1\u01af",
            "\1\u01b0",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01b2",
            "\1\u01b3",
            "",
            "\1\u01b4",
            "\1\u01b5",
            "\1\u01b6",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01b8",
            "",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01ba",
            "",
            "\1\u01bb",
            "\1\u01bc",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\u01bf",
            "\1\u01c0",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01c2",
            "",
            "\1\u01c3",
            "\1\u01c4",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01c6",
            "",
            "\1\u01c7",
            "\1\u01c8",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\u01ca",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01cd",
            "\1\u01ce",
            "\1\u01cf",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01d1",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01d3",
            "\1\u01d4",
            "\1\u01d5",
            "\1\u01d6",
            "\1\u01d7",
            "\1\u01d8",
            "",
            "\1\u01d9",
            "\1\u01da",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01de",
            "\1\u01df",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\u01e1",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01e3",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "",
            "\1\u01e5",
            "\1\u01e6",
            "",
            "\1\u01e7",
            "\1\u01e8",
            "\1\u01e9",
            "",
            "\1\u01ea",
            "\1\u01eb",
            "\1\u01ec",
            "",
            "\1\u01ed",
            "",
            "",
            "\1\u01ee",
            "\1\u01ef",
            "\1\u01f0",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\u01f2",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01f4",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01f6",
            "\1\u01f7",
            "\1\u01f8",
            "\1\u01f9",
            "",
            "",
            "",
            "\1\u01fa",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\u01fd",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u01ff",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0202",
            "\1\u0203",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0205",
            "\1\u0206",
            "\1\u0207",
            "\1\u0208",
            "\1\45\13\uffff\12\45\7\uffff\22\45\1\u0209\7\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\u020c",
            "",
            "\1\u020d",
            "\1\u020e",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0210",
            "\1\u0211",
            "",
            "",
            "\1\45\13\uffff\12\45\7\uffff\22\45\1\u0212\7\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\u0217",
            "\1\u0218",
            "\1\u0219",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0220",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "",
            "",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "\1\u0223",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\u0225",
            "",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            "\1\45\13\uffff\12\45\7\uffff\22\45\1\u0227\7\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "",
            "\1\45\13\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32"+
            "\45",
            "",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__61 | T__62 | T__63 | T__64 | T__65 | T__66 | T__67 | T__68 | T__69 | T__70 | T__71 | T__72 | T__73 | T__74 | T__75 | T__76 | T__77 | T__78 | T__79 | T__80 | T__81 | T__82 | T__83 | T__84 | T__85 | T__86 | T__87 | T__88 | T__89 | T__90 | T__91 | T__92 | T__93 | T__94 | T__95 | T__96 | T__97 | T__98 | T__99 | T__100 | T__101 | T__102 | T__103 | T__104 | T__105 | T__106 | T__107 | T__108 | T__109 | T__110 | T__111 | T__112 | T__113 | T__114 | T__115 | T__116 | T__117 | T__118 | T__119 | T__120 | T__121 | T__122 | T__123 | T__124 | T__125 | T__126 | T__127 | T__128 | T__129 | T__130 | T__131 | T__132 | T__133 | T__134 | T__135 | T__136 | T__137 | OR | AND | XOR | EQ | BANGEQ | LTGT | GT | GE | LT | LE | IS | NOT | NULL | IN | BETWEEN | PLUS | SUB | STAR | SLASH | PERCENT | BARBAR | ROWID | NUMBER | VARCHAR2 | NVARCHAR2 | BYTE | CHAR | DATE | TIMESTAMP | BEFORE | PRIMARY | DEFAULT | IDENTIFIER | INT | COMMENT | WS | STRING | QUOTED_NAME );";
        }
    }
 

}