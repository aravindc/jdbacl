/*
 * (c) Copyright 2010-2011 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.databene.jdbacl.sql;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.databene.commons.ArrayBuilder;
import org.databene.commons.Expression;
import org.databene.commons.ParseException;
import org.databene.commons.StringUtil;
import org.databene.commons.SyntaxError;
import org.databene.commons.expression.ValueCollectionContainsExpression;
import org.databene.commons.expression.ConcatExpression;
import org.databene.commons.expression.ConditionalAndExpression;
import org.databene.commons.expression.ConditionalOrExpression;
import org.databene.commons.expression.DivisionExpression;
import org.databene.commons.expression.EqualsExpression;
import org.databene.commons.expression.ExclusiveOrExpression;
import org.databene.commons.expression.ExpressionUtil;
import org.databene.commons.expression.GreaterExpression;
import org.databene.commons.expression.GreaterOrEqualsExpression;
import org.databene.commons.expression.LessExpression;
import org.databene.commons.expression.LessOrEqualsExpression;
import org.databene.commons.expression.LogicalComplementExpression;
import org.databene.commons.expression.ModuloExpression;
import org.databene.commons.expression.MultiplicationExpression;
import org.databene.commons.expression.NotEqualsExpression;
import org.databene.commons.expression.NullExpression;
import org.databene.commons.expression.SubtractionExpression;
import org.databene.commons.expression.SumExpression;
import org.databene.commons.expression.UnaryMinusExpression;
import org.databene.jdbacl.model.DBColumnType;
import org.databene.jdbacl.model.DBPrimaryKeyConstraint;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DefaultDBColumn;
import org.databene.jdbacl.model.DefaultDBTable;
import org.databene.jdbacl.sql.parser.ANTLRNoCaseStringStream;
import org.databene.jdbacl.sql.parser.SQLLexer;
import org.databene.jdbacl.sql.parser.SQLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Document class.<br/><br/>
 * Created: 05.08.2010 10:19:38
 * @since 0.1
 * @author Volker Bergmann
 */
public class SQLParserUtil {
	
	static final Logger LOGGER = LoggerFactory.getLogger(SQLParserUtil.class);

    public static Object parse(CharStream in) throws ParseException {
    	String text = ""; // TODO no text available due to Reader
        try {
        	SQLParser parser = parser(in);
        	SQLParser.commands_return r = parser.commands();
	        checkForSyntaxErrors(text, "weightedLiteralList", parser, r);
	        if (r != null) {
	        	return convertNode((CommonTree) r.getTree());
	        } else
	        	return null;
        } catch (RuntimeException e) {
        	if (e.getCause() instanceof RecognitionException)
        		throw mapToParseException((RecognitionException) e.getCause(), text);
        	else
        		throw e;
        } catch (RecognitionException e) {
        	throw mapToParseException(e, text);
        }
    }
	
    public static Expression<?> parseExpression(CharStream in) throws ParseException {
    	String text = (in instanceof ANTLRNoCaseStringStream ? in.toString() : null);
        try {
        	SQLParser parser = parser(in);
        	SQLParser.expression_return r = parser.expression();
	        checkForSyntaxErrors(text, "expression", parser, r);
	        if (r != null) {
	        	return convertExpressionNode((CommonTree) r.getTree());
	        } else
	        	return null;
        } catch (RuntimeException e) {
        	if (e.getCause() instanceof RecognitionException)
        		throw mapToParseException((RecognitionException) e.getCause(), text);
        	else
        		throw e;
        } catch (RecognitionException e) {
        	throw mapToParseException(e, text);
        }
    }
	
	private static Object convertNode(CommonTree node) {
    	switch (node.getType()) {
			case SQLLexer.CREATE_TABLE: return convertCreateTable(node);
			case SQLLexer.DROP_TABLE: return convertDropTable(node);
			case SQLLexer.ALTER_TABLE: return convertAlterTable(node);
			case SQLLexer.CREATE_SEQUENCE: return convertCreateSequence(node);
			case SQLLexer.DROP_SEQUENCE: return convertDropSequence(node);
			case SQLLexer.CREATE_INDEX: return convertCreateIndex(node);
			case SQLLexer.COMMENT_TABLE: return convertTableComment(node);
			case SQLLexer.COMMENT_COLUMN: return convertColumnComment(node);
		}
    	if (node.isNil()) {
    		List<Object> nodes = convertNodes(getChildNodes(node));
    		return nodes.toArray();
    	}
		throw new ParseException("Unknown token type", "'" + node.getText() + "'");
    }

	@SuppressWarnings("rawtypes")
	private static Expression convertExpressionNode(CommonTree node) {
    	switch (node.getType()) {
			case SQLLexer.OR: return convertOr(node);
			case SQLLexer.AND: return convertAnd(node);
			case SQLLexer.XOR: return convertXor(node);
			case SQLLexer.EQ: return convertEq(node);
			case SQLLexer.BANGEQ: return convertBangEq(node);
			case SQLLexer.LTGT: return convertBangEq(node); // <>
			case SQLLexer.GT: return convertGt(node);
			case SQLLexer.GE: return convertGe(node);
			case SQLLexer.LT: return convertLt(node);
			case SQLLexer.LE: return convertLe(node);
			case SQLLexer.IS: return convertIs(node);
			case SQLLexer.NOT: return convertNot(node);
			case SQLLexer.NULL: return convertNull(node);
			case SQLLexer.IN: return convertIn(node);
			case SQLLexer.BETWEEN: return convertBetween(node);
			case SQLLexer.PLUS: return convertPlus(node);
			case SQLLexer.SUB: return convertSub(node);
			case SQLLexer.STAR: return convertStar(node);
			case SQLLexer.SLASH: return convertSlash(node);
			case SQLLexer.PERCENT: return convertPercent(node);
			case SQLLexer.BARBAR: return convertBarBar(node);
			case SQLLexer.INVOCATION: return convertInvocation(node);
			case SQLLexer.QUOTED_NAME: return convertQuotedName(node);
			case SQLLexer.IDENTIFIER: return convertIdentifier(node);
			case SQLLexer.STRING: return convertStringToExpression(node);
			case SQLLexer.INT: return convertInt(node);
		}
    	/* TODO what's this?
    	if (node.isNil()) {
    		List<Object> nodes = convertNodes(getChildNodes(node));
    		return nodes.toArray();
    	}
    	*/
		throw new ParseException("Unknown token type (" + node.getType() + ")", "'" + node.getText() + "'");
    }

	@SuppressWarnings("unchecked")
	private static Expression<Boolean> convertOr(CommonTree node) {
		ConditionalOrExpression result = new ConditionalOrExpression("OR");
		for (CommonTree childNode : getChildNodes(node))
				result.addTerm(convertExpressionNode(childNode));
		return result;
	}

	@SuppressWarnings("unchecked")
	private static Expression<Boolean> convertAnd(CommonTree node) {
		ConditionalAndExpression result = new ConditionalAndExpression("AND");
		for (CommonTree childNode : getChildNodes(node))
				result.addTerm(convertExpressionNode(childNode));
		return result;
	}

	private static Expression<Boolean> convertXor(CommonTree node) {
		return new ExclusiveOrExpression(
				convertExpressionNode(childAt(0, node)), 
				convertExpressionNode(childAt(1, node)));
	}

	private static Expression<Boolean> convertEq(CommonTree node) {
		return new EqualsExpression("=", 
				convertExpressionNode(childAt(0, node)), convertExpressionNode(childAt(1, node)));
	}

	private static Expression<Boolean> convertBangEq(CommonTree node) {
		return new NotEqualsExpression(convertExpressionNode(childAt(0, node)), convertExpressionNode(childAt(1, node)));
	}

	private static Expression<Boolean> convertGt(CommonTree node) {
		return new GreaterExpression(convertExpressionNode(childAt(0, node)), convertExpressionNode(childAt(1, node)));
	}

	private static Expression<Boolean> convertGe(CommonTree node) {
		return new GreaterOrEqualsExpression(convertExpressionNode(childAt(0, node)), convertExpressionNode(childAt(1, node)));
	}

	private static Expression<Boolean> convertLt(CommonTree node) {
		return new LessExpression(convertExpressionNode(childAt(0, node)), convertExpressionNode(childAt(1, node)));
	}

	private static Expression<Boolean> convertLe(CommonTree node) {
		return new LessOrEqualsExpression(convertExpressionNode(childAt(0, node)), convertExpressionNode(childAt(1, node)));
	}

	private static Expression<Boolean> convertIs(CommonTree node) {
		return new EqualsExpression("IS", convertExpressionNode(childAt(0, node)), convertExpressionNode(childAt(1, node)));
	}

	private static Expression<?> convertNot(CommonTree node) {
		return new LogicalComplementExpression("NOT ", convertExpressionNode(childAt(0, node)));
	}

	private static Expression<?> convertNull(CommonTree node) {
		return new NullExpression();
	}

	private static Expression<?> convertIn(CommonTree node) {
		return new ValueCollectionContainsExpression(
				"IN", convertExpressionNode(childAt(0, node)), convertValueList(childAt(1, node)));
	}

	private static Expression<? extends Collection<?>> convertValueList(CommonTree node) {
		List<Expression<?>> result = new ArrayList<Expression<?>>();
		for (CommonTree child : getChildNodes(node))
			result.add(convertExpressionNode(child));
		return ExpressionUtil.constant(result);
	}

	private static Expression<?> convertBetween(CommonTree node) {
		return new BetweenExpression(convertExpressionNode(childAt(0, node)), 
				convertExpressionNode(childAt(1, node)), 
				convertExpressionNode(childAt(2, node)));
	}

	@SuppressWarnings("unchecked")
	private static Expression<?> convertPlus(CommonTree node) {
		SumExpression result = new SumExpression();
		for (CommonTree child : getChildNodes(node))
			result.addTerm(convertExpressionNode(child));
		return result;
	}

	@SuppressWarnings("unchecked")
	private static Expression<?> convertSub(CommonTree node) {
		if (node.getChildCount() == 1) {
			return new UnaryMinusExpression<Object>(convertExpressionNode(childAt(0, node)));
		} else {
			SubtractionExpression result = new SubtractionExpression();
			for (CommonTree child : getChildNodes(node))
				result.addTerm(convertExpressionNode(child));
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	private static Expression<?> convertStar(CommonTree node) {
		MultiplicationExpression result = new MultiplicationExpression();
		for (CommonTree child : getChildNodes(node))
			result.addTerm(convertExpressionNode(child));
		return result;
	}

	@SuppressWarnings("unchecked")
	private static Expression<?> convertSlash(CommonTree node) {
		DivisionExpression result = new DivisionExpression();
		for (CommonTree child : getChildNodes(node))
			result.addTerm(convertExpressionNode(child));
		return result;
	}

	private static Expression<?> convertPercent(CommonTree node) {
		return new ModuloExpression(
				convertExpressionNode(childAt(0, node)), 
				convertExpressionNode(childAt(1, node)));
	}

	private static Expression<String> convertBarBar(CommonTree node) {
		return new ConcatExpression("||", 
				convertExpressionNode(childAt(0, node)), 
				convertExpressionNode(childAt(1, node)));
	}

	private static Expression<?> convertInvocation(CommonTree node) {
		String functionName = convertIdentifier(childAt(0, node)).evaluate(null);
		Expression<?>[] arguments = convertArguments(childAt(1, node));
		return new FunctionInvocation(functionName, arguments);
	}

	@SuppressWarnings("rawtypes")
	private static Expression<?>[] convertArguments(CommonTree node) {
		ArrayBuilder<Expression> result = new ArrayBuilder<Expression>(Expression.class);
		for (CommonTree child : getChildNodes(node))
			result.add(convertExpressionNode(child));
		return result.toArray();
	}

	private static Expression<String> convertIdentifier(CommonTree node) {
		return new ColumnExpression(node.getText(), false);
	}

	private static Expression<String> convertQuotedName(CommonTree node) {
		String quotedColName = node.getText().trim();
		String colName = quotedColName.substring(1, quotedColName.length() - 1);
		return new ColumnExpression(colName, true);
	}

	private static Expression<?> convertStringToExpression(CommonTree node) {
		return ExpressionUtil.constant(node.getText());
	}

	private static Expression<?> convertInt(CommonTree node) {
		return ExpressionUtil.constant(new BigInteger(node.getText()));
	}

	private static Object convertTableComment(CommonTree node) {
	    // TODO Auto-generated method stub
	    return null;
    }

	private static Object convertColumnComment(CommonTree node) {
	    // TODO Auto-generated method stub
	    return null;
    }

	private static Object convertAlterTable(CommonTree node) {
	    // TODO Auto-generated method stub
	    return null;
    }

	private static Object convertDropTable(CommonTree node) {
	    // TODO Auto-generated method stub
	    return null;
    }

	private static Object convertCreateSequence(CommonTree node) {
	    // TODO Auto-generated method stub
	    return null;
    }

	private static Object convertCreateIndex(CommonTree node) {
	    // TODO Auto-generated method stub
	    return null;
    }

	private static Object convertDropSequence(CommonTree node) {
	    // TODO Auto-generated method stub
	    return null;
    }

	private static List<Object> convertNodes(List<CommonTree> nodes) {
	    List<Object> result = new ArrayList<Object>();
	    for (CommonTree node : nodes)
	    	result.add(convertNode(node));
	    return result;
    }

	private static DBTable convertCreateTable(CommonTree node) {
		String tableName = convertString(childAt(0, node));
		DefaultDBTable table = new DefaultDBTable(tableName);
		convertTableDetails(childAt(1, node), table);
		// TODO parse ora_configs
	    return table;
    }

	private static void convertTableDetails(CommonTree node, DefaultDBTable table) {
		for (CommonTree subNode : getChildNodes(node))
			convertTableDetail(subNode, table);
    }

	private static void convertTableDetail(CommonTree node, DefaultDBTable table) {
		switch (node.getType()) {
			case SQLLexer.COLUMN_SPEC: convertColumnSpec(node, table); break;
			case SQLLexer.PRIMARY: convertInlinePK(node, table); break;
			default: throw new ParseException("Unknown table detail token type", 
					String.valueOf(node.getText()), 
					node.getLine(), 
					node.getCharPositionInLine());
		}
    }

	private static void convertInlinePK(CommonTree node, DefaultDBTable table) {
	    String constraintName = convertString(childAt(0, node));
	    String[] pkColumnNames = convertNameList(childAt(1, node));
	    DBPrimaryKeyConstraint pk = new DBPrimaryKeyConstraint(table, constraintName, pkColumnNames);
	    table.setPrimaryKey(pk);
    }

	private static String[] convertNameList(CommonTree node) {
		String[] result = new String[node.getChildCount()];
		for (int i = 0; i < result.length; i++)
			result[i] = convertString(childAt(i, node));
		return result;
    }

	private static void convertColumnSpec(CommonTree node, DefaultDBTable table) {
		String columnName = convertString(childAt(0, node));
		String columnTypeName;
		Integer size = null;
		Integer fractionDigits = null;
		int detailOffset = 2;
		columnTypeName = convertString(childAt(1, node));
		if (node.getChildCount() > 2 && childAt(2, node).getType() == SQLLexer.SIZE) {
			detailOffset++;
			CommonTree sizeNode = childAt(2, node);
			size = convertInteger(childAt(0, sizeNode));
			if (sizeNode.getChildCount() > 1) {
				CommonTree subNode2 = childAt(1, sizeNode);
				if (subNode2.getType() == SQLLexer.INT) {
					fractionDigits = convertInteger(subNode2);
				} else {
					// TODO support (n BYTE) / (n CHAR)
				}
			}
		}
		DefaultDBColumn column = new DefaultDBColumn(columnName, table, DBColumnType.getInstance(/* TODO */ -1, columnTypeName), size, fractionDigits);
		table.addColumn(column);
	    for (int i = detailOffset; i < node.getChildCount(); i++)
	    	convertColumnDetail(childAt(i, node), column);
    }

	private static Integer convertInteger(CommonTree node) {
	    return Integer.parseInt(node.getText());
    }

	private static void convertColumnDetail(CommonTree node, DefaultDBColumn column) {
		switch (node.getType()) {
			case SQLLexer.NOT : column.setNullable(false); break;
			case SQLLexer.DEFAULT : column.setNullable(false); break;
			default: throw new ParseException("Unknown column detail token type", 
					String.valueOf(node.getText()), 
					node.getLine(), 
					node.getCharPositionInLine());
		}
	    // TODO Auto-generated method stub
    }

	private static String convertString(CommonTree node) {
	    return node.getText();
    }

	private static SQLParser parser(CharStream in) {
		SQLLexer lex = new SQLLexer(in);
	    CommonTokenStream tokens = new CommonTokenStream(lex);
	    SQLParser parser = new SQLParser(tokens);
	    return parser;
    }

    private static ParseException mapToParseException(RecognitionException cause, String text) {
    	return new ParseException("Error parsing SQL", cause, 
    			text, cause.line, cause.charPositionInLine);
    }

    @SuppressWarnings("unchecked")
    private static List<CommonTree> getChildNodes(CommonTree node) {
	    return node.getChildren();
    }

    private static CommonTree childAt(int index, CommonTree node) {
	    return (CommonTree) node.getChild(index);
    }

	private static void checkForSyntaxErrors(String text, String type,
			SQLParser parser, ParserRuleReturnScope r) {
		if (parser.getNumberOfSyntaxErrors() > 0)
			throw new SyntaxError("Illegal " + type, text, -1, -1);
		CommonToken stop = (CommonToken) r.stop;
		if (text != null && stop.getStopIndex() < StringUtil.trimRight(text).length() - 1) {
			if (stop.getStopIndex() == 0)
				throw new SyntaxError("Syntax error after " + stop.getText(), text);
			else
				throw new SyntaxError("Syntax error at the beginning ", text);
		}
	}
	
}