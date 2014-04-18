package com.agilebc.data.trade;

import com.agilebc.data.AbstractAgilebcData;
import com.agilebc.util.TradeType;

public class LinkedCoinElm  extends AbstractAgilebcData {
	protected Coin rootCoin = null;
	protected Coin currCoin = null;  // current coin

	protected LinkedCoinElm prevCoinElm = null;
	protected LinkedCoinElm nextCoinElm = null;
	protected TradeType nextAction = null;
	
	/**@param chain length is not calculate until the end of chain has been determined, 
	 * then updated for all elements with the same chain length.  
	 * coin length includes all elements in the chain, including head and tails */
	private int chainLength = -1; 
	/**@param seqId is the sequence id of the element in a chain.  It starts at 0 */
	private int seqId = 0;
	private String chainStr = null;
	
	public LinkedCoinElm (Coin root, Coin currCoin) {
		this.rootCoin = root;
		this.currCoin = currCoin;
	}
	
	/**
	 *    this data structure isn't a circular structure.  what is method is detecting is merely if starting coin is same as the ending coin
	 * @return
	 */
	public boolean isFullCircled () {
		if (isTail()) {
			updateChainLength(seqId + 1);
			return true;
		}
		
		return false;
	}
	
	public boolean isTail () {
		if (nextAction == null && 
				seqId > 0 &&
				rootCoin.equals(currCoin)) {
			return true;
		}
		
		return false;
	}
	
	public boolean isHead () {
		if (prevCoinElm == null &&
				seqId == 0 &&
				rootCoin.equals(currCoin)) {
			return true;
		}
		
		return false;
	}
	
	
	private void updateChainLength (int chainlength) {
		this.chainLength = chainlength;
		
		//--- go through linked list util reach the head
		if (prevCoinElm != null) {
			prevCoinElm.updateChainLength(chainlength);
		}
	}
	
	
	/**
	 *     insert the next coin to the linked list.  this will internally call
	 *  setNextCoinElm, which will take care next element's sequence id and the 
	 *  reverse link back to this.
	 * @param newCoin
	 * @return
	 */
	public LinkedCoinElm insertNextCoin (Coin newCoin, TradeType action) {
		LinkedCoinElm next = this.cloneShallow();
		next.setCurrCoin(newCoin);
		this.setNextCoinElm(next);
		this.nextAction = action;
		
		return next;
	}
	
	
	/**
	 *   clone the entire chain from head to current location.  
	 *   
	 *   @return the full chain of cloned of linkedCoinElms from current link to its head.  
	 */
	public LinkedCoinElm cloneDeep () {
		LinkedCoinElm copyMe = cloneShallow();
		
		LinkedCoinElm pointer = copyMe;
		while (pointer.getPrevCoinElm() != null) {
			LinkedCoinElm copyOfCopyMePrev = pointer.getPrevCoinElm().cloneShallow();
			
			copyOfCopyMePrev.setNextCoinElm(pointer);
			pointer.setPrevCoinElm(copyOfCopyMePrev);
			
			pointer = copyOfCopyMePrev;
		}
		
		return copyMe;
	}
	
	
	/**
	 *    creates a new LinkedCoin of itself, however the linked reference references to the same object, not a true clone
	 * @return
	 */
	private LinkedCoinElm cloneShallow() {
		LinkedCoinElm newElm = new LinkedCoinElm(this.rootCoin, this.currCoin);
		newElm.setSeqId(this.seqId);
		newElm.setPrevCoinElm(this.prevCoinElm);
		
		newElm.setNextAction(this.nextAction); //this is usually null at time of clone.  
		newElm.setNextCoinElm(this.nextCoinElm); //this is usually null at time of clone.  
		
		return newElm;
	}
	
	
	/**
	 *   determines if this link is linked to an other link.
	 * @return
	 */
	public boolean hasNext() {
		return (nextCoinElm == null) ? false : true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public LinkedCoinElm getTail () {
		if (isTail()) {
			return this;
		}

		return nextCoinElm.getTail();
	}
	
	
	public LinkedCoinElm getHead () {
		if (isHead()) {
			return this;
		}
		
		return prevCoinElm.getHead();
	}
	

	public Coin getRootCoin() {
		return rootCoin;
	}

	public void setRootCoin(Coin rootCoin) {
		this.rootCoin = rootCoin;
	}



	public LinkedCoinElm getNextCoinElm() {
		return nextCoinElm;
	}

	public void setNextCoinElm(LinkedCoinElm nextCoinElm) {
		this.nextCoinElm = nextCoinElm;
		
		if (nextCoinElm != null) {
			nextCoinElm.setSeqId(this.seqId + 1);
			nextCoinElm.setPrevCoinElm(this);
		}
	}

	public TradeType getNextAction() {
		return nextAction;
	}

	public void setNextAction(TradeType nextAction) {
		this.nextAction = nextAction;
	}

	public int getSeqId() {
		return seqId;
	}

	private void setSeqId(int seqId) {
		this.seqId = seqId;
	}


	public LinkedCoinElm getPrevCoinElm() {
		return prevCoinElm;
	}


	public void setPrevCoinElm(LinkedCoinElm prevCoinElm) {
		this.prevCoinElm = prevCoinElm;
	}


	public int getChainLength() {
		return chainLength;
	}


	public void setChainLength(int chainLength) {
		this.chainLength = chainLength;
	}
	
	public Coin getCurrCoin() {
		return currCoin;
	}

	public void setCurrCoin(Coin currCoin) {
		this.currCoin = currCoin;
	}

	public String getChainStr() {
		return chainStr;
	}

	public void setChainStr(String chainStr) {
		this.chainStr = chainStr;
	}
}
