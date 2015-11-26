/*
 * 
 */
package com.rapidminer.gui;

import com.rapidminer.Process;
import com.rapidminer.gui.flow.ProcessPanel;

/**
 * This interface gives access to the process related methods.
 * 
 * @author Gabor Bakos
 */
public interface ProcessState {

	public void validateProcess(boolean force);

	public int getProcessState();

	public Process getProcess();

	/** Creates a new process. */
	public void newProcess();

	/**
	 * Runs or resumes the current process. If the process is started, checks for potential errors
	 * first and prevents execution unless the user has disabled the pre-run check.
	 */
	public void runProcess();

	/**
	 * Runs or resumes the current process.
	 *
	 * @param precheckBeforeExecution
	 *            if {@code true} and the process is started, checks for potential errors first and
	 *            prevents execution unless the user has disabled the pre-run check
	 */
	public void runProcess(boolean precheckBeforeExecution);

	/**
	 * Can be used to stop the currently running process. Please note that the
	 * ProcessThread will still be running in the background until the current
	 * operator is finished.
	 */
	public void stopProcess();

	public void pauseProcess();

	/**
	 * Sets a new process and registers the MainFrame's listeners.
	 */
	public void setProcess(Process process, boolean newProcess);

	/**
	 * Sets a new process and registers the MainFrame's listeners.
	 */
	public void setProcess(Process process, boolean newProcess, boolean addToUndoList);

	/** Returns true if the process has changed since the last save. */
	public boolean isChanged();

	public void undo();

	public void redo();

	/**
	 * Returns <code>true</code> if the current process has undo steps available.
	 * @return
	 */
	public boolean hasUndoSteps();

	/**
	 * Returns <code>true</code> if the current process has redo steps available.
	 * @return
	 */
	public boolean hasRedoSteps();

	public void setOpenedProcess(Process process, boolean showInfo,
			final String sourceName);

	//Removed in 6.x
//	public void saveAsTemplate();

	public void fireProcessUpdated();

	public void processHasBeenSaved();

	public ProcessPanel getProcessPanel();
}