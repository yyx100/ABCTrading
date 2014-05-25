package com.agilebc.trading;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agilebc.data.trade.ExchangeFee;
import com.agilebc.data.trade.OrderBook;
import com.agilebc.data.trade.OrderBookElm;
import com.agilebc.data.trade.OrderDepth;
import com.agilebc.data.trade.TradeElement;
import com.agilebc.data.trade.TradeResult;
import com.agilebc.util.TradeType;

import static org.junit.Assert.*;

public class TradeCalculatorTest {
	public static Logger applog = LoggerFactory.getLogger(TradeCalculatorTest.class);

	public final static double _DTOLERANCE = 0.000001d;
	
	
	private OrderBook ob = null;
	private ExchangeFee fee = null;
	
	@Before
	public void initOrderBook () {
		ob = new OrderBook();
		OrderDepth odBuy = new OrderDepth(TradeType.BUY);
		OrderDepth odSell = new OrderDepth(TradeType.SELL);
		
		ob.addOrderDepth(odBuy);
		ob.addOrderDepth(odSell);

		odBuy.addOrder(0.028, 7);   //2 0.196	18	0.515
		odBuy.addOrder(0.025, 53);  //5 1.325	115	2.997
		odBuy.addOrder(0.029, 11);	//1 0.319	11	0.319
		odBuy.addOrder(0.027, 13);  //3 0.351	31	0.866
		odBuy.addOrder(0.026, 31);  //4 0.806	62	1.672

		odSell.addOrder(0.035, 43);    //5	total=1.505  accumulated=2.671
		odSell.addOrder(0.033, 5);     //2	total=0.165  accumulated=0.792
		odSell.addOrder(0.032, 7);     //3	total=0.224  accumulated=0.627
		odSell.addOrder(0.031, 13);    //1	total=0.403  accumulated=0.403
		odSell.addOrder(0.034, 11);    //4	total=0.374  accumulated=1.166
	}
	
	@Before
	public void initFee () {
		fee = new ExchangeFee();
		fee.setRatioBuyFee(0.002);
		fee.setRatioSellFee(0.003);
	}
	
	
	@Test
	public void execWithCommissionBuyTest1 () {
		TradeResult tr = null;
		OrderDepth od = ob.getOrderDepth(TradeType.SELL); //buy order test against sell books

		//--- test order depth 1 ---
		double buyQuant=0.403d;
		double receiveQuant=12.974d;
		int expDepth = 1;
		
		applog.info("=== BUY: {}===>  Exepect Receive:{} at depth:{}",buyQuant, receiveQuant, expDepth );

		tr = TradeCalculator.execWithCommission(TradeType.BUY, buyQuant, ob, fee);
		assertTrue(tr.isOrderFillable());
		assertTrue(tr.getDepth() == expDepth);
		assertEquals(receiveQuant, tr.getToCoinTotal(), _DTOLERANCE);
		checkEachDepth(TradeType.BUY, tr.getTrades(), tr.getDepth(), od, receiveQuant);
	}
	
	@Test
	public void execWithCommissionBuyTest2 () {
		TradeResult tr = null;
		OrderDepth od = ob.getOrderDepth(TradeType.SELL); //buy order test against sell books
		
		//--- test order depth 2 ---
		double buyQuant=0.501d;
		double receiveQuant=16.0311875d;
		int expDepth = 2;
		
		applog.info("=== BUY: {}===>  Exepect Receive:{} at depth:{}",buyQuant, receiveQuant, expDepth );
		
		tr = TradeCalculator.execWithCommission(TradeType.BUY, buyQuant , ob, fee);
		assertTrue(tr.isOrderFillable());
		assertTrue(tr.getDepth() == expDepth);
		assertEquals(receiveQuant, tr.getToCoinTotal(), _DTOLERANCE);
		checkEachDepth(TradeType.BUY, tr.getTrades(), tr.getDepth(), od, receiveQuant);
	}
	
	@Test
	public void execWithCommissionBuyTest3 () {
		TradeResult tr = null;
		OrderDepth od = ob.getOrderDepth(TradeType.SELL); //buy order test against sell books
		
		//--- test order depth 3 ---
		double buyQuant=2.6763527054108216432865731462926d;
		double receiveQuant=79d;
		int expDepth = 5;
		
		applog.info("=== BUY: {}===>  Exepect Receive:{} at depth:{}",buyQuant, receiveQuant, expDepth );
		
		tr = TradeCalculator.execWithCommission(TradeType.BUY, buyQuant , ob, fee);
		assertTrue(tr.isOrderFillable());
		assertTrue(tr.getDepth() == expDepth);
		assertEquals(receiveQuant, tr.getToCoinTotal(), _DTOLERANCE);
		checkEachDepth(TradeType.BUY, tr.getTrades(), tr.getDepth(), od, receiveQuant);
	}
	
	
	@Test
	public void execWithCommissionBuyTest4 () {
		TradeResult tr = null;
		OrderDepth od = ob.getOrderDepth(TradeType.SELL); //buy order test against sell books
		
		//--- test order can't be filled ---
		double buyQuant=2.6773547094188376753507014028056d;
		double receiveQuant=80d;
		int expDepth = 5;
		
		applog.info("=== BUY: {}===>  Exepect Receive:{} at depth:{}",buyQuant, receiveQuant, expDepth );
		tr = TradeCalculator.execWithCommission(TradeType.BUY, buyQuant , ob, fee);
		applog.info("=== BUY: {}===>  Actual Receive:{} at depth:{}", buyQuant, tr.getToCoinTotal(),  tr.getDepth() );

		assertTrue(!tr.isOrderFillable());
		assertTrue(tr.getDepth() == expDepth);
		//assertEquals(receiveQuant, tr.getToCoinTotal(), _DTOLERANCE);
		//checkEachDepth(TradeType.BUY, tr.getTrades(), tr.getDepth(), od, receiveQuant);
	}
	
	
	@Test
	public void execWithCommissionBuyTest5 () {
		TradeResult tr = null;
		OrderDepth od = ob.getOrderDepth(TradeType.SELL); //buy order test against sell books

		//--- test order just have enough to buy at tier 1 ---
		double buyQuant=0.40380761523046;
		double receiveQuant=13;
		int expDepth = 1;
		boolean expFilled = true;

		applog.info("=== BUY: {}===>  Exepect Receive:{} at depth:{}, fillable:{}.",buyQuant, receiveQuant, expDepth, expFilled );
		tr = TradeCalculator.execWithCommission(TradeType.BUY,buyQuant, ob, fee);
		applog.info("=== BUY: {}===>  Actual Receive:{} at depth:{}, fillable:{}", buyQuant, tr.getToCoinTotal(),  tr.getDepth(), tr.isOrderFillable() );

		assertTrue(tr.isOrderFillable());
		assertTrue(tr.getDepth() == expDepth);
		assertEquals(receiveQuant, tr.getToCoinTotal(), _DTOLERANCE);
		checkEachDepth(TradeType.BUY, tr.getTrades(), tr.getDepth(), od, receiveQuant);
	}
	
	
	@Test
	public void execWithCommissionSellTest1 () {
		TradeResult tr = null;
		OrderDepth od = ob.getOrderDepth(TradeType.BUY); //sell order test against buy books

		//--- test order depth 1 ---
		double sellQuant=11d;
		double receiveQuant=0.318043d;
		int expDepth = 1;
		boolean expFilled = true;
		
		applog.info("=== SELL: {}===>  Exepect Receive:{} at depth:{}, fillable:{}.",sellQuant, receiveQuant, expDepth, expFilled );
		tr = TradeCalculator.execWithCommission(TradeType.SELL, sellQuant, ob, fee);
		applog.info("=== SELL: {}===>  Actual Receive:{} at depth:{}, fillable:{}.", sellQuant, tr.getToCoinTotal(),  tr.getDepth(), tr.isOrderFillable() );

		assertTrue(tr.isOrderFillable());
		assertTrue(tr.getDepth() == expDepth);
		assertEquals(receiveQuant, tr.getToCoinTotal(), _DTOLERANCE);
		checkEachDepth(TradeType.SELL, tr.getTrades(), tr.getDepth(), od, receiveQuant/(1-fee.getRatioSellFee()));
	}
	
	
	@Test
	public void execWithCommissionSellTest2 () {
		TradeResult tr = null;
		OrderDepth od = ob.getOrderDepth(TradeType.BUY); //sell order test against buy books

		//--- test order depth 4 ---
		double sellQuant=61d;
		double receiveQuant=1.641062d;
		int expDepth = 4;
		boolean expFilled = true;
		
		applog.info("=== SELL: {}===>  Exepect Receive:{} at depth:{}, fillable:{}.",sellQuant, receiveQuant, expDepth, expFilled );
		tr = TradeCalculator.execWithCommission(TradeType.SELL, sellQuant, ob, fee);
		applog.info("=== SELL: {}===>  Actual Receive:{} at depth:{}, fillable:{}.", sellQuant, tr.getToCoinTotal(),  tr.getDepth(), tr.isOrderFillable() );

		assertTrue(tr.isOrderFillable());
		assertTrue(tr.getDepth() == expDepth);
		assertEquals(receiveQuant, tr.getToCoinTotal(), _DTOLERANCE);
		checkEachDepth(TradeType.SELL, tr.getTrades(), tr.getDepth(), od, receiveQuant/(1-fee.getRatioSellFee()));
	}
	
	
	
	@Test
	public void execWithCommissionSellTest3 () {
		TradeResult tr = null;
		OrderDepth od = ob.getOrderDepth(TradeType.BUY); //sell order test against buy books

		//--- test order depth 5 ---
		double sellQuant=115d;
		double receiveQuant=2.988009d;
		int expDepth = 5;
		boolean expFilled = true;
		
		applog.info("=== SELL: {}===>  Exepect Receive:{} at depth:{}, fillable:{}.",sellQuant, receiveQuant, expDepth, expFilled );
		tr = TradeCalculator.execWithCommission(TradeType.SELL, sellQuant, ob, fee);
		applog.info("=== SELL: {}===>  Actual Receive:{} at depth:{}, fillable:{}.", sellQuant, tr.getToCoinTotal(),  tr.getDepth(), tr.isOrderFillable() );

		assertTrue(tr.isOrderFillable());
		assertTrue(tr.getDepth() == expDepth);
		assertEquals(receiveQuant, tr.getToCoinTotal(), _DTOLERANCE);
		checkEachDepth(TradeType.SELL, tr.getTrades(), tr.getDepth(), od, receiveQuant/(1-fee.getRatioSellFee()));
	}
	
		
	@Test
	public void execWithCommissionSellTest4 () {
		TradeResult tr = null;
		OrderDepth od = ob.getOrderDepth(TradeType.BUY); //sell order test against buy books

		//--- test order depth 5 ---
		double sellQuant=116d;
		double receiveQuant=2.988009d;
		int expDepth = 5;
		boolean expFilled = false;
		
		applog.info("=== SELL: {}===>  Exepect Receive:{} at depth:{}, fillable:{}.",sellQuant, receiveQuant, expDepth, expFilled );
		tr = TradeCalculator.execWithCommission(TradeType.SELL, sellQuant, ob, fee);
		applog.info("=== SELL: {}===>  Actual Receive:{} at depth:{}, fillable:{}.", sellQuant, tr.getToCoinTotal(),  tr.getDepth(), tr.isOrderFillable() );

		assertTrue(tr.isOrderFillable() == expFilled);
		//assertTrue(tr.getDepth() == expDepth);
		//assertEquals(receiveQuant, tr.getToCoinTotal(), _DTOLERANCE);
		//checkEachDepth(TradeType.SELL, tr.getTrades(), tr.getDepth(), od, receiveQuant/(1-fee.getRatioSellFee()));
	}
	
	
	
	
	private void checkEachDepth(TradeType ty, ArrayList<TradeElement> ords, int depth, OrderDepth od, double receiveQuant ) {
		assertTrue(depth == ords.size()); //--- oder should match exactly the depth
		
		int depp = depth -1; //check depth equal until the last one( last one won't match)
		for (int i=0; i< depth; i++) {
			TradeElement te = ords.get(i);
			OrderBookElm oe = od.getBookElementAt(i+1);

			double total = te.getQuantity();
			if (TradeType.SELL.equals(ty)) {
				total *= te.getPrice();
			}

			if (i<depp) {
				applog.info("\t===  each depth testing:[{}]===> expect(od){}/{} : actual(te){}/{}", i, oe.getQuantity(),oe.getPrice(), total, te.getPrice());
				assertEquals( te.getPrice() , oe.getPrice(), _DTOLERANCE);
				assertEquals(te.getQuantity(), oe.getQuantity(), _DTOLERANCE);
				receiveQuant -= total;
			}
			else { //--- last in loop
				applog.info("\t=== final depth testing:[{}]===> expect(od){}/{} : actual(te){}/{}", i, receiveQuant, oe.getPrice(), total, te.getPrice());
				assertTrue(te.getQuantity() <= oe.getQuantity());
				
				assertEquals(total, receiveQuant, _DTOLERANCE);
				assertEquals(te.getPrice(), oe.getPrice(), _DTOLERANCE);
			}
		}
	}
	
}
