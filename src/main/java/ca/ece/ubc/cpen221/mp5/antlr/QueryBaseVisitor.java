// Generated from Query.g4 by ANTLR 4.7
package ca.ece.ubc.cpen221.mp5.antlr;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import ca.ece.ubc.cpen221.mp5.YelpDB;
import ca.ece.ubc.cpen221.mp5.antlr.QueryParser.*;
import ca.ece.ubc.cpen221.mp5.datatypes.YelpRestaurant;

/**
 * Edited QueryVisitorBase from the auto-generated class to execute the query
 * string that was just parsed
 */
public class QueryBaseVisitor extends AbstractParseTreeVisitor<Set<YelpRestaurant>>
		implements QueryVisitor<Set<YelpRestaurant>> {

	private YelpDB yelpDB;

	/*
	 * Abstraction Function: a YelpDB for the visitor to perform the query on
	 * 
	 * Rep Invariant: yelpDB is not null
	 */

	public QueryBaseVisitor(YelpDB yelpDB) {
		this.yelpDB = yelpDB;
	}

	/**
	 * Figures out what type of child it needs to visit next
	 * 
	 * @param ctx
	 *            context of this query base node
	 * @return the return value of the child it visits
	 */
	@Override
	public Set<YelpRestaurant> visitQuery(QueryParser.QueryContext ctx) {
		if (ctx.getChild(0) instanceof QueryParser.OrExprContext)
			return visitOrExpr((OrExprContext) ctx.getChild(0));

		else if (ctx.getChild(0) instanceof QueryParser.AndExprContext)
			return visitAndExpr((AndExprContext) ctx.getChild(0));

		else
			return visitAtom((AtomContext) ctx.getChild(0));
	}

	/**
	 * @param ctx
	 *            context of this node
	 * @return union of the return values of the children
	 */
	@Override
	public Set<YelpRestaurant> visitOrExpr(QueryParser.OrExprContext ctx) {
		Set<YelpRestaurant> retSet = new HashSet<YelpRestaurant>();

		for (int i = 0; i < ctx.getChildCount(); i += 2)
			retSet.addAll(visitAndExpr((QueryParser.AndExprContext) ctx.getChild(i)));

		return retSet;
	}

	/**
	 * @param ctx
	 *            context of this node
	 * @return the intersection of return values of the children
	 */
	@Override
	public Set<YelpRestaurant> visitAndExpr(QueryParser.AndExprContext ctx) {
		Set<YelpRestaurant> retSet = yelpDB.getRestaurants();

		for (int i = 0; i < ctx.getChildCount(); i += 2)
			retSet.retainAll(visitAtom((QueryParser.AtomContext) ctx.getChild(i)));

		return retSet;
	}

	/**
	 * Figures out the type of child and visits that child
	 * 
	 * @param ctx
	 *            context of this node
	 * @return the return value of the child that it visits
	 */
	@Override
	public Set<YelpRestaurant> visitAtom(QueryParser.AtomContext ctx) {
		if (ctx.getChild(0) instanceof InContext)
			return visitIn((InContext) ctx.getChild(0));

		else if (ctx.getChild(0) instanceof CategoryContext)
			return visitCategory((CategoryContext) ctx.getChild(0));

		else if (ctx.getChild(0) instanceof NameContext)
			return visitName((NameContext) ctx.getChild(0));

		else if (ctx.getChild(0) instanceof PriceContext)
			return visitPrice((PriceContext) ctx.getChild(0));

		else if (ctx.getChild(0) instanceof RatingContext)
			return visitRating((RatingContext) ctx.getChild(0));

		else
			return visitOrExpr((OrExprContext) ctx.getChild(1));
	}

	/**
	 * Filters the set of all restaurants in the database to just those in the
	 * neighborhood specified by the node context
	 * 
	 * @param ctx
	 *            the context of this node
	 * @return set of yelp restaurants in the specified neighborhood
	 */
	@Override
	public Set<YelpRestaurant> visitIn(InContext ctx) {
		String text = ctx.getChild(1).getText();
		String testText = text.substring(1, text.length() - 1);

		Stream<YelpRestaurant> retStream = yelpDB.getRestaurants().stream()
				.filter(yr -> new HashSet<String>(Arrays.asList(yr.getNeighborhoods())).contains(testText));
		Set<YelpRestaurant> retSet = retStream.collect(Collectors.toCollection(HashSet::new));

		return retSet;
	}

	/**
	 * Filters the set of restaurants contained in yelpDB to just those that fit
	 * into the category specified by ctx
	 * 
	 * @param ctx
	 *            the context of this node
	 * @return set of yelp restaurants that fall under the specified category
	 */
	@Override
	public Set<YelpRestaurant> visitCategory(CategoryContext ctx) {
		String text = ctx.getChild(1).getText();
		String testText = text.substring(1, text.length() - 1);

		Stream<YelpRestaurant> retStream = yelpDB.getRestaurants().stream()
				.filter(yr -> new HashSet<String>(Arrays.asList(yr.getCategories())).contains(testText));
		Set<YelpRestaurant> retSet = retStream.collect(Collectors.toCollection(HashSet::new));

		return retSet;
	}

	/**
	 * Finds the restaurant(s) with the name specified by ctx
	 * 
	 * @param ctx
	 *            the context of this node
	 * @return set of yelp restaurants whose name matches the specified name
	 */
	@Override
	public Set<YelpRestaurant> visitName(NameContext ctx) {
		String text = ctx.getChild(1).getText();
		String testText = text.substring(1, text.length() - 1);

		Stream<YelpRestaurant> retStream = yelpDB.getRestaurants().stream().filter(yr -> yr.getName().equals(testText));
		Set<YelpRestaurant> retSet = retStream.collect(Collectors.toCollection(HashSet::new));

		return retSet;
	}

	/**
	 * Filters all restaurants in the yelpDB to just those whose ratings fit in the
	 * range specified by ctx
	 * 
	 * @param ctx
	 *            the context of this node
	 * @return set of yelp restaurants that fall under the specified rating range
	 */
	@Override
	public Set<YelpRestaurant> visitRating(RatingContext ctx) {

		String text = ctx.getChild(2).getText();
		String ineq = ctx.getChild(1).getText();
		double rating = Double.parseDouble(text);

		Predicate<YelpRestaurant> predicate;

		switch (ineq) {
		case ">":
			predicate = (yr -> yr.getStars() > rating);
			break;
		case ">=":
			predicate = (yr -> yr.getStars() >= rating);
			break;
		case "<":
			predicate = (yr -> yr.getStars() < rating);
			break;
		case "<=":
			predicate = (yr -> yr.getStars() <= rating);
			break;
		default:
			predicate = (yr -> yr.getStars() == rating);
			break;
		}

		Stream<YelpRestaurant> retStream = yelpDB.getRestaurants().stream().filter(predicate);
		Set<YelpRestaurant> retSet = retStream.collect(Collectors.toCollection(HashSet::new));
		return retSet;
	}

	/**
	 * Filters all restaurants in yelpDB to just those whose pricing is in the range
	 * specified by ctx
	 * 
	 * @param ctx
	 *            context of this node
	 * @return set of yelp restaurants whose pricing falls under the specified range
	 */
	@Override
	public Set<YelpRestaurant> visitPrice(PriceContext ctx) {
		String text = ctx.getChild(2).getText();
		String ineq = ctx.getChild(1).getText();
		double price = Double.parseDouble(text);

		Predicate<YelpRestaurant> predicate;

		switch (ineq) {
		case ">":
			predicate = (yr -> yr.getPrice() > price);
			break;
		case ">=":
			predicate = (yr -> yr.getPrice() >= price);
			break;
		case "<":
			predicate = (yr -> yr.getPrice() < price);
			break;
		case "<=":
			predicate = (yr -> yr.getPrice() <= price);
			break;
		default:
			predicate = (yr -> yr.getPrice() == price);
			break;
		}

		Stream<YelpRestaurant> retStream = yelpDB.getRestaurants().stream().filter(predicate);
		Set<YelpRestaurant> retSet = retStream.collect(Collectors.toCollection(HashSet::new));
		return retSet;
	}
}