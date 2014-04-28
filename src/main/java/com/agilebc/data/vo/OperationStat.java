package com.agilebc.data.vo;

public class OperationStat {

	private ExecutionState execSt = null;
	
	
	public OperationStat () {
		this.execSt = ExecutionState.INIT;
	}
	
	
	public OperationStat (ExecutionState startState) {
		this.execSt = startState;
	}
	
	/**
	 *    set to specified execution state.  returns false if no change is detected
	 * @param execSt
	 * @return 
	 */
	public synchronized boolean changeExecutionState (ExecutionState execSt) {
		if (this.execSt.equals(execSt)){
			return false;
		}
		else {
			this.execSt = execSt;
		}
		
		return true;
	}
	
	
	public ExecutionState getExecutionState(){
		return this.execSt;
	}
	
	
	public synchronized boolean isRunning () {
		return ExecutionState.RUN.equals(execSt);
	}
	
	
	public synchronized void startRun () {
		changeExecutionState(ExecutionState.RUN);
	}
	
	
	public synchronized void stopRun() {
		changeExecutionState(ExecutionState.STOP);
	}

	public synchronized void waitRun() {
		changeExecutionState(ExecutionState.WAIT);
	}
	
}
