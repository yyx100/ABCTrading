package com.agilebc.data.trade;

import com.agilebc.data.agilebcdata;
import com.agilebc.util.TradeDirection;

public class LinkedCoinElm  extends agilebcdata {
	private Coin rootCoin = null;
	private Coin currCoin = null;  // current coin
	
	private LinkedCoinElm prevCoinElm = null;
	private LinkedCoinElm nextCoinElm = null;
	private TradeDirection nextAction = null;
	
	/**@para chainlength is not calculate until the end of chain has been determined. */
	private int chainLength = -1; 
	private int seqId = 0;
	
	
	public LinkedCoinElm (Coin root, Coin currCoin) {
		this.rootCoin = root;
		this.currCoin = currCoin;
	}
	
	/**
	 *    this datastructure isn't a cicurlar structure.  what is method is detecting is merely if starting coin is same as the ending coin
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
	public LinkedCoinElm insertNextCoin (Coin newCoin, TradeDirection action) {
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

	public Coin getCurrCoin() {
		return currCoin;
	}

	public void setCurrCoin(Coin currCoin) {
		this.currCoin = currCoin;
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

	public TradeDirection getNextAction() {
		return nextAction;
	}

	public void setNextAction(TradeDirection nextAction) {
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

}
